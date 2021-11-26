package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ARTICLES
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.databinding.ItemTitleHasMoreSellerSearchBinding
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ArticleSearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleHasMoreSellerSearchViewHolder(
    view: View,
    private val orderSearchListener: OrderSearchListener,
    private val productSearchListener: ProductSearchListener,
    private val faqSearchListener: FaqSearchListener,
    private val articleSearchListener: ArticleSearchListener
) : AbstractViewHolder<TitleHasMoreSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_has_more_seller_search
    }

    private val binding: ItemTitleHasMoreSellerSearchBinding? by viewBinding()

    override fun bind(element: TitleHasMoreSellerSearchUiModel?) {
        binding?.run {
            tvMoreResultSellerSearch.text = element?.actionTitle
            tvMoreResultSellerSearch.setOnClickListener {
                when (element?.id) {
                    ORDER -> orderSearchListener.onOrderMoreClicked(element)
                    PRODUCT -> productSearchListener.onProductMoreClicked(element)
                    FAQ -> faqSearchListener.onFaqMoreClicked(element)
                    ARTICLES -> articleSearchListener.onArticleMoreClicked(element)
                }
            }
        }
    }
}