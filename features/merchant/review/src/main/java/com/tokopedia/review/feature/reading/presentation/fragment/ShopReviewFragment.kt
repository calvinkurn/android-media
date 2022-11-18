package com.tokopedia.review.feature.reading.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewRatingOnlyEmptyState
import com.tokopedia.shop.common.view.interfaces.ShopPageSharedListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
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
            goToTopFab = findViewById(R.id.read_review_go_to_top_fab)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showShopPageReviewHeader()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REPORT_REVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK -> {
                showToaster(getString(R.string.review_reading_success_submit_report))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        super.onSuccessGetRatingAndTopic(ratingAndTopics)
        if (ratingOnlyContainer?.isVisible.orFalse()) {
            nestedScrollViewContainer?.visible()
        } else {
            nestedScrollViewContainer?.gone()
        }
    }

    override fun onSuccessGetShopRatingAndTopic(shopRatingAndTopics: ProductrevGetShopRatingAndTopic) {
        super.onSuccessGetShopRatingAndTopic(shopRatingAndTopics)
        if (ratingOnlyContainer?.isVisible.orFalse()) {
            nestedScrollViewContainer?.visible()
        } else {
            nestedScrollViewContainer?.gone()
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

    override fun showFilteredEmpty() {
        super.showFilteredEmpty()
        emptyStateContainer?.apply {
            emptyStateImage?.setImageUrl(EMPTY_FILTERED_STATE_IMAGE_URL)
            show()
        }
        nestedScrollViewContainer?.visible()
        view?.let {
            getRecyclerView(it)?.apply {
                val newLayoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                layoutParams = newLayoutParams
            }
        }
    }

    override fun hideFilteredEmpty() {
        super.hideFilteredEmpty()
        nestedScrollViewContainer?.gone()
        view?.let {
            getRecyclerView(it)?.apply {
                val newLayoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                    height = Int.ZERO
                }
                layoutParams = newLayoutParams
            }
        }
    }

    override fun showError(throwable: Throwable) {
        super.showError(throwable)
        nestedScrollViewContainer?.visible()
    }

    override fun onFailGetProductReviews(throwable: Throwable) {
        logToCrashlytics(throwable)
        if (currentPage == Int.ZERO) {
            showError(throwable)
        } else {
            showErrorToaster(getString(R.string.review_reading_connection_error)) {
                loadData(currentPage)
            }
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean = false

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstCompletelyVisibleItemPosition = (
                        recyclerView.layoutManager as? LinearLayoutManager
                        )?.findFirstCompletelyVisibleItemPosition().orZero()
                val rvIsReachTop = currentScrollPosition == 0 || firstCompletelyVisibleItemPosition == 0
                val rvIsScrollingUp = dy < Int.ZERO
                currentScrollPosition += dy
                goToTopFab?.circleMainMenu?.showWithCondition(!rvIsReachTop && rvIsScrollingUp)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val canScrollVertically = recyclerView.canScrollVertically(RecyclerView.NO_POSITION)
                if (canScrollVertically && newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    reviewHeader?.getReviewRatingContainer()?.gone()
                } else if (!canScrollVertically){
                    reviewHeader?.getReviewRatingContainer()?.visible()
                }
            }
        }
    }

    private fun showToaster(message: String) {
        Toaster.build(
                requireView(),
                message,
                Toaster.toasterLength,
                Toaster.TYPE_NORMAL
        ).show()
    }

    private fun showErrorToaster(message: String, action: () -> Unit) {
        Toaster.build(
                requireView(),
                message,
                Toaster.LENGTH_INDEFINITE,
                Toaster.TYPE_ERROR,
                getString(R.string.review_refresh)
        ) { action.invoke() }.show()
    }

    override fun redirectToPDP(productId: String) {
        val pdpAppLink = getPdpAppLink(productId)
        context?.let {
            val intent = RouteManager.getIntent(
                context,
                pdpAppLink
            )
            startActivity(intent)
        }
    }

    private fun getPdpAppLink(productId: String): String {
        val basePdpAppLink = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        return createAffiliateLink(basePdpAppLink)
    }

    private fun createAffiliateLink(basePdpAppLink: String): String {
        return (activity as? ShopPageSharedListener)?.createPdpAffiliateLink(basePdpAppLink).orEmpty()
    }
}