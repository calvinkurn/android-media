package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.*
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel

class BenefitPackageAdapterFactoryImpl(
    private val benefitPackageDataListener: BenefitPackageDataListener,
    private val benefitPackageErrorListener: BenefitPackageErrorListener
) : BaseAdapterTypeFactory(), BenefitPackageAdapterFactory {

    override fun type(benefitPackageHeaderUiModel: BenefitPackageHeaderUiModel): Int {
        return BenefitPackageHeaderViewHolder.LAYOUT
    }

    override fun type(benefitPackageDataUiModel: BenefitPackageDataUiModel): Int {
        return BenefitPackageDataViewHolder.LAYOUT
    }

    override fun type(benefitPackageErrorUiModel: BenefitPackageErrorUiModel): Int {
        return BenefitPackageErrorViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return BenefitPackageLoaderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BenefitPackageHeaderViewHolder.LAYOUT -> BenefitPackageHeaderViewHolder(parent)
            BenefitPackageDataViewHolder.LAYOUT -> BenefitPackageDataViewHolder(
                parent,
                benefitPackageDataListener
            )
            BenefitPackageErrorViewHolder.LAYOUT -> BenefitPackageErrorViewHolder(
                parent,
                benefitPackageErrorListener
            )
            BenefitPackageLoaderViewHolder.LAYOUT -> BenefitPackageLoaderViewHolder(
                parent
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}