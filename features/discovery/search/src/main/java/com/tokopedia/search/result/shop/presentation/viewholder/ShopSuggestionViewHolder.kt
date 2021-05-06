package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.model.ShopSuggestionDataView
import com.tokopedia.unifyprinciples.Typography

internal class ShopSuggestionViewHolder(itemView: View): AbstractViewHolder<ShopSuggestionDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_suggestion_layout
    }

    private var suggestionText: Typography? = null

    override fun bind(element: ShopSuggestionDataView?) {
        element ?: return

        suggestionText = itemView.findViewById(R.id.shopSuggestionTextView)
        suggestionText?.text = MethodChecker.fromHtml(element.text)
    }
}