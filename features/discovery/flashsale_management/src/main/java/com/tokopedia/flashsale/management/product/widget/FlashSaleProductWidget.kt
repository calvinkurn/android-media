package com.tokopedia.flashsale.management.product.widget

import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.ekstension.loadUrl
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.data.StatusColor
import kotlinx.android.synthetic.main.widget_flash_sale_product_widget.view.*

/**
 * Created by hendry on 31/10/18.
 */
class FlashSaleProductWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var imageWidth: Int = 0
    var hideDetail: Boolean = true

    init {
        applyAttrs(attrs)
        LayoutInflater.from(context).inflate(R.layout.widget_flash_sale_product_widget,
                this, true)
        setData(null)
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.FlashSaleProductWidget)
        try {
            imageWidth = styledAttributes.getDimensionPixelOffset(R.styleable.FlashSaleProductWidget_fspw_image_width, 0)
            hideDetail = styledAttributes.getBoolean(R.styleable.FlashSaleProductWidget_fspw_hide_detail, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    fun setData(item: FlashSaleProductItem?) {
        if (item == null) {
            visibility = View.GONE
        } else {
            if (item.campaign.message.isEmpty()) {
                vgMessageContainer.visibility = View.GONE
            } else {
                tvMessage.text = item.campaign.message
                vgMessageContainer.visibility = View.VISIBLE
            }
            if (hideDetail || item.campaign.discountedPercentage <= 0) {
                tvPercentOff.visibility = View.GONE
            } else {
                tvPercentOff.text = context.getString(R.string.x_percent_off,
                        KMNumbers.formatToPercentString(item.campaign.discountedPercentage.toDouble() / 100))
                tvPercentOff.visibility = View.VISIBLE
            }
            ivProduct.loadUrl(item.campaign.imageUrl, context.resources.getDimension(R.dimen.dp_4))
            tvDepartmentName.text = item.departmentId.toString()
            if (item.campaign.productStatus == FlashSaleProductStatusTypeDef.RESERVE) {
                // show mark
                ivCheckMark.visibility = View.VISIBLE
                tvStatus.visibility = View.GONE
            } else {
                ivCheckMark.visibility = View.GONE
                if (item.campaign.getProductStatusString(context).isEmpty()) {
                    tvStatus.visibility = View.GONE
                } else {
                    tvStatus.text = item.campaign.getProductStatusString(context)
                    val statusColor = item.campaign.getProductStatusColor()
                    tvStatus.setTextColor(ContextCompat.getColor(context, statusColor.textColor))
                    tvStatus.setBackgroundResource(statusColor.bgDrawableRes)
                    tvStatus.visibility = View.VISIBLE
                }
            }
            tvProductName.text = item.name
            if (hideDetail || item.campaign.discountedPercentage <= 0) {
                tvStrikePrice.visibility = View.GONE
            } else {
                tvStrikePrice.paintFlags = tvStrikePrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
                tvStrikePrice.text = KMNumbers.formatRupiahString(item.campaign.originalPrice.toLong())
                tvStrikePrice.visibility = View.VISIBLE
            }
            if (item.campaign.discountedPrice > 0 && !hideDetail) {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.campaign.discountedPrice.toLong())
            } else if (item.campaign.originalPrice > 0) {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.campaign.originalPrice.toLong())
            } else {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.price.toLong())
            }
            if (hideDetail || item.campaign.stock <= 0) {
                tvStock.visibility = View.GONE
            } else {
                val stockText = "${context.getString(R.string.label_stock)} ${item.campaign.stock}"
                tvStock.text = stockText
                tvStock.visibility = View.VISIBLE
            }
            visibility = View.VISIBLE
        }
    }
}