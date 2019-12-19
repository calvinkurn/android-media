package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageFavoritesModel
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_favorites.view.*
import kotlinx.android.synthetic.main.layout_digital_home_search_category_item.view.*

class DigitalHomePageSearchViewHolder(itemView: View?, private val onSearchCategoryClickListener: OnSearchCategoryClickListener) :
        AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        with(itemView) {
            digital_homepage_search_category_image.loadImage(element.icon)

            // Add search query shading to category name
            val spannableString = SpannableStringBuilder(element.label)
            val searchQueryIndex = element.label.indexOf(element.searchQuery, ignoreCase = true)
            if (searchQueryIndex > -1) {
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, searchQueryIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(StyleSpan(Typeface.BOLD), searchQueryIndex + element.searchQuery.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            digital_homepage_search_category_name.text = spannableString

            setOnClickListener { onSearchCategoryClickListener.onSearchCategoryClicked(element, adapterPosition) }
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_search_category_item
    }

    interface OnSearchCategoryClickListener {
        fun onSearchCategoryClicked(category: DigitalHomePageSearchCategoryModel, position: Int)
    }
}