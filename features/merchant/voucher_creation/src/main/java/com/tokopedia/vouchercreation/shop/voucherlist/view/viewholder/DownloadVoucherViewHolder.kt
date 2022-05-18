package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherType
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.databinding.ItemMvcDownloadVoucherBinding

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherViewHolder(itemView: View?) : AbstractViewHolder<DownloadVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_download_voucher
        private const val STRAIGHT_ANGLE = 180
    }

    private var binding: ItemMvcDownloadVoucherBinding? by viewBinding()

    override fun bind(element: DownloadVoucherUiModel) {
        binding?.apply {
            tvMvcRatio.text = element.ratioStr
            tvMvcRatioDescription.text = element.description
            cbxMvcDownloadVoucher.setOnCheckedChangeListener(null)
            cbxMvcDownloadVoucher.isChecked = element.isSelected
            cbxMvcDownloadVoucher.setOnCheckedChangeListener { _, isChecked ->
                element.onCheckBoxClicked(element.downloadVoucherType)
                element.isSelected = isChecked
            }
            setViewExpansion(element.isExpanded)
            root.setOnClickListener {
                val isExpanded = imgMvcVoucher.isVisible
                if (!isExpanded) {
                    element.onImageOpened(adapterPosition)
                }
                toggleViewExpansion(isExpanded)
                element.isExpanded = imgMvcVoucher.isVisible
            }
            icMvcChevron.setOnClickListener {
                element.onChevronIconClicked(element.downloadVoucherType)
            }
            root.setupDownloadImage(element.downloadVoucherType)
        }
    }

    private fun View.setupDownloadImage(downloadVoucherType: DownloadVoucherType) {
        val imageWidth = resources?.getDimensionPixelSize(downloadVoucherType.widthRes)
        binding?.let { binding ->
            binding.imgMvcVoucher.layoutParams?.width = imageWidth.toZeroIfNull()
            binding.imgMvcVoucher.requestLayout()
            Glide.with(this)
                .load(downloadVoucherType.imageUrl)
                .into(binding.imgMvcVoucher)
        }
    }

    private fun setViewExpansion(isExpanded: Boolean) = with(itemView) {
        binding?.imgMvcVoucher?.isVisible = isExpanded
        rotateChevronIcon(!isExpanded)
    }

    private fun toggleViewExpansion(voucherIsVisible: Boolean) = with(itemView) {
        binding?.imgMvcVoucher?.isVisible = !voucherIsVisible
        rotateChevronIcon(voucherIsVisible)
    }

    private fun rotateChevronIcon(isExpanded: Boolean) = with(itemView) {
        val angle = if (isExpanded) Int.ZERO else STRAIGHT_ANGLE
        binding?.icMvcChevron?.rotation = angle.toFloat()
    }

}