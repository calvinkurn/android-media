package com.tokopedia.people.views.fragment

import android.app.Activity
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.people.R
import com.tokopedia.people.analytic.UserVideoPostImpressCoordinator
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentVideoBinding
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.people.views.adapter.UserPostBaseAdapter
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.LOADING
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_CONTENT
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_EMPTY
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_ERROR
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_LOADING
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_LOGIN_TO_SET_REMINDER
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_PLAY_ROOM
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyR

class UserProfileVideoFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userSession: UserSessionInterface,
    private val userProfileTracker: UserProfileTracker,
    private val impressCoordinator: UserVideoPostImpressCoordinator,
) : TkpdBaseV4Fragment(), AdapterCallback, UserPostBaseAdapter.PlayWidgetCallback {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, GRID_SPAN_COUNT)
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

    private val mAdapter: UserPostBaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserPostBaseAdapter(this, this) { cursor ->
            submitAction(UserProfileAction.LoadPlayVideo(cursor))
        }
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

        fetchPlayVideo(viewModel.profileUserID)
    }

    override fun onPause() {
        super.onPause()
        impressCoordinator.sendTracker { userProfileTracker.sendAll() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchPlayVideo(userId: String) {
        mAdapter.resetAdapter()
        mAdapter.cursor = ""
        mAdapter.startDataLoading(userId)
    }

    private fun getSpanSizeLookUp(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter.getItemViewType(position)) {
                    LOADING -> LOADING_SPAN
                    else -> DATA_SPAN
                }
            }
        }
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
                        mAdapter.notifyItemChanged(event.position)
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
                                fetchPlayVideo(viewModel.profileUserID)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                val prev = it.prevValue?.videoPostsContent
                val value = it.value.videoPostsContent
                if ((prev == null && value == UserPostModel()) || (prev == value)) return@collectLatest
                mAdapter.onSuccess(value)
            }
        }
    }

    /** Render UI */
    private fun setupPlayVideo() {
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()

        binding.rvPost.layoutManager = gridLayoutManager
        if (binding.rvPost.itemDecorationCount == 0) {
            val spacing =
                requireContext().resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl1)
            binding.rvPost.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))
        }
        binding.rvPost.adapter = mAdapter
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

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        if (viewModel.isSelfProfile) emptyPostSelf()
        else emptyPostVisitor()
        binding.userVideoContainer.displayedChild = PAGE_EMPTY
    }

    override fun onStartFirstPageLoad() {
        binding.userVideoContainer.displayedChild = PAGE_LOADING
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        binding.userVideoContainer.displayedChild = PAGE_CONTENT
    }

    override fun onStartPageLoad(pageNumber: Int) {
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }

    override fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int) {
        submitAction(UserProfileAction.SaveReminderActivityResult(channelId, pos, isActive))

        if (userSession.isLoggedIn.not()) {
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN_TO_SET_REMINDER,
            )
        } else {
            submitAction(UserProfileAction.ClickUpdateReminder(false))
        }
    }

    override fun updatePlayWidgetLatestData(
        channelId: String,
        totalView: String,
        isReminderSet: Boolean,
    ) {
        mAdapter.updatePlayWidgetLatestData(channelId, totalView, isReminderSet)
    }

    override fun onPlayWidgetLargeClick(appLink: String, channelID: String, isLive: Boolean, imageUrl: String, pos: Int) {
        userProfileTracker.clickVideo(
            viewModel.profileUserID,
            viewModel.isSelfProfile,
            isLive,
            channelID,
            imageUrl,
            pos,
        )
        val intent = RouteManager.getIntent(context, appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    override fun onImpressPlayWidgetData(item: PlayPostContentItem, isLive: Boolean, channelId: String, pos: Int) {
        impressCoordinator.initiateDataImpress(item) {
            userProfileTracker.impressionVideo(
                viewModel.profileUserID,
                viewModel.isSelfProfile,
                isLive,
                channelId,
                it.coverUrl,
                pos,
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_PLAY_ROOM -> {
                val channelId = data?.extras?.getString(EXTRA_CHANNEL_ID) ?: return
                val totalView = data.extras?.getString(EXTRA_TOTAL_VIEW).orEmpty()
                val isReminderSet = data.extras?.getBoolean(EXTRA_IS_REMINDER, false) ?: false

                mAdapter.updatePlayWidgetLatestData(channelId, totalView, isReminderSet)
            }
        }
    }

    companion object {
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val TAG = "UserProfileVideoFragment"
        private const val GRID_SPAN_COUNT = 2
        private const val LOADING_SPAN = 2
        private const val DATA_SPAN = 1

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
