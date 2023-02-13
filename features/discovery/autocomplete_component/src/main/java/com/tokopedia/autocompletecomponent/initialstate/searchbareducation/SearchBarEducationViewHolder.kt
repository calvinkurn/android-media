package com.tokopedia.autocompletecomponent.initialstate.searchbareducation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutSearchbarEducationBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class SearchBarEducationViewHolder(
    itemView: View,
    private val listener: SearchBarEducationListener
): AbstractViewHolder<SearchBarEducationDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_searchbar_education
    }

    private var binding: LayoutSearchbarEducationBinding? by viewBinding()

    override fun bind(data: SearchBarEducationDataView) {
        bindClick(data)
        bindTitle(data)
        bindLabelAction(data)
        bindIcon(data)
    }

    private fun bindClick(data: SearchBarEducationDataView) {
        binding?.autocompleteSearchBarEducationContainer?.setOnClickListener {
            listener.onSearchBarEducationClick(data.item)
        }
    }

    private fun bindTitle(data: SearchBarEducationDataView) {
        binding?.autocompleteSearchBarEducationTitle?.text = data.header
    }

    private fun bindLabelAction(data: SearchBarEducationDataView) {
        binding?.autocompleteSearchBarEducationAction?.text = data.labelAction
    }

    private fun bindIcon(data: SearchBarEducationDataView) {
        binding?.autocompleteSearchBarEducationIcon?.loadImage(data.item.imageUrl)
    }
}