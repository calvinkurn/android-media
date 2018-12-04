package com.tokopedia.chat_common

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).setHasFixedSize(true)
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {

    }

    override fun shouldHandleUrlManually(url: String): Boolean {

    }

    override fun onGoToWebView(attachment: String, id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleBranchIOLinkClick(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isBranchIOLink(url: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImageUploadClicked(imageUrl: String, replyTime: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}