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
import com.tokopedia.applink.ApplinkConst.INTERNAL_CONTENT_DETAIL
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.people.R
import com.tokopedia.people.analytic.UserFeedPostImpressCoordinator
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentFeedBinding
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity
import com.tokopedia.people.views.adapter.UserFeedPostsBaseAdapter
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_CONTENT
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_EMPTY
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_ERROR
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.content.PostUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.common.R as contentCommonR

class UserProfileFeedFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userProfileTracker: UserProfileTracker,
    private val impressCoordinator: UserFeedPostImpressCoordinator,
) : TkpdBaseV4Fragment(), UserFeedPostsBaseAdapter.FeedPostsCallback {

    private var nextCursor: String = ""

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, GRID_SPAN_COUNT)
    }

    private val mAdapter: UserFeedPostsBaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserFeedPostsBaseAdapter(this) {
            viewModel.submitAction(UserProfileAction.LoadFeedPosts(nextCursor))
        }
    }

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(UserProfileActivity.EXTRA_USERNAME) ?: "",
        )
    }

    private var _binding: UpFragmentFeedBinding? = null
    private val binding: UpFragmentFeedBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = UpFragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        setupFeedsPosts()

        fetchFeedsPosts()
    }

    override fun onPause() {
        super.onPause()
        impressCoordinator.sendTracker { userProfileTracker.sendAll() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getScreenName(): String = TAG

    private fun setupFeedsPosts() {
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()

        binding.rvFeed.layoutManager = gridLayoutManager
        if (binding.rvFeed.itemDecorationCount == 0) {
            val spacing = requireContext().resources.getDimensionPixelOffset(contentCommonR.dimen.content_common_space_1)
            binding.rvFeed.addItemDecoration(GridSpacingItemDecoration(GRID_SPAN_COUNT, spacing, false))
        }
        binding.rvFeed.adapter = mAdapter
    }

    private fun getSpanSizeLookUp(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter.getItem(position)) {
                    is UserFeedPostsBaseAdapter.Model.Loading -> LOADING_SPAN
                    else -> DATA_SPAN
                }
            }
        }
    }

    private fun fetchFeedsPosts() {
        binding.userFeedContainer.displayedChild = UserProfileFragment.PAGE_LOADING
        viewModel.submitAction(UserProfileAction.LoadFeedPosts())
    }

    private fun initObserver() {
        observeUiEvent()
        observeUiState()
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                if (event is UserProfileUiEvent.ErrorFeedPosts) {
                    with(binding) {
                        userFeedContainer.displayedChild = PAGE_ERROR

                        globalErrorFeed.refreshBtn?.setOnClickListener {
                            fetchFeedsPosts()
                        }
                    }
                } else return@collect
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                val prev = it.prevValue?.feedPostsContent
                val value = it.value.feedPostsContent
                if ((prev == null && value == UserFeedPostsUiModel()) || (prev == value)) return@collectLatest
                renderFeedPosts(value)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun renderFeedPosts(data: UserFeedPostsUiModel) {
        if (data.posts.isEmpty()) emptyPost()
        else {
            nextCursor = data.pagination.cursor
            val model = buildList {
                addAll(data.posts.map { postUiModel ->
                        UserFeedPostsBaseAdapter.Model.FeedPosts(postUiModel)
                    },
                )
                if (data.pagination.hasNext) add(UserFeedPostsBaseAdapter.Model.Loading)
            }

            if (binding.rvFeed.isComputingLayout.not()) mAdapter.setItemsAndAnimateChanges(model)
            binding.userFeedContainer.displayedChild = PAGE_CONTENT
        }
    }

    override fun onFeedPostsClick(
        appLink: String,
        itemID: String,
        imageUrl: String,
        position: Int,
        mediaType: String,
    ) {
        userProfileTracker.clickPost(
            viewModel.profileUserID,
            viewModel.isSelfProfile,
            itemID,
            mediaType,
        )

        /** temporary solution by hardcoding the url to cdp **/
//        val intent = RouteManager.getIntent(requireContext(), appLink)
        val intent = RouteManager.getIntent(
            requireContext(), UriUtil.buildUri(
                INTERNAL_CONTENT_DETAIL,
                viewModel.profileUserID
            )
        )
        intent.putExtra(KEY_ENTRY_POINT, VAL_ENTRY_POINT)
        intent.putExtra(KEY_POSITION, position)
        intent.putExtra(KEY_VISITED_USER_ID, viewModel.profileUserID)
        intent.putExtra(KEY_VISITED_USER_ENCRYPTED_ID, viewModel.profileUserEncryptedID)
        startActivityForResult(intent, REQUEST_CODE_TO_CDP)
    }

    override fun onImpressFeedPostData(item: PostUiModel, position: Int) {
        impressCoordinator.initiateDataImpress(item) {
            userProfileTracker.impressionPost(
                viewModel.profileUserID,
                viewModel.isSelfProfile,
                it.id,
                it.media.first().coverURL,
                position,
                it.media.first().type,
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_TO_CDP -> {
                val needRefresh = data?.extras?.getBoolean(ACTION_TO_REFRESH, false) ?: false
                if (!needRefresh) return
                (parentFragment as UserProfileFragment).refreshLandingPageData(true)
                viewModel.submitAction(UserProfileAction.LoadFeedPosts(isRefresh = true))
            }
        }
    }

    private fun emptyPost() {
        if (viewModel.isSelfProfile) emptyPostSelf()
        else emptyPostVisitor()
        binding.userFeedContainer.displayedChild = PAGE_EMPTY
    }

    private fun emptyPostSelf() {
        binding.emptyFeed.textErrorEmptyTitle.text = getString(R.string.up_empty_post_title_on_self)
        binding.emptyFeed.textErrorEmptyDesc.apply {
            text = getString(R.string.up_empty_post_desc_on_self)
            show()
        }
    }

    private fun emptyPostVisitor() {
        binding.emptyFeed.textErrorEmptyTitle.text = getString(R.string.up_empty_post_on_visitor)
        binding.emptyFeed.textErrorEmptyDesc.hide()
    }

    companion object {
        private const val TAG = "UserProfileFeedFragment"
        private const val KEY_ENTRY_POINT = "entry_point"
        private const val KEY_POSITION = "position"
        private const val KEY_VISITED_USER_ID = "visited_user_id"
        private const val KEY_VISITED_USER_ENCRYPTED_ID = "visited_user_encrypted_id"
        private const val VAL_ENTRY_POINT = "user_profile"
        private const val GRID_SPAN_COUNT = 3
        private const val LOADING_SPAN = 3
        private const val DATA_SPAN = 1
        private const val REQUEST_CODE_TO_CDP = 1234
        private const val ACTION_TO_REFRESH = "action_to_refresh"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): UserProfileFeedFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileFeedFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileFeedFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as UserProfileFeedFragment
        }
    }
}
