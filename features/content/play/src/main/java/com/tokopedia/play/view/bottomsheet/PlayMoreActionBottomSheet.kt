package com.tokopedia.play.view.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayMoreActionType
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.action.OpenFooterUserReport
import com.tokopedia.play.view.uimodel.action.OpenUserReport
import com.tokopedia.play.view.uimodel.event.OpenUserReportEvent
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isGeneral
import com.tokopedia.play.view.uimodel.state.KebabMenuType
import com.tokopedia.play.view.viewcomponent.KebabMenuSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSubmissionViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionBottomSheet @Inject constructor(
    private val analytic: PlayAnalytic,
    private val trackingQueue: TrackingQueue,
    private val analytic2Factory: PlayAnalytic2.Factory
) : BottomSheetUnify(),
    KebabMenuSheetViewComponent.Listener,
    PlayUserReportSheetViewComponent.Listener,
    PlayUserReportSubmissionViewComponent.Listener {

    private var childView: View? = null

    private val kebabMenuSheetView by viewComponent { KebabMenuSheetViewComponent(it, this) }
    private val userReportSheetView by viewComponent { PlayUserReportSheetViewComponent(it, this) }
    private val userReportSubmissionSheetView by viewComponent {
        PlayUserReportSubmissionViewComponent(
            it,
            this
        )
    }

    private lateinit var playViewModel: PlayViewModel

    private var userReportTimeMillis: Date = Date()

    var mListener: Listener? = null

    private val reportAction by lazy {
        PlayMoreActionUiModel(
            type = PlayMoreActionType.Report,
            icon = getIconUnifyDrawable(
                requireContext(),
                IconUnify.WARNING,
                MethodChecker.getColor(
                    requireContext(),
                    com.tokopedia.unifycomponents.R.color.Unify_NN900
                )
            ),
            subtitleRes = R.string.play_kebab_report_title,
            onClick = {
                shouldOpenUserReport()
            },
            priority = 4,
            onImpress = { analytic2?.impressUserReport() }
        )
    }

    private val pipAction by lazy {
        PlayMoreActionUiModel(
            type = PlayMoreActionType.PiP,
            icon = MethodChecker.getDrawable(requireContext(), R.drawable.ic_play_pip),
            subtitleRes = R.string.play_kebab_pip,
            onClick = {
                analytic2?.clickPiP()
                mListener?.onPipClicked(this)
            },
            priority = 2,
            onImpress = { analytic2?.impressPiP() }
        )
    }

    private val watchAction by lazy {
        PlayMoreActionUiModel(
            type = PlayMoreActionType.WatchMode,
            icon = getIconUnifyDrawable(
                requireContext(),
                IconUnify.VISIBILITY,
                MethodChecker.getColor(
                    requireContext(),
                    com.tokopedia.unifycomponents.R.color.Unify_NN900
                )
            ),
            subtitleRes = R.string.play_kebab_watch_mode,
            onClick = {
                analytic2?.clickWatchMode()
                mListener?.onWatchModeClicked(this)
            },
            priority = 3,
            onImpress = { analytic2?.impressWatchMode() }
        )
    }

    private val seePerformanceAction by lazy {
        PlayMoreActionUiModel(
            type = PlayMoreActionType.SeePerformance,
            icon = getIconUnifyDrawable(
                requireContext(),
                IconUnify.GRAPH,
                MethodChecker.getColor(
                    requireContext(),
                    com.tokopedia.unifycomponents.R.color.Unify_NN900
                )
            ),
            subtitleRes = R.string.play_kebab_see_performance,
            onClick = {
                mListener?.onSeePerformanceClicked(this)
            },
            priority = 1,
            onImpress = {

            }
        )
    }

    private var analytic2: PlayAnalytic2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireParentFragment() is PlayUserInteractionFragment) {
            val grandParentActivity =
                ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

            playViewModel = ViewModelProvider(
                grandParentActivity,
                grandParentActivity.viewModelProviderFactory
            ).get(PlayViewModel::class.java)
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserve()
        setupListAction()
    }

    fun setState(isFreeze: Boolean) {}

    private fun setupView(view: View) {
        kebabMenuSheetView.hide()
        userReportSheetView.hide()
        userReportSubmissionSheetView.hide()
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    private fun initBottomSheet() {
        clearContentPadding = true
        showHeader = false
        childView =
            View.inflate(requireContext(), R.layout.bottom_sheet_play_more_action, null)
        setChild(childView)
        setCloseClickListener {
            hideSheets()
        }
    }

    /***
     *
     * Setup Observer
     */
    private fun setupObserve() {
        observeKebabInsets()
        observeUserReport()
        observeUserReportSubmission()
        observeEvent()
        observeBottomInsets()
        observeCast()
        observeVideoMeta()
        observeState()
    }

    private fun observeBottomInsets() {
        playViewModel.observableBottomInsetsState.observe(
            viewLifecycleOwner,
            DistinctObserver { bottomInsets ->
                renderPiPView(bottomInsets = bottomInsets)
            }
        )
    }

    private fun observeCast() {
        playViewModel.observableCastState.observe(viewLifecycleOwner) {
            renderPiPView()
        }
    }

    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner) { meta ->
            renderPiPView(videoPlayer = meta.videoPlayer)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            playViewModel.uiState.withCache().collectLatest {
                val cachedState = it

                if (cachedState.isChanged { it.status.channelStatus.statusType }) renderPiPView(
                    isFreezeOrBanned = cachedState.value.status.channelStatus.statusType.isFreeze || cachedState.value.status.channelStatus.statusType.isBanned
                )

                if (analytic2 != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic2 = analytic2Factory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = it.value.channel.channelInfo
                )
            }
        }
    }

    private fun renderPiPView(
        videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (!playViewModel.isPiPAllowed || !videoPlayer.isGeneral() || isFreezeOrBanned || playViewModel.isCastAllowed) {
            removeAction(pipAction)
            return
        }

        if (!bottomInsets.isAnyShown) {
            buildListAction(pipAction)
        } else {
            removeAction(pipAction)
        }
    }

    private fun observeKebabInsets() {
        playViewModel.observableKebabMenuSheet.observe(
            viewLifecycleOwner,
            DistinctObserver { kebabMenuType ->
                kebabMenuType[KebabMenuType.ThreeDots]?.let { it ->
                    if (it is BottomInsetsState.Shown) {
                        kebabMenuSheetView.show(listOfAction)
                    } else {
                        kebabMenuSheetView.hide()
                    }
                }

                kebabMenuType[KebabMenuType.UserReportList]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        userReportSheetView.showView()
                    } else {
                        userReportSheetView.hide()
                    }
                }

                kebabMenuType[KebabMenuType.UserReportSubmission]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        userReportSubmissionSheetView.showView(state.estimatedInsetsHeight)
                    } else {
                        userReportSubmissionSheetView.hide()
                    }
                }
            }
        )
    }

    private fun observeUserReport() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.userReportItems.withCache().collectLatest {
                val data = it.value
                when (data.resultState) {
                    is ResultState.Loading -> userReportSheetView.showPlaceholder()
                    is ResultState.Success ->
                        if (it.prevValue != data) userReportSheetView.setReportSheet(data)
                    is ResultState.Fail -> userReportSheetView.showError(
                        isConnectionError = data.resultState is ConnectException || data.resultState.error is UnknownHostException,
                        onError = {}
                    )
                }
            }
        }
    }

    private fun observeUserReportSubmission() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.userReportSubmission.collectLatest {
                when (it) {
                    is ResultState.Success -> hideSheets()
                    is ResultState.Fail -> doShowToaster(
                        toasterType = Toaster.TYPE_ERROR,
                        message = ErrorHandler.getErrorMessage(requireContext(), it.error)
                    )
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    OpenUserReportEvent -> {
                        doActionUserReport()
                    }
                }
            }
        }
    }

    private val listOfAction = mutableListOf<PlayMoreActionUiModel>()

    private fun buildListAction(action: PlayMoreActionUiModel) {
        if (!listOfAction.contains(watchAction) && !playViewModel.videoOrientation.isHorizontal && !playViewModel.hasNoMedia) listOfAction.add(
            watchAction
        ) // Watch Mode
        if (!listOfAction.contains(action)) listOfAction.add(action)
    }

    private fun removeAction(action: PlayMoreActionUiModel) {
        listOfAction.remove(action)
    }

    /***
     * Private Methods
     */

    private fun setupListAction() {
        buildListAction(action = reportAction)

        if(playViewModel.performanceSummaryPageLink.isNotEmpty())
            buildListAction(action = seePerformanceAction)
    }

    private fun hideSheets() {
        if (requireParentFragment() is PlayUserInteractionFragment) (requireParentFragment() as PlayUserInteractionFragment).hideBottomSheet()
        playViewModel.hideThreeDotsSheet()
    }

    private fun doActionUserReport() {
        analytic.clickUserReport()
        playViewModel.onShowUserReportSheet()
        playViewModel.getUserReportList()
    }

    private fun shouldOpenUserReport() {
        playViewModel.submitAction(OpenUserReport)
    }

    private fun onSubmitUserReport(reasonId: Int, description: String) {
        analytic.clickUserReportSubmissionDialogSubmit()
        val channelData = playViewModel.latestCompleteChannelData
        playViewModel.submitUserReport(
            mediaUrl = getMediaUrl(channelData.videoMetaInfo),
            timestamp = getTimestampVideo(channelData.channelDetail.channelInfo.startTime),
            reportDesc = description,
            reasonId = reasonId
        )
    }

    private fun getMediaUrl(mediaInfo: PlayVideoMetaInfoUiModel): String {
        return when (mediaInfo.videoPlayer) {
            is PlayVideoPlayerUiModel.YouTube -> "https://youtu.be/${mediaInfo.videoPlayer.youtubeId}"
            is PlayVideoPlayerUiModel.General -> mediaInfo.videoPlayer.params.videoUrl
            else -> ""
        }
    }

    private fun getTimestampVideo(startTime: String): Long {
        return if (playViewModel.channelType.isLive) {
            val startTimeInMiliSecond: Date = try {
                startTime.toDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS
                )
            } catch (e: Exception) {
                Date()
            }
            val duration = userReportTimeMillis.time - startTimeInMiliSecond.time
            TimeUnit.MILLISECONDS.toSeconds(duration)
        } else {
            TimeUnit.MILLISECONDS.toSeconds(playViewModel.getVideoTimestamp())
        }
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCTAText: String,
        secondaryCTAText: String,
        primaryAction: () -> Unit,
        secondaryAction: () -> Unit = {}
    ) {
        activity?.let {
            val dialog =
                DialogUnify(context = it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCTAText)
                setPrimaryCTAClickListener {
                    primaryAction()
                    dismiss()
                }
                setSecondaryCTAText(secondaryCTAText)
                setSecondaryCTAClickListener {
                    secondaryAction()
                    dismiss()
                }
            }.show()
        }
    }

    private fun doShowToaster(
        toasterType: Int,
        message: String,
        actionText: String = "",
        actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        Toaster.build(
            view = requireView().rootView,
            text = message,
            type = toasterType,
            actionText = actionText,
            clickListener = actionClickListener
        ).show()
    }

    /***
     * KebabMenuSheetViewComponent Listener
     */
    override fun onCloseButtonClicked(view: KebabMenuSheetViewComponent) {
        hideSheets()
    }

    /***
     * PlayUserReportSheetViewComponent Listener
     */
    override fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent) {
        playViewModel.hideUserReportSheet()
    }

    override fun onItemReportClick(
        view: PlayUserReportSheetViewComponent,
        item: PlayUserReportReasoningUiModel.Reasoning
    ) {
        userReportTimeMillis = Calendar.getInstance().time
        playViewModel.onShowUserReportSubmissionSheet(estimatedSheetHeight = userReportSheetView.rootView.measuredHeight)
        userReportSubmissionSheetView.setView(item)
    }

    override fun onFooterClicked(view: PlayUserReportSheetViewComponent) {
        playViewModel.submitAction(OpenFooterUserReport(getString(R.string.play_user_report_footer_weblink)))
    }

    /***
     * PlayUserReportSubmissionViewComponent Listener
     */
    override fun onCloseButtonClicked(view: PlayUserReportSubmissionViewComponent) {
        playViewModel.hideUserReportSubmissionSheet()

        /**
         * Hacky but can be improved, this bottom sheet has it own keyboard because it overlayed the bottom sheet
         */
        playViewModel.onKeyboardHidden()
        view.rootView.hideKeyboard()
    }

    override fun onFooterClicked(view: PlayUserReportSubmissionViewComponent) {
        playViewModel.submitAction(OpenFooterUserReport(getString(R.string.play_user_report_footer_weblink)))
    }

    override fun onShowVerificationDialog(
        view: PlayUserReportSubmissionViewComponent,
        reasonId: Int,
        description: String
    ) {
        val isUse = description.isNotEmpty()
        analytic.clickUserReportSubmissionBtnSubmit(isUse)

        showDialog(
            title = getString(R.string.play_user_report_verification_dialog_title),
            description = getString(R.string.play_user_report_verification_dialog_desc),
            primaryCTAText = getString(R.string.play_user_report_verification_dialog_btn_ok),
            secondaryCTAText = getString(R.string.play_pip_cancel),
            primaryAction = {
                onSubmitUserReport(reasonId, description)
            }
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        playViewModel.hideThreeDotsSheet()
        super.onCancel(dialog)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    /***
     * BottomSheet Listener
     */

    interface Listener {
        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onPipClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onSeePerformanceClicked(bottomSheet: PlayMoreActionBottomSheet)
    }

    companion object {
        private const val TAG = "PlayMoreActionBottomSheet"
    }
}
