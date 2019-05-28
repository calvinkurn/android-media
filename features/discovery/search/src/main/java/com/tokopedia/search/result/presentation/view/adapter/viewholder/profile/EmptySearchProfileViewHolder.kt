package com.tokopedia.search.result.presentation.view.adapter.viewholder.profile

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener

class EmptySearchProfileViewHolder(view: View, private val emptyStateListener: EmptyStateListener) : AbstractViewHolder<EmptySearchProfileViewModel>(view) {

    private val context: Context
    private val noResultImage: ImageView
    private val emptyTitleTextView: TextView
    private val emptyContentTextView: TextView
    private val emptyButtonItemButton: Button
    private val selectedFilterRecyclerView: RecyclerView

    init {
        noResultImage = view.findViewById<View>(R.id.no_result_image) as ImageView
        emptyTitleTextView = view.findViewById<View>(R.id.text_view_empty_title_text) as TextView
        emptyContentTextView = view.findViewById<View>(R.id.text_view_empty_content_text) as TextView
        emptyButtonItemButton = view.findViewById<View>(R.id.button_add_promo) as Button
        context = itemView.context
        selectedFilterRecyclerView = itemView.findViewById(R.id.selectedFilterRecyclerView)
    }

    override fun bind(model: EmptySearchProfileViewModel) {

        noResultImage.setImageResource(model.imageRes)
        emptyTitleTextView.text = model.title

        if (!TextUtils.isEmpty(model.content)) {
            emptyContentTextView.text = boldTextBetweenQuotes(model.content!!)
            emptyContentTextView.visibility = View.VISIBLE
        } else {
            emptyContentTextView.visibility = View.GONE
        }

        if (TextUtils.isEmpty(model.buttonText)) {
            emptyButtonItemButton.visibility = View.GONE
        } else {
            emptyButtonItemButton.text = model.buttonText
            emptyButtonItemButton.setOnClickListener {
                emptyStateListener?.onEmptyButtonClicked()
            }
            emptyButtonItemButton.visibility = View.VISIBLE
        }
    }

    private fun boldTextBetweenQuotes(text: String): CharSequence {
        val quoteSymbol = "\""
        val firstQuotePos = text.indexOf(quoteSymbol)
        val lastQuotePos = text.lastIndexOf(quoteSymbol)

        val str = SpannableStringBuilder(text)
        str.setSpan(StyleSpan(Typeface.BOLD), firstQuotePos, lastQuotePos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return str
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_list_empty_search_product
    }
}