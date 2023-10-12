package com.tokopedia.play.broadcaster.view.fragment.summary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.navigation.broadcaster.PlayBroadcasterArgument
import com.tokopedia.content.common.navigation.people.UserProfileNavigation
import com.tokopedia.content.common.navigation.people.UserProfileParam
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.util.Router
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPostVideoBinding
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.TagUiState
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet.Companion.TAB_AUTO_GENERATED
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet.Companion.TAB_UPLOAD_IMAGE
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet.DataSource
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 07, 2022
 */
class PlayBroadcastPostVideoFragment @Inject constructor(
    private val analytic: PlayBroadcastAnalytic,
    private val userSession: UserSessionInterface,
    private val router: Router,
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator
) : PlayBaseBroadcastFragment(),
    TagListViewComponent.Listener,
    PlayBroadcastSetupCoverBottomSheet.Listener {

    private val parentViewModel: PlayBroadcastViewModel by activityViewModels {
        parentViewModelFactoryCreator.create(
            requireActivity()
        )
    }

    private var _binding: FragmentPlayBroadcastPostVideoBinding? = null
    private val binding: FragmentPlayBroadcastPostVideoBinding
        get() = _binding!!

    private val tagListView by viewComponent { TagListViewComponent(it, binding.layoutTagRecommendation, this) }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private lateinit var viewModel: PlayBroadcastSummaryViewModel

    override fun getScreenName(): String = "Play Post Video Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), (parentFragment as ViewModelFactoryProvider).getFactory()).get(
            PlayBroadcastSummaryViewModel::class.java
        )

        setupTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastPostVideoBinding.inflate(
            LayoutInflater.from(requireContext())
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
        when (childFragment) {
            is PlayBroadcastSetupCoverBottomSheet -> {
                childFragment.setupListener(listener = this)
                childFragment.setupDataSource(
                    dataSource = object : DataSource {
                        override fun getEntryPoint(): String {
                            return PAGE_NAME
                        }

                        override fun getContentAccount(): ContentAccountUiModel {
                            return parentViewModel.selectedAccount
                        }

                        override fun getChannelId(): String {
                            return parentViewModel.channelId
                        }

                        override fun getChannelTitle(): String {
                            return parentViewModel.channelTitle
                        }

                        override fun getDataStore(): PlayBroadcastDataStore {
                            return parentViewModel.mDataStore
                        }
                    }
                )

                val isShowCoachMark = parentViewModel.isShowSetupCoverCoachMark
                childFragment.needToShowCoachMark(isShowCoachMark)
                if (isShowCoachMark) parentViewModel.submitAction(PlayBroadcastAction.SetShowSetupCoverCoachMark)
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
                when (it) {
                    PlayBroadcastSummaryEvent.BackToReportPage -> requireActivity().onBackPressed()
                    PlayBroadcastSummaryEvent.OpenSelectCoverBottomSheet -> openSetupCoverBottomSheet()
                    is PlayBroadcastSummaryEvent.PostVideo -> {
                        when (val networkResult = it.networkResult) {
                            is NetworkResult.Loading -> binding.btnPostVideo.isLoading = true
                            is NetworkResult.Success -> redirectAfterPostVideo()
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
                            else -> {
                                // no-op
                            }
                        }
                    }
                    else -> { }
                }
            }
        }
    }

    private fun renderCoverInfo(prev: ChannelSummaryUiState?, value: ChannelSummaryUiState) {
        if (prev == value || value.isEmpty()) return

        binding.clCoverPreview.apply {
            setCoverWithPlaceholder(value.coverUrl)
            setTitle(value.title)
            setAuthorName(viewModel.shopName)
        }
    }

    private fun renderTag(prev: NetworkResult<TagUiState>?, value: NetworkResult<TagUiState>) {
        if (prev == value) return

        when (value) {
            NetworkResult.Loading -> tagListView.setPlaceholder()
            is NetworkResult.Success -> tagListView.setTags(value.data.tags.toList())
            is NetworkResult.Fail -> tagListView.setError()
            else -> {
                // no-op
            }
        }
    }

    private fun redirectAfterPostVideo() {
        if (GlobalConfig.isSellerApp()) {
            /** Keep existing flow */
            if (activity?.callingActivity == null) {
                val intent = router.getIntent(context, ApplinkConst.SHOP, userSession.shopId)
                    .putExtraForPostVideoRedirection()
                startActivity(intent)
                activity?.finish()
            } else {
                activity?.setResult(
                    Activity.RESULT_OK,
                    Intent().putExtraForPostVideoRedirection()
                )
                activity?.finish()
            }
        } else {
            /** Go to Feed */
            val intent = router.getIntent(context, ApplinkConst.FEED)
                .putExtraForPostVideoRedirection()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun Intent.putExtraForPostVideoRedirection(): Intent {
        return this.putExtra(PlayBroadcasterArgument.NEWLY_BROADCAST_CHANNEL_SAVED, true)
            .putExtra(PlayBroadcasterArgument.EXTRA_SEE_TRANSCODING_CHANNEL_APPLINK, generateSeeTranscodingChannelAppLink())
    }

    private fun generateSeeTranscodingChannelAppLink(): String {
        return if (viewModel.account.isUser) {
            UserProfileNavigation.generateAppLink(viewModel.account.id) {
                setSelectedTab(UserProfileParam.SelectedTab.Video)
            }
        } else if (viewModel.account.isShop) {
            UriUtil.buildUri(ApplinkConst.SHOP, viewModel.account.id)
        } else {
            ""
        }
    }

    private fun openSetupCoverBottomSheet() {
        childFragmentManager.executePendingTransactions()
        val existingFragment = childFragmentManager.findFragmentByTag(
            PlayBroadcastSetupCoverBottomSheet.TAG
        )
        if (existingFragment is PlayBroadcastSetupCoverBottomSheet && existingFragment.isVisible) return
        getSetupCoverBottomSheet()?.show(childFragmentManager)
    }

    private fun getSetupCoverBottomSheet() = PlayBroadcastSetupCoverBottomSheet
        .getFragment(childFragmentManager, requireActivity().classLoader)

    /**
     * Listener
     */
    override fun onTagClicked(view: TagListViewComponent, tag: PlayTagItem) {
        analytic.clickContentTag(tag.tag, !tag.isChosen)
        viewModel.submitAction(PlayBroadcastSummaryAction.ToggleTag(tag))
    }

    override fun onTagRefresh(view: TagListViewComponent) {
        viewModel.submitAction(PlayBroadcastSummaryAction.RefreshLoadTag)
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
        const val PAGE_NAME = "report page"
    }

    override fun dismissSetupCover(source: Int) {
        if (getSetupCoverBottomSheet()?.isAdded == true) getSetupCoverBottomSheet()?.dismiss()

        if (parentViewModel.uploadedCoverSource == TAB_AUTO_GENERATED && source != TAB_AUTO_GENERATED) {
            toaster.showToaster(getString(R.string.play_setup_cover_upload_image_toaster))
        } else if (parentViewModel.uploadedCoverSource == TAB_UPLOAD_IMAGE && source != TAB_UPLOAD_IMAGE) {
            toaster.showToaster(getString(R.string.play_setup_cover_auto_generated_toaster))
        }
        parentViewModel.submitAction(PlayBroadcastAction.SetCoverUploadedSource(source))
    }

}
