package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.ProductAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseProductUiModel
import kotlinx.android.synthetic.main.detail_products_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsViewHolder(
        itemView: View, actionListener: SomDetailAdapter.ActionListener?
) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    private val somDetailProductsCardAdapter = BaseListAdapter<BaseProductUiModel, ProductAdapterFactoryImpl>(ProductAdapterFactoryImpl(actionListener))

    override fun bind(item: SomDetailData, position: Int) {
        with(itemView) {
            if (item.dataObject is SomDetailProducts) {
                if (item.dataObject.isTopAds || item.dataObject.isBroadcastChat) {
                    ads_title.text = if (item.dataObject.isTopAds) context.getString(R.string.sale_from_top_ads)
                    else context.getString(R.string.sale_from_broadcast_chat)
                    group_ads.show()
                } else {
                    group_ads.hide()
                }
                if (item.dataObject.listProducts.isNotEmpty()) {
                    rv_products?.visibility = View.VISIBLE
                    rv_products?.apply {
                        isNestedScrollingEnabled = false
                        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                        adapter = somDetailProductsCardAdapter
                    }
                    somDetailProductsCardAdapter.data.clear()
                    somDetailProductsCardAdapter.data.addAll(item.dataObject.listProducts)
                } else {
                    rv_products?.visibility = View.GONE
                }
            }
        }
    }
}