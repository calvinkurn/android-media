package com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel

class OrderTrackingToolbarHandler {

    private var toolbarLiveTracking: ToolbarLiveTrackingUiModel? = null

    fun setToolbarLiveTracking(toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel) {
        toolbarLiveTracking = toolbarLiveTrackingUiModel
    }

    fun setToolbarScrolling(
        binding: FragmentTokofoodOrderTrackingBinding?,
        isCompletedOrder: Boolean
    ) {
        if (!isCompletedOrder) {
            binding?.rvOrderTracking?.run {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val mLayoutManager = (layoutManager as? LinearLayoutManager)
                        val firstVisibleItem =
                            mLayoutManager?.findFirstVisibleItemPosition().orZero()

                        if (firstVisibleItem > Int.ZERO) {
                            binding.setCustomToolbarView()
                        } else {
                            binding.setNormalToolbarView()
                        }
                    }
                })
            }
        }
    }

    private fun FragmentTokofoodOrderTrackingBinding.setCustomToolbarView() {
        orderTrackingToolbar.run {
            val descToolbar =
                if (toolbarLiveTracking?.composeEstimation?.isNotEmpty() == true) {
                    context.getString(
                        R.string.order_tracking_toolbar_desc_with_eta,
                        toolbarLiveTracking?.orderStatusTitle.orEmpty(),
                        toolbarLiveTracking?.composeEstimation.orEmpty()
                    )
                } else {
                    toolbarLiveTracking?.orderStatusTitle.orEmpty()
                }
            setTitle(toolbarLiveTracking?.merchantName)
            subtitle = descToolbar
        }
    }

    private fun FragmentTokofoodOrderTrackingBinding.setNormalToolbarView() {
        orderTrackingToolbar.run {
            title = context.getString(R.string.title_tokofood_post_purchase)
            subtitle = ""
        }
    }
}