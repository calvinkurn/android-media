package com.tokopedia.groupchat.room.view.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.util.TransparentStatusBarHelper
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.presenter.PlayPresenter
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
import com.tokopedia.groupchat.room.di.DaggerPlayComponent
import javax.inject.Inject

/**
 * @author : Steven 11/02/19
 */

class PlayFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(), PlayContract.View {

    companion object {
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

    override fun loadData(page: Int) {

    }

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            viewState.onSuccessGetInfoFirstTime(it)
        }
    }

    private fun initView(view: View) {
        activity?.let {
            viewState = PlayViewStateImpl(view, it)
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
        return GroupChatTypeFactoryImpl()
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

    override fun onBackPressed() {
        viewState.onBackPressed(activity)
    }

}