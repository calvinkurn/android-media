package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.navigation.people.UserProfileActivityResult.EXTRA_IS_FOLLOW
import com.tokopedia.content.common.util.Router
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.people.ErrorMessage
import com.tokopedia.people.Loading
import com.tokopedia.people.R
import com.tokopedia.people.Success
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.utils.isInternetAvailable
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.adapter.ProfileFollowingAdapter
import com.tokopedia.people.views.adapter.listener.UserFollowListener
import com.tokopedia.people.views.uimodel.FollowResultUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.people.R as peopleR

class FollowingListingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val userSession: UserSessionInterface,
    private val userProfileTracker: UserProfileTracker,
    private val router: Router
) : TkpdBaseV4Fragment(), AdapterCallback, UserFollowListener {

    private var userProfileClickedPosition = -1
    private var followersContainer: ViewFlipper? = null
    private var globalError: LocalLoad? = null

    private var isLoggedIn: Boolean = false
    private var isSwipeRefresh: Boolean? = null

    private val viewModel: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FollowerFollowingViewModel::class.java]
    }

    private val mAdapter: ProfileFollowingAdapter by lazy {
        ProfileFollowingAdapter(
            viewModel = viewModel,
            callback = this,
            listener = this
        )
    }

    private val onItemShopClickedResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult
        isLoggedIn = userSession.isLoggedIn
        refreshMainUi()
    }

    private val onItemUserClickedResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult
        val isFollow = result.data?.getBooleanExtra(EXTRA_IS_FOLLOW, false) ?: false
        mAdapter.updateFollowUnfollow(userProfileClickedPosition, isFollow)
        userProfileClickedPosition = -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isLoggedIn = userSession.isLoggedIn
        return inflater.inflate(R.layout.up_fragment_psger_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersContainer = view.findViewById(R.id.container_follower_list)
        globalError = view.findViewById(R.id.ge_followers)
        initObserver()
        initMainUi()
    }

    private fun initObserver() {
        addListObserver()
        addFollowersErrorObserver()
        observeFollowResult()
    }

    private fun initMainUi() {
        val rvFollowers = view?.findViewById<RecyclerView>(R.id.rv_followers)
        rvFollowers?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading("")

        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            mAdapter.lastCursor = ""
            mAdapter.startDataLoading("")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addListObserver() =
        viewModel.profileFollowingsListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Loading -> {
                        mAdapter.resetAdapter()
                        mAdapter.notifyDataSetChanged()
                    }

                    is Success -> {
                        if (isSwipeRefresh == true) {
                            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing =
                                false
                            isSwipeRefresh = !isSwipeRefresh!!
                            mAdapter.resetAdapter()
                        }

                        mAdapter.onSuccess(it.data.followingList, it.data.nextCursor)
                    }

                    is ErrorMessage -> {
                        mAdapter.onError()
                    }
                }
            }
        }

    private fun addFollowersErrorObserver() =
        viewModel.followersErrorLiveData.observe(viewLifecycleOwner) {
            if (isSwipeRefresh == true) {
                view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing =
                    false
                isSwipeRefresh = !isSwipeRefresh!!
            } else {
                // Hide shimmer
            }

            mAdapter.onError()

            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        followersContainer?.displayedChild = PAGE_ERROR

                        globalError?.refreshBtn?.setOnClickListener {
                            followersContainer?.displayedChild = PAGE_LOADING
                            refreshMainUi()
                        }
                    }

                    is IllegalStateException -> {
                        followersContainer?.displayedChild = PAGE_ERROR

                        globalError?.refreshBtn?.setOnClickListener {
                            followersContainer?.displayedChild = PAGE_LOADING
                            refreshMainUi()
                        }
                    }

                    is RuntimeException -> {
                        when (it.localizedMessage?.toIntOrNull()) {
                            ReponseStatus.NOT_FOUND -> {
                                followersContainer?.displayedChild = PAGE_ERROR

                                globalError?.refreshBtn?.setOnClickListener {
                                    followersContainer?.displayedChild = PAGE_LOADING
                                    refreshMainUi()
                                }
                            }

                            ReponseStatus.INTERNAL_SERVER_ERROR -> {
                                followersContainer?.displayedChild = PAGE_ERROR

                                globalError?.refreshBtn?.setOnClickListener {
                                    followersContainer?.displayedChild = PAGE_LOADING
                                    refreshMainUi()
                                }
                            }

                            else -> {
                                followersContainer?.displayedChild = PAGE_ERROR

                                globalError?.refreshBtn?.setOnClickListener {
                                    followersContainer?.displayedChild = PAGE_LOADING
                                    refreshMainUi()
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun observeFollowResult() {
        viewModel.followResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is FollowResultUiModel.Fail -> {
                    val errMessage = result.message.ifBlank {
                        if (result.isFollowed) {
                            getString(peopleR.string.up_error_unfollow)
                        } else {
                            getString(peopleR.string.up_error_follow)
                        }
                    }
                    requireView().showErrorToast(errMessage)
                    updateItemPerPosition(result.itemPosition)
                }

                is FollowResultUiModel.Success -> {
                    if (result.message.isNotBlank()) requireView().showToast(result.message)
                }
            }
        }
    }

    override fun getScreenName() = TAG

    override fun onResume() {
        super.onResume()

        if (isLoggedIn != userSession.isLoggedIn) {
            refreshMainUi()
            isLoggedIn = userSession.isLoggedIn
        }
    }

    private fun refreshMainUi() {
        mAdapter.resetAdapter()
        mAdapter.lastCursor = ""
        mAdapter.startDataLoading("")
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        followersContainer?.displayedChild = PAGE_EMPTY
        val textTitle = view?.findViewById<TextView>(R.id.text_error_empty_title)
        val textDescription = view?.findViewById<TextView>(R.id.text_error_empty_desc)

        if (viewModel.userId == userSession.userId) {
            textTitle?.text = getString(peopleR.string.up_empty_page_my_following_title)
        } else {
            textTitle?.text = getString(peopleR.string.up_empty_page_following_title)
        }
        textDescription?.showWithCondition(viewModel.userId == userSession.userId)
        textDescription?.text = getString(peopleR.string.up_empty_page_my_following_desc)
    }

    override fun onStartFirstPageLoad() {
        if (isSwipeRefresh != true) {
            followersContainer?.displayedChild = PAGE_LOADING
        }
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        followersContainer?.displayedChild = PAGE_CONTENT
    }

    override fun onStartPageLoad(pageNumber: Int) {
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }

    override fun onItemUserClicked(model: PeopleUiModel.UserUiModel, position: Int) {
        userProfileClickedPosition = position
        userProfileTracker.clickUserFollowing(model.id, model.isMySelf)
        val intent = router.getIntent(
            requireContext(),
            model.appLink
        )
        router.route(onItemUserClickedResult, intent)
    }

    override fun onItemShopClicked(model: PeopleUiModel.ShopUiModel, position: Int) {
        userProfileTracker.clickUserFollowing(model.id, false)
        router.route(requireContext(), model.appLink)
    }

    override fun onFollowUserClicked(model: PeopleUiModel.UserUiModel, position: Int) {
        doFollowAction(model.isFollowed) {
            trackFollowPeople(model.id, model.isMySelf, model.isFollowed)
            viewModel.followUser(model.encryptedId, model.isFollowed, position)
            mAdapter.items[position] = model.copy(
                isFollowed = !model.isFollowed
            )
            mAdapter.notifyItemChanged(position)
        }
    }

    override fun onFollowShopClicked(model: PeopleUiModel.ShopUiModel, position: Int) {
        doFollowAction(model.isFollowed) {
            trackFollowPeople(model.id, false, model.isFollowed)
            viewModel.followShop(model.id, model.isFollowed, position)
            mAdapter.items[position] = model.copy(
                isFollowed = !model.isFollowed
            )
            mAdapter.notifyItemChanged(position)
        }
    }

    private fun doFollowAction(isFollowed: Boolean, callNetworkRequest: () -> Unit) {
        if (!isInternetAvailable(isFollowed)) return

        if (!userSession.isLoggedIn) {
            val intent = router.getIntent(requireContext(), ApplinkConst.LOGIN)
            router.route(onItemShopClickedResult, intent)
        } else {
            callNetworkRequest.invoke()
        }
    }

    private fun isInternetAvailable(isFollowed: Boolean): Boolean {
        return if (requireContext().isInternetAvailable()) {
            true
        } else {
            val errorMessage = if (isFollowed) {
                getString(peopleR.string.up_error_unfollow)
            } else {
                getString(peopleR.string.up_error_follow)
            }
            requireView().showErrorToast(errorMessage)
            false
        }
    }

    private fun trackFollowPeople(userId: String, isMySelf: Boolean, isFollowed: Boolean) {
        if (isFollowed) {
            userProfileTracker.clickUnfollowFromFollowing(userId, isMySelf)
        } else {
            userProfileTracker.clickFollowFromFollowing(userId, isMySelf)
        }
    }

    private fun updateItemPerPosition(position: Int) {
        if (mAdapter.itemCount <= position) return

        when (val item = mAdapter.getItem(position)) {
            is PeopleUiModel.ShopUiModel ->
                mAdapter.items[position] = item.copy(
                    isFollowed = !item.isFollowed
                )

            is PeopleUiModel.UserUiModel ->
                mAdapter.items[position] = item.copy(
                    isFollowed = !item.isFollowed
                )
        }
        mAdapter.notifyItemChanged(position)
    }

    companion object {
        const val PAGE_CONTENT = 0
        const val PAGE_LOADING = 1
        const val PAGE_ERROR = 2
        const val PAGE_EMPTY = 3

        private const val TAG = "FollowerListingFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): FollowingListingFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FollowingListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowingListingFragment::class.java.name
            ) as FollowingListingFragment
        }
    }
}
