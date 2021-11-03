package com.tokopedia.review.feature.inboxreview.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemRatingFilterBottomSheetBinding
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper

class RatingListAdapter(private val ratingListListener: RatingListListener) : RecyclerView.Adapter<RatingListAdapter.RatingListViewHolder>() {

    var ratingFilterList: MutableList<ListItemRatingWrapper> = mutableListOf()

    fun setRatingFilter(newOptionList: MutableList<ListItemRatingWrapper>) {
        val callBack = RatingListDiffUtil(ratingFilterList, newOptionList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.ratingFilterList.clear()
        this.ratingFilterList.addAll(newOptionList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateRatingFilter(updatedState: Boolean, position: Int) {
        val isSelected = ratingFilterList.getOrNull(position)
        ratingFilterList.map { filterItemUiModel ->
            if (isSelected == filterItemUiModel) {
                filterItemUiModel.isSelected = updatedState
                notifyItemChanged(position)
            }
        }
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

    override fun onBindViewHolder(holder: RatingListViewHolder, position: Int) {
        ratingFilterList[position].let { holder.bind(it) }
    }

    class RatingListViewHolder(itemView: View, private val ratingListListener: RatingListListener) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemRatingFilterBottomSheetBinding.bind(itemView)

        fun bind(data: ListItemRatingWrapper) {
            with(binding) {
                ratingCheckbox.setOnCheckedChangeListener(null)
                ratingCheckbox.isChecked = data.isSelected
                ratingStarLabel.text = data.sortValue

                ratingCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    ratingListListener.onItemRatingClicked(data.sortValue, isChecked, adapterPosition)
                }

                root.setOnClickListener {
                    ratingCheckbox.isChecked = !ratingCheckbox.isChecked
                }
            }
        }
    }
}