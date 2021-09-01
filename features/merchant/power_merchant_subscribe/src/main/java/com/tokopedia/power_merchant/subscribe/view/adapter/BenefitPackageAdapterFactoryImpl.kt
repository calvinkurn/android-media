package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.BenefitPackageDataViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.BenefitPackageHeaderViewHolder
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel

class BenefitPackageAdapterFactoryImpl(
    private val benefitPackageDataListener: BenefitPackageDataListener
) : BaseAdapterTypeFactory(), BenefitPackageAdapterFactory {

    override fun type(benefitPackageHeaderUiModel: BenefitPackageHeaderUiModel): Int {
        return BenefitPackageHeaderViewHolder.LAYOUT
    }

    override fun type(benefitPackageDataUiModel: BenefitPackageDataUiModel): Int {
        return BenefitPackageDataViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BenefitPackageHeaderViewHolder.LAYOUT -> BenefitPackageHeaderViewHolder(parent)
            BenefitPackageDataViewHolder.LAYOUT -> BenefitPackageDataViewHolder(
                parent,
                benefitPackageDataListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}