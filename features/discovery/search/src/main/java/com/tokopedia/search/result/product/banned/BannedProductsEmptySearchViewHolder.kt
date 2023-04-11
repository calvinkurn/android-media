package com.tokopedia.search.result.product.banned

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultBannedProductsEmptySearchLayoutBinding
import com.tokopedia.search.result.product.banned.BannedProductsEmptySearchDataView
import com.tokopedia.utils.view.binding.viewBinding

class BannedProductsEmptySearchViewHolder(itemView: View): AbstractViewHolder<BannedProductsEmptySearchDataView>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_banned_products_empty_search_layout
    }

    private var binding: SearchResultBannedProductsEmptySearchLayoutBinding? by viewBinding()

    override fun bind(element: BannedProductsEmptySearchDataView?) {
        if (element == null) return

        binding?.searchResultBannedProductsEmptySearchMessage?.text = MethodChecker.fromHtml(element.errorMessage)
    }
}
