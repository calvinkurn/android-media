package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.people.ErrorMessage
import com.tokopedia.people.Loading
import com.tokopedia.people.R
import com.tokopedia.people.Success
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UserProfileFeedFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userProfileTracker: UserProfileTracker,
    private val impressCoordinator: UserFeedPostImpressCoordinator,
) : TkpdBaseV4Fragment(), AdapterCallback, UserFeedPostsBaseAdapter.FeedPostsCallback {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, GRID_SPAN_COUNT)
    }

    private val mAdapter: UserFeedPostsBaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserFeedPostsBaseAdapter(this, this) { cursor ->
            viewModel.submitAction(UserProfileAction.LoadFeedPosts(cursor))
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

    private fun fetchFeedsPosts() {
        binding.userFeedContainer.displayedChild = UserProfileFragment.PAGE_LOADING
        viewModel.submitAction(UserProfileAction.LoadFeedPosts(""))
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

    override fun onFeedPostsClick(appLink: String, itemID: String, imageUrl: String, position: Int, mediaType: String) {
        userProfileTracker.clickPost(
            viewModel.profileUserID,
            viewModel.isSelfProfile,
            itemID,
            imageUrl,
            position,
            mediaType,
        )
        val intent = RouteManager.getIntent(requireContext(), appLink)
        intent.putExtra(KEY_SOURCE, VAL_SOURCE)
        intent.putExtra(KEY_POSITION, position)
        startActivity(intent)
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

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        if (viewModel.isSelfProfile) emptyPostSelf()
        else emptyPostVisitor()
        binding.userFeedContainer.displayedChild = PAGE_EMPTY
    }

    override fun onStartFirstPageLoad() {
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        binding.userFeedContainer.displayedChild = PAGE_CONTENT
    }

    override fun onStartPageLoad(pageNumber: Int) {
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }

    companion object {
        private const val TAG = "UserProfileFeedFragment"
        private const val KEY_SOURCE = "source"
        private const val KEY_POSITION = "position"
        private const val VAL_SOURCE = "user_profile"
        private const val GRID_SPAN_COUNT = 3
        private const val LOADING_SPAN = 3
        private const val DATA_SPAN = 1

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
