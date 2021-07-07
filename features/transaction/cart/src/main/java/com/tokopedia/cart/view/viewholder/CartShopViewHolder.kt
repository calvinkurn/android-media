package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemShopBinding
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ERROR
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

    fun bindData(cartShopHolderData: CartShopHolderData) {
        renderWarningAndError(cartShopHolderData)
        renderWarningItemHeader(cartShopHolderData)
        renderShopName(cartShopHolderData)
        renderShopBadge(cartShopHolderData)
        if (cartShopHolderData.isCollapsed) {
            renderCollapsedCartItems(cartShopHolderData)
        } else {
            renderExpandedCartItems(cartShopHolderData)
        }
        renderAccordion(cartShopHolderData)
        renderCheckBox(cartShopHolderData)
        renderFulfillment(cartShopHolderData)
        renderPreOrder(cartShopHolderData)
        renderIncidentLabel(cartShopHolderData)
        renderFreeShipping(cartShopHolderData)
        renderEstimatedTimeArrival(cartShopHolderData)
        renderMaximumWeight(cartShopHolderData)
    }

    private fun renderWarningAndError(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData?.isWarning == true) {
            binding.llWarningAndError.root.show()
        } else {
            binding.llWarningAndError.root.gone()
        }
    }

    private fun renderShopName(cartShopHolderData: CartShopHolderData) {
        val shopName = cartShopHolderData.shopGroupAvailableData?.shopName
        binding.tvShopName.text = shopName
        binding.tvShopName.setOnClickListener {
            actionListener.onCartShopNameClicked(
                    cartShopHolderData.shopGroupAvailableData?.shopId,
                    cartShopHolderData.shopGroupAvailableData?.shopName,
                    cartShopHolderData.shopGroupAvailableData?.isTokoNow ?: false)
        }
    }

    private fun renderShopBadge(cartShopHolderData: CartShopHolderData) {
        val shopTypeInfoData = cartShopHolderData.shopGroupAvailableData?.shopTypeInfo
        if (shopTypeInfoData?.shopBadge?.isNotBlank() == true) {
            ImageHandler.loadImageWithoutPlaceholder(binding.imgShopBadge, shopTypeInfoData.shopBadge)
            binding.imgShopBadge.contentDescription = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shopTypeInfoData.title.toLowerCase(Locale("id")))
            binding.imgShopBadge.show()
        } else {
            binding.imgShopBadge.gone()
        }
    }

    private fun renderExpandedCartItems(cartShopHolderData: CartShopHolderData) {
        val cartItemAdapter = CartItemAdapter(cartItemAdapterListener, compositeSubscription, adapterPosition)
        cartItemAdapter.addDataList(cartShopHolderData.shopGroupAvailableData?.cartItemDataList)
        val linearLayoutManager = LinearLayoutManager(binding.rvCartItem.context)
        binding.rvCartItem.layoutManager = linearLayoutManager
        binding.rvCartItem.adapter = cartItemAdapter
        (binding.rvCartItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
    }

    private fun renderCollapsedCartItems(cartShopHolderData: CartShopHolderData) {
        val maxIndex = min(10, cartShopHolderData.shopGroupAvailableData?.cartItemHolderDataList?.size
                ?: 0)
        val cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
        cartCartCollapsedProductAdapter.parentPosition = adapterPosition
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList = cartShopHolderData.shopGroupAvailableData?.cartItemHolderDataList?.subList(0, maxIndex)
                ?: mutableListOf()
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvCartItem.layoutManager = layoutManager
        binding.rvCartItem.adapter = cartCartCollapsedProductAdapter
        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        val paddingLeft = itemView.context?.resources?.getDimension(R.dimen.dp_40)?.toInt() ?: 0
        val paddingRight = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        binding.rvCartItem.addItemDecoration(CartHorizontalItemDecoration(paddingLeft, paddingRight))
    }

    private fun renderAccordion(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.isCollapsible) {
            if (cartShopHolderData.isCollapsed) {
                binding.imageChevron.rotation = 0f
                binding.textAccordion.text = cartShopHolderData.showMoreWording
            } else {
                binding.imageChevron.rotation = 180f
                binding.textAccordion.text = cartShopHolderData.showLessWording
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
            if (cartShopHolderData.shopGroupAvailableData?.fulfillmentName?.isNotBlank() == true) {
                if (cartShopHolderData.shopGroupAvailableData?.isFulfillment == true && cartShopHolderData.shopGroupAvailableData?.fulfillmentBadgeUrl?.isNotEmpty() == true) {
                    iuImageFulfill.show()
                    iuImageFulfill.loadImageWithoutPlaceholder(cartShopHolderData.shopGroupAvailableData?.fulfillmentBadgeUrl
                            ?: "")
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.show()
                tvFulfillDistrict.text = cartShopHolderData.shopGroupAvailableData?.fulfillmentName
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderEstimatedTimeArrival(cartShopHolderData: CartShopHolderData) {
        val eta = cartShopHolderData.shopGroupAvailableData?.estimatedTimeArrival ?: ""
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

    private fun renderWarningItemHeader(data: CartShopHolderData) {
        with(binding.llWarningAndError) {
            when {
                data.shopGroupAvailableData?.isWarning == true -> {
                    val warningDescription = data.shopGroupAvailableData?.warningDescription
                    if (warningDescription?.isNotBlank() == true) {
                        tickerWarning.tickerTitle = data.shopGroupAvailableData?.warningTitle
                        tickerWarning.setTextDescription(warningDescription)
                    } else {
                        tickerWarning.tickerTitle = null
                        tickerWarning.setTextDescription(data.shopGroupAvailableData?.warningTitle
                                ?: "")
                    }
                    tickerWarning.tickerType = TYPE_WARNING
                    tickerWarning.tickerShape = SHAPE_LOOSE
                    tickerWarning.closeButtonVisibility = View.GONE
                    tickerWarning.show()
                    tickerWarning.post {
                        binding.llWarningAndError.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        binding.llWarningAndError.tickerWarning.requestLayout()
                    }
                    layoutWarning.show()
                }
                data.shopGroupAvailableData?.shopTicker?.isNotEmpty() == true -> {
                    tickerWarning.tickerTitle = null
                    tickerWarning.setTextDescription(data.shopGroupAvailableData?.shopTicker ?: "")
                    tickerWarning.tickerType = TYPE_WARNING
                    tickerWarning.tickerShape = SHAPE_LOOSE
                    tickerWarning.closeButtonVisibility = View.GONE
                    tickerWarning.show()
                    tickerWarning.post {
                        binding.llWarningAndError.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        binding.llWarningAndError.tickerWarning.requestLayout()
                    }
                    layoutError.gone()
                    layoutWarning.show()
                    root.show()
                }
                else -> {
                    tickerWarning.gone()
                    layoutWarning.gone()
                }
            }
        }
    }

    private fun cbSelectShopClickListener(cartShopHolderData: CartShopHolderData) {
        val isChecked: Boolean
        if (cartShopHolderData.isPartialSelected) {
            isChecked = true
            cartShopHolderData.setAllItemSelected(true)
            cartShopHolderData.isPartialSelected = false
        } else {
            isChecked = !cartShopHolderData.isAllSelected
        }
        var isAllSelected = true
        cartShopHolderData.shopGroupAvailableData?.cartItemDataList?.forEach {
            if (it.cartItemData.isError && it.cartItemData.isSingleChild) {
                isAllSelected = false
                return@forEach
            }
        }
        cartShopHolderData.setAllItemSelected(isAllSelected)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            actionListener.onShopItemCheckChanged(adapterPosition, isChecked)
        }
    }

    private fun renderPreOrder(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.shopGroupAvailableData?.preOrderInfo?.isNotBlank() == true) {
                labelPreOrder.text = cartShopHolderData.shopGroupAvailableData?.preOrderInfo
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
            if (cartShopHolderData.shopGroupAvailableData?.incidentInfo?.isNotBlank() == true) {
                labelIncident.text = cartShopHolderData.shopGroupAvailableData?.incidentInfo
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
            if (cartShopHolderData.shopGroupAvailableData?.freeShippingBadgeUrl?.isNotBlank() == true) {
                ImageHandler.loadImageWithoutPlaceholderAndError(
                        imgFreeShipping, cartShopHolderData.shopGroupAvailableData?.freeShippingBadgeUrl
                )
                val contentDescriptionStringResource = if (cartShopHolderData.shopGroupAvailableData?.isFreeShippingExtra == true) {
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
        if (cartShopHolderData.shopGroupAvailableData?.shouldValidateWeight == true) {
            val currentWeight = cartShopHolderData.shopGroupAvailableData?.totalWeight ?: return
            val maximumWeight = cartShopHolderData.shopGroupAvailableData?.maximumShippingWeight
                    ?: return
            val extraWeight = (currentWeight - maximumWeight) / 1000
            val descriptionText = cartShopHolderData.shopGroupAvailableData?.maximumWeightWording
                    ?: ""
            if (extraWeight > 0 && descriptionText.isNotEmpty()) {
                with(binding.llWarningAndError) {
                    tickerWarning.tickerTitle = null
                    tickerWarning.setTextDescription(descriptionText.replace(ShopGroupAvailableData.MAXIMUM_WEIGHT_WORDING_REPLACE_KEY, NumberFormat.getNumberInstance(Locale("in", "id")).format(extraWeight)))
                    tickerWarning.tickerType = TYPE_WARNING
                    tickerWarning.tickerShape = SHAPE_LOOSE
                    tickerWarning.closeButtonVisibility = View.GONE
                    tickerWarning.show()
                    tickerWarning.post {
                        binding.llWarningAndError.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        binding.llWarningAndError.tickerWarning.requestLayout()
                    }
                    layoutError.gone()
                    layoutWarning.show()
                    root.show()
                }
            } else {
                with(binding.llWarningAndError) {
                    tickerWarning.gone()
                    layoutWarning.gone()
                    root.gone()
                }
            }
        }
    }

    companion object {
        val TYPE_VIEW_ITEM_SHOP = R.layout.item_shop

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
    }

}