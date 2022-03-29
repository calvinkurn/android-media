package com.tokopedia.reviewcommon.feature.media.detail.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.PartialWidgetReviewDetailSupplementaryInfoBinding
import com.tokopedia.reviewcommon.databinding.WidgetReviewDetailSupplementaryInfoBinding
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailSupplementaryInfoUiModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailSupplementaryUiState
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper

@SuppressLint("ClickableViewAccessibility")
class ReviewDetailSupplementaryInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetReviewDetailSupplementaryInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val reviewOnPreDrawListener = reviewOnPreDrawListener()
    private var currentUiState: ReviewDetailSupplementaryUiState? = null
    private var listener: Listener? = null

    init {
        binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.setOnTouchListener(ReviewDetailSupplementaryInfo())
    }

    private fun reviewOnPreDrawListener(): ViewTreeObserver.OnPreDrawListener {
        return ViewTreeObserver.OnPreDrawListener {
            var continueDraw = true
            if (currentUiState is ReviewDetailSupplementaryUiState.Showing) {
                binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.layout?.run {
                    val lines = lineCount
                    val currentText = text
                    val nonEllipsizedTextLength = getLineEnd(lines - 1)
                    val ellipsisCount = getEllipsisCount(lines - 1)
                    val isEllipsized = ellipsisCount.isMoreThanZero()
                    if (isEllipsized) {
                        val seeMoreText = HtmlLinkHelper(
                            context,
                            context.getString(R.string.review_media_common_see_more)
                        ).spannedString ?: ""
                        val seeMoreTextLength = seeMoreText.length * 15 / 10 // since see more text is a link, it might be wider so we add some error correction
                        val concatenatedNonEllipsizedText = SpannableStringBuilder().apply {
                            append(currentText.take(nonEllipsizedTextLength - ellipsisCount - seeMoreTextLength))
                            append(seeMoreText)
                        }
                        binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.text = concatenatedNonEllipsizedText
                        continueDraw = false
                    }
                }
            }
            binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.viewTreeObserver.removeOnPreDrawListener(reviewOnPreDrawListener)
            continueDraw
        }
    }

    private fun hideReviewDetailSupplementaryInfo() {
        gone()
    }

    private fun showReviewDetailSupplementaryInfoLoading() {
        show()
        binding.layoutReviewDetailSupplementaryInfo.root.gone()
        binding.layoutReviewDetailSupplementaryInfoShimmer.root.show()
    }

    private fun showReviewDetailSupplementaryInfoData(
        data: ReviewDetailSupplementaryInfoUiModel,
        source: Source
    ) {
        show()
        binding.layoutReviewDetailSupplementaryInfoShimmer.root.gone()
        setData(data, source)
        binding.layoutReviewDetailSupplementaryInfo.root.show()
    }

    private fun PartialWidgetReviewDetailSupplementaryInfoBinding.setupProductVariant(
        variant: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }
        tvReviewDetailProductVariant.run {
            text = buildString {
                append("Varian: ")
                append(variant)
            }
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(variant.isNotBlank())
        }
    }

    private fun PartialWidgetReviewDetailSupplementaryInfoBinding.setupReviewText(
        review: CharSequence,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_96
        }
        val maxLines = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> 3
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> Int.MAX_VALUE
        }
        tvReviewDetailReviewText.run {
            if (source == Source.REVIEW_DETAIL_FRAGMENT) {
                viewTreeObserver.removeOnPreDrawListener(reviewOnPreDrawListener)
                viewTreeObserver.addOnPreDrawListener(reviewOnPreDrawListener)
            }
            this.maxLines = maxLines
            text = review
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(review.isNotBlank())
        }
    }

    private fun PartialWidgetReviewDetailSupplementaryInfoBinding.setupComplaint(
        complaint: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }
        tvReviewDetailReviewComplaint.run {
            text = complaint
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(complaint.isNotBlank())
        }
    }

    private fun setData(data: ReviewDetailSupplementaryInfoUiModel, source: Source) {
        with(binding.layoutReviewDetailSupplementaryInfo) {
            setupProductVariant(data.variant, source)
            setupReviewText(data.review, source)
            setupComplaint(data.complaint, source)
        }
    }

    fun updateUi(uiState: ReviewDetailSupplementaryUiState, source: Source) {
        currentUiState = uiState
        when (uiState) {
            is ReviewDetailSupplementaryUiState.Hidden -> hideReviewDetailSupplementaryInfo()
            is ReviewDetailSupplementaryUiState.Loading -> showReviewDetailSupplementaryInfoLoading()
            is ReviewDetailSupplementaryUiState.Showing -> showReviewDetailSupplementaryInfoData(uiState.data, source)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private inner class ReviewDetailSupplementaryInfo: OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (v is TextView) {
                val action = event.action
                val text = v.text
                if (text is Spanned) {
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                        var x = event.x
                        var y = event.y.toInt()

                        x -= v.totalPaddingLeft
                        y -= v.totalPaddingTop

                        x += v.scrollX
                        y += v.scrollY

                        val layout = v.layout
                        val line = layout.getLineForVertical(y)
                        val off = layout.getOffsetForHorizontal(line, x)

                        val link = text.getSpans(off, off, URLSpan::class.java)
                        if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                            listener?.onDescriptionSeeMoreClicked()
                        }
                        return true
                    }
                }
            }
            return v.onTouchEvent(event)
        }
    }

    interface Listener {
        fun onDescriptionSeeMoreClicked()
    }

    enum class Source {
        REVIEW_DETAIL_FRAGMENT, EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET
    }
}