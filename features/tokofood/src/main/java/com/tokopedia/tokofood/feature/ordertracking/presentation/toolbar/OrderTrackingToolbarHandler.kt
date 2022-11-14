package com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
import java.lang.ref.WeakReference

class OrderTrackingToolbarHandler(
    val activity: WeakReference<Activity?>,
    private val binding: FragmentTokofoodOrderTrackingBinding?
) {

    init {
        (activity.get() as? AppCompatActivity)?.apply {
            supportActionBar?.hide()

            setSupportActionBar(binding?.orderTrackingToolbar)

            binding?.orderTrackingToolbar?.bringToFront()

            supportActionBar?.run {
                title = getString(com.tokopedia.tokofood.R.string.title_tokofood_post_purchase)
            }
        }
    }

    private var toolbarLiveTracking: ToolbarLiveTrackingUiModel? = null

    fun setToolbarLiveTracking(toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel) {
        toolbarLiveTracking = toolbarLiveTrackingUiModel
    }

    fun updateToolbarPoolBased(toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel) {
        this.toolbarLiveTracking = ToolbarLiveTrackingUiModel(
            this.toolbarLiveTracking?.merchantName.orEmpty(),
            toolbarLiveTrackingUiModel.orderStatusTitle,
            toolbarLiveTrackingUiModel.composeEstimation
        )
    }

    fun setToolbarScrolling(orderStatus: String) {
        if (orderStatus !in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)) {
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
        } else {
            binding?.rvOrderTracking?.clearOnScrollListeners()
        }
    }

    private fun FragmentTokofoodOrderTrackingBinding.setCustomToolbarView() {
        orderTrackingToolbar.run {
            val descToolbar =
                if (toolbarLiveTracking?.composeEstimation?.isNotBlank() == true) {
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
            title = context.getString(com.tokopedia.tokofood.R.string.title_tokofood_post_purchase)
            subtitle = ""
        }
    }
}
