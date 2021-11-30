package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewStatisticsBottomSheet : BottomSheetUnify() {

    companion object {
        const val READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG = "Read Review BottomSheet Tag"
        fun createInstance(ratingDetail: List<ProductReviewDetail>, satisfactionRate: String): ReadReviewStatisticsBottomSheet {
            return ReadReviewStatisticsBottomSheet().apply {
                this.ratingDetail = ratingDetail
                this.satisfactionRate = satisfactionRate
            }
        }
    }

    private var ratingDetail = listOf<ProductReviewDetail>()
    private var satisfactionRate = ""

    private var satisfactionRateTypography: Typography? = null
    private var fiveStarProgressBar: ReadReviewRatingBar? = null
    private var fourStarProgressBar: ReadReviewRatingBar? = null
    private var threeStarProgressBar: ReadReviewRatingBar? = null
    private var twoStarProgressBar: ReadReviewRatingBar? = null
    private var oneStarProgressBar: ReadReviewRatingBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_read_review_statistics, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupView(ratingDetail, satisfactionRate)
    }

    private fun setupView(ratingDetail: List<ProductReviewDetail>, satisfactionRate: String) {
        satisfactionRateTypography?.text = satisfactionRate
        val progressBars = getListOfProgressBars()
        ratingDetail.forEachIndexed { index, productReviewDetail ->
            if (productReviewDetail.rate == ratingDetail.size - index) {
                bindProgressBarData(progressBars[index], productReviewDetail)
            }
        }
    }

    private fun bindViews(view: View) {
        satisfactionRateTypography = view.findViewById(R.id.bottom_sheet_review_statistic_satisfaction_rate)
        fiveStarProgressBar = view.findViewById(R.id.bottom_sheet_review_statistic_five_progress_bar)
        fourStarProgressBar = view.findViewById(R.id.bottom_sheet_review_statistic_four_progress_bar)
        threeStarProgressBar = view.findViewById(R.id.bottom_sheet_review_statistic_three_progress_bar)
        twoStarProgressBar = view.findViewById(R.id.bottom_sheet_review_statistic_two_progress_bar)
        oneStarProgressBar = view.findViewById(R.id.bottom_sheet_review_statistic_one_progress_bar)
    }

    private fun bindProgressBarData(progressBar: ReadReviewRatingBar?, reviewDetail: ProductReviewDetail) {
        progressBar?.setProgressAndRatingCount(reviewDetail.percentage, reviewDetail.totalReviews)
    }

    private fun getListOfProgressBars(): List<ReadReviewRatingBar?> {
        return listOf(fiveStarProgressBar, fourStarProgressBar, threeStarProgressBar, twoStarProgressBar, oneStarProgressBar)
    }
}