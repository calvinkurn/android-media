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
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.people.ErrorMessage
import com.tokopedia.people.Loading
import com.tokopedia.people.R
import com.tokopedia.people.Success
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.listener.FollowerFollowingListener
import com.tokopedia.people.listener.FollowingFollowerListener
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.adapter.ProfileFollowingAdapter
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FollowingListingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val userSession: UserSessionInterface,
    private val userProfileTracker: UserProfileTracker,
) : TkpdBaseV4Fragment(),
    AdapterCallback,
    FollowerFollowingListener,
    FollowingFollowerListener {

    private var followersContainer: ViewFlipper? = null
    private var globalError: LocalLoad? = null

    private var isLoggedIn: Boolean = false
    private var isSwipeRefresh: Boolean? = null

    private val viewModel: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }

    private val mAdapter: ProfileFollowingAdapter by lazy {
        ProfileFollowingAdapter(
            viewModel,
            this,
            userSession,
            this,
            this,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        isLoggedIn = userSession.isLoggedIn
        return inflater.inflate(R.layout.up_fragment_psger_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersContainer = view.findViewById(R.id.container_follower_list)
        globalError = view?.findViewById(R.id.ge_followers)
        initObserver()
        initMainUi()
    }

    private fun initObserver() {
        addListObserver()
        addFollowersErrorObserver()
    }

    private fun initMainUi() {
        viewModel.username = arguments?.getString(UserProfileFragment.EXTRA_USER_ID).orEmpty()

        val rvFollowers = view?.findViewById<RecyclerView>(R.id.rv_followers)
        rvFollowers?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading("")

        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            mAdapter.cursor = ""
            mAdapter.startDataLoading("")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addListObserver() =
        viewModel.profileFollowingsListLiveData.observe(
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

                            mAdapter.onSuccess(it.data)
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

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()

        if (isLoggedIn != userSession.isLoggedIn) {
            refreshMainUi()
            isLoggedIn = userSession.isLoggedIn
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW && resultCode == Activity.RESULT_OK) {
            isLoggedIn = userSession.isLoggedIn
            refreshMainUi()
        } else if (requestCode == UserProfileFragment.REQUEST_CODE_USER_PROFILE) {
            val position = data?.getIntExtra(UserProfileFragment.EXTRA_POSITION_OF_PROFILE, -1)
            data?.getStringExtra(UserProfileFragment.EXTRA_FOLLOW_UNFOLLOW_STATUS)?.let {
                if (position != null && position != -1) {
                    if (it == UserProfileFragment.EXTRA_VALUE_IS_FOLLOWED)
                        mAdapter.updateFollowUnfollow(position, true)
                    else
                        mAdapter.updateFollowUnfollow(position, false)
                }
            }
        }
    }

    private fun refreshMainUi() {
        mAdapter.resetAdapter()
        mAdapter.cursor = ""
        mAdapter.startDataLoading("")
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        followersContainer?.displayedChild = PAGE_EMPTY
        val textTitle = view?.findViewById<TextView>(R.id.text_error_empty_title)
        val textDescription = view?.findViewById<TextView>(R.id.text_error_empty_desc)

        val currentUserId = arguments?.getString(UserProfileFragment.EXTRA_USER_ID)
        if (currentUserId == userSession.userId)
            textTitle?.text = getString(com.tokopedia.people.R.string.up_empty_page_my_following_title)
        else
            textTitle?.text = getString(com.tokopedia.people.R.string.up_empty_page_following_title)
        textDescription?.showWithCondition(currentUserId == userSession.userId)
        textDescription?.text = getString(com.tokopedia.people.R.string.up_empty_page_my_following_desc)
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
        ): FollowingListingFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FollowingListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowingListingFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as FollowingListingFragment
        }
    }

    override fun callstartActivityFromFragment(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun callstartActivityFromFragment(applink: String, requestCode: Int) {
        startActivityForResult(RouteManager.getIntent(context, applink), requestCode)
    }

    override fun clickUser(userId: String, self: Boolean) {
        userProfileTracker.clickUserFollowing(userId, self)
    }

    override fun clickUnfollow(userId: String, self: Boolean) {
        userProfileTracker.clickUnfollowFromFollowing(userId, self)
    }

    override fun clickFollow(userId: String, self: Boolean) {
        userProfileTracker.clickFollowFromFollowing(userId, self)
    }
}
