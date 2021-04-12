package com.tokopedia.tkpd.tkpdreputation.review.product.view

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterView
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.*
import com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel.ReviewProductViewModel
import com.tokopedia.tkpd.tkpdreputation.review.product.view.widget.RatingBarReview
import com.tokopedia.tkpd.tkpdreputation.review.product.view.widget.ReviewProductItemFilterView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class ReviewProductFragment : BaseListFragment<ReviewProductModel, ReviewProductTypeFactoryAdapter>(), ReviewProductContentViewHolder.ListenerReviewHolder {

    companion object {

        private const val RATING_5 = 5
        private const val RATING_4 = 4
        private const val RATING_3 = 3
        private const val RATING_2 = 2
        private const val RATING_1 = 1

        private const val INITIAL_PAGE = 1
        private const val TOTAL_FILTER_ITEM = 5
        const val EXTRA_PRODUCT_ID = "product_id"
        private const val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"
        private const val EXTRA_DEFAULT_POSITION = "EXTRA_DEFAULT_POSITION"

        fun getInstance(productId: String): ReviewProductFragment {
            val reviewProductFragment = ReviewProductFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            reviewProductFragment.arguments = bundle
            return reviewProductFragment
        }
    }

    @Inject
    lateinit var reputationTracking: ReputationTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ReviewProductViewModel

    private lateinit var productId: String

    private var reviewHelpfulList: List<ReviewProductModel>? = null

    private var ratingProduct: TextView? = null
    private var ratingProductStar: RatingBar? = null
    private var counterReview: TextView? = null
    private var fiveStarReview: RatingBarReview? = null
    private var fourStarReview: RatingBarReview? = null
    private var threeStarReview: RatingBarReview? = null
    private var twoStarReview: RatingBarReview? = null
    private var oneStarReview: RatingBarReview? = null
    private var customViewQuickFilterView: CustomViewQuickFilterView? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(EXTRA_PRODUCT_ID, "") ?: ""

        viewModel = ViewModelProvider(this, viewModelFactory).get(ReviewProductViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_review, container, false)
        ratingProduct = view.findViewById(R.id.rating_value)
        ratingProductStar = view.findViewById(R.id.product_rating)
        counterReview = view.findViewById(R.id.total_review)
        fiveStarReview = view.findViewById(R.id.five_star)
        fourStarReview = view.findViewById(R.id.four_star)
        threeStarReview = view.findViewById(R.id.three_star)
        twoStarReview = view.findViewById(R.id.two_star)
        oneStarReview = view.findViewById(R.id.one_star)
        customViewQuickFilterView = view.findViewById(R.id.filter_review)
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        setupFilterView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_vertical_product_review)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        getRecyclerView(view)?.addItemDecoration(dividerItemDecoration)
        observeViewModel()
    }

    override fun getAdapterTypeFactory(): ReviewProductTypeFactoryAdapter {
        return ReviewProductTypeFactoryAdapter(this)
    }

    override fun getScreenName(): String? = null


    override fun initInjector() {
        DaggerReputationComponent.builder()
                .reputationModule(ReputationModule())
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: ReviewProductModel?) {}

    override fun loadData(page: Int) {
        var isWithPhoto = false
        if (page <= INITIAL_PAGE && customViewQuickFilterView?.selectedFilter == getString(R.string.review_label_all)) {
            viewModel.getRatingReview(productId)
            viewModel.getHelpfulReview(productId)
        }
        var filter = customViewQuickFilterView?.selectedFilter ?: ""
        if (filter == getString(R.string.review_label_with_photo)) {
            filter = ""
            isWithPhoto = true
        } else if (filter == getString(R.string.review_label_all)) {
            filter = ""
            isWithPhoto = false
        }
        viewModel.getProductReview(productId, page, filter, isWithPhoto)
    }

    override fun onDeleteReviewResponse(element: ReviewProductModelContent?, adapterPosition: Int) {
        viewModel.deleteReview(element?.reviewId ?: "", element?.reputationId ?: "", productId)
    }

    override fun onMenuClicked(adapterPosition: Int) {}

    override fun onSeeReplied(adapterPosition: Int) {}

    override fun onGoToReportReview(shopId: String?, reviewId: String?, adapterPosition: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId.toIntOrZero())
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        startActivity(intent)
    }

    override fun onLikeDislikePressed(reviewId: String, likeStatus: Int, productId: String?, status: Boolean, adapterPosition: Int) {
        viewModel.postLikeDislikeReview(reviewId, likeStatus, productId)
    }

    override fun goToPreviewImage(position: Int, list: ArrayList<ImageUpload>?, element: ReviewProductModelContent?) {
        val listLocation = ArrayList<String>()
        val listDesc = mutableListOf<String>()
        if (list != null) {
            for (image in list) {
                listLocation.add(image.picSrcLarge)
                listDesc.add(image.description)
            }
        }

        val elementProductId = element?.productId

        val bundle = Bundle()
        bundle.putStringArrayList(EXTRA_IMAGE_URL_LIST, listLocation)
        bundle.putInt(EXTRA_DEFAULT_POSITION, position)
        bundle.putString(EXTRA_PRODUCT_ID, elementProductId)
        RouteManager.route(
                context,
                bundle,
                ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY,
                productId
        )
        reputationTracking.eventImageClickOnReview(
                elementProductId,
                element?.reviewId
        )

    }

    override fun onGoToProfile(reviewerId: String?, adapterPosition: Int) {
        RouteManager.route(context, ApplinkConst.PROFILE, reviewerId)
    }

    override fun onGoToShopInfo(shopId: String?) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP, shopId)
        startActivity(intent)
    }

    override fun onSmoothScrollToReplyView(adapterPosition: Int) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            getRecyclerView(view)?.smoothScrollToPosition(adapterPosition)
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.review_product_recycler_view
    }

    override fun createAdapterInstance(): BaseListAdapter<ReviewProductModel, ReviewProductTypeFactoryAdapter> {
        return ReviewProductAdapter(adapterTypeFactory)
    }

    private fun observeViewModel() {
        viewModel.getReviewProductList().observe(viewLifecycleOwner, observerReviewProductList)
        viewModel.getHelpfulReviewList().observe(viewLifecycleOwner, observerReviewHelpfulList)
        viewModel.getRatingReview().observe(viewLifecycleOwner, observerRatingReview)
        viewModel.getPostLikeDislike().observe(viewLifecycleOwner, observerSuccessPostLikeDislike)
        viewModel.getErrorPostLikeDislike().observe(viewLifecycleOwner, observerErrorPostLikeDislike)
        viewModel.getDeleteReview().observe(viewLifecycleOwner, observerSuccessDeleteReview)
        viewModel.getShowProgressDialog().observe(viewLifecycleOwner, observerProgressLoading)
    }

    private val observerReviewProductList = Observer { result: Result<Pair<List<ReviewProductModel>, Boolean>> ->
        when (result) {
            is Success -> {
                val data = result.data
                val reviewProductList = data.first.toMutableList()
                val isHasNextPage = data.second
                if (isLoadingInitialData && customViewQuickFilterView?.selectedFilter == getString(R.string.review_label_all)) {
                    reviewProductList.add(0, ReviewProductModelTitleHeader(getString(R.string.product_review_label_all_review)))
                    reviewHelpfulList?.let {
                        for (i in it.indices) {
                            reviewProductList.add(i, it[i])
                        }
                    }
                }
                renderList(reviewProductList, isHasNextPage)
            }
            is Fail -> showGetListError(result.throwable)
        }
    }

    private val observerReviewHelpfulList = Observer { reviewHelpfulList: List<ReviewProductModel> ->
        if (reviewHelpfulList.isNotEmpty()) {
            setListReviewHelpful(reviewHelpfulList)
        }
    }

    private val observerRatingReview = Observer { dataResponseReviewStarCount: DataResponseReviewStarCount ->
        ratingProduct?.text = dataResponseReviewStarCount.ratingScore
        ratingProductStar?.rating = dataResponseReviewStarCount.ratingScore.toFloat()
        counterReview?.text = getString(R.string.product_review_counter_review_formatted, dataResponseReviewStarCount.getTotalReview())
        for (detailReviewStarCount in dataResponseReviewStarCount.detail ?: emptyList()) {
            val percentageFloatReview = detailReviewStarCount.percentage.replace("%", "").replace(",", ".").toFloat()
            when (detailReviewStarCount.rate) {
                RATING_5 -> {
                    fiveStarReview?.percentageProgress = percentageFloatReview
                    fiveStarReview?.totalReview = detailReviewStarCount.totalReview
                }
                RATING_4 -> {
                    fourStarReview?.percentageProgress = percentageFloatReview
                    fourStarReview?.totalReview = detailReviewStarCount.totalReview
                }
                RATING_3 -> {
                    threeStarReview?.percentageProgress = percentageFloatReview
                    threeStarReview?.totalReview = detailReviewStarCount.totalReview
                }
                RATING_2 -> {
                    twoStarReview?.percentageProgress = percentageFloatReview
                    twoStarReview?.totalReview = detailReviewStarCount.totalReview
                }
                RATING_1 -> {
                    oneStarReview?.percentageProgress = percentageFloatReview
                    oneStarReview?.totalReview = detailReviewStarCount.totalReview
                }
            }
        }
    }

    private val observerSuccessPostLikeDislike = Observer { pairLikeDislikeDomainAndReviewId: Pair<LikeDislikeDomain, String> ->
        val likeDislikeDomain = pairLikeDislikeDomainAndReviewId.first
        val reviewId = pairLikeDislikeDomainAndReviewId.second
        (adapter as ReviewProductAdapter).updateLikeStatus(
                likeDislikeDomain.likeStatus,
                likeDislikeDomain.totalLike,
                reviewId
        )
    }

    private val observerErrorPostLikeDislike = Observer { tripleEReviewIdLikeStatus: Triple<Throwable, String, Int> ->
        val e = tripleEReviewIdLikeStatus.first
        val reviewId = tripleEReviewIdLikeStatus.second
        val likeStatus = tripleEReviewIdLikeStatus.third
        (adapter as ReviewProductAdapter).updateLikeStatusError(reviewId, likeStatus)
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
    }

    private val observerSuccessDeleteReview = Observer { result: Result<String> ->
        when (result) {
            is Success -> (adapter as ReviewProductAdapter).updateDeleteReview(result.data)
            is Fail -> NetworkErrorHelper.showCloseSnackbar(
                    activity,
                    ErrorHandler.getErrorMessage(context, result.throwable)
            )
        }
    }

    private val observerProgressLoading = Observer { isShowProgressLoading: Boolean ->
        if (isShowProgressLoading) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    private fun setupFilterView() {
        val quickFilterItemList = mutableListOf<QuickFilterItem>()
        val allFilterItem = CustomViewQuickFilterItem()
        allFilterItem.type = getString(R.string.review_label_all)
        val productReviewItemFilterViewAll = ReviewProductItemFilterView(requireContext())
        productReviewItemFilterViewAll.isActive = false
        productReviewItemFilterViewAll.setAll(true)
        allFilterItem.defaultView = productReviewItemFilterViewAll
        val productReviewItemFilterViewAllActive = ReviewProductItemFilterView(requireContext())
        productReviewItemFilterViewAllActive.isActive = true
        productReviewItemFilterViewAllActive.setAll(true)
        allFilterItem.selectedView = productReviewItemFilterViewAllActive
        allFilterItem.isSelected = true

        quickFilterItemList.add(allFilterItem)

        val productReviewItemFilterViewWithPhoto = ReviewProductItemFilterView(requireContext())
        productReviewItemFilterViewWithPhoto.isActive = false
        productReviewItemFilterViewWithPhoto.setWithPhoto(true)

        val productReviewItemFilterViewWithPhotoActive = ReviewProductItemFilterView(requireContext())
        productReviewItemFilterViewWithPhotoActive.isActive = true
        productReviewItemFilterViewWithPhotoActive.setWithPhoto(true)

        val imageFilterItem = CustomViewQuickFilterItem()
        imageFilterItem.type = getString(R.string.review_label_with_photo)
        imageFilterItem.defaultView = productReviewItemFilterViewWithPhoto
        imageFilterItem.selectedView = productReviewItemFilterViewWithPhotoActive

        quickFilterItemList.add(imageFilterItem)

        for (i in 1..TOTAL_FILTER_ITEM) {
            val quickFilterItem = CustomViewQuickFilterItem()
            quickFilterItem.type = i.toString()
            val productReviewItemFilterView = ReviewProductItemFilterView(requireContext())
            productReviewItemFilterView.isActive = false
            productReviewItemFilterView.rating = i
            quickFilterItem.defaultView = productReviewItemFilterView

            val productReviewItemFilterViewActive = ReviewProductItemFilterView(requireContext())
            productReviewItemFilterViewActive.isActive = true
            productReviewItemFilterViewActive.rating = i
            quickFilterItem.selectedView = productReviewItemFilterViewActive

            quickFilterItemList.add(quickFilterItem)
        }
        customViewQuickFilterView?.renderFilter(quickFilterItemList)
        customViewQuickFilterView?.setListener { typeFilter ->
            reputationTracking.eventClickFilterReview(
                    addSuffixIfNeeded(typeFilter),
                    productId
            )
            loadInitialData()
        }
    }

    private fun setListReviewHelpful(reviewHelpfulList: List<ReviewProductModel>) {
        val mutableReviewHelpfulList = reviewHelpfulList.toMutableList()
        mutableReviewHelpfulList.add(0, ReviewProductModelTitleHeader(getString(R.string.product_review_label_helpful_review)))
        for (i in mutableReviewHelpfulList.indices) {
            adapter.addElement(i, mutableReviewHelpfulList[i])
        }
        this.reviewHelpfulList = mutableReviewHelpfulList
    }

    private fun toInt(string: String?): Int {
        return try {
            string?.toInt() ?: -1
        } catch (nfe: NumberFormatException) {
            -1
        }
    }

    private fun addSuffixIfNeeded(typeFilter: String): String {
        return if (!TextUtils.isEmpty(typeFilter) && TextUtils.isDigitsOnly(typeFilter)) {
            "$typeFilter star(s)"
        } else typeFilter
    }

}