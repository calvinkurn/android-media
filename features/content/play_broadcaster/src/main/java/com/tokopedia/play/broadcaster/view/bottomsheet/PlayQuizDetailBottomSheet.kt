package com.tokopedia.play.broadcaster.view.bottomsheet

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
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PlayQuizDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val playBroadcastMapper: PlayBroadcastMapper,
) : BottomSheetUnify(), PlayInteractiveLeaderboardViewComponent.Listener {

    private val leaderboardSheetView by viewComponent {
        PlayInteractiveLeaderboardViewComponent(
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
        setTitle("Respon quiz")
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
                when (it.quizDetail) {
                    QuizDetailStateUiModel.Error -> leaderboardSheetView.setError()
                    QuizDetailStateUiModel.Loading -> leaderboardSheetView.setLoading()
                    is QuizDetailStateUiModel.Success -> setUIModel(it.quizDetail.dataUiModel)
                    QuizDetailStateUiModel.Unknown -> leaderboardSheetView.setError()
                }
            }
        }
    }

    private fun setUIModel(dataUiModel: QuizDetailDataUiModel) {
        leaderboardSheetView.setData(
            listOf(
                playBroadcastMapper.mapQuizDetailToLeaderBoard(
                    dataUiModel
                )
            )
        )
        leaderboardSheetView.showWithHeight((getScreenHeight()*0.6).toInt())
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
        parentViewModel.submitAction(PlayBroadcastAction.ClickCloseQuizDetailBottomSheet)
    }

    override fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        parentViewModel.submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
    }
}