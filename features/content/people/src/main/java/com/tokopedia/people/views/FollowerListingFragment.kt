package com.tokopedia.people.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.people.*
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSession
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class FollowerListingFragment : BaseDaggerFragment(), AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var followersContainer: ViewFlipper? = null
    private var globalError: LocalLoad? = null
    private var isLoggedIn: Boolean = false
    private var isSwipeRefresh: Boolean? = null

    val userSessionInterface: UserSession by lazy {
        UserSession(context)
    }

    private val mPresenter: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }


    private val mAdapter: ProfileFollowersAdapter by lazy {
        ProfileFollowersAdapter(
            mPresenter,
            this,
            userSessionInterface,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        isLoggedIn = userSessionInterface?.isLoggedIn
        return inflater.inflate(R.layout.up_fragment_psger_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersContainer = view.findViewById(R.id.container)
        globalError = view.findViewById(R.id.ge_followers)
        initObserver()
        initMainUi()
    }

    private fun initObserver() {
        addListObserver()
        addFollowersErrorObserver()
    }

    private fun initMainUi() {
        val rvFollowers = view?.findViewById<RecyclerView>(R.id.rv_followers)
        rvFollowers?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_ID))

        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            mAdapter.cursor = ""
            mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_ID))
        }
    }

    private fun refreshMainUi() {
        mAdapter.resetAdapter()
        mAdapter.cursor = ""
        mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_ID))
    }

    private fun addListObserver() =
        mPresenter.profileFollowersListLiveData.observe(viewLifecycleOwner, Observer {
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
                            mAdapter.clear()
                            mAdapter.items.addAll(it.data.profileFollowers.profileFollower)
                            mAdapter.notifyDataSetChanged()
                        } else {
                            mAdapter.onSuccess(it.data)
                        }

                    }
                    is ErrorMessage -> {
                        mAdapter.onError()
                    }
                }
            }
        })

    private fun addFollowersErrorObserver() =
        mPresenter.followersErrorLiveData.observe(viewLifecycleOwner, Observer {
            if (isSwipeRefresh == true) {
                view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing =
                    false
                isSwipeRefresh = !isSwipeRefresh!!
            } else {
                //Hide shimmer
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
                                followersContainer?.displayedChild =PAGE_ERROR
                                globalError?.refreshBtn?.setOnClickListener {
                                    followersContainer?.displayedChild = PAGE_LOADING
                                    refreshMainUi()
                                }
                            }
                        }
                    }
                }
            }
        })

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()

        if (isLoggedIn != userSessionInterface.isLoggedIn) {
            refreshMainUi()
            isLoggedIn = userSessionInterface.isLoggedIn
        }
    }

    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserProfileFragment.REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            isLoggedIn = userSessionInterface.isLoggedIn
            refreshMainUi()
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        followersContainer?.displayedChild = PAGE_EMPTY

        val textTitle = view?.findViewById<TextView>(R.id.text_error_empty_title)
        val textDescription = view?.findViewById<TextView>(R.id.text_error_empty_desc)

        textTitle?.text = getString(com.tokopedia.people.R.string.up_lb_no_follower)
        textDescription?.hide()
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

        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowerListingFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}

