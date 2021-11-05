package com.tokopedia.recommendation_widget_common.widget.carousel.chips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip

/**
 * Created by yfsx on 05/11/21.
 */
class RecomChipsAdapter (private val listener: RecomChipsListener): RecyclerView.Adapter<RecomChipsViewHolder>() {
    private val annotationList = mutableListOf<AnnotationChip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomChipsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecomChipsViewHolder.LAYOUT, parent, false)
        return RecomChipsViewHolder(view, listener)
    }

    override fun getItemCount(): Int = annotationList.size

    override fun onBindViewHolder(holder: RecomChipsViewHolder, position: Int) {
        holder.bind(annotationList[position])
    }

    override fun onBindViewHolder(holder: RecomChipsViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) holder.bind(annotationList[position], payloads)
        else super.onBindViewHolder(holder, position, payloads)
    }

    fun submitList(list: List<AnnotationChip>) {
        val diffCallback = RecomChipsDiffUtil(annotationList.toMutableList(), list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        annotationList.clear()
        annotationList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getSelectedChip() =
        annotationList.find { it.recommendationFilterChip.isActivated }?.recommendationFilterChip

    interface RecomChipsListener {
        fun onFilterAnnotationClicked(annotationChip: AnnotationChip, position: Int)
    }
}