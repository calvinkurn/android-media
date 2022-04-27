package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroQuizDetailBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class PlayQuizDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
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
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity())
        )[PlayBroadcastViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentViewModel.getQuizDetailData()
        observeQuizDetail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQuizDetail() {
        parentViewModel.observableQuizDetailState.observe(viewLifecycleOwner) {
            when (it) {
                QuizDetailStateUiModel.Error -> leaderboardSheetView.setError()
                QuizDetailStateUiModel.Loading -> leaderboardSheetView.setLoading()
                is QuizDetailStateUiModel.Success -> setUIModel(it.dataUiModel)
                QuizDetailStateUiModel.Unknown -> TODO()
            }
        }
    }

    private fun setUIModel(dataUiModel: QuizDetailDataUiModel) {
        val lb = PlayLeaderboardUiModel(
            title = dataUiModel.question,
            reward = dataUiModel.reward,
            choices = dataUiModel.choices.map {
                QuizChoicesUiModel(
                    id = it.id,
                    text = it.text,
                    type = PlayQuizOptionState.Unknown,
                    isLoading = false,
                )
            },
            endsIn = dataUiModel.countDownEnd,
            otherParticipant = 0,
            otherParticipantText = "",
            winners = emptyList()
        )
        leaderboardSheetView.setData(listOf(lb))
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
        TODO("Not yet implemented")
    }

    override fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        TODO("Not yet implemented")
    }
}