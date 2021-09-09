package com.tokopedia.seller.search.feature.suggestion.view.viewholder.articles

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ArticleSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_article.view.*

class ItemArticleSearchViewHolder(
    view: View,
    private val articleSearchListener: ArticleSearchListener
) : AbstractViewHolder<ArticleSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_result_article
    }

    override fun bind(element: ArticleSellerSearchUiModel) {
        with(itemView) {
            ivSearchResultArticle.setImageUrl(element.imageUrl.orEmpty())
            tvSearchResultArticleTitle.bindTitleText(
                element.title.orEmpty(),
                element.keyword.orEmpty()
            )
            tvSearchResultArticleDesc?.text = element.desc
            setOnClickListener {
                articleSearchListener.onArticleItemClicked(element, adapterPosition)
            }
        }
    }
}