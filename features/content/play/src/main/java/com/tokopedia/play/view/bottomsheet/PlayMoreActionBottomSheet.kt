package com.tokopedia.play.view.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.state.KebabMenuType
import com.tokopedia.play.view.viewcomponent.KebabMenuSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSubmissionViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.flow.collectLatest
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*
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
        get() = (requireView().height * 0.9).toInt()

    private var displayMetrix = DisplayMetrics()

    companion object {
        private const val TAG = "PlayMoreActionBottomSheet"
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

    private var userReportTimeMillis: Long = 0L

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
        setCloseClickListener { dismiss() }
    }

    private fun setObserve(){
        observeBottomInsets()
        observeUserReport()
        observeUserReportSubmission()
        observeLoginState()
    }

    private fun observeBottomInsets() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { (_, type) ->
                type.playKebabMenuBottomSheetUiState.kebabMenuType[KebabMenuType.ThreeDots]?.let { it ->
                    if (it is BottomInsetsState.Shown) {
                        customPeekHeight = (displayMetrix.heightPixels * 0.2).toInt()
                        kebabMenuSheetView.showWithHeight(customPeekHeight)
                    }
                    else kebabMenuSheetView.hide()
                }

                type.playKebabMenuBottomSheetUiState.kebabMenuType[KebabMenuType.UserReportList]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        customPeekHeight = (displayMetrix.heightPixels * 0.8).toInt()
                        userReportSheetView.showWithHeight(customPeekHeight)
                    }
                    else userReportSheetView.hide()
                }

                type.playKebabMenuBottomSheetUiState.kebabMenuType[KebabMenuType.UserReportSubmission]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        customPeekHeight = (displayMetrix.heightPixels * 0.8).toInt()
                        userReportSubmissionSheetView.showWithHeight(customPeekHeight)
                    }
                    else userReportSubmissionSheetView.hide()
                }
            }
        }
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
                    dismiss()
                }
                is PlayResult.Failure ->
                    doShowToaster(
                    toasterType = Toaster.TYPE_ERROR,
                    message = ErrorHandler.getErrorMessage(requireContext(), it.error)
                )
            }
        })
    }

    private fun observeLoginState(){
        playViewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

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

    private fun handleInteractionEvent(event: InteractionEvent) {
        when (event) {
            is InteractionEvent.OpenUserReport -> doActionUserReport()
        }
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun doActionUserReport(){
        analytic.clickUserReport()
        playViewModel.onShowUserReportSheet(userReportSheetHeight)
        playViewModel.getUserReportList()
    }

    private fun shouldOpenUserReport() {
        playViewModel.doInteractionEvent(InteractionEvent.OpenUserReport)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = 911)
    }

    interface Listener {
        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onNoAction(bottomSheet: PlayMoreActionBottomSheet)
    }

    override fun onReportClick(view: KebabMenuSheetViewComponent) {
        shouldOpenUserReport()
    }

    override fun onCloseButtonClicked(view: KebabMenuSheetViewComponent) {
        dismiss()
    }

    override fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent) {
       playViewModel.hideUserReportSheet()
    }

    override fun onItemReportClick(
        view: PlayUserReportSheetViewComponent,
        item: PlayUserReportReasoningUiModel.Reasoning
    ) {
        userReportTimeMillis = Calendar.getInstance().timeInMillis
        playViewModel.onShowUserReportSubmissionSheet(userReportSheetHeight)
        userReportSubmissionSheetView.setView(item)
    }

    override fun onFooterClicked(view: PlayUserReportSheetViewComponent) {
        openApplink(applink = getString(R.string.play_user_report_footer_weblink))
    }

    override fun onCloseButtonClicked(view: PlayUserReportSubmissionViewComponent) {
        playViewModel.hideUserReportSubmissionSheet()
    }

    override fun onFooterClicked(view: PlayUserReportSubmissionViewComponent) {
        openApplink(applink = getString(R.string.play_user_report_footer_weblink))
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
            val startTimeInSecond = startTime.toLongOrZero()
            val duration = (userReportTimeMillis - startTimeInSecond) / 1
            duration
        }else{
            playViewModel.getVideoTimestamp()
        }
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
            view = requireView(),
            text = message,
            type = toasterType,
            actionText = actionText,
            clickListener = actionClickListener
        ).show()
    }
}