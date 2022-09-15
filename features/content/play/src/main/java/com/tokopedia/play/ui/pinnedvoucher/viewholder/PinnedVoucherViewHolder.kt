package com.tokopedia.play.ui.pinnedvoucher.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.R
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 23/02/21.
 */
class PinnedVoucherViewHolder(itemView: View, private val listener: Listener) :
    BaseViewHolder(itemView) {

    private val ivVoucherImage =
        itemView.findViewById<AppCompatImageView>(R.id.iv_pinned_voucher_image)
    private val tvVoucherTitle = itemView.findViewById<Typography>(R.id.tv_pinned_voucher_title)
    private val tvVoucherDescription =
        itemView.findViewById<Typography>(R.id.tv_pinned_voucher_description)

    private val childClickListener = View.OnClickListener {
        itemView.performClick()
    }

    init {
        tvVoucherTitle.setOnClickListener(childClickListener)
        ivVoucherImage.setOnClickListener(childClickListener)
        tvVoucherDescription.setOnClickListener(childClickListener)
    }

    fun bind(item: PlayVoucherUiModel.Merchant) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onVoucherImpressed(item, adapterPosition)
        }

        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description

        ivVoucherImage.setImageResource(
            when {
                item.isPrivate -> R.drawable.ic_play_special_voucher
                item.type == MerchantVoucherType.Shipping -> R.drawable.ic_play_shipping_voucher
                else -> R.drawable.ic_play_discount_voucher
            }
        )

        itemView.setOnClickListener {
            if (item.copyable) listener.onVoucherClicked(item)
        }
    }

    interface Listener {
        fun onVoucherImpressed(voucher: PlayVoucherUiModel.Merchant, position: Int)
        fun onVoucherClicked(voucher: PlayVoucherUiModel.Merchant)
    }
}
