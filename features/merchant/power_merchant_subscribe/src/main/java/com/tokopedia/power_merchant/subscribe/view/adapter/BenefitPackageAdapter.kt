package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel

class BenefitPackageAdapter(
    benefitPackageAdapterFactoryImpl: BenefitPackageAdapterFactoryImpl
) : BaseAdapter<BenefitPackageAdapterFactoryImpl>(benefitPackageAdapterFactoryImpl) {

    fun setBenefitPackageData(data: List<BaseBenefitPackageUiModel>) {
        visitables.clear()
        visitables.addAll(data)
        notifyDataSetChanged()
    }

    fun setBenefitPackageError(item: BenefitPackageErrorUiModel) {
        if (visitables.getOrNull(lastIndex) !is BenefitPackageErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(lastIndex)
        }
    }
}