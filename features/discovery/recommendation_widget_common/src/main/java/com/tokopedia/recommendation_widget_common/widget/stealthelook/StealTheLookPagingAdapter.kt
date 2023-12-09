package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

internal class StealTheLookPagingAdapter: ListAdapter<StealTheLookStyleModel, StealTheLookStyleViewHolder>(StealTheLookDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StealTheLookStyleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(recommendation_widget_commonR.layout.recommendation_widget_steal_the_look_page, parent, false)
        return StealTheLookStyleViewHolder(view)
    }

    override fun onBindViewHolder(holder: StealTheLookStyleViewHolder, position: Int) {
        try { holder.bind(getItem(position)) }
        catch (_: Exception) { }
    }

    override fun onViewRecycled(holder: StealTheLookStyleViewHolder) {
        holder.onViewRecycled()
    }
}
