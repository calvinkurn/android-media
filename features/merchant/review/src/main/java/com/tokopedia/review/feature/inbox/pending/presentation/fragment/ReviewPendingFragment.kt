package com.tokopedia.review.feature.inbox.pending.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import kotlinx.android.synthetic.main.fragment_review_pending.*
import kotlinx.android.synthetic.main.partial_review_pending_connection_error.*
import javax.inject.Inject

class ReviewPendingFragment : BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
        ReviewPendingItemListener, HasComponent<ReviewPendingComponent> {

    companion object {
        fun createNewInstance() : ReviewPendingFragment {
            return ReviewPendingFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewPendingViewModel

    override fun getAdapterTypeFactory(): ReviewPendingAdapterTypeFactory {
        return ReviewPendingAdapterTypeFactory(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewPendingUiModel) {
        // No Op
    }

    override fun loadData(page: Int) {
        getPendingReviewData(page)
    }

    override fun onCardClicked(reputationId: Int, productId: String) {
        goToCreateReviewActivity(reputationId, productId)
    }

    override fun getComponent(): ReviewPendingComponent? {
        return activity?.run {
            DaggerReviewPendingComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setupErrorPage()
    }

    private fun setupErrorPage() {
        reviewPendingConnectionErrorRetryButton.setOnClickListener {
            // Retry
        }
        reviewPendingConnectionErrorGoToSettingsButton.setOnClickListener {
            goToSettings()
        }
    }

    private fun showError() {
        reviewPendingConnectionError.show()
    }

    private fun hideError() {
        reviewPendingConnectionError.hide()
    }

    private fun showErrorToaster(errorMessage: String) {

    }

    private fun getPendingReviewData(page: Int) {

    }

    private fun goToCreateReviewActivity(reputationId: Int, productId: String) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId.toString(), productId)
    }

    private fun goToSettings() {
        RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
    }



}