package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment : BaseDaggerFragment(), FeedContentCreationTypeBottomSheet.Listener {

    private var binding: FragmentFeedBaseBinding? = null

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
        binding = FragmentFeedBaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedMainViewModel.fetchFeedTabs()

        observeFeedTabData()
        observeCreateContentBottomSheetData()
    }

    override fun onDestroyView() {
        binding = null
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
        feedMainViewModel.feedTabs.observe(
            viewLifecycleOwner
        ) {
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
        if (userSession.isLoggedIn) {
            binding?.let {
                it.btnFeedCreatePost.show()
                it.feedUserProfileImage.show()
            }
        } else {
            binding?.let {
                it.btnFeedCreatePost.hide()
                it.feedUserProfileImage.hide()
            }
        }
    }

    private fun initView(data: FeedTabsModel) {
        binding?.let {
            adapter = FeedPagerAdapter(childFragmentManager, lifecycle, data.data)

            it.vpFeedTabItemsContainer.adapter = adapter
            it.vpFeedTabItemsContainer.registerOnPageChangeCallback(object :
                OnPageChangeCallback() {
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

            if (firstTabData != null) {
                it.tyFeedFirstTab.text = firstTabData.title
                it.tyFeedFirstTab.setOnClickListener { _ ->
                    it.vpFeedTabItemsContainer.setCurrentItem(TAB_FIRST_INDEX, true)
                }
                it.tyFeedFirstTab.show()
            } else {
                it.tyFeedFirstTab.hide()
            }

            if (secondTabData != null) {
                it.tyFeedSecondTab.text = secondTabData.title
                it.tyFeedSecondTab.setOnClickListener { _ ->
                    it.vpFeedTabItemsContainer.setCurrentItem(TAB_SECOND_INDEX, true)
                }
                it.tyFeedSecondTab.show()
            } else {
                it.tyFeedSecondTab.hide()
            }

            if (data.meta.showMyProfile && data.meta.profilePhotoUrl.isNotEmpty()) {
                it.feedUserProfileImage.setImageUrl(data.meta.profilePhotoUrl)
                it.feedUserProfileImage.setOnClickListener { _ ->
                    RouteManager.route(it.root.context, data.meta.profileApplink)
                }
                it.feedUserProfileImage.show()
            } else {
                it.feedUserProfileImage.hide()
            }

            it.btnFeedCreatePost.setOnClickListener {
                onCreatePostClicked()
            }

            it.btnFeedLive.setOnClickListener {
                onNavigateToLive()
            }

            it.feedUserProfileImage.setOnClickListener {
                onNavigateToProfile()
            }
        }
    }

    private fun onChangeTab(position: Int) {
        binding?.let {
            val newTabView =
                if (position == TAB_FIRST_INDEX) it.tyFeedFirstTab else it.tyFeedSecondTab

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
            RouteManager.route(it, getString(R.string.feed_applink_live_room))
        }
    }

    private fun onNavigateToProfile() {
        Toast.makeText(context, "Navigate to Profile", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAB_FIRST_INDEX = 0
        const val TAB_SECOND_INDEX = 1
    }
}
