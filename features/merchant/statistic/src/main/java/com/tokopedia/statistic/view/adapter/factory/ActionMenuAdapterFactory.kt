package com.tokopedia.statistic.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.view.adapter.viewholder.ActionMenuViewHolder
import com.tokopedia.statistic.view.model.ActionMenuUiModel

/**
 * Created By @ilhamsuaib on 14/02/21
 */

class ActionMenuAdapterFactory(
        private val pageName: String,
        private val userId: String,
        private val onItemClick: (menu: ActionMenuUiModel) -> Unit
) : BaseAdapterTypeFactory() {

    fun type(model: ActionMenuUiModel): Int = ActionMenuViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ActionMenuViewHolder.RES_LAYOUT -> ActionMenuViewHolder(parent, pageName, userId, onItemClick)
            else -> super.createViewHolder(parent, type)
        }
    }
}