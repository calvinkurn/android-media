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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ProductrevGetReviewDetailProduct
import com.tokopedia.review.common.data.ProductrevGetReviewDetailReputation
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponse
import com.tokopedia.review.common.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.FragmentReviewDetailBinding
import com.tokopedia.review.feature.historydetails.analytics.ReviewDetailTracking
import com.tokopedia.review.feature.historydetails.di.DaggerReviewDetailComponent
import com.tokopedia.review.feature.historydetails.di.ReviewDetailComponent
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

        fun createNewInstance(feedbackId: String): ReviewDetailFragment {
            return ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_FEEDBACK_ID, feedbackId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewDetailViewModel

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null
    private var reviewConnectionErrorRetryButton: UnifyButton? = null

    private var binding by autoClearedNullable<FragmentReviewDetailBinding>()

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
        binding?.reviewDetailScrollView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding?.reviewDetailScrollView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewPerformanceMonitoringListener? {
        return if (context is ReviewPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDataFromArguments()
        binding = FragmentReviewDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        bindViews()
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

    override fun onAttachedImagesClicked(
        productName: String,
        attachedImages: List<String>,
        position: Int
    ) {
        (viewModel.reviewDetails.value as? Success)?.let {
            ReviewDetailTracking.eventClickImageGallery(
                it.data.product.productId,
                it.data.review.feedbackId,
                viewModel.getUserId()
            )
        }
        goToImagePreview(productName, attachedImages.filter { it.isNotEmpty() }, position)
    }

    override fun onBackPressed() {
        if (::viewModel.isInitialized) {
            (viewModel.reviewDetails.value as? Success)?.let {
                ReviewDetailTracking.eventClickBack(
                    it.data.product.productId,
                    it.data.review.feedbackId,
                    viewModel.getUserId()
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EDIT_FORM_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessEditForm()
                }
            }
        }
    }

    override fun onReviewScoreClicked(score: Int): Boolean {
        (viewModel.reviewDetails.value as? Success)?.let {
            ReviewDetailTracking.eventClickSmiley(
                it.data.product.productId,
                it.data.review.feedbackId,
                viewModel.getUserId()
            )
            viewModel.submitReputation(it.data.reputation.reputationId, score)
        }
        return false
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setFeedbackId(it.getString(KEY_FEEDBACK_ID) ?: "")
        }
    }

    private fun observeReviewDetails() {
        viewModel.reviewDetails.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    if (it.isRefresh) {
                        hideError()
                        hideLoading()
                        with(it.data) {
                            setProduct(product, review.feedbackId)
                            setReview(review, product.productName)
                            setResponse(response)
                            setReputation(reputation, response.shopName)
                            setTicker(review.editable, review.editDisclaimer)
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
        viewModel.submitReputationResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    viewModel.getReviewDetails(viewModel.feedbackId, false)
                    onSuccessInsertReputation()
                }
                is Fail -> {
                    viewModel.getReviewDetails(viewModel.feedbackId, false)
                    onFailInsertReputation(
                        it.fail.message
                            ?: getString(R.string.review_history_details_toaster_modify_smiley_error_default_message)
                    )
                }
                is LoadingView -> {
                    binding?.reviewHistoryDetailReputation?.showLoading()
                }
            }
        })
    }

    private fun setProduct(product: ProductrevGetReviewDetailProduct, feedbackId: String) {
        with(product) {
            binding?.reviewDetailProductCard?.setOnClickListener {
                ReviewDetailTracking.eventClickProductCard(
                    productId,
                    feedbackId,
                    viewModel.getUserId()
                )
                goToPdp(productId)
            }
            binding?.reviewDetailProductImage?.loadImage(productImageUrl)
            binding?.reviewDetailProductName?.text = productName
            if (productVariantName.isNotBlank()) {
                binding?.reviewDetailProductVariant?.apply {
                    text = getString(R.string.review_pending_variant, productVariantName)
                    show()
                }
            }
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview, productName: String) {
        with(review) {
            binding?.reviewDetailStars?.apply {
                setImageResource(getReviewStar(rating))
                show()
            }
            binding?.reviewDetailName?.apply {
                context?.let {
                    text = if (sentAsAnonymous) {
                        HtmlLinkHelper(
                            it,
                            getString(R.string.review_history_details_anonymous)
                        ).spannedString
                    } else {
                        HtmlLinkHelper(
                            it,
                            getString(R.string.review_history_details_name, reviewerName)
                        ).spannedString
                    }
                    show()
                }
            }
            addHeaderIcons(editable)
            if (attachments.isNotEmpty()) {
                binding?.reviewDetailAttachedImages?.apply {
                    setImages(attachments, productName, this@ReviewDetailFragment)
                    show()
                }
            } else {
                binding?.reviewDetailAttachedImages?.hide()
            }
            binding?.reviewDetailDate?.setTextAndCheckShow(
                getString(
                    R.string.review_date,
                    reviewTimeFormatted
                )
            )
            if (reviewText.isEmpty()) {
                binding?.reviewDetailContent?.apply {
                    text = getString(R.string.no_reviews_yet)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_32
                        )
                    )
                    show()
                }
            } else {
                binding?.reviewDetailContent?.apply {
                    text = reviewText
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                        )
                    )
                    show()
                }
            }
            binding?.reviewDetailBadRatingReason?.showBadRatingReason(badRatingReasonFmt)
            binding?.reviewDetailBadRatingDisclaimerWidget?.setDisclaimer(ratingDisclaimer)
        }
    }

    private fun setResponse(response: ProductrevGetReviewDetailResponse) {
        if (response.responseText.isEmpty()) {
            binding?.reviewDetailResponse?.hide()
            return
        }
        binding?.reviewDetailResponse?.apply {
            setContent(response)
            show()
        }
    }

    private fun setReputation(reputation: ProductrevGetReviewDetailReputation, shopName: String) {
        with(reputation) {
            binding?.reviewHistoryDetailReputation?.apply {
                resetState()
                setReviewScoreClickListener(this@ReviewDetailFragment)
                when {
                    !isLocked && !editable && score == SCORE_MAX -> {
                        setFinalScore(score)
                        setShopName(shopName)
                        hideLoading()
                        show()
                    }
                    !editable && isLocked && score != SCORE_ZERO -> {
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

    private fun setTicker(isEditable: Boolean, editDisclaimer: String) {
        if (isEditable) {
            binding?.reviewDetailTicker?.run {
                setTextDescription(editDisclaimer)
                show()
            }
            binding?.reviewDetailTips?.gone()
        } else {
            binding?.reviewDetailTips?.run {
                description = editDisclaimer
                show()
            }
            binding?.reviewDetailTicker?.gone()
        }
    }

    private fun bindViews() {
        reviewConnectionErrorRetryButton =
            view?.findViewById(com.tokopedia.review.inbox.R.id.reviewConnectionErrorRetryButton)
    }

    private fun initHeader() {
        binding?.reviewDetailHeader?.apply {
            title = getString(R.string.review_history_details_toolbar)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initErrorPage() {
        reviewConnectionErrorRetryButton?.setOnClickListener {
            retry()
        }
    }

    private fun addHeaderIcons(editable: Boolean) {
        binding?.reviewDetailHeader?.apply {
            clearIcons()
            addRightIcon(R.drawable.ic_history_details_share)
            rightIcons?.firstOrNull()?.setOnClickListener {
                (viewModel.reviewDetails.value as? Success)?.let {
                    ReviewDetailTracking.eventClickShare(
                        it.data.product.productId,
                        it.data.review.feedbackId,
                        viewModel.getUserId()
                    )
                }
                goToSharing()
            }
            if (!editable) {
                return
            }
            addRightIcon(R.drawable.ic_edit_review_history_detail)
            rightIcons?.let {
                it[INDEX_OF_EDIT_BUTTON].setOnClickListener {
                    (viewModel.reviewDetails.value as? Success)?.let { success ->
                        ReviewDetailTracking.eventClickEdit(
                            success.data.product.productId,
                            success.data.review.feedbackId,
                            viewModel.getUserId()
                        )
                    }
                    goToEditForm()
                }
            }
        }
    }

    private fun showError() {
        binding?.reviewDetailConnectionError?.root?.show()
    }

    private fun hideError() {
        binding?.reviewDetailConnectionError?.root?.hide()
    }

    private fun showLoading() {
        binding?.reviewDetailShimmer?.root?.show()
    }

    private fun hideLoading() {
        binding?.reviewDetailShimmer?.root?.hide()
    }

    private fun goToEditForm() {
        with((viewModel.reviewDetails.value as Success).data) {
            val uri = UriUtil.buildUri(
                ApplinkConstInternalMarketplace.EDIT_REVIEW,
                reputation.reputationId,
                product.productId
            )
            val intent = RouteManager.getIntent(
                context, Uri.parse(uri).buildUpon()
                    .appendQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID, viewModel.feedbackId)
                    .build().toString()
            )
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
        startActivity(context?.let {
            ImagePreviewSliderActivity.getCallingIntent(
                it,
                productName,
                attachedImages,
                attachedImages,
                position
            )
        })
    }

    private fun goToPdp(productId: String) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
    }

    private fun retry() {
        viewModel.retry()
    }

    private fun getReviewLink(): String {
        return UriUtil.buildUri(
            ApplinkConst.PRODUCT_REPUTATION,
            (viewModel.reviewDetails.value as Success).data.product.productId
        )
    }

    private fun clearIcons() {
        binding?.reviewDetailHeader?.rightContentView?.removeAllViews()
    }

    private fun onSuccessEditForm() {
        retry()
        view?.let {
            Toaster.build(
                it,
                getString(R.string.review_history_detail_toaster_edit_success),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.review_oke)
            ).show()
        }
    }

    private fun onSuccessInsertReputation() {
        view?.let {
            Toaster.build(
                it,
                getString(R.string.review_history_details_toaster_modify_smiley_success),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.review_oke)
            ).show()
        }
    }

    private fun onFailInsertReputation(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.review_oke)
            ).show()
        }
    }
}