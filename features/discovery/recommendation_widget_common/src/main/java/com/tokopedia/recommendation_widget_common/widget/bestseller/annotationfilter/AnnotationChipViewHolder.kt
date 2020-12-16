package com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.annotation_filter_chip_view_holder.view.*

/**
 * Created by Lukas on 05/11/20.
 */
class AnnotationChipViewHolder(
        private val view: View,
        private val listener: AnnotationChipListener
) : RecyclerView.ViewHolder(view) {

    fun bind(element: RecommendationFilterChipsEntity.RecommendationFilterChip) {
        view.annotation_chip.chipText = element.title
        view.annotation_chip.chipType = if(element.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
    }

    fun bind(element: RecommendationFilterChipsEntity.RecommendationFilterChip, payload: List<Any>){
        if(payload.isEmpty()) return
        val bundle = payload.first() as Bundle
        if(bundle.containsKey(KEY_UPDATE_FILTER_SELECTED_RECOM)){
            view.annotation_chip.chipType = if(element.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        } else if(bundle.containsKey(KEY_UPDATE_FILTER_NAME_RECOM)){
            view.annotation_chip.chipText = element.title
        }
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
    }


    companion object{
        val LAYOUT = R.layout.annotation_filter_chip_view_holder
        const val KEY_UPDATE_FILTER_SELECTED_RECOM = "KEY_UPDATE_FILTER_SELECTED_RECOM"
        const val KEY_UPDATE_FILTER_NAME_RECOM = "KEY_UPDATE_FILTER_NAME_RECOM"
    }
}