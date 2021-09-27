package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHighlightedTopicListener
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewHighlightedTopic: BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var topicRating: Typography? = null
    private var topicTitle: Typography? = null
    private var reviewCount: Typography? = null
    private var impressHolder: ImpressHolder = ImpressHolder()

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_highlighted_topic, this)
        bindViews()
    }

    private fun bindViews() {
        topicRating = findViewById(R.id.read_review_topic_rating)
        topicTitle = findViewById(R.id.read_review_highlighted_topic_title)
        reviewCount = findViewById(R.id.read_review_highlighted_topic_review_count)
        topicRating?.background = ContextCompat.getDrawable(context, R.drawable.bg_highlighted_topic_rating)
        background = ContextCompat.getDrawable(context, R.drawable.bg_review_highlighted_topic)
    }

    fun setHighlightedTopic(topic: ProductTopic) {
        topicRating?.text = String.format(context.getString(R.string.review_reading_rating_format), topic.rating)
        topicTitle?.text = topic.formatted
        reviewCount?.text = context.getString(R.string.review_reading_highlighted_topic_review_count, topic.reviewCount)
    }

    fun setListener(listener: ReadReviewHighlightedTopicListener, topicPosition: Int) {
        setOnClickListener {
            listener.onHighlightedTopicClicked(topicTitle?.text.toString(), topicPosition)
        }
        addOnImpressionListener(impressHolder) {
            listener.onHighlightedTopicImpressed(topicTitle?.text.toString(), topicPosition)
        }
    }
}