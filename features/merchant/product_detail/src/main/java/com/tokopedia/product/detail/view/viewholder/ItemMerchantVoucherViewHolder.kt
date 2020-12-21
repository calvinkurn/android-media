package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.model.getAmountShortString
import com.tokopedia.merchantvoucher.common.model.getMinSpendLongString
import com.tokopedia.merchantvoucher.common.model.getTypeString
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.adapter.ProductMerchantVoucherAdapter
import kotlinx.android.synthetic.main.item_product_merchant_voucher.view.*

/**
 * Created by Yehezkiel on 22/09/20
 */
class ItemMerchantVoucherViewHolder(view: View, val listener: ProductMerchantVoucherAdapter.PdpMerchantVoucherInterface?) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_product_merchant_voucher
    }

    fun bind(element: MerchantVoucherViewModel) {
        renderIcon(element.merchantVoucherType)
        renderTitle(element)
        renderDescription(element)

        itemView.setOnClickListener {
            listener?.onMerchantVoucherClicked(element)
        }
    }

    private fun renderDescription(element: MerchantVoucherViewModel) = with(itemView) {
        val voucherDesc = element.getMinSpendLongString(context)
        merchant_voucher_desc?.text = voucherDesc
    }

    private fun renderTitle(element: MerchantVoucherViewModel) = with(itemView) {
        val voucherTitle = context.getString(R.string.double_string_builder,
                element.getTypeString(context),
                element.getAmountShortString())

        merchant_voucher_title?.text = voucherTitle
    }

    private fun renderIcon(merchantVoucherType: Int?) = with(itemView) {
        ic_merchant_voucher?.run {
            when (merchantVoucherType) {
                MerchantVoucherTypeDef.TYPE_DISCOUNT, MerchantVoucherTypeDef.TYPE_CASHBACK -> {
                    setImageResource(R.drawable.ic_voucher_discount)
                    show()
                }
                MerchantVoucherTypeDef.TYPE_FREE_ONGKIR -> {
                    setImageResource(R.drawable.ic_voucher_shipping)
                    show()
                }
                else -> hide()
            }
        }
    }

}