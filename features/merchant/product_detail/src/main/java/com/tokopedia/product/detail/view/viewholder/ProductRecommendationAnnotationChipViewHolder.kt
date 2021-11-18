package com.tokopedia.product.detail.view.viewholder

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicAnnotationChipBinding
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by Lukas on 03/08/20.
 */

class ProductRecommendationAnnotationChipViewHolder (
        private val view: View,
        private val listener: ProductRecommendationViewHolder.AnnotationChipListener
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemDynamicAnnotationChipBinding.bind(view)

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
        val LAYOUT = R.layout.item_dynamic_annotation_chip
        const val KEY_UPDATE_FILTER_SELECTED_RECOM = "KEY_UPDATE_FILTER_SELECTED_RECOM"
        const val KEY_UPDATE_FILTER_NAME_RECOM = "KEY_UPDATE_FILTER_NAME_RECOM"
    }
}