package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class CreateReviewBottomSheet : BottomSheetUnify() {

    companion object {
        fun create() {

        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel
}