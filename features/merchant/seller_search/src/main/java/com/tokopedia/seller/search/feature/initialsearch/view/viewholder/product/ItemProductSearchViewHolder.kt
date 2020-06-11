package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_product.view.*

class ItemProductSearchViewHolder(
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    fun bind(itemSellerSearchUiModel: ItemSellerSearchUiModel) {
        with(itemView) {
            ivSearchResultProduct?.setImageUrl(itemSellerSearchUiModel.imageUrl.orEmpty())
            tvSearchResultProductTitle?.text = itemSellerSearchUiModel.title
            tvSearchResultProductDesc?.text = itemSellerSearchUiModel.desc
        }
    }
}