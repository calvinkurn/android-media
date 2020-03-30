package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.util.thousandFormatted
import kotlinx.android.synthetic.main.partial_product_social_proof_pv_2.view.*

/**
 * Created by Yehezkiel on 2020-03-18
 */

class PartialAttributeInfoPvView private constructor(private val view: View) {

    companion object {
        fun build(_view: View) = PartialAttributeInfoPvView(_view)
    }

    fun renderDataDynamicPdp(countView: Int, txStats: TxStatsDynamicPdp? = null) {
        with(view) {
            txt_seen_pv.text = countView.thousandFormatted()
            txt_tx_success_pv.text = txStats?.itemSoldPaymentVerified?.toIntOrNull()?.thousandFormatted()
            visible()
        }
    }

    fun renderWishlistCount(count: Int) {
        with(view) {
            txt_wishlist_pv.text = count.toString()
            txt_wishlist_pv.visible()
            label_wishlist_pv.visible()
        }

    }
}