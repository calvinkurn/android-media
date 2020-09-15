package com.tokopedia.topchat.chatsetting.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel

class ChatSettingListAdapter(
        baseListAdapterTypeFactory: ChatSettingTypeFactory?
) : BaseListAdapter<Visitable<*>, ChatSettingTypeFactory>(baseListAdapterTypeFactory) {

    fun shouldDrawDivider(position: Int): Boolean {
        return (!isNextItemDivider(position) && !isTitle(position)) || isLastItem(position)
    }

    private fun isTitle(position: Int): Boolean {
        val item = visitables.getOrNull(position) ?: return false
        return item is ChatSettingTitleUiModel
    }

    private fun isNextItemDivider(position: Int): Boolean {
        val item = visitables.getOrNull(position + 1) ?: return false
        return item is ChatSettingDividerUiModel
    }

    private fun isLastItem(position: Int): Boolean {
        return position == visitables.lastIndex
    }

}