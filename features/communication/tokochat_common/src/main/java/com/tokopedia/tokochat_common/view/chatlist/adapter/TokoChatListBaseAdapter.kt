package com.tokopedia.tokochat_common.view.chatlist.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.chatlist.adapter.delegate.TokoChatListItemDelegate
import com.tokopedia.tokochat_common.view.chatlist.listener.TokoChatListItemListener

class TokoChatListBaseAdapter(
    itemListener: TokoChatListItemListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatListItemDelegate(itemListener))
    }
}
