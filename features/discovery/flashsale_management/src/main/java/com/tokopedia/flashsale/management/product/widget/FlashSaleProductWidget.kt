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
import com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.product.data.*
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

    /**
     * Only show the stock statistics only if the product is outside the inReview and Ready State
     */
    fun shouldShowStatisticPostSubmission(item: FlashSaleProductItem?): Boolean {
        return item is FlashSalePostProductItem &&
                item.getCampaignStatusId() != FlashSaleCampaignStatusIdTypeDef.IN_REVIEW &&
                item.getCampaignStatusId() != FlashSaleCampaignStatusIdTypeDef.READY
    }

    fun setData(item: FlashSaleProductItem?) {
        if (item == null) {
            visibility = View.GONE
        } else {
            if (item.getMessage().isEmpty() ||
                    item.getProductStatus() == FlashSaleProductStatusTypeDef.SUBMITTED) {
                vgBlackMessage.visibility = View.GONE
            } else {
                tvMessage.text = item.getMessage()
                vgBlackMessage.visibility = View.VISIBLE
            }
            if (hideDetail || item.getDiscountPercentage() <= 0) {
                tvPercentOff.visibility = View.GONE
            } else {
                tvPercentOff.text = context.getString(R.string.x_percent_off,
                        Math.round(item.getDiscountPercentage().toDouble()).toString().replace("%%", "%"))
                tvPercentOff.visibility = View.VISIBLE
            }
            ImageHandler.LoadImage(ivProduct, item.getProductImageUrl())
            tvDepartmentName.text = item.getDepartmentNameString()
            if (item is FlashSaleSubmissionProductItem) {
                if (item.getMessage().isNotEmpty()) {
                    ivCheckMark.visibility = View.GONE
                    tvStatus.visibility = View.GONE
                } else if (item.getProductStatus() == FlashSaleProductStatusTypeDef.RESERVE) {
                    // show mark
                    ivCheckMark.visibility = View.VISIBLE
                    tvStatus.visibility = View.GONE
                } else if (item.getProductStatus() == FlashSaleProductStatusTypeDef.REJECTED) {
                    // rejected status is not shown until campaign is inReview
                    ivCheckMark.visibility = View.GONE
                    tvStatus.text = FlashSaleProductStatusTypeDef.SUBMITTED.getProductStatusString(context)
                    val statusColor = StatusColor(R.color.tkpd_main_green, R.drawable.rect_green_rounded_left)
                    tvStatus.setTextColor(ContextCompat.getColor(context, statusColor.textColor))
                    tvStatus.setBackgroundResource(statusColor.bgDrawableRes)
                    tvStatus.visibility = View.VISIBLE
                } else {
                    ivCheckMark.visibility = View.GONE
                    if (item.getProductStatus().getProductStatusString(context).isEmpty()) {
                        tvStatus.visibility = View.GONE
                    } else {
                        tvStatus.text = item.getProductStatus().getProductStatusString(context)
                        val statusColor = item.campaign.getProductStatusColor()
                        tvStatus.setTextColor(ContextCompat.getColor(context, statusColor.textColor))
                        tvStatus.setBackgroundResource(statusColor.bgDrawableRes)
                        tvStatus.visibility = View.VISIBLE
                    }
                }
            } else { // item is postsubmission.
                ivCheckMark.visibility = View.GONE
                // When campaign is "inReview", all product status become "Waiting", so we ignore the real status.
                if (item.getCampaignStatusId() == FlashSaleCampaignStatusIdTypeDef.IN_REVIEW) {
                    tvStatus.text = context.getString(R.string.flash_sale_waiting)
                    val statusColor = StatusColor(R.color.tkpd_main_green, R.drawable.rect_green_rounded_left)
                    tvStatus.setTextColor(ContextCompat.getColor(context, statusColor.textColor))
                    tvStatus.setBackgroundResource(statusColor.bgDrawableRes)
                    tvStatus.visibility = View.VISIBLE
                } else if (item.getCampaignStatusId() == FlashSaleCampaignStatusIdTypeDef.READY ||
                        item.getCampaignStatusId() == FlashSaleCampaignStatusIdTypeDef.READY_LOCKED) {
                    // only show status when campaign status is ready and ready locked
                    val statusText = item.getCampaignAdminStatusId().getAdminStatusStringAfterReview(context)
                    if (statusText.isEmpty()) {
                        tvStatus.visibility = View.GONE
                    } else {
                        tvStatus.text = statusText
                        val statusColor = item.getCampaignAdminStatusId().getAdminStatusColorAfterReview()
                        tvStatus.setTextColor(ContextCompat.getColor(context, statusColor.textColor))
                        tvStatus.setBackgroundResource(statusColor.bgDrawableRes)
                        tvStatus.visibility = View.VISIBLE
                    }
                } else {
                    tvStatus.visibility = View.GONE
                }
            }
            tvProductName.text = item.getProductName()
            if (hideDetail || item.getDiscountPercentage() <= 0) {
                tvStrikePrice.visibility = View.GONE
            } else {
                tvStrikePrice.paintFlags = tvStrikePrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
                tvStrikePrice.text = KMNumbers.formatRupiahString(item.getCampOriginalPrice().toLong())
                tvStrikePrice.visibility = View.VISIBLE
            }
            if (item.getDiscountedPrice() > 0 && !hideDetail) {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.getDiscountedPrice().toLong())
            } else if (item.getCampOriginalPrice() > 0) {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.getCampOriginalPrice().toLong())
            } else {
                tvFinalPrice.text = KMNumbers.formatRupiahString(item.getProductPrice().toLong())
            }

            // if post submission after in review, and after ready, and it is accepted, show Sold x of x
            if (shouldShowStatisticPostSubmission(item)) {
                tvStock.text = context.getString(R.string.label_sold_x_from_x,
                        item.getOriginalCustomStock() - item.getCustomStock(),
                        item.getOriginalCustomStock())
                tvStock.visibility = View.VISIBLE
            } else { // else show Stock xx
                if (hideDetail || item.getCustomStock() <= 0) {
                    tvStock.visibility = View.GONE
                } else {
                    val stockText = "${context.getString(R.string.label_stock)} ${item.getCustomStock()}"
                    tvStock.text = stockText
                    tvStock.visibility = View.VISIBLE
                }
            }
            visibility = View.VISIBLE
        }
    }
}