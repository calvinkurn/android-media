package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopProductSearchResultSuggestionUiModel
import com.tokopedia.shop.product.view.listener.ShopProductSearchSuggestionListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import java.net.URLDecoder

/**
 * Created by Rafli Syam 13/09/2021
 */
class ShopProductSearchResultSuggestionViewHolder(
        val itemView: View,
        val listener: ShopProductSearchSuggestionListener?
): AbstractViewHolder<ShopProductSearchResultSuggestionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_search_suggestion_view

        private const val KEYWORD_PARAMS = "q="
        private const val QUERY_SPLIT_DELIMITER = "&"
        private const val ENCODING_FORMAT = "UTF-8"
    }

    private val suggestionTextView: Typography? = itemView.findViewById(R.id.suggestion_text)

    override fun bind(element: ShopProductSearchResultSuggestionUiModel) {
        val suggestionTextString = HtmlLinkHelper(itemView.context, element.suggestionText)
        val keyword = extractKeyword(queryString = element.queryString)
        suggestionTextView?.text = suggestionTextString.spannedString
        suggestionTextView?.setOnClickListener {
            searchProductsByKeyword(keyword)
        }
    }

    /**
     * Extract suggestion keyword from query string
     * example BE response "q=keyword&rf=true"
     * @return keyword
     */
    private fun extractKeyword(queryString: String): String {
        val decodedQueryString = URLDecoder.decode(queryString, ENCODING_FORMAT)
        return decodedQueryString.split(QUERY_SPLIT_DELIMITER).let {
            if (it.size.isMoreThanZero()) {
                it.first().replace(KEYWORD_PARAMS, "")
            } else {
                ""
            }
        }
    }

    private fun searchProductsByKeyword(keyword: String) {
        listener?.onSearchProductsBySuggestedKeyword(keyword)
    }

}