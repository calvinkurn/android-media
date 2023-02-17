package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment : BaseDaggerFragment(), FeedContentCreationTypeBottomSheet.Listener {

    private var binding: FragmentFeedBaseBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedMainViewModel: FeedMainViewModel

    private var adapter: FeedPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            feedMainViewModel = viewModelProvider[FeedMainViewModel::class.java]
            feedMainViewModel.fetchFeedTabs()
        }
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

        feedMainViewModel.feedTabs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> initView(it.data)
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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

    private fun initView(data: FeedTabsModel) {
        binding?.let {
            adapter = FeedPagerAdapter(requireActivity(), data.data)

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

            it.tyFeedForYouTab.setOnClickListener { _ ->
                it.vpFeedTabItemsContainer.setCurrentItem(TAB_FOR_YOU_INDEX, true)
            }

            it.tyFeedFollowingTab.setOnClickListener { _ ->
                it.vpFeedTabItemsContainer.setCurrentItem(TAB_FOLLOWING_INDEX, true)
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
                .getFragment(it.supportFragmentManager, it.classLoader)
            creationBottomSheet.setListener(this)
            creationBottomSheet.setData(creationItemList)
            creationBottomSheet.show(it.supportFragmentManager)
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
            }

            CreateContentType.CREATE_SHORT_VIDEO -> {
                RouteManager.route(requireContext(), ApplinkConst.PLAY_SHORTS)

            }

        }
    }

}
