package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchDoubleLineItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle


class DigitalHomePageSearchDoubleLineViewHolder (itemView: View?) :
        AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        val bind = ViewDigitalHomeSearchDoubleLineItemBinding.bind(itemView)
        with(bind){
            imgDoubleLine.apply {
                loadImageCircle(element.icon)
                adjustViewBounds = true
            }
            val spannableString = SpannableStringBuilder(element.label)
            val searchQueryIndex = element.label.indexOf(element.searchQuery, ignoreCase = true)
            if (searchQueryIndex > -1) {
                spannableString.setSpan(StyleSpan(Typeface.BOLD), searchQueryIndex, searchQueryIndex + element.searchQuery.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            tgMainTitleDoubleLine.text = spannableString
            tgMainDescDoubleLine.text = element.subtitle
        }
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_double_line_item
    }
}