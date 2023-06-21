package com.tokopedia.centralizedpromo.view.fragment.partialview

import androidx.annotation.CallSuper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapter
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.BaseUiListModel
import com.tokopedia.centralizedpromo.databinding.FragmentCentralizedPromoBinding

abstract class BasePartialListView<T : BaseUiListModel<V>, F : BaseAdapterTypeFactory, V : Visitable<F>>(
    binding: FragmentCentralizedPromoBinding,
    typeFactory: F,
    coachMarkListener: CoachMarkListener,
    showCoachMark: Boolean
) : BasePartialView<T>(coachMarkListener, binding, showCoachMark) {

    protected val adapter by lazy {
        CentralizedPromoAdapter<V, F>(typeFactory, ::onResultDispatched)
    }

    abstract fun onRecyclerViewItemEmpty()
    abstract fun onRecyclerViewResultDispatched()

    @CallSuper
    open fun onRecyclerViewItemNotEmpty(data: T) {
        adapter.setElements(data.items)
    }

    override fun renderSuccess(data: T) {
        if (data.errorMessage.isNotBlank()) {
            renderError(MessageErrorException(data.errorMessage))
        } else {
            bindSuccessData(data)
            if (data.items.isNullOrEmpty()) {
                onRecyclerViewItemEmpty()
                shouldNotifyFragmentForCoachMark()
            } else {
                onRecyclerViewItemNotEmpty(data)
            }
        }
    }

    private fun onResultDispatched() {
        onRecyclerViewResultDispatched()
        shouldNotifyFragmentForCoachMark()
    }
}
