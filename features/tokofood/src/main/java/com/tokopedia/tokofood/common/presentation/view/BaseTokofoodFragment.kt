package com.tokopedia.tokofood.common.presentation.view

import android.content.Context
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel

abstract class BaseTokofoodFragment : BaseMultiFragment() {
    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

}