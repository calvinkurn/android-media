package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment : BaseDaggerFragment() {
    private var binding: FragmentFeedBaseBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val feedMainViewModel: FeedMainViewModel by viewModels { viewModelFactory }

    private var adapter: FeedPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedMainViewModel.fetchFeedTabs()
        feedMainViewModel.feedTabs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> initView(it.data)
                is Fail -> {

                }
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
                it.feedUserProfileImage.loadImage(data.meta.profilePhotoUrl)
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
        Toast.makeText(context, "Create Post Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun onNavigateToLive() {
        Toast.makeText(context, "Navigate to Live", Toast.LENGTH_SHORT).show()
    }

    private fun onNavigateToProfile() {
        Toast.makeText(context, "Navigate to Profile", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAB_FIRST_INDEX = 0
        const val TAB_SECOND_INDEX = 1
    }
}
