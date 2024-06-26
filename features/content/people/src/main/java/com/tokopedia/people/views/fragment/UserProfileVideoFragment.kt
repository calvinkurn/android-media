package com.tokopedia.people.views.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK
import com.tokopedia.content.common.util.WindowWidthSizeClass
import com.tokopedia.content.common.util.calculateWindowSizeClass
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.people.R
import com.tokopedia.people.analytic.UserVideoPostImpressCoordinator
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentVideoBinding
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.people.views.adapter.PlayVideoAdapter
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_CONTENT
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_EMPTY
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_ERROR
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_LOADING
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_LOGIN_TO_SET_REMINDER
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_PLAY_ROOM
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.viewholder.PlayVideoViewHolder
import com.tokopedia.play.widget.ui.bottomsheet.PlayWidgetActionMenuBottomSheet
import com.tokopedia.play.widget.ui.dialog.PlayWidgetDeleteDialogContainer
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class UserProfileVideoFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userSession: UserSessionInterface,
    private val userProfileTracker: UserProfileTracker,
    private val impressCoordinator: UserVideoPostImpressCoordinator,
) : TkpdBaseV4Fragment() {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(
            activity,
            spanCount,
        )
    }

    private val spanCount by lazyThreadSafetyNone {
        when(activity?.calculateWindowSizeClass()?.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 2
            WindowWidthSizeClass.Medium -> 3
            WindowWidthSizeClass.Expanded -> 4
            else -> DEFAULT_SPAN_COUNT
        }
    }

    private var _binding: UpFragmentVideoBinding? = null
    private val binding: UpFragmentVideoBinding
        get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(EXTRA_USERNAME) ?: "",
        )
    }

    private val mAdapter: PlayVideoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PlayVideoAdapter(
            channelWidgetListener = object : PlayVideoViewHolder.Channel.Listener {

                override fun onPlayReminderClick(channel: PlayWidgetChannelUiModel) {
                    submitAction(UserProfileAction.SaveReminderActivityResult(channel))

                    if (userSession.isLoggedIn.not()) {
                        startActivityForResult(
                            RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN_TO_SET_REMINDER,
                        )
                    } else {
                        submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
                    }
                }

                override fun onPlayWidgetLargeClick(
                    appLink: String,
                    channelID: String,
                    isLive: Boolean,
                ) {
                    userProfileTracker.clickVideo(
                        viewModel.profileUserID,
                        viewModel.isSelfProfile,
                        isLive,
                        channelID,
                    )
                    val intent = RouteManager.getIntent(context, appLink)
                    startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
                }

                override fun onImpressPlayWidgetData(
                    channel: PlayWidgetChannelUiModel,
                    isLive: Boolean,
                    channelId: String,
                    pos: Int
                ) {
                    impressCoordinator.initiateDataImpress(channel) {
                        userProfileTracker.impressionVideo(
                            viewModel.profileUserID,
                            viewModel.isSelfProfile,
                            isLive,
                            channelId,
                            channel.video.coverUrl,
                            pos,
                        )
                    }
                }

                override fun onMenuActionButtonClicked(channel: PlayWidgetChannelUiModel) {
                    submitAction(UserProfileAction.ClickPlayVideoMenuAction(channel))
                }
            },
            transcodeWidgetListener = object : PlayVideoViewHolder.Transcode.Listener {
                override fun onDeletePlayChannel(channel: PlayWidgetChannelUiModel) {
                    submitAction(UserProfileAction.DeletePlayChannel(channel.channelId))
                }
            },
            onLoading = {
                submitAction(UserProfileAction.LoadPlayVideo())
            }
        )
    }

    private val playChannelDeleteConfirmationDialog by lazy {
        PlayWidgetDeleteDialogContainer(object : PlayWidgetDeleteDialogContainer.Listener {
            override fun onDeleteButtonClicked(channelId: String) {
                submitAction(UserProfileAction.DeletePlayChannel(channelId))
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = UpFragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        setupPlayVideo()

        if (activity?.lastNonConfigurationInstance == null)
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
    }

    override fun onPause() {
        super.onPause()
        impressCoordinator.sendTracker { userProfileTracker.sendAll() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        observeUiEvent()
        observeUiState()
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileUiEvent.SuccessUpdateReminder -> {
                        view?.showToast(event.message)
                    }
                    is UserProfileUiEvent.ErrorUpdateReminder -> {
                        val message = when (event.throwable) {
                            is UnknownHostException, is SocketTimeoutException -> {
                                requireContext().getString(R.string.up_error_local_error)
                            }
                            else -> event.throwable.message ?: getDefaultErrorMessage()
                        }

                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.ErrorVideoPosts -> {
                        with(binding) {
                            userVideoContainer.displayedChild = PAGE_ERROR

                            globalErrorVideo.refreshBtn?.setOnClickListener {
                                userVideoContainer.displayedChild = PAGE_LOADING
                                submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
                            }
                        }
                    }
                    is UserProfileUiEvent.SuccessDeleteChannel -> {
                        view?.showToast(getString(R.string.up_play_video_deleted))
                    }
                    is UserProfileUiEvent.ErrorDeleteChannel -> {
                        val message = when (event.throwable) {
                            is UnknownHostException, is SocketTimeoutException -> {
                                requireContext().getString(R.string.up_error_local_error)
                            }
                            else -> event.throwable.message ?: getDefaultErrorMessage()
                        }

                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.OpenPlayVideoActionMenu -> {
                        val bottomSheet = PlayWidgetActionMenuBottomSheet.getFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        )

                        bottomSheet.setChannel(event.channel)
                        bottomSheet.setListener(object : PlayWidgetActionMenuBottomSheet.Listener {
                            override fun onClickShare(channel: PlayWidgetChannelUiModel) {
                                submitAction(UserProfileAction.ClickCopyLinkPlayChannel(channel))
                            }

                            override fun onClickSeePerformance(channel: PlayWidgetChannelUiModel) {
                                submitAction(UserProfileAction.ClickSeePerformancePlayChannel(channel))
                            }

                            override fun onClickSeePerformanceVideoAnalytics(channel: PlayWidgetChannelUiModel) {
                                RouteManager.route(requireContext(), PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK)
                            }

                            override fun onClickDeleteVideo(channel: PlayWidgetChannelUiModel) {
                                submitAction(UserProfileAction.ClickDeletePlayChannel(channel))
                            }
                        })

                        bottomSheet.show(childFragmentManager)
                    }
                    is UserProfileUiEvent.CopyLinkPlayVideo -> {
                        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                            .setPrimaryClip(ClipData.newPlainText("play-widget", event.copyText))

                        view?.showToast(getString(R.string.up_play_video_link_copied))
                    }
                    is UserProfileUiEvent.OpenPerformancePlayChannel -> {
                        RouteManager.route(requireContext(), event.appLink)
                    }
                    is UserProfileUiEvent.ShowDeletePlayVideoConfirmationDialog -> {
                        playChannelDeleteConfirmationDialog.confirmDelete(requireContext(), event.channel.channelId)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderPlayVideo(it.prevValue?.videoPostsContent, it.value.videoPostsContent)
            }
        }
    }

    /** Render UI */
    private fun renderPlayVideo(
        prev: UserPlayVideoUiModel?,
        curr: UserPlayVideoUiModel,
    ) {
        if(prev == curr) return

        when(curr.status) {
            UserPlayVideoUiModel.Status.Loading -> {
                if (curr.items.isEmpty()) {
                    binding.userVideoContainer.displayedChild = PAGE_CONTENT
                    mAdapter.setItemsAndAnimateChanges(
                        List(4) {
                            PlayVideoAdapter.Model.Shimmer
                        }
                    )
                }
            }
            UserPlayVideoUiModel.Status.Success -> {
                if(curr.items.isEmpty()) {
                    if (viewModel.isSelfProfile) emptyPostSelf()
                    else emptyPostVisitor()

                    binding.userVideoContainer.displayedChild = PAGE_EMPTY

                    return
                }

                val mappedList = curr.items.map {
                    if(it.channelType == PlayWidgetChannelType.Transcoding || it.channelType == PlayWidgetChannelType.FailedTranscoding) {
                        PlayVideoAdapter.Model.Transcode(it)
                    }
                    else {
                        PlayVideoAdapter.Model.Channel(it)
                    }
                }

                val finalList = if(curr.nextCursor.isEmpty()) mappedList else mappedList + listOf(PlayVideoAdapter.Model.Loading)
                mAdapter.setItemsAndAnimateChanges(finalList)
                binding.userVideoContainer.displayedChild = PAGE_CONTENT
            }
            else -> {}
        }
    }

    private fun setupPlayVideo() {
        binding.rvPost.layoutManager = gridLayoutManager
        if (binding.rvPost.itemDecorationCount == 0) {
            val spacing =
                requireContext().resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl1)
            binding.rvPost.addItemDecoration(GridSpacingItemDecoration(gridLayoutManager.spanCount, spacing, false))
        }
        binding.rvPost.adapter = mAdapter

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(mAdapter.getItem(position)) {
                    is PlayVideoAdapter.Model.Loading -> spanCount
                    else -> 1
                }
            }
        }
    }

    private fun getDefaultErrorMessage() = getString(R.string.up_error_unknown)

    override fun getScreenName(): String = TAG

    private fun submitAction(action: UserProfileAction) {
        viewModel.submitAction(action)
    }

    private fun emptyPostSelf() {
        binding.emptyVideo.textErrorEmptyTitle.text = getString(R.string.up_empty_post_title_on_self)
        binding.emptyVideo.textErrorEmptyDesc.apply {
            text = getString(R.string.up_empty_post_desc_on_self)
            show()
        }
    }

    private fun emptyPostVisitor() {
        binding.emptyVideo.textErrorEmptyTitle.text = getString(R.string.up_empty_post_on_visitor)
        binding.emptyVideo.textErrorEmptyDesc.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_PLAY_ROOM -> {
                val channelId = data?.extras?.getString(EXTRA_CHANNEL_ID) ?: return
                val totalView = data.extras?.getString(EXTRA_TOTAL_VIEW).orEmpty()
                val isReminderSet = data.extras?.getBoolean(EXTRA_IS_REMINDER, false) ?: false

                submitAction(UserProfileAction.UpdatePlayChannelInfo(channelId, totalView, isReminderSet))
            }
            REQUEST_CODE_LOGIN_TO_SET_REMINDER -> {
                viewModel.submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = true))
            }
        }
    }

    companion object {
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val TAG = "UserProfileVideoFragment"
        private const val DEFAULT_SPAN_COUNT = 2

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): UserProfileVideoFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileVideoFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileVideoFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as UserProfileVideoFragment
        }
    }
}
