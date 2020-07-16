package com.tokopedia.review.feature.createreputation.ui.widget

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.ui.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_create_review_expanded_text_area_bottomsheet.*

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(textAreaListener: TextAreaListener, text: String): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
            }
        }
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val view = View.inflate(it, R.layout.widget_create_review_expanded_text_area_bottomsheet,null)
            setChild(view)
            setTitle(resources.getString(R.string.review_create_bottom_sheet_title))
            showCloseIcon = false
            isFullpage = true
            setAction(ContextCompat.getDrawable(it, R.drawable.ic_collapse)) {
                textAreaListener?.onCollapseButtonClicked(reviewCreateTextAreaBottomSheet.text.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewCreateTextAreaBottomSheet.apply {
            setText(this@CreateReviewTextAreaBottomSheet.text)
            requestFocus()
        }
    }
}