package com.tokopedia.discovery.autocomplete.adapter

import android.support.annotation.LayoutRes
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.autocomplete.viewmodel.HotlistSearch
import com.tokopedia.discovery.search.view.adapter.ItemClickListener
import com.tokopedia.discovery.util.AutoCompleteTracking
import kotlinx.android.synthetic.main.layout_hotlist_item_autocomplete.view.*
import java.util.*

class HotlistViewHolder(val view : View, val clickListener : ItemClickListener) : AbstractViewHolder<HotlistSearch>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_hotlist_item_autocomplete
    }

    override fun bind(element: HotlistSearch) {
        ImageHandler.loadImageAndCache(
                view.img_hotlist,
                element.imageUrl)

        view.setOnClickListener {
            AutoCompleteTracking.eventClickInHotlist(
                    view.context,
                    element.searchTerm,
                    element.keyword,
                    element.eventId,
                    adapterPosition,
                    element.applink
            )

            clickListener.onItemClicked(element.applink, element.url)
        }

        val startIndex = indexOfSearchQuery(element.getKeyword(), element.getSearchTerm())
        if (startIndex == -1) {
            view.title_hotlist.text = element.keyword.toLowerCase()
        } else {
            val highlightedTitle = SpannableString(element.getKeyword())
            highlightedTitle.setSpan(TextAppearanceSpan(view.context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedTitle.setSpan(TextAppearanceSpan(view.context, R.style.searchTextHiglight),
                    startIndex + element.getSearchTerm().length,
                    element.getKeyword().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.title_hotlist.text = highlightedTitle
        }
    }

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }
}