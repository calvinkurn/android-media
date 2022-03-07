package com.tokopedia.review.feature.imagepreview.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.data.ProductrevLikeReview
import com.tokopedia.review.common.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.common.presentation.listener.ReviewReportBottomSheetListener
import com.tokopedia.review.common.presentation.widget.ReviewReportBottomSheet
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.credibility.presentation.activity.ReviewCredibilityActivity
import com.tokopedia.review.feature.gallery.data.Detail
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGalleryFragment
import com.tokopedia.review.feature.gallery.presentation.uimodel.ReviewGalleryRoutingUiModel
import com.tokopedia.review.feature.imagepreview.analytics.ReviewImagePreviewTracking
import com.tokopedia.review.feature.imagepreview.presentation.activity.ReviewImagePreviewActivity
import com.tokopedia.review.feature.imagepreview.presentation.adapter.ReviewImagePreviewAdapter
import com.tokopedia.review.feature.imagepreview.presentation.adapter.ReviewImagePreviewLayoutManager
import com.tokopedia.review.feature.imagepreview.presentation.di.DaggerReviewImagePreviewComponent
import com.tokopedia.review.feature.imagepreview.presentation.di.ReviewImagePreviewComponent
import com.tokopedia.review.feature.imagepreview.presentation.listener.*
import com.tokopedia.review.feature.imagepreview.presentation.uimodel.ReviewImagePreviewBottomSheetUiModel
import com.tokopedia.review.feature.imagepreview.presentation.uimodel.ReviewImagePreviewUiModel
import com.tokopedia.review.feature.imagepreview.presentation.viewmodel.ReviewImagePreviewViewModel
import com.tokopedia.review.feature.imagepreview.presentation.widget.ReviewImagePreviewDetailWidget
import com.tokopedia.review.feature.imagepreview.presentation.widget.ReviewImagePreviewExpandedReviewBottomSheet
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.UserReviewStats
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewImagePreviewFragment : BaseDaggerFragment(), HasComponent<ReviewImagePreviewComponent>,
    ReviewReportBottomSheetListener, ReviewBasicInfoListener,
    ReviewImagePreviewSwipeListener, ReviewImagePreviewListener,
    OnBackPressedListener, ReviewImagePreviewLoadMoreListener {

    companion object {
        const val REPORT_REVIEW_ACTIVITY_CODE = 200
        private const val POSITION_INDEX_COUNTER = 1
        const val GALLERY_SOURCE_CREDIBILITY_SOURCE = "gallery"
        const val READING_IMAGE_PREVIEW_CREDIBILITY_SOURCE = "reading image preview"
        const val KEY_CACHE_ID = "cacheId"
        const val KEY_FINAL_LIKE_COUNT = "final like count"
        fun newInstance(
            cacheManagerId: String,
            isFromGallery: Boolean
        ): ReviewImagePreviewFragment {
            return ReviewImagePreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewImagePreviewActivity.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                    putBoolean(ReviewImagePreviewActivity.EXTRA_IS_FROM_GALLERY, isFromGallery)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewImagePreviewViewModel

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var closeButton: IconUnify? = null
    private var menuButton: IconUnify? = null
    private var imagesRecyclerView: RecyclerView? = null
    private var reviewImagePreviewDetail: ReviewImagePreviewDetailWidget? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private val adapter by lazy {
        ReviewImagePreviewAdapter(this)
    }
    private var expandedReviewBottomSheet: ReviewImagePreviewExpandedReviewBottomSheet? = null

    private var productReview: ProductReview = ProductReview()
    private var index: Int = 0
    private var shopId: String = ""
    private var productId: String = ""
    private var areComponentsHidden = false
    private var isLikeValueChange: Boolean = false
    private var galleryRoutingData: ReviewGalleryRoutingUiModel = ReviewGalleryRoutingUiModel()
    private var isFromGallery = false
    private var isProductReview: Boolean = true

    private var currentRecyclerViewPosition = 0
    private var currentPage = 0
    private var endlessScrollListener: EndlessScrollListener? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onLoadMore() {
        currentPage++
        viewModel.setPage(currentPage)
    }

    override fun getComponent(): ReviewImagePreviewComponent? {
        return activity?.run {
            DaggerReviewImagePreviewComponent.builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .build()
        }
    }

    override fun onImageSwiped(previousIndex: Int, index: Int) {
        if (index != RecyclerView.NO_POSITION) {
            trackOnSwipeImage(index, previousIndex)
            adjustPhotoCount(index)
            currentRecyclerViewPosition = index + 1
            updateReviewDetailByPosition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.tokopedia.review.R.layout.fragment_review_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupCloseButton()
        setupThreeDots()
        setupRecyclerView()
        if (isFromGallery) {
            viewModel.setProductId(galleryRoutingData.productId)
            setupReviewDetailFromGallery()
            setAdapterDataFromGallery()
            setupEndlessScrollListener()
            observeReviewImagesResult()
        } else {
            setAdapterDataFromReading()
            setupReviewDetail()
        }
        observeToggleLikeReviewResult()
    }

    override fun onReportOptionClicked(reviewId: String, shopId: String) {
        goToReportReview(reviewId, shopId)
    }

    override fun onImageClicked() {
        if (areComponentsHidden) {
            showComponents()
        } else {
            hideComponentsButImage()
        }
    }

    override fun disableScroll() {
        (imagesRecyclerView?.layoutManager as? ReviewImagePreviewLayoutManager)?.setScrollEnabled(
            false
        )
    }

    override fun enableScroll() {
        (imagesRecyclerView?.layoutManager as? ReviewImagePreviewLayoutManager)?.setScrollEnabled(
            true
        )
    }

    override fun onImageLoadFailed(index: Int) {
        showErrorToaster(
            getString(R.string.review_reading_connection_error),
            getString(R.string.review_refresh)
        ) {
            adapter.reloadImageAtIndex(index)
        }
    }

    override fun onImageImpressed() {
        if (isFromGallery) {
            galleryRoutingData.getSelectedReview(currentRecyclerViewPosition)?.let {
                ReviewImagePreviewTracking.trackImpressImage(
                    galleryRoutingData.totalImageCount,
                    galleryRoutingData.productId,
                    it.attachmentId,
                    it.imageNumber - POSITION_INDEX_COUNTER,
                    viewModel.getUserId(),
                    trackingQueue
                )
            }
        }
    }

    override fun onBackPressed() {
        finishActivity()
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REPORT_REVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            showToaster(getString(R.string.review_reading_success_submit_report))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String) {
        ReviewImagePreviewTracking.trackClickReviewerName(
            isFromGallery, feedbackId, userId, statistics,
            if (isFromGallery) viewModel.getProductId() else productId, viewModel.getUserId()
        )
    }

    override fun onUserNameClicked(userId: String) {
        goToReviewCredibility(userId)
    }

    private fun getDataFromArguments() {
        arguments?.let {
            val cacheManagerId =
                it.getString(ReviewImagePreviewActivity.EXTRA_CACHE_MANAGER_ID) ?: ""
            isFromGallery = it.getBoolean(ReviewImagePreviewActivity.EXTRA_IS_FROM_GALLERY)
            if (isFromGallery) {
                setImagesDataFromCacheManager(cacheManagerId)
            } else {
                setProductReviewDataFromCacheManager(cacheManagerId)
            }
        }
    }

    private fun setImagesDataFromCacheManager(cacheManagerId: String) {
        context?.let { context ->
            with(SaveInstanceCacheManager(context, cacheManagerId)) {
                galleryRoutingData = get(
                    ReviewGalleryFragment.KEY_REVIEW_GALLERY_ROUTING_DATA,
                    ReviewGalleryRoutingUiModel::class.java
                ) ?: ReviewGalleryRoutingUiModel()
            }
        }
    }


    private fun setProductReviewDataFromCacheManager(cacheManagerId: String) {
        context?.let { context ->
            with(SaveInstanceCacheManager(context, cacheManagerId)) {
                productReview = get(
                    ReadReviewFragment.PRODUCT_REVIEW_KEY,
                    ProductReview::class.java
                ) ?: ProductReview()
                index = get(ReadReviewFragment.INDEX_KEY, Int::class.java) ?: 0
                shopId = get(ReadReviewFragment.SHOP_ID_KEY, String::class.java) ?: ""
                productId = get(ReadReviewFragment.PRODUCT_ID_KEY, String::class.java) ?: ""
                isProductReview =
                    get(ReadReviewFragment.IS_PRODUCT_REVIEW_KEY, Boolean::class.java) ?: true
            }
        }
    }

    private fun bindViews(view: View) {
        closeButton = view.findViewById(R.id.review_image_preview_close_button)
        menuButton = view.findViewById(R.id.review_image_preview_menu_button)
        imagesRecyclerView = view.findViewById(R.id.review_image_preview_recyclerview)
        reviewImagePreviewDetail = view.findViewById(R.id.review_image_preview_review_detail)
        coordinatorLayout = view.findViewById(R.id.review_gallery_coordinator_layout)
    }

    private fun setupCloseButton() {
        closeButton?.setOnClickListener {
            finishActivity()
        }
    }

    private fun setupThreeDots() {
        menuButton?.setOnClickListener {
            activity?.supportFragmentManager?.let {
                if (isFromGallery) {
                    ReviewReportBottomSheet.newInstance(
                        galleryRoutingData.getSelectedReview(currentRecyclerViewPosition)?.feedbackId
                            ?: "",
                        galleryRoutingData.shopId,
                        this
                    ).show(it, ReviewReportBottomSheet.TAG)
                } else {
                    ReviewReportBottomSheet.newInstance(
                        productReview.feedbackID,
                        shopId,
                        this
                    ).show(it, ReviewReportBottomSheet.TAG)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        imagesRecyclerView?.apply {
            adapter = this@ReviewImagePreviewFragment.adapter
            layoutManager = ReviewImagePreviewLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        addPagerSnapHelperToRecyclerView()
    }

    private fun setAdapterDataFromReading() {
        adapter.addData(productReview.imageAttachments.map { ReviewImagePreviewUiModel(it.imageUrl) })
        setRecyclerViewCurrentItem()
    }

    private fun setAdapterDataFromGallery() {
        adapter.addData(galleryRoutingData.loadedReviews.map { ReviewImagePreviewUiModel(it.fullImageUrl) })
        setRecyclerViewCurrentItem(galleryRoutingData.currentPosition)
    }

    private fun setupEndlessScrollListener() {
        (imagesRecyclerView?.layoutManager as? ReviewImagePreviewLayoutManager)?.let {
            endlessScrollListener = EndlessScrollListener(this, it)
            endlessScrollListener?.let { imagesRecyclerView?.addOnScrollListener(it) }
        }
    }

    private fun setRecyclerViewCurrentItem(position: Int = index) {
        if (position != 0) {
            imagesRecyclerView?.scrollToPosition(position - 1)
        }
    }

    private fun addPagerSnapHelperToRecyclerView() {
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(imagesRecyclerView)
        imagesRecyclerView?.addOnScrollListener(SnapPagerScrollListener(helper, this))
    }

    private fun setupReviewDetailFromGallery() {
        currentPage = galleryRoutingData.page
        updateReviewDetailByPosition(true)
    }

    private fun setupReviewDetail() {
        with(productReview) {
            reviewImagePreviewDetail?.apply {
                setCredibilityData(isProductReview, isAnonymous, user.userID, feedbackID)
                setBasicInfoListener(this@ReviewImagePreviewFragment)
                setPhotoCount(index, imageAttachments.size.toLong())
                setRating(productRating)
                setReviewerName(user.fullName)
                setTimeStamp(reviewCreateTimestamp)
                setReviewerImage(user.image)
                setReviewMessage(message) { openExpandedReviewBottomSheet() }
                setLikeCount(likeDislike.totalLike.toString())
                setLikeButtonClickListener {
                    if (isProductReview) {
                        ReviewImagePreviewTracking.trackOnLikeReviewClicked(
                            productReview.feedbackID,
                            isLiked(productReview.likeDislike.likeStatus),
                            productId,
                            isFromGallery
                        )
                    } else {
                        ReviewImagePreviewTracking.trackOnShopReviewLikeReviewClicked(
                            productReview.feedbackID,
                            isLiked(productReview.likeDislike.likeStatus),
                            shopId
                        )
                    }
                    viewModel.toggleLikeReview(
                        productReview.feedbackID,
                        productReview.likeDislike.likeStatus
                    )
                }
                setLikeButtonImage(likeDislike.isLiked())
                setVariantName(variantName)
                setStats(userReviewStats)
                setBadRatingReason(badRatingReasonFmt)
            }
            setThreeDotsVisibility(isReportable && !areComponentsHidden)
        }
    }

    private fun observeToggleLikeReviewResult() {
        viewModel.toggleLikeReviewReview.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessLikeReview(it.data)
                is Fail -> onFailLikeReview(it.throwable)
            }
        })
    }

    private fun observeReviewImagesResult() {
        viewModel.reviewImages.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetReviewImages(it.data)
                is Fail -> onFailGetReviewImages(it.throwable)
            }
        })
    }

    private fun onSuccessLikeReview(toggleLikeReviewResponseReview: ProductrevLikeReview) {
        with(toggleLikeReviewResponseReview) {
            updateLikeCount(feedbackId, totalLike)
            updateLikeButton(isLiked())
            updateLikeStatus(feedbackId, likeStatus)
            isLikeValueChange = true
        }
    }

    private fun onFailLikeReview(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessGetReviewImages(productrevGetReviewImage: ProductrevGetReviewImage) {
        adapter.addData(
            productrevGetReviewImage.reviewImages.map { reviewImage ->
                ReviewImagePreviewUiModel(
                    productrevGetReviewImage.detail.reviewGalleryImages.firstOrNull { it.attachmentId == reviewImage.imageId }?.fullsizeURL
                        ?: ""
                )
            }
        )
        galleryRoutingData.loadedReviews.addAll(mapToUiModel(productrevGetReviewImage))
        updateHasNextPage(productrevGetReviewImage.hasNext)
    }

    private fun onFailGetReviewImages(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun setThreeDotsVisibility(isReportable: Boolean) {
        menuButton?.showWithCondition(isReportable)
    }

    private fun updateLikeButton(isLiked: Boolean) {
        reviewImagePreviewDetail?.setLikeButtonImage(isLiked)
    }

    private fun updateLikeCount(reviewId: String, totalLike: Int) {
        reviewImagePreviewDetail?.setLikeCount(totalLike.toString())
        if (isFromGallery) {
            galleryRoutingData.loadedReviews.forEach {
                if (it.feedbackId == reviewId) {
                    it.totalLiked = totalLike
                }
            }
            return
        }
        productReview.likeDislike = productReview.likeDislike.copy(totalLike = totalLike)
    }

    private fun updateLikeStatus(reviewId: String, likeStatus: Int) {
        if (isFromGallery) {
            galleryRoutingData.loadedReviews.forEach {
                if (it.feedbackId == reviewId) {
                    it.isLiked = isLiked(likeStatus)
                }
            }
            return
        }
        productReview.likeDislike = productReview.likeDislike.copy(likeStatus = likeStatus)
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId)
        startActivityForResult(intent, REPORT_REVIEW_ACTIVITY_CODE)
    }

    private fun hideComponentsButImage() {
        closeButton?.invisible()
        menuButton?.invisible()
        reviewImagePreviewDetail?.invisible()
        areComponentsHidden = true
    }

    private fun showComponents() {
        closeButton?.show()
        if (productReview.isReportable || galleryRoutingData.getSelectedReview()?.isReportable == true) {
            menuButton?.show()
        }
        reviewImagePreviewDetail?.show()
        areComponentsHidden = false
    }

    private fun openExpandedReviewBottomSheet() {
        if (isProductReview) {
            ReviewImagePreviewTracking.trackOnSeeAllClicked(
                if (isFromGallery) galleryRoutingData.getSelectedReview()?.feedbackId
                    ?: "" else productReview.feedbackID,
                galleryRoutingData.productId,
                isFromGallery
            )
        } else {
            ReviewImagePreviewTracking.trackOnShopReviewSeeAllClicked(
                productReview.feedbackID,
                productId
            )
        }
        if (isFromGallery) {
            galleryRoutingData.getSelectedReview(currentRecyclerViewPosition)?.let {
                expandedReviewBottomSheet =
                    ReviewImagePreviewExpandedReviewBottomSheet.createInstance(
                        getBottomSheetUiModel(
                            it.rating,
                            it.reviewTime,
                            it.reviewerName,
                            it.review,
                            it.variantName,
                            it.userStats,
                            it.userId,
                            it.isAnonymous,
                            isProductReview,
                            it.feedbackId,
                            viewModel.getProductId(),
                            isFromGallery,
                            viewModel.getUserId(),
                            it.userImage,
                            it.badRatingReason
                        )
                    )
            }
        } else {
            with(productReview) {
                expandedReviewBottomSheet =
                    ReviewImagePreviewExpandedReviewBottomSheet.createInstance(
                        getBottomSheetUiModel(
                            productRating,
                            reviewCreateTimestamp,
                            user.fullName,
                            message,
                            variantName,
                            userReviewStats,
                            user.userID,
                            isAnonymous,
                            isProductReview,
                            feedbackID,
                            productId,
                            isFromGallery,
                            viewModel.getUserId(),
                            user.image,
                            badRatingReasonFmt
                        )
                    )
            }
        }
        configBottomSheet()
        activity?.supportFragmentManager?.let {
            expandedReviewBottomSheet?.show(
                it,
                ReviewImagePreviewExpandedReviewBottomSheet.REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG
            )
        }
    }

    private fun configBottomSheet() {
        expandedReviewBottomSheet?.apply {
            showKnob = true
            showCloseIcon = false
            clearContentPadding = true
            isDragable = true
            isHideable = true
            isFullpage = true
        }
    }

    private fun finishActivity() {
        activity?.apply {
            if (isLikeValueChange) activity?.setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun showErrorToaster(
        message: String,
        actionText: String = "",
        onClickListener: View.OnClickListener = View.OnClickListener { }
    ) {
        coordinatorLayout?.let {
            Toaster.build(
                it,
                message,
                Toaster.toasterLength,
                Toaster.TYPE_ERROR,
                actionText,
                onClickListener
            ).show()
        }
    }

    private fun showToaster(message: String) {
        coordinatorLayout?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun isLiked(likeStatus: Int): Boolean {
        return likeStatus == LikeDislike.LIKED
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

    private fun updateReviewDetailByPosition(isFirstTimeUpdate: Boolean = false) {
        with(galleryRoutingData) {
            if (isFirstTimeUpdate) currentRecyclerViewPosition = currentPosition
            this.loadedReviews.firstOrNull {
                it.imageNumber == currentRecyclerViewPosition
            }?.let { selectedReview ->
                with(selectedReview) {
                    reviewImagePreviewDetail?.apply {
                        if (isFirstTimeUpdate) {
                            setPhotoCount(currentPosition, totalImageCount)
                        }
                        setCredibilityData(isProductReview, isAnonymous, userId, feedbackId)
                        setBasicInfoListener(this@ReviewImagePreviewFragment)
                        setRating(rating)
                        setReviewerName(reviewerName)
                        setTimeStamp(reviewTime)
                        setReviewMessage(review) { openExpandedReviewBottomSheet() }
                        setStats(userStats)
                        setBadRatingReason(badRatingReason)
                        setReviewerImage(userImage)
                        setLikeCount(totalLiked.toString())
                        setLikeButtonClickListener {
                            ReviewImagePreviewTracking.trackOnLikeReviewClicked(
                                feedbackId,
                                isLiked,
                                viewModel.getProductId(),
                                isFromGallery
                            )
                            viewModel.toggleLikeReview(
                                feedbackId,
                                mapToLikeStatus(selectedReview.isLiked)
                            )
                        }
                        setLikeButtonImage(isLiked)
                        setVariantName(variantName)
                    }
                    setThreeDotsVisibility(isReportable && !areComponentsHidden)
                }
            }
        }
    }

    private fun trackOnSwipeImage(index: Int, previousIndex: Int) {
        if (isProductReview) {
            ReviewImagePreviewTracking.trackSwipeImage(
                if (isFromGallery) productReview.feedbackID else galleryRoutingData.getSelectedReview(
                    index
                )?.feedbackId ?: "",
                previousIndex,
                index,
                productReview.imageAttachments.size,
                productId
            )
            return
        }
        ReviewImagePreviewTracking.trackShopReviewSwipeImage(
            productReview.feedbackID,
            previousIndex,
            index,
            productReview.imageAttachments.size,
            shopId
        )
    }

    private fun adjustPhotoCount(index: Int) {
        reviewImagePreviewDetail?.setPhotoCount(
            index + 1,
            if (isFromGallery) galleryRoutingData.totalImageCount else productReview.imageAttachments.size.toLong()
        )
    }

    private fun updateHasNextPage(hasNextPage: Boolean) {
        endlessScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun mapToLikeStatus(isLiked: Boolean): Int {
        return if (isLiked) LikeDislike.LIKED else 0
    }

    private fun mapToUiModel(productrevGetReviewImage: ProductrevGetReviewImage): List<ReviewGalleryUiModel> {
        val reviewImages = mutableListOf<ReviewGalleryUiModel>()
        productrevGetReviewImage.reviewImages.forEachIndexed { index, reviewImage ->
            reviewImages.add(
                getReviewGalleryUiModelBasedOnDetail(
                    productrevGetReviewImage.detail,
                    reviewImage.feedbackId,
                    reviewImage.imageId,
                    reviewImage.imageNumber
                )
            )
        }
        return reviewImages
    }

    private fun getReviewGalleryUiModelBasedOnDetail(
        detail: Detail,
        feedbackId: String,
        attachmentId: String,
        imageNumber: Int
    ): ReviewGalleryUiModel {
        var reviewGalleryUiModel = ReviewGalleryUiModel()
        detail.reviewDetail.firstOrNull { it.feedbackId == feedbackId }?.apply {
            reviewGalleryUiModel = reviewGalleryUiModel.copy(
                rating = this.rating,
                variantName = this.variantName,
                reviewerName = this.user.fullName,
                isLiked = this.isLiked,
                totalLiked = this.totalLike,
                review = this.review,
                reviewTime = this.createTimestamp,
                isReportable = this.isReportable,
                userStats = this.userStats,
                isAnonymous = this.isAnonymous,
                userId = this.user.userId,
                userImage = this.user.image
            )
        }
        detail.reviewGalleryImages.firstOrNull { it.attachmentId == attachmentId }?.apply {
            reviewGalleryUiModel = reviewGalleryUiModel.copy(
                imageUrl = this.thumbnailURL,
                fullImageUrl = this.fullsizeURL,
                attachmentId = this.attachmentId
            )
        }
        reviewGalleryUiModel = reviewGalleryUiModel.copy(
            feedbackId = feedbackId,
            imageNumber = imageNumber
        )
        return reviewGalleryUiModel
    }

    private fun goToReviewCredibility(userId: String) {
        RouteManager.route(
            context,
            Uri.parse(
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                    userId,
                    getCredibilitySource()
                )
            ).buildUpon()
                .appendQueryParameter(
                    ReviewCredibilityActivity.PARAM_PRODUCT_ID,
                    if (isFromGallery) viewModel.getProductId() else productId
                ).build()
                .toString()
        )
    }

    private fun getBottomSheetUiModel(
        rating: Int, timeStamp: String, reviewerName: String,
        reviewMessage: String, variantName: String,
        userStats: List<UserReviewStats>, userId: String,
        isAnonymous: Boolean = false, isProductReview: Boolean = false, feedbackId: String = "",
        productId: String, isFromGallery: Boolean, currentUserId: String,
        reviewerImage: String, badRatingReason: String
    ): ReviewImagePreviewBottomSheetUiModel {
        return ReviewImagePreviewBottomSheetUiModel(
            rating,
            timeStamp,
            reviewerName,
            reviewMessage,
            variantName,
            userStats,
            userId,
            isAnonymous,
            isProductReview,
            feedbackId,
            productId,
            isFromGallery,
            currentUserId,
            reviewerImage,
            getCredibilitySource(),
            badRatingReason
        )
    }

    private fun getCredibilitySource(): String {
        return if (isFromGallery) GALLERY_SOURCE_CREDIBILITY_SOURCE else READING_IMAGE_PREVIEW_CREDIBILITY_SOURCE
    }
}