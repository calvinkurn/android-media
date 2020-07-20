package com.tokopedia.review.feature.historydetails.presentation.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.presentation.uimodel.ReviewProductUiModel
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickedListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.historydetails.analytics.ReviewDetailTracking
import com.tokopedia.review.feature.historydetails.di.DaggerReviewDetailComponent
import com.tokopedia.review.feature.historydetails.di.ReviewDetailComponent
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.android.synthetic.main.partial_review_connection_error.view.*
import javax.inject.Inject

class ReviewDetailFragment : BaseDaggerFragment(), HasComponent<ReviewDetailComponent>, ReviewAttachedImagesClickedListener, OnBackPressedListener {

    companion object {
        const val KEY_FEEDBACK_ID = "feedbackID"
        const val INDEX_OF_EDIT_BUTTON = 1
        const val EDIT_FORM_REQUEST_CODE = 420

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
    }

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
                        setReputation(reputation)
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
            reviewDetailName.apply {
                context?.let {
                    text = if (sentAsAnonymous) {
                        HtmlLinkHelper(it, getString(R.string.review_history_details_anonymous)).spannedString
                    } else {
                        HtmlLinkHelper(it, getString(R.string.review_history_details_name, reviewerName)).spannedString
                    }
                    show()
                }
            }
            addHeaderIcons(editable)
            reviewDetailAttachedImages.setImages(attachments, productName, this@ReviewDetailFragment)
            reviewDetailDate.setTextAndCheckShow(getString(R.string.review_date, reviewTimeFormatted))
            if(reviewText.isEmpty()) {
                reviewDetailContent.apply {
                    text = getString(R.string.no_reviews_yet)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_32))
                    show()
                }
            } else {
                reviewDetailContent.setTextAndCheckShow(reviewText)
            }
        }
    }

    private fun setResponse(response: ProductrevGetReviewDetailResponse) {
        if(response.responseText.isEmpty()) {
            reviewDetailResponse.hide()
            return
        }
        reviewDetailResponse.setContent(response)
    }

    private fun setReputation(reputation: ProductrevGetReviewDetailReputation) {
        with(reputation) {
            when {
                !editable && isLocked && score != 0  -> {
                    reviewHistoryDetailReputation.setFinalScore(score)
                }
                else -> {
                    // Later
                }
            }
        }
    }

    private fun initHeader() {
        reviewDetailHeader.apply {

            title = getString(R.string.review_history_details_toolbar)
        }
    }

    private fun initErrorPage() {
        reviewDetailConnectionError.apply {
            reviewConnectionErrorRetryButton.setOnClickListener {
                retry()
            }
        }
    }

    private fun addHeaderIcons(editable: Boolean) {
        reviewDetailHeader.apply {
            addRightIcon(R.drawable.ic_share)
            rightIcons?.firstOrNull()?.setOnClickListener {
                goToSharing()
            }
            if(!editable) {
                return
            }
            addRightIcon(R.drawable.ic_edit_review_history_detail)
            rightIcons?.let {
                it[INDEX_OF_EDIT_BUTTON].setOnClickListener {
                    goToEditForm()
                }
            }
        }
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
        with((viewModel.reviewDetails.value as Success).data) {
            val uri = UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputation.reputationId.toString(), product.productId.toString())
            val intent = RouteManager.getIntent(context, Uri.parse(uri).buildUpon()
                    .appendQueryParameter(ReviewConstants.PARAM_IS_EDIT_MODE, ReviewConstants.EDIT_MODE.toString())
                    .appendQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID, viewModel.feedbackId.toString()).build().toString())
            startActivityForResult(intent, EDIT_FORM_REQUEST_CODE)
        }
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