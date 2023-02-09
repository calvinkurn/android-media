package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapter
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment : BaseDaggerFragment() {
    private var binding: FragmentFeedBaseBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedMainViewModel: FeedMainViewModel

    private var adapter: FeedAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

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
        initView()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    private fun initView() {
        binding?.let {
            LinearSnapHelper().attachToRecyclerView(it.rvFeedTabItemsContainer)
            adapter = FeedAdapter()
            linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            it.rvFeedTabItemsContainer.layoutManager = linearLayoutManager
            it.rvFeedTabItemsContainer.adapter = adapter

            it.rvFeedTabItemsContainer.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            linearLayoutManager?.let { lm ->
                                val position = lm.findFirstVisibleItemPosition()
                                onChangeTab(position)
                            }
                        }
                    }
                })

            it.tyFeedForYouTab.setOnClickListener { _ ->
                it.rvFeedTabItemsContainer.smoothScrollToPosition(TAB_FOR_YOU_INDEX)
            }

            it.tyFeedFollowingTab.setOnClickListener { _ ->
                it.rvFeedTabItemsContainer.smoothScrollToPosition(TAB_FOLLOWING_INDEX)
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
        Toast.makeText(context, "Create Post Clicked", Toast.LENGTH_SHORT).show()
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
}
