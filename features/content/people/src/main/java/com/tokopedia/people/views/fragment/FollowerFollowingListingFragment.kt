package com.tokopedia.people.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.views.TotalFollowListener
import com.tokopedia.people.views.adapter.ProfileFollowUnfollowViewPagerAdapter
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_DISPLAY_NAME
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_IS_FOLLOWERS
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWERS
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWINGS
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.unifycomponents.TabsUnify
import javax.inject.Inject

class FollowerFollowingListingFragment @Inject constructor(
    private var userProfileTracker: UserProfileTracker,
) : TkpdBaseV4Fragment(), TotalFollowListener {

    private var userId = ""

    var tabLayout: TabsUnify? = null
    var ffViewPager: ViewPager? = null
    var isFollowersTab: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                ),
                MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G500),
            )
        }

        initViewPager(ffViewPager)

        ffViewPager?.apply {
            tabLayout?.setupWithViewPager(this)
            tabLayout?.getUnifyTabLayout()?.setupWithViewPager(this)
        }
        tabLayout?.getUnifyTabLayout()?.removeAllTabs()

        val totalFollowers = arguments?.getString(EXTRA_TOTAL_FOLLOWERS).orEmpty()
        val totalFollowing = arguments?.getString(EXTRA_TOTAL_FOLLOWINGS).orEmpty()

        tabLayout?.addNewTab(
            String.format(
                getString(com.tokopedia.people.R.string.up_title_followers),
                totalFollowers
            )
        )
        tabLayout?.addNewTab(
            String.format(
                getString(com.tokopedia.people.R.string.up_title_following),
                totalFollowing
            )
        )

        if (tabLayout != null &&
            tabLayout?.getUnifyTabLayout() != null &&
            tabLayout?.getUnifyTabLayout()?.tabCount!! >= DEFAULT_TAB_TOTAL
        ) {
            if (isFollowersTab) {
                tabLayout?.getUnifyTabLayout()?.getTabAt(FOLLOWERS_PAGE_POSITION)?.select()
            } else {
                tabLayout?.getUnifyTabLayout()?.getTabAt(FOLLOWING_PAGE_POSITION)?.select()
            }
        }
    }

    var adapter: ProfileFollowUnfollowViewPagerAdapter? = null
    private fun initViewPager(viewPager: ViewPager?) {
        adapter = ProfileFollowUnfollowViewPagerAdapter(requireFragmentManager())

        arguments?.let {
            FollowerListingFragment.getFragment(
                childFragmentManager,
                requireContext().classLoader,
                it,
            )
        }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(
                    EXTRA_TOTAL_FOLLOWERS,
                    getString(com.tokopedia.people.R.string.up_lb_followers),
                ) +
                    " " + getString(com.tokopedia.people.R.string.up_lb_followers),
            )
        }

        arguments?.let {
            FollowingListingFragment.getFragment(
                childFragmentManager,
                requireContext().classLoader,
                it,
            )
        }?.let {
            adapter?.addFragment(
                it,
                arguments?.getString(EXTRA_TOTAL_FOLLOWINGS, getString(R.string.up_lb_following)) +
                    " " + getString(R.string.up_lb_following),
            )
        }

        // setting adapter to view pager.
        viewPager?.adapter = adapter

        viewPager?.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position == 1) {
                        userProfileTracker.openFollowingTab(
                            userId,
                        )
                    } else {
                        userProfileTracker.openFollowersTab(
                            userId,
                        )
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            },
        )
    }

    override fun updateFollowCount(followCount: FollowListUiModel.FollowCount) {
        tabLayout?.getUnifyTabLayout()?.let {
            if (it.tabCount < DEFAULT_TAB_TOTAL) return
            it.getTabAt(FOLLOWERS_PAGE_POSITION)?.text = String.format(
                getString(com.tokopedia.people.R.string.up_title_followers),
                followCount.totalFollowers
            )

            it.getTabAt(FOLLOWING_PAGE_POSITION)?.text = String.format(
                getString(com.tokopedia.people.R.string.up_title_following),
                followCount.totalFollowing
            )
        }
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

    override fun getScreenName(): String = TAG

    companion object {
        private const val TAG = "FollowerFollowingListingFragment"
        const val REQUEST_CODE_LOGIN_TO_FOLLOW = 100

        private const val FOLLOWERS_PAGE_POSITION = 0
        private const val FOLLOWING_PAGE_POSITION = 1
        private const val DEFAULT_TAB_TOTAL = 2

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): FollowerFollowingListingFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FollowerFollowingListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowerFollowingListingFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as FollowerFollowingListingFragment
        }
    }
}
