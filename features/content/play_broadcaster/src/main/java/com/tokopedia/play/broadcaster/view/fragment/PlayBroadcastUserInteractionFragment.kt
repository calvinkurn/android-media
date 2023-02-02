package com.tokopedia.play.broadcaster.view.fragment

import android.annotation.SuppressLint
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
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterErrorType
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.producttag.ProductTagAnalyticHelper
import com.tokopedia.play.broadcaster.domain.model.PinnedProductException
import com.tokopedia.play.broadcaster.pusher.PlayBroadcaster
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimerState
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.manager.PlayBroadcastToasterManager
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
import com.tokopedia.play.broadcaster.ui.state.OnboardingUiModel
import com.tokopedia.play.broadcaster.ui.state.PinnedMessageUiState
import com.tokopedia.play.broadcaster.ui.state.QuizFormUiState
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroInteractiveBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroSelectGameBottomSheet
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
import com.tokopedia.play.broadcaster.view.partial.*
import com.tokopedia.play.broadcaster.view.partial.game.GameIconViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val pinnedMessageView: PinnedMessageView by detachableView(R.id.pinned_msg_view)

    private val actionBarLiveView by viewComponent {
        ActionBarLiveViewComponent(
            it,
            object : ActionBarLiveViewComponent.Listener {
                override fun onCameraIconClicked() {
                    analytic.clickSwitchCameraOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                    broadcaster.flip()
                }

                override fun onEndStreamClicked() {
                    activity?.onBackPressed()
                }
            }
        )
    }

    /**
     * Interactive
     */
    private val interactiveActiveView by viewComponentOrNull {
        InteractiveActiveViewComponent(
            it,
            object : InteractiveActiveViewComponent.Listener {
                override fun onWidgetClicked(view: InteractiveActiveViewComponent) {
                    if (view.interactiveType == InteractiveActiveViewComponent.InteractiveType.QUIZ) {
                        analytic.onClickOngoingQuiz(
                            parentViewModel.channelId,
                            parentViewModel.channelTitle,
                            parentViewModel.interactiveId,
                            parentViewModel.activeInteractiveTitle
                        )
                    }
                    parentViewModel.submitAction(PlayBroadcastAction.ClickOngoingWidget)
                }
            }
        )
    }
    private val interactiveFinishedView by viewComponentOrNull { InteractiveFinishViewComponent(it) }

    private val interactiveGameResultViewComponent by viewComponentOrNull {
        InteractiveGameResultViewComponent(
            it,
            object : InteractiveGameResultViewComponent.Listener {
                override fun onGameResultClicked(view: InteractiveGameResultViewComponent) {
                    analytic.onClickGameResult(parentViewModel.channelId, parentViewModel.channelTitle)
                    parentViewModel.submitAction(PlayBroadcastAction.ClickGameResultWidget)
                    view.hideCoachMark()
                }
            }
        )
    }

    private val chatListView by viewComponent { ChatListViewComponent(it) }
    private val productTagView by viewComponent {
        ProductTagViewComponent(
            it,
            object : ProductTagViewComponent.Listener {
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

                override fun onPinClicked(product: ProductUiModel) {
                    analytic.onClickPinProductLiveRoom(product.id)
                    parentViewModel.submitAction(PlayBroadcastAction.ClickPinProduct(product))
                }

                override fun onImpressPinnedProduct(product: ProductUiModel) {
                    analytic.onImpressPinProductLiveRoom(product.id)
                }
            },
            scope = this.lifecycleScope
        )
    }

    /** Game */
    private val gameIconView by viewComponent {
        GameIconViewComponent(
            it,
            object : GameIconViewComponent.Listener {
                override fun onIconClicked() {
                    interactiveGameResultViewComponent?.hideCoachMark()
                    analytic.onClickInteractiveTool(channelId = parentViewModel.channelId)
                    analytic.onClickGameIconButton(channelId = parentViewModel.channelId, channelTitle = parentViewModel.channelTitle)
                    openSelectInteractiveSheet()
                    productTagView.hideCoachMark()
                }
            }
        )
    }
    private val quizForm: QuizFormView by detachableView(R.id.view_quiz_form)

    private lateinit var exitDialog: DialogUnify
    private lateinit var forceStopDialog: DialogUnify
    private var pauseLiveDialog: DialogUnify? = null

    private val fragmentViewContainer = FragmentViewContainer()

    private lateinit var productTagAnalyticHelper: ProductTagAnalyticHelper

    private var isPausedFragment = false

    private val toasterManager = PlayBroadcastToasterManager(this)

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
                        // TODO("Revamp this")
                        return if (::parentViewModel.isInitialized) {
                            parentViewModel.productSectionList
                        } else {
                            emptyList()
                        }
                    }

                    override fun isEligibleForPin(): Boolean = true

                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return parentViewModel.uiState.value.selectedContentAccount
                    }

                    override fun creationId(): String {
                        return parentViewModel.channelId
                    }

                    override fun maxProduct(): Int {
                        return parentViewModel.maxProduct
                    }
                })

                childFragment.setListener(object : ProductSetupFragment.Listener {
                    override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
                        parentViewModel.submitAction(
                            PlayBroadcastAction.SetProduct(productTagSectionList)
                        )
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
    }

    private fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity())
        )
    }

    private fun initAnalytic() {
        productTagAnalyticHelper = ProductTagAnalyticHelper(analytic)
    }

    private fun setupView() {
        observeTitle()
        actionBarLiveView.setAuthorImage(parentViewModel.getAuthorImage())

        ivShareLink.setOnClickListener {
            doCopyShareLink()
            analytic.clickShareIconOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
        iconProduct.setOnClickListener {
            interactiveGameResultViewComponent?.hideCoachMark()
            gameIconView.cancelCoachMark()
            doShowProductInfo()
            analytic.clickProductTagOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
            productTagView.hideCoachMark()
        }
        pinnedMessageView.setOnPinnedClickedListener { _, message ->
            parentViewModel.submitAction(PlayBroadcastAction.EditPinnedMessage)
            interactiveGameResultViewComponent?.hideCoachMark()
            gameIconView.cancelCoachMark()

            if (message.isBlank()) {
                analytic.clickAddPinChatMessage(
                    channelId = parentViewModel.channelId,
                    titleChannel = parentViewModel.channelTitle
                )
            } else {
                analytic.clickEditPinChatMessage(
                    channelId = parentViewModel.channelId,
                    titleChannel = parentViewModel.channelTitle
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            quizForm.listen().collect {
                trackQuizFormEvent(it)
                parentViewModel.submitAction(
                    when (it) {
                        QuizFormView.Event.Back -> PlayBroadcastAction.ClickBackOnQuiz
                        QuizFormView.Event.Next -> PlayBroadcastAction.ClickNextOnQuiz
                        is QuizFormView.Event.TitleChanged -> PlayBroadcastAction.InputQuizTitle(it.title)
                        is QuizFormView.Event.OptionChanged -> PlayBroadcastAction.InputQuizOption(it.order, it.text)
                        is QuizFormView.Event.SelectQuizOption -> PlayBroadcastAction.SelectQuizOption(it.order)
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
            QuizFormView.Event.Submit ->
                analytic.onClickStartQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle
                )
            QuizFormView.Event.Close ->
                analytic.onClickBackQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle
                )
            QuizFormView.Event.Next ->
                analytic.onClickContinueQuiz(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle
                )
            QuizFormView.Event.BackSelectDuration ->
                analytic.onClickBackQuizDuration(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle
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

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            actionBarLiveView.setTitle(it.title)
        }
    }

    private fun setupObserve() {
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeEvent()
        observeUiState()
        observeUiEvent()
        observeBroadcastTimerState()
    }

    override fun onResume() {
        super.onResume()
        if (isPausedFragment) {
            resumeBroadcast(false)
            isPausedFragment = false
        }
    }

    override fun onPause() {
        super.onPause()
        isPausedFragment = true
        pauseBroadcast()
        productTagAnalyticHelper.sendTrackingProduct()
        parentViewModel.sendLogs()
    }

    /**
     * Dismissing all dialog -> not ideal because the it doesn't eliminate the root cause
     * Need to revamp the flow of stopping live stream and all
     */
    override fun onDestroyView() {
        if (::exitDialog.isInitialized) getExitDialog().dismiss()
        if (::forceStopDialog.isInitialized) forceStopDialog.dismiss()

        toasterManager.dismissActiveToaster()

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
                    analytic.clickDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                    stopBroadcast()
                    navigateToSummary()
                }
            )
        }
        return exitDialog
    }

    private fun showForceStopDialog(
        title: String = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
        message: String = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
        buttonTitle: String = getString(R.string.play_live_broadcast_dialog_end_timeout_primary),
        buttonListener: () -> Unit = {
            analytic.clickDialogSeeReportOnLivePage(
                parentViewModel.channelId,
                parentViewModel.channelTitle
            )
            navigateToSummary()
        }
    ) {
        if (!::forceStopDialog.isInitialized) {
            forceStopDialog = requireContext().getDialog(
                title = title,
                desc = message,
                primaryCta = buttonTitle,
                primaryListener = { dialog ->
                    dialog.dismiss()
                    buttonListener()
                }
            )
        }
        if (!forceStopDialog.isShowing) {
            analytic.viewDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
            forceStopDialog.show()
            dismissPauseDialog()
        }
    }

    private fun showDialogContinueLive() {
        if (pauseLiveDialog == null) {
            pauseLiveDialog = context?.getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    analytic.clickDialogContinueBroadcastOnLivePage(
                        parentViewModel.channelId,
                        parentViewModel.channelTitle
                    )
                    resumeBroadcast(shouldContinue = true)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    stopBroadcast()
                    navigateToSummary()
                }
            )
        }

        if (pauseLiveDialog?.isShowing == false) {
            pauseLiveDialog?.show()
            analytic.viewDialogContinueBroadcastOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
    }

    private fun dismissPauseDialog() {
        if (pauseLiveDialog?.isShowing == true) pauseLiveDialog?.dismiss()
    }

    private fun showErrorToaster(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        toasterManager.showErrorToaster(
            err = err,
            customErrMessage = customErrMessage,
            duration = duration,
            actionLabel = actionLabel,
            actionListener = actionListener
        )
    }

    @SuppressLint("ResourceFragmentDetector")
    private fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        toasterManager.showToaster(
            message = message,
            type = type,
            duration = duration,
            actionLabel = actionLabel,
            actionListener = actionListener
        )
    }

    private fun doCopyShareLink() {
        PlayShareWrapper.copyToClipboard(requireContext(), parentViewModel.shareContents) {
            showToaster(
                message = getString(R.string.play_live_broadcast_share_link_copied),
                actionLabel = getString(R.string.play_ok)
            )
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

    private fun handleBroadcastRecovered() {
        showLoading(false)
        errorLiveNetworkLossView.hide()
        showToaster(getString(R.string.play_live_broadcast_network_recover))
    }

    private fun handleBroadcastError(error: Throwable) {
        analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle, error.localizedMessage)
        if (error is BroadcasterException) {
            when (error.errorType) {
                BroadcasterErrorType.InternetUnavailable,
                BroadcasterErrorType.StreamFailed -> {
                    errorLiveNetworkLossView.show()
                    reconnectLiveStreaming()
                }
                BroadcasterErrorType.AuthFailed,
                BroadcasterErrorType.UrlEmpty,
                BroadcasterErrorType.ServiceNotReady, -> {
                    showErrorToaster(
                        error,
                        getString(R.string.play_live_broadcast_connect_fail),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = {
                            parentViewModel.doResumeBroadcaster(shouldContinue = true)
                        }
                    )
                }
                BroadcasterErrorType.StartFailed -> {
                    showErrorToaster(
                        error,
                        getString(R.string.play_broadcaster_default_error),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = {
                            showLoading(true)
                            reconnectLiveStreaming()
                        }
                    )
                }
                else -> {
                    showErrorToaster(
                        error,
                        getString(R.string.play_live_broadcast_unrecoverable_error),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_ok),
                        actionListener = {
                            activity?.finish()
                        }
                    )
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //region observe
    /**
     * Observe
     */

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeChatList() {
        parentViewModel.observableChatList.observe(
            viewLifecycleOwner,
            object : Observer<List<PlayChatUiModel>> {
                override fun onChanged(chatList: List<PlayChatUiModel>) {
                    setChatList(chatList)
                    parentViewModel.observableChatList.removeObserver(this)
                }
            }
        )

        parentViewModel.observableNewChat.observe(viewLifecycleOwner, EventObserver(::setNewChat))
    }

    private fun observeMetrics() {
        parentViewModel.observableNewMetrics.observe(viewLifecycleOwner, EventObserver(::setNewMetrics))
    }

    private fun observeEvent() {
        parentViewModel.observableEvent.observe(viewLifecycleOwner) {
            when {
                it.freeze -> showForceStopDialog()
                it.banned -> showForceStopDialog(
                    title = it.title,
                    message = it.message,
                    buttonTitle = it.buttonTitle
                ) {
                    navigateToSummary()
                }
            }
            stopBroadcast()
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
                renderInteractiveView(prevState?.game, state.game)
                renderGameIconView(
                    prevState?.game,
                    state.game,
                    prevState?.interactiveConfig,
                    state.interactiveConfig,
                    prevState?.onBoarding,
                    state.onBoarding
                )

                renderInteractionView(state.interactiveSetup, state.quizForm, state.pinnedMessage)

                renderSetupDialog(
                    prevState?.interactiveSetup,
                    state.interactiveSetup,
                    prevState?.game,
                    state.game,
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
                showLoading(false)
                when (event) {
                    is PlayBroadcastEvent.ShowErrorCreateQuiz -> quizForm.setError(event.error)
                    PlayBroadcastEvent.ShowQuizDetailBottomSheet -> openQuizDetailSheet()
                    PlayBroadcastEvent.ShowLeaderboardBottomSheet -> openLeaderboardSheet()
                    is PlayBroadcastEvent.CreateInteractive.Success -> {
                        analytic.onStartInteractive(
                            channelId = parentViewModel.channelId,
                            interactiveId = parentViewModel.interactiveId,
                            interactiveTitle = parentViewModel.activeInteractiveTitle,
                            durationInMs = event.durationInMs
                        )
                    }
                    is PlayBroadcastEvent.ShowInteractiveGameResultWidget -> showInteractiveGameResultWidget(event.showCoachMark)
                    PlayBroadcastEvent.DismissGameResultCoachMark -> dismissGameResultCoachMark()
                    is PlayBroadcastEvent.FailPinUnPinProduct -> {
                        if (event.isPinned) {
                            analytic.onImpressFailUnPinProductLiveRoom()
                        } else {
                            analytic.onImpressFailPinProductLiveRoom()
                        }

                        if (event.throwable is PinnedProductException) {
                            analytic.onImpressColdDownPinProductSecondEvent(true)
                            showToaster(
                                message = if (event.throwable.message.isEmpty()) getString(R.string.play_bro_pin_product_failed) else event.throwable.message,
                                type = Toaster.TYPE_ERROR
                            )
                        } else {
                            showErrorToaster(event.throwable)
                        }
                    }
                    PlayBroadcastEvent.ShowLoading -> showLoading(true)
                    PlayBroadcastEvent.ShowLiveEndedDialog -> {
                        stopBroadcast()
                        showForceStopDialog()
                    }
                    PlayBroadcastEvent.ShowResumeLiveDialog -> showDialogContinueLive()
                    is PlayBroadcastEvent.ShowError -> {
                        if (event.onRetry == null) {
                            showErrorToaster(event.error)
                        } else {
                            showErrorToaster(
                                event.error,
                                duration = Toaster.LENGTH_INDEFINITE,
                                actionLabel = getString(R.string.play_broadcast_try_again),
                                actionListener = {
                                    event.onRetry.invoke()
                                }
                            )
                        }
                    }
                    is PlayBroadcastEvent.BroadcastReady -> {
                        startBroadcast(event.ingestUrl)
                        parentViewModel.startTimer()
                    }
                    is PlayBroadcastEvent.ShowBroadcastError -> handleBroadcastError(event.error)
                    is PlayBroadcastEvent.BroadcastRecovered -> handleBroadcastRecovered()
                    else -> {}
                }
            }
        }
    }

    private fun observeBroadcastTimerState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.broadcastTimerStateChanged.collectLatest { state ->
                when (state) {
                    is PlayBroadcastTimerState.Active -> showCounterDuration(state.duration)
                    PlayBroadcastTimerState.Finish -> {
                        stopBroadcast()
                        showForceStopDialog()
                    }
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
                titleChannel = parentViewModel.channelTitle
            )
        }

        pinnedMessageView.setMode(
            if (state.message.isEmpty()) {
                PinnedMessageView.Mode.Empty
            } else {
                PinnedMessageView.Mode.Filled(state.message)
            }
        )

        when (state.editStatus) {
            PinnedMessageEditStatus.Editing,
            PinnedMessageEditStatus.Uploading -> {
                val formView = getPinnedFormView()
                if (formView.visibility != View.VISIBLE) {
                    formView.setPinnedMessage(state.message)
                } else {
                    gameIconView.cancelCoachMark()
                }
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
        val newList = state.flatMap { tagSectionUiModel ->
            tagSectionUiModel.products
        }

        val pinnedProduct = newList.filter { it.pinStatus.isPinned }
        if (pinnedProduct.isNotEmpty()) {
            sortedList.add(pinnedProduct.first())
        }
        sortedList.addAll(newList.filterNot { it.pinStatus.isPinned })

        productTagView.setProducts(
            sortedList
        )
    }

    private fun renderInteractiveView(
        prevState: GameUiModel?,
        state: GameUiModel,
    ) {
        /**
         * Render:
         * - if game has changed <b>or</b>
         * - if isPlaying state has changed to not playing
         */
        if (prevState != state) {
            when (state) {
                is GameUiModel.Giveaway -> renderGiveawayView(state)
                is GameUiModel.Quiz -> renderQuizView(state)
                GameUiModel.Unknown -> {
                    interactiveActiveView?.hide()
                    interactiveFinishedView?.hide()
                }
            }
        }
    }

    private fun renderGameIconView(
        prevState: GameUiModel?,
        state: GameUiModel,
        prevConfig: InteractiveConfigUiModel?,
        config: InteractiveConfigUiModel,
        prevOnboarding: OnboardingUiModel?,
        onboarding: OnboardingUiModel
    ) {
        if (prevState == state &&
            prevConfig == config &&
            prevOnboarding?.firstInteractive == onboarding.firstInteractive
        ) {
            return
        }

        if (state !is GameUiModel.Unknown || config.isNoGameActive() || config.availableGameList().isEmpty()) {
            gameIconView.hide()
        } else {
            gameIconView.show()
            if (prevState != state) {
                analytic.onImpressInteractiveTool(parentViewModel.channelId)
                analytic.onImpressGameIconButton(parentViewModel.channelId, parentViewModel.channelTitle)
            }
            if (!hasPinnedFormView() && !isQuizFormVisible() && onboarding.firstInteractive) {
                gameIconView.showCoachmark()
            } else {
                gameIconView.cancelCoachMark()
            }
        }
    }

    private fun renderInteractionView(
        state: InteractiveSetupUiModel,
        quizFormState: QuizFormUiState,
        pinnedState: PinnedMessageUiState
    ) {
        // Have to be invisible because gone will resulting in not-rounded unify timer
        if (state.type == GameType.Unknown &&
            quizFormState.quizFormState == QuizFormStateUiModel.Nothing &&
            pinnedState.editStatus == PinnedMessageEditStatus.Nothing
        ) {
            clInteraction.visible()
        } else {
            clInteraction.invisible()
        }
    }

    private fun renderGiveawayView(state: GameUiModel.Giveaway) {
        when (val status = state.status) {
            is GameUiModel.Giveaway.Status.Upcoming -> {
                interactiveActiveView?.setUpcomingGiveaway(
                    title = state.title,
                    targetTime = status.startTime,
                    onDurationEnd = {
                        parentViewModel.submitAction(PlayBroadcastAction.GiveawayUpcomingEnded)
                    }
                )
                interactiveActiveView?.show()
                interactiveFinishedView?.hide()
            }
            is GameUiModel.Giveaway.Status.Ongoing -> {
                interactiveActiveView?.setOngoingGiveaway(
                    title = state.title,
                    targetTime = status.endTime,
                    onDurationEnd = {
                        parentViewModel.submitAction(PlayBroadcastAction.GiveawayOngoingEnded)
                    }
                )
                interactiveActiveView?.show()
                interactiveFinishedView?.hide()
            }
            GameUiModel.Giveaway.Status.Finished -> {
                interactiveActiveView?.hide()

                interactiveFinishedView?.setupGiveaway()
                interactiveFinishedView?.show()
            }
            GameUiModel.Giveaway.Status.Unknown -> {
                interactiveActiveView?.hide()
                interactiveFinishedView?.hide()
            }
        }
    }

    private fun renderQuizView(state: GameUiModel.Quiz) {
        when (val status = state.status) {
            is GameUiModel.Quiz.Status.Ongoing -> {
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
                    state.title
                )
                interactiveFinishedView?.hide()
            }
            GameUiModel.Quiz.Status.Finished -> {
                interactiveActiveView?.hide()

                interactiveFinishedView?.setupQuiz()
                interactiveFinishedView?.show()
            }
            GameUiModel.Quiz.Status.Unknown -> {
                interactiveActiveView?.hide()
                interactiveFinishedView?.hide()
            }
        }
    }

    private fun renderSetupDialog(
        prevSetup: InteractiveSetupUiModel?,
        setup: InteractiveSetupUiModel,
        prevGame: GameUiModel?,
        game: GameUiModel,
    ) {
        if (prevSetup == setup && prevGame == game) return

        //If seller is not going to setup or if there is active game
        //then hide the setup dialog
        if (setup.type == GameType.Unknown ||
            setup.type == GameType.Quiz ||
            game !is GameUiModel.Unknown) {
            val dialog = InteractiveSetupDialogFragment.get(childFragmentManager)
            if (dialog?.isAdded == true) dialog.dismiss()
        } else {
            gameIconView.cancelCoachMark()
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
        configState: InteractiveConfigUiModel
    ) {
        if (prevConfigState != configState) {
            quizForm.applyQuizConfig(configState.quizConfig)
        }

        quizForm.setFormData(state.quizFormData, state.isNeedToUpdateUI)

        if (prevState?.quizFormState != state.quizFormState) {
            when (state.quizFormState) {
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
            interactiveGameResultViewComponent?.showCoachMark("", getString(R.string.play_bro_interactive_game_result_coachmark))
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

    /** Game Region */
    private fun showQuizForm(isShow: Boolean) {
        if (isShow) gameIconView.cancelCoachMark()

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

    private fun startBroadcast(ingestUrl: String) {
        broadcaster.start(ingestUrl)
    }

    private fun pauseBroadcast() {
        if (parentViewModel.isBroadcastStopped) return
        showLoading(false)
        errorLiveNetworkLossView.hide()
        broadcaster.pause()
    }

    private fun resumeBroadcast(shouldContinue: Boolean) {
        if (parentViewModel.isBroadcastStopped) return
        broadcaster.resume(shouldContinue)
    }

    private fun reconnectLiveStreaming() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(PlayBroadcaster.RETRY_TIMEOUT)
            broadcaster.retry()
        }
    }

    private fun stopBroadcast() {
        broadcaster.stop()
        parentViewModel.stopTimer()
    }

    companion object {
        private const val PINNED_MSG_FORM_TAG = "PINNED_MSG_FORM"
    }
}
