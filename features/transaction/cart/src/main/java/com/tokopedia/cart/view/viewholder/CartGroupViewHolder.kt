package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemGroupBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.text.NumberFormat
import java.util.*
import kotlin.math.min

class CartGroupViewHolder(
    private val binding: ItemGroupBinding,
    private val actionListener: ActionListener,
    private val compositeSubscription: CompositeSubscription,
    private var plusCoachmark: CoachMark2?
) : RecyclerView.ViewHolder(binding.root) {

    private val localCacheHandler: LocalCacheHandler by lazy {
        LocalCacheHandler(itemView.context, KEY_ONBOARDING_ICON_PIN)
    }

    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }

    fun bindData(cartGroupHolderData: CartGroupHolderData) {
        renderGroupName(cartGroupHolderData)
        renderGroupBadge(cartGroupHolderData)
        renderIconPin(cartGroupHolderData)
        renderShopEnabler(cartGroupHolderData)
        renderCartItems(cartGroupHolderData)
        renderCheckBox(cartGroupHolderData)
        renderFulfillment(cartGroupHolderData)
        renderPreOrder(cartGroupHolderData)
        renderIncidentLabel(cartGroupHolderData)
        renderFreeShipping(cartGroupHolderData)
        renderEstimatedTimeArrival(cartGroupHolderData)
        renderMaximumWeight(cartGroupHolderData)
        renderAddOnInfo(cartGroupHolderData)
    }

    private fun renderIconPin(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.isShowPin) {
            binding.iconPin.show()
            cartGroupHolderData.pinCoachmarkMessage.let {
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

    private fun renderShopEnabler(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.enablerLabel.isNotBlank()) {
            binding.labelEpharmacy.text = cartGroupHolderData.enablerLabel
            binding.labelEpharmacy.visible()
        } else {
            binding.labelEpharmacy.gone()
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
        binding.tvShopName.text = Utils.getHtmlFormat(cartGroupHolderData.groupName)
        if (cartGroupHolderData.isError) {
            val shopId = cartGroupHolderData.productUiModelList.getOrNull(0)?.shopHolderData?.shopId
            val shopName = cartGroupHolderData.productUiModelList.getOrNull(0)?.shopHolderData?.shopName
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
            ImageHandler.loadImageWithoutPlaceholder(binding.imageShopBadge, cartGroupHolderData.groupBadge)
            val contentDescription = if (cartGroupHolderData.isTypeOWOC()) cartGroupHolderData.groupName else cartGroupHolderData.productUiModelList.getOrNull(0)?.shopHolderData?.shopTypeInfo?.title
            binding.imageShopBadge.contentDescription = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, contentDescription)
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

    private fun renderCheckBox(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            val padding10 = SHOP_HEADER_PADDING_10.dpToPx(itemView.resources.displayMetrics)
            val padding16 = itemView.resources.getDimensionPixelSize(R.dimen.dp_16)
            if (!cartGroupHolderData.isError) {
                cbSelectShop.show()
//                cbSelectShop.isEnabled = true
                cbSelectShop.isChecked = cartGroupHolderData.isAllSelected
                cbSelectShop.skipAnimation()
                rlShopHeader.setPadding(padding10, padding16, padding10, padding10)
                initCheckboxWatcherDebouncer(cartGroupHolderData, compositeSubscription)
            } else {
                cbSelectShop.gone()
                rlShopHeader.setPadding(padding16, padding16, padding10, padding10)
            }
        }
    }

    private fun initCheckboxWatcherDebouncer(cartGroupHolderData: CartGroupHolderData, compositeSubscription: CompositeSubscription) {
        binding.cbSelectShop.let {
            compositeSubscription.add(
                rxViewClickDebounce(it, CHECKBOX_WATCHER_DEBOUNCE_TIME).subscribe(object : Subscriber<Boolean>() {
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
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderEstimatedTimeArrival(cartGroupHolderData: CartGroupHolderData) {
        val eta = cartGroupHolderData.estimatedTimeArrival
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
        if (adapterPosition != RecyclerView.NO_POSITION) {
            actionListener.onShopItemCheckChanged(adapterPosition, isChecked)
        }
    }

    private fun renderPreOrder(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            if (cartGroupHolderData.preOrderInfo.isNotBlank()) {
                labelPreOrder.text = cartGroupHolderData.preOrderInfo
                labelPreOrder.show()
                separatorPreOrder.show()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
        }
    }

    private fun renderIncidentLabel(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            if (cartGroupHolderData.incidentInfo.isNotBlank()) {
                labelIncident.text = cartGroupHolderData.incidentInfo
                labelIncident.show()
                separatorIncident.show()
            } else {
                labelIncident.gone()
                separatorIncident.gone()
            }
        }
    }

    private fun renderFreeShipping(cartGroupHolderData: CartGroupHolderData) {
        with(binding) {
            if (cartGroupHolderData.freeShippingBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping,
                    cartGroupHolderData.freeShippingBadgeUrl
                )
                val contentDescriptionStringResource = if (cartGroupHolderData.isFreeShippingPlus) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                imgFreeShipping.contentDescription = itemView.context.getString(contentDescriptionStringResource)
                imgFreeShipping.show()
                separatorFreeShipping.show()
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
                separatorFreeShipping.gone()
            }
        }
    }

    private fun renderMaximumWeight(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.shouldValidateWeight) {
            val currentWeight = cartGroupHolderData.totalWeight
            val maximumWeight = cartGroupHolderData.maximumShippingWeight
            val extraWeight = (currentWeight - maximumWeight) / 1000
            val descriptionText = cartGroupHolderData.maximumWeightWording
            if (extraWeight > 0 && descriptionText.isNotEmpty()) {
                with(binding) {
                    tickerWarning.tickerTitle = null
                    tickerWarning.setTextDescription(descriptionText.replace(CartGroupHolderData.MAXIMUM_WEIGHT_WORDING_REPLACE_KEY, NumberFormat.getNumberInstance(Locale("in", "id")).format(extraWeight)))
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

    private fun renderAddOnInfo(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.addOnText.isNotEmpty()) {
            binding.addonInfoWidgetLayout.root.visible()
            binding.addonInfoWidgetLayout.descAddonInfo.text = cartGroupHolderData.addOnText
            binding.addonInfoWidgetLayout.ivAddonLeft.loadImage(cartGroupHolderData.addOnImgUrl)
            binding.addonInfoWidgetLayout.root.setOnClickListener {
                if (cartGroupHolderData.addOnType == CartGroupHolderData.ADD_ON_GIFTING) {
                    actionListener.onClickAddOnCart(
                        cartGroupHolderData.productUiModelList.firstOrNull()?.productId ?: "",
                        cartGroupHolderData.addOnId
                    )
                } else if (cartGroupHolderData.addOnType == CartGroupHolderData.ADD_ON_EPHARMACY) {
                    actionListener.onClickEpharmacyInfoCart(cartGroupHolderData.enablerLabel, cartGroupHolderData.shop.shopId, cartGroupHolderData.productUiModelList)
                }
            }
            if (cartGroupHolderData.addOnType == CartGroupHolderData.ADD_ON_GIFTING) {
                actionListener.addOnImpression(
                    cartGroupHolderData.productUiModelList.firstOrNull()?.productId ?: ""
                )
            }
        } else {
            binding.addonInfoWidgetLayout.root.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_group

        private const val COLLAPSED_PRODUCTS_LIMIT = 10

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
        const val KEY_ONBOARDING_ICON_PIN = "KEY_ONBOARDING_ICON_PIN"
        const val KEY_HAS_SHOWN_ICON_PIN_ONBOARDING = "KEY_HAS_SHOWN_ICON_PIN_ONBOARDING"

        private const val ITEM_DECORATION_PADDING_LEFT = 48
        private const val SHOP_HEADER_PADDING_10 = 10
    }
}
