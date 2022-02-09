package com.tokopedia.checkout.view.viewholder

import android.graphics.Paint
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.utils.WeightFormatterUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShipmentCartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var shipmentItemListener: ShipmentItemListener? = null
    private val productBundlingInfo: ConstraintLayout = itemView.findViewById(R.id.product_bundling_info)
    private val imageBundle: ImageUnify = itemView.findViewById(R.id.image_bundle)
    private val textBundleTitle: Typography = itemView.findViewById(R.id.text_bundle_title)
    private val textBundlePrice: Typography = itemView.findViewById(R.id.text_bundle_price)
    private val textBundleSlashPrice: Typography = itemView.findViewById(R.id.text_bundle_slash_price)
    private val vBundlingProductSeparator: View = itemView.findViewById(R.id.v_bundling_product_separator)
    private val llFrameItemProductContainer: LinearLayout = itemView.findViewById(R.id.ll_frame_item_product_container)
    private val rlProductInfo: ConstraintLayout = itemView.findViewById(R.id.rl_product_info)
    private val mIvProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val mTvProductName: Typography = itemView.findViewById(R.id.tv_product_name)
    private val mTvProductPrice: Typography = itemView.findViewById(R.id.tv_product_price)
    private val mTvProductOriginalPrice: Typography = itemView.findViewById(R.id.tv_product_original_price)
    private val mTvProductCountAndWeight: Typography = itemView.findViewById(R.id.tv_item_count_and_weight)
    private val mTvOptionalNoteToSeller: TextView = itemView.findViewById(R.id.tv_optional_note_to_seller)
    private val mRlPurchaseProtection: RelativeLayout = itemView.findViewById(R.id.rlayout_purchase_protection)
    private val mTvPPPLinkText: TextView = itemView.findViewById(R.id.text_link_text)
    private val mTvPPPPrice: TextView = itemView.findViewById(R.id.text_protection_desc)
    private val mCbPPP: CheckboxUnify = itemView.findViewById(R.id.checkbox_ppp)
    private val mSeparatorMultipleProductSameStore: View = itemView.findViewById(R.id.v_separator_multiple_product_same_store)
    private val tickerError: Ticker = itemView.findViewById(R.id.checkout_ticker_product_error)
    private val productTicker: Ticker = itemView.findViewById(R.id.product_ticker)
    private val mTextVariant: Typography = itemView.findViewById(R.id.text_variant)
    private val mLayoutProductInfo: FlexboxLayout = itemView.findViewById(R.id.layout_product_info)
    private val mIconTooltip: IconUnify = itemView.findViewById(R.id.icon_tooltip)
    private val mPricePerProduct: Typography = itemView.findViewById(R.id.text_item_per_product)
    private val layoutGiftingAddonProductLevel: ConstraintLayout = itemView.findViewById(R.id.layout_addon_gifting_product_level)
    private val buttonGiftingAddOnProductLevel: ButtonGiftingAddOnView = itemView.findViewById(R.id.button_gifting_addon_product_level)

    fun bindViewHolder(cartItem: CartItemModel, listener: ShipmentItemListener?) {
        shipmentItemListener = listener
        if (cartItem.isError) {
            showShipmentWarning(cartItem)
        } else {
            hideShipmentWarning()
        }
        ImageHandler.LoadImage(mIvProductImage, cartItem.imageUrl)
        mTvProductName.text = cartItem.name
        mTvProductCountAndWeight.text = String.format(mTvProductCountAndWeight.context
                .getString(R.string.iotem_count_and_weight_format), cartItem.quantity.toString(),
                WeightFormatterUtil.getFormattedWeight(cartItem.weight, cartItem.quantity))
        if (!TextUtils.isEmpty(cartItem.variant)) {
            mTextVariant.text = cartItem.variant
            mTextVariant.visibility = View.VISIBLE
        } else {
            mTextVariant.visibility = View.GONE
        }
        renderProductPrice(cartItem)
        renderNotesToSeller(cartItem)
        renderPurchaseProtection(cartItem)
        renderProductTicker(cartItem)
        renderProductProperties(cartItem)
        renderBundlingInfo(cartItem)
        renderAddOnProductLevel(cartItem)
    }

    private fun renderProductProperties(cartItemModel: CartItemModel) {
        val productInformationList = cartItemModel.productInformation
        if (productInformationList != null && !productInformationList.isEmpty()) {
            for (i in productInformationList.indices) {
                val productInfo = Typography(itemView.context)
                productInfo.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                productInfo.setType(SMALL)
                if (mLayoutProductInfo.childCount > 0) {
                    productInfo.text = ", " + productInformationList[i]
                } else {
                    productInfo.text = productInformationList[i]
                }
                mLayoutProductInfo.addView(productInfo)
            }
            mLayoutProductInfo.visibility = View.VISIBLE
        } else {
            mLayoutProductInfo.visibility = View.GONE
        }
    }

    private fun renderProductPrice(cartItem: CartItemModel) {
        mTvProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItem.price.toLong(), false).removeDecimalSuffix()
        val dp4 = mTvProductPrice.resources.getDimensionPixelOffset(R.dimen.dp_4)
        if (cartItem.originalPrice > 0) {
            mTvProductPrice.setPadding(0, dp4, 0, 0)
            mTvProductOriginalPrice.setPadding(0, dp4, 0, 0)
            mTvProductOriginalPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItem.originalPrice.toLong(), false).removeDecimalSuffix()
            mTvProductOriginalPrice.paintFlags = mTvProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mTvProductOriginalPrice.visibility = View.VISIBLE
        } else {
            mTvProductPrice.setPadding(0, dp4, 0, 0)
            mTvProductOriginalPrice.visibility = View.GONE
        }
    }

    private fun renderNotesToSeller(cartItem: CartItemModel) {
        if (!TextUtils.isEmpty(cartItem.noteToSeller)) {
            mTvOptionalNoteToSeller.text = cartItem.noteToSeller
            mTvOptionalNoteToSeller.visibility = View.VISIBLE
        } else {
            mTvOptionalNoteToSeller.visibility = View.GONE
        }
    }

    private fun renderPurchaseProtection(cartItem: CartItemModel) {
        mRlPurchaseProtection.visibility = if (cartItem.isProtectionAvailable && !cartItem.isError) View.VISIBLE else View.GONE
        if (cartItem.isProtectionAvailable && !cartItem.isError) {
            mIconTooltip.setOnClickListener { shipmentItemListener?.navigateToWebView(cartItem) }
            mTvPPPLinkText.text = cartItem.protectionTitle
            mTvPPPPrice.text = cartItem.protectionSubTitle
            mPricePerProduct.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItem.protectionPricePerProduct.toLong(), false).removeDecimalSuffix()
            if (cartItem.isProtectionCheckboxDisabled) {
                mCbPPP.isEnabled = false
                mCbPPP.isChecked = true
                mCbPPP.skipAnimation()
            } else {
                mCbPPP.isEnabled = true
                mCbPPP.isChecked = cartItem.isProtectionOptIn
                mCbPPP.skipAnimation()
                mCbPPP.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean -> shipmentItemListener?.notifyOnPurchaseProtectionChecked(checked, adapterPosition + 1) }
            }
        }
    }

    private fun renderProductTicker(cartItemModel: CartItemModel) {
        if (cartItemModel.isShowTicker && !TextUtils.isEmpty(cartItemModel.tickerMessage)) {
            productTicker.visibility = View.VISIBLE
            productTicker.setTextDescription(cartItemModel.tickerMessage)
        } else productTicker.visibility = View.GONE
    }

    private fun showShipmentWarning(cartItemModel: CartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.errorMessage)) {
            if (!TextUtils.isEmpty(cartItemModel.errorMessageDescription)) {
                tickerError.tickerTitle = cartItemModel.errorMessage
                tickerError.setTextDescription(cartItemModel.errorMessageDescription)
            } else {
                tickerError.setTextDescription(cartItemModel.errorMessage)
            }

            if (cartItemModel.isBundlingItem) {
                if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                    tickerError.visible()
                } else {
                    tickerError.gone()
                }
            } else {
                tickerError.visible()
            }
        } else {
            tickerError.gone()
        }

        if (!cartItemModel.isShopError) {
            disableItemView()
        }
    }

    private fun hideShipmentWarning() {
        tickerError.gone()
        enableItemView()
    }

    private fun enableItemView() {
        productBundlingInfo.alpha = 1.0f
        llFrameItemProductContainer.alpha = 1.0f
    }

    private fun disableItemView() {
        productBundlingInfo.alpha = 0.5f
        llFrameItemProductContainer.alpha = 0.5f
    }

    private fun renderBundlingInfo(cartItemModel: CartItemModel) {
        val ivProductImageLayoutParams = mIvProductImage.layoutParams as MarginLayoutParams
        val tvOptionalNoteToSellerLayoutParams = mTvOptionalNoteToSeller.layoutParams as MarginLayoutParams
        val productContainerLayoutParams = llFrameItemProductContainer.layoutParams as MarginLayoutParams
        val productInfoLayoutParams = rlProductInfo.layoutParams as MarginLayoutParams
        val bottomMargin = itemView.resources.getDimensionPixelSize(R.dimen.dp_8)
        if (cartItemModel.isBundlingItem) {
            if (!TextUtils.isEmpty(cartItemModel.bundleIconUrl)) {
                ImageHandler.loadImage2(imageBundle, cartItemModel.bundleIconUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            }

            ivProductImageLayoutParams.leftMargin = itemView.resources.getDimensionPixelSize(R.dimen.dp_14)
            tvOptionalNoteToSellerLayoutParams.leftMargin = itemView.resources.getDimensionPixelSize(R.dimen.dp_14)
            vBundlingProductSeparator.visibility = View.VISIBLE
            val productImageLayoutParams = mIvProductImage.layoutParams as MarginLayoutParams
            val productNameLayoutParams = mTvProductName.layoutParams as MarginLayoutParams
            val productMarginTop = itemView.resources.getDimensionPixelSize(R.dimen.dp_12)
            if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                productBundlingInfo.visibility = View.VISIBLE
                productImageLayoutParams.topMargin = 0
                productNameLayoutParams.topMargin = 0
                mSeparatorMultipleProductSameStore.invisible()
            } else {
                productBundlingInfo.visibility = View.GONE
                productImageLayoutParams.topMargin = productMarginTop
                productNameLayoutParams.topMargin = productMarginTop
                mSeparatorMultipleProductSameStore.gone()
            }
            textBundleTitle.text = cartItemModel.bundleTitle
            textBundlePrice.text = convertPriceValueToIdrFormat(cartItemModel.bundlePrice, false).removeDecimalSuffix()
            textBundleSlashPrice.text = convertPriceValueToIdrFormat(cartItemModel.bundleOriginalPrice, false).removeDecimalSuffix()
            textBundleSlashPrice.paintFlags = textBundleSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productContainerLayoutParams.bottomMargin = 0
            productInfoLayoutParams.bottomMargin = 0
        } else {
            ivProductImageLayoutParams.leftMargin = 0
            tvOptionalNoteToSellerLayoutParams.leftMargin = 0
            vBundlingProductSeparator.visibility = View.GONE
            productBundlingInfo.visibility = View.GONE
            mSeparatorMultipleProductSameStore.show()
            productContainerLayoutParams.bottomMargin = bottomMargin
            productInfoLayoutParams.bottomMargin = bottomMargin
        }
    }

    private fun renderAddOnProductLevel(cartItemModel: CartItemModel) {
        val addOns = cartItemModel.addOnProductLevelModel
        if (addOns.status == 0) {
            layoutGiftingAddonProductLevel.visibility = View.GONE
            buttonGiftingAddOnProductLevel.visibility = View.GONE
        } else {
            if (addOns.status == 1) {
                buttonGiftingAddOnProductLevel.state = ButtonGiftingAddOnView.State.ACTIVE
            } else if (addOns.status == 2) {
                buttonGiftingAddOnProductLevel.state = ButtonGiftingAddOnView.State.INACTIVE
            }
            layoutGiftingAddonProductLevel.visibility = View.VISIBLE
            buttonGiftingAddOnProductLevel.visibility = View.VISIBLE
            buttonGiftingAddOnProductLevel.title = addOns.addOnsButtonModel.title
            buttonGiftingAddOnProductLevel.desc = addOns.addOnsButtonModel.description
            buttonGiftingAddOnProductLevel.urlLeftIcon = addOns.addOnsButtonModel.leftIconUrl
            buttonGiftingAddOnProductLevel.urlRightIcon = addOns.addOnsButtonModel.rightIconUrl
            buttonGiftingAddOnProductLevel.setOnClickListener { shipmentItemListener?.openAddOnBottomSheet(cartItemModel) }
        }
    }

    interface ShipmentItemListener {
        fun notifyOnPurchaseProtectionChecked(checked: Boolean, position: Int)
        fun navigateToWebView(cartItem: CartItemModel)
        fun openAddOnBottomSheet(cartItem: CartItemModel)
    }

    companion object {
        private const val IMAGE_ALPHA_DISABLED = 128
        private const val IMAGE_ALPHA_ENABLED = 255
    }

}