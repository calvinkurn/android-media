package com.tokopedia.chat_common.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.di.ChatRoomComponent
import com.tokopedia.chat_common.di.DaggerChatComponent
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chat_common.view.listener.BaseChatContract
import javax.inject.Inject
/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), BaseChatContract.View {

    @Inject
    lateinit var presenter: BaseChatPresenter

    private lateinit var chatViewState: TopChatViewState

    private lateinit var actionBox: View

    private lateinit var adapter: BaseChatAdapter


    override fun onSuccessGetChat(listChat: ArrayList<Visitable<*>>) {
        chatViewState.hideLoading()
        chatViewState.developmentView()
        chatViewState.addList(listChat)
    }

    override fun developmentView() {
        val dummyList = arrayListOf<Visitable<*>>()
        chatViewState.hideLoading()
        chatViewState.developmentView()
    }

    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shouldHandleUrlManually(url: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    companion object {

        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): BaseChatFragment {
            val fragment = TopChatRoomFragment()
            fragment.arguments = bundle
            return fragment
        }

    }


    override fun loadData(page: Int) {
        return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initView(view)
    }

    fun initView(view: View?) {
        view?.run {
            chatViewState = TopChatViewState(this)
        }
        chatViewState?.run{
            chatViewState.showLoading()
            adapter = BaseChatAdapter(adapterTypeFactory, arrayListOf())
            chatViewState.setAdapter(adapter)
        }

        hideLoading()
        presenter.getChatUseCase(arguments!!.getString("message_id"))
    }

    override fun initInjector() {

        DaggerChatComponent.builder()
                .chatRoomComponent(getComponent(ChatRoomComponent::class.java))
                .build()
                .inject(this)

    }
}