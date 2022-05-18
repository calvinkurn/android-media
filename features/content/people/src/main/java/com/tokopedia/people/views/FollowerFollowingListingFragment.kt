package com.tokopedia.people.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.people.R
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.views.UserProfileFragment.Companion.EXTRA_DISPLAY_NAME
import com.tokopedia.people.views.UserProfileFragment.Companion.EXTRA_IS_FOLLOWERS
import com.tokopedia.people.views.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWERS
import com.tokopedia.people.views.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWINGS
import com.tokopedia.unifycomponents.TabsUnify
import javax.inject.Inject


class FollowerFollowingListingFragment : BaseDaggerFragment() {

    private var userId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var tabLayout: TabsUnify? = null
    var ffViewPager: ViewPager? = null
    var isFollowersTab: Boolean = true


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
        isFollowersTab = arguments?.getBoolean(EXTRA_IS_FOLLOWERS, true) == true
        setHeader()
        setMainUi()
    }

    private fun setMainUi() {
        ffViewPager = view?.findViewById(R.id.view_pager)
        tabLayout = view?.findViewById(R.id.tp_follow)
        tabLayout?.apply {
            tabLayout.setTabTextColors(
                MethodChecker.getColor(
                    activity,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                ),
                MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            )
        }

        initViewPager(ffViewPager)

        ffViewPager?.apply {
            tabLayout?.setupWithViewPager(this)
            tabLayout?.getUnifyTabLayout()?.setupWithViewPager(this)
        }
        tabLayout?.getUnifyTabLayout()?.removeAllTabs()
        tabLayout?.addNewTab(
            arguments?.getString(
                EXTRA_TOTAL_FOLLOWERS,
                getString(com.tokopedia.people.R.string.up_lb_followers)
            )
                    + " " +
                    getString(com.tokopedia.people.R.string.up_lb_followers)
        )
        tabLayout?.addNewTab(
            arguments?.getString(EXTRA_TOTAL_FOLLOWINGS, getString(R.string.up_lb_following))
                    + " " +
                    getString(R.string.up_lb_following)
        )



        if(tabLayout != null
            && tabLayout?.getUnifyTabLayout() != null
            && tabLayout?.getUnifyTabLayout()?.tabCount!! >= 2
        ) {
            if (isFollowersTab) {
                tabLayout?.getUnifyTabLayout()?.getTabAt(0)?.select()
            } else {
                tabLayout?.getUnifyTabLayout()?.getTabAt(1)?.select()
            }
        }
    }

    var adapter: ProfileFollowUnfollowViewPagerAdapter? = null
    private fun initViewPager(viewPager: ViewPager?) {
        adapter = ProfileFollowUnfollowViewPagerAdapter(requireFragmentManager())

        arguments?.let { FollowerListingFragment.newInstance(it) }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(
                    EXTRA_TOTAL_FOLLOWERS,
                    getString(com.tokopedia.people.R.string.up_lb_followers)
                )
                        + " " + getString(com.tokopedia.people.R.string.up_lb_followers)
            )
        }
        arguments?.let { FollowingListingFragment.newInstance(it) }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(EXTRA_TOTAL_FOLLOWINGS, getString(R.string.up_lb_following))
                        + " " + getString(R.string.up_lb_following)
            )
        }

        // setting adapter to view pager.
        viewPager?.adapter = adapter

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position == 1) {
                    UserProfileTracker().openFollowingTab(
                        userId
                    )
                }
                else{
                    UserProfileTracker().openFollowersTab(
                        userId
                    )
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setHeader() {
        val header = view?.findViewById<HeaderUnify>(R.id.header_follower)
        header?.apply {

            title = arguments?.getString(EXTRA_DISPLAY_NAME).toString()
            userId = arguments?.getString(UserProfileFragment.EXTRA_USER_ID).toString()
            subheaderView?.gone()

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

        }
    }

    override fun getScreenName(): String {
        return SCREEN
    }


    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

 

    companion object {
        const val SCREEN = "FollowerFollowingListFragment"
        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowerFollowingListingFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}

