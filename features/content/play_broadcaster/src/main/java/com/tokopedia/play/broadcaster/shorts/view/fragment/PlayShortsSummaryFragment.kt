package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsSummaryBinding
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalytic
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.shorts.view.bottomsheet.InterspersingConfirmationBottomSheet
import com.tokopedia.play.broadcaster.shorts.view.compose.PlayShortsSummaryConfigLayout
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import com.tokopedia.play.broadcaster.R
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsSummaryFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val analytic: PlayShortsAnalytic,
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = TAG

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsSummaryBinding? = null
    private val binding: FragmentPlayShortsSummaryBinding get() = _binding!!

    private val tagListView by viewComponent {
        TagListViewComponent(
            it, binding.layoutTagRecommendation,
            object : TagListViewComponent.Listener {
                override fun onTagClicked(view: TagListViewComponent, tag: PlayTagItem) {
                    analytic.clickContentTag(tag.tag, viewModel.selectedAccount)

                    viewModel.submitAction(PlayShortsAction.SelectTag(tag))
                }

                override fun onTagRefresh(view: TagListViewComponent) {
                    analytic.clickRefreshContentTag(viewModel.selectedAccount)

                    viewModel.submitAction(PlayShortsAction.LoadTag)
                }
            }
        )
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setAttachedFragment()
        super.onCreate(savedInstanceState)
    }

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

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {

                val uiState by viewModel.uiState.collectAsStateWithLifecycle(PlayShortsUiState.Empty)

                PlayShortsSummaryConfigLayout(
                    tagsState = uiState.tags,
                    isInterspersingVideoAllowed = uiState.interspersingConfig.isInterspersingAllowed,
                    isInterspersing = uiState.interspersingConfig.isInterspersing,
                    onRefreshTag = {
                        analytic.clickRefreshContentTag(viewModel.selectedAccount)

                        viewModel.submitAction(PlayShortsAction.LoadTag)
                    },
                    onTagClick = { item ->
                        analytic.clickContentTag(item.tag, viewModel.selectedAccount)

                        viewModel.submitAction(PlayShortsAction.SelectTag(item))
                    },
                    onInterspersingChanged = { isActive ->
                        analytic.clickInterspersingToggle(viewModel.selectedAccount, viewModel.shortsId, isActive)
                        viewModel.submitAction(PlayShortsAction.SwitchInterspersing)
                    }
                )
            }
        }

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analytic.openScreenSummaryPage(viewModel.selectedAccount)

        setupListener()
        setupObserver()

        viewModel.submitAction(PlayShortsAction.LoadTag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAttachedFragment() {
        childFragmentManager.addFragmentOnAttachListener { _, childFragment ->
            when (childFragment) {
                is InterspersingConfirmationBottomSheet -> {
                    childFragment.data = InterspersingConfirmationBottomSheet.Data(
                        newCoverUri = viewModel.coverUri,
                        oldCoverUri = viewModel.productVideo.coverUrl,
                        needSnapNewCover = !viewModel.isCoverSelected
                    )

                    childFragment.listener = object : InterspersingConfirmationBottomSheet.Listener {
                        override fun clickPdpVideo() {
                            analytic.clickVideoPdpCard(viewModel.selectedAccount, viewModel.shortsId)
                            viewModel.submitAction(PlayShortsAction.ClickVideoPreview)
                        }

                        override fun clickNext() {
                            analytic.clickNextInterspersingConfirmation(viewModel.selectedAccount, viewModel.shortsId)
                            viewModel.submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = false))
                        }

                        override fun clickClose() {
                            analytic.clickCloseInterspersingConfirmation(viewModel.selectedAccount, viewModel.shortsId)
                        }

                        override fun clickBack() {
                            analytic.clickBackInterspersingConfirmation(viewModel.selectedAccount, viewModel.shortsId)
                        }
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.icBack.setOnClickListener {
            analytic.clickBackOnSummaryPage(viewModel.selectedAccount)

            activity?.onBackPressed()
        }

        binding.btnUploadVideo.setOnClickListener {
            analytic.clickUploadVideo(viewModel.shortsId, viewModel.selectedAccount)

            viewModel.submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
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
                        showErrorToaster(event.throwable)
                    }
                    is PlayShortsUiEvent.ErrorCheckInterspersing -> {
                        analytic.impressInterspersingError(viewModel.selectedAccount, viewModel.shortsId)
                        showErrorToaster(
                            throwable = event.throwable,
                            customErrMessage = getString(R.string.play_shorts_interspersing_video_failed_check_pdp_video),
                            actionLabel = getString(R.string.play_broadcast_try_again),
                        ) {
                            viewModel.submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
                        }
                    }
                    is PlayShortsUiEvent.ShowInterspersingConfirmation -> {
                        showInterspersingConfirmation()
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

        binding.ivCover.setImageUrl(viewModel.coverUri)
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
            is NetworkResult.Success -> tagListView.setTags(
                tags = curr.tags.data.tags.toList(),
                maxTags = curr.tags.data.maxTags,
            )
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
                        if(curr.tags.data.tags.isEmpty()) true
                        else curr.tags.data.tags.count { it.isChosen } in curr.tags.data.minTags..curr.tags.data.maxTags
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

    private fun showErrorToaster(
        throwable: Throwable,
        customErrMessage: String? = null,
        actionLabel: String = "",
        actionListener: () -> Unit = {},
    ) {
        toaster.showError(
            throwable,
            customErrMessage = customErrMessage,
            duration = Toaster.LENGTH_LONG,
            actionLabel = actionLabel,
            actionListener = { actionListener() },
        )
    }

    private fun showInterspersingConfirmation() {
        InterspersingConfirmationBottomSheet
            .getFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
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
