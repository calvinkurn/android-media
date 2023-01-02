package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsSummaryBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsSummaryFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = TAG

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsSummaryBinding? = null
    private val binding: FragmentPlayShortsSummaryBinding get() = _binding!!

    private val tagListView by viewComponent {
        TagListViewComponent(
            it, binding.layoutTagRecommendation,
            object : TagListViewComponent.Listener {
                override fun onTagClicked(view: TagListViewComponent, tag: PlayTagUiModel) {
                    viewModel.submitAction(PlayShortsAction.SelectTag(tag))
                }

                override fun onTagRefresh(view: TagListViewComponent) {
                    viewModel.submitAction(PlayShortsAction.LoadTag)
                }
            }
        )
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayShortsSummaryBinding.inflate(
            inflater,
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        setupObserver()

        viewModel.submitAction(PlayShortsAction.LoadTag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        /** TODO: setup cover, name, pict, title here */
    }

    private fun setupListener() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnUploadVideo.setOnClickListener {
            viewModel.submitAction(PlayShortsAction.ClickUploadVideo)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderCover(it.prevValue, it.value)
                renderSummaryInfo(it.prevValue, it.value)
                renderTag(it.prevValue, it.value)
                renderUploadButton(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is PlayShortsUiEvent.ErrorUploadMedia -> {
                        toaster.showError(
                            event.throwable,
                            duration = Toaster.LENGTH_SHORT
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderCover(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.coverForm == curr.coverForm) return

        if (curr.coverForm.coverUri.isEmpty()) {
            binding.ivCover.setImageUrl(curr.media.mediaUri)
        } else {
            binding.ivCover.setImageUrl(curr.coverForm.coverUri)
        }
    }

    private fun renderSummaryInfo(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.titleForm != curr.titleForm) {
            binding.tvShortTitle.text = curr.titleForm.title
        }

        if (prev?.selectedAccount != curr.selectedAccount) {
            binding.tvName.text = MethodChecker.fromHtml(curr.selectedAccount.name)
            binding.ivProfile.setImageUrl(curr.selectedAccount.iconUrl)
        }
    }

    private fun renderTag(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.tags == curr.tags) return

        when (curr.tags) {
            is NetworkResult.Loading -> tagListView.setPlaceholder()
            is NetworkResult.Success -> tagListView.setTags(curr.tags.data.toList())
            is NetworkResult.Fail -> tagListView.setError()
            else -> {}
        }
    }

    private fun renderUploadButton(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.tags == curr.tags && prev.uploadState == curr.uploadState) return

        when(curr.uploadState) {
            is PlayShortsUploadUiState.Success -> {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            is PlayShortsUploadUiState.Loading -> {
                binding.btnUploadVideo.isLoading = true
                binding.btnUploadVideo.isEnabled = false
            }
            else -> {
                /**
                 * Enabled if :
                 * 1. Tags available && min. choose 1 tag
                 * 2. Tag is empty
                 * 3. Error load tag
                 */
                val isButtonEnabled = when(curr.tags) {
                    is NetworkResult.Success -> {
                        if(curr.tags.data.isEmpty()) true
                        else curr.tags.data.firstOrNull { it.isChosen } != null
                    }
                    is NetworkResult.Fail -> {
                        true
                    }
                    else -> false
                }

                binding.btnUploadVideo.isLoading = false
                binding.btnUploadVideo.isEnabled = isButtonEnabled
            }
        }
    }

    companion object {
        const val TAG = "PlayShortsSummaryFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsSummaryFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayShortsSummaryFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsSummaryFragment::class.java.name
            ) as PlayShortsSummaryFragment
        }
    }
}
