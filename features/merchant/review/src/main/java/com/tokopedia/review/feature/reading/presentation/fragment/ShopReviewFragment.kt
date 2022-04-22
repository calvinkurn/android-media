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
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewRatingOnlyEmptyState
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by @ilhamsuaib on 29/03/22.
 *
 * Inflated using on-demand DF Fragment in NewShopPageFragment.kt using reflection
 * please be aware for make any changes
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

    private var nestedScrollViewContainer: NestedScrollView? = null
    private var ratingOnlyContainer: ReadReviewRatingOnlyEmptyState? = null
    private var emptyStateContainer: View? = null
    private var emptyStateImage: ImageUnify? = null
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
            nestedScrollViewContainer = findViewById(R.id.rating_only_container)
            ratingOnlyContainer = findViewById(R.id.read_review_rating_only)
            emptyStateContainer = findViewById(R.id.shop_read_review_list_empty)
            emptyStateImage = findViewById(R.id.read_review_empty_list_image)
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

    override fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        super.onSuccessGetRatingAndTopic(ratingAndTopics)
        if (ratingOnlyContainer?.isVisible.orFalse()) {
            nestedScrollViewContainer?.visible()
        }
    }

    override fun onSuccessGetShopRatingAndTopic(shopRatingAndTopics: ProductrevGetShopRatingAndTopic) {
        super.onSuccessGetShopRatingAndTopic(shopRatingAndTopics)
        if (ratingOnlyContainer?.isVisible.orFalse()) {
            nestedScrollViewContainer?.visible()
        }
    }

    override fun showPageNotFound() {
        super.showPageNotFound()
        globalError?.gone()
        nestedScrollViewContainer?.visible()
        emptyStateContainer?.apply {
            emptyStateImage?.setImageUrl(EMPTY_FILTERED_STATE_IMAGE_URL)
            emptyStateTitle?.text = getString(R.string.review_reading_empty_review_shop_page_title)
            emptyStateSubtitle?.text = getString(R.string.review_reading_empty_review_shop_page_subtitle)
            visible()
        }
    }

    override fun showError() {
        super.showError()
        nestedScrollViewContainer?.visible()
    }

    override fun hasInitialSwipeRefresh(): Boolean = false

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
}