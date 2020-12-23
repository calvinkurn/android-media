package com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipViewHolder.Companion.KEY_UPDATE_FILTER_NAME_RECOM
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipViewHolder.Companion.KEY_UPDATE_FILTER_SELECTED_RECOM

/**
 * Created by Lukas on 03/08/20.
 */

class AnnotationFilterDiffUtil(
        private val oldList: List<RecommendationFilterChipsEntity.RecommendationFilterChip>,
        private val newList: List<RecommendationFilterChipsEntity.RecommendationFilterChip>
) : DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isActivated == newList[newItemPosition].isActivated
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val bundle = Bundle()
        if(oldList[oldItemPosition].title != newList[newItemPosition].title){
            bundle.putString(KEY_UPDATE_FILTER_NAME_RECOM, newList[newItemPosition].title)
        } else if(oldList[oldItemPosition].isActivated != newList[newItemPosition].isActivated) {
            bundle.putBoolean(KEY_UPDATE_FILTER_SELECTED_RECOM, newList[newItemPosition].isActivated)
        }
        return bundle
    }
}