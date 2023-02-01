package com.tokopedia.cart.view.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemShopBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.utils.resources.isDarkMode
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.text.NumberFormat
import java.util.*
import kotlin.math.min

class CartShopViewHolder(
    private val binding: ItemShopBinding,
    private val actionListener: ActionListener,
    private val cartItemAdapterListener: CartItemAdapter.ActionListener,
    private val compositeSubscription: CompositeSubscription
) : RecyclerView.ViewHolder(binding.root) {

    // variable to hold identifier
    private var cartString: String = ""

    private val localCacheHandler: LocalCacheHandler by lazy {
        LocalCacheHandler(itemView.context, KEY_ONBOARDING_ICON_PIN)
    }

    fun bindUpdatedWeight(cartShopHolderData: CartShopHolderData) {
        if (cartString != cartShopHolderData.cartString) {
            // workaround for different binding
            bindData(cartShopHolderData)
        }
        renderMaximumWeight(cartShopHolderData)
        cartShopHolderData.isNeedToRefreshWeight = false
        renderCartShopGroupTicker(cartShopHolderData)
    }

    fun bindData(cartShopHolderData: CartShopHolderData) {
        renderShopName(cartShopHolderData)
        renderShopBadge(cartShopHolderData)
        renderIconPin(cartShopHolderData)
        renderCartItems(cartShopHolderData)
        renderAccordion(cartShopHolderData)
        renderCheckBox(cartShopHolderData)
        renderFulfillment(cartShopHolderData)
        renderPreOrder(cartShopHolderData)
        renderIncidentLabel(cartShopHolderData)
        renderFreeShipping(cartShopHolderData)
        renderEstimatedTimeArrival(cartShopHolderData)
        renderMaximumWeight(cartShopHolderData)
        renderCartShopGroupTicker(cartShopHolderData)
        renderGiftingAddOn(cartShopHolderData)
        cartString = cartShopHolderData.cartString
    }

    private fun renderIconPin(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.isShowPin) {
            binding.iconPin.show()
            cartShopHolderData.pinCoachmarkMessage.let {
                if (it.isNotBlank()) {
                    showIconPinOnboarding(it)
                }
            }
        } else {
            binding.iconPin.gone()
        }
    }

    private fun showIconPinOnboarding(coachmarkMessage: String) {
        val hasShownOnboarding = localCacheHandler.getBoolean(KEY_HAS_SHOWN_ICON_PIN_ONBOARDING, false)
        if (hasShownOnboarding) return

        itemView.context.let {
            val onboardingItems = ArrayList<CoachMark2Item>().apply {
                add(CoachMark2Item(binding.iconPin, coachmarkMessage, ""))
            }

            CoachMark2(it).apply {
                showCoachMark(onboardingItems)
            }

            localCacheHandler.apply {
                putBoolean(KEY_HAS_SHOWN_ICON_PIN_ONBOARDING, true)
                applyEditor()
            }
        }
    }

    private fun renderCartItems(cartShopHolderData: CartShopHolderData) {
        if (!cartShopHolderData.isError && cartShopHolderData.isCollapsed) {
            renderCollapsedCartItems(cartShopHolderData)
        } else {
            renderExpandedCartItems(cartShopHolderData)
            if (cartShopHolderData.clickedCollapsedProductIndex != RecyclerView.NO_POSITION) {
                scrollToSelectedExpandedProduct(cartShopHolderData.clickedCollapsedProductIndex)
                cartShopHolderData.clickedCollapsedProductIndex = RecyclerView.NO_POSITION
            }
        }
    }

    private fun renderShopName(cartShopHolderData: CartShopHolderData) {
        val shopName = cartShopHolderData.shopName
        binding.tvShopName.text = Utils.getHtmlFormat(shopName)
        binding.tvShopName.setOnClickListener {
            actionListener.onCartShopNameClicked(
                cartShopHolderData.shopId,
                cartShopHolderData.shopName,
                cartShopHolderData.isTokoNow
            )
        }
    }

    private fun renderShopBadge(cartShopHolderData: CartShopHolderData) {
        val shopTypeInfoData = cartShopHolderData.shopTypeInfo
        if (shopTypeInfoData.shopBadge.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholder(binding.imageShopBadge, shopTypeInfoData.shopBadge)
            binding.imageShopBadge.contentDescription = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shopTypeInfoData.title.toLowerCase(Locale("id")))
            binding.imageShopBadge.show()
        } else {
            binding.imageShopBadge.gone()
        }
    }

    private fun renderExpandedCartItems(cartShopHolderData: CartShopHolderData) {
        val cartItemAdapter = CartItemAdapter(cartItemAdapterListener)
        cartItemAdapter.addDataList(cartShopHolderData.productUiModelList)
        val linearLayoutManager = LinearLayoutManager(binding.rvCartItem.context)
        binding.rvCartItem.layoutManager = linearLayoutManager
        binding.rvCartItem.adapter = cartItemAdapter
        (binding.rvCartItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        binding.rvCartItem.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        binding.rvCartItem.requestLayout()
    }

    private fun renderCollapsedCartItems(cartShopHolderData: CartShopHolderData) {
        // remove item with the same bundleGroupId or productId value
        val cartItemDataList = cartShopHolderData.productUiModelList.distinctBy {
            if (it.isBundlingItem) it.bundleGroupId else it.productId
        }
        val maxIndex = min(COLLAPSED_PRODUCTS_LIMIT, cartItemDataList.size)
        val cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList = cartItemDataList.subList(0, maxIndex)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvCartItem.layoutManager = layoutManager
        binding.rvCartItem.adapter = cartCartCollapsedProductAdapter

        setCollapsedRecyclerViewHeight(cartItemDataList)

        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        val paddingLeft = ITEM_DECORATION_PADDING_LEFT.dpToPx(itemView.resources.displayMetrics)
        val paddingRight = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        binding.rvCartItem.addItemDecoration(CartHorizontalItemDecoration(paddingLeft, paddingRight))
    }

    private fun setCollapsedRecyclerViewHeight(cartItemDataList: List<CartItemHolderData>) {
        var hasProductWithVariant = false
        if (cartItemDataList.isNotEmpty()) {
            val maxIndex = min(cartItemDataList.size, COLLAPSED_PRODUCTS_LIMIT)
            loop@ for (cartItemData in cartItemDataList.subList(0, maxIndex)) {
                if (cartItemData.variant.isNotBlank()) {
                    hasProductWithVariant = true
                    break@loop
                }
            }
            if (hasProductWithVariant) {
                binding.rvCartItem.layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.cart_collapsed_inner_recycler_view_height_with_variant)
            } else {
                binding.rvCartItem.layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.cart_collapsed_inner_recycler_view_height_without_variant)
            }
            binding.rvCartItem.requestLayout()
        }
    }

    private fun renderAccordion(cartShopHolderData: CartShopHolderData) {
        if (!cartShopHolderData.isError && cartShopHolderData.isCollapsible) {
            val showMoreWording = itemView.context.getString(R.string.label_tokonow_show_more)
            val showLessWording = itemView.context.getString(R.string.label_tokonow_show_less)
            if (cartShopHolderData.isCollapsed) {
                binding.imageChevron.rotation = CHEVRON_ROTATION_0
                binding.textAccordion.text = showMoreWording
            } else {
                binding.imageChevron.rotation = CHEVRON_ROTATION_180
                binding.textAccordion.text = showLessWording
            }

            binding.layoutAccordion.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (cartShopHolderData.isCollapsed) {
                        actionListener.onExpandAvailableItem(position)
                    } else {
                        actionListener.onCollapseAvailableItem(position)
                    }
                }
            }

            binding.layoutAccordion.show()
            binding.separatorAccordion.show()
        } else {
            binding.layoutAccordion.gone()
            binding.separatorAccordion.gone()
        }
    }

    private fun renderCheckBox(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            val padding10 = SHOP_HEADER_PADDING_10.dpToPx(itemView.resources.displayMetrics)
            val padding16 = itemView.resources.getDimensionPixelSize(R.dimen.dp_16)
            if (!cartShopHolderData.isError) {
                cbSelectShop.show()
                cbSelectShop.isEnabled = true
                cbSelectShop.isChecked = cartShopHolderData.isAllSelected
                cbSelectShop.skipAnimation()
                rlShopHeader.setPadding(padding10, padding16, padding10, padding10)
                initCheckboxWatcherDebouncer(cartShopHolderData, compositeSubscription)
            } else {
                cbSelectShop.gone()
                rlShopHeader.setPadding(padding16, padding16, padding10, padding10)
            }
        }
    }

    private fun initCheckboxWatcherDebouncer(cartShopHolderData: CartShopHolderData, compositeSubscription: CompositeSubscription) {
        binding.cbSelectShop.let {
            compositeSubscription.add(
                rxViewClickDebounce(it, CHECKBOX_WATCHER_DEBOUNCE_TIME).subscribe(object : Subscriber<Boolean>() {
                    override fun onNext(isChecked: Boolean) {
                        cbSelectShopClickListener(cartShopHolderData)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
            )
        }
    }

    private fun renderFulfillment(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.fulfillmentName.isNotBlank()) {
                if (cartShopHolderData.isFulfillment && cartShopHolderData.fulfillmentBadgeUrl.isNotEmpty()) {
                    iuImageFulfill.show()
                    iuImageFulfill.loadImageWithoutPlaceholder(cartShopHolderData.fulfillmentBadgeUrl)
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.show()
                tvFulfillDistrict.text = cartShopHolderData.fulfillmentName
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderEstimatedTimeArrival(cartShopHolderData: CartShopHolderData) {
        val eta = cartShopHolderData.estimatedTimeArrival
        with(binding) {
            if (eta.isNotBlank()) {
                textEstimatedTimeArrival.text = eta
                textEstimatedTimeArrival.show()
                separatorEstimatedTimeArrival.show()
            } else {
                textEstimatedTimeArrival.gone()
                separatorEstimatedTimeArrival.gone()
            }
        }
    }

    private fun cbSelectShopClickListener(cartShopHolderData: CartShopHolderData) {
        val isChecked: Boolean
        if (cartShopHolderData.isPartialSelected) {
            isChecked = true
            cartShopHolderData.isAllSelected = true
            cartShopHolderData.isPartialSelected = false
        } else {
            isChecked = !cartShopHolderData.isAllSelected
        }
        var isAllSelected = true
        cartShopHolderData.productUiModelList.forEach {
            if (it.isError && it.isSingleChild) {
                isAllSelected = false
                return@forEach
            }
        }
        cartShopHolderData.isAllSelected = isAllSelected
        if (adapterPosition != RecyclerView.NO_POSITION) {
            actionListener.onShopItemCheckChanged(adapterPosition, isChecked)
        }
    }

    private fun renderPreOrder(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.preOrderInfo.isNotBlank()) {
                labelPreOrder.text = cartShopHolderData.preOrderInfo
                labelPreOrder.show()
                separatorPreOrder.show()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
        }
    }

    private fun renderIncidentLabel(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.incidentInfo.isNotBlank()) {
                labelIncident.text = cartShopHolderData.incidentInfo
                labelIncident.show()
                separatorIncident.show()
            } else {
                labelIncident.gone()
                separatorIncident.gone()
            }
        }
    }

    private fun renderFreeShipping(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.freeShippingBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping,
                    cartShopHolderData.freeShippingBadgeUrl
                )
                val contentDescriptionStringResource = if (cartShopHolderData.isFreeShippingPlus) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                imgFreeShipping.contentDescription = itemView.context.getString(contentDescriptionStringResource)
                imgFreeShipping.show()
                separatorFreeShipping.show()
                if (!cartShopHolderData.hasSeenFreeShippingBadge && cartShopHolderData.isFreeShippingPlus) {
                    cartShopHolderData.hasSeenFreeShippingBadge = true
                    actionListener.onViewFreeShippingPlusBadge()
                }
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
        }
    }

    private fun renderMaximumWeight(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shouldValidateWeight) {
            val currentWeight = cartShopHolderData.totalWeight
            val maximumWeight = cartShopHolderData.maximumShippingWeight
            val extraWeight = (currentWeight - maximumWeight) / 1000
            val descriptionText = cartShopHolderData.maximumWeightWording
            if (extraWeight > 0 && descriptionText.isNotEmpty()) {
                with(binding) {
                    tickerWarning.tickerTitle = null
                    tickerWarning.setTextDescription(descriptionText.replace(CartShopHolderData.MAXIMUM_WEIGHT_WORDING_REPLACE_KEY, NumberFormat.getNumberInstance(Locale("in", "id")).format(extraWeight)))
                    tickerWarning.tickerType = TYPE_WARNING
                    tickerWarning.tickerShape = SHAPE_LOOSE
                    tickerWarning.closeButtonVisibility = View.GONE
                    tickerWarning.show()
                    tickerWarning.post {
                        binding.tickerWarning.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        )
                        binding.tickerWarning.requestLayout()
                    }
                }
            } else {
                with(binding) {
                    tickerWarning.gone()
                }
            }
        } else {
            with(binding) {
                tickerWarning.gone()
            }
        }
    }

    private fun renderGiftingAddOn(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.addOnText.isNotEmpty()) {
            binding.giftingWidgetLayout.root.visible()
            binding.giftingWidgetLayout.descGifting.text = cartShopHolderData.addOnText
            ImageHandler.loadImageWithoutPlaceholder(binding.giftingWidgetLayout.ivAddonLeft, cartShopHolderData.addOnImgUrl)
            binding.giftingWidgetLayout.root.setOnClickListener {
                actionListener.onClickAddOnCart(cartShopHolderData.productUiModelList.firstOrNull()?.productId ?: "", cartShopHolderData.addOnId)
            }
            actionListener.addOnImpression(cartShopHolderData.productUiModelList.firstOrNull()?.productId ?: "")
        } else {
            binding.giftingWidgetLayout.root.gone()
        }
    }

    private fun scrollToSelectedExpandedProduct(productIndex: Int) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            binding.rvCartItem.post {
                if (binding.tickerWarning.isVisible) {
                    binding.tickerWarning.post {
                        val paddingOffset = itemView.context?.resources?.getDimensionPixelSize(R.dimen.dp_16)
                            ?: 0
                        val tickerHeight = binding.tickerWarning.height
                        val totalOffset = calculateScrollOffset(productIndex, tickerHeight + paddingOffset)
                        actionListener.scrollToClickedExpandedProduct(position, totalOffset * -1)
                    }
                } else {
                    val totalOffset = calculateScrollOffset(productIndex, 0)
                    actionListener.scrollToClickedExpandedProduct(position, totalOffset * -1)
                }
            }
        }
    }

    private fun calculateScrollOffset(productIndex: Int, tickerHeight: Int): Int {
        val child: View? = binding.rvCartItem.getChildAt(0)
        val productHeight = child?.height ?: 0
        val offset = productIndex * productHeight
        val paddingOffset = SCROLL_PADDING_OFFSET.dpToPx(itemView.resources.displayMetrics)
        return offset + paddingOffset + tickerHeight
    }

    private fun renderCartShopGroupTicker(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.hasSelectedProduct && !cartShopHolderData.isError &&
            cartShopHolderData.cartShopGroupTicker.enableTicker &&
            !cartShopHolderData.isOverweight
        ) {
            binding.apply {
                val cartShopGroupTicker = cartShopHolderData.cartShopGroupTicker
                when (cartShopGroupTicker.state) {
                    CartShopGroupTickerState.FIRST_LOAD, CartShopGroupTickerState.LOADING -> {
                        cartShopTickerText.gone()
                        cartShopTickerLeftIcon.gone()
                        cartShopTickerRightIcon.gone()
                        cartShopTickerLargeLoader.type = LoaderUnify.TYPE_LINE
                        cartShopTickerLargeLoader.show()
                        cartShopTickerSmallLoader.type = LoaderUnify.TYPE_LINE
                        cartShopTickerSmallLoader.show()
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_BN50))
                        layoutCartShopTicker.setOnClickListener(null)
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.SUCCESS_AFFORD, CartShopGroupTickerState.SUCCESS_NOT_AFFORD -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        cartShopTickerText.text = MethodChecker.fromHtml(cartShopGroupTicker.tickerText)
                        cartShopTickerText.show()
                        if (cartShopGroupTicker.leftIcon.isNotBlank() && cartShopGroupTicker.leftIconDark.isNotBlank()) {
                            if (root.context.isDarkMode()) {
                                cartShopTickerLeftIcon.setImageUrl(cartShopGroupTicker.leftIconDark)
                            } else {
                                cartShopTickerLeftIcon.setImageUrl(cartShopGroupTicker.leftIcon)
                            }
                            cartShopTickerLeftIcon.show()
                        } else {
                            cartShopTickerLeftIcon.gone()
                        }
                        if (cartShopGroupTicker.rightIcon.isNotBlank() && cartShopGroupTicker.rightIconDark.isNotBlank()) {
                            if (root.context.isDarkMode()) {
                                cartShopTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIconDark)
                            } else {
                                cartShopTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIcon)
                            }
                            cartShopTickerRightIcon.show()
                        } else {
                            cartShopTickerRightIcon.gone()
                        }
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_BN50))
                        layoutCartShopTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerClicked(cartShopHolderData)
                        }
                        if (!cartShopHolderData.cartShopGroupTicker.hasSeenTicker) {
                            actionListener.onViewCartShopGroupTicker(cartShopHolderData)
                            cartShopHolderData.cartShopGroupTicker.hasSeenTicker = true
                        }
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.FAILED -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        cartShopTickerText.text = MethodChecker.fromHtml(cartShopGroupTicker.errorText)
                        cartShopTickerText.show()
                        cartShopTickerLeftIcon.gone()
                        val iconColor = MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                        val reloadIcon = getIconUnifyDrawable(root.context, IconUnify.RELOAD, iconColor)
                        cartShopTickerRightIcon.setImageDrawable(reloadIcon)
                        cartShopTickerRightIcon.show()
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_RN50))
                        layoutCartShopTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerRefreshClicked(adapterPosition, cartShopHolderData)
                        }
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.EMPTY -> {
                        layoutCartShopTicker.gone()
                    }
                }
                if (cartShopGroupTicker.state == CartShopGroupTickerState.FIRST_LOAD) {
                    actionListener.checkCartShopGroupTicker(cartShopHolderData)
                }
            }
        } else {
            binding.layoutCartShopTicker.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shop

        private const val COLLAPSED_PRODUCTS_LIMIT = 10

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
        const val KEY_ONBOARDING_ICON_PIN = "KEY_ONBOARDING_ICON_PIN"
        const val KEY_HAS_SHOWN_ICON_PIN_ONBOARDING = "KEY_HAS_SHOWN_ICON_PIN_ONBOARDING"

        private const val ITEM_DECORATION_PADDING_LEFT = 48
        private const val SHOP_HEADER_PADDING_10 = 10
        private const val SCROLL_PADDING_OFFSET = 12

        private const val CHEVRON_ROTATION_0 = 0f
        private const val CHEVRON_ROTATION_180 = 180f
    }
}
