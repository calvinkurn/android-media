package com.tokopedia.review.feature.createreputation.ui.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(context: Context): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.run {
            val view = View.inflate(context, R.layout.widget_create_review_expanded_text_area_bottomsheet,null)
            setChild(view)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}