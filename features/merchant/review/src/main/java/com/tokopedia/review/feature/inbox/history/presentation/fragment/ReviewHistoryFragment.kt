package com.tokopedia.review.feature.inbox.history.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.history.di.DaggerReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.di.ReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.presentation.adapter.ReviewHistoryAdapterTypeFactory
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.viewmodel.ReviewHistoryViewModel
import kotlinx.android.synthetic.main.fragment_review_history.*
import javax.inject.Inject

class ReviewHistoryFragment : BaseListFragment<ReviewHistoryUiModel, ReviewHistoryAdapterTypeFactory>(), HasComponent<ReviewHistoryComponent> {

    companion object {
        fun createNewInstance() : ReviewHistoryFragment {
            return ReviewHistoryFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewHistoryViewModel

    override fun getComponent(): ReviewHistoryComponent? {
        return activity?.run {
            DaggerReviewHistoryComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return reviewHistoryRecyclerView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_history, container, false)
    }

    override fun getAdapterTypeFactory(): ReviewHistoryAdapterTypeFactory {
        return ReviewHistoryAdapterTypeFactory()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewHistoryUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewList)
    }

    override fun getDefaultInitialPage(): Int {
        return ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return reviewHistorySwipeRefresh
    }




}