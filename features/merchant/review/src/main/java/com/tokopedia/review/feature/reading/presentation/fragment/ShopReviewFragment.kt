package com.tokopedia.review.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.common.util.ReviewConstants

/**
 * Created by @ilhamsuaib on 29/03/22.
 */

class ShopReviewFragment : ReadReviewFragment() {

    companion object {

        fun createNewInstance(
            productId: String = "",
            shopId: String = ""
        ): ShopReviewFragment {
            return ShopReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                    putString(ReviewConstants.ARGS_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disablePullToRefresh()
        showShopPageReviewHeader()
    }

    private fun disablePullToRefresh() {
        view?.let {
            val recyclerView = getRecyclerView(it) ?: return
            recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    swipeToRefresh.isEnabled = e.action == MotionEvent.ACTION_UP
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })
        }
    }
}