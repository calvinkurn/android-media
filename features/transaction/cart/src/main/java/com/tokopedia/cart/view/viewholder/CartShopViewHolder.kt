package com.tokopedia.cart.view.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemShopBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.lang.Math.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.min

class CartShopViewHolder(private val binding: ItemShopBinding,
                         private val actionListener: ActionListener,
                         private val cartItemAdapterListener: CartItemAdapter.ActionListener,
                         private val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    private val localCacheHandler: LocalCacheHandler by lazy {
        LocalCacheHandler(itemView.context, KEY_ONBOARDING_ICON_PIN)
    }

    fun bindUpdatedWeight(cartShopHolderData: CartShopHolderData) {
        renderMaximumWeight(cartShopHolderData)
        cartShopHolderData.isNeedToRefreshWeight = false
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
        if (cartShopHolderData.isCollapsed) {
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
        binding.tvShopName.text = shopName
        binding.tvShopName.setOnClickListener {
            actionListener.onCartShopNameClicked(
                    cartShopHolderData.shopId,
                    cartShopHolderData.shopName,
                    cartShopHolderData.isTokoNow)
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
        val cartItemAdapter = CartItemAdapter(cartItemAdapterListener, adapterPosition)
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
        val maxIndex = min(10, cartShopHolderData.productUiModelList.size)
        val cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
        cartCartCollapsedProductAdapter.parentPosition = adapterPosition
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList = cartShopHolderData.productUiModelList.subList(0, maxIndex)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvCartItem.layoutManager = layoutManager
        binding.rvCartItem.adapter = cartCartCollapsedProductAdapter

        setCollapsedRecyclerViewHeight(cartShopHolderData)

        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        val paddingLeft = itemView.context?.resources?.getDimension(R.dimen.dp_48)?.toInt() ?: 0
        val paddingRight = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        binding.rvCartItem.addItemDecoration(CartHorizontalItemDecoration(paddingLeft, paddingRight))
    }

    private fun setCollapsedRecyclerViewHeight(cartShopHolderData: CartShopHolderData) {
        var hasProductWithVariant = false
        val cartItemDataList = cartShopHolderData.productUiModelList
        if (cartItemDataList.isNotEmpty()) {
            val maxIndex = min(cartItemDataList.size, 10)
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
        if (cartShopHolderData.isCollapsible) {
            val showMoreWording: String
            val showLessWording = itemView.context.getString(R.string.label_tokonow_show_less)
            val itemCount = cartShopHolderData.productUiModelList.size
            showMoreWording = if (itemCount > 10) {
                val exceedItemCount = itemCount - 10
                itemView.context.getString(R.string.label_tokonow_show_other, exceedItemCount)
            } else {
                itemView.context.getString(R.string.label_tokonow_show_more)
            }

            if (cartShopHolderData.isCollapsed) {
                binding.imageChevron.rotation = 0f
                binding.textAccordion.text = showMoreWording
            } else {
                binding.imageChevron.rotation = 180f
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
        binding.cbSelectShop.isEnabled = true
        binding.cbSelectShop.isChecked = cartShopHolderData.isAllSelected
        binding.cbSelectShop.skipAnimation()
        initCheckboxWatcherDebouncer(cartShopHolderData, compositeSubscription)
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
                        imgFreeShipping, cartShopHolderData.freeShippingBadgeUrl
                )
                val contentDescriptionStringResource = if (cartShopHolderData.isFreeShippingExtra) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                imgFreeShipping.contentDescription = itemView.context.getString(contentDescriptionStringResource)
                imgFreeShipping.show()
                separatorFreeShipping.show()
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
                        binding.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
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
        val paddingOffset = itemView.context?.resources?.getDimensionPixelSize(R.dimen.dp_12)
                ?: 0
        return offset + paddingOffset + tickerHeight
    }

    companion object {
        val LAYOUT = R.layout.item_shop

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
        const val KEY_ONBOARDING_ICON_PIN = "KEY_ONBOARDING_ICON_PIN"
        const val KEY_HAS_SHOWN_ICON_PIN_ONBOARDING = "KEY_HAS_SHOWN_ICON_PIN_ONBOARDING"
    }

}