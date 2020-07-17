package com.tokopedia.review.feature.historydetails.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.historydetails.data.ProductrevGetReviewDetailProduct
import com.tokopedia.review.feature.historydetails.data.ProductrevGetReviewDetailResponse
import com.tokopedia.review.feature.historydetails.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.feature.historydetails.di.DaggerReviewDetailComponent
import com.tokopedia.review.feature.historydetails.di.ReviewDetailComponent
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.review.common.presentation.uimodel.ReviewProductUiModel
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickedListener
import com.tokopedia.review.feature.historydetails.analytics.ReviewDetailTracking
import com.tokopedia.review.feature.inbox.common.util.OnBackPressedListener
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.android.synthetic.main.partial_review_connection_error.view.*
import javax.inject.Inject

class ReviewDetailFragment : BaseDaggerFragment(), HasComponent<ReviewDetailComponent>, ReviewAttachedImagesClickedListener, OnBackPressedListener {

    companion object {
        const val KEY_FEEDBACK_ID = "feedbackID"

        fun createNewInstance(feedbackId: Int) : ReviewDetailFragment{
            return ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_FEEDBACK_ID, feedbackId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewDetailViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDataFromArguments()
        return inflater.inflate(R.layout.fragment_review_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initErrorPage()
        observeReviewDetails()
    }

    override fun getComponent(): ReviewDetailComponent? {
        return activity?.run {
            DaggerReviewDetailComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewDetails)
    }

    override fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int) {
        goToImagePreview(productName, attachedImages, position)
    }

    override fun onBackPressed() {
        (viewModel.reviewDetails.value as? Success)?.let {
            ReviewDetailTracking.eventClickBack(it.data.product.productId, it.data.review.feedbackId, viewModel.getUserId())
        }
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setFeedbackId(it.getInt(KEY_FEEDBACK_ID))
        }
    }

    private fun observeReviewDetails() {
        viewModel.reviewDetails.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    hideError()
                    hideLoading()
                    with(it.data) {
                        setProduct(product, review.feedbackId)
                        setReview(review, product.productName)
                        setResponse(response)
                    }
                }
                is Fail -> {
                    showError()
                    hideLoading()
                }
                is LoadingView -> {
                    showLoading()
                    hideError()
                }
            }
        })
    }

    private fun setProduct(product: ProductrevGetReviewDetailProduct, feedbackId: Int) {
        with(product) {
            reviewDetailProductCard.apply {
                setItem(ReviewProductUiModel(productId, productImageUrl, productName, productVariantName))
                setOnClickListener {
                    ReviewDetailTracking.eventClickProductCard(productId, feedbackId, viewModel.getUserId())
                    goToPdp(productId)
                }
            }
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview, productName: String) {
        with(review) {
            reviewDetailStars.apply {
                setImageResource(getReviewStar(rating))
                show()
            }
            if(sentAsAnonymous) {
                reviewDetailName.setTextAndCheckShow(getString(R.string.review_detail_anonymous))
            } else {
                reviewDetailName.setTextAndCheckShow(getString(R.string.review_detail_name, reviewerName))
            }
            reviewDetailDate.setTextAndCheckShow(getString(R.string.review_date, reviewTimeFormatted))
            reviewDetailContent.setTextAndCheckShow(reviewText)
            reviewDetailAttachedImages.setImages(attachments, productName, this@ReviewDetailFragment)
            if(editable) {
                addEditHeaderIcon()
            }
        }
    }

    private fun setResponse(response: ProductrevGetReviewDetailResponse) {
        reviewDetailResponse.setContent(response)
    }

    private fun initHeader() {
        reviewDetailHeader.addRightIcon(R.drawable.ic_share)
        reviewDetailHeader.rightIcons?.firstOrNull()?.setOnClickListener {
            goToSharing()
        }
    }

    private fun initErrorPage() {
        reviewDetailConnectionError.apply {
            reviewConnectionErrorRetryButton.setOnClickListener {
                retry()
            }
            reviewConnectionErrorRetryButton.setOnClickListener {
                goToSettings()
            }
        }
    }

    private fun addEditHeaderIcon() {
        reviewDetailHeader.addRightIcon(R.drawable.ic_pencil_edit)
    }

    private fun showError() {
        reviewDetailConnectionError.show()
    }

    private fun hideError() {
        reviewDetailConnectionError.hide()
    }

    private fun showLoading() {
        reviewDetailShimmer.show()
    }

    private fun hideLoading() {
        reviewDetailShimmer.hide()
    }

    private fun goToEditForm() {

    }

    private fun goToSettings() {
        RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
    }

    private fun goToSharing() {

    }

    private fun goToImagePreview(productName: String, attachedImages: List<String>, position: Int) {
        startActivity(context?.let { ImagePreviewSliderActivity.getCallingIntent(it, productName, attachedImages, attachedImages, position) })
    }

    private fun goToPdp(productId: Int) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId.toString())
    }

    private fun retry() {
        viewModel.retry()
    }
}