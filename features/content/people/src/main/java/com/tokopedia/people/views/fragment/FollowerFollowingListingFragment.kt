package com.tokopedia.people.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFollowFollowingParentContentBinding
import com.tokopedia.people.databinding.UpFollowFollowingParentShimmerBinding
import com.tokopedia.people.databinding.UpFragmentFollowerFollowingListingBinding
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity.Companion.EXTRA_ACTIVE_TAB
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity.Companion.EXTRA_USERNAME
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.state.LoadingState
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.unifycomponents.setCustomText
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.people.R as peopleR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FollowerFollowingListingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private var userProfileTracker: UserProfileTracker
) : TkpdBaseV4Fragment() {

    private var _binding: UpFragmentFollowerFollowingListingBinding? = null
    private val binding: UpFragmentFollowerFollowingListingBinding
        get() = _binding!!

    private val bindingContent: UpFollowFollowingParentContentBinding
        get() = binding.layoutFollowFollowingParentContent

    private val bindingShimmer: UpFollowFollowingParentShimmerBinding
        get() = binding.layoutFollowFollowingParentShimmer

    private val viewModel: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FollowerFollowingViewModel::class.java]
    }

    private val userId: String
        get() = arguments?.getString(EXTRA_USERNAME).orEmpty()

    private val selectedTab: String
        get() = arguments?.getString(EXTRA_ACTIVE_TAB).orEmpty()

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
        setData()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingContent.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        _binding = null
    }

    private fun setData() {
        viewModel.getProfile(userId)
    }

    private fun setTabs(
        prevState: ProfileUiModel?,
        currState: ProfileUiModel
    ) {
        if (prevState == currState || currState == ProfileUiModel.Empty) return

        bindingContent.tpFollow.getUnifyTabLayout().setTabTextColors(
            MethodChecker.getColor(
                activity,
                unifyprinciplesR.color.Unify_NN600
            ),
            MethodChecker.getColor(
                activity,
                unifyprinciplesR.color.Unify_GN500
            )
        )

        val listOfPages = listOf<Pair<String, Fragment>>(
            Pair(
                String.format(
                    getString(peopleR.string.up_title_followers),
                    currState.stats.totalFollowerFmt
                ),
                FollowerListingFragment.getFragment(
                    childFragmentManager,
                    requireContext().classLoader
                )
            ),
            Pair(
                String.format(
                    getString(peopleR.string.up_title_following),
                    currState.stats.totalFollowingFmt
                ),
                FollowingListingFragment.getFragment(
                    childFragmentManager,
                    requireContext().classLoader
                )
            )
        )

        bindingContent.viewPager.adapter =
            ViewPagerAdapter(this, listOfPages.map(Pair<String, Fragment>::second))

        TabsUnifyMediator(bindingContent.tpFollow, bindingContent.viewPager) { tab, position ->
            tab.setCustomText(listOfPages[position].first)
        }

        bindingContent.viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        bindingContent.viewPager.setCurrentItem(
            if (selectedTab == EXTRA_FOLLOWING) {
                FOLLOWING_PAGE_POSITION
            } else {
                FOLLOWERS_PAGE_POSITION
            },
            false
        )
    }

    private fun setHeader(prevName: String?, currName: String) {
        if (prevName == currName) return

        val header = view?.findViewById<HeaderUnify>(peopleR.id.header_follower)
        header?.apply {
            title = currName

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.profileInfo.withCache().collectLatest { (prevState, currState) ->
                setHeader(prevState?.name, currState.name)
                setTabs(prevState, currState)
            }
        }
        viewModel.followCount.observe(viewLifecycleOwner) {
            updateFollowCount(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loadingState.withCache().collectLatest { (prevState, currState) ->
                updateLoadingPage(prevState, currState)
            }
        }
    }

    private fun updateLoadingPage(
        prevState: LoadingState?,
        currState: LoadingState
    ) {
        if (prevState == currState) return

        when (currState) {
            is LoadingState.Error -> {
                bindingContent.root.hide()
                bindingShimmer.root.hide()
                binding.globalError.show()
                binding.globalError.setType(GlobalError.SERVER_ERROR)
                binding.globalError.setActionClickListener { viewModel.getProfile(userId) }
            }

            LoadingState.Hide -> {
                bindingContent.root.show()
                binding.globalError.hide()
                bindingShimmer.root.hide()
            }

            LoadingState.Show -> {
                bindingContent.root.hide()
                binding.globalError.hide()
                bindingShimmer.root.show()
            }
        }
    }

    private fun updateFollowCount(followCount: FollowListUiModel.FollowCount) {
        bindingContent.tpFollow.getUnifyTabLayout().let { tabLayout ->
            if (tabLayout.tabCount < DEFAULT_TAB_TOTAL) return@let

            tabLayout.getTabAt(FOLLOWERS_PAGE_POSITION)?.let { tab ->
                val oldValue = tab.getCustomText()
                val newValue = String.format(
                    getString(peopleR.string.up_title_followers),
                    followCount.totalFollowers
                )
                if (oldValue != newValue) tab.setCustomText(newValue)
            }

            tabLayout.getTabAt(FOLLOWING_PAGE_POSITION)?.let {
                val oldValue = it.getCustomText()
                val newValue = String.format(
                    getString(peopleR.string.up_title_following),
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
        const val REQUEST_CODE_LOGIN_TO_FOLLOW = 100
        const val EXTRA_FOLLOWING = "following"
        const val EXTRA_FOLLOWERS = "followers"

        private const val TAG = "FollowerFollowingListingFragment"
        private const val FOLLOWERS_PAGE_POSITION = 0
        private const val FOLLOWING_PAGE_POSITION = 1
        private const val DEFAULT_TAB_TOTAL = 2

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): FollowerFollowingListingFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? FollowerFollowingListingFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowerFollowingListingFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FollowerFollowingListingFragment
        }
    }
}
