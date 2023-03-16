package com.tokopedia.shop.product.view.viewholder

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopProductSearchSuggestionViewBinding
import com.tokopedia.shop.product.view.datamodel.ShopProductSearchResultSuggestionUiModel
import com.tokopedia.shop.product.view.listener.ShopProductSearchSuggestionListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.net.URLDecoder

/**
 * Created by Rafli Syam 13/09/2021
 */
class ShopProductSearchResultSuggestionViewHolder(
    val itemView: View,
    val listener: ShopProductSearchSuggestionListener?
) : AbstractViewHolder<ShopProductSearchResultSuggestionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_search_suggestion_view

        private const val KEYWORD_PARAMS = "q="
        private const val QUERY_SPLIT_DELIMITER = "&rf="
        private const val ENCODING_FORMAT = "UTF-8"
        private const val DEFAULT_FIRST_INDEX = 0
    }

    private val viewBinding: ItemShopProductSearchSuggestionViewBinding? by viewBinding()
    private val suggestionTextView: Typography? = viewBinding?.suggestionText

    override fun bind(element: ShopProductSearchResultSuggestionUiModel) {
        suggestionTextView?.text = getClickableSpanText(element.suggestionText, element.queryString)
        suggestionTextView?.movementMethod = LinkMovementMethod.getInstance()
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

    private fun getClickableSpanText(suggestionTextHtmlString: String, keywordQueryString: String): SpannableString {
        val suggestionTextSpannedString = SpannableString(MethodChecker.fromHtml(suggestionTextHtmlString))
        val keyword = extractKeyword(queryString = keywordQueryString)
        val keywordWithQuote = "\"$keyword\""
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                searchProductsByKeyword(keyword)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        val indexOfKeyword = suggestionTextSpannedString.indexOf(keywordWithQuote)
        val startClickedIndex = indexOfKeyword.takeIf { it.isMoreThanZero() } ?: DEFAULT_FIRST_INDEX
        val endClickedIndex = if (indexOfKeyword.isMoreThanZero()) {
            (indexOfKeyword + keywordWithQuote.length)
        } else {
            suggestionTextSpannedString.lastIndex
        }
        suggestionTextSpannedString.setSpan(
            clickableSpan,
            startClickedIndex,
            endClickedIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return suggestionTextSpannedString
    }

    private fun searchProductsByKeyword(keyword: String) {
        listener?.onSearchProductsBySuggestedKeyword(keyword)
    }
}
