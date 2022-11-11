package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsSummaryBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsSummaryFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = TAG

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsSummaryBinding? = null
    private val binding: FragmentPlayShortsSummaryBinding get() = _binding!!

    private val tagListView by viewComponent {
        TagListViewComponent(it, binding.layoutTagRecommendation, object : TagListViewComponent.Listener {
            override fun onTagClicked(view: TagListViewComponent, tag: PlayTagUiModel) {
                /** TODO: handle this */
            }

            override fun onTagRefresh(view: TagListViewComponent) {
                /** TODO: handle this */
            }
        })
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
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        /** TODO: setup cover, name, pict, title here */
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderTag(it.prevValue, it.value)
                renderUploadButton(it.prevValue, it.value)
            }
        }
    }

    private fun renderTag(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if(prev?.tags == curr.tags) return

        when(curr.tags) {
            is NetworkResult.Loading -> tagListView.setPlaceholder()
            is NetworkResult.Success -> tagListView.setTags(curr.tags.data.toList())
            is NetworkResult.Fail -> tagListView.setError()
        }
    }

    private fun renderUploadButton(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if(prev?.tags == curr.tags) return

        val isTagSelected = if(curr.tags is NetworkResult.Success) {
            curr.tags.data.firstOrNull { it.isChosen } != null
        }
        else {
            false
        }

        binding.btnUploadVideo.isEnabled = isTagSelected
    }

    companion object {
        private const val TAG = "PlayShortsSummaryFragment"

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
