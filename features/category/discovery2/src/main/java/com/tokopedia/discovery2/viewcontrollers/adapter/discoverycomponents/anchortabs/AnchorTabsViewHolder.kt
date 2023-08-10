package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ScrollData
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.Toaster

const val MILLI_SECONDS_PER_INCH = 100f
const val DELAY_FOR_DEBOUNCE = 200L

class AnchorTabsViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val anchorRV: RecyclerView = itemView.findViewById(R.id.anchor_rv)
    private var linearLayoutManager: LinearLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    var viewModel: AnchorTabsViewModel? = null

    val observer = Observer<ScrollData> { data ->
        data?.let {
            viewModel?.let { anchorTabsViewModel ->
                if (anchorTabsViewModel.pauseDispatchChanges && !data.isAutoScroll) {
                    anchorTabsViewModel.pauseDispatchChanges = false
                    anchorTabsViewModel.updateSelectedSection(anchorTabsViewModel.selectedSectionId, false)
                }
            }
        }
    }

    private var smoothScroller: SmoothScroller = object : LinearSmoothScroller(itemView.context) {
        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
                return MILLI_SECONDS_PER_INCH / displayMetrics.densityDpi
            } else {
                super.calculateSpeedPerPixel(displayMetrics)
            }
        }
    }

    init {
        anchorRV.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(false)
        anchorRV.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as AnchorTabsViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.let { viewModel ->
                viewModel.getCarouselItemsListData().observe(it) { item ->
                    if (fragment.view != null) {
                        mDiscoveryRecycleAdapter.addDataList(item)
                        anchorRV.post {
                            if (viewModel.selectedSectionPos < viewModel.getListSize()) {
                                if (viewModel.wasSectionDeleted()) {
                                    linearLayoutManager.scrollToPositionWithOffset(
                                        viewModel.selectedSectionPos,
                                        0
                                    )
                                } else {
                                    smoothScroller.targetPosition = viewModel.selectedSectionPos
                                    linearLayoutManager.startSmoothScroll(smoothScroller)
                                }
                            }
                        }
                    }
                }

                viewModel.getUpdatePositionsLD().observe(it) { shouldUpdate ->
                    if (shouldUpdate) {
                        updatePositionChanged()
                    }
                }

                (fragment as DiscoveryFragment).getScrollLiveData().observe(it, observer)

                viewModel.shouldShowMissingSectionToaster().observe(it) { shouldShowToast ->
                    if (shouldShowToast) {
                        fragment.activity?.let { activity ->
                            SnackbarManager.getContentView(activity)
                        }?.let { contentView ->
                            Toaster.build(
                                contentView,
                                itemView.context.getString(R.string.discovery_this_section_is_still_empty),
                                Toast.LENGTH_SHORT,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getCarouselItemsListData()?.removeObservers(it)
            viewModel?.getUpdatePositionsLD()?.removeObservers(it)
            viewModel?.shouldShowMissingSectionToaster()?.removeObservers(it)
            (fragment as DiscoveryFragment).getScrollLiveData().removeObserver(observer)
        }
    }

    private fun updatePositionChanged() {
        mDiscoveryRecycleAdapter.submitList(ArrayList(viewModel?.components?.getComponentsItem()!!))
        viewModel?.let { viewModel ->
            anchorRV.postDelayed({
                if (viewModel.selectedSectionPos < viewModel.getListSize()) {
                    smoothScroller.targetPosition = viewModel.selectedSectionPos
                    linearLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, DELAY_FOR_DEBOUNCE)
        }
    }
}
