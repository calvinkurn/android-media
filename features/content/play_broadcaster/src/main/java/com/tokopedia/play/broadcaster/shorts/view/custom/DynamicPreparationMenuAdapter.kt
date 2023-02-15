package com.tokopedia.play.broadcaster.shorts.view.custom

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
class DynamicPreparationMenuAdapter(
    onClick: (DynamicPreparationMenu) -> Unit
) : BaseDiffUtilAdapter<DynamicPreparationMenuAdapter.Item>() {

    init {
        delegatesManager
            .addDelegate(DynamicPreparationMenuAdapterDelegate(onClick))
    }

    override fun areItemsTheSame(
        oldItem: DynamicPreparationMenuAdapter.Item,
        newItem: DynamicPreparationMenuAdapter.Item
    ): Boolean {
        return oldItem.data.menuId == newItem.data.menuId
    }

    override fun areContentsTheSame(
        oldItem: DynamicPreparationMenuAdapter.Item,
        newItem: DynamicPreparationMenuAdapter.Item
    ): Boolean {
        return oldItem == newItem
    }

    data class Item(
        val data: DynamicPreparationMenu,
        val isShow: Boolean
    )
}
