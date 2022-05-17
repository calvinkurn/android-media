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
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.view.partial.game.QuizOptionDetailViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PlayQuizDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val playBroadcastMapper: PlayBroadcastMapper,
) : BottomSheetUnify(), PlayInteractiveLeaderboardViewComponent.Listener,
    QuizOptionDetailViewComponent.Listener {

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
        leaderboardSheetView.setTitle(getString(com.tokopedia.play.broadcaster.R.string.play_bro_ongoing_bottomsheet_title))
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.65f).toInt()
        }
        parentViewModel.getQuizDetailData()
        observeQuizDetail()
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
                        leaderboardSheetView.setError() /*for debugging layout purpose*/
//                        setUIModel(
//                            it.quizBottomSheetUiState.quizDetailState.dataUiModel,
//                            it.quizBottomSheetUiState.quizChoiceDetailState
//                        )
                    }
                    QuizDetailStateUiModel.Unknown -> dismiss()
                }
            }
        }
    }

    private fun setUIModel(
        quizDetailDataUiModel: QuizDetailDataUiModel,
        quizChoiceDetailState: QuizChoiceDetailStateUiModel
    ) {
        leaderboardSheetView.setData(
            listOf(
                playBroadcastMapper.mapQuizDetailToLeaderBoard(
                    quizDetailDataUiModel
                )
            )
        )
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

    }

    private fun showChoiceDetail(dataUiModel: QuizChoiceDetailUiModel) {
        leaderboardSheetView.hide()
        choiceDetailSheetView.setData(dataUiModel)
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

    companion object {
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

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.submitAction(PlayBroadcastAction.DismissQuizDetailBottomSheet)
        super.onDismiss(dialog)
    }
}