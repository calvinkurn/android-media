package com.tokopedia.review.feature.inbox.pending.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import javax.inject.Inject

class ReviewPendingFragment  {

    @Inject
    lateinit var viewModel: ReviewPendingViewModel

    private fun showErrorToaster(errorMessage: String) {

    }
}