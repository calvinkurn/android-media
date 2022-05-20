package com.tokopedia.play.broadcaster.view.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroQuizDetailBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.view.partial.game.QuizOptionDetailViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PlayQuizDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator
) : BottomSheetUnify(), PlayInteractiveLeaderboardViewComponent.Listener,
    QuizOptionDetailViewComponent.Listener {

    private val sheetType
        get() = arguments?.getString(ARG_TYPE) ?: ""

    private val leaderboardSheetView by viewComponent {
        PlayInteractiveLeaderboardViewComponent(
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

    private lateinit var parentViewModel: PlayBroadcastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            BottomSheetPlayBroQuizDetailBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)
        showHeader = false
        clearContentPadding = true
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity())
        )[PlayBroadcastViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.65f).toInt()
        }
        if (sheetType.isNotBlank()){
            when (sheetType){
                Type.QUIZ_DETAIL.toString().lowercase() -> setupQuizDetail()
                Type.LEADERBOARD.toString().lowercase() -> setupLeaderBoard()
            }
            observeQuizDetail()
        }
    }

    private fun setupQuizDetail(){
        leaderboardSheetView.setTitle(getString(com.tokopedia.play.broadcaster.R.string.play_bro_ongoing_bottomsheet_title))
        parentViewModel.getQuizDetailData()
    }

    private fun setupLeaderBoard(){
        leaderboardSheetView.setTitle(getString(commonR.string.play_interactive_leaderboard_title))
        parentViewModel.getLeaderboardWithSlots()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQuizDetail() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            parentViewModel.uiState.collectLatest {
                when (it.quizBottomSheetUiState.quizDetailState) {
                    QuizDetailStateUiModel.Error -> leaderboardSheetView.setError()
                    QuizDetailStateUiModel.Loading -> leaderboardSheetView.setLoading()
                    is QuizDetailStateUiModel.Success -> {
                        setUIModel(
                            it.quizBottomSheetUiState.quizDetailState.leaderboardSlots,
                            it.quizBottomSheetUiState.quizChoiceDetailState
                        )
                    }
                    QuizDetailStateUiModel.Unknown -> {
                        if (isVisible)
                            dismiss()
                    }
                }
            }
        }
    }

    private fun setUIModel(
        listOfLeaderboardUiModel: List<PlayLeaderboardUiModel>,
        quizChoiceDetailState: QuizChoiceDetailStateUiModel
    ) {
        leaderboardSheetView.setData(listOfLeaderboardUiModel)
        when (quizChoiceDetailState) {
            QuizChoiceDetailStateUiModel.Error -> showErrorChoiceDetail()
            QuizChoiceDetailStateUiModel.Loading -> showLoadingChoiceDetail()
            is QuizChoiceDetailStateUiModel.Success -> showChoiceDetail(quizChoiceDetailState.dataUiModel)
            QuizChoiceDetailStateUiModel.Unknown -> showQuizDetail()
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
        choiceDetailSheetView.setData(dataUiModel, isOngoingBottomsheet())
        choiceDetailSheetView.show()

    }

    private fun showErrorChoiceDetail() {
        leaderboardSheetView.hide()
        choiceDetailSheetView.show()
        choiceDetailSheetView.setError()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    override fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        dismiss()
    }

    override fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        parentViewModel.submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
    }

    override fun onChoiceItemClicked(item: QuizChoicesUiModel) {
        parentViewModel.submitAction(PlayBroadcastAction.ClickQuizChoiceOption(item))
    }

    override fun onBackButtonClicked(view: QuizOptionDetailViewComponent) {
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
        const val ARG_TYPE = "ARG_TYPE"

        private const val TAG = "PlayQuizDetailBottomSheet"
        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayQuizDetailBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayQuizDetailBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayQuizDetailBottomSheet::class.java.name
            ) as PlayQuizDetailBottomSheet
        }
    }

    private fun isOngoingBottomsheet() = sheetType == Type.QUIZ_DETAIL.toString().lowercase()

    enum class Type {
        LEADERBOARD,
        QUIZ_DETAIL
    }
}