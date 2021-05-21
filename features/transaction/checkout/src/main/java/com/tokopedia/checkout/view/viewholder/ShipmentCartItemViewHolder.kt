package com.tokopedia.checkout.view.viewholder

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.utils.WeightFormatterUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ShipmentCartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var shipmentItemListener: ShipmentItemListener? = null
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
    private val mLlShippingWarningContainer: LinearLayout = itemView.findViewById(R.id.ll_shipping_warning_container)
    private val mSeparatorMultipleProductSameStore: View = itemView.findViewById(R.id.v_separator_multiple_product_same_store)
    private val tvErrorShipmentItemTitle: TextView = itemView.findViewById(R.id.tv_error_shipment_item_title)
    private val tvErrorShipmentItemDescription: TextView = itemView.findViewById(R.id.tv_error_shipment_item_description)
    private val productTicker: Ticker = itemView.findViewById(R.id.product_ticker)
    private val mTextVariant: Typography = itemView.findViewById(R.id.text_variant)
    private val mLayoutProductInfo: FlexboxLayout = itemView.findViewById(R.id.layout_product_info)
    private val mIconTooltip: IconUnify = itemView.findViewById(R.id.icon_tooltip)
    private val mPricePerProduct: Typography = itemView.findViewById(R.id.text_item_per_product)

    fun bindViewHolder(cartItem: CartItemModel, listener: ShipmentItemListener?) {
        shipmentItemListener = listener
        if (cartItem.isError) {
            showShipmentWarning(cartItem)
        } else {
            hideShipmentWarning()
        }
        mSeparatorMultipleProductSameStore.visibility = View.VISIBLE
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
        val dp10 = mTvProductPrice.resources.getDimensionPixelOffset(R.dimen.dp_10)
        if (cartItem.originalPrice > 0) {
            mTvProductPrice.setPadding(dp4, dp4, 0, 0)
            mTvProductOriginalPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItem.originalPrice.toLong(), false).removeDecimalSuffix()
            mTvProductOriginalPrice.paintFlags = mTvProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mTvProductOriginalPrice.visibility = View.VISIBLE
        } else {
            mTvProductPrice.setPadding(dp10, dp4, 0, 0)
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
        mRlPurchaseProtection.visibility = if (cartItem.isProtectionAvailable) View.VISIBLE else View.GONE
        if (cartItem.isProtectionAvailable) {
            mIconTooltip.setOnClickListener { shipmentItemListener?.navigateToWebView(cartItem.protectionLinkUrl) }
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
            productTicker.setTextDescription(cartItemModel.tickerMessage ?: "")
        } else productTicker.visibility = View.GONE
    }

    private fun showShipmentWarning(cartItemModel: CartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.errorMessage)) {
            tvErrorShipmentItemTitle.text = cartItemModel.errorMessage
            tvErrorShipmentItemTitle.visibility = View.VISIBLE
            mLlShippingWarningContainer.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(cartItemModel.errorMessageDescription)) {
                tvErrorShipmentItemDescription.text = cartItemModel.errorMessageDescription
                tvErrorShipmentItemDescription.visibility = View.VISIBLE
            } else {
                tvErrorShipmentItemDescription.visibility = View.GONE
            }
        } else {
            mLlShippingWarningContainer.visibility = View.GONE
        }
        disableItemView()
    }

    private fun hideShipmentWarning() {
        mLlShippingWarningContainer.visibility = View.GONE
        enableItemView()
    }

    private fun disableItemView() {
        val colorGreyNonActiveText = ContextCompat.getColor(mTvProductName.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_20)
        mTvProductName.setTextColor(colorGreyNonActiveText)
        mTvProductPrice.setTextColor(colorGreyNonActiveText)
        mTvProductOriginalPrice.setTextColor(colorGreyNonActiveText)
        mTvOptionalNoteToSeller.setTextColor(colorGreyNonActiveText)
        mTvProductCountAndWeight.setTextColor(colorGreyNonActiveText)
        mTextVariant.setTextColor(colorGreyNonActiveText)
        setImageFilterGrayScale()
    }

    private fun setImageFilterGrayScale() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val disabledColorFilter = ColorMatrixColorFilter(matrix)
        mIvProductImage.colorFilter = disabledColorFilter
        mIvProductImage.imageAlpha = IMAGE_ALPHA_DISABLED
    }

    private fun enableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mTvProductName.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        mTextVariant.setTextColor(ContextCompat.getColor(mTextVariant.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        mTvProductPrice.setTextColor(ContextCompat.getColor(mTvProductPrice.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        mTvProductOriginalPrice.setTextColor(ContextCompat.getColor(mTvProductOriginalPrice.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        mTvProductCountAndWeight.setTextColor(ContextCompat.getColor(mTvProductCountAndWeight.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mTvOptionalNoteToSeller.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        setImageFilterNormal()
    }

    private fun setImageFilterNormal() {
        mIvProductImage.colorFilter = null
        mIvProductImage.imageAlpha = IMAGE_ALPHA_ENABLED
    }

    interface ShipmentItemListener {
        fun notifyOnPurchaseProtectionChecked(checked: Boolean, position: Int)
        fun navigateToWebView(protectionLinkUrl: String?)
    }

    companion object {
        private const val IMAGE_ALPHA_DISABLED = 128
        private const val IMAGE_ALPHA_ENABLED = 255
    }

}