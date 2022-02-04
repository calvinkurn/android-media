package com.tokopedia.pdpsimulation.paylater.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.presentation.viewholder.*

class PayLaterAdapterFactoryImpl(
    val interaction: PayLaterOptionInteraction,
) : BaseAdapterTypeFactory(), PayLaterAdapterFactory {

    override fun type(detail: Detail) = PayLaterDetailViewHolder.LAYOUT

    override fun type(seeMoreOptionsUiModel: SeeMoreOptionsUiModel) = PayLaterSeeMoreViewHolder.LAYOUT

    override fun type(sectionTitleUiModel: SectionTitleUiModel) = PayLaterSectionHeaderViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel) = PayLaterShimmerViewHolder.LAYOUT

    override fun type(supervisorUiModel: SupervisorUiModel) = PayLaterSupervisorViewHolder.LAYOUT
    override fun type(content: Content): Int {
        return when(content.type) {
            2 -> InstallmentDividerViewHolder.LAYOUT
            else -> PayLaterInstallmentInfoViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PayLaterDetailViewHolder.LAYOUT -> PayLaterDetailViewHolder(parent, interaction)
            PayLaterSeeMoreViewHolder.LAYOUT -> PayLaterSeeMoreViewHolder(parent, interaction)
            PayLaterSectionHeaderViewHolder.LAYOUT -> PayLaterSectionHeaderViewHolder(parent)
            PayLaterSupervisorViewHolder.LAYOUT -> PayLaterSupervisorViewHolder(parent)
            PayLaterInstallmentInfoViewHolder.LAYOUT  -> PayLaterInstallmentInfoViewHolder(parent)
            InstallmentDividerViewHolder.LAYOUT  -> InstallmentDividerViewHolder(parent)
            PayLaterShimmerViewHolder.LAYOUT -> PayLaterShimmerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}