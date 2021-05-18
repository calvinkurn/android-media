package com.tokopedia.autocomplete.initialstate.productline

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class InitialStateProductListTitleViewHolder(itemView: View) : AbstractViewHolder<InitialStateProductLineTitleDataView>(itemView) {

    override fun bind(element: InitialStateProductLineTitleDataView) {
        itemView.titleTextView?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.title, getString(R.string.content_desc_titleTextView)) }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_product_list
    }
}