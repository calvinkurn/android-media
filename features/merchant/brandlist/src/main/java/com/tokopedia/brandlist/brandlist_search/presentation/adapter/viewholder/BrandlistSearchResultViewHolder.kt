package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel
import kotlinx.android.synthetic.main.item_search_result.view.*
import java.util.*


class BrandlistSearchResultViewHolder(view: View): AbstractViewHolder<BrandlistSearchResultViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_result
    }

    private val context: Context = itemView.context
    private val imgBrandLogo = itemView.iv_brand_logo
    private val imgBrandImage = itemView.iv_brand_image
    private val txtBrandName = itemView.tv_brand_name

    override fun bind(element: BrandlistSearchResultViewModel) {
        processString(element.name, element.searchQuery)
        bindData(element.defaultUrl, element.logoUrl)
    }

    private fun bindData(brandLogoUrl: String, brandImageUrl: String) {
        ImageHandler.loadImage(context, imgBrandLogo, brandLogoUrl, null)
        if(brandImageUrl.isNotBlank()) {
            ImageHandler.loadImage(context, imgBrandImage, brandImageUrl, null)
        } else {
            imgBrandImage.visibility = View.GONE
        }
    }

    private fun processString(name: String, searchQuery: String) {
        val startIndex = indexOfSearchQuery(name, searchQuery)
        if (startIndex == -1) {
            txtBrandName.text = name
        } else {
            val highlightedTitle = SpannableString(name)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    startIndex + searchQuery.length,
                    name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txtBrandName.text = highlightedTitle
        }
    }

    // To Do
    // Should be implemented tracking when card is clicked
    // clickBrandOnSearchBox(categoryTab: String, optionalParam: String, isLogin: Boolean, keyword: String)
    // optionalParam = "optional param"
    // categoryTab = "categoryTab"

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }
}