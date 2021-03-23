package com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import kotlinx.android.synthetic.main.item_sticky_title.view.*

class StickyTitleAdapter(
        private val stickyTitleModelList: StickyTitleModelList,
        private val stickyTitleInterface: StickyTitleInterface
): RecyclerView.Adapter<StickyTitleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickyTitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticky_title, parent, false)
        return StickyTitleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stickyTitleModelList.stickyTitles.size
    }

    override fun onBindViewHolder(holder: StickyTitleViewHolder, position: Int) {
        val isCurrentProduct = position == 0
        holder.bind(stickyTitleModelList.stickyTitles[position], stickyTitleInterface, isCurrentProduct)
    }
}

class StickyTitleViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(stickyTitle: StickyTitleModel, stickyTitleInterface: StickyTitleInterface, isCurrentProduct: Boolean) {
        val context = view.context
        view.tv_sticky_title.text = stickyTitle.title

        if (isCurrentProduct) {
            view.holder_sticky_title.cardElevation = 0f
            view.holder_sticky_title.background = ContextCompat.getDrawable(context, R.drawable.bg_sticky_current_product)
        } else {
            view.holder_sticky_title.cardElevation = 8f
            view.holder_sticky_title.setOnClickListener {
                stickyTitleInterface.onStickyTitleClick(stickyTitle)
            }
        }
    }
}

data class StickyTitleModelList(val stickyTitles: List<StickyTitleModel>)
data class StickyTitleModel(val title: String, val recommendationItem: RecommendationItem)