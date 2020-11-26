package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * @author by jessica on 26/05/20
 */
class PlayBroadcastSummaryFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var summaryInfoView: SummaryInfoPartialView
    private lateinit var btnFinish: UnifyButton
    private lateinit var loaderView: LoaderUnify

    private var toasterBottomMargin = 0

    override fun getScreenName(): String = "Play Summary Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast_summary, container, false)

        observeChannelInfo()
        observeLiveDuration()
        observeLiveTrafficMetrics()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupInsets(view)
        setupContent()
    }

    override fun onStart() {
        super.onStart()
        requireView().requestApplyInsetsWhenAttached()
        btnFinish.requestApplyInsetsWhenAttached()
    }

    private fun initView(view: View) {
        with(view) {
            summaryInfoView = SummaryInfoPartialView(this as ViewGroup)
            btnFinish = findViewById(R.id.btn_finish)
            loaderView = findViewById(R.id.loader_summary)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.showActionBar(false)
        btnFinish.setOnClickListener {
            analytic.clickDoneOnReportPage(parentViewModel.channelId, parentViewModel.title)
            activity?.finish()
        }
        summaryInfoView.entranceAnimation(view as ViewGroup)
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        btnFinish.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupContent() {
        viewModel.fetchLiveTraffic()
    }

    private fun setChannelInfo(channelInfo: ChannelInfoUiModel) {
        summaryInfoView.setChannelTitle(channelInfo.title)
        summaryInfoView.setChannelCover(channelInfo.coverUrl)
    }

    private fun setSummaryInfo(dataList: List<TrafficMetricUiModel>) {
        summaryInfoView.setSummaryInfo(dataList)
    }

    private fun setLiveDuration(timeElapsed: String) {
        summaryInfoView.setLiveDuration(timeElapsed)
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            duration: Int = Toaster.LENGTH_LONG,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            val offset24 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
            toasterBottomMargin = btnFinish.height + offset24
        }

        view?.showToaster(
                message = message,
                duration = duration,
                type = type,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    /**
     * Observe
     */
    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer{
            when (it) {
                is NetworkResult.Success -> setChannelInfo(it.data)
                is NetworkResult.Fail -> if (GlobalConfig.DEBUG) showToaster(
                        it.error.localizedMessage,
                        type = Toaster.TYPE_ERROR
                )
            }
        })
    }

    private fun observeLiveTrafficMetrics() {
        viewModel.observableTrafficMetrics.observe(viewLifecycleOwner, Observer{
            when(it) {
                is NetworkResult.Loading -> {
                    loaderView.visible()
                    summaryInfoView.hideError()
                }
                is NetworkResult.Success -> {
                    loaderView.gone()
                    summaryInfoView.hideError()
                    setSummaryInfo(it.data)
                }
                is NetworkResult.Fail -> {
                    loaderView.gone()
                    if (GlobalConfig.DEBUG) showToaster(
                            it.error.localizedMessage,
                            type = Toaster.TYPE_ERROR
                    )
                    summaryInfoView.showError { it.onRetry() }
                    analytic.viewErrorOnReportPage(parentViewModel.channelId, parentViewModel.title, it.error.localizedMessage)
                }
            }
        })
    }

    private fun observeLiveDuration() {
        parentViewModel.observableReportDuration.observe(viewLifecycleOwner, Observer(this::setLiveDuration))
    }
}