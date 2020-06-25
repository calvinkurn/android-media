package com.tokopedia.review.feature.details.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
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
import com.tokopedia.review.feature.details.data.ProductrevGetReviewDetailProduct
import com.tokopedia.review.feature.details.data.ProductrevGetReviewDetailResponse
import com.tokopedia.review.feature.details.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.feature.details.di.DaggerReviewDetailComponent
import com.tokopedia.review.feature.details.di.ReviewDetailComponent
import com.tokopedia.review.feature.details.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.review.common.presentation.uimodel.ReviewProductUiModel
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.android.synthetic.main.partial_review_connection_error.view.*
import javax.inject.Inject

class ReviewDetailFragment : BaseDaggerFragment(), HasComponent<ReviewDetailComponent> {

    companion object {
        const val KEY_FEEDBACK_ID = "feedbackID"
        const val KEY_REPUTATION_ID = "reputationID"

        fun createNewInstance(feedbackId: Int, reputationId: Int) : ReviewDetailFragment{
            return ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_FEEDBACK_ID, feedbackId)
                    putInt(KEY_REPUTATION_ID, reputationId)
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
        getReviewData()
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

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setFeedbackAndReputationId(it.getInt(KEY_FEEDBACK_ID), it.getInt(KEY_REPUTATION_ID))
        }
    }

    private fun observeReviewDetails() {
        viewModel.reviewDetails.observe(this, Observer {
            when(it) {
                is Success -> {
                    hideError()
                    hideLoading()
                    with(it.data) {
                        setProduct(product)
                        setReview(review)
                        setResponse(response)
                        if(status.editable) {
                            addEditHeaderIcon()
                        }
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

    private fun setProduct(product: ProductrevGetReviewDetailProduct) {
        with(product) {
            reviewDetailProductCard.setItem(ReviewProductUiModel(productId, productImageUrl, productName, productVariantName))
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview) {
        with(review) {
            reviewDetailStars.apply {
                setImageResource(getReviewStar(rating))
                show()
            }
            if(reviewerData.isAnonym) {
                reviewDetailName.setTextAndCheckShow(getString(R.string.review_detail_anonymous))
            } else {
                reviewDetailName.setTextAndCheckShow(getString(R.string.review_detail_name, reviewerData.fullName))
            }
            reviewDetailDate.setTextAndCheckShow(getString(R.string.review_date, reviewTimeFormatted))
            reviewDetailContent.setTextAndCheckShow(reviewText)
            reviewDetailAttachedImages.setImages(attachments)
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
                getReviewData()
            }
            reviewConnectionErrorRetryButton.setOnClickListener {
                goToSettings()
            }
        }
    }

    private fun getReviewData() {
        viewModel.getReviewDetails()
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
}