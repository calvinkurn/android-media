package com.tokopedia.play.broadcaster.view.fragment.summary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.*
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPostVideoBinding
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.TagUiState
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 07, 2022
 */
class PlayBroadcastPostVideoFragment @Inject constructor(
    private val analytic: PlayBroadcastAnalytic,
    private val userSession: UserSessionInterface
) : PlayBaseBroadcastFragment(), TagListViewComponent.Listener {

    private var _binding: FragmentPlayBroadcastPostVideoBinding? = null
    private val binding: FragmentPlayBroadcastPostVideoBinding
        get() = _binding!!

    private val tagListView by viewComponent { TagListViewComponent(it, binding.rvTagsRecommendation.id, this) }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private lateinit var viewModel: PlayBroadcastSummaryViewModel

    override fun getScreenName(): String = "Play Post Video Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), (parentFragment as ViewModelFactoryProvider).getFactory()).get(
            PlayBroadcastSummaryViewModel::class.java)

        setupTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastPostVideoBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
            is PlayBroadcastSetupBottomSheet -> {
                childFragment.setListener(object: PlayBroadcastSetupBottomSheet.Listener {
                    override fun onCoverChanged(cover: PlayCoverUiModel) {
                        viewModel.submitAction(
                            PlayBroadcastSummaryAction.SetCover(cover)
                        )
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return viewModel.productList
                    }

                    override fun getChannelId(): String {
                        return viewModel.channelId
                    }
                })
            }
        }
    }

    private fun setupView() {
        binding.icBroSummaryBack.setOnClickListener {
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickBackToReportPage)
        }

        binding.clCoverPreview.setOnClickListener {
            analytic.clickCoverOnReportPage(viewModel.channelId, viewModel.channelTitle)
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickEditCover)
        }

        binding.btnPostVideo.setOnClickListener {
            analytic.clickPostingVideoNow()
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
        }
    }

    private fun setupObservable() {
        observeUiState()
        observeEvent()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderCoverInfo(it.prevValue?.channelSummary, it.value.channelSummary)
                renderTag(it.prevValue?.tag, it.value.tag)
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    PlayBroadcastSummaryEvent.BackToReportPage -> requireActivity().onBackPressed()
                    PlayBroadcastSummaryEvent.OpenSelectCoverBottomSheet -> openCoverSetupFragment()
                    is PlayBroadcastSummaryEvent.PostVideo -> {
                        when (val networkResult = it.networkResult) {
                            is NetworkResult.Loading -> binding.btnPostVideo.isLoading = true
                            is NetworkResult.Success -> openShopPageWithBroadcastStatus(true)
                            is NetworkResult.Fail -> {
                                binding.btnPostVideo.isLoading = false
                                toaster.showError(
                                    err = networkResult.error,
                                    customErrMessage = networkResult.error.localizedMessage
                                        ?: getString(R.string.play_broadcaster_default_error),
                                    actionLabel = getString(R.string.play_broadcast_try_again),
                                    actionListener = { networkResult.onRetry() }
                                )
                            }
                        }
                    }
                    else -> { }
                }
            }
        }
    }

    private fun renderCoverInfo(prev: ChannelSummaryUiState?, value: ChannelSummaryUiState) {
        if(prev == value || value.isEmpty()) return

        binding.clCoverPreview.apply {
            setCoverWithPlaceholder(value.coverUrl)
            setTitle(value.title)
            setShopName(viewModel.shopName)
        }
    }

    private fun renderTag(prev: TagUiState?, value: TagUiState) {
        if(prev == value) return

        tagListView.setTags(value.tags.toList())
    }

    private fun openShopPageWithBroadcastStatus(isSaved: Boolean) {
        if (activity?.callingActivity == null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, userSession.shopId)
                .putExtra(NEWLY_BROADCAST_CHANNEL_SAVED, isSaved)
            startActivity(intent)
            activity?.finish()
        } else {
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().putExtra(NEWLY_BROADCAST_CHANNEL_SAVED, isSaved)
            )
            activity?.finish()
        }
    }

    private fun openCoverSetupFragment() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    /**
     * Listener
     */
    override fun onTagClicked(view: TagListViewComponent, tag: PlayTagUiModel) {
        analytic.clickContentTag(tag.tag, !tag.isChosen)
        viewModel.submitAction(PlayBroadcastSummaryAction.ToggleTag(tag))
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupReturnTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.IN))
            .setDuration(300)

        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(450)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.OUT))
            .setDuration(250)

        sharedElementReturnTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(450)
    }

    companion object {
        private const val NEWLY_BROADCAST_CHANNEL_SAVED = "EXTRA_NEWLY_BROADCAST_SAVED"
    }
}