package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.adapter.bottomsheet.FeedContentCreationTypeBottomSheet
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.onboarding.ImmersiveFeedOnboarding
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play_common.util.extension.awaitLayout
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment : BaseDaggerFragment(), FeedContentCreationTypeBottomSheet.Listener {

    private var _binding: FragmentFeedBaseBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var userSession: UserSessionInterface


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val feedMainViewModel: FeedMainViewModel by viewModels { viewModelFactory }

    private var adapter: FeedPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is FeedContentCreationTypeBottomSheet -> {
                    fragment.setListener(this)
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        feedMainViewModel.fetchFeedTabs()
        feedMainViewModel.fetchData()
        observeFeedTabData()
        observeCreateContentBottomSheetData()
    }

    private fun observeFeedTabData() {
        feedMainViewModel.feedTabs.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> initView(it.data)
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeCreateContentBottomSheetData() {
        feedMainViewModel.feedCreateContentBottomSheetData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Success -> {
                }
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        feedMainViewModel.checkLoginStatus()
    }

    override fun onDestroyView() {
        _binding = null
        adapter = null
        super.onDestroyView()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    private fun initView(data: FeedTabsModel) {
        adapter = FeedPagerAdapter(requireActivity(), data.data)

        binding.vpFeedTabItemsContainer.adapter = adapter
        binding.vpFeedTabItemsContainer.registerOnPageChangeCallback(object :
            OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                onChangeTab(position)
            }
        })

        binding.btnFeedCreatePost.showWithCondition(data.meta.createContentAllowed)
        binding.feedUserProfileImage.showWithCondition(data.meta.shouldShowProfile)

        val onboarding = ImmersiveFeedOnboarding.Builder(requireContext())
            .setCreateContentView(
                if (data.meta.createContentAllowed) binding.btnFeedCreatePost
                else null
            )
            .setProfileEntryPointView(
                if (data.meta.shouldShowProfile) binding.feedUserProfileImage
                else null
            )
            .build()

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            onboarding.show()
        }

        binding.tyFeedForYouTab.setOnClickListener { _ ->
            binding.vpFeedTabItemsContainer.setCurrentItem(TAB_FOR_YOU_INDEX, true)
        }

        binding.tyFeedFollowingTab.setOnClickListener { _ ->
            binding.vpFeedTabItemsContainer.setCurrentItem(TAB_FOLLOWING_INDEX, true)
        }

        binding.btnFeedCreatePost.setOnClickListener {
            onCreatePostClicked()
        }

        binding.btnFeedLive.setOnClickListener {
            onNavigateToLive()
        }
        binding.feedUserProfileImage.setImageUrl(data.meta.profilePhotoUrl)

        binding.feedUserProfileImage.setOnClickListener {
            onNavigateToProfile()
        }
    }

    private fun onChangeTab(position: Int) {
        binding.let {
            val newTabView = if (position == 0) it.tyFeedForYouTab else it.tyFeedFollowingTab

            val newConstraintSet = ConstraintSet()
            newConstraintSet.clone(it.root)
            newConstraintSet.connect(
                it.viewFeedTabIndicator.id,
                ConstraintSet.TOP,
                newTabView.id,
                ConstraintSet.BOTTOM
            )
            newConstraintSet.connect(
                it.viewFeedTabIndicator.id,
                ConstraintSet.START,
                newTabView.id,
                ConstraintSet.START
            )
            newConstraintSet.connect(
                it.viewFeedTabIndicator.id,
                ConstraintSet.END,
                newTabView.id,
                ConstraintSet.END
            )

            newConstraintSet.applyTo(it.root)
        }
    }

    private fun onCreatePostClicked() {
        activity?.let {
            val creationBottomSheet = FeedContentCreationTypeBottomSheet
                .getFragment(childFragmentManager, it.classLoader)

            val feedCreateBottomSheetDataResult = feedMainViewModel.feedCreateContentBottomSheetData.value
            if (feedCreateBottomSheetDataResult is Success) {
                val list = feedCreateBottomSheetDataResult.data
                if (list.isNotEmpty()) {
                    creationBottomSheet.setData(feedCreateBottomSheetDataResult.data)
                    creationBottomSheet.show(childFragmentManager)
                }
            }
        }
    }

    private fun onNavigateToLive() {
        Toast.makeText(context, "Navigate to Live", Toast.LENGTH_SHORT).show()
    }

    private fun onNavigateToProfile() {
        Toast.makeText(context, "Navigate to Profile", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAB_FOR_YOU_INDEX = 0
        const val TAB_FOLLOWING_INDEX = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreationItemClick(creationTypeItem: ContentCreationTypeItem) {
        when (creationTypeItem.type) {
            CreateContentType.CREATE_LIVE -> {
                RouteManager.route(
                    requireContext(),
                    ApplinkConst.PLAY_BROADCASTER
                )
            }
            CreateContentType.CREATE_POST -> {
                val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2)
                intent.putExtra(
                    BundleData.APPLINK_AFTER_CAMERA_CAPTURE,
                    ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
                )
                intent.putExtra(
                    BundleData.MAX_MULTI_SELECT_ALLOWED,
                    BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
                )
                intent.putExtra(
                    BundleData.TITLE,
                    getString(R.string.feed_post_sebagai)
                )
                intent.putExtra(
                    BundleData.APPLINK_FOR_GALLERY_PROCEED,
                    ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
                )
                startActivity(intent)
                TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))

            }

            CreateContentType.CREATE_SHORT_VIDEO -> {
                RouteManager.route(requireContext(), ApplinkConst.PLAY_SHORTS)
            }
            else -> {}
        }
    }
}
