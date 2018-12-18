package com.tokopedia.topchat.revamp.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.topchat.revamp.di.DaggerChatComponent
import com.tokopedia.topchat.revamp.di.DaggerTopChatRoomComponent
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.topchat.revamp.presenter.TopChatRoomPresenter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View {

    @Inject
    lateinit var presenter: TopChatRoomPresenter

    lateinit var chatViewState: TopChatViewState

    lateinit var session : UserSessionInterface

    private lateinit var actionBox: View

    private lateinit var adapter: BaseChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initView(view)
        loadInitialData()
    }

    override fun loadInitialData() {
        presenter.connectWebSocket(messageId)
    }

    override fun removeDummy(it: Visitable<*>) {
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    fun onSuccessGetChat(listChat: ArrayList<Visitable<*>>) {

//      TODO MOVE THIS TO TOPCHATVIEW STATE
//        chatViewState.hideLoading()
//        chatViewState.addList(listChat)
        chatViewState.developmentView()

    }

    override fun developmentView() {
        val dummyList = arrayListOf<Visitable<*>>()

        dummyList.add(MessageViewModel("1", "1960918", "lawan", "User", "", "", "213123123", "213123123", true, false, false, "hi1"))
        dummyList.add(MessageViewModel("2", "7977933", "lawan", "User", "", "", "213123124", "213123123", true, false, true, "hi2"))
        dummyList.add(MessageViewModel("3", "1960918", "lawan", "User", "", "", "213123125", "213123123", true, false, false, "hi3"))
        dummyList.add(MessageViewModel("4", "7977933", "lawan", "User", "", "", "213123126", "213123123", true, false, true, "hi4"))
        dummyList.add(MessageViewModel("5", "1960918", "lawan", "User", "", "", "213123127", "213123123", true, false, false, "hi5"))
        dummyList.add(MessageViewModel("6", "7977933", "lawan", "User", "", "", "213123128", "213123123", true, false, true, "hi6"))
        dummyList.add(MessageViewModel("11", "1960918", "lawan", "User", "", "", "213123123", "213123123", true, false, false, "hi11"))
        dummyList.add(MessageViewModel("21", "7977933", "lawan", "User", "", "", "213123124", "213123123", true, false, true, "hi21"))
        dummyList.add(MessageViewModel("31", "1960918", "lawan", "User", "", "", "213123125", "213123123", true, false, false, "hi31"))
        dummyList.add(MessageViewModel("41", "7977933", "lawan", "User", "", "", "213123126", "213123123", true, false, true, "hi41"))
        dummyList.add(MessageViewModel("51", "1960918", "lawan", "User", "", "", "213123127", "213123123", true, false, false, "hi51"))
        dummyList.add(MessageViewModel("61", "7977933", "lawan", "User", "", "", "213123128", "213123123", true, false, true, "hi61"))

        chatViewState.addList(dummyList)
        chatViewState.hideLoading()
        chatViewState.developmentView()
    }

    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {
        //TODO create analytic class
//        TrackingUtils.sendGTMEvent(
//                new EventTracking(
//                        "clickInboxChat",
//                        "inbox-chat",
//                        "click on thumbnail",
//                        viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
//                ).getEvent()
//        );
        super.onImageAnnouncementClicked(viewModel)
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        super.onProductClicked(element)
        if (activity!!.applicationContext is AbstractionRouter) {
            val abstractionRouter = activity!!
                    .applicationContext as AbstractionRouter
            abstractionRouter.analyticTracker.sendEventTracking(
                    AttachProductAnalytics.getEventClickChatAttachedProductImage().event
            )
        }
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        chatViewState.addMessage(visitable)
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

    fun initView(view: View?) {
        view?.run {
            chatViewState = TopChatViewState(this)

            chatViewState?.run {
                chatViewState.showLoading()
                adapter = BaseChatAdapter(adapterTypeFactory, arrayListOf())
                chatViewState.setAdapter(adapter)
            }

            hideLoading()
            arguments?.run {
                //            presenter.getChatUseCase(this.getString("message_id", ""))
            }
        }
    }

    override fun initInjector() {

//        DaggerChatComponent.builder()
//                .topChatRoomComponent(getComponent(TopChatRoomComponent::class.java))
//                .build()
//                .inject(this)

        if (activity != null && (activity as Activity).application != null) {
            val chatRoomComponent = DaggerTopChatRoomComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
            val chatComponent
                    = DaggerChatComponent.builder().topChatRoomComponent(chatRoomComponent).build()
            chatComponent.inject(this)
            presenter.attachView(this)
        }
    }
//
//    override fun getNetworkMode(): Int {
//        return 1
//    }
//
//    override fun disableAction() {
//        chatViewState.setActionable(false)
//    }
//
//    override fun showSnackbarError(string: Unit) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun addDummyMessage(visitable: Visitable<*>) {
//        chatViewState.addMessage(visitable)
//    }
//
//    override fun removeDummy(visitable: Visitable<*>) {
//        chatViewState.removeDummy(visitable)
//    }

    override fun onSendButtonClicked() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}