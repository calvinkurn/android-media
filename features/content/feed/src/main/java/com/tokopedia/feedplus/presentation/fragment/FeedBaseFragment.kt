package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.adapter.bottomsheet.FeedContentCreationTypeBottomSheet
import com.tokopedia.feedplus.presentation.customview.UploadInfoView
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.onboarding.ImmersiveFeedOnboarding
import com.tokopedia.feedplus.presentation.receiver.FeedMultipleSourceUploadReceiver
import com.tokopedia.feedplus.presentation.receiver.UploadInfo
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    @Inject
    lateinit var uploadReceiver: FeedMultipleSourceUploadReceiver.Factory

    private var adapter: FeedPagerAdapter? = null

    private var liveApplink: String = ""
    private var profileApplink: String = ""

    private var mOnboarding: ImmersiveFeedOnboarding? = null

    private val appLinkTabPosition: Int
        get() = arguments?.getInt(
            ApplinkConstInternalContent.EXTRA_FEED_TAB_POSITION,
            TAB_FIRST_INDEX
        ) ?: TAB_FIRST_INDEX

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

        observeFeedTabData()
        observeCreateContentBottomSheetData()

        observeUpload()
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

    private fun observeUpload() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                uploadReceiver.create(this@FeedBaseFragment)
                    .observe()
                    .collectLatest { info ->
                        when (info) {
                            is UploadInfo.Progress -> {
                                binding.uploadView.show()
                                binding.uploadView.setProgress(info.progress)
                                binding.uploadView.setThumbnail(info.thumbnailUrl)
                            }
                            UploadInfo.Finished -> {
                                binding.uploadView.hide()
                            }
                            is UploadInfo.Failed -> {
                                binding.uploadView.setFailed()
                                binding.uploadView.setRetryWhenFailed(info.onRetry)
                            }
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        feedMainViewModel.fetchFeedTabs()
    }

    override fun onPause() {
        super.onPause()
        mOnboarding?.dismiss()
        mOnboarding = null
    }

    private fun initView(data: FeedTabsModel) {
        adapter = FeedPagerAdapter(
            childFragmentManager,
            lifecycle,
            data.data,
            appLinkExtras = arguments ?: Bundle.EMPTY,
        )

        binding.vpFeedTabItemsContainer.adapter = adapter
        binding.vpFeedTabItemsContainer.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                onChangeTab(position)
            }
        })

        var firstTabData: FeedDataModel? = null
        var secondTabData: FeedDataModel? = null

        if (data.data.isNotEmpty()) {
            firstTabData = data.data[TAB_FIRST_INDEX]
            if (data.data.size > TAB_SECOND_INDEX && data.data[TAB_SECOND_INDEX].isActive) {
                secondTabData = data.data[TAB_SECOND_INDEX]
            }
        }

        mOnboarding = ImmersiveFeedOnboarding.Builder(requireContext())
            .setCreateContentView(
                if (data.meta.isCreationActive && !feedMainViewModel.hasShownCreateContent()) {
                    binding.btnFeedCreatePost
                } else {
                    null
                }
            )
            .setProfileEntryPointView(
                if (data.meta.showMyProfile && !feedMainViewModel.hasShownProfileEntryPoint()) {
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

        if (firstTabData != null) {
            binding.tyFeedFirstTab.text = firstTabData.title
            binding.tyFeedFirstTab.setOnClickListener { _ ->
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_FIRST_INDEX, true)
            }
            binding.tyFeedFirstTab.show()
        } else {
            binding.tyFeedFirstTab.hide()
        }

        if (secondTabData != null) {
            binding.tyFeedSecondTab.text = secondTabData.title
            binding.tyFeedSecondTab.setOnClickListener { _ ->
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_SECOND_INDEX, true)
            }
            binding.tyFeedSecondTab.show()
        } else {
            binding.tyFeedSecondTab.hide()
        }

        if (data.meta.showMyProfile) {
            if (data.meta.profilePhotoUrl.isNotEmpty())
                binding.feedUserProfileImage.setImageUrl(data.meta.profilePhotoUrl)
            binding.feedUserProfileImage.setOnClickListener { _ ->
                RouteManager.route(binding.root.context, data.meta.profileApplink)
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

        if (data.meta.isCreationActive) {
            binding.btnFeedCreatePost.show()
        } else {
            binding.btnFeedCreatePost.hide()
        }

        if (data.meta.showLive) {
            binding.btnFeedLive.show()
        } else {
            binding.btnFeedLive.hide()
        }

        scrollToDefaultTabPosition()
    }

    private fun scrollToDefaultTabPosition() {
        binding.vpFeedTabItemsContainer.setCurrentItem(appLinkTabPosition, true)
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
    }
}
