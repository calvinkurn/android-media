package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductsItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailProductsCardAdapter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsViewHolder(itemView: View, actionListener: SomDetailAdapter.ActionListener?) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
    private val somDetailProductsCardAdapter = SomDetailProductsCardAdapter(actionListener)
    private val binding by viewBinding<DetailProductsItemBinding>()

    override fun bind(item: SomDetailData, position: Int) {
        binding?.run {
            if (item.dataObject is SomDetailProducts) {
                if (item.dataObject.isTopAds || item.dataObject.isBroadcastChat) {
                    adsTitle.text = if (item.dataObject.isTopAds) root.context.getString(R.string.sale_from_top_ads)
                    else root.context.getString(R.string.sale_from_broadcast_chat)
                    groupAds.show()
                } else {
                    groupAds.hide()
                }
                if (item.dataObject.listProducts.isNotEmpty()) {
                    rvProducts.visibility = View.VISIBLE
                    rvProducts.apply {
                        isNestedScrollingEnabled = false
                        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                        adapter = somDetailProductsCardAdapter
                    }
                    somDetailProductsCardAdapter.listProducts = item.dataObject.listProducts.toMutableList()
                } else {
                    rvProducts.visibility = View.GONE
                }
            }
        }
    }
}