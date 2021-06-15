package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewReportBottomSheetListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewReportBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance(reviewId: String, shopId: String, listener: ReadReviewReportBottomSheetListener): ReadReviewReportBottomSheet {
            return ReadReviewReportBottomSheet().apply {
                this.reviewId = reviewId
                this.shopId = shopId
                this.listener = listener
            }
        }
    }

    private var reportOption: Typography? = null

    private var reviewId = ""
    private var shopId = ""
    private var listener: ReadReviewReportBottomSheetListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_read_review_report_option, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setClickListener()
    }

    private fun bindView(view: View) {
        reportOption = view.findViewById(R.id.read_review_report_option)
    }

    private fun setClickListener() {
        reportOption?.setOnClickListener { listener?.onReportOptionClicked(reviewId, shopId) }
    }

}