package com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.databinding.AnnotationFilterChipViewHolderBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Lukas on 05/11/20.
 */
class AnnotationChipViewHolder(
        private val view: View,
        private val listener: AnnotationChipListener
) : RecyclerView.ViewHolder(view) {

    private var binding: AnnotationFilterChipViewHolderBinding? by viewBinding()
    fun bind(element: RecommendationFilterChipsEntity.RecommendationFilterChip) {
        binding?.annotationChip?.chipText = element.title
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
        if(element.isActivated) {
            binding?.annotationChip?.chipType = ChipsUnify.TYPE_SELECTED
        } else{
            binding?.annotationChip?.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    fun bind(element: RecommendationFilterChipsEntity.RecommendationFilterChip, payload: List<Any>){
        if(payload.isEmpty()) return
        val bundle = payload.first() as Bundle
        if(bundle.containsKey(KEY_UPDATE_FILTER_SELECTED_RECOM)){
            binding?.annotationChip?.chipType = if(element.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        } else if(bundle.containsKey(KEY_UPDATE_FILTER_NAME_RECOM)){
            binding?.annotationChip?.chipText = element.title
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