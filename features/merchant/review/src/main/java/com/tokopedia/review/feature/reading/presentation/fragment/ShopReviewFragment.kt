package com.tokopedia.review.feature.reading.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.unifyprinciples.Typography

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

    private var ratingOnlyContainer: NestedScrollView? = null
    private var emptyStateTitle: Typography? = null
    private var emptyStateSubtitle: Typography? = null
    private var globalError: GlobalError? = null

    override fun onAttach(context: Context) {
        SplitCompat.install(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_read_review, container, false).apply {
            ratingOnlyContainer = findViewById(R.id.rating_only_container)
            emptyStateTitle = findViewById(R.id.read_review_empty_list_title)
            emptyStateSubtitle = findViewById(R.id.read_review_empty_list_subtitle)
            globalError = findViewById(R.id.read_review_network_error)
        }
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

    override fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        super.onSuccessGetRatingAndTopic(ratingAndTopics)
        val isRatingAndTopicsAvailable = !(ratingAndTopics.rating.totalRatingTextAndImage == 0L && ratingAndTopics.rating.totalRatingWithImage == 0L)
        val isRatingAndReviewAvailable = ratingAndTopics.rating.totalRating != 0L

        // show rating bar only
        ratingOnlyContainer?.showWithCondition(!isRatingAndTopicsAvailable)

        if (!isRatingAndReviewAvailable) {
            // show empty state with specific shop wording
            showFilteredEmpty()
        }
    }

    override fun onSuccessGetShopRatingAndTopic(shopRatingAndTopics: ProductrevGetShopRatingAndTopic) {
        super.onSuccessGetShopRatingAndTopic(shopRatingAndTopics)
        val isShopRatingAndTopicsAvailable = !(shopRatingAndTopics.rating.totalRatingTextAndImage == 0L && shopRatingAndTopics.rating.totalRatingWithImage == 0L)
        val isRatingAndReviewAvailable = shopRatingAndTopics.rating.totalRating != 0L

        // show rating bar only
        ratingOnlyContainer?.showWithCondition(!isShopRatingAndTopicsAvailable)

        if (!isRatingAndReviewAvailable) {
            // show empty state with specific shop wording
            showFilteredEmpty()
        }
    }

    override fun showFilteredEmpty() {
        super.showFilteredEmpty()
        emptyStateTitle?.text = getString(R.string.review_reading_empty_review_shop_page_title)
        emptyStateSubtitle?.text = getString(R.string.review_reading_empty_review_shop_page_subtitle)
        globalError?.gone()
    }

    override fun hasInitialSwipeRefresh(): Boolean = false
}