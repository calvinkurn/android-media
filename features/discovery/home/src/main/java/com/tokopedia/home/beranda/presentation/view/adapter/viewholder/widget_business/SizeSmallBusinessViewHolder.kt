package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import kotlinx.android.synthetic.main.layout_template_small_business.view.*

class SizeSmallBusinessViewHolder (itemView: View?) : AbstractViewHolder<HomeWidget.ContentItemTab>(itemView) {

    override fun bind(element: HomeWidget.ContentItemTab?) {
        renderProduct(element?.name)
        renderTitle(element)
        renderSubtitle(element)
        renderLabel(element?.tagName, element?.tagType)
    }

    private fun renderProduct(name: String?) {
        itemView.productName.text = name ?: ""

        if (name.isNullOrEmpty()) {
            itemView.productName.visibility = View.GONE
        } else {
            itemView.productName.visibility = View.VISIBLE
        }
    }

    private fun renderTitle(element: HomeWidget.ContentItemTab?) {
        itemView.title.text = element?.title1st ?: ""

        if (element?.title1st.isNullOrEmpty()) {
            itemView.title.visibility = View.GONE
        } else {
            itemView.title.visibility = View.VISIBLE
        }

        if (element?.desc1st.isNullOrEmpty()) {
            itemView.subtitle.maxLines = 2
        } else {
            itemView.subtitle.maxLines = 1
        }
    }

    private fun renderSubtitle(element: HomeWidget.ContentItemTab?) {
        itemView.subtitle.text = element?.desc1st ?: ""

        if (element?.desc1st.isNullOrEmpty()) {
            itemView.subtitle.visibility = View.GONE
        } else {
            itemView.subtitle.visibility = View.VISIBLE
        }

        if (element?.title1st.isNullOrEmpty() &&
                element?.tagName.isNullOrEmpty()) {
            itemView.subtitle.maxLines = 3
        } else {
            itemView.subtitle.maxLines = 1
        }
    }

    private fun renderLabel(tagName: String?, tagType: Int?) {
        if (tagName.isNullOrEmpty()) {
            itemView.tagLine.visibility = View.GONE
        } else {
            itemView.tagLine.visibility = View.VISIBLE
            itemView.tagLine.text = tagName
            when (tagType) {
                1 -> {
                    itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_pink_label)
                    itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_pink))
                }
                2 -> {
                    itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_yellow_label)
                    itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_yellow))
                }
                3 -> {
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_green_label)
                    itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_green))
                }
                else -> {
                    itemView.tagLine.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        val LAYOUT: Int = R.layout.layout_template_small_business
    }

}
