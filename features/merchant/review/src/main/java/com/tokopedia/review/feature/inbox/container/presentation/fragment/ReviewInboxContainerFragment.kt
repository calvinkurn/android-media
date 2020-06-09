package com.tokopedia.review.feature.inbox.container.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.inbox.container.di.DaggerReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.di.ReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.presentation.viewmodel.ReviewInboxContainerViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_review_inbox_container.*
import javax.inject.Inject

class ReviewInboxContainerFragment : BaseDaggerFragment(), HasComponent<ReviewInboxContainerComponent>{

    @Inject
    lateinit var viewModel: ReviewInboxContainerViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ReviewInboxContainerComponent? {
        return activity?.run {
            DaggerReviewInboxContainerComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_review_inbox_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeReviewTabs()
//        viewModel.getReviewTabs()
        addTabs()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewTabs)
    }

    private fun initView() {
        reviewInboxTabs.getUnifyTabLayout().setupWithViewPager(reviewInboxViewPager)
    }

    private fun observeReviewTabs() {
        viewModel.reviewTabs.observe(this, Observer {
            when(it) {
                is Success -> {
                    it.data.tabs.forEach {
                        // do stuff
                    }
                }
            }
        })
    }

    private fun addTabs() {
        reviewInboxTabs.addNewTab(getString(R.string.review_pending_tab_title))
        reviewInboxTabs.addNewTab(getString(R.string.review_history_tab_title))
    }

}