package com.tokopedia.review.feature.media.detail.presentation.widget

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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.databinding.PartialWidgetReviewDetailSupplementaryInfoBinding
import com.tokopedia.review.databinding.WidgetReviewDetailSupplementaryInfoBinding
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailSupplementaryInfoUiModel
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailSupplementaryUiState
import com.tokopedia.unifycomponents.HtmlLinkHelper

@SuppressLint("ClickableViewAccessibility")
class ReviewDetailSupplementaryInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseReviewDetailCustomView<WidgetReviewDetailSupplementaryInfoBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val REVIEW_DETAIL_MAX_LINES_COLLAPSED = 2
    }

    override val binding = WidgetReviewDetailSupplementaryInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val reviewOnGlobalLayoutListener = reviewOnGlobalLayoutListener()
    private var currentUiState: ReviewDetailSupplementaryUiState? = null
    private var listener: Listener? = null

    init {
        binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.setOnTouchListener(ReviewDetailSupplementaryInfo())
    }

    private fun reviewOnGlobalLayoutListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener(::formatReviewText)
    }

    private fun hideReviewDetailSupplementaryInfo() {
        show()
        animateHide()
    }

    private fun showReviewDetailSupplementaryInfoLoading() {
        show()
        binding.layoutReviewDetailSupplementaryInfo.root.gone()
        binding.layoutReviewDetailSupplementaryInfoShimmer.root.show()
        animateShow()
    }

    private fun showReviewDetailSupplementaryInfoData(
        data: ReviewDetailSupplementaryInfoUiModel,
        source: Source
    ) {
        show()
        binding.layoutReviewDetailSupplementaryInfoShimmer.root.gone()
        setData(data, source)
        binding.layoutReviewDetailSupplementaryInfo.root.show()
        animateShow()
    }

    private fun PartialWidgetReviewDetailSupplementaryInfoBinding.setupReviewText(
        review: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_96
        }
        val maxLines = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> REVIEW_DETAIL_MAX_LINES_COLLAPSED
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> Int.MAX_VALUE
        }
        tvReviewDetailReviewText.run {
            if (source == Source.REVIEW_DETAIL_FRAGMENT) {
                viewTreeObserver.removeOnGlobalLayoutListener(reviewOnGlobalLayoutListener)
                viewTreeObserver.addOnGlobalLayoutListener(reviewOnGlobalLayoutListener)
            }
            this.maxLines = maxLines
            text = MethodChecker.fromHtml(review)
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
            setupReviewText(data.review, source)
            setupComplaint(data.complaint, source)
        }
    }

    private fun formatReviewText() {
        if (currentUiState is ReviewDetailSupplementaryUiState.Showing) {
            binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.layout?.run {
                val lines = lineCount
                val currentText = text
                val nonEllipsizedTextLength = getLineEnd(lines.dec())
                val ellipsisCount = getEllipsisCount(lines.dec())
                val isEllipsized = ellipsisCount.isMoreThanZero()
                if (isEllipsized) {
                    val seeMoreText = HtmlLinkHelper(
                        context,
                        context.getString(R.string.review_media_common_see_more)
                    ).spannedString ?: ""
                    val seeMoreTextLength = seeMoreText.length
                    val concatenatedNonEllipsizedText = SpannableStringBuilder().apply {
                        append(currentText.take((nonEllipsizedTextLength - ellipsisCount - seeMoreTextLength).coerceAtLeast(Int.ZERO)))
                        append(seeMoreText)
                    }
                    binding.layoutReviewDetailSupplementaryInfo.tvReviewDetailReviewText.text = concatenatedNonEllipsizedText
                }
            }
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