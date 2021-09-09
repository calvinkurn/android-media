package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewCredibilityStatisticWidget : BaseCustomView {

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

    private var icon: ImageUnify? = null
    private var count: Typography? = null
    private var title: Typography? = null


    private fun init() {
        View.inflate(context, R.layout.widget_review_credibility_statistic, this)
        bindViews()
    }

    private fun bindViews() {
        icon = findViewById(R.id.review_credibility_statistic_icon)
        count = findViewById(R.id.review_credibility_statistic_count)
        title = findViewById(R.id.review_credibility_statistic_title)
    }

    fun setData(imageUrl: String, title: String, formattedCount: String) {
        this.icon?.loadImage(imageUrl)
        this.title?.text = title
        this.count?.text = formattedCount
    }
}