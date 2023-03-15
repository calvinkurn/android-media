package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.adapter.bottomsheet.FeedContentCreationTypeBottomSheet
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.onboarding.ImmersiveFeedOnboarding
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
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

    private var liveApplink: String = ""
    private var profileApplink: String = ""

    private var mOnboarding: ImmersiveFeedOnboarding? = null

    private val onNonLoginGoToFollowingTab =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (userSession.isLoggedIn) {
                Toaster.build(
                    binding.root,
                    getString(
                        R.string.feed_report_login_success_toaster_text,
                        userSession.name
                    ),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
                ).show()

                feedMainViewModel.changeCurrentTabByType(TAB_TYPE_FOLLOWING)
            } else {
                feedMainViewModel.changeCurrentTabByType(TAB_TYPE_FOR_YOU)
            }
        }

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
    ): View {
        _binding = FragmentFeedBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedMainViewModel.fetchFeedTabs()

        observeFeedTabData()
        observeCreateContentBottomSheetData()
        observeCurrentTabPosition()
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

    override fun onResume() {
        super.onResume()
        feedMainViewModel.fetchFeedMetaData()
    }

    private fun observeFeedTabData() {
        feedMainViewModel.feedTabs.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> initTabsView(it.data)
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        feedMainViewModel.metaData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> initMetaView(it.data)
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeCreateContentBottomSheetData() {
        feedMainViewModel.feedCreateContentBottomSheetData.observe(viewLifecycleOwner) {
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

    private fun observeCurrentTabPosition() {
        feedMainViewModel.currentTabIndex.observe(viewLifecycleOwner) {
            binding.vpFeedTabItemsContainer.setCurrentItem(it, true)
            onChangeTab(it)
        }
    }

    override fun onPause() {
        super.onPause()
        mOnboarding?.dismiss()
        mOnboarding = null
    }

    private fun initMetaView(meta: MetaModel) {
        liveApplink = meta.liveApplink
        profileApplink = meta.profileApplink

        mOnboarding = ImmersiveFeedOnboarding.Builder(requireContext())
            .setCreateContentView(
                if (meta.isCreationActive && !feedMainViewModel.hasShownCreateContent()) {
                    binding.btnFeedCreatePost
                } else {
                    null
                }
            )
            .setProfileEntryPointView(
                if (meta.showMyProfile && !feedMainViewModel.hasShownProfileEntryPoint()) {
                    binding.feedUserProfileImage
                } else {
                    null
                }
            )
            .setListener(object : ImmersiveFeedOnboarding.Listener {
                override fun onStarted() {
                    binding.vOnboardingPreventInteraction.show()
                }

                override fun onCompleteCreateContentOnboarding() {
                    feedMainViewModel.setHasShownCreateContent()
                }

                override fun onCompleteProfileEntryPointOnboarding() {
                    feedMainViewModel.setHasShownProfileEntryPoint()
                }

                override fun onDismissed() {
                    binding.vOnboardingPreventInteraction.hide()
                }
            })
            .build()

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            mOnboarding?.show()
        }

        if (meta.showMyProfile) {
            if (meta.profilePhotoUrl.isNotEmpty()) {
                binding.feedUserProfileImage.setImageUrl(meta.profilePhotoUrl)
            }
            binding.feedUserProfileImage.setOnClickListener {
                RouteManager.route(binding.root.context, meta.profileApplink)
            }
            binding.feedUserProfileImage.show()
        } else {
            binding.feedUserProfileImage.hide()
        }

        binding.btnFeedCreatePost.setOnClickListener {
            onCreatePostClicked()
        }

        binding.btnFeedLive.setOnClickListener {
            onNavigateToLive()
        }

        binding.feedUserProfileImage.setOnClickListener {
            onNavigateToProfile()
        }

        if (meta.isCreationActive) {
            binding.btnFeedCreatePost.show()
        } else {
            binding.btnFeedCreatePost.hide()
        }

        if (meta.showLive) {
            binding.btnFeedLive.show()
            binding.labelFeedLive.show()
        } else {
            binding.btnFeedLive.hide()
            binding.labelFeedLive.hide()
        }
    }

    private fun initTabsView(data: List<FeedDataModel>) {
        adapter = FeedPagerAdapter(childFragmentManager, lifecycle, data)

        binding.vpFeedTabItemsContainer.adapter = adapter
        binding.vpFeedTabItemsContainer.registerOnPageChangeCallback(object :
                OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (feedMainViewModel.getTabType(position) == TAB_TYPE_FOLLOWING && !userSession.isLoggedIn) {
                        onNonLoginGoToFollowingTab.launch(
                            RouteManager.getIntent(
                                context,
                                ApplinkConst.LOGIN
                            )
                        )
                    }
                    onChangeTab(position)
                }
            })

        var firstTabData: FeedDataModel? = null
        var secondTabData: FeedDataModel? = null

        if (data.isNotEmpty()) {
            firstTabData = data[TAB_FIRST_INDEX]
            if (data.size > TAB_SECOND_INDEX && data[TAB_SECOND_INDEX].isActive) {
                secondTabData = data[TAB_SECOND_INDEX]
            }
        }

        if (firstTabData != null) {
            binding.tyFeedFirstTab.text = firstTabData.title
            binding.tyFeedFirstTab.setOnClickListener {
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_FIRST_INDEX, true)
            }
            binding.tyFeedFirstTab.show()
        } else {
            binding.tyFeedFirstTab.hide()
        }

        if (secondTabData != null) {
            binding.tyFeedSecondTab.text = secondTabData.title
            binding.tyFeedSecondTab.setOnClickListener {
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_SECOND_INDEX, true)
            }
            binding.tyFeedSecondTab.show()
        } else {
            binding.tyFeedSecondTab.hide()
        }
    }

    private fun onChangeTab(position: Int) {
        val newTabView = if (position == TAB_FIRST_INDEX) {
            binding.tyFeedFirstTab
        } else {
            binding.tyFeedSecondTab
        }

        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(binding.root)
        newConstraintSet.connect(
            binding.viewFeedTabIndicator.id,
            ConstraintSet.TOP,
            newTabView.id,
            ConstraintSet.BOTTOM
        )
        newConstraintSet.connect(
            binding.viewFeedTabIndicator.id,
            ConstraintSet.START,
            newTabView.id,
            ConstraintSet.START
        )
        newConstraintSet.connect(
            binding.viewFeedTabIndicator.id,
            ConstraintSet.END,
            newTabView.id,
            ConstraintSet.END
        )

        newConstraintSet.applyTo(binding.root)
    }

    private fun onCreatePostClicked() {
        activity?.let {
            val creationBottomSheet = FeedContentCreationTypeBottomSheet
                .getFragment(childFragmentManager, it.classLoader)

            val feedCreateBottomSheetDataResult =
                feedMainViewModel.feedCreateContentBottomSheetData.value
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
        context?.let {
            RouteManager.route(it, liveApplink)
        }
    }

    private fun onNavigateToProfile() {
        context?.let {
            RouteManager.route(it, profileApplink)
        }
    }

    companion object {
        const val TAB_FIRST_INDEX = 0
        const val TAB_SECOND_INDEX = 1

        const val TAB_TYPE_FOR_YOU = "foryou"
        const val TAB_TYPE_FOLLOWING = "following"
    }
}
