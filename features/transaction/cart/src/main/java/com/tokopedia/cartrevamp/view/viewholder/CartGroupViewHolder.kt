package com.tokopedia.cartrevamp.view.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemGroupRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cartrevamp.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CartGroupViewHolder(
    private val binding: ItemGroupRevampBinding,
    private val actionListener: ActionListener,
    private val compositeSubscription: CompositeSubscription,
    private var plusCoachmark: CoachMark2?
) : RecyclerView.ViewHolder(binding.root) {

    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }

    fun bindData(cartGroupHolderData: CartGroupHolderData) {
        renderDivider(cartGroupHolderData)
        renderGroupName(cartGroupHolderData)
        renderGroupBadge(cartGroupHolderData)
        renderCartItems(cartGroupHolderData)
        renderCheckBox(cartGroupHolderData)
        renderFulfillment(cartGroupHolderData)
        renderFreeShipping(cartGroupHolderData)
        renderMaximumWeight(cartGroupHolderData)
        renderCartShopGroupTicker(cartGroupHolderData)
        validateGroupConstraintLayout(cartGroupHolderData)
        validateProductPoliciesMargin(cartGroupHolderData)
    }

    private fun renderDivider(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.isError || (cartGroupHolderData.isFirstItem && !cartGroupHolderData.isPreviousHasSelectedAmountWidget)) {
            binding.headerDivider.gone()
        } else {
            binding.headerDivider.visible()
            val dividerHeight = (
                if (cartGroupHolderData.isPreviousHasSelectedAmountWidget) {
                    SELECTED_AMOUNT_DIVIDER_HEIGHT
                } else {
                    AVAILABLE_DIVIDER_HEIGHT
                }
                ).dpToPx(itemView.resources.displayMetrics)
            binding.headerDivider.layoutParams.height = dividerHeight
        }
    }

    private fun renderCartItems(cartGroupHolderData: CartGroupHolderData) {
        if (!cartGroupHolderData.isError && cartGroupHolderData.isCollapsed) {
            renderCollapsedCartItems(cartGroupHolderData)
            binding.rvCartItem.visible()
        } else {
            binding.rvCartItem.gone()
        }
    }

    private fun renderGroupName(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.isError) {
            binding.tvShopName.apply {
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_NN600
                    )
                )
                weightType = Typography.REGULAR
            }
        } else {
            binding.tvShopName.apply {
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_NN950
                    )
                )
                weightType = Typography.BOLD
            }
        }

        binding.tvShopName.text = Utils.getHtmlFormat(cartGroupHolderData.groupName)
        if (cartGroupHolderData.isError) {
            val shopId = cartGroupHolderData.productUiModelList.getOrNull(0)?.shopHolderData?.shopId
            val shopName =
                cartGroupHolderData.productUiModelList.getOrNull(0)?.shopHolderData?.shopName
            binding.tvShopName.setOnClickListener {
                actionListener.onCartShopNameClicked(
                    shopId,
                    shopName,
                    cartGroupHolderData.isTokoNow
                )
            }
        } else if (cartGroupHolderData.groupAppLink.isNotEmpty()) {
            binding.tvShopName.setOnClickListener {
                actionListener.onCartGroupNameClicked(
                    cartGroupHolderData.groupAppLink
                )
            }
        } else {
            binding.tvShopName.setOnClickListener(null)
        }
    }

    private fun renderGroupBadge(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.groupBadge.isNotBlank()) {
            binding.imageShopBadge.loadImageWithoutPlaceholder(cartGroupHolderData.groupBadge)
            val contentDescription =
                if (cartGroupHolderData.isTypeOWOC()) {
                    cartGroupHolderData.groupName
                } else {
                    cartGroupHolderData.productUiModelList.getOrNull(
                        0
                    )?.shopHolderData?.shopTypeInfo?.title
                }
            binding.imageShopBadge.contentDescription = itemView.context.getString(
                purchase_platformcommonR.string.pp_cd_image_shop_badge_with_shop_type,
                contentDescription
            )
            binding.imageShopBadge.show()
        } else {
            binding.imageShopBadge.gone()
        }
    }

    private fun renderCollapsedCartItems(cartGroupHolderData: CartGroupHolderData) {
        // remove item with the same bundleGroupId or productId value
        val cartItemDataList = cartGroupHolderData.productUiModelList.distinctBy {
            if (it.isBundlingItem) it.bundleGroupId else it.productId
        }
        val maxIndex = min(COLLAPSED_PRODUCTS_LIMIT, cartItemDataList.size)
        val cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList =
            cartItemDataList.subList(0, maxIndex)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvCartItem.layoutManager = layoutManager
        binding.rvCartItem.adapter = cartCartCollapsedProductAdapter

        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        val paddingLeft = ITEM_DECORATION_PADDING_LEFT.dpToPx(itemView.resources.displayMetrics)
        val paddingRight = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        binding.rvCartItem.addItemDecoration(
            CartHorizontalItemDecoration(
                paddingLeft,
                paddingRight
            )
        )
    }

    private fun renderCheckBox(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            val padding12 = SHOP_HEADER_PADDING_12.dpToPx(itemView.resources.displayMetrics)
            val padding16 = itemView.resources.getDimensionPixelSize(R.dimen.dp_16)
            if (!cartGroupHolderData.isError) {
                cbSelectShop.show()
//                cbSelectShop.isEnabled = true
                cbSelectShop.isChecked = cartGroupHolderData.isAllSelected
                cbSelectShop.skipAnimation()
                clShopHeader.setPadding(padding16, padding16, padding16, padding12)
                initCheckboxWatcherDebouncer(cartGroupHolderData, compositeSubscription)
            } else {
                cbSelectShop.gone()
                clShopHeader.setPadding(padding16, padding16, padding12, padding12)
            }
        }
    }

    private fun initCheckboxWatcherDebouncer(
        cartGroupHolderData: CartGroupHolderData,
        compositeSubscription: CompositeSubscription
    ) {
        binding.cbSelectShop.let {
            compositeSubscription.add(
                rxViewClickDebounce(it, CHECKBOX_WATCHER_DEBOUNCE_TIME).subscribe(object :
                        Subscriber<Boolean>() {
                        override fun onNext(isChecked: Boolean) {
                            cbSelectShopClickListener(cartGroupHolderData)
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                        }
                    })
            )
        }
    }

    private fun renderFulfillment(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            if (cartGroupHolderData.fulfillmentName.isNotBlank()) {
                if (cartGroupHolderData.fulfillmentBadgeUrl.isNotEmpty()) {
                    iuImageFulfill.show()
                    iuImageFulfill.loadImageWithoutPlaceholder(cartGroupHolderData.fulfillmentBadgeUrl)
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.show()
                tvFulfillDistrict.text = cartGroupHolderData.fulfillmentName
                rlProductPoliciesLayout.show()
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
                rlProductPoliciesLayout.gone()
            }
        }
    }

    private fun validateGroupConstraintLayout(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(clShopHeader)

            if (cartGroupHolderData.groupBadge.isNotBlank()) {
                if (cartGroupHolderData.isError) {
                    constraintSet.connect(
                        R.id.image_shop_badge,
                        ConstraintSet.START,
                        R.id.cb_select_shop,
                        ConstraintSet.END,
                        0
                    )
                }
                else {
                    constraintSet.connect(
                        R.id.image_shop_badge,
                        ConstraintSet.START,
                        R.id.cb_select_shop,
                        ConstraintSet.END,
                        GROUP_DEFAULT_MARGIN.dpToPx(itemView.resources.displayMetrics)
                    )
                }
                constraintSet.connect(
                    R.id.tv_shop_name,
                    ConstraintSet.START,
                    R.id.image_shop_badge,
                    ConstraintSet.END,
                    4
                )
            } else {
                if (cartGroupHolderData.isError) {
                    constraintSet.connect(
                        R.id.tv_shop_name,
                        ConstraintSet.START,
                        R.id.cb_select_shop,
                        ConstraintSet.END,
                        0
                    )
                }
                else {
                    constraintSet.connect(
                        R.id.tv_shop_name,
                        ConstraintSet.START,
                        R.id.cb_select_shop,
                        ConstraintSet.END,
                        GROUP_DEFAULT_MARGIN.dpToPx(itemView.resources.displayMetrics)
                    )
                }
            }

            if (cartGroupHolderData.fulfillmentName.isNotBlank() || (cartGroupHolderData.shouldValidateWeight && cartGroupHolderData.extraWeight > 0)) {
                constraintSet.apply {
                    clear(R.id.image_shop_badge, ConstraintSet.BOTTOM)
                }
            } else {
                constraintSet.apply {
                    connect(
                        R.id.image_shop_badge,
                        ConstraintSet.BOTTOM,
                        R.id.cb_select_shop,
                        ConstraintSet.BOTTOM
                    )
                }
            }
            constraintSet.applyTo(clShopHeader)
        }
    }

    private fun validateProductPoliciesMargin(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.fulfillmentName.isNotBlank() && cartGroupHolderData.fulfillmentBadgeUrl.isNotEmpty()) {
            val iuImageFulfillLayoutParams = binding.iuImageFulfill.layoutParams as MarginLayoutParams
            if (cartGroupHolderData.isError) {
                iuImageFulfillLayoutParams.marginStart = 0
            }
            else {
                iuImageFulfillLayoutParams.marginStart = GROUP_DEFAULT_MARGIN.dpToPx(itemView.resources.displayMetrics)
            }
        }
        else if (cartGroupHolderData.fulfillmentName.isNotBlank()) {
            val tvFulfillLayoutParams = binding.tvFulfillDistrict.layoutParams as MarginLayoutParams
            if (cartGroupHolderData.isError) {
                tvFulfillLayoutParams.marginStart = 0
            }
            else {
                tvFulfillLayoutParams.marginStart = GROUP_DEFAULT_MARGIN.dpToPx(itemView.resources.displayMetrics)
            }
        }
    }

    private fun cbSelectShopClickListener(cartGroupHolderData: CartGroupHolderData) {
        val isChecked: Boolean
        if (cartGroupHolderData.isPartialSelected) {
            isChecked = true
            cartGroupHolderData.isAllSelected = true
            cartGroupHolderData.isPartialSelected = false
        } else {
            isChecked = !cartGroupHolderData.isAllSelected
        }
        var isAllSelected = true
        cartGroupHolderData.productUiModelList.forEach {
            if (it.isError && it.isSingleChild) {
                isAllSelected = false
                return@forEach
            }
        }
        cartGroupHolderData.isAllSelected = isAllSelected
        if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
            actionListener.onShopItemCheckChanged(absoluteAdapterPosition, isChecked)
        }
    }

    private fun renderFreeShipping(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            if (cartGroupHolderData.freeShippingBadgeUrl.isNotBlank()) {
                imgFreeShipping.loadImageWithoutPlaceholder(cartGroupHolderData.freeShippingBadgeUrl)
                val contentDescriptionStringResource = if (cartGroupHolderData.isFreeShippingPlus) {
                    purchase_platformcommonR.string.pp_cd_image_badge_plus
                } else {
                    purchase_platformcommonR.string.pp_cd_image_badge_bo
                }
                imgFreeShipping.contentDescription =
                    itemView.context.getString(contentDescriptionStringResource)
                imgFreeShipping.show()
//                separatorFreeShipping.show()
                if (!cartGroupHolderData.hasSeenFreeShippingBadge && cartGroupHolderData.isFreeShippingPlus) {
                    cartGroupHolderData.hasSeenFreeShippingBadge = true
                    actionListener.onViewFreeShippingPlusBadge()
                }
                if (cartGroupHolderData.coachmarkPlus.isShown && !plusCoachmarkPrefs.getPlusCoachMarkHasShown()) {
                    val coachMarkItem = ArrayList<CoachMark2Item>()
                    coachMarkItem.add(
                        CoachMark2Item(
                            imgFreeShipping,
                            cartGroupHolderData.coachmarkPlus.title,
                            cartGroupHolderData.coachmarkPlus.content,
                            CoachMark2.POSITION_BOTTOM
                        )
                    )
                    plusCoachmark?.showCoachMark(coachMarkItem)
                    plusCoachmarkPrefs.setPlusCoachmarkHasShown(true)
                }
            } else {
                imgFreeShipping.gone()
//                separatorFreeShipping.gone()
            }
        }
    }

    private fun renderMaximumWeight(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.shouldValidateWeight) {
            val extraWeight = cartGroupHolderData.extraWeight
            if (extraWeight > 0) {
                binding.rlProductPoliciesLayout.show()
                setOverweightText(extraWeight)
                binding.tvOverweight.show()
            } else {
                binding.tvOverweight.gone()
            }
        } else {
            binding.tvOverweight.gone()
        }
    }

    private fun setOverweightText(extraWeight: Double) {
        val text = itemView.resources.getString(R.string.cart_text_overweight).replace(
            "%s",
            NumberFormat.getNumberInstance(
                Locale("in", "id")
            ).format(extraWeight)
        )
        val spannableString = SpannableString(text)
        val firstSentenceIndex = text.indexOf(".")
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_YN500
                )
            ),
            0,
            firstSentenceIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN600
                )
            ),
            firstSentenceIndex + 2,
            text.lastIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvOverweight.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun renderCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        binding.itemCartBasketBuilding.vBmgmProductSeparator.gone()
        if (cartGroupHolderData.hasSelectedProduct && !cartGroupHolderData.isError &&
            cartGroupHolderData.cartShopGroupTicker.enableCartAggregator &&
            !cartGroupHolderData.isOverweight
        ) {
            binding.itemCartBasketBuilding.apply {
                val cartShopGroupTicker = cartGroupHolderData.cartShopGroupTicker
                when (cartShopGroupTicker.state) {
                    CartShopGroupTickerState.LOADING -> {
                        tvBmgmTicker.gone()
                        icBmgmTicker.gone()
                        iuTickerRightIcon.gone()
                        cartShopTickerLargeLoader.type =
                            LoaderUnify.TYPE_LINE
                        cartShopTickerLargeLoader.show()
                        cartShopTickerSmallLoader.type =
                            LoaderUnify.TYPE_RECT
                        cartShopTickerSmallLoader.show()
                        ivTickerBg.setImageResource(R.drawable.bg_cart_basket_building_loading_ticker)
                        layoutBasketBuildingTicker.setOnClickListener(null)
                        layoutBasketBuildingTicker.show()
                    }

                    CartShopGroupTickerState.SUCCESS_AFFORD, CartShopGroupTickerState.SUCCESS_NOT_AFFORD -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        tvBmgmTicker.text = MethodChecker.fromHtml(cartShopGroupTicker.tickerText)
                        tvBmgmTicker.show()
                        if (cartShopGroupTicker.leftIcon.isNotBlank() && cartShopGroupTicker.leftIconDark.isNotBlank()) {
                            if (root.context.isDarkMode()) {
                                icBmgmTicker.setImageUrl(cartShopGroupTicker.leftIconDark)
                            } else {
                                icBmgmTicker.setImageUrl(cartShopGroupTicker.leftIcon)
                            }
                            icBmgmTicker.show()
                        } else {
                            icBmgmTicker.gone()
                        }
                        if (cartShopGroupTicker.rightIcon.isNotBlank() && cartShopGroupTicker.rightIconDark.isNotBlank()) {
                            // TODO: handle Right Icon
//                            if (root.context.isDarkMode()) {
//                                iuTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIconDark)
//                            } else {
//                                iuTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIcon)
//                            }
                            val color = ContextCompat.getColor(
                                itemView.context,
                                unifyprinciplesR.color.Unify_NN500
                            )
                            iuTickerRightIcon.setImage(
                                IconUnify.CHEVRON_RIGHT,
                                color,
                                null,
                                color,
                                null
                            )
                            iuTickerRightIcon.show()
                        } else {
                            iuTickerRightIcon.gone()
                        }
                        ivTickerBg.setImageResource(R.drawable.bg_cart_bmgm)
                        layoutBasketBuildingTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerClicked(cartGroupHolderData)
                        }
                        if (!cartGroupHolderData.cartShopGroupTicker.hasSeenTicker) {
                            actionListener.onViewCartShopGroupTicker(cartGroupHolderData)
                            cartGroupHolderData.cartShopGroupTicker.hasSeenTicker = true
                        }
                        layoutBasketBuildingTicker.show()
                    }

                    CartShopGroupTickerState.FAILED -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        tvBmgmTicker.text = MethodChecker.fromHtml(cartShopGroupTicker.errorText)
                        tvBmgmTicker.show()
                        icBmgmTicker.gone()
                        val iconColor = MethodChecker.getColor(
                            root.context,
                            unifyprinciplesR.color.Unify_NN900
                        )
                        val reloadIcon =
                            getIconUnifyDrawable(root.context, IconUnify.RELOAD, iconColor)
                        iuTickerRightIcon.setImageDrawable(reloadIcon)
                        iuTickerRightIcon.show()
                        ivTickerBg.setImageResource(R.drawable.bg_cart_basket_building_error_ticker)
                        layoutBasketBuildingTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerRefreshClicked(
                                absoluteAdapterPosition,
                                cartGroupHolderData
                            )
                        }
                        layoutBasketBuildingTicker.show()
                    }

                    CartShopGroupTickerState.FIRST_LOAD, CartShopGroupTickerState.EMPTY -> {
                        layoutBasketBuildingTicker.gone()
                    }
                }
                if (cartShopGroupTicker.state == CartShopGroupTickerState.FIRST_LOAD) {
                    actionListener.checkCartShopGroupTicker(cartGroupHolderData)
                }
            }
        } else {
            cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.FIRST_LOAD
            binding.itemCartBasketBuilding.layoutBasketBuildingTicker.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_group_revamp

        private const val COLLAPSED_PRODUCTS_LIMIT = 10

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L

        private const val SELECTED_AMOUNT_DIVIDER_HEIGHT = 1
        private const val AVAILABLE_DIVIDER_HEIGHT = 8

        private const val GROUP_DEFAULT_MARGIN = 4

        private const val ITEM_DECORATION_PADDING_LEFT = 50
        private const val SHOP_HEADER_PADDING_12 = 12
    }
}
