package com.tokopedia.topchat.chatsetting.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel

class ChatSettingListAdapter(
        baseListAdapterTypeFactory: ChatSettingTypeFactory?
) : BaseListAdapter<Visitable<*>, ChatSettingTypeFactory>(baseListAdapterTypeFactory) {

    fun isNextItemDivider(position: Int): Boolean {
        val item = visitables.getOrNull(position + 1) ?: return false
        return item is ChatSettingDividerUiModel
    }

}