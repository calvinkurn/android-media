package com.tokopedia.play.broadcaster.view.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.updateScrollingChild
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroQuizDetailBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.view.partial.game.QuizOptionDetailViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayGameLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.play_common.R as commonR

class PlayBroInteractiveBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic,
    private val router: Router,
) : BottomSheetUnify(), PlayGameLeaderboardViewComponent.Listener,
    QuizOptionDetailViewComponent.Listener {

    private val sheetType
        get() = arguments?.getString(ARG_TYPE) ?: Type.UNKNOWN.sheetType
    private val sheetSize
        get() = arguments?.getString(ARG_SIZE) ?: Size.HALF.tag

    private val leaderboardSheetView by viewComponent {
        PlayGameLeaderboardViewComponent(
            it,
            this
        )
    }

    private val choiceDetailSheetView by viewComponent {
        QuizOptionDetailViewComponent(
            it,
            this
        )
    }

    private var _binding: BottomSheetPlayBroQuizDetailBinding? = null
    private val binding: BottomSheetPlayBroQuizDetailBinding
        get() = _binding!!

    private val parentViewModel: PlayBroadcastViewModel by activityViewModels {
        parentViewModelFactoryCreator.create(
            requireActivity()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            BottomSheetPlayBroQuizDetailBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)
        showHeader = false
        clearContentPadding = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        leaderboardSheetView.addItemTouchListener(object :
            RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                bottomSheet.updateScrollingChild(rv)
                return false
            }
        })
        leaderboardSheetView.clearTopPadding()

        choiceDetailSheetView.addOnTouchListener { v, _, _, _, _ ->
            bottomSheet.updateScrollingChild(v)
        }

        when (Size.mapFromString(sheetSize)) {
            Size.HALF -> {
                binding.root.layoutParams = binding.root.layoutParams.apply {
                    height = (getScreenHeight() * HEIGHT_MULTIPLIER).toInt()
                }
            }
            Size.FULL -> {
                binding.root.layoutParams = binding.root.layoutParams.apply {
                    height = (getScreenHeight() * 1f).toInt()
                }
            }
        }
        if (Type.mapFromString(sheetType) != Type.UNKNOWN) {
            when (Type.mapFromString(sheetType)) {
                Type.QUIZ_DETAIL -> setupQuizDetail()
                Type.LEADERBOARD -> setupLeaderBoard()
                Type.REPORT -> setupReport()
            }
            observeQuizDetail()
        }
    }

    private fun setupQuizDetail() {
        leaderboardSheetView.setTitle(getString(com.tokopedia.play.broadcaster.R.string.play_bro_ongoing_bottomsheet_title))
        parentViewModel.getQuizDetailData()
    }

    private fun setupLeaderBoard() {
        leaderboardSheetView.setTitle(getString(commonR.string.play_interactive_leaderboard_title))
        parentViewModel.getLeaderboardWithSlots()
    }

    private fun setupReport() {
        leaderboardSheetView.setTitle(getString(commonR.string.play_interactive_leaderboard_title))
        parentViewModel.getLeaderboardWithSlots(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQuizDetail() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            parentViewModel.uiState.collectLatest {
                when (it.quizBottomSheetUiState.quizDetailState) {
                    is QuizDetailStateUiModel.Error -> leaderboardSheetView.setError()
                    QuizDetailStateUiModel.Loading -> leaderboardSheetView.setLoading()
                    is QuizDetailStateUiModel.Success -> {
                        setUIModel(
                            it.quizBottomSheetUiState.quizDetailState.leaderboardSlots,
                            it.quizBottomSheetUiState.quizChoiceDetailState
                        )
                    }
                    QuizDetailStateUiModel.Empty -> {
                        if (isVisible)
                            dismiss()
                    }
                }
            }
        }
    }

    private fun setUIModel(
        listOfLeaderboardUiModel: List<LeaderboardGameUiModel>,
        quizChoiceDetailState: QuizChoiceDetailStateUiModel
    ) {
        leaderboardSheetView.setData(listOfLeaderboardUiModel)
        when (quizChoiceDetailState) {
            is QuizChoiceDetailStateUiModel.Error -> showErrorChoiceDetail()
            QuizChoiceDetailStateUiModel.Loading -> showLoadingChoiceDetail()
            is QuizChoiceDetailStateUiModel.Success -> showChoiceDetail(quizChoiceDetailState.dataUiModel)
            QuizChoiceDetailStateUiModel.Empty -> showQuizDetail()
        }
    }

    private fun showQuizDetail() {
        leaderboardSheetView.show()
        choiceDetailSheetView.hide()
    }


    private fun showLoadingChoiceDetail() {
        leaderboardSheetView.hide()
        choiceDetailSheetView.setLoading()
        choiceDetailSheetView.show()
    }

    private fun showChoiceDetail(dataUiModel: QuizChoiceDetailUiModel) {
        leaderboardSheetView.hide()
        choiceDetailSheetView.setData(dataUiModel, isQuizDetail())
        choiceDetailSheetView.show()
        when (Type.mapFromString(sheetType)) {
            Type.QUIZ_DETAIL -> {
                analytic.onImpressQuizChoiceDetail(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    dataUiModel.choice.interactiveId,
                    dataUiModel.choice.interactiveTitle,
                )
            }
            Type.REPORT -> {
                analytic.onImpressReportQuizChoiceDetail(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    dataUiModel.choice.interactiveId,
                    dataUiModel.choice.interactiveTitle,
                )
            }
        }

    }

    private fun showErrorChoiceDetail() {
        leaderboardSheetView.hide()
        choiceDetailSheetView.show()
        choiceDetailSheetView.setError()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    override fun onCloseButtonClicked(view: PlayGameLeaderboardViewComponent) {
        when (Type.mapFromString(sheetType)) {
            Type.QUIZ_DETAIL -> {
                analytic.onClickCloseOngoingQuizBottomSheet(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    parentViewModel.interactiveId,
                    parentViewModel.activeInteractiveTitle,
                )
            }
            Type.LEADERBOARD -> {
                analytic.onClickCloseGameResultBottomsheet(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            }
            Type.REPORT -> {
                analytic.onClickCloseGameResultReport(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                )
            }
        }
        dismiss()
    }

    override fun onChatWinnerButtonClicked(
        view: PlayGameLeaderboardViewComponent,
        winner: LeaderboardGameUiModel.Winner,
        position: Int
    ) {
        analytic.onClickChatWinnerIcon(
            parentViewModel.channelId,
            parentViewModel.channelTitle,
        )
        router.route(
            requireContext(),
            "${ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG}${ADDITIONAL_ARG}",
            winner.id,
            winner.topChatMessage
        )
    }

    override fun onRefreshButtonClicked(view: PlayGameLeaderboardViewComponent) {
        if (!isQuizDetail()) {
            analytic.onClickRefreshGameResult(
                parentViewModel.channelId,
                parentViewModel.channelTitle,
            )
        }
        parentViewModel.submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
    }

    override fun onChoiceItemClicked(item: QuizChoicesUiModel) {
        when (Type.mapFromString(sheetType)) {
            Type.QUIZ_DETAIL -> {
                analytic.onCLickQuizOptionLive(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    item.interactiveId,
                    item.interactiveTitle,
                )
            }
            Type.REPORT -> {
                analytic.onCLickQuizOptionReport(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    item.interactiveId,
                    item.interactiveTitle,
                )
            }
        }
        parentViewModel.submitAction(PlayBroadcastAction.ClickQuizChoiceOption(item))
    }

    override fun onRefreshButtonImpressed(view: PlayGameLeaderboardViewComponent) {
        analytic.onImpressFailedLeaderboard(parentViewModel.channelId, parentViewModel.channelTitle)
    }

    override fun onLeaderBoardImpressed(
        view: PlayGameLeaderboardViewComponent,
        leaderboard: LeaderboardGameUiModel.Header
    ) {
        when (Type.mapFromString(sheetType)) {
            Type.LEADERBOARD ->
                analytic.onImpressOngoingLeaderBoard(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    leaderboard.id,
                    leaderboard.title,
                    leaderboard.reward,
                )
            Type.REPORT ->
                analytic.onImpressReportLeaderboard(
                    parentViewModel.channelId,
                    parentViewModel.channelTitle,
                    leaderboard.id,
                    leaderboard.title,
                    leaderboard.leaderBoardType.toString().lowercase(),
                )
        }
    }

    override fun onBackButtonClicked(view: QuizOptionDetailViewComponent) {
        analytic.onClickBackQuizChoiceDetail(
            parentViewModel.channelId,
            parentViewModel.channelTitle,
            parentViewModel.interactiveId,
            parentViewModel.activeInteractiveTitle,
        )
        parentViewModel.submitAction(PlayBroadcastAction.ClickBackOnChoiceDetail)
    }

    override fun onRefreshButtonClicked(view: QuizOptionDetailViewComponent) {
        parentViewModel.submitAction(PlayBroadcastAction.ClickRefreshQuizOption)
    }

    override fun loadMoreParticipant() {
        parentViewModel.submitAction(PlayBroadcastAction.LoadMoreCurrentChoiceParticipant)
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.submitAction(PlayBroadcastAction.DismissQuizDetailBottomSheet)
        super.onDismiss(dialog)
    }


    companion object {
        private const val HEIGHT_MULTIPLIER = 0.65f

        private const val ARG_TYPE = "ARG_TYPE"
        private const val ARG_SIZE = "ARG_SIZE"
        private const val ADDITIONAL_ARG = "&source=tx_ask_buyer"

        private const val TAG = "PlayQuizDetailBottomSheet"
        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            type: Type,
            size: Size,
        ): PlayBroInteractiveBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroInteractiveBottomSheet
            return oldInstance ?: (fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroInteractiveBottomSheet::class.java.name,
            ) as PlayBroInteractiveBottomSheet).apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type.sheetType)
                    putString(ARG_SIZE, size.tag)
                }
            }
        }

        fun setupQuizDetail(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroInteractiveBottomSheet {
            return getFragment(fragmentManager, classLoader, Type.QUIZ_DETAIL, Size.HALF)
        }

        fun setupOngoingLeaderboard(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroInteractiveBottomSheet {
            return getFragment(fragmentManager, classLoader, Type.LEADERBOARD, Size.HALF)
        }

        fun setupReportLeaderboard(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroInteractiveBottomSheet {
            return getFragment(fragmentManager, classLoader, Type.REPORT, Size.FULL)
        }
    }

    private fun isQuizDetail() = sheetType == Type.QUIZ_DETAIL.sheetType

    enum class Type(val sheetType: String) {
        LEADERBOARD("leaderboard"),
        QUIZ_DETAIL("quiz_detail"),
        REPORT("report"),
        UNKNOWN("");

        companion object {
            fun mapFromString(sheetType: String): Type {
                return values().firstOrNull {
                    it.sheetType == sheetType
                } ?: UNKNOWN
            }
        }
    }

    enum class Size(val tag: String) {
        FULL("full"),
        HALF("half"),
        UNKNOWN("");

        companion object {
            fun mapFromString(tag: String): Size {
                return values().firstOrNull {
                    it.tag == tag
                } ?: UNKNOWN
            }
        }
    }
}
