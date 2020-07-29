package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.TxStats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.data.util.successRate
import com.tokopedia.product.detail.data.util.productThousandFormatted
import kotlinx.android.synthetic.main.partial_product_detail_visibility.view.*

class PartialAttributeInfoView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialAttributeInfoView(_view)
    }

    fun renderDataDynamicPdp(countView: Int, txStats: TxStatsDynamicPdp? = null, isSocialProofPv:Boolean) {
        with(view) {
            txt_seen.text = countView.productThousandFormatted()
            if (isSocialProofPv) {
                label_tx_success.text = context.getString(R.string.label_dibeli)
                txt_tx_success.text = txStats?.itemSoldPaymentVerified?.toIntOrNull()?.thousandFormatted()
            } else {
                label_tx_success.text = context.getString(R.string.label_success_transaction)
                txt_tx_success.text = context.getString(R.string.template_success_rate,
                        txStats?.getSuccessRateRound, txStats?.countSold?.toIntOrNull()?.thousandFormatted())
            }

            visible()
        }
    }

    fun renderWishlistCount(count: Int) {
        with(view) {
            txt_wishlist.text = count.toString()
            txt_wishlist.visible()
            label_wishlist.visible()
        }

    }
}