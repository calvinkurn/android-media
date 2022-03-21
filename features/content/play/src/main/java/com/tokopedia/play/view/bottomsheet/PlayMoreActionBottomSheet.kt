package com.tokopedia.play.view.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.action.OpenFooterUserReport
import com.tokopedia.play.view.uimodel.action.OpenUserReport
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.event.OpenUserReportEvent
import com.tokopedia.play.view.uimodel.state.KebabMenuType
import com.tokopedia.play.view.viewcomponent.KebabMenuSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSubmissionViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionBottomSheet @Inject constructor(
    private val analytic: PlayAnalytic
    ) : BottomSheetUnify(), KebabMenuSheetViewComponent.Listener,
    PlayUserReportSheetViewComponent.Listener,
    PlayUserReportSubmissionViewComponent.Listener {

    private val userReportSheetHeight: Int
        get() = (displayMetrix.heightPixels * MAX_PERCENT_HEIGHT).toInt()

    private val kebabMenuSheetHeight: Int
        get() = (displayMetrix.heightPixels * MIN_PERCENT_HEIGHT).toInt()

    private var displayMetrix = DisplayMetrics()

    companion object {
        private const val TAG = "PlayMoreActionBottomSheet"

        private const val MAX_PERCENT_HEIGHT = 0.9
        private const val MIN_PERCENT_HEIGHT = 0.2
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val grandParentActivity = ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

        playViewModel = ViewModelProvider(
            grandParentActivity, grandParentActivity.viewModelProviderFactory
        ).get(PlayViewModel::class.java)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setObserve()
        (requireContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrix)
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
    private fun setObserve(){
        observeBottomInsets()
        observeUserReport()
        observeUserReportSubmission()
        observeEvent()
    }

    private fun observeBottomInsets() {
        playViewModel.observableKebabMenuSheet.observe(viewLifecycleOwner, DistinctObserver { kebabMenuType ->
            kebabMenuType[KebabMenuType.ThreeDots]?.let { it ->
                if (it is BottomInsetsState.Shown) {
                    customPeekHeight = kebabMenuSheetHeight
                    kebabMenuSheetView.showWithHeight(customPeekHeight)
                }
                else kebabMenuSheetView.hide()
            }

            kebabMenuType[KebabMenuType.UserReportList]?.let { state ->
                if (state is BottomInsetsState.Shown) {
                    customPeekHeight = userReportSheetHeight
                    userReportSheetView.showWithHeight(customPeekHeight)
                }
                else userReportSheetView.hide()
            }

            kebabMenuType[KebabMenuType.UserReportSubmission]?.let { state ->
                if (state is BottomInsetsState.Shown) {
                    customPeekHeight = userReportSheetHeight
                    userReportSubmissionSheetView.showWithHeight(customPeekHeight)
                }
                else userReportSubmissionSheetView.hide()
            }
        })
    }

    private fun observeUserReport() {
        playViewModel.observableUserReportReasoning.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> if (it.showPlaceholder) userReportSheetView.showPlaceholder()
                is PlayResult.Success -> userReportSheetView.setReportSheet(it.data)
                is PlayResult.Failure -> userReportSheetView.showError(
                    isConnectionError = it.error is ConnectException || it.error is UnknownHostException,
                    onError = it.onRetry
                )
            }
        })
    }

    private fun observeUserReportSubmission() {
        playViewModel.observableUserReportSubmission.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Success -> {
                    hideSheets()
                }
                is PlayResult.Failure ->
                    doShowToaster(
                    toasterType = Toaster.TYPE_ERROR,
                    message = ErrorHandler.getErrorMessage(requireContext(), it.error)
                )
            }
        })
    }

    private fun observeEvent(){
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    OpenUserReportEvent -> doActionUserReport()
                    is OpenPageEvent -> openPageByApplink(
                        applink = event.applink,
                        params = event.params.toTypedArray(),
                        requestCode = event.requestCode,
                        pipMode = event.pipMode
                    )
                }
            }
        }
    }

    /***
     * Private Methods
     */

    private fun hideSheets(){
        (requireParentFragment() as PlayUserInteractionFragment).hideBottomSheet()
        playViewModel.hideThreeDotsSheet()
    }

    private fun doActionUserReport(){
        analytic.clickUserReport()
        playViewModel.onShowUserReportSheet(userReportSheetHeight)
        playViewModel.getUserReportList()
    }

    private fun shouldOpenUserReport() {
        playViewModel.submitAction(OpenUserReport)
    }

    private fun onSubmitUserReport(reasonId: Int, description: String) {
        analytic.clickUserReportSubmissionDialogSubmit()
        val channelData = playViewModel.latestCompleteChannelData

        playViewModel.submitUserReport(
            channelId = channelData.id.toLongOrZero(),
            shopId = channelData.partnerInfo.id,
            mediaUrl = getMediaUrl(channelData.id),
            timestamp = getTimestampVideo(channelData.channelDetail.channelInfo.startTime),
            reportDesc = description,
            reasonId = reasonId
        )
    }

    private fun getMediaUrl(channelId: String) : String = "${TokopediaUrl.getInstance().WEB}play/channel/$channelId"

    private fun getTimestampVideo(startTime: String): Long{
        return if(playViewModel.channelType.isLive){
            val startTimeInMiliSecond : Date = try {
                startTime.toDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS
                )
            }catch (e: Exception){
                Date()
            }
            val duration = userReportTimeMillis.time - startTimeInMiliSecond.time
            TimeUnit.MILLISECONDS.toSeconds(duration)
        }else{
            TimeUnit.MILLISECONDS.toSeconds(playViewModel.getVideoTimestamp())
        }
    }

    /****
     * Common Methods can do better - move to parent
     */
    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && playViewModel.isPiPAllowed && !playViewModel.isFreezeOrBanned) {
            playViewModel.requestPiPBrowsingPage(
                OpenApplinkUiModel(applink = applink, params = params.toList(), requestCode, shouldFinish)
            )
        } else {
            openApplink(applink, *params, requestCode = requestCode, shouldFinish = shouldFinish)
        }
    }

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(requireContext(), applink, *params)
        } else {
            val intent = RouteManager.getIntent(requireContext(), applink, *params)
            startActivityForResult(intent, requestCode)
        }
        requireActivity().overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) requireActivity().finish()
    }

    private fun showDialog(title: String, description: String, primaryCTAText: String, secondaryCTAText: String, primaryAction: () -> Unit, secondaryAction: () -> Unit = {}){
        activity?.let {
            val dialog = DialogUnify(context = it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
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
    override fun onReportClick(view: KebabMenuSheetViewComponent) {
        shouldOpenUserReport()
    }

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
        playViewModel.onShowUserReportSubmissionSheet(userReportSheetHeight)
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

        showDialog(title = getString(R.string.play_user_report_verification_dialog_title), description = getString(R.string.play_user_report_verification_dialog_desc),
            primaryCTAText = getString(R.string.play_user_report_verification_dialog_btn_ok), secondaryCTAText = getString(R.string.play_pip_cancel),
            primaryAction = {
                onSubmitUserReport( reasonId, description)
            })
    }

    /***
     * BottomSheet Listener
     */

    interface Listener {
        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onNoAction(bottomSheet: PlayMoreActionBottomSheet)
    }
}