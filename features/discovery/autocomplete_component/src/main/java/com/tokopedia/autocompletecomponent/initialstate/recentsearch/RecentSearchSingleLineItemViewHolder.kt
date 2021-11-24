package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteSingleLineItemBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchSingleLineItemViewHolder(
    itemView: View,
    private val listener: RecentSearchListener,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_autocomplete_single_line_item
    }

    private var binding: LayoutAutocompleteSingleLineItemBinding? by viewBinding()

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconSearch(item)
        bindRecentSearchTextView(item)
        bindRemoveButton(item)
        bindListener(item)
    }

    private fun bindIconSearch(item: BaseItemInitialStateSearch) {
        binding?.iconImage?.let {
            ImageHandler.loadImage2(it, item.imageUrl, R.drawable.autocomplete_ic_time)
        }
    }

    private fun bindRecentSearchTextView(item: BaseItemInitialStateSearch) {
        binding?.singleLineTitle?.let {
            it.text = MethodChecker.fromHtml(item.title)
        }
    }

    private fun bindRemoveButton(item: BaseItemInitialStateSearch) {
        binding?.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(
                binding?.actionShortcutButton,
                item.shortcutImage,
                R.drawable.autocomplete_ic_remove
            )
        }
    }

    private fun bindListener(item: BaseItemInitialStateSearch){
        binding?.actionShortcutButton?.setOnClickListener { _ ->
            listener.onDeleteRecentSearchItem(item)
        }
        binding?.autocompleteSingleLineItem?.setOnClickListener { _ ->
            listener.onRecentSearchItemClicked(item)
        }
    }
}