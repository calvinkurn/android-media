package com.tokopedia.review.feature.gallery.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.presentation.listener.ReviewReportBottomSheetListener
import com.tokopedia.review.common.presentation.widget.ReviewReportBottomSheet
import com.tokopedia.review.feature.gallery.presentation.activity.ReviewGalleryActivity
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryImagesAdapter
import com.tokopedia.review.feature.gallery.presentation.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageSwipeListener
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import com.tokopedia.review.feature.gallery.presentation.widget.ReviewGalleryReviewDetailWidget
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import javax.inject.Inject

class ReviewGalleryFragment : BaseDaggerFragment(), HasComponent<ReviewGalleryComponent>, ReviewReportBottomSheetListener, ReviewGalleryImageSwipeListener {

    companion object {
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
    private var imagesRecyclerVIew: RecyclerView? = null
    private var reviewDetail: ReviewGalleryReviewDetailWidget? = null
    private val adapter by lazy {
        ReviewGalleryImagesAdapter()
    }

    private var productReview: ProductReview = ProductReview()
    private var index: Int = 0
    private var shopId: String = ""
    private var productId: String = ""
    private var areComponentsHidden = false

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

    override fun onImageSwiped(index: Int) {
        reviewDetail?.setPhotoCount(index, productReview.imageAttachments.size)
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
        imagesRecyclerVIew = view.findViewById(R.id.review_gallery_recyclerview)
        reviewDetail = view.findViewById(R.id.review_gallery_review_detail)
    }

    private fun setupCloseButton() {
        closeButton?.setOnClickListener { activity?.finish() }
    }

    private fun setupThreeDots() {
        menuButton?.setOnClickListener { activity?.supportFragmentManager?.let { ReviewReportBottomSheet.newInstance(productReview.feedbackID, shopId, this).show(it, ReviewReportBottomSheet.TAG) } }
    }

    private fun setupRecyclerView() {
        imagesRecyclerVIew?.apply {
            adapter = this@ReviewGalleryFragment.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setOnClickListener {
                if (areComponentsHidden) {
                    showComponents()
                } else {
                    hideComponentsButImage()
                }
            }
        }
        adapter.setData(productReview.imageAttachments.map { it.imageUrl })
    }

    private fun setupReviewDetail() {
        with(productReview) {
            reviewDetail?.apply {
                setPhotoCount(index, imageAttachments.size)
                setRating(productRating)
                setReviewerName(user.fullName)
                setTimeStamp(reviewCreateTime)
                setReviewMessage(message)
                setLikeCount(likeDislike.totalLike)
                setLikeButtonClickListener {
                    viewModel.toggleLikeReview(productReview.feedbackID, shopId, productId, productReview.likeDislike.likeStatus)
                }
            }
        }
    }

    private fun observeToggleLikeReviewResult() {
        viewModel.toggleLikeReview.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId.toIntOrZero())
        startActivity(intent)
    }

    private fun hideComponentsButImage() {
        closeButton?.hide()
        menuButton?.hide()
        areComponentsHidden = true
    }

    private fun showComponents() {
        closeButton?.show()
        menuButton?.show()
        areComponentsHidden = false
    }
}