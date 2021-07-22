package com.tokopedia.review.feature.gallery.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.review.common.presentation.listener.ReviewReportBottomSheetListener
import com.tokopedia.review.common.presentation.widget.ReviewReportBottomSheet
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.gallery.analytics.ReviewGalleryTracking
import com.tokopedia.review.feature.gallery.presentation.activity.ReviewGalleryActivity
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryImagesAdapter
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryLayoutManager
import com.tokopedia.review.feature.gallery.presentation.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageListener
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageSwipeListener
import com.tokopedia.review.feature.gallery.presentation.listener.SnapPagerScrollListener
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import com.tokopedia.review.feature.gallery.presentation.widget.ReviewGalleryExpandedReviewBottomSheet
import com.tokopedia.review.feature.gallery.presentation.widget.ReviewGalleryReviewDetailWidget
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryFragment : BaseDaggerFragment(), HasComponent<ReviewGalleryComponent>, ReviewReportBottomSheetListener,
        ReviewGalleryImageSwipeListener, ReviewGalleryImageListener, OnBackPressedListener {

    companion object {
        const val REPORT_REVIEW_ACTIVITY_CODE = 200
        fun newInstance(cacheManagerId: String): ReviewGalleryFragment {
            return ReviewGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewGalleryActivity.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewGalleryViewModel

    private var closeButton: IconUnify? = null
    private var menuButton: IconUnify? = null
    private var imagesRecyclerView: RecyclerView? = null
    private var reviewDetail: ReviewGalleryReviewDetailWidget? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private val adapter by lazy {
        ReviewGalleryImagesAdapter(this)
    }
    private var expandedReviewBottomSheet: ReviewGalleryExpandedReviewBottomSheet? = null

    private var productReview: ProductReview = ProductReview()
    private var index: Int = 0
    private var shopId: String = ""
    private var productId: String = ""
    private var areComponentsHidden = false
    private var isLikeValueChange: Boolean = false

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ReviewGalleryComponent? {
        return activity?.run {
            DaggerReviewGalleryComponent.builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onImageSwiped(previousIndex: Int, index: Int) {
        if(index != RecyclerView.NO_POSITION) {
            ReviewGalleryTracking.trackSwipeImage(productReview.feedbackID, previousIndex, index, productReview.imageAttachments.size, productId)
            reviewDetail?.setPhotoCount(index + 1, productReview.imageAttachments.size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupCloseButton()
        setupThreeDots()
        setupRecyclerView()
        setupReviewDetail()
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
        (imagesRecyclerView?.layoutManager as? ReviewGalleryLayoutManager)?.setScrollEnabled(false)
    }

    override fun enableScroll() {
        (imagesRecyclerView?.layoutManager as? ReviewGalleryLayoutManager)?.setScrollEnabled(true)
    }

    override fun onImageLoadFailed(index: Int) {
        showErrorToaster(getString(R.string.review_reading_connection_error), getString(R.string.review_refresh)) {
            adapter.reloadImageAtIndex(index)
        }
    }

    override fun onBackPressed() {
        finishActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REPORT_REVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            showToaster(getString(R.string.review_reading_success_submit_report))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getDataFromArguments() {
        arguments?.getString(ReviewGalleryActivity.EXTRA_CACHE_MANAGER_ID)?.let {
            context?.let { context ->
                with(SaveInstanceCacheManager(context, it)) {
                    productReview = get(ReadReviewFragment.PRODUCT_REVIEW_KEY, ProductReview::class.java)
                            ?: ProductReview()
                    index = get(ReadReviewFragment.INDEX_KEY, Int::class.java) ?: 0
                    shopId = get(ReadReviewFragment.SHOP_ID_KEY, String::class.java) ?: ""
                    productId = get(ReadReviewFragment.PRODUCT_ID_KEY, String::class.java) ?: ""
                }
            }
        }
    }

    private fun bindViews(view: View) {
        closeButton = view.findViewById(R.id.review_gallery_close_button)
        menuButton = view.findViewById(R.id.review_gallery_menu_button)
        imagesRecyclerView = view.findViewById(R.id.review_gallery_recyclerview)
        reviewDetail = view.findViewById(R.id.review_gallery_review_detail)
        coordinatorLayout = view.findViewById(R.id.review_gallery_coordinator_layout)
    }

    private fun setupCloseButton() {
        closeButton?.setOnClickListener {
            finishActivity()
        }
    }

    private fun setupThreeDots() {
        menuButton?.setOnClickListener { activity?.supportFragmentManager?.let { ReviewReportBottomSheet.newInstance(productReview.feedbackID, shopId, this).show(it, ReviewReportBottomSheet.TAG) } }
    }

    private fun setupRecyclerView() {
        imagesRecyclerView?.apply {
            adapter = this@ReviewGalleryFragment.adapter
            layoutManager = ReviewGalleryLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        addPagerSnapHelperToRecyclerView()
        adapter.setData(productReview.imageAttachments.map { it.imageUrl })
        imagesRecyclerView?.scrollToPosition(index - 1)
    }

    private fun addPagerSnapHelperToRecyclerView() {
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(imagesRecyclerView)
        imagesRecyclerView?.addOnScrollListener(SnapPagerScrollListener(helper, this))
    }

    private fun setupReviewDetail() {
        with(productReview) {
            reviewDetail?.apply {
                setPhotoCount(index, imageAttachments.size)
                setRating(productRating)
                setReviewerName(user.fullName)
                setTimeStamp(reviewCreateTimestamp)
                setReviewMessage(message) { openExpandedReviewBottomSheet() }
                setLikeCount(likeDislike.totalLike)
                setLikeButtonClickListener {
                    ReviewGalleryTracking.trackOnLikeReviewClicked(productReview.feedbackID, isLiked(productReview.likeDislike.likeStatus), productId)
                    viewModel.toggleLikeReview(productReview.feedbackID, shopId, productId, productReview.likeDislike.likeStatus)
                }
                setLikeButtonImage(likeDislike.isLiked())
            }
            setThreeDotsVisibility(isReportable)
        }
    }

    private fun observeToggleLikeReviewResult() {
        viewModel.toggleLikeReview.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessLikeReview(it.data)
                is Fail -> onFailLikeReview()
            }
        })
    }

    private fun onSuccessLikeReview(toggleLikeReviewResponse: ToggleProductReviewLike) {
        with(toggleLikeReviewResponse) {
            updateLikeCount(totalLike)
            updateLikeButton(isLiked())
            updateLikeStatus(likeStatus)
            isLikeValueChange = true
        }
    }

    private fun onFailLikeReview() {
        // No Op
    }

    private fun setThreeDotsVisibility(isReportable: Boolean) {
        menuButton?.showWithCondition(isReportable)
    }

    private fun updateLikeButton(isLiked: Boolean) {
        reviewDetail?.setLikeButtonImage(isLiked)
    }

    private fun updateLikeCount(totalLike: Int) {
        reviewDetail?.setLikeCount(totalLike)
    }

    private fun updateLikeStatus(likeStatus: Int) {
        productReview.likeDislike = productReview.likeDislike.copy(likeStatus = likeStatus)
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId.toLongOrZero())
        startActivityForResult(intent, REPORT_REVIEW_ACTIVITY_CODE)
    }

    private fun hideComponentsButImage() {
        closeButton?.invisible()
        menuButton?.invisible()
        reviewDetail?.invisible()
        areComponentsHidden = true
    }

    private fun showComponents() {
        closeButton?.show()
        if (productReview.isReportable) {
            menuButton?.show()
        }
        reviewDetail?.show()
        areComponentsHidden = false
    }

    private fun openExpandedReviewBottomSheet() {
        ReviewGalleryTracking.trackOnSeeAllClicked(productReview.feedbackID, productId)
        if (expandedReviewBottomSheet == null) {
            with(productReview) {
                expandedReviewBottomSheet = ReviewGalleryExpandedReviewBottomSheet.createInstance(productRating, reviewCreateTimestamp, user.fullName, message)
                configBottomSheet()
            }
        }
        activity?.supportFragmentManager?.let { expandedReviewBottomSheet?.show(it, ReviewGalleryExpandedReviewBottomSheet.REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG) }
    }

    private fun configBottomSheet() {
        expandedReviewBottomSheet?.apply {
            showKnob = true
            showCloseIcon = false
            clearContentPadding = true
            isDragable = true
            isHideable = true
        }
    }

    private fun finishActivity() {
        activity?.apply {
            if (isLikeValueChange) setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun showErrorToaster(message: String, actionText: String = "", onClickListener: View.OnClickListener = View.OnClickListener {  }) {
        coordinatorLayout?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR, actionText, onClickListener).show()
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
}