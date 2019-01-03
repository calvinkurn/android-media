package com.tokopedia.chatbot.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(adapterTypeFactory: ChatbotTypeFactoryImpl)
    : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterTypeFactory) {

    override fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) {
                visitables.add(0, loadingMoreModel)
            } else {
                visitables.add(0, loadingModel)
            }
            notifyItemInserted(0)
        }
    }
}