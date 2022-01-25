package com.tokopedia.pdpsimulation.common.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.common.presentation.viewholder.PayLaterDetailViewHolder
import com.tokopedia.pdpsimulation.common.presentation.viewholder.PayLaterSectionHeaderViewHolder
import com.tokopedia.pdpsimulation.common.presentation.viewholder.PayLaterSeeMoreViewHolder
import com.tokopedia.pdpsimulation.common.presentation.viewholder.PayLaterSupervisorViewHolder
import com.tokopedia.pdpsimulation.paylater.domain.model.*

class PayLaterAdapterFactoryImpl(
    private val interaction: PayLaterOptionInteraction,
) : BaseAdapterTypeFactory(), PayLaterAdapterFactory {

    override fun type(detail: Detail) = PayLaterDetailViewHolder.LAYOUT

    override fun type(seeMoreOptionsUiModel: SeeMoreOptionsUiModel) = PayLaterSeeMoreViewHolder.LAYOUT

    override fun type(sectionTitleUiModel: SectionTitleUiModel) = PayLaterSectionHeaderViewHolder.LAYOUT

    override fun type(supervisorUiModel: SupervisorUiModel) = PayLaterSupervisorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PayLaterDetailViewHolder.LAYOUT -> PayLaterDetailViewHolder(parent, interaction)
            PayLaterSeeMoreViewHolder.LAYOUT -> PayLaterSeeMoreViewHolder(parent, interaction)
            PayLaterSectionHeaderViewHolder.LAYOUT -> PayLaterSectionHeaderViewHolder(parent)
            PayLaterSupervisorViewHolder.LAYOUT -> PayLaterSupervisorViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}