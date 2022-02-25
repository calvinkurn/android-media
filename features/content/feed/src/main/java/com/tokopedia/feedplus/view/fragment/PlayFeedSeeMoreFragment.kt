package com.tokopedia.feedplus.view.fragment

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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.activity.PlayVideoLiveListActivity
import com.tokopedia.feedplus.view.adapter.viewholder.playseemore.PlaySeeMoreAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.PlayWidgetAnalyticsListenerImp
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import javax.inject.Inject

/**
 * Created by shruti on 20/02/22.
 */
class PlayFeedSeeMoreFragment : BaseDaggerFragment() , PlayWidgetListener {

    private val rvWidgetSample by lazy(LazyThreadSafetyMode.NONE) { view?.findViewById<RecyclerView>(R.id.rv_widget_sample) }
    private val widgetType by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE)?:""}
    private val sourceType by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_TYPE)?:""}
    private val sourceId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_ID)?:"" }
    private val filterCategory by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_UPCOMING_FILTER_CATEGORY)?:"" }

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
    lateinit var playWidgetAnalyticsListenerImp: PlayWidgetAnalyticsListenerImp

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var analyticListener: PlayAnalyticsTracker



    override fun getScreenName(): String {
        return "PlaySeeMoreFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }

    private fun initVar() {
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
            getLivePlayDataRsp.observe(lifecycleOwner, Observer {
//                hideAdapterLoading()
                when (it) {
                    is Success -> {
                        onSuccessPlayTabData(
                                it.data.playGetContentSlot
                        )
                    }
                    is Fail -> {
                        //TODO implement error case
//                        fetchFirstPage()
                    }
                }
            })
            reminderObservable.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Success -> onSuccessReminderSet(it.data)
                    else -> {
                        showToast(getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder), Toaster.TYPE_ERROR)
                    }
                }
            })


        }
    }
    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentLivePageCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(FeedPlayVideoTabMapper.map(playDataResponse.data, playDataResponse.meta, shopId = userSession.shopId))

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setUpShopDataHeader()
        playFeedVideoTabViewModel.getLivePlayData(widgetType, sourceId, sourceType)
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

        rvWidgetSample?.adapter = adapter
    }
    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(rvWidgetSample?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getLivePlayData(widgetType, sourceId, sourceType)
            }
        }
    }

    private fun setUpShopDataHeader() {
        (activity as PlayVideoLiveListActivity).getShopInfoLayout()?.run {
            shopHeader.text =
                when (widgetType) {
                    WIDGET_UPCOMING -> getString(R.string.feed_play_header_upcoming_text)
                    else ->  getString(R.string.feed_play_header_text)
            }

            product_detail_back_icon?.setOnClickListener { activity?.finish() }
            show()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        analyticListener.clickOnSeeAllOnLagiLiveCarousel(widgetType, filterCategory)

    }

    companion object{
        const val WIDGET_LIVE ="live"
        const val WIDGET_UPCOMING ="upcoming"

        private const val OPEN_PLAY_CHANNEL = 1858
        private const val EXTRA_PLAY_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_PLAY_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"


        fun createInstance(bundle: Bundle) : Fragment {
            val fragment = PlayFeedSeeMoreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        super.onWidgetOpenAppLink(view, appLink)
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, OPEN_PLAY_CHANNEL)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {

            OPEN_PLAY_CHANNEL -> {
                val channelId = data.getStringExtra(EXTRA_PLAY_CHANNEL_ID)
                val totalView = data.getStringExtra(EXTRA_PLAY_TOTAL_VIEW)
//                updatePlayWidgetTotalView(channelId, totalView)
            }

            else -> {
            }
        }
    }


    override fun onToggleReminderClicked(view: PlayWidgetLargeView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        super.onToggleReminderClicked(view, channelId, reminderType, position)
        playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(channelId, reminderType, position)

    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false)
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()

        }
    }
    private fun onSuccessReminderSet(playWidgetFeedReminderInfoData: PlayWidgetFeedReminderInfoData) {
        showToast(
                if (playWidgetFeedReminderInfoData.reminderType.reminded) getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                else getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder), Toaster.TYPE_NORMAL)

        val adapterPositionForItem = adapter.getPositionInList(playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.itemPosition)
        adapter.updateItemInList(adapterPositionForItem, playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.reminderType)

    }
}