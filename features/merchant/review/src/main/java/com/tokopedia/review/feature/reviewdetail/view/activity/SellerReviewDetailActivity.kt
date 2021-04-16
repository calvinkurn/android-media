package com.tokopedia.review.feature.reviewdetail.view.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.reviewdetail.di.component.DaggerReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailActivity : BaseSimpleActivity(), HasComponent<ReviewProductDetailComponent>, ReviewSellerPerformanceMonitoringListener {

    companion object {
        const val APPLINK_PARAM_PRODUCT_ID = "productId"
    }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromApplink()
        isMainApp()
        startPerformanceMonitoring()
    }

    override fun getNewFragment(): Fragment = SellerReviewDetailFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_detail
    }

    override fun getParentViewResourceID(): Int {
        return R.id.seller_review_detail_parent_view
    }

    override fun getComponent(): ReviewProductDetailComponent {
        return DaggerReviewProductDetailComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .reviewProductDetailModule(ReviewProductDetailModule())
                .build()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_PREPARE_METRICS,
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_NETWORK_METRICS,
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.SELLER_REVIEW_DETAIL_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun isMainApp() {
        if (!GlobalConfig.isSellerApp()) {
            if (isSellerAppInstalled()) {
                val appLink = ApplinkConst.SELLER_REVIEW
                val parameterizedAppLinks = Uri.parse(appLink).buildUpon()
                        .appendQueryParameter(APPLINK_PARAM_PRODUCT_ID, productId)
                        .toString()
                val sellerHomeAppLink = Uri.parse(ApplinkConstInternalSellerapp.SELLER_HOME).buildUpon()
                        .appendQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, "true")
                        .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_IS_AUTO_LOGIN, "true")
                        .toString()
                val sellerHomeIntent = RouteManager.getIntent(this, sellerHomeAppLink).apply {
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, arrayListOf(parameterizedAppLinks))
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(sellerHomeIntent)
                finish()
                return
            }
            RouteManager.route(this, Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(ReviewInboxConstants.PARAM_TAB, ReviewInboxConstants.SELLER_TAB).build().toString())
            finish()
        }
    }

    private fun getDataFromApplink() {
        val uri = intent.data
        productId = uri?.getQueryParameter(APPLINK_PARAM_PRODUCT_ID) ?: ""
        if(productId.isNotBlank()) {
            intent.putExtra(SellerReviewDetailFragment.PRODUCT_ID, productId.toIntOrZero())
        }
    }

    private fun isSellerAppInstalled(): Boolean {
        with(SellerMigrationConstants) {
            return try {
                packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP) != null
            } catch (anfe: ActivityNotFoundException) {
                false
            }
        }
    }

}