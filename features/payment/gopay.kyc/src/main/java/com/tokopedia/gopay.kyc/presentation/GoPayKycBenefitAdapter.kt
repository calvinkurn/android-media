package com.tokopedia.gopay.kyc.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class GoPayKycBenefitAdapter(adapterTypeFactory: GoPayKycBenefitFactory):
    BaseListAdapter<Visitable<*>, GoPayKycBenefitFactory>(adapterTypeFactory){

    fun addAllElements(element: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(element)
        notifyDataSetChanged()
    }

}