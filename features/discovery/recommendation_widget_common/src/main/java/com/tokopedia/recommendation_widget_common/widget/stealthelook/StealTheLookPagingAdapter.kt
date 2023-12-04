package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

internal class StealTheLookPagingAdapter(
    private val trackingQueue: TrackingQueue
): ListAdapter<StealTheLookPageModel, StealTheLookPageViewHolder>(StealTheLookDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StealTheLookPageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(recommendation_widget_commonR.layout.recommendation_widget_steal_the_look_page, parent, false)

        return StealTheLookPageViewHolder(view, trackingQueue)
    }

    override fun onBindViewHolder(holder: StealTheLookPageViewHolder, position: Int) {
        try { holder.bind(getItem(position)) }
        catch (_: Exception) { }
    }

    override fun onViewRecycled(holder: StealTheLookPageViewHolder) {
        holder.onViewRecycled()
    }

    fun getItemAt(position: Int): StealTheLookPageModel? {
        if (position !in currentList.indices) return null
        return getItem(position)
    }
}
