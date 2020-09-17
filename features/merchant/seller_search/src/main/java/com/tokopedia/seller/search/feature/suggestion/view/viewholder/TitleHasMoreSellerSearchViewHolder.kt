package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import kotlinx.android.synthetic.main.item_title_has_more_seller_search.view.*

class TitleHasMoreSellerSearchViewHolder(view: View,
                                         private val orderSearchListener: OrderSearchListener,
                                         private val productSearchListener: ProductSearchListener,
                                         private val faqSearchListener: FaqSearchListener): AbstractViewHolder<TitleHasMoreSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_has_more_seller_search
    }

    override fun bind(element: TitleHasMoreSellerSearchUiModel?) {
        with(itemView) {
            tvMoreResultSellerSearch?.text = element?.actionTitle
            tvMoreResultSellerSearch?.setOnClickListener {
                when (element?.id) {
                    ORDER -> orderSearchListener.onOrderMoreClicked(element, adapterPosition)
                    PRODUCT -> productSearchListener.onProductMoreClicked(element, adapterPosition)
                    FAQ -> faqSearchListener.onFaqMoreClicked(element, adapterPosition)
                }
            }
        }
    }

}