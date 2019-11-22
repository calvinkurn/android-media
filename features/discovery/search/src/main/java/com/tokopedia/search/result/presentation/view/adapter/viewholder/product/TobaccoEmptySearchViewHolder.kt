package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.TobaccoEmptySearchViewModel
import com.tokopedia.search.result.presentation.view.listener.TobaccoRedirectToBrowserListener
import kotlinx.android.synthetic.main.search_result_tobacco_empty_search_layout.view.*

class TobaccoEmptySearchViewHolder(
        itemView: View,
        private val tobaccoRedirectToBrowserListener: TobaccoRedirectToBrowserListener
): AbstractViewHolder<TobaccoEmptySearchViewModel>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_tobacco_empty_search_layout
    }

    override fun bind(element: TobaccoEmptySearchViewModel?) {
        if (element == null) return

        itemView.searchResultTobaccoEmptySearchMessage?.text = MethodChecker.fromHtml(element.errorMessage)
        itemView.searchResultTobaccoEmptySearchGoToBrowserButton?.showWithCondition(element.encriptedLiteUrl.isNotEmpty())
        itemView.searchResultTobaccoEmptySearchGoToBrowserButton?.setOnClickListener {
            tobaccoRedirectToBrowserListener.onGoToBrowserClicked(element.encriptedLiteUrl)
        }
    }
}