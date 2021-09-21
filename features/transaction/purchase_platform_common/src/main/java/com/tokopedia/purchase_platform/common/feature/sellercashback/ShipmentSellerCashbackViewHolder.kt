package com.tokopedia.purchase_platform.common.feature.sellercashback

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.R

class ShipmentSellerCashbackViewHolder(itemView: View, private val sellerCashbackListener: SellerCashbackListener) : RecyclerView.ViewHolder(itemView) {
    private val tvSellerCashbackPrice: TextView = itemView.findViewById(R.id.tv_seller_cashback_price)
    private val rlContainer: RelativeLayout = itemView.findViewById(R.id.rl_container)

    fun bindViewHolder(shipmentSellerCashbackModel: ShipmentSellerCashbackModel) {
        if (shipmentSellerCashbackModel.isVisible) {
            tvSellerCashbackPrice.text = shipmentSellerCashbackModel.sellerCashbackFmt
            rlContainer.show()
        } else {
            rlContainer.gone()
        }
        sellerCashbackListener.onCashbackUpdated(shipmentSellerCashbackModel.sellerCashback)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_SELLER_CASHBACK = R.layout.item_seller_cashback
    }

}