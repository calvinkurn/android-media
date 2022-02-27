package com.tokopedia.createpost.uprofile.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.di.DaggerUserProfileComponent
import com.tokopedia.createpost.uprofile.di.UserProfileModule
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.EXTRA_DISPLAY_NAME
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWERS
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWINGS
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.EXTRA_USER_NAME
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.user.session.UserSession
import javax.inject.Inject


class FollowerFollowingListingFragment : BaseDaggerFragment(), View.OnClickListener,
    AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mPresenter: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }

    var tabLayout: TabsUnify? = null
    var ffViewPager: ViewPager? = null

//    private val mAdapter: UserPostBaseAdapter by lazy {
//        UserPostBaseAdapter(
//            mPresenter,
//            this
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        return inflater.inflate(R.layout.up_fragment_follower_following_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initObserver()
        //initListener()
        setHeader()
        val userSessionInterface = UserSession(context)
        //mPresenter.getUserDetails(userSessionInterface.userId)
        //initUserPost()
        setMainUi()
    }

    private fun initObserver() {
        //observeUserProfile()
    }

    private fun initListener() {
//        view?.findViewById<Group>(R.id.gr_following)?.setOnClickListener(this)
//        view?.findViewById<Group>(R.id.gr_follower)?.setOnClickListener(this)
    }

    private fun initUserPost() {
//        val postRv = view?.findViewById<RecyclerView>(R.id.recycler_view)
//        postRv?.layoutManager = GridLayoutManager(activity, 2)
//        postRv?.adapter = mAdapter
//        mAdapter.resetAdapter()
//        mAdapter.startDataLoading()
    }

//    private fun observeUserProfile() =
//        mPresenter.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                when (it) {
//                    is Loading -> showLoader()
//                    is ErrorMessage -> {
//
//                    }
//                    is Success -> {
//                        setMainUi(it.data)
//                    }
//                }
//            }
//        })

    private fun setMainUi() {
        ffViewPager = view?.findViewById(R.id.view_pager)
        tabLayout = view?.findViewById(R.id.tp_follow)
//        tabLayout?.addNewTab("Followers")
//        tabLayout?.addNewTab("Followings")
        tabLayout?.visibility = View.VISIBLE
        tabLayout?.apply {
            tabLayout.setTabTextColors(
                MethodChecker.getColor(
                    activity,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                ),
                MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            )
        }
//        tabLayout?.addNewTab("Followers")
//        tabLayout?.addNewTab("Followings")

        initViewPager(ffViewPager!!)
//
//        // If we dont use setupWithViewPager() method then
//        // tabs are not used or shown when activity opened
        tabLayout?.setupWithViewPager(ffViewPager!!)
        tabLayout?.getUnifyTabLayout()?.setupWithViewPager(ffViewPager!!)
        tabLayout?.show()
    }

    var adapter: ProfileFollowUnfollowViewPagerAdapter? = null
    private fun initViewPager(viewPager: ViewPager) {
        adapter = ProfileFollowUnfollowViewPagerAdapter(requireFragmentManager())

        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        arguments?.let { FollowerListingFragment.newInstance(it) }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(EXTRA_TOTAL_FOLLOWERS, "Followers") + " " + "Followers"
            )
        }
        arguments?.let { FollowingListingFragment.newInstance(it) }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(EXTRA_TOTAL_FOLLOWINGS, "Followings") + " " + "Following"
            )
        }

        // setting adapter to view pager.
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }


            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun showLoader() {

    }


    private fun setHeader() {
        val header = view?.findViewById<HeaderUnify>(R.id.header_follower)
        header?.apply {

            title = arguments?.getString(EXTRA_DISPLAY_NAME).toString()
            val subTitle = arguments?.getString(EXTRA_USER_NAME).toString()

            if (subTitle.isNotBlank()) {
                subtitle = "@$subTitle"
            }

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

    override fun onClick(source: View) {
        when (source.id) {
            R.id.gr_follower -> {
                Toast.makeText(context, "Follower", Toast.LENGTH_SHORT).show()
            }
            R.id.gr_following -> {
                Toast.makeText(context, "following", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

        }
    }

    companion object {
        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowerFollowingListingFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun onEmptyList(rawObject: Any?) {
        // TODO("Not yet implemented")
    }

    override fun onStartFirstPageLoad() {
        //  TODO("Not yet implemented")
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        //  TODO("Not yet implemented")
    }

    override fun onStartPageLoad(pageNumber: Int) {
        // TODO("Not yet implemented")
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        //  TODO("Not yet implemented")
    }

    override fun onError(pageNumber: Int) {
        // TODO("Not yet implemented")
    }

}

