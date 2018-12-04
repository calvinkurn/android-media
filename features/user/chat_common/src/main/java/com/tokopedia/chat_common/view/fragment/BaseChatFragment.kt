package com.tokopedia.chat_common

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener

/**
 * @author by nisie on 23/11/18.
 */
abstract class BaseChatFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
                                , ImageAnnouncementListener, ChatLinkHandlerListener
                                , ImageUploadListener, ProductAttachmentListener{


    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return BaseChatTypeFactoryImpl(this,
                this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        return
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

}