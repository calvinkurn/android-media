package com.tokopedia.discovery.autocomplete.adapter

import android.support.annotation.LayoutRes
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.autocomplete.viewmodel.TopProfileSearch
import com.tokopedia.discovery.search.view.adapter.ItemClickListener
import com.tokopedia.discovery.util.AutoCompleteTracking
import kotlinx.android.synthetic.main.layout_profile_item_autocomplete.view.*

class TopProfileViewHolder(val view: View, val clickListener : ItemClickListener) : AbstractViewHolder<TopProfileSearch>(view) {

    private val context = view.context
    private lateinit var boundedProfileSearch : TopProfileSearch

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_top_profile_item_autocomplete
    }

    override fun bind(element: TopProfileSearch) {
        boundedProfileSearch = element

        setTitle()
        setSubTitle()
        loadImageIntoProfileAvatar()
        setBadgesKOLVisibleIfKOL()
        setItemViewOnClickListener()
    }

    private fun setTitle() {
        view.title_text_view.text = getHighlightedTitle()
    }

    private fun getHighlightedTitle() : SpannableString {
        val highlightedTitle = SpannableString(boundedProfileSearch.keyword)

        highlightedTitle.setSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            0, boundedProfileSearch.keyword.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return highlightedTitle
    }

    private fun setSubTitle() {
        view.subtitle_text_view.text = boundedProfileSearch.affiliateUserName
    }

    private fun loadImageIntoProfileAvatar() {
        ImageHandler.loadImageCircle2(context, view.profile_avatar, boundedProfileSearch.imageUrl)
    }

    private fun setBadgesKOLVisibleIfKOL() {
        if (boundedProfileSearch.isKOL) {
            view.badges_kol.visibility = View.VISIBLE
        } else {
            view.badges_kol.visibility = View.GONE
        }
    }

    private fun setItemViewOnClickListener() {
        itemView.setOnClickListener {
            AutoCompleteTracking.eventClickTopProfile(itemView.context, getFormattedStringForAutoCompleteTracking())

            clickListener.onItemClicked(boundedProfileSearch.applink, boundedProfileSearch.url, false)
        }
    }

    private fun getFormattedStringForAutoCompleteTracking() : String {
        return String.format(
            "keyword: %s - profile: %s - profile id: %s - po: %s",
            boundedProfileSearch.searchTerm,
            boundedProfileSearch.keyword,
            boundedProfileSearch.peopleId,
            (adapterPosition + 1).toString()
        )
    }
}