package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.producttag.ProductTagAnalyticHelper
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveInitState
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveState
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.state.PinnedMessageUiState
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.ProductIconView
import com.tokopedia.play.broadcaster.view.custom.pinnedmessage.PinnedMessageFormView
import com.tokopedia.play.broadcaster.view.custom.pinnedmessage.PinnedMessageView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.*
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.play_common.R as commonR

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic
): PlayBaseBroadcastFragment(), FragmentWithDetachableView {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private val clInteraction: ConstraintLayout by detachableView(R.id.cl_interaction)
    private val viewStatInfo: PlayStatInfoView by detachableView(R.id.view_stat_info)
    private val ivShareLink: AppCompatImageView by detachableView(R.id.iv_share_link)
    private val iconProduct: ProductIconView by detachableView(R.id.icon_product)
    private val pmvMetrics: PlayMetricsView by detachableView(R.id.pmv_metrics)
    private val loadingView: FrameLayout by detachableView(R.id.loading_view)
//    private val errorLiveNetworkLossView: View by detachableView(R.id.error_live_view)
//    private val debugView: PlayLivePusherDebugView by detachableView(R.id.live_debug_view)
    private val pinnedMessageView: PinnedMessageView by detachableView(R.id.pinned_msg_view)

    private val actionBarLiveView by viewComponent {
        ActionBarLiveViewComponent(it, object: ActionBarLiveViewComponent.Listener {
            override fun onCameraIconClicked() {
                analytic.clickSwitchCameraOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                (activity as? PlayBroadcastActivity)?.flipCamera()
            }

            override fun onEndStreamClicked() {
                activity?.onBackPressed()
            }
        })
    }

    private val chatListView by viewComponent { ChatListViewComponent(it) }
    private val interactiveView by viewComponent {
        BroadcastInteractiveViewComponent(it, object : BroadcastInteractiveViewComponent.Listener {
            override fun onNewGameClicked(view: BroadcastInteractiveViewComponent) {
                if (allowSetupInteractive()) {
                    interactiveSetupView.setActiveTitle(parentViewModel.setupInteractiveTitle)
                    interactiveSetupView.setAvailableDurations(parentViewModel.interactiveDurations)
                    interactiveSetupView.setSelectedDuration(parentViewModel.selectedInteractiveDuration)
                    interactiveSetupView.show()
                    analytic.onClickInteractiveTool(parentViewModel.channelId)
                } else {
                    showToaster(
                        message = getString(R.string.play_interactive_broadcast_not_allowed),
                        duration = Toaster.LENGTH_SHORT
                    )
                }
            }

            override fun onSeeWinnerClicked(view: BroadcastInteractiveViewComponent) {
                openInteractiveLeaderboardSheet()
                analytic.onClickWinnerIcon(
                    parentViewModel.channelId,
                    parentViewModel.interactiveId,
                    parentViewModel.activeInteractiveTitle
                )
            }
        })
    }

    private val interactiveSetupView by viewComponent {
        BroadcastInteractiveSetupViewComponent(it, object : BroadcastInteractiveSetupViewComponent.Listener {
            override fun onTitleInputChanged(
                view: BroadcastInteractiveSetupViewComponent,
                title: String
            ) {
                parentViewModel.setInteractiveTitle(title)
            }

            override fun onPickerValueChanged(
                view: BroadcastInteractiveSetupViewComponent,
                durationInMs: Long
            ) {
                parentViewModel.setSelectedInteractiveDuration(durationInMs)
            }

            override fun onApplyButtonClicked(
                view: BroadcastInteractiveSetupViewComponent,
                title: String,
                durationInMs: Long
            ) {
                parentViewModel.createInteractiveSession(title, durationInMs)
            }
        })
    }
    private val productTagView by viewComponent {
        ProductTagViewComponent(it, object: ProductTagViewComponent.Listener {
            override fun impressProductTag(view: ProductTagViewComponent) {
                analytic.impressProductTag(parentViewModel.channelId)
            }

            override fun scrollProductTag(
                view: ProductTagViewComponent,
                product: ProductUiModel,
                position: Int
            ) {
                productTagAnalyticHelper.trackScrollProduct(parentViewModel.channelId, product, position)
            }
        })
    }

    private lateinit var exitDialog: DialogUnify
    private lateinit var forceStopDialog: DialogUnify

    private val fragmentViewContainer = FragmentViewContainer()

    private var toasterBottomMargin = 0

    private lateinit var productTagAnalyticHelper: ProductTagAnalyticHelper

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity()),
        ).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_user_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnalytic()
        setupView()
        setupInsets()
        setupObserve()

//        if((activity as? PlayBroadcastActivity)?.isDialogContinueLiveStreamOpen() == false &&
//            (activity as? PlayBroadcastActivity)?.isRequiredPermissionGranted() == true)
//            parentViewModel.startLiveTimer()

//        if (GlobalConfig.DEBUG) setupDebugView(view)
    }

    override fun onStart() {
        super.onStart()
        actionBarLiveView.rootView.requestApplyInsetsWhenAttached()
        ivShareLink.requestApplyInsetsWhenAttached()
    }

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSetupFragment -> {
                childFragment.setDataSource(object : ProductSetupFragment.DataSource {
                    override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                        //TODO("Revamp this")
                        return if (::parentViewModel.isInitialized) parentViewModel.productSectionList
                        else emptyList()
                    }
                })
            }
        }
    }

    private fun initAnalytic() {
        productTagAnalyticHelper = ProductTagAnalyticHelper(analytic)
    }

    private fun setupView() {
        observeTitle()
        actionBarLiveView.setShopIcon(parentViewModel.getShopIconUrl())

        ivShareLink.setOnClickListener{
            doCopyShareLink()
            analytic.clickShareIconOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
        iconProduct.setOnClickListener {
            doShowProductInfo()
            analytic.clickProductTagOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
        pinnedMessageView.setOnPinnedClickedListener { _, message ->
            parentViewModel.submitAction(PlayBroadcastAction.EditPinnedMessage)

            interactiveView.cancelCoachMark()

            if (message.isBlank()) {
                analytic.clickAddPinChatMessage(
                    channelId = parentViewModel.channelId,
                    titleChannel = parentViewModel.channelTitle,
                )
            } else {
                analytic.clickEditPinChatMessage(
                    channelId = parentViewModel.channelId,
                    titleChannel = parentViewModel.channelTitle,
                )
            }
        }
    }

    private fun setupInsets() {
        actionBarLiveView.rootView.doOnApplyWindowInsets { v, insets, _, _ ->
            v.updatePadding(top = insets.systemWindowInsetTop)
        }

        ivShareLink.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

//    private fun setupDebugView(view: View) {
//        val ivSetting = view.findViewById<AppCompatImageView>(R.id.iv_setting)
//
//        ivSetting.show()
//        ivSetting.setOnClickListener {
//            debugView.show()
//        }

//        observeLiveInfo()
//        observeLiveStats()
//    }

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            actionBarLiveView.setTitle(it.title)
        }
    }

//    private fun observeLiveInfo() {
//        parentViewModel.observableLivePusherInfo.observe(viewLifecycleOwner) {
//            when (it) {
//                is PlayLiveLogState.Init -> debugView.setLiveInfo(it)
//                is PlayLiveLogState.Changed -> debugView.updateState(it.state)
//            }
//        }
//    }

//    private fun observeLiveStats() {
//        parentViewModel.observableLivePusherStatistic.observe(viewLifecycleOwner) {
//            if (it is PlayLivePusherStatistic) {
//                debugView.updateStats(it)
//            }
//        }
//    }

    private fun setupObserve() {
//        observeLiveState()
//        observeLiveDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeEvent()
        observeInteractiveConfig()
        observeCreateInteractiveSession()
        observeUiState()
        observeUiEvent()
    }

    override fun onPause() {
        super.onPause()
        productTagAnalyticHelper.sendTrackingProduct()
    }

    override fun onDestroy() {
        try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
        super.onDestroy()
    }

    /**
     * Dismissing all dialog -> not ideal because the it doesn't eliminate the root cause
     * Need to revamp the flow of stopping live stream and all
     */
    override fun onDestroyView() {
        if (::exitDialog.isInitialized) getExitDialog().dismiss()
        if (::forceStopDialog.isInitialized) forceStopDialog.dismiss()

        super.onDestroyView()
    }

    override fun onBackPressed(): Boolean {
        return when {
            isPinnedFormVisible() -> {
                parentViewModel.submitAction(PlayBroadcastAction.CancelEditPinnedMessage)
                true
            }
            interactiveSetupView.isShown() -> interactiveSetupView.interceptBackPressed()
            else -> showDialogWhenActionClose()
        }
    }

    /**
     * render to ui
     */
    private fun showCounterDuration(remainingInMs: Long) {
        viewStatInfo.setTimerCounter(remainingInMs)
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        viewStatInfo.setTotalView(totalView)
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        viewStatInfo.setTotalLike(totalLike)
    }

    private fun setChatList(chatList: List<PlayChatUiModel>) {
        chatListView.setChatList(chatList)
    }

    private fun setNewChat(chat: PlayChatUiModel) {
        chatListView.showNewChat(chat)
    }

    private fun setNewMetrics(metrics: List<PlayMetricUiModel>) {
        pmvMetrics.addMetricsToQueue(metrics)
    }

    private fun showDialogWhenActionClose(): Boolean {
        getExitDialog().show()
        analytic.viewDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        return true
    }

    private fun getExitDialog(): DialogUnify {
        if (!::exitDialog.isInitialized) {
           exitDialog = requireContext().getDialog(
                   actionType = DialogUnify.HORIZONTAL_ACTION,
                   title = getString(R.string.play_live_broadcast_dialog_end_title),
                   desc = getString(R.string.play_live_broadcast_dialog_end_desc),
                   primaryCta = getString(R.string.play_live_broadcast_dialog_end_primary),
                   primaryListener = { dialog -> dialog.dismiss() },
                   secondaryCta = getString(R.string.play_broadcast_exit),
                   secondaryListener = { dialog ->
                       parentViewModel.submitAction(PlayBroadcastAction.ExitLive)
//                       parentViewModel.stopLiveStream(shouldNavigate = true)
                       analytic.clickDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                   }
           )
        }
        return exitDialog
    }

    private fun showDialogWhenTimeout() {
        showForceStopDialog(
                title = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
                message = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
                buttonTitle = getString(R.string.play_live_broadcast_dialog_end_timeout_primary),
                onClickListener = {
                    analytic.clickDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                }
        )
    }

    private fun showForceStopDialog(
            title: String,
            message: String,
            buttonTitle: String,
            onClickListener: () -> Unit = { }) {
        if (!::forceStopDialog.isInitialized) {
            forceStopDialog = requireContext().getDialog(
                    title = title,
                    desc = message,
                    primaryCta = buttonTitle,
                    primaryListener = { dialog ->
                        dialog.dismiss()
                        navigateToSummary()
                        onClickListener()
                    }
            )
        }
        if (!forceStopDialog.isShowing) {
            analytic.viewDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
            forceStopDialog.show()
        }
    }

    private fun showDialogContinueLiveStreaming() {
        if (!isVisible || !isAdded) return
        (requireActivity() as? PlayBroadcastActivity)?.showDialogContinueLiveStreaming()
    }

    private fun showErrorToaster(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        val errMessage = if (customErrMessage == null) {
            ErrorHandler.getErrorMessage(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
        } else {
            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            getString(
                commonR.string.play_custom_error_handler_msg,
                customErrMessage,
                errCode
            )
        }
        showToaster(errMessage, Toaster.TYPE_ERROR, duration, actionLabel, actionListener)
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
            toasterBottomMargin = ivShareLink.height + offset24
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

    private fun doCopyShareLink() {
        PlayShareWrapper.copyToClipboard(requireContext(), parentViewModel.shareContents) {
            showToaster(
                    message = getString(R.string.play_live_broadcast_share_link_copied),
                    actionLabel = getString(R.string.play_ok))
        }
    }

    private fun doShowProductInfo() {
        childFragmentManager.beginTransaction()
            .add(ProductSetupFragment::class.java, null, null)
            .commit()
    }

    private fun navigateToSummary() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastSummaryFragment::class.java)
        analytic.openReportScreen(parentViewModel.channelId)
    }

//    private fun handleLivePushInfo(state: PlayLiveViewState) {
//        if (!isVisible) return
//        errorLiveNetworkLossView.hide()
//        when (state) {
//            is PlayLiveViewState.Connecting -> showLoading(true)
//            is PlayLiveViewState.Started -> showLoading(false)
//            is PlayLiveViewState.Stopped -> {
//                showLoading(false)
//                val exitDialog = getExitDialog()
//                exitDialog.dialogSecondaryCTA.isLoading = false
//                exitDialog.dismiss()
//                if (state.shouldNavigate) navigateToSummary()
//            }
//            is PlayLiveViewState.Error -> {
//                showLoading(false)
//                handleLivePushError(state)
//            }
//            is PlayLiveViewState.Resume -> {
//                showLoading(false)
//                if (!state.isResumed) showDialogContinueLiveStreaming()
//            }
//            is PlayLiveViewState.Recovered -> {
//                showLoading(false)
//                showToaster(
//                        message = getString(R.string.play_live_broadcast_network_recover),
//                        type = Toaster.TYPE_NORMAL
//                )
//            }
//        }
//    }

//    private fun handleLivePushError(state: PlayLiveViewState.Error) {
//        when(state.error.type) {
//            PlayLivePusherErrorType.NetworkPoor -> showErrorToaster(
//                err = state.error,
//                customErrMessage = getString(R.string.play_live_broadcast_network_poor),
//            )
//            PlayLivePusherErrorType.NetworkLoss -> errorLiveNetworkLossView.show()
//            PlayLivePusherErrorType.ConnectFailed -> {
//                showErrorToaster(
//                    err = state.error,
//                    customErrMessage = getString(R.string.play_live_broadcast_connect_fail),
//                    duration = Toaster.LENGTH_INDEFINITE,
//                    actionLabel = getString(R.string.play_broadcast_try_again),
//                    actionListener = { parentViewModel.reconnectLiveStream() }
//                )
//            }
//            PlayLivePusherErrorType.SystemError -> showErrorToaster(
//                err = state.error,
//                customErrMessage = getString(R.string.play_dialog_unsupported_device_desc),
//                duration = Toaster.LENGTH_INDEFINITE,
//                actionLabel = getString(R.string.play_ok),
//                actionListener = { parentViewModel.stopLiveStream(shouldNavigate = true) }
//            )
//        }
//        analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle, state.error.reason)
//    }

    private fun showLoading(isLoading: Boolean) {
        loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //region observe
    /**
     * Observe
     */
//    private fun observeLiveState() {
//        parentViewModel.observableLiveViewState.observe(viewLifecycleOwner, Observer(::handleLivePushInfo))
//    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

//    private fun observeLiveDuration() {
//        parentViewModel.observableLiveTimerState.observe(viewLifecycleOwner) {
//            when(it)  {
//                is PlayLiveTimerState.Active -> showCounterDuration(it.remainingInMs)
//                is PlayLiveTimerState.Finish -> {
//                    showDialogWhenTimeout()
//                }
//            }
//        }
//    }

    private fun observeChatList() {
        parentViewModel.observableChatList.observe(viewLifecycleOwner, object : Observer<List<PlayChatUiModel>> {
            override fun onChanged(chatList: List<PlayChatUiModel>) {
                setChatList(chatList)
                parentViewModel.observableChatList.removeObserver(this)
            }
        })

        parentViewModel.observableNewChat.observe(viewLifecycleOwner, EventObserver(::setNewChat))
    }

    private fun observeMetrics() {
        parentViewModel.observableNewMetrics.observe(viewLifecycleOwner, EventObserver(::setNewMetrics))
    }

    private fun observeEvent() {
        parentViewModel.observableEvent.observe(viewLifecycleOwner) {
            when {
                it.freeze -> {
                    showForceStopDialog(
                            title = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
                            message = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
                            buttonTitle = getString(R.string.play_live_broadcast_dialog_end_timeout_primary)
                    )
                }
                it.banned -> {
                    showForceStopDialog(
                            title = it.title,
                            message = it.message,
                            buttonTitle = it.buttonTitle
                    )
                }
            }
        }
    }

    private fun observeInteractiveConfig() {
        parentViewModel.observableInteractiveConfig.observe(viewLifecycleOwner) { config ->
            interactiveSetupView.setConfig(config)
        }
        parentViewModel.observableInteractiveState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BroadcastInteractiveState.Forbidden -> {
                    interactiveView.hide()
                }
                is BroadcastInteractiveState.Allowed -> {
                    handleHasInteractiveState(state)
                    interactiveView.show()
                }
            }
        }
    }

    private fun observeCreateInteractiveSession() {
        parentViewModel.observableCreateInteractiveSession.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Loading -> interactiveSetupView.setLoading(true)
                is NetworkResult.Success -> {
                    analytic.onStartInteractive(
                        channelId = parentViewModel.channelId,
                        interactiveId = parentViewModel.interactiveId,
                        interactiveTitle = parentViewModel.activeInteractiveTitle,
                        durationInMs = state.data.durationInMs
                    )
                    interactiveSetupView.setLoading(false)
                    interactiveSetupView.reset()
                }
                is NetworkResult.Fail -> {
                    interactiveSetupView.setLoading(false)
                    showErrorToaster(
                        err = state.error,
                        customErrMessage = getString(R.string.play_interactive_broadcast_create_fail),
                        duration = Toaster.LENGTH_SHORT
                    )
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderPinnedMessageView(prevState?.pinnedMessage, state.pinnedMessage)
                renderProductTagView(prevState?.selectedProduct, state.selectedProduct)

                if (::exitDialog.isInitialized) {
                    val exitDialog = getExitDialog()
                    exitDialog.dialogSecondaryCTA.isLoading = state.isExiting
                    exitDialog.dialogSecondaryCTA.isEnabled = !state.isExiting
                    exitDialog.dialogPrimaryCTA.isEnabled = !state.isExiting
                }
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayBroadcastEvent.ShowError -> showErrorToaster(event.error)
                }
            }
        }
    }
    //endregion

    private fun renderPinnedMessageView(
        prevState: PinnedMessageUiState?,
        state: PinnedMessageUiState
    ) {
        if (prevState == state) return

        if (!state.editStatus.isEditing) hideKeyboard()

        /**
         * Pinned Message success uploading
         */
        if (prevState?.editStatus?.isUploading == true && state.editStatus.isNothing) {

            analytic.clickSavePinChatMessage(
                channelId = parentViewModel.channelId,
                titleChannel = parentViewModel.channelTitle,
            )
        }

        pinnedMessageView.setMode(
            if (state.message.isEmpty()) PinnedMessageView.Mode.Empty
            else PinnedMessageView.Mode.Filled(state.message)
        )

        when(state.editStatus) {
            PinnedMessageEditStatus.Editing,
            PinnedMessageEditStatus.Uploading -> {
                val formView = getPinnedFormView()
                if (formView.visibility != View.VISIBLE) formView.setPinnedMessage(state.message)
                else interactiveView.cancelCoachMark()
                formView.setLoading(state.editStatus == PinnedMessageEditStatus.Uploading)
                formView.visibility = View.VISIBLE
                clInteraction.visibility = View.GONE
            }
            PinnedMessageEditStatus.Nothing -> {
                if (!hasPinnedFormView()) return
                val formView = getPinnedFormView()
                formView.visibility = View.GONE
                clInteraction.visibility = View.VISIBLE
            }
        }
    }

    private fun renderProductTagView(
        prevState: List<ProductTagSectionUiModel>?,
        state: List<ProductTagSectionUiModel>
    ) {
        if (prevState == state) return

        productTagView.setProducts(
            state.filterNot { it.campaignStatus.isUpcoming() }
                .flatMap { it.products }
        )
    }

    private fun isPinnedFormVisible(): Boolean {
        val formView = view?.findViewWithTag<View>(PINNED_MSG_FORM_TAG)
        return formView != null && formView.visibility == View.VISIBLE
    }

    private fun hasPinnedFormView(): Boolean {
        return view?.findViewWithTag<View>(PINNED_MSG_FORM_TAG) != null
    }

    private fun getPinnedFormView(): PinnedMessageFormView {
        val view = this.view
        require(view is ViewGroup)
        val pinnedView = view.findViewWithTag(PINNED_MSG_FORM_TAG) ?: run {
            val theView = PinnedMessageFormView(view.context).apply {
                tag = PINNED_MSG_FORM_TAG
            }
            view.addView(theView)
            theView.visibility = View.GONE
            theView
        }

        val pinnedMessageFormViewListener = object : PinnedMessageFormView.Listener {

            override fun onCloseButtonClicked(view: PinnedMessageFormView) {
                parentViewModel.submitAction(PlayBroadcastAction.CancelEditPinnedMessage)
            }

            override fun onPinnedMessageSaved(view: PinnedMessageFormView, message: String) {
                hideKeyboard()
                parentViewModel.submitAction(PlayBroadcastAction.SetPinnedMessage(message))
            }
        }

        pinnedView.setListener(pinnedMessageFormViewListener)
        return pinnedView
    }

    private fun allowSetupInteractive(): Boolean {
        return parentViewModel.interactiveDurations.isNotEmpty()
    }

    private fun handleHasInteractiveState(state: BroadcastInteractiveState.Allowed) {

        fun setLive(timeInMs: Long) {
            interactiveView.setLive(timeInMs) {
                parentViewModel.onInteractiveLiveEnded()
            }
        }

        when (state) {
            is BroadcastInteractiveState.Allowed.Init -> handleInitInteractiveState(state.state)
            is BroadcastInteractiveState.Allowed.Schedule -> {
                interactiveView.setSchedule(state.title, state.timeToStartInMs) {
                    setLive(state.durationInMs)
                }
            }
            is BroadcastInteractiveState.Allowed.Live -> {
                setLive(state.remainingTimeInMs)
            }
        }
    }

    private fun handleInitInteractiveState(state: BroadcastInteractiveInitState) {
        when (state) {
            is BroadcastInteractiveInitState.NoPrevious -> {
                analytic.onImpressInteractiveTool(parentViewModel.channelId)
                interactiveView.setInit(state.showOnBoarding && !hasPinnedFormView())
            }
            BroadcastInteractiveInitState.Loading -> interactiveView.setLoading()
            is BroadcastInteractiveInitState.HasPrevious -> {
                analytic.onImpressWinnerIcon(parentViewModel.channelId, parentViewModel.interactiveId, parentViewModel.activeInteractiveTitle)
                interactiveView.setFinish(state.coachMark)
            }
        }
    }

    private fun openInteractiveLeaderboardSheet() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val leaderBoardBottomSheet = fragmentFactory.instantiate(
            requireContext().classLoader,
            PlayInteractiveLeaderBoardBottomSheet::class.java.name) as PlayInteractiveLeaderBoardBottomSheet
        leaderBoardBottomSheet.show(childFragmentManager)
    }

    companion object {
        private const val PINNED_MSG_FORM_TAG = "PINNED_MSG_FORM"
    }
}