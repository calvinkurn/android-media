package com.tokopedia.groupchat.room.view.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.chatbot.di.DaggerPlayComponent
import com.tokopedia.design.component.ToasterError
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel
import com.tokopedia.groupchat.common.util.TransparentStatusBarHelper
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.presenter.PlayPresenter
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 11/02/19
 */

class PlayFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(), PlayContract.View,
        ChatroomContract.QuickReply,
        ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        ChatroomContract.ChatItem.GroupChatPointsViewHolderListener{

    @Inject
    lateinit var userSession: UserSessionInterface

    private var snackbarWebsocket: Snackbar? = null

    companion object {

        const val YOUTUBE_DELAY = 1500

        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): PlayFragment {
            val fragment = PlayFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    @Inject
    lateinit var presenter: PlayPresenter

    open lateinit var viewState: PlayViewState

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.play_fragment, container, false)
        TransparentStatusBarHelper.assistActivity(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        presenter.getPlayInfo(arguments?.getString(PlayActivity.EXTRA_CHANNEL_UUID), onSuccessGetInfo())
    }

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            viewState.setToolbarData(it.title, it.bannerUrl, it.totalView, it.blurredBannerUrl)
            viewState.setSponsorData(it.adsId, it.adsImageUrl, it.adsName)
            viewState.initVideoFragment(childFragmentManager, it)
            viewState.onSuccessGetInfo(it)
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat)
        }
    }

    private fun initView(view: View) {
        view?.let {
            viewState = PlayViewStateImpl(userSession, it, this, this, this, this, this)
        }
        setToolbarView(view)
    }

    private fun setToolbarView(view: View) {

        var toolbar = viewState.getToolbar()

        if (isLollipopOrNewer()) {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            toolbar?.setPadding(0, getStatusBarHeight(), 0, 0)
        }
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun isLollipopOrNewer(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.chat_list)
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun getAdapterTypeFactory(): GroupChatTypeFactoryImpl {
        return GroupChatTypeFactoryImpl(this, this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val playComponent = DaggerPlayComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            playComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun loadData(page: Int) {

    }

    override fun addQuickReply(text: String?) {
    }

    override fun onImageAnnouncementClicked(url: String?) {

    }

    override fun onVoteComponentClicked(type: String?, name: String?) {

    }

    override fun onSprintSaleProductClicked(sprintSaleViewModel: SprintSaleProductViewModel?, position: Int) {

    }

    override fun onSprintSaleComponentClicked(sprintSaleAnnouncementViewModel: SprintSaleAnnouncementViewModel?) {

    }

    override fun onSprintSaleIconClicked(sprintSaleViewModel: SprintSaleViewModel?) {

    }

    override fun onPointsClicked(url: String?) {

    }

    override fun onOpenWebSocket() {
        snackbarWebsocket?.dismiss()
    }

    override fun onMessageReceived(item: Visitable<*>, hideMessage: Boolean) {

    }

    override fun setSnackBarConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackbarWebsocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.connecting))
            snackbarWebsocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.show()
            }
        }
    }

    override fun setSnackBarRetryConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackbarWebsocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.sendbird_error_retry))
            snackbarWebsocket?.let{
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.setAction(getString(R.string.retry), View.OnClickListener {
                    setSnackBarConnectingWebSocket()
                })
                it.show()
            }
        }
    }

}
