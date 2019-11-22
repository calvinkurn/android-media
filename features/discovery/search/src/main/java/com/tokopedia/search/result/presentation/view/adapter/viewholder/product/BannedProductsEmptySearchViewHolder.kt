package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel
import com.tokopedia.search.result.presentation.view.listener.BannedProductsRedirectToBrowserListener
import kotlinx.android.synthetic.main.search_result_banned_products_empty_search_layout.view.*

class BannedProductsEmptySearchViewHolder(
        itemView: View,
        private val bannedProductsRedirectToBrowserListener: BannedProductsRedirectToBrowserListener
): AbstractViewHolder<BannedProductsEmptySearchViewModel>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_banned_products_empty_search_layout
    }

    override fun bind(element: BannedProductsEmptySearchViewModel?) {
        if (element == null) return

        itemView.searchResultBannedProductsEmptySearchMessage?.text = MethodChecker.fromHtml(element.errorMessage)
        itemView.searchResultBannedProductsEmptySearchGoToBrowserButton?.showWithCondition(element.encriptedLiteUrl.isNotEmpty())
        itemView.searchResultBannedProductsEmptySearchGoToBrowserButton?.setOnClickListener {
            bannedProductsRedirectToBrowserListener.onGoToBrowserClicked(element.encriptedLiteUrl)
        }
    }
}