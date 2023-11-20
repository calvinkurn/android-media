package com.tokopedia.home_component.widget.special_release

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by frenzel
 */
internal class SpecialReleaseDiffUtilCallback: DiffUtil.ItemCallback<SpecialReleaseRevampItemDataModel>() {
    override fun areItemsTheSame(
        oldItem: SpecialReleaseRevampItemDataModel,
        newItem: SpecialReleaseRevampItemDataModel
    ): Boolean = oldItem.grid.id == newItem.grid.id

    override fun areContentsTheSame(
        oldItem: SpecialReleaseRevampItemDataModel,
        newItem: SpecialReleaseRevampItemDataModel
    ): Boolean = oldItem == newItem
}
