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
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class PlayQuizDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
) : BottomSheetUnify() {

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
            when (it){
                is QuizDetailStateUiModel.Error -> TODO()
                is QuizDetailStateUiModel.Loading -> {}
                is QuizDetailStateUiModel.Success -> setUIModel(it.dataUiModel)
                is QuizDetailStateUiModel.Unknown -> TODO()
            }
        }
    }

    private fun setUIModel(dataUiModel: QuizDetailDataUiModel) {
        binding.tvBroQuizDetailQuestion.text = dataUiModel.question
        binding.tvRewards.text = dataUiModel.reward
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
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
}