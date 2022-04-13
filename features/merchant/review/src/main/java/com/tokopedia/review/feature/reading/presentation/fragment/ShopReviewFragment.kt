package com.tokopedia.review.feature.reading.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.review.common.util.ReviewConstants

/**
 * Created by @ilhamsuaib on 29/03/22.
 *
 * Inflated using on-demand DF Fragment in NewShopPageFragment.kt
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

    override fun onAttach(context: Context) {
        SplitCompat.install(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_read_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showShopPageReviewHeader()
        hideHeaderOnScrolled()
    }

    private fun hideHeaderOnScrolled() {
        view?.let {
            val recyclerView = getRecyclerView(it) ?: return

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val canScrollVertically = recyclerView.canScrollVertically(RecyclerView.NO_POSITION)
                    if (canScrollVertically && newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        reviewHeader?.hideRatingContainer()
                    } else if (!canScrollVertically){
                        reviewHeader?.showRatingContainer()
                    }
                }
            })
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean = false
}