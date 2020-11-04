package com.tokopedia.play.broadcaster.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
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

    companion object {
        private const val KEY_RESULT_SAVE_VIDEO = "play_broadcaster_save_video"
        private const val KEY_RESULT_DELETE_VIDEO = "play_broadcaster_delete_video"
    }

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var btnSaveVideo: UnifyButton
    private lateinit var btnDeleteVideo: UnifyButton
    private lateinit var loaderView: LoaderUnify
    private lateinit var deleteVideoDialog: DialogUnify

    private val summaryInfoView by viewComponent(isEagerInit = true) { SummaryInfoViewComponent(it) }

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
        observeSaveVideo()
        observeDeleteVideo()

        parentViewModel.getReportDuration()

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
        btnDeleteVideo.requestApplyInsetsWhenAttached()
    }

    private fun initView(view: View) {
        with(view) {
            btnSaveVideo = findViewById(R.id.btn_save_video)
            btnDeleteVideo = findViewById(R.id.btn_delete_video)
            loaderView = findViewById(R.id.loader_summary)
        }
    }

    private fun setupView(view: View) {
        summaryInfoView.entranceAnimation(view as ViewGroup)
        btnSaveVideo.setOnClickListener {
            viewModel.saveVideo()
        }
        btnDeleteVideo.setOnClickListener { showConfirmDeleteVideoDialog() }
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        btnDeleteVideo.doOnApplyWindowInsets { v, insets, _, margin ->
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

    private fun showConfirmDeleteVideoDialog() {
        if (!::deleteVideoDialog.isInitialized) {
            deleteVideoDialog = requireContext().getDialog(
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    title = getString(R.string.play_summary_delete_dialog_title),
                    desc = getString(R.string.play_summary_delete_dialog_message),
                    primaryCta = getString(R.string.play_summary_delete_dialog_action_delete),
                    primaryListener = { dialog ->
                        dialog.dismiss()
                        viewModel.deleteVideo()
                    },
                    secondaryCta = getString(R.string.play_summary_delete_dialog_action_back),
                    secondaryListener = { dialog -> dialog.dismiss() },
                    cancelable = true
            )
        }
        if (!deleteVideoDialog.isShowing) deleteVideoDialog.show()
    }

    /**
     * Observe
     */
    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer{
            when (it) {
                is NetworkResult.Success -> setChannelInfo(it.data)
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
                    summaryInfoView.showError { it.onRetry() }
                    analytic.viewErrorOnReportPage(
                            channelId = parentViewModel.channelId,
                            titleChannel = parentViewModel.title,
                            errorMessage = it.error.localizedMessage?:getString(R.string.play_broadcaster_default_error)
                    )
                }
            }
        })
    }

    private fun observeLiveDuration() {
        parentViewModel.observableReportDuration.observe(viewLifecycleOwner, Observer(this::setLiveDuration))
    }

    private fun observeSaveVideo() {
        viewModel.observableSaveVideo.observe(viewLifecycleOwner, Observer{
            when (it) {
                is NetworkResult.Loading -> btnSaveVideo.isLoading = true
                is NetworkResult.Success -> {
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(KEY_RESULT_SAVE_VIDEO, "ok")
                    })
                    activity?.finish()
                }
                is NetworkResult.Fail -> {
                    btnSaveVideo.isLoading = false
                    view?.showToaster(
                            message = it.error.localizedMessage?:getString(R.string.play_broadcaster_default_error),
                            type = Toaster.TYPE_ERROR,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = View.OnClickListener { view -> it.onRetry() }
                    )
                }
            }
        })
    }

    private fun observeDeleteVideo() {
        viewModel.observableDeleteVideo.observe(viewLifecycleOwner, Observer{
            when (it) {
                is NetworkResult.Loading -> btnDeleteVideo.isLoading = true
                is NetworkResult.Success -> {
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(KEY_RESULT_DELETE_VIDEO, "ok")
                    })
                    activity?.finish()
                }
                is NetworkResult.Fail -> {
                    btnDeleteVideo.isLoading = false
                    view?.showToaster(
                            message = it.error.localizedMessage?:getString(R.string.play_broadcaster_default_error),
                            type = Toaster.TYPE_ERROR,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = View.OnClickListener { view -> it.onRetry() }
                    )
                }
            }
        })
    }
}