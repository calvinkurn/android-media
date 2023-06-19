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
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem.data.menu == newItem.data.menu
    }

    override fun areContentsTheSame(
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem == newItem
    }

    data class Item(
        val data: DynamicPreparationMenu,
        val isShow: Boolean
    )
}
