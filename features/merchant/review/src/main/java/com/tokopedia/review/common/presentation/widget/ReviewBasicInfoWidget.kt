package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.reading.data.UserReviewStats
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewBasicInfoWidget : BaseCustomView {

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

    private var ratingStars: ImageUnify? = null
    private var timeStamp: Typography? = null
    private var reviewerName: Typography? = null
    private var variant: Typography? = null
    private var reviewerStats: Typography? = null
    private var profilePicture: ImageUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_basic_info, this)
        bindViews()
    }

    private fun bindViews() {
        ratingStars = findViewById(R.id.review_item_rating)
        timeStamp = findViewById(R.id.review_item_timestamp)
        reviewerName = findViewById(R.id.review_item_reviewer_name)
        variant = findViewById(R.id.review_item_variant)
        reviewerStats = findViewById(R.id.review_item_reviewer_stats)
        profilePicture = findViewById(R.id.review_item_reviewer_image)
    }

    fun setRating(rating: Int) {
        ratingStars?.loadImage(getReviewStar(rating))
    }

    fun setCreateTime(createTime: String) {
        timeStamp?.text = createTime
    }

    fun setReviewerName(
        name: String,
        userId: String = "",
        listener: ReviewBasicInfoListener? = null,
        isAnonymous: Boolean = true
    ) {
        reviewerName?.apply {
            text = name
            if (isAnonymous) {
                setOnClickListener { }
            } else {
                setOnClickListener {
                    listener?.onUserNameClicked(context, userId)
                }
            }
        }
    }

    fun invertColors() {
        val color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White_96
        )
        timeStamp?.setTextColor(color)
        reviewerName?.setTextColor(color)
        variant?.setTextColor(color)
        reviewerStats?.setTextColor(color)
    }

    fun setVariantName(variantName: String) {
        if (variantName.isEmpty()) return
        this.variant?.apply {
            text = context.getString(R.string.review_gallery_variant, variantName)
            show()
        }
    }

    fun setStats(
        userStats: List<UserReviewStats>,
        userId: String,
        listener: ReviewBasicInfoListener,
        isAnonymous: Boolean
    ) {
        if (isAnonymous) {
            hideStats()
            return
        }
        var textToShow = ""
        userStats.forEach {
            if (it.formatted.isNotBlank()) {
                if (textToShow.isNotBlank()) {
                    textToShow += " "
                }
                textToShow += context.getString(
                    R.string.review_reading_reviewer_stats,
                    it.formatted
                )
            }
        }
        if (textToShow.isNotBlank()) {
            reviewerStats?.apply {
                text = textToShow
                show()
                setOnClickListener {
                    listener.onUserNameClicked(context, userId)
                }
            }
        }
    }

    fun hideStats() {
        reviewerStats?.hide()
    }

    fun setCountColorToGreen() {
        reviewerStats?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun setReviewerImage(imageUrl: String) {
        profilePicture?.apply {
            loadImage(imageUrl)
            show()
        }
    }

    fun hideReviewerImage() {
        profilePicture?.hide()
    }
}