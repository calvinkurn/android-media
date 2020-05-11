package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivVoucherImage: ImageView = itemView.findViewById(R.id.iv_voucher_image)
    private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tv_voucher_title)
    private val tvVoucherDescription: TextView = itemView.findViewById(R.id.tv_voucher_description)

    fun bind(item: MerchantVoucherUiModel) {
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description

        ivVoucherImage.setImageResource(
                if (item.type == MerchantVoucherType.Shipping) R.drawable.ic_play_shipping_voucher
                else R.drawable.ic_play_discount_voucher
        )
    }
}