package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.people.model.MutationUiModel
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
import com.tokopedia.people.views.adapter.ProfileFollowersAdapter
import com.tokopedia.people.views.adapter.listener.UserFollowListener
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSession
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FollowerListingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val userProfileTracker: UserProfileTracker,
) : TkpdBaseV4Fragment(), AdapterCallback {

    private var followersContainer: ViewFlipper? = null
    private var globalError: LocalLoad? = null
    private var isLoggedIn: Boolean = false
    private var isSwipeRefresh: Boolean? = null

    val userSessionInterface: UserSession by lazy {
        UserSession(context)
    }

    private val viewModel: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }

    private val mListener = object : UserFollowListener {
        override fun onItemUserClicked(model: ProfileUiModel.UserUiModel, position: Int) {
            goToUserProfilePage(model, position)
        }

        override fun onItemShopClicked(model: ProfileUiModel.ShopUiModel, position: Int) {
            goToShopPage(model, position)
        }

        override fun onFollowUserClicked(model: ProfileUiModel.UserUiModel, position: Int) {
            doFollowUserClicked(model, position)
        }

        override fun onFollowShopClicked(model: ProfileUiModel.ShopUiModel, position: Int) {
            doFollowShopClicked(model, position)
        }
    }

    private val mAdapter: ProfileFollowersAdapter by lazy {
        ProfileFollowersAdapter(
            viewModel,
            this,
            mListener,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        isLoggedIn = userSessionInterface?.isLoggedIn
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
        viewModel.username = arguments?.getString(UserProfileFragment.EXTRA_USER_ID).orEmpty()

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

    // todo: get response from followResult, show result, if its error then show error

    private fun refreshMainUi() {
        mAdapter.resetAdapter()
        mAdapter.lastCursor = ""
        mAdapter.startDataLoading("")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addListObserver() =
        viewModel.profileFollowersListLiveData.observe(
            viewLifecycleOwner,
            Observer {
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

                            mAdapter.onSuccess(it.data, it.nextCursor)
                        }
                        is ErrorMessage -> {
                            mAdapter.onError()
                        }
                    }
                }
            },
        )

    private fun addFollowersErrorObserver() =
        viewModel.followersErrorLiveData.observe(
            viewLifecycleOwner,
            Observer {
                if (isSwipeRefresh == true) {
                    view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing =
                        false
                    isSwipeRefresh = !isSwipeRefresh!!
                } else {
                    // Hide shimmer
                }

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
            },
        )

    private fun observeFollowResult() {
        viewModel.followResult.observe(viewLifecycleOwner, Observer { result ->
            when(val data = result.first) {
                is MutationUiModel.Error -> {
                    val errMessage = data.message.ifBlank {
                        if (result.second) {
                            getString(com.tokopedia.people.R.string.up_error_unfollow)
                        } else {
                            getString(com.tokopedia.people.R.string.up_error_follow)
                        }
                    }
                    requireView().showErrorToast(errMessage)
                    updateItemPerPosition(result.third)
                }
                is MutationUiModel.Success -> {
                    if (data.message.isNotBlank()) {
                        requireView().showToast(data.message)
                    }
                }
            }
        })
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onResume() {
        super.onResume()

        if (isLoggedIn != userSessionInterface.isLoggedIn) {
            refreshMainUi()
            isLoggedIn = userSessionInterface.isLoggedIn
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW && resultCode == Activity.RESULT_OK) {
            isLoggedIn = userSessionInterface.isLoggedIn
            refreshMainUi()
        } else if (requestCode == UserProfileFragment.REQUEST_CODE_USER_PROFILE) {
            val position = data?.getIntExtra(UserProfileFragment.EXTRA_POSITION_OF_PROFILE, -1)
            data?.getStringExtra(UserProfileFragment.EXTRA_FOLLOW_UNFOLLOW_STATUS)?.let {
                if (position != null && position != -1) {
                    if (position != null && position != -1) {
                        if (it == UserProfileFragment.EXTRA_VALUE_IS_FOLLOWED)
                            mAdapter.updateFollowUnfollow(position, true)
                        else
                            mAdapter.updateFollowUnfollow(position, false)
                    }
                }
            }
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        followersContainer?.displayedChild = PAGE_EMPTY

        val textTitle = view?.findViewById<TextView>(R.id.text_error_empty_title)
        val textDescription = view?.findViewById<TextView>(R.id.text_error_empty_desc)

        val currentUserId = arguments?.getString(UserProfileFragment.EXTRA_USER_ID)
        if (currentUserId == userSessionInterface.userId)
            textTitle?.text = getString(com.tokopedia.people.R.string.up_empty_page_my_follower_title)
        else
            textTitle?.text = getString(com.tokopedia.people.R.string.up_empty_page_follower_title)
        textDescription?.showWithCondition(currentUserId == userSessionInterface.userId)
        textDescription?.text = getString(com.tokopedia.people.R.string.up_empty_page_my_follower_desc)
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

    companion object {
        const val PAGE_CONTENT = 0
        const val PAGE_ERROR = 2
        const val PAGE_LOADING = 1
        const val PAGE_EMPTY = 3

        private const val TAG = "FollowerListingFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): FollowerListingFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FollowerListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowerListingFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as FollowerListingFragment
        }
    }

    private fun goToUserProfilePage(item: ProfileUiModel.UserUiModel, position: Int) {
        userProfileTracker.clickUserFollowers(item.id, item.isMySelf)
        val intent = RouteManager.getIntent(
            requireContext(),
            item.appLink,
        ).apply {
            putExtra(UserProfileFragment.EXTRA_POSITION_OF_PROFILE, position)
        }
        startActivityForResult(
            intent,
            UserProfileFragment.REQUEST_CODE_USER_PROFILE,
        )
    }

    private fun goToShopPage(item: ProfileUiModel.ShopUiModel, position: Int) {
        userProfileTracker.clickUserFollowers(item.id, false)
        RouteManager.route(requireContext(), item.appLink)
    }

    private fun doFollowUserClicked(item: ProfileUiModel.UserUiModel, position: Int) {
        if (!isInternetAvailable(item.isFollowed)) return

        if (!userSessionInterface.isLoggedIn) {
            val requestCode = if (item.isFollowed) {
                FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW
            } else {
                FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW
            }
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                requestCode
            )
        } else {
            trackFollowPeople(item.id, item.isMySelf, item.isFollowed)
            viewModel.followUser(item.encryptedId, item.isFollowed, position)
            mAdapter.items[position] = item.copy(
                isFollowed = !item.isFollowed
            )
            mAdapter.notifyItemChanged(position)
        }
    }

    private fun doFollowShopClicked(item: ProfileUiModel.ShopUiModel, position: Int) {
        if (!isInternetAvailable(item.isFollowed)) return

        if (!userSessionInterface.isLoggedIn) {
            val requestCode = if (item.isFollowed) {
                FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW
            } else {
                FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW
            }
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                requestCode
            )
        } else {
            trackFollowPeople(item.id, false, item.isFollowed)
            viewModel.followShop(item.id, item.isFollowed, position)
            mAdapter.items[position] = item.copy(
                isFollowed = !item.isFollowed
            )
            mAdapter.notifyItemChanged(position)
        }
    }

    private fun trackFollowPeople(userId: String, isMySelf: Boolean, isFollowed: Boolean) {
        if (isFollowed) {
            userProfileTracker.clickUnfollowFromFollowers(userId, isMySelf)
        } else {
            userProfileTracker.clickFollowFromFollowers(userId, isMySelf)
        }
    }

    private fun updateItemPerPosition(position: Int) {
        if (mAdapter.itemCount <= position) return

        when (val item = mAdapter.getItem(position)) {
            is ProfileUiModel.ShopUiModel ->
                mAdapter.items[position] = item.copy(
                    isFollowed = !item.isFollowed
                )
            is ProfileUiModel.UserUiModel ->
                mAdapter.items[position] = item.copy(
                    isFollowed = !item.isFollowed
                )
        }
        mAdapter.notifyItemChanged(position)
    }

    private fun isInternetAvailable(isFollowed: Boolean): Boolean {
        return if (requireContext().isInternetAvailable()) true
        else {
            val errorMessage = if (isFollowed) {
                getString(com.tokopedia.people.R.string.up_error_unfollow)
            } else {
                getString(com.tokopedia.people.R.string.up_error_follow)
            }
            requireView().showErrorToast(errorMessage)
            false
        }
    }
}
