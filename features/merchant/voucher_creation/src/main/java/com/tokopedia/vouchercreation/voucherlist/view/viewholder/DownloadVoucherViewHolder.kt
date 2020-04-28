package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.DownloadVoucherUiModel
import kotlinx.android.synthetic.main.item_mvc_download_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherViewHolder(itemView: View?) : AbstractViewHolder<DownloadVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_download_voucher
    }

    override fun bind(element: DownloadVoucherUiModel) {
        with(itemView) {
            tvMvcRatio.text = element.ratioStr
            tvMvcRatioDescription.text = element.description
            cbxMvcDownloadVoucher.isChecked = element.isSelected
            cbxMvcDownloadVoucher.setOnCheckedChangeListener { _, isChecked ->
                element.isSelected = isChecked
            }
            setOnClickListener {
                setViewExpansion(imgMvcVoucher.isVisible)
            }
        }
    }

    private fun setViewExpansion(isExpanded: Boolean) = with(itemView) {
        imgMvcVoucher.isVisible = !isExpanded
        rotateChevronIcon(isExpanded)
    }

    private fun rotateChevronIcon(isExpanded: Boolean) = with(itemView) {
        @DrawableRes val icon = if (isExpanded) {
            R.drawable.ic_mvc_chevron_down
        } else {
            R.drawable.ic_mvc_chevron_up
        }
        icMvcChevron.loadImageDrawable(icon)
    }
}