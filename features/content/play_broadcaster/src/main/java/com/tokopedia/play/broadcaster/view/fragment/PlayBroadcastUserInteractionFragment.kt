package com.tokopedia.play.broadcaster.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.producttag.ProductTagAnalyticHelper
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherStatistic
import com.tokopedia.play.broadcaster.pusher.view.PlayLivePusherDebugView
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSetupUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.ui.state.OnboardingUiModel
import com.tokopedia.play.broadcaster.ui.state.PinnedMessageUiState
import com.tokopedia.play.broadcaster.ui.state.QuizFormUiState
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherErrorType
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroSelectGameBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroInteractiveBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.ProductIconView
import com.tokopedia.play.broadcaster.view.custom.game.quiz.QuizFormView
import com.tokopedia.play.broadcaster.view.custom.pinnedmessage.PinnedMessageFormView
import com.tokopedia.play.broadcaster.view.custom.pinnedmessage.PinnedMessageView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.dialog.InteractiveSetupDialogFragment
import com.tokopedia.play.broadcaster.view.fragment.summary.PlayBroadcastSummaryFragment
import com.tokopedia.play.broadcaster.view.interactive.InteractiveActiveViewComponent
import com.tokopedia.play.broadcaster.view.interactive.InteractiveFinishViewComponent
import com.tokopedia.play.broadcaster.view.interactive.InteractiveGameResultViewComponent
import com.tokopedia.play.broadcaster.view.partial.ActionBarLiveViewComponent
import com.tokopedia.play.broadcaster.view.partial.ChatListViewComponent
import com.tokopedia.play.broadcaster.view.partial.ProductTagViewComponent
import com.tokopedia.play.broadcaster.view.partial.game.GameIconViewComponent
import com.tokopedia.play.broadcaster.view.state.PlayLiveTimerState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.Collections
import javax.inject.Inject
import com.tokopedia.play_common.R as commonR

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment(),
    FragmentWithDetachableView {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private val clInteraction: ConstraintLayout by detachableView(R.id.cl_interaction)
    private val viewStatInfo: PlayStatInfoView by detachableView(R.id.view_stat_info)
    private val ivShareLink: AppCompatImageView by detachableView(R.id.iv_share_link)
    private val iconProduct: ProductIconView by detachableView(R.id.icon_product)
    private val pmvMetrics: PlayMetricsView by detachableView(R.id.pmv_metrics)
    private val loadingView: FrameLayout by detachableView(R.id.loading_view)
    private val errorLiveNetworkLossView: View by detachableView(R.id.error_live_view)
    private val debugView: PlayLivePusherDebugView by detachableView(R.id.live_debug_view)
    private val pinnedMessageView: PinnedMessageView by detachableView(R.id.pinned_msg_view)

    private val actionBarLiveView by viewComponent {
        ActionBarLiveViewComponent(it, object: ActionBarLiveViewComponent.Listener {
            override fun onCameraIconClicked() {
                parentViewModel.switchCamera()
                analytic.clickSwitchCameraOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
            }

            override fun onEndStreamClicked() {
                activity?.onBackPressed()
            }
        })
    }

    /**
     * Interactive
     */
    private val interactiveActiveView by viewComponentOrNull {
        InteractiveActiveViewComponent(it, object : InteractiveActiveViewComponent.Listener {
            override fun onWidgetClicked(view: InteractiveActiveViewComponent) {
                if (view.interactiveType == InteractiveActiveViewComponent.InteractiveType.QUIZ){
                    analytic.onClickOngoingQuiz(
                        parentViewModel.channelId,
                        parentViewModel.channelTitle,
                        parentViewModel.interactiveId,
                        parentViewModel.activeInteractiveTitle,
                    )
                }
                parentViewModel.submitAction(PlayBroadcastAction.ClickOngoingWidget)
            }
    }) }
    private val interactiveFinishedView by viewComponentOrNull { InteractiveFinishViewComponent(it) }

    private val interactiveGameResultViewComponent by viewComponentOrNull { InteractiveGameResultViewComponent(it, object : InteractiveGameResultViewComponent.Listener {
        override fun onGameResultClicked(view: InteractiveGameResultViewComponent) {
            analytic.onClickGameResult(parentViewModel.channelId, parentViewModel.channelTitle)
            parentViewModel.submitAction(PlayBroadcastAction.ClickGameResultWidget)
            view.hideCoachMark()
        }
    }) }

    private val chatListView by viewComponent { ChatListViewComponent(it) }
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

            override fun onPinProductClicked(product: ProductUiModel) {
                parentViewModel.submitAction(PlayBroadcastAction.ClickPinProduct(product))
            }
        })
    }

    /** Game */
    private val gameIconView by viewComponent { GameIconViewComponent(it, object : GameIconViewComponent.Listener {
            override fun onIconClicked() {
                interactiveGameResultViewComponent?.hideCoachMark()
                analytic.onClickInteractiveTool(channelId = parentViewModel.channelId)
                analytic.onClickGameIconButton(channelId = parentViewModel.channelId, channelTitle = parentViewModel.channelTitle)
                openSelectInteractiveSheet()
            }
        })
    }
    private val quizForm: QuizFormView by detachableView(R.id.view_quiz_form)

    private lateinit var exitDialog: DialogUnify
    private lateinit var forceStopDialog: DialogUnify

    private val fragmentViewContainer = FragmentViewContainer()

    private var toasterBottomMargin = 0

    private lateinit var productTagAnalyticHelper: ProductTagAnalyticHelper

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = getViewModelProvider()
            .get(PlayBroadcastViewModel::class.java)
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

        if((activity as? PlayBroadcastActivity)?.isDialogContinueLiveStreamOpen() == false &&
            (activity as? PlayBroadcastActivity)?.isRequiredPermissionGranted() == true)
            parentViewModel.startLiveTimer()

        if (GlobalConfig.DEBUG) setupDebugView(view)
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
            is InteractiveSetupDialogFragment -> {
                childFragment.setDataSource(object : InteractiveSetupDialogFragment.DataSource {
                    override fun getViewModelProvider(): ViewModelProvider {
                        return this@PlayBroadcastUserInteractionFragment.getViewModelProvider()
                    }
                })
            }
        }

        /**
         * Hide coachmark everytime there's a dialog (either floating dialog or bottomsheet)
         */
        if (childFragment is DialogFragment) gameIconView.cancelCoachMark()
    }

    private fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity()),
        )
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
            interactiveGameResultViewComponent?.hideCoachMark()
            gameIconView.cancelCoachMark()
            doShowProductInfo()
            analytic.clickProductTagOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
        pinnedMessageView.setOnPinnedClickedListener { _, message ->
            parentViewModel.submitAction(PlayBroadcastAction.EditPinnedMessage)
            interactiveGameResultViewComponent?.hideCoachMark()
            gameIconView.cancelCoachMark()

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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            quizForm.listen().collect {
                trackQuizFormEvent(it)
                parentViewModel.submitAction(
                    when(it) {
                        QuizFormView.Event.Back -> PlayBroadcastAction.ClickBackOnQuiz
                        QuizFormView.Event.Next -> PlayBroadcastAction.ClickNextOnQuiz
                        is QuizFormView.Event.TitleChanged -> PlayBroadcastAction.InputQuizTitle(it.title)
                        is QuizFormView.Event.OptionChanged -> PlayBroadcastAction.InputQuizOption(it.order, it.text)
                        is QuizFormView.Event.SelectQuizOption -> PlayBroadcastAction.SelectQuizOption(it.order)
                        is QuizFormView.Event.GiftChanged -> PlayBroadcastAction.InputQuizGift(it.gift)
                        is QuizFormView.Event.SaveQuizData -> PlayBroadcastAction.SaveQuizData(it.quizFormData)
                        is QuizFormView.Event.SelectDuration -> PlayBroadcastAction.SelectQuizDuration(it.duration)
                        QuizFormView.Event.Submit -> PlayBroadcastAction.SubmitQuizForm
                        else -> PlayBroadcastAction.Ignore
                    }
                )
            }
        }
    }

    private fun trackQuizFormEvent(event: QuizFormView.Event) {
        when (event) {
            QuizFormView.Event.GiftClicked ->
                analytic.onClickQuizGift(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            QuizFormView.Event.GiftClosed ->
                analytic.onClickCloseQuizGift(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            QuizFormView.Event.Submit ->
                analytic.onClickStartQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            QuizFormView.Event.Close ->
                analytic.onClickBackQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle
                )
            QuizFormView.Event.Next ->
                analytic.onClickContinueQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            QuizFormView.Event.BackSelectDuration ->
                analytic.onClickBackQuizDuration(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            else -> {}
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

    private fun setupDebugView(view: View) {
        val ivSetting = view.findViewById<AppCompatImageView>(R.id.iv_setting)

        ivSetting.show()
        ivSetting.setOnClickListener {
            debugView.show()
        }

        observeLiveInfo()
        observeLiveStats()
    }

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            actionBarLiveView.setTitle(it.title)
        }
    }

    private fun observeLiveInfo() {
        parentViewModel.observableLivePusherInfo.observe(viewLifecycleOwner) {
            when (it) {
                is PlayLiveLogState.Init -> debugView.setLiveInfo(it)
                is PlayLiveLogState.Changed -> debugView.updateState(it.state)
            }
        }
    }

    private fun observeLiveStats() {
        parentViewModel.observableLivePusherStatistic.observe(viewLifecycleOwner) {
            if (it is PlayLivePusherStatistic) {
                debugView.updateStats(it)
            }
        }
    }

    private fun setupObserve() {
        observeLiveState()
        observeLiveDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeEvent()
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
            isQuizFormVisible() -> {
                parentViewModel.submitAction(PlayBroadcastAction.ClickBackOnQuiz)
                true
            }
            /** TODO: gonna delete this */
//            interactiveSetupView.isShown() -> interactiveSetupView.interceptBackPressed()
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
                       parentViewModel.stopLiveStream(shouldNavigate = true)
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

    @SuppressLint("ResourceFragmentDetector")
    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            duration: Int = Toaster.LENGTH_LONG,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            val offset24 = resources.getDimensionPixelOffset(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5
            )
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
        analytic.impressReportPage(parentViewModel.channelId)
    }

    private fun handleLivePushInfo(state: PlayLiveViewState) {
        if (!isVisible) return
        errorLiveNetworkLossView.hide()
        when (state) {
            is PlayLiveViewState.Connecting -> showLoading(true)
            is PlayLiveViewState.Started -> showLoading(false)
            is PlayLiveViewState.Stopped -> {
                showLoading(false)
                val exitDialog = getExitDialog()
                exitDialog.dialogSecondaryCTA.isLoading = false
                exitDialog.dismiss()
                if (state.shouldNavigate) navigateToSummary()
            }
            is PlayLiveViewState.Error -> {
                showLoading(false)
                handleLivePushError(state)
            }
            is PlayLiveViewState.Resume -> {
                showLoading(false)
                if (!state.isResumed) showDialogContinueLiveStreaming()
            }
            is PlayLiveViewState.Recovered -> {
                showLoading(false)
                showToaster(
                        message = getString(R.string.play_live_broadcast_network_recover),
                        type = Toaster.TYPE_NORMAL
                )
            }
        }
    }

    private fun handleLivePushError(state: PlayLiveViewState.Error) {
        when(state.error.type) {
            PlayLivePusherErrorType.NetworkPoor -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_live_broadcast_network_poor),
            )
            PlayLivePusherErrorType.NetworkLoss -> errorLiveNetworkLossView.show()
            PlayLivePusherErrorType.ConnectFailed -> {
                showErrorToaster(
                    err = state.error,
                    customErrMessage = getString(R.string.play_live_broadcast_connect_fail),
                    duration = Toaster.LENGTH_INDEFINITE,
                    actionLabel = getString(R.string.play_broadcast_try_again),
                    actionListener = { parentViewModel.reconnectLiveStream() }
                )
            }
            PlayLivePusherErrorType.SystemError -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_dialog_unsupported_device_desc),
                duration = Toaster.LENGTH_INDEFINITE,
                actionLabel = getString(R.string.play_ok),
                actionListener = { parentViewModel.stopLiveStream(shouldNavigate = true) }
            )
        }
        analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle, state.error.reason)
    }

    private fun showLoading(isLoading: Boolean) {
        loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //region observe
    /**
     * Observe
     */
    private fun observeLiveState() {
        parentViewModel.observableLiveViewState.observe(viewLifecycleOwner, Observer(::handleLivePushInfo))
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeLiveDuration() {
        parentViewModel.observableLiveTimerState.observe(viewLifecycleOwner) {
            when(it)  {
                is PlayLiveTimerState.Active -> showCounterDuration(it.remainingInMs)
                is PlayLiveTimerState.Finish -> {
                    showDialogWhenTimeout()
                }
            }
        }
    }

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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderPinnedMessageView(prevState?.pinnedMessage, state.pinnedMessage)
                renderProductTagView(prevState?.selectedProduct, state.selectedProduct)
                renderQuizForm(
                    prevState?.quizForm,
                    state.quizForm,
                    prevState?.interactiveConfig,
                    state.interactiveConfig
                )
                renderInteractiveView(prevState?.interactive, state.interactive)
                renderGameIconView(
                    prevState?.interactive,
                    state.interactive,
                    prevState?.interactiveConfig,
                    state.interactiveConfig,
                    prevState?.onBoarding,
                    state.onBoarding,
                )

                renderInteractionView(state.interactiveSetup, state.quizForm, state.pinnedMessage)

                renderSetupDialog(
                    prevState?.interactiveSetup,
                    state.interactiveSetup,
                    prevState?.interactive,
                    state.interactive,
                )

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
                    is PlayBroadcastEvent.ShowErrorCreateQuiz -> quizForm.setError(event.error)
                    PlayBroadcastEvent.ShowQuizDetailBottomSheet -> openQuizDetailSheet()
                    PlayBroadcastEvent.ShowLeaderboardBottomSheet -> openLeaderboardSheet()
                    is PlayBroadcastEvent.CreateInteractive.Success -> {
                        analytic.onStartInteractive(
                            channelId = parentViewModel.channelId,
                            interactiveId = parentViewModel.interactiveId,
                            interactiveTitle = parentViewModel.activeInteractiveTitle,
                            durationInMs = event.durationInMs,
                        )
                    }
                    is PlayBroadcastEvent.ShowInteractiveGameResultWidget -> showInteractiveGameResultWidget(event.showCoachMark)
                    PlayBroadcastEvent.DismissGameResultCoachMark -> dismissGameResultCoachMark()
                    else -> {}
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
                else gameIconView.cancelCoachMark()
                formView.setLoading(state.editStatus == PinnedMessageEditStatus.Uploading)
                formView.visibility = View.VISIBLE
            }
            PinnedMessageEditStatus.Nothing -> {
                if (!hasPinnedFormView()) return
                val formView = getPinnedFormView()
                formView.visibility = View.GONE
            }
        }
    }

    private fun renderProductTagView(
        prevState: List<ProductTagSectionUiModel>?,
        state: List<ProductTagSectionUiModel>
    ) {
        if (prevState == state) return

        val sortedList = mutableListOf<ProductUiModel>()
        val newList = state.filterNot { it.campaignStatus.isUpcoming() }
            .flatMap { tagSectionUiModel ->
                tagSectionUiModel.products
            }

        val pinnedProduct = newList.filter { it.pinStatus.isPinned }
        if(pinnedProduct.isNotEmpty()) sortedList.add(pinnedProduct.first())
        sortedList.addAll(newList.filterNot { it.pinStatus.isPinned })

        productTagView.setProducts(
            sortedList
        )
    }

    private fun renderInteractiveView(
        prevState: InteractiveUiModel?,
        state: InteractiveUiModel,
    ) {
        /**
         * Render:
         * - if interactive has changed <b>or</b>
         * - if isPlaying state has changed to not playing
         */
        if (prevState != state) {
            when (state) {
                is InteractiveUiModel.Giveaway -> renderGiveawayView(state)
                is InteractiveUiModel.Quiz -> renderQuizView(state)
                InteractiveUiModel.Unknown -> {
                    interactiveActiveView?.hide()
                    interactiveFinishedView?.hide()
                }
            }
        }
    }

    private fun renderGameIconView(
        prevState: InteractiveUiModel?,
        state: InteractiveUiModel,
        prevConfig: InteractiveConfigUiModel?,
        config: InteractiveConfigUiModel,
        prevOnboarding: OnboardingUiModel?,
        onboarding: OnboardingUiModel,
    ) {
        if (prevState == state &&
            prevConfig == config &&
            prevOnboarding?.firstInteractive == onboarding.firstInteractive) return

        if (state !is InteractiveUiModel.Unknown || config.isNoGameActive()) {
            gameIconView.hide()
        }
        else {
            gameIconView.show()
            if (prevState != state) {
                analytic.onImpressInteractiveTool(parentViewModel.channelId)
                analytic.onImpressGameIconButton(parentViewModel.channelId,parentViewModel.channelTitle)
            }
            if (!hasPinnedFormView() && !isQuizFormVisible() && onboarding.firstInteractive) {
                gameIconView.showCoachmark()
            } else gameIconView.cancelCoachMark()
        }
    }

    private fun renderInteractionView(
        state: InteractiveSetupUiModel,
        quizFormState: QuizFormUiState,
        pinnedState: PinnedMessageUiState,
    ) {
        //Have to be invisible because gone will resulting in not-rounded unify timer
        if (state.type == GameType.Unknown &&
            quizFormState.quizFormState == QuizFormStateUiModel.Nothing &&
            pinnedState.editStatus == PinnedMessageEditStatus.Nothing
        ) {
            clInteraction.visible()
        } else clInteraction.invisible()
    }

    private fun renderGiveawayView(state: InteractiveUiModel.Giveaway) {
        when (val status = state.status) {
            is InteractiveUiModel.Giveaway.Status.Upcoming -> {
                interactiveActiveView?.setUpcomingGiveaway(
                    desc = state.title,
                    targetTime = status.startTime,
                    onDurationEnd = {
                        parentViewModel.submitAction(PlayBroadcastAction.GiveawayUpcomingEnded)
                    }
                )
                interactiveActiveView?.show()
                interactiveFinishedView?.hide()
            }
            is InteractiveUiModel.Giveaway.Status.Ongoing -> {
                interactiveActiveView?.setOngoingGiveaway(
                    desc = state.title,
                    targetTime = status.endTime,
                    onDurationEnd = {
                        parentViewModel.submitAction(PlayBroadcastAction.GiveawayOngoingEnded)
                    }
                )
                interactiveActiveView?.show()
                interactiveFinishedView?.hide()
            }
            InteractiveUiModel.Giveaway.Status.Finished -> {
                interactiveActiveView?.hide()

                interactiveFinishedView?.setupGiveaway()
                interactiveFinishedView?.show()
            }
            InteractiveUiModel.Giveaway.Status.Unknown -> {
                interactiveActiveView?.hide()
                interactiveFinishedView?.hide()
            }
        }
    }

    private fun renderQuizView(state: InteractiveUiModel.Quiz) {
        when (val status = state.status) {
            is InteractiveUiModel.Quiz.Status.Ongoing -> {
                interactiveActiveView?.setQuiz(
                    question = state.title,
                    targetTime = status.endTime,
                    onDurationEnd = {
                        parentViewModel.submitAction(PlayBroadcastAction.QuizEnded)
                    }
                )
                interactiveActiveView?.show()
                analytic.onImpressOngoingQuizWidget(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    state.id,
                    state.title,
                )
                interactiveFinishedView?.hide()
            }
            InteractiveUiModel.Quiz.Status.Finished -> {
                interactiveActiveView?.hide()

                interactiveFinishedView?.setupQuiz()
                interactiveFinishedView?.show()
            }
            InteractiveUiModel.Quiz.Status.Unknown -> {
                interactiveActiveView?.hide()
                interactiveFinishedView?.hide()
            }
        }
    }

    private fun renderSetupDialog(
        prevSetup: InteractiveSetupUiModel?,
        setup: InteractiveSetupUiModel,
        prevInteractive: InteractiveUiModel?,
        interactive: InteractiveUiModel,
    ) {
        if (prevSetup == setup && prevInteractive == interactive) return

        //If seller is not going to setup or if there is active interactive
        //then hide the setup dialog
        if (setup.type == GameType.Unknown ||
            setup.type == GameType.Quiz ||
            interactive !is InteractiveUiModel.Unknown) {
            val dialog = InteractiveSetupDialogFragment.get(childFragmentManager)
            if (dialog?.isAdded == true) dialog.dismiss()
        } else {
            InteractiveSetupDialogFragment.getOrCreate(
                childFragmentManager,
                requireActivity().classLoader
            ).showNow(childFragmentManager)
        }
    }

    private fun renderQuizForm(
        prevState: QuizFormUiState?,
        state: QuizFormUiState,
        prevConfigState: InteractiveConfigUiModel?,
        configState: InteractiveConfigUiModel,
    ) {
        if(prevConfigState != configState)
            quizForm.applyQuizConfig(configState.quizConfig)

        quizForm.setFormData(state.quizFormData, state.isNeedToUpdateUI)

        if(prevState?.quizFormState != state.quizFormState) {
            when(state.quizFormState) {
                QuizFormStateUiModel.Nothing -> {
                    hideKeyboard()
                    showQuizForm(false)
                }
                QuizFormStateUiModel.Preparation -> {
                    showQuizForm(true)
                }
                is QuizFormStateUiModel.SetDuration -> {
                    /** TODO: handle set duration */
                }
            }

            quizForm.setFormState(state.quizFormState)
        }
    }

    private fun showInteractiveGameResultWidget(showCoachMark: Boolean) {
        interactiveGameResultViewComponent?.show()
        if (showCoachMark) {
            interactiveGameResultViewComponent?.showCoachMark("", getString(R.string.play_bro_interactive_game_result_coachmark),)
        }
    }

    private fun dismissGameResultCoachMark() {
        interactiveGameResultViewComponent?.hideCoachMark()
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

    /** Game Region */
    private fun showQuizForm(isShow: Boolean) {
        if(isShow) gameIconView.cancelCoachMark()

        quizForm.showWithCondition(isShow)
    }

    private fun isQuizFormVisible(): Boolean {
        return quizForm.visibility == View.VISIBLE
    }

    private fun openSelectInteractiveSheet() {
        val fragment = PlayBroSelectGameBottomSheet.getFragment(childFragmentManager, requireContext().classLoader)
        fragment.show(childFragmentManager)
    }

    private fun openQuizDetailSheet() {
        val quizDetailBottomSheet = PlayBroInteractiveBottomSheet.setupQuizDetail(
            childFragmentManager,
            requireContext().classLoader
        )
        quizDetailBottomSheet.show(childFragmentManager)
    }

    private fun openLeaderboardSheet() {
        val ongoingLeaderboardBottomSheet = PlayBroInteractiveBottomSheet.setupOngoingLeaderboard(
            childFragmentManager,
            requireContext().classLoader
        )
        ongoingLeaderboardBottomSheet.show(childFragmentManager)
    }

    companion object {
        private const val PINNED_MSG_FORM_TAG = "PINNED_MSG_FORM"
    }

}