package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroSelectGameBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.itemdecoration.SelectGameItemDecoration
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.game.SelectGameViewHolder
import com.tokopedia.play.broadcaster.view.adapter.SelectGameAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class PlayBroSelectGameBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic,
) : BottomSheetUnify(), SelectGameViewHolder.Listener {

    private var _binding: BottomSheetPlayBroSelectGameBinding? = null
    private val binding: BottomSheetPlayBroSelectGameBinding
        get() = _binding!!

    private lateinit var viewModel: PlayBroadcastViewModel

    private val adapter: SelectGameAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        SelectGameAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = BottomSheetPlayBroSelectGameBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)
        showHeader = false

        viewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity()),
        )[PlayBroadcastViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserver()
        analytic.onImpressSelectGame(
            viewModel.channelId,
            viewModel.channelTitle,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * HEIGHT_MULTIPLIER).toInt()
        }

        binding.rvGame.apply {
            addItemDecoration(SelectGameItemDecoration(requireContext()))
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@PlayBroSelectGameBottomSheet.adapter
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderGameOption(it.prevValue?.interactiveConfig, it.value.interactiveConfig)
            }
        }
    }

    private fun renderGameOption(
        prevValue: InteractiveConfigUiModel?,
        value: InteractiveConfigUiModel
    ) {
        if(value == prevValue) return

        adapter.setItemsAndAnimateChanges(value.availableGameList().map {
            SelectGameAdapter.Model.Item(it)
        })
    }

    override fun onGameOptionClick(gameType: GameType) {
        viewModel.submitAction(PlayBroadcastAction.ClickGameOption(gameType))
        when (gameType) {
            is GameType.Quiz -> analytic.onClickGameOption( viewModel.channelId, viewModel.channelTitle, KEY_QUIZ_ANALYTIC)
        }
        dismiss()
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        private const val HEIGHT_MULTIPLIER = 0.65f

        private const val TAG = "SelectGameBottomSheet"
        private const val KEY_QUIZ_ANALYTIC = "quiz"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroSelectGameBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroSelectGameBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                                        classLoader,
                                        PlayBroSelectGameBottomSheet::class.java.name
                                    ) as PlayBroSelectGameBottomSheet
        }
    }
}