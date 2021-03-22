package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    private val ivVoucherImage: ImageView = itemView.findViewById(R.id.iv_voucher_image)
    private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tv_voucher_title)
    private val tvVoucherDescription: TextView = itemView.findViewById(R.id.tv_voucher_description)
    private val llVoucherCopy: View = itemView.findViewById(R.id.ll_voucher_copy)
    private val tvVoucherCopy: TextView = itemView.findViewById(R.id.tv_voucher_copy)

    fun bind(item: MerchantVoucherUiModel) {
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description

        ivVoucherImage.setImageResource(
                when (item.type) {
                    MerchantVoucherType.Private -> R.drawable.ic_play_special_voucher
                    MerchantVoucherType.Shipping -> R.drawable.ic_play_shipping_voucher
                    else -> R.drawable.ic_play_discount_voucher
                }
        )

        if (item.copyable) {
            llVoucherCopy.show()

            tvVoucherCopy.setOnClickListener {
                listener.onCopyVoucherCode(item)
            }
        } else {
            llVoucherCopy.hide()

            tvVoucherCopy.setOnClickListener(null)
        }
    }

    interface Listener {
        fun onCopyVoucherCode(voucher: MerchantVoucherUiModel)
    }
}