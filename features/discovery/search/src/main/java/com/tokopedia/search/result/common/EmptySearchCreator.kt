package com.tokopedia.search.result.common

import android.content.Context
import android.support.annotation.DrawableRes
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel

open class EmptySearchCreator(
        private val context: Context
) {

    open fun createEmptySearchViewModel(query: String, isFilterActive: Boolean, sectionTitle: String): EmptySearchViewModel {
        val emptySearchViewModel = EmptySearchViewModel()

        emptySearchViewModel.imageRes = getEmptySearchImageRes()
        emptySearchViewModel.title = getEmptySearchTitle(context, sectionTitle)
        emptySearchViewModel.content = getEmptySearchContent(query, isFilterActive)
        emptySearchViewModel.buttonText = getEmptySearchButtonText(isFilterActive)

        return emptySearchViewModel
    }

    @DrawableRes
    private fun getEmptySearchImageRes(): Int {
        return R.drawable.ic_empty_search
    }

    private fun getEmptySearchTitle(context: Context, sectionTitle: String): String {
        val templateText = context.getString(R.string.msg_empty_search_with_filter_1)
        return String.format(templateText, sectionTitle)
    }

    private fun getEmptySearchContent(query: String, isFilterActive: Boolean): String {
        return if (isFilterActive) {
            String.format(context.getString(R.string.msg_empty_search_with_filter_2), query)
        } else {
            String.format(context.getString(R.string.empty_search_content_template), query)
        }
    }

    private fun getEmptySearchButtonText(isFilterActive: Boolean): String {
        return if (isFilterActive) {
            ""
        } else {
            context.getString(R.string.empty_search_button_text)
        }
    }
}
