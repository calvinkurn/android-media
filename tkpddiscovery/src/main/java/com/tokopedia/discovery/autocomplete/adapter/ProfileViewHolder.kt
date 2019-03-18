package com.tokopedia.discovery.autocomplete.adapter

import android.support.annotation.LayoutRes
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.R
import com.tokopedia.discovery.autocomplete.viewmodel.ProfileSearch
import com.tokopedia.discovery.search.view.adapter.ItemClickListener
import com.tokopedia.discovery.util.AutoCompleteTracking
import kotlinx.android.synthetic.main.layout_profile_item_autocomplete.view.*
import java.util.*

class ProfileViewHolder(val view: View, val clickListener : ItemClickListener) : AbstractViewHolder<ProfileSearch>(view) {

    private val context = view.context
    private var searchQueryStartIndexInKeyword = -1
    private lateinit var boundedProfileSearch : ProfileSearch

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_profile_item_autocomplete
    }

    override fun bind(element: ProfileSearch) {
        boundedProfileSearch = element

        setSearchQueryStartIndexInKeyword()

        setTitle()
        setSubTitle()
        loadImageIntoProfileAvatar()
        setBadgesKOLVisibleIfKOL()
        setItemViewOnClickListener()
        setIconCopyQueryOnClickListener()
    }

    private fun setSearchQueryStartIndexInKeyword() {
        val displayName = boundedProfileSearch.keyword
        val searchTerm = boundedProfileSearch.searchTerm

        searchQueryStartIndexInKeyword = if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun setTitle() {
        if (searchQueryStartIndexInKeyword == -1) {
            view.title_text_view.text = boundedProfileSearch.keyword.toLowerCase()
        } else {
            view.title_text_view.text = getHighlightedTitle()
        }
    }

    private fun setSubTitle() {
        view.subtitle_text_view.text = boundedProfileSearch.affiliateUserName
    }

    private fun getHighlightedTitle() : SpannableString {
        val highlightedTitle = SpannableString(boundedProfileSearch.keyword)

        highlightTitleBeforeKeyword(highlightedTitle)

        highlightTitleAfterKeyword(highlightedTitle)

        return highlightedTitle
    }

    private fun highlightTitleBeforeKeyword(highlightedTitle: SpannableString) {
        highlightedTitle.setSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            0, searchQueryStartIndexInKeyword, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun highlightTitleAfterKeyword(highlightedTitle: SpannableString) {
        val highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + boundedProfileSearch.searchTerm.length
        val highlightAfterKeywordEndIndex = boundedProfileSearch.keyword.length

        highlightedTitle.setSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            highlightAfterKeywordStartIndex, highlightAfterKeywordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun loadImageIntoProfileAvatar() {
        Glide.with(context).load(boundedProfileSearch.imageUrl).into(view.profile_avatar)
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
            AutoCompleteTracking.eventClickProfile(itemView.context, getFormattedStringForAutoCompleteTracking())

            clickListener.onItemClicked(boundedProfileSearch.applink, boundedProfileSearch.url)
        }
    }

    private fun getFormattedStringForAutoCompleteTracking() : String {
        return String.format(
            "keyword: %s - profile: %s - position: %s - page: %s",
            boundedProfileSearch.searchTerm,
            boundedProfileSearch.keyword,
            (adapterPosition + 1).toString(),
            boundedProfileSearch.applink
        )
    }

    private fun setIconCopyQueryOnClickListener() {
        view.icon_copy_query.setOnClickListener {
            clickListener.copyTextToSearchView(boundedProfileSearch.keyword)
        }
    }
}