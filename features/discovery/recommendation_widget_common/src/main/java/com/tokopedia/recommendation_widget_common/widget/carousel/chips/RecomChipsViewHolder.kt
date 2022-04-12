package com.tokopedia.recommendation_widget_common.widget.carousel.chips

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomAnnotationChipBinding
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by yfsx on 05/11/21.
 */
class RecomChipsViewHolder (
    private val view: View,
    private val listener: RecomChipsAdapter.RecomChipsListener
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRecomAnnotationChipBinding.bind(view)

    fun bind(element: AnnotationChip) {
        binding.annotationChip.chipText = element.recommendationFilterChip.name
        binding.annotationChip.chipType = if(element.recommendationFilterChip.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
    }

    fun bind(element: AnnotationChip, payload: List<Any>){
        if(payload.isEmpty()) return
        val bundle = payload.first() as Bundle
        if(bundle.containsKey(KEY_UPDATE_FILTER_SELECTED_RECOM)){
            binding.annotationChip.chipType = if(element.recommendationFilterChip.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        } else if(bundle.containsKey(KEY_UPDATE_FILTER_NAME_RECOM)){
            binding.annotationChip.chipText = element.recommendationFilterChip.name
        }
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
    }


    companion object{
        val LAYOUT = R.layout.item_recom_annotation_chip
        const val KEY_UPDATE_FILTER_SELECTED_RECOM = "KEY_UPDATE_FILTER_SELECTED_RECOM"
        const val KEY_UPDATE_FILTER_NAME_RECOM = "KEY_UPDATE_FILTER_NAME_RECOM"
    }
}