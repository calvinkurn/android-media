package com.tokopedia.tokofood.example

import android.content.Context
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment

abstract class BaseTokofoodFragment : BaseMultiFragment() {
    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

}