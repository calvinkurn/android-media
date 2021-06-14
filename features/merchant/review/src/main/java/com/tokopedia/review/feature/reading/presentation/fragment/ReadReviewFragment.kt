package com.tokopedia.review.feature.reading.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReadReviewFragment : BaseListFragment<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(),
        HasComponent<ReadReviewComponent>, ReadReviewItemListener, ReadReviewHeaderListener {


    companion object {
        fun createNewInstance(productId: String): ReadReviewFragment {
            return ReadReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReadReviewViewModel

    private var reviewHeader: ReadReviewHeader? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null

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
        // No Op
    }

    override fun loadData(page: Int) {
        getProductReview(page)
    }

    override fun getComponent(): ReadReviewComponent? {
        return activity?.run {
            DaggerReadReviewComponent.builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onThreeDotsClicked() {
        // No Op
    }

    override fun onChevronClicked() {
        if (statisticsBottomSheet == null) {
            statisticsBottomSheet = ReadReviewStatisticsBottomSheet.createInstance(getReviewStatistics(), getSatisfactionRate())
        }
        activity?.supportFragmentManager?.let { statisticsBottomSheet?.show(it, ReadReviewStatisticsBottomSheet.READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getProductIdFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_read_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindViews(view)
        observeRatingAndTopics()
        observeProductReviews()
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewHeader = view.findViewById(R.id.read_review_header)
    }

    private fun getProductReview(page: Int) {

    }

    private fun observeRatingAndTopics() {
        viewModel.ratingAndTopic.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetRatingAndTopic(it.data)
                is Fail -> {
                }
            }
        })
    }

    private fun observeProductReviews() {
        viewModel.productReviews.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        reviewHeader?.apply {
            setData(ratingAndTopics.rating)
            setListener(this@ReadReviewFragment)
        }
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return viewModel.getReviewStatistics()
    }

    private fun getSatisfactionRate(): String {
        return viewModel.getReviewSatisfactionRate()
    }

}