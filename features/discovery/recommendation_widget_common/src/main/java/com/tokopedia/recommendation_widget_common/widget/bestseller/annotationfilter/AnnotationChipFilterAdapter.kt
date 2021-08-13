package com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity

/**
 * Created by Lukas on 05/11/20.
 */
class AnnotationChipFilterAdapter (private val listener: AnnotationChipListener): RecyclerView.Adapter<AnnotationChipViewHolder>(){
    private val annotationList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnotationChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(AnnotationChipViewHolder.LAYOUT, parent, false)
        return AnnotationChipViewHolder(view, listener)
    }

    override fun getItemCount(): Int = annotationList.size

    override fun onBindViewHolder(holder: AnnotationChipViewHolder, position: Int) {
        holder.bind(annotationList[position])
    }

    override fun onBindViewHolder(holder: AnnotationChipViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()) holder.bind(annotationList[position], payloads)
        else super.onBindViewHolder(holder, position, payloads)
    }

    fun submitList(list: List<RecommendationFilterChipsEntity.RecommendationFilterChip>){
        val diffCallback = AnnotationFilterDiffUtil(annotationList.toMutableList(), list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        annotationList.clear()
        annotationList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}