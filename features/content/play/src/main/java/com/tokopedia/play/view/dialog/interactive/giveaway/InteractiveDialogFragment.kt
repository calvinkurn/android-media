package com.tokopedia.play.view.dialog.interactive.giveaway

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.custom.interactive.follow.InteractiveFollowView
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.QuizAnsweredEvent
import com.tokopedia.play.view.uimodel.event.ShowErrorEvent
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.GiveawayWidgetView
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.view.game.quiz.QuizWidgetView
import com.tokopedia.play_common.view.game.setupGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class InteractiveDialogFragment @Inject constructor(
    private val userSession: UserSessionInterface,
    private val analytic: PlayNewAnalytic,
) : DialogFragment() {

    private var mDataSource: DataSource? = null

    private lateinit var viewModel: PlayViewModel

    private val job = SupervisorJob()
    private val scope = CoroutineScope(CoroutineDispatchersProvider.main + job)

    private val followViewListener = object : InteractiveFollowView.Listener {
        override fun onFollowImpressed(view: InteractiveFollowView) {
            analytic.impressFollowShopInteractive(
                shopId = viewModel.partnerId.toString(),
                gameType = viewModel.gameData,
                channelId = viewModel.channelId
            )
        }

        override fun onFollowClicked(view: InteractiveFollowView) {
            viewModel.submitAction(PlayViewerNewAction.FollowInteractive)
            analytic.clickFollowShopInteractive(
                interactiveId = viewModel.gameData.id,
                shopId = viewModel.partnerId.toString(),
                gameType = viewModel.gameData,
                channelId = viewModel.channelId,
                channelType = viewModel.channelType
            )
        }
    }

    private val giveawayViewListener = object : GiveawayWidgetView.Listener {
        override fun onTapTapClicked(view: GiveawayWidgetView) {
            viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
            analytic.clickTapTap(
                interactiveId = viewModel.gameData.id,
                channelId = viewModel.channelId,
                channelType = viewModel.channelType
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vmProvider = mDataSource?.getViewModelProvider() ?: ViewModelProvider(this)
        viewModel = vmProvider.get(PlayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FrameLayout(inflater.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.submitAction(PlayViewerNewAction.StopPlayingInteractive)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun showNow(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    private fun setupView() {
        view?.setOnClickListener { dismiss() }
    }

    private fun setupObserve() {
        observeUiState()
        observeUiEvent()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                if (!state.interactive.isPlaying) {
                    dismiss()
                    return@collectLatest
                }

                when (state.interactive.game) {
                    is GameUiModel.Giveaway -> renderGiveawayDialog(
                        state.interactive.game,
                        state.partner
                    )
                    is GameUiModel.Quiz -> renderQuizDialog(
                        state.interactive.game,
                        state.partner
                    )
                }

            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowErrorEvent -> {
                        val errMsg = ErrorHandler.getErrorMessage(
                            context, event.error, ErrorHandler.Builder()
                                .className(PlayViewModel::class.java.simpleName).build()
                        )
                        doShowToaster(toasterType = Toaster.TYPE_ERROR, message = errMsg)
                    }
                    is QuizAnsweredEvent -> {
                        val widget = currentWidget
                        if (widget is QuizWidgetView) {
                            widget.animateAnswer(event.isTrue)
                        }
                        dismissDialog()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun dismissDialog(){
        job.cancelChildren()
        scope.launch {
            delay(FADE_TRANSITION_DELAY)
            dismiss()
        }
    }

    private fun renderGiveawayDialog(
        giveaway: GameUiModel.Giveaway,
        partner: PlayPartnerInfo,
    ) {
        val giveawayStatus = giveaway.status
        if (partner.status == PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed) ||
            !userSession.isLoggedIn
        ) {
            setChildView { ctx ->
                InteractiveFollowView(ctx).apply {
                    setListener(followViewListener)
                }
            }.apply {
                setBadgeUrl(partner.badgeUrl)
                setAvatarUrl(partner.iconUrl)
                setPartnerName(partner.name)
                setLoading(partner.isLoadingFollow)
                getHeader().setupGiveaway(giveaway.title)
            }
        } else if (giveawayStatus is GameUiModel.Giveaway.Status.Ongoing) {
            setChildView { ctx ->
                GiveawayWidgetView(ctx).apply {
                    setListener(giveawayViewListener)
                }
            }.apply {
                setTitle(giveaway.title)
                setTargetTime(giveawayStatus.endTime) {
                    viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                }
                getHeader().isEditable = false
            }
        }
    }

    private fun renderQuizDialog(
        quiz: GameUiModel.Quiz,
        partner: PlayPartnerInfo,
    ) {
        val status = quiz.status
        when {
            partner.status == PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed) ||
                    !userSession.isLoggedIn -> {
                setChildView { ctx ->
                    val view = InteractiveFollowView(ctx)
                    view.setListener(followViewListener)
                    view
                }.apply {
                    setBadgeUrl(partner.badgeUrl)
                    setAvatarUrl(partner.iconUrl)
                    setPartnerName(partner.name)
                    setLoading(partner.isLoadingFollow)
                    getHeader().setupQuiz(quiz.title)
                }
            }
            status is GameUiModel.Quiz.Status.Ongoing -> {
                setChildView { ctx ->
                    QuizWidgetView(ctx)
                }.apply {
                    setTitle(quiz.title)
                    setTargetTime(status.endTime) {
                        viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                    }
                    setupQuizForm(quiz.listOfChoices)
                    setListener(object : QuizWidgetView.Listener {
                        override fun onQuizOptionClicked(item: QuizChoicesUiModel) {
                            viewModel.submitAction(PlayViewerNewAction.ClickQuizOptionAction(item))
                            analytic.clickQuizOption(
                                interactiveId = viewModel.gameData.id,
                                shopId = viewModel.partnerId.toString(),
                                choiceAlphabet = if (item.type is PlayQuizOptionState.Default) (item.type as PlayQuizOptionState.Default).alphabet.toString() else "",
                                channelId = viewModel.channelId
                            )
                        }

                        override fun onQuizImpressed() {
                            analytic.impressQuizOptions(
                                shopId = viewModel.partnerId.toString(),
                                interactiveId = viewModel.gameData.id,
                                channelId = viewModel.channelId
                            )
                        }
                    })
                    getHeader().isEditable = false
                }
            }
        }
    }

    private fun doShowToaster(
        toasterType: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        message: String,
    ) {
        Toaster.build(
            view = requireView(),
            message,
            type = toasterType,
            actionText = actionText
        ).show()
    }

    private inline fun <reified V : View> setChildView(
        viewCreator: (Context) -> V
    ): V {
        val parent = view as ViewGroup
        val firstChild = parent.getChildAt(0)
        return if (firstChild !is V) {
            parent.removeAllViews()
            val view = viewCreator(parent.context)
            val lParams = FrameLayout.LayoutParams(
                (WIDTH_PERCENTAGE * getScreenWidth()).toInt(),
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lParams.gravity = Gravity.CENTER
            parent.addView(view, lParams)
            view.isClickable = true
            view
        } else firstChild
    }

    private val currentWidget: View?
        get() {
            val parent = view as ViewGroup
            return if (parent.childCount < 0) null
            else parent.getChildAt(0)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }

    companion object {
        private const val TAG = "InteractiveDialogFragment"

        private const val FADE_TRANSITION_DELAY = 3000L

        private const val WIDTH_PERCENTAGE = 0.6

        fun get(fragmentManager: FragmentManager): InteractiveDialogFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? InteractiveDialogFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): InteractiveDialogFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                InteractiveDialogFragment::class.java.name
            ) as InteractiveDialogFragment
        }
    }

    interface DataSource {

        fun getViewModelProvider(): ViewModelProvider
    }
}
