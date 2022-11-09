package com.tokopedia.play.broadcaster.shorts.view.custom

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
class DynamicPreparationMenuAdapter(
    onClick: (DynamicPreparationMenu) -> Unit
) : BaseDiffUtilAdapter<DynamicPreparationMenu>() {

    init {
        delegatesManager
            .addDelegate(DynamicPreparationMenuAdapterDelegate(onClick))
    }

    override fun areItemsTheSame(
        oldItem: DynamicPreparationMenu,
        newItem: DynamicPreparationMenu
    ): Boolean {
        return oldItem.menuId == newItem.menuId
    }

    override fun areContentsTheSame(
        oldItem: DynamicPreparationMenu,
        newItem: DynamicPreparationMenu
    ): Boolean {
        return oldItem == newItem
    }
}
