package com.tokopedia.autocompletecomponent.initialstate.productline

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleProductListBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class InitialStateProductListTitleViewHolder(itemView: View) : AbstractViewHolder<InitialStateProductLineTitleDataView>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_product_list
    }
    private var binding: LayoutTitleProductListBinding? by viewBinding()

    override fun bind(element: InitialStateProductLineTitleDataView) {
        binding?.layoutTitleAutoComplete?.titleTextView?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.title, getString(R.string.content_desc_titleTextView)) }
    }
}