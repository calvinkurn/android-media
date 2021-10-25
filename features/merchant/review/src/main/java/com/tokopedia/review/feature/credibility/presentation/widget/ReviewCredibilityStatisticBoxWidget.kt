package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.review.R
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class ReviewCredibilityStatisticBoxWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var title: Typography? = null
    private var layoutWrapper: ConstraintLayout? = null
    private var statisticWidgets: List<ReviewCredibilityStatisticWidget?> = listOf()

    private fun init() {
        View.inflate(context, R.layout.widget_review_credibility_statistic_box, this)
        bindViews()
    }

    private fun bindViews() {
        title = findViewById(R.id.review_credibility_statistic_box_title)
        layoutWrapper = findViewById(R.id.review_credibility_statistic_box_wrapper)
        statisticWidgets = listOf(
            findViewById(R.id.review_credibility_statistics_1),
            findViewById(R.id.review_credibility_statistics_2),
            findViewById(R.id.review_credibility_statistics_3)
        )
        layoutWrapper?.setBackgroundResource(R.drawable.bg_review_credibility_statistics_box)
    }

    fun setStatistics(title: String, statistics: List<ReviewerCredibilityStat>) {
        this.title?.text = HtmlLinkHelper(context, title).spannedString
        statisticWidgets.forEachIndexed { index, reviewerCredibilityStat ->
            statistics.getOrNull(index)?.let {
                reviewerCredibilityStat?.setData(it.imageURL, it.title, it.countFmt)
            }
        }
    }
}