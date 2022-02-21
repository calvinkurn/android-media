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
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.videoTabComponent.analytics.PlayWidgetAnalyticsListenerImp
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import javax.inject.Inject

/**
 * Created by meyta.taliti on 27/01/22.
 */
class PlayFeedSeeMoreFragment : BaseDaggerFragment() , PlayWidgetListener {

    private val rvWidgetSample by lazy(LazyThreadSafetyMode.NONE) { view?.findViewById<RecyclerView>(R.id.rv_widget_sample) }
    private val widgetType by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE)?:""
    }

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
                                it.data.playGetContentSlot,
                                it.data.playGetContentSlot.meta.next_cursor
                        )
                    }
                    is Fail -> {
                        //TODO implement error case
//                        fetchFirstPage()
                    }
                }
            })


        }
    }
    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse, cursor: String) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentLivePageCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(FeedPlayVideoTabMapper.map(playDataResponse.data, playDataResponse.meta))

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setUpShopDataHeader()
        playFeedVideoTabViewModel.getLivePlayData(widgetType)
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
    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener? {
        return object : EndlessRecyclerViewScrollListener(rvWidgetSample?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getLivePlayData(widgetType)
            }
        }
    }

    private fun getSampleWidgets(): List<PlayFeedUiModel> {
        return listOf(
                PlayWidgetLargeUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget()
                ),
                PlayWidgetLargeUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget()
                )
        )
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


}