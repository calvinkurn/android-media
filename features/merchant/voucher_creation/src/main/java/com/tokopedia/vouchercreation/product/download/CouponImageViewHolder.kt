package com.tokopedia.vouchercreation.product.download

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.item_coupon_image_download.view.*

class CouponImageViewHolder(itemView: View?) : AbstractViewHolder<CouponImageUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_coupon_image_download
        private const val STRAIGHT_ANGLE = 180
    }

    override fun bind(element: CouponImageUiModel) {
        with(itemView) {
            tvMvcRatio.text = element.ratioStr
            tvMvcRatioDescription.text = element.description
            cbxMvcDownloadVoucher.setOnCheckedChangeListener(null)
            cbxMvcDownloadVoucher.isChecked = element.isSelected
            cbxMvcDownloadVoucher.setOnCheckedChangeListener { _, isChecked ->
                element.onCheckBoxClicked(element.imageType)
                element.isSelected = isChecked
            }
            setViewExpansion(element.isExpanded)
            setOnClickListener {
                val isExpanded = imgMvcVoucher.isVisible
                if (!isExpanded) {
                    element.onImageOpened(adapterPosition)
                }
                toggleViewExpansion(isExpanded)
                element.isExpanded = imgMvcVoucher.isVisible
            }
            icMvcChevron?.setOnClickListener {
                element.onChevronIconClicked(element.imageType)
            }
            setupDownloadImage(element.imageType)
        }
    }

    private fun View.setupDownloadImage(type: ImageType) {
        Glide.with(this)
                .load(type.imageUrl)
                .into(imgMvcVoucher)
    }

    private fun setViewExpansion(isExpanded: Boolean) = with(itemView) {
        imgMvcVoucher.isVisible = isExpanded
        rotateChevronIcon(!isExpanded)
    }

    private fun toggleViewExpansion(voucherIsVisible: Boolean) = with(itemView) {
        imgMvcVoucher.isVisible = !voucherIsVisible
        rotateChevronIcon(voucherIsVisible)
    }

    private fun rotateChevronIcon(isExpanded: Boolean) = with(itemView) {
        val angle = if (isExpanded) Int.ZERO else STRAIGHT_ANGLE
        icMvcChevron.rotation = angle.toFloat()
    }

}