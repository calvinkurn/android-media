package com.tokopedia.product.detail.view.viewholder

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_dynamic_annotation_chip.view.*

/**
 * Created by Lukas on 03/08/20.
 */

class ProductRecommendationAnnotationChipViewHolder (
        private val view: View,
        private val listener: ProductRecommendationViewHolder.AnnotationChipListener
) : RecyclerView.ViewHolder(view) {

    fun bind(element: AnnotationChip) {
        view.annotation_chip.chipText = element.recommendationFilterChip.name
        view.annotation_chip.chipType = if(element.recommendationFilterChip.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        view.setOnClickListener {
            listener.onFilterAnnotationClicked(element, adapterPosition)
        }
    }

    fun bind(element: AnnotationChip, payload: List<Any>){
        if(payload.isEmpty()) return
        val bundle = payload.first() as Bundle
        if(bundle.containsKey(KEY_UPDATE_FILTER_SELECTED_RECOM)){
            view.annotation_chip.chipType = if(element.recommendationFilterChip.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        } else if(bundle.containsKey(KEY_UPDATE_FILTER_NAME_RECOM)){
            view.annotation_chip.chipText = element.recommendationFilterChip.name
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