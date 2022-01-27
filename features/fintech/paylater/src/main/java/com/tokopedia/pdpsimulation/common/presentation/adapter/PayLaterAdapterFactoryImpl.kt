package com.tokopedia.pdpsimulation.common.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.common.presentation.viewholder.*
import com.tokopedia.pdpsimulation.paylater.domain.model.*

class PayLaterAdapterFactoryImpl(
    private val interaction: PayLaterOptionInteraction,
) : BaseAdapterTypeFactory(), PayLaterAdapterFactory {

    override fun type(detail: Detail) = PayLaterDetailViewHolder.LAYOUT

    override fun type(seeMoreOptionsUiModel: SeeMoreOptionsUiModel) = PayLaterSeeMoreViewHolder.LAYOUT

    override fun type(sectionTitleUiModel: SectionTitleUiModel) = PayLaterSectionHeaderViewHolder.LAYOUT

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
            else -> super.createViewHolder(parent, type)
        }
    }
}