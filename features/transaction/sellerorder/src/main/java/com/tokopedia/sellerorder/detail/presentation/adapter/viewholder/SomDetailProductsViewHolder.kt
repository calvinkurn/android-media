package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailProductsCardAdapter
import kotlinx.android.synthetic.main.detail_products_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener?) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
    private val somDetailProductsCardAdapter = SomDetailProductsCardAdapter(actionListener)

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailProducts) {
            if (item.dataObject.listProducts.isNotEmpty()) {
                itemView.rv_products?.visibility = View.VISIBLE
                itemView.rv_products?.apply {
                    isNestedScrollingEnabled = false
                    layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                    adapter = somDetailProductsCardAdapter
                }
                somDetailProductsCardAdapter.listProducts = item.dataObject.listProducts.toMutableList()
            } else {
                itemView.rv_products?.visibility = View.GONE
            }
        }

        val coachmarkProducts = CoachMarkItem(itemView,
                itemView.context.getString(R.string.coachmark_product),
                itemView.context.getString(R.string.coachmark_product_info))

        actionListener?.onAddedCoachMarkProducts(
                coachmarkProducts
        )
    }
}