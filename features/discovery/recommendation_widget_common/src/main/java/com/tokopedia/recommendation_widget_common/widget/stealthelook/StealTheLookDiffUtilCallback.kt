package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal class StealTheLookDiffUtilCallback: DiffUtil.ItemCallback<StealTheLookPageModel>() {

    override fun areItemsTheSame(
        oldItem: StealTheLookPageModel,
        newItem: StealTheLookPageModel
    ): Boolean = oldItem.page == newItem.page

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: StealTheLookPageModel,
        newItem: StealTheLookPageModel
    ): Boolean = oldItem == newItem
}

