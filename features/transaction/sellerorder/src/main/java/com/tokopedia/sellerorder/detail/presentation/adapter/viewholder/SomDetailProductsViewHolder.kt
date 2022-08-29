package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductsItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsViewHolder(
    itemView: View?
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.detail_products_item
    }

    private val binding by viewBinding<DetailProductsItemBinding>()

    override fun bind(item: SomDetailData) {
        binding?.run {
            if (item.dataObject is SomDetailProducts) {
                if (item.dataObject.isTopAds || item.dataObject.isBroadcastChat) {
                    adsTitle.text =
                        if (item.dataObject.isTopAds) root.context.getString(R.string.sale_from_top_ads)
                        else root.context.getString(R.string.sale_from_broadcast_chat)
                    groupAds.show()
                } else {
                    groupAds.hide()
                }
            }
        }
    }
}