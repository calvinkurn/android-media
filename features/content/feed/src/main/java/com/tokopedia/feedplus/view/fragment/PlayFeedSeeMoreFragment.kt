package com.tokopedia.feedplus.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.activity.PlayVideoLiveListActivity
import com.tokopedia.feedplus.view.adapter.viewholder.playseemore.PlaySeeMoreAdapter
import com.tokopedia.feedplus.view.analytics.widget.FeedPlayVideoDetailPageAnalyticsListener
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.pref.PlayWidgetPreference
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab
import com.tokopedia.videoTabComponent.view.uimodel.SelectedPlayWidgetCard
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by shruti on 20/02/22.
 */
class PlayFeedSeeMoreFragment : BaseDaggerFragment(), PlayWidgetListener {

    private val rvWidgetSample by lazy(LazyThreadSafetyMode.NONE) { view?.findViewById<RecyclerView>(R.id.rv_widget_sample) }
    private val widgetType by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE) ?: "" }
    private val sourceType by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_TYPE) ?: "" }
    private val sourceId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_ID) ?: "" }
    private val filterCategory by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_FILTER_CATEGORY) ?: "" }

    private lateinit var adapter: PlaySeeMoreAdapter
    private lateinit var playWidgetCoordinator: PlayWidgetCoordinatorVideoTab
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private val playFeedVideoTabViewModel: PlayFeedVideoTabViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(PlayFeedVideoTabViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var playWidgetAnalyticsListenerImp: FeedPlayVideoDetailPageAnalyticsListener

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analyticListener: PlayAnalyticsTracker

    @Inject
    lateinit var playWidgetPreference: PlayWidgetPreference

    override fun getScreenName(): String {
        return "PlaySeeMoreFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }

    private fun initVar() {
        playWidgetAnalyticsListenerImp.setOnClickChannelCard { channelId, position ->
            playFeedVideoTabViewModel.selectedPlayWidgetCard = SelectedPlayWidgetCard(channelId, position)
        }

        playWidgetCoordinator = PlayWidgetCoordinatorVideoTab(this).apply {
            setListener(this@PlayFeedSeeMoreFragment)
            setAnalyticListener(playWidgetAnalyticsListenerImp)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_see_more, container, false)
    }
    override fun initInjector() {
        DaggerFeedPlusComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        playFeedVideoTabViewModel.run {
            getLiveOrUpcomingPlayDataRsp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onSuccessPlayTabData(
                                it.data.playGetContentSlot
                            )
                        }
                        is Fail -> {
                            Timber.e(it.throwable)
                        }
                    }
                }
            )
            reminderObservable.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> onSuccessReminderSet(it.data)
                        is Fail -> {
                            val errorMsg = if (it.throwable is CustomUiMessageThrowable) {
                                getString(
                                    (it.throwable as? CustomUiMessageThrowable)?.errorMessageId
                                        ?: com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder
                                )
                            } else {
                                it.throwable.message
                                    ?: getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder)
                            }
                            showToast(errorMsg, Toaster.TYPE_ERROR)
                        }
                    }
                }
            )

            playWidgetReminderEvent.observe(
                viewLifecycleOwner,
                Observer {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME)
                }
            )
        }
    }
    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentLivePageCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(
            FeedPlayVideoTabMapper.map(
                playDataResponse.data,
                playDataResponse.meta,
                shopId = userSession.shopId,
                playWidgetPreference = playWidgetPreference
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendAnalyticsForLehatSemua()
        setupView(view)
        setUpShopDataHeader()
        playFeedVideoTabViewModel.getPlayDetailPageData(widgetType, sourceId, sourceType)
    }

    private fun sendAnalyticsForLehatSemua() {
        if (widgetType == WIDGET_LIVE) {
            analyticListener.clickOnSeeAllOnLagiLiveCarousel()
        } else {
            analyticListener.clickOnSeeAllOnUpcomingCarousel(if (filterCategory.isNotEmpty()) filterCategory else DEFAULT_FILTER_CATEGORY)
        }
    }

    private fun setupView(view: View) {
        adapter = PlaySeeMoreAdapter(
            coordinator = playWidgetCoordinator
        )
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            rvWidgetSample?.addOnScrollListener(it)
            it.resetState()
        }
        playWidgetAnalyticsListenerImp.filterCategory = filterCategory
        playWidgetAnalyticsListenerImp.entryPoint = if (widgetType == WIDGET_LIVE) ENTRY_POINT_WIDGET_LIVE else ENTRY_POINT_WIDGET_UPCOMING

        rvWidgetSample?.adapter = adapter
    }
    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(rvWidgetSample?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getPlayDetailPageData(widgetType, sourceId, sourceType)
            }
        }
    }

    private fun setUpShopDataHeader() {
        (activity as PlayVideoLiveListActivity).getShopInfoLayout()?.run {
            shopHeader.text =
                when (widgetType) {
                    WIDGET_UPCOMING -> getString(com.tokopedia.feedplus.R.string.feed_play_header_upcoming_text)
                    else -> getString(com.tokopedia.feedplus.R.string.feed_play_header_text)
                }

            product_detail_back_icon?.setOnClickListener { activity?.finish() }
            show()
        }
    }

    companion object {
        const val WIDGET_LIVE = "live"
        const val WIDGET_UPCOMING = "upcoming"
        const val ENTRY_POINT_WIDGET_LIVE = "lagi live"
        const val ENTRY_POINT_WIDGET_UPCOMING = "upcoming"
        const val DEFAULT_FILTER_CATEGORY = "Untukmu"
        private const val REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME = 258

        private const val REQUEST_CODE_PLAY_ROOM = 123
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = PlayFeedSeeMoreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        super.onWidgetOpenAppLink(view, appLink)
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    override fun onToggleReminderClicked(view: PlayWidgetLargeView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        super.onToggleReminderClicked(view, channelId, reminderType, position)
        playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(channelId, reminderType, position)
    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }
    private fun onSuccessReminderSet(playWidgetFeedReminderInfoData: PlayWidgetFeedReminderInfoData) {
        showToast(
            if (playWidgetFeedReminderInfoData.reminderType.reminded) {
                getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_success_add_reminder)
            } else {
                getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_success_remove_reminder)
            },
            Toaster.TYPE_NORMAL
        )

        val adapterPositionForItem = adapter.getPositionInList(playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.itemPosition)
        adapter.updatePlayWidgetInfo(
            adapterPositionForItem,
            playWidgetFeedReminderInfoData.channelId,
            null,
            playWidgetFeedReminderInfoData.reminderType == PlayWidgetReminderType.Reminded
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME -> if (resultCode == Activity.RESULT_OK) {
                val playWidgetFeedReminderInfoData = playFeedVideoTabViewModel.playWidgetReminderEvent.value
                if (playWidgetFeedReminderInfoData != null) playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.reminderType, playWidgetFeedReminderInfoData.itemPosition)
            }
            REQUEST_CODE_PLAY_ROOM -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedCard = playFeedVideoTabViewModel.selectedPlayWidgetCard

                    val channelId = data?.extras?.getString(EXTRA_CHANNEL_ID) ?: selectedCard.channelId
                    val totalView = data?.extras?.getString(EXTRA_TOTAL_VIEW)
                    val isReminderSet = data?.extras?.getBoolean(EXTRA_IS_REMINDER, false)

                    val position = adapter.getPositionInList(channelId, selectedCard.position)

                    adapter.updatePlayWidgetInfo(position, channelId, totalView, isReminderSet)

                    playFeedVideoTabViewModel.selectedPlayWidgetCard = SelectedPlayWidgetCard.Empty
                }
            }
        }
    }
}
