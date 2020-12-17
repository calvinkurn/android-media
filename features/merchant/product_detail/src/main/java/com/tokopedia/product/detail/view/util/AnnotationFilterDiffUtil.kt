package com.tokopedia.product.detail.view.util

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationAnnotationChipViewHolder.Companion.KEY_UPDATE_FILTER_NAME_RECOM
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationAnnotationChipViewHolder.Companion.KEY_UPDATE_FILTER_SELECTED_RECOM
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip

/**
 * Created by Lukas on 03/08/20.
 */

class AnnotationFilterDiffUtil(
        private val oldList: List<AnnotationChip>,
        private val newList: List<AnnotationChip>
) : DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].recommendationFilterChip.name == newList[newItemPosition].recommendationFilterChip.name
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].recommendationFilterChip.isActivated == newList[newItemPosition].recommendationFilterChip.isActivated
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val bundle = Bundle()
        if(oldList[oldItemPosition].recommendationFilterChip.name != newList[newItemPosition].recommendationFilterChip.name){
            bundle.putString(KEY_UPDATE_FILTER_NAME_RECOM, newList[newItemPosition].recommendationFilterChip.name)
        } else if(oldList[oldItemPosition].recommendationFilterChip.isActivated != newList[newItemPosition].recommendationFilterChip.isActivated) {
            bundle.putBoolean(KEY_UPDATE_FILTER_SELECTED_RECOM, newList[newItemPosition].recommendationFilterChip.isActivated)
        }
        return bundle
    }
}