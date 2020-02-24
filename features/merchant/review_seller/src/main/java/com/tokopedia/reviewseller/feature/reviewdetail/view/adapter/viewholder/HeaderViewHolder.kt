package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.HeaderModel
import kotlinx.android.synthetic.main.item_header_review_detail.view.*

class HeaderViewHolder(val view: View?): AbstractViewHolder<HeaderModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_header_review_detail
    }

    override fun bind(element: HeaderModel?) {
        itemView.rating_star_value.text = element?.ratingAvg.toString()
        itemView.total_review.setText("**1.234** Ulasan")
        itemView.review_period_filter_button.chip_container.background = ResourcesCompat.getDrawable(itemView.resources, R.drawable.order_filter_selector_chip, null)
        val chipsPadding = 40.pxToDp(itemView.resources.displayMetrics)
        val chipsTextSize = 42.pxToDp(itemView.resources.displayMetrics).toFloat()
            itemView.review_period_filter_button.chip_container.setPadding(chipsPadding, chipsPadding, chipsPadding, chipsPadding)
            itemView.review_period_filter_button.chip_text.textSize = chipsTextSize
            itemView.review_period_filter_button.setChevronClickListener {
                Log.d("RAFLI", "TES")
            }
        }
    }