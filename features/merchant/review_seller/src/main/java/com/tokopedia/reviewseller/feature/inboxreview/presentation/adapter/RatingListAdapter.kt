package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import kotlinx.android.synthetic.main.item_rating_filter_bottom_sheet.view.*

class RatingListAdapter(private val ratingListListener: RatingListListener) : RecyclerView.Adapter<RatingListAdapter.RatingListViewHolder>() {

    var ratingFilterList: MutableList<ListItemRatingWrapper> = mutableListOf()

    fun setRatingFilter(newOptionList: MutableList<ListItemRatingWrapper>) {
        this.ratingFilterList = newOptionList
        notifyDataSetChanged()
    }

    fun updateRatingFilter(updatedState: Boolean, position: Int) {
        val isSelected = ratingFilterList.getOrNull(position)
        ratingFilterList.map { filterItemUiModel ->
            if (isSelected == filterItemUiModel) {
                filterItemUiModel.isSelected = updatedState
            }
        }
        notifyItemChanged(position)
    }

    fun resetRatingFilter() {
        ratingFilterList.mapIndexed { index, filterItemUiModel ->
            if (filterItemUiModel.isSelected) {
                filterItemUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating_filter_bottom_sheet, parent, false)
        return RatingListViewHolder(view, ratingListListener)
    }

    override fun getItemCount(): Int {
        return ratingFilterList.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: RatingListViewHolder, position: Int) {
        ratingFilterList[position].let { holder.bind(it) }
    }

    class RatingListViewHolder(itemView: View, private val ratingListListener: RatingListListener) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun bind(data: ListItemRatingWrapper) {
            with(itemView) {
                rating_checkbox.setOnCheckedChangeListener(null)
                rating_checkbox.isChecked = data.isSelected
                rating_star_label.text = data.sortValue

                rating_checkbox.setOnCheckedChangeListener { _, isChecked ->
                    ratingListListener.onItemRatingClicked(data.sortValue, isChecked, adapterPosition)
                }
            }
        }
    }
}