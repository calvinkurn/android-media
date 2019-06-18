package com.tokopedia.discovery.autocomplete.adapter

import android.os.Build
import android.support.annotation.LayoutRes
import android.text.*
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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

        boundedProfileSearch.keyword = decodeHTML(boundedProfileSearch.keyword)
        boundedProfileSearch.affiliateUserName = decodeHTML(boundedProfileSearch.affiliateUserName)

        setSearchQueryStartIndexInKeyword()

        setTitle()
        setSubTitle()
        loadImageIntoProfileAvatar()
        setBadgesKOLVisibleIfKOL()
        setItemViewOnClickListener()
    }

    private fun decodeHTML(encodedHTML : String) : String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(encodedHTML, Html.FROM_HTML_MODE_COMPACT).toString()
        } else {
            Html.fromHtml(encodedHTML).toString()
        }
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
            view.titleTextView.text = boundedProfileSearch.keyword
        } else {
            view.titleTextView.text = getHighlightedTitle()
        }
    }

    private fun setSubTitle() {
        view.subtitleTextView.text = boundedProfileSearch.affiliateUserName
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
        ImageHandler.loadImageCircle2(context, view.profileAvatar, boundedProfileSearch.imageUrl)
    }

    private fun setBadgesKOLVisibleIfKOL() {
        if (boundedProfileSearch.isKOL) {
            view.badgesKol.visibility = View.VISIBLE
        } else {
            view.badgesKol.visibility = View.GONE
        }
    }

    private fun setItemViewOnClickListener() {
        itemView.setOnClickListener {
            AutoCompleteTracking.eventClickProfile(itemView.context, getFormattedStringForAutoCompleteTracking())

            clickListener.onItemClicked(boundedProfileSearch.applink, boundedProfileSearch.url, false)
        }
    }

    private fun getFormattedStringForAutoCompleteTracking() : String {
        return String.format(
            "keyword: %s - profile: %s - profile id: %s - po: %s",
            boundedProfileSearch.searchTerm,
            boundedProfileSearch.keyword,
            boundedProfileSearch.peopleId,
            boundedProfileSearch.positionOfType.toString()
        )
    }
}
