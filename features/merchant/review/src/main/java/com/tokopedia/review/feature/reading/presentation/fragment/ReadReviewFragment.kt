package com.tokopedia.review.feature.reading.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import javax.inject.Inject

class ReadReviewFragment : BaseListFragment<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(), HasComponent<ReadReviewComponent>, ReadReviewItemListener {

    @Inject
    lateinit var viewModel: ReadReviewViewModel

    override fun getAdapterTypeFactory(): ReadReviewAdapterTypeFactory {
        return ReadReviewAdapterTypeFactory(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReadReviewUiModel?) {
        TODO("Not yet implemented")
    }

    override fun loadData(page: Int) {
        TODO("Not yet implemented")
    }

    override fun getComponent(): ReadReviewComponent? {
        return activity?.run {
            DaggerReadReviewComponent.builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onThreeDotsClicked() {
        TODO("Not yet implemented")
    }

}