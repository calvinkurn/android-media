package com.tokopedia.shop.review.shop.view

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreview.ImagePreviewActivity.Companion.getCallingIntent
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.review.analytic.ReputationTracking
import com.tokopedia.shop.review.analytic.ReputationTrackingConstant
import com.tokopedia.shop.review.di.DaggerReputationComponent
import com.tokopedia.shop.review.di.ReputationModule
import com.tokopedia.shop.review.product.view.adapter.ReviewProductAdapter
import com.tokopedia.shop.review.product.view.adapter.ReviewProductContentViewHolder
import com.tokopedia.shop.review.product.view.adapter.ReviewProductModelContent
import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import com.tokopedia.shop.review.shop.view.adapter.ReviewShopModelContent
import com.tokopedia.shop.review.shop.view.adapter.ReviewShopTypeFactoryAdapter
import com.tokopedia.shop.review.shop.view.adapter.ReviewShopViewHolder
import com.tokopedia.shop.review.shop.view.presenter.ReviewShopContract
import com.tokopedia.shop.review.shop.view.presenter.ReviewShopPresenter
import com.tokopedia.shop.review.shop.view.uimodel.ImageUpload
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopFragment : BaseListFragment<ReviewShopModelContent?, ReviewShopTypeFactoryAdapter?>(), ReviewProductContentViewHolder.ListenerReviewHolder, ReviewShopContract.View, ReviewShopViewHolder.ShopReviewHolderListener {

    companion object {
        const val SHOP_ID = "shop_id"
        const val SHOP_DOMAIN = "shop_domain"
        fun createInstance(shopId: String?, shopDomain: String?): ReviewShopFragment {
            val shopReviewFragment = ReviewShopFragment()
            val bundle = Bundle()
            bundle.putString(SHOP_ID, shopId)
            bundle.putString(SHOP_DOMAIN, shopDomain)
            shopReviewFragment.arguments = bundle
            return shopReviewFragment
        }
    }

    @kotlin.jvm.JvmField
    @Inject
    var shopReviewPresenter: ReviewShopPresenter? = null
    @kotlin.jvm.JvmField
    @Inject
    var reputationTracking: ReputationTracking? = null
    private var progressDialog: ProgressDialog? = null
    protected var shopId: String? = null
    protected var shopDomain: String? = null

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.content = getString(R.string.review_shop_empty_list_content)
        return emptyModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = arguments!!.getString(SHOP_ID, "")
        shopDomain = arguments!!.getString(SHOP_DOMAIN, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        return inflater.inflate(R.layout.fragment_shop_review_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_vertical_product_review)!!)
        getRecyclerView(view)?.addItemDecoration(dividerItemDecoration)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onDestroy() {
        super.onDestroy()
        shopReviewPresenter!!.onDestroy()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerReputationComponent
                .builder()
                .reputationModule(ReputationModule())
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopReviewPresenter!!.attachView(this)
    }

    override fun onItemClicked(productReviewModelContent: ReviewShopModelContent?) {}
    override fun loadData(page: Int) {
        shopReviewPresenter!!.getShopReview(shopDomain, shopId, page)
    }

    override fun getAdapterTypeFactory(): ReviewShopTypeFactoryAdapter {
        return ReviewShopTypeFactoryAdapter(this, this)
    }

    override fun onGoToProfile(reviewerId: String?, adapterPosition: Int) {
        onGoToProfileTracking(adapterPosition)
        startActivity(RouteManager.getIntent(activity, ApplinkConst.PROFILE, reviewerId.toString()))
    }

    protected fun onGoToProfileTracking(adapterPosition: Int) {
        reputationTracking!!.eventClickUserAccountPage(getString(R.string.review), adapterPosition, shopId,
                shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun goToPreviewImage(position: Int, list: ArrayList<ImageUpload>, element: ReviewProductModelContent?) {
        val listLocation = ArrayList<String>()
        val listDesc = ArrayList<String>()
        for (image in list!!) {
            listLocation.add(image?.picSrcLarge.orEmpty())
            listDesc.add(image?.description.orEmpty())
        }
        startActivity(getCallingIntent(activity!!,
                listLocation,
                listDesc,
                position))
    }

    override fun onGoToShopInfo(shopId: String?) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP, shopId)
        startActivity(intent)
    }

    override fun onDeleteReviewResponse(element: ReviewProductModelContent, adapterPosition: Int) {
        onDeleteReviewResponseTracking(element, adapterPosition)
        shopReviewPresenter!!.deleteReview(element.reviewId, element.reputationId, element.productId)
    }

    protected fun onDeleteReviewResponseTracking(element: ReviewProductModelContent?, adapterPosition: Int) {
        reputationTracking!!.eventClickChooseThreeDotMenuPage(getString(R.string.review), adapterPosition, ReputationTrackingConstant.DELETE, shopId,
                shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun onSmoothScrollToReplyView(adapterPosition: Int) {
        getRecyclerView(view)?.smoothScrollToPosition(adapterPosition)
    }

    override fun onGoToReportReview(shopId: String?, reviewId: String?, adapterPosition: Int) {
        onGoToReportReviewTracking(shopId, adapterPosition)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, Integer.valueOf(shopId.orEmpty()))
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        startActivity(intent)
    }

    protected fun onGoToReportReviewTracking(shopId: String?, adapterPosition: Int) {
        reputationTracking!!.eventClickChooseThreeDotMenuPage(getString(R.string.review), adapterPosition, ReputationTrackingConstant.REPORT, shopId,
                shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun onMenuClicked(adapterPosition: Int) {
        reputationTracking!!.eventCLickThreeDotMenuPage(getString(R.string.review), adapterPosition, shopId, shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun onSeeReplied(adapterPosition: Int) {
        reputationTracking!!.eventClickSeeRepliesPage(getString(R.string.review), adapterPosition, shopId, shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun onLikeDislikePressed(reviewId: String?, likeStatus: Int, productId: String?, status: Boolean, adapterPosition: Int) {
        onLikeDislikeTracking(productId, status, adapterPosition)
        if(shopReviewPresenter?.isLogin == true)
            shopReviewPresenter!!.postLikeDislikeReview(reviewId, likeStatus, productId)
        else
            redirectToLoginPage()
    }

    protected fun onLikeDislikeTracking(productId: String?, status: Boolean, adapterPosition: Int) {
        reputationTracking!!.eventClickLikeDislikeReviewPage(getString(R.string.review), status, adapterPosition, shopId,
                shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun onErrorDeleteReview(e: Throwable?) {
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
    }

    override fun onSuccessDeleteReview(deleteReviewResponseDomain: DeleteReviewResponseDomain?, reviewId: String?) {
        (adapter as ReviewProductAdapter<*, *>).updateDeleteReview(reviewId)
    }

    override fun onErrorPostLikeDislike(e: Throwable?, reviewId: String?, likeStatus: Int) {
        (adapter as ReviewProductAdapter<*, *>).updateLikeStatusError(reviewId, likeStatus)
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
    }

    override fun onSuccessPostLikeDislike(likeDislikeDomain: LikeDislikeDomain?, reviewId: String?) {
        (adapter as ReviewProductAdapter<*, *>).updateLikeStatus(likeDislikeDomain?.likeStatus ?: 0,
                likeDislikeDomain?.totalLike ?: 0, reviewId)
    }

    override fun onGoToDetailProduct(productId: String?, adapterPosition: Int) {
        onGoToDetailProductTracking(productId, adapterPosition)
        if (context != null) {
            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    protected fun onGoToDetailProductTracking(productId: String?, adapterPosition: Int) {
        reputationTracking!!.eventClickProductPictureOrNamePage(getString(R.string.review), adapterPosition, productId,
                shopReviewPresenter!!.isMyShop(shopId))
    }

    override fun hideProgressLoading() {
        progressDialog!!.dismiss()
    }

    override fun showProgressLoading() {
        progressDialog!!.show()
    }

    override fun createAdapterInstance(): BaseListAdapter<ReviewShopModelContent?, ReviewShopTypeFactoryAdapter?> {
        return ReviewProductAdapter(adapterTypeFactory)
    }

    private fun redirectToLoginPage() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

}