package com.tokopedia.review.feature.historydetails.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.historydetails.analytics.ReviewDetailTracking
import com.tokopedia.review.feature.historydetails.di.DaggerReviewDetailComponent
import com.tokopedia.review.feature.historydetails.di.ReviewDetailComponent
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.android.synthetic.main.partial_review_connection_error.view.*
import javax.inject.Inject

class ReviewDetailFragment : BaseDaggerFragment(),
        HasComponent<ReviewDetailComponent>, ReviewAttachedImagesClickListener,
        OnBackPressedListener, ReviewScoreClickListener, ReviewPerformanceMonitoringContract {

    companion object {
        const val KEY_FEEDBACK_ID = "feedbackID"
        const val INDEX_OF_EDIT_BUTTON = 1
        const val EDIT_FORM_REQUEST_CODE = 420
        const val SHARE_TYPE_PLAIN_TEXT = "text/plain"

        const val SCORE_ZERO = 0
        const val SCORE_MAX = 2

        fun createNewInstance(feedbackId: Long) : ReviewDetailFragment{
            return ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_FEEDBACK_ID, feedbackId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewDetailViewModel

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

    override fun stopPreparePerfomancePageMonitoring() {
        reviewPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        reviewDetailScrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                reviewDetailScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewPerformanceMonitoringListener? {
        return if(context is ReviewPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initErrorPage()
        observeReviewDetails()
        observeInsertReputationResult()
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
        removeObservers(viewModel.submitReputationResult)
    }

    override fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int) {
        (viewModel.reviewDetails.value as? Success)?.let {
            ReviewDetailTracking.eventClickImageGallery(it.data.product.productId, it.data.review.feedbackId, viewModel.getUserId())
        }
        goToImagePreview(productName, attachedImages.filter { it.isNotEmpty() }, position)
    }

    override fun onBackPressed() {
        if(::viewModel.isInitialized) {
            (viewModel.reviewDetails.value as? Success)?.let {
                ReviewDetailTracking.eventClickBack(it.data.product.productId, it.data.review.feedbackId, viewModel.getUserId())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            EDIT_FORM_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    onSuccessEditForm()
                }
            }
        }
    }

    override fun onReviewScoreClicked(score: Int): Boolean {
        (viewModel.reviewDetails.value as? Success)?.let {
            ReviewDetailTracking.eventClickSmiley(it.data.product.productId, it.data.review.feedbackId, viewModel.getUserId())
            viewModel.submitReputation(it.data.reputation.reputationId, score)
        }
        return false
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setFeedbackId(it.getLong(KEY_FEEDBACK_ID))
        }
    }

    private fun observeReviewDetails() {
        viewModel.reviewDetails.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    if(it.isRefresh) {
                        hideError()
                        hideLoading()
                        with(it.data) {
                            setProduct(product, review.feedbackId)
                            setReview(review, product.productName)
                            setResponse(response)
                            setReputation(reputation, response.shopName)
                            setTicker(review.editable)
                        }
                    } else {
                        with(it.data) {
                            setReview(review, product.productName)
                            setReputation(reputation, response.shopName)
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

    private fun observeInsertReputationResult() {
        viewModel.submitReputationResult.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    viewModel.getReviewDetails(viewModel.feedbackId, false)
                    onSuccessInsertReputation()
                }
                is Fail -> {
                    viewModel.getReviewDetails(viewModel.feedbackId, false)
                    onFailInsertReputation(it.fail.message ?: getString(R.string.review_history_details_toaster_modify_smiley_error_default_message))
                }
                is LoadingView -> {
                    reviewHistoryDetailReputation.showLoading()
                }
            }
        })
    }

    private fun setProduct(product: ProductrevGetReviewDetailProduct, feedbackId: Long) {
        with(product) {
            reviewDetailProductCard.setOnClickListener {
                ReviewDetailTracking.eventClickProductCard(productId, feedbackId, viewModel.getUserId())
                goToPdp(productId)
            }
            reviewDetailProductImage.loadImage(productImageUrl)
            reviewDetailProductName.text = productName
            if(productVariantName.isNotBlank()) {
                reviewDetailProductVariant.apply {
                    text = getString(R.string.review_pending_variant, productVariantName)
                    show()
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
            if(attachments.isNotEmpty()) {
                reviewDetailAttachedImages.apply {
                    setImages(attachments, productName, this@ReviewDetailFragment)
                    show()
                }
            } else {
                reviewDetailAttachedImages.hide()
            }
            reviewDetailDate.setTextAndCheckShow(getString(R.string.review_date, reviewTimeFormatted))
            if(reviewText.isEmpty()) {
                reviewDetailContent.apply {
                    text = getString(R.string.no_reviews_yet)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                    show()
                }
            } else {
                reviewDetailContent.apply {
                    text = reviewText
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    show()
                }
            }
        }
    }

    private fun setResponse(response: ProductrevGetReviewDetailResponse) {
        if(response.responseText.isEmpty()) {
            reviewDetailResponse.hide()
            return
        }
        reviewDetailResponse.apply {
            setContent(response)
            show()
        }
    }

    private fun setReputation(reputation: ProductrevGetReviewDetailReputation, shopName: String) {
        with(reputation) {
            reviewHistoryDetailReputation.apply {
                resetState()
                setReviewScoreClickListener(this@ReviewDetailFragment)
                when {
                    !isLocked && !editable && score == SCORE_MAX -> {
                        setFinalScore(score)
                        setShopName(shopName)
                        hideLoading()
                        show()
                    }
                    !editable && isLocked && score != SCORE_ZERO  -> {
                        setFinalScore(score)
                        setShopName(shopName)
                        hideLoading()
                        show()
                    }
                    !isLocked -> {
                        setShopName(shopName)
                        setEditableScore(score)
                        hideLoading()
                        show()
                    }
                    isLocked && score == 0 -> {
                        setShopName(shopName)
                        setExpired()
                        hideLoading()
                        show()
                    }
                    else -> {
                        hide()
                    }
                }
            }
        }
    }

    private fun setTicker(isEditable: Boolean) {
        if(isEditable) {
            reviewDetailTicker.apply {
                tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerTitle = getString(R.string.review_history_details_ticker_editable_title)
                setTextDescription(getString(R.string.review_history_details_ticker_editable_subtitle))
            }
            return
        }
        reviewDetailTicker.apply {
            tickerType = Ticker.TYPE_INFORMATION
            tickerTitle = ""
            setTextDescription(getString(R.string.review_history_details_ticker_uneditable_subtitle))
        }
    }

    private fun initHeader() {
        reviewDetailHeader.apply {
            title = getString(R.string.review_history_details_toolbar)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
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
            clearIcons()
            addRightIcon(R.drawable.ic_share)
            rightIcons?.firstOrNull()?.setOnClickListener {
                (viewModel.reviewDetails.value as? Success)?.let {
                    ReviewDetailTracking.eventClickShare(it.data.product.productId, it.data.review.feedbackId, viewModel.getUserId())
                }
                goToSharing()
            }
            if(!editable) {
                return
            }
            addRightIcon(R.drawable.ic_edit_review_history_detail)
            rightIcons?.let {
                it[INDEX_OF_EDIT_BUTTON].setOnClickListener {
                    (viewModel.reviewDetails.value as? Success)?.let { success ->
                        ReviewDetailTracking.eventClickEdit(success.data.product.productId, success.data.review.feedbackId, viewModel.getUserId())
                    }
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
        val sendIntent = Intent()
        sendIntent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getReviewLink())
            type = SHARE_TYPE_PLAIN_TEXT
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun goToImagePreview(productName: String, attachedImages: List<String>, position: Int) {
        startActivity(context?.let { ImagePreviewSliderActivity.getCallingIntent(it, productName, attachedImages, attachedImages, position) })
    }

    private fun goToPdp(productId: Long) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId.toString())
    }

    private fun retry() {
        viewModel.retry()
    }

    private fun getReviewLink() : String {
        return UriUtil.buildUri(ApplinkConst.PRODUCT_REPUTATION, (viewModel.reviewDetails.value as Success).data.product.productId.toString())
    }

    private fun clearIcons() {
        reviewDetailHeader.rightContentView.removeAllViews()
    }

    private fun onSuccessEditForm() {
        retry()
        view?.let { Toaster.build(it, getString(R.string.review_history_detail_toaster_edit_success), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.review_oke)).show() }
    }

    private fun onSuccessInsertReputation() {
        view?.let { Toaster.build(it, getString(R.string.review_history_details_toaster_modify_smiley_success), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.review_oke)).show() }
    }

    private fun onFailInsertReputation(message: String) {
        view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.review_oke)).show() }
    }
}