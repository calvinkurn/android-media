package com.tokopedia.people.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentFollowerFollowingListingBinding
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_DISPLAY_NAME
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_IS_FOLLOWERS
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWERS
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.EXTRA_TOTAL_FOLLOWINGS
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.unifycomponents.setCustomText
import javax.inject.Inject

class FollowerFollowingListingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private var userProfileTracker: UserProfileTracker
) : TkpdBaseV4Fragment() {

    private var _binding: UpFragmentFollowerFollowingListingBinding? = null
    private val binding: UpFragmentFollowerFollowingListingBinding
        get() = _binding!!

    private var userId = ""

    private val viewModel: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position == FOLLOWING_PAGE_POSITION) {
                userProfileTracker.openFollowingTab(userId)
            } else {
                userProfileTracker.openFollowersTab(userId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpFragmentFollowerFollowingListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        setMainUi()

        observeFollowCount()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        _binding = null
    }

    private fun setMainUi() {
        val bundle = arguments ?: return

        val totalFollowers = bundle.getString(EXTRA_TOTAL_FOLLOWERS).orEmpty()
        val totalFollowing = bundle.getString(EXTRA_TOTAL_FOLLOWINGS).orEmpty()

        binding.tpFollow.getUnifyTabLayout().setTabTextColors(
            MethodChecker.getColor(
                activity,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600
            ),
            MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        )

        val listOfPages = listOf<Pair<String, Fragment>>(
            Pair(
                String.format(getString(R.string.up_title_followers), totalFollowers),
                FollowerListingFragment.getFragment(
                    childFragmentManager,
                    requireContext().classLoader,
                    bundle
                )
            ),
            Pair(
                String.format(getString(R.string.up_title_following), totalFollowing),
                FollowingListingFragment.getFragment(
                    childFragmentManager,
                    requireContext().classLoader,
                    bundle
                )
            )
        )

        binding.viewPager.adapter = ViewPagerAdapter(this, listOfPages.map(Pair<String, Fragment>::second))

        TabsUnifyMediator(binding.tpFollow, binding.viewPager) { tab, position ->
            tab.setCustomText(listOfPages[position].first)
        }

        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        if (arguments?.getBoolean(EXTRA_IS_FOLLOWERS, true) == true) {
            binding.tpFollow.getUnifyTabLayout().getTabAt(FOLLOWERS_PAGE_POSITION)?.select()
        } else {
            binding.tpFollow.getUnifyTabLayout().getTabAt(FOLLOWING_PAGE_POSITION)?.select()
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

    private fun observeFollowCount() {
        viewModel.followCount.observe(viewLifecycleOwner) {
            updateFollowCount(it)
        }
    }

    private fun updateFollowCount(followCount: FollowListUiModel.FollowCount) {
        binding.tpFollow.getUnifyTabLayout().let { tabLayout ->
            if (tabLayout.tabCount < DEFAULT_TAB_TOTAL) return@let

            tabLayout.getTabAt(FOLLOWERS_PAGE_POSITION)?.let { tab ->
                val oldValue = tab.getCustomText()
                val newValue = String.format(
                    getString(R.string.up_title_followers),
                    followCount.totalFollowers
                )
                if (oldValue != newValue) tab.setCustomText(newValue)
            }

            tabLayout.getTabAt(FOLLOWING_PAGE_POSITION)?.let {
                val oldValue = it.getCustomText()
                val newValue = String.format(
                    getString(com.tokopedia.people.R.string.up_title_following),
                    followCount.totalFollowing
                )
                if (oldValue != newValue) it.setCustomText(newValue)
            }
        }
    }

    override fun getScreenName(): String = TAG

    class ViewPagerAdapter(
        fragment: Fragment,
        private val pages: List<Fragment>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return pages.size
        }

        override fun createFragment(position: Int): Fragment {
            return pages[position]
        }
    }

    companion object {
        private const val TAG = "FollowerFollowingListingFragment"
        const val REQUEST_CODE_LOGIN_TO_FOLLOW = 100

        private const val FOLLOWERS_PAGE_POSITION = 0
        private const val FOLLOWING_PAGE_POSITION = 1
        private const val DEFAULT_TAB_TOTAL = 2

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): FollowerFollowingListingFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FollowerFollowingListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowerFollowingListingFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FollowerFollowingListingFragment
        }
    }
}
