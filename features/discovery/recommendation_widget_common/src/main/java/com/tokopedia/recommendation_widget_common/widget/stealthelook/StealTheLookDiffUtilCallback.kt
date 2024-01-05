package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal class StealTheLookDiffUtilCallback: DiffUtil.ItemCallback<StealTheLookStyleModel>() {

    override fun areItemsTheSame(
        oldItem: StealTheLookStyleModel,
        newItem: StealTheLookStyleModel
    ): Boolean = oldItem.stylePosition == newItem.stylePosition

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: StealTheLookStyleModel,
        newItem: StealTheLookStyleModel
    ): Boolean = oldItem == newItem
}

