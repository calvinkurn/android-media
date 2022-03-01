package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction

class SimulationEmptyViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
        AbstractViewHolder<EmptyModel>(itemView) {

    private val emptyState: EmptyStateUnify = itemView.findViewById(R.id.emptyStateInstallment)

    override fun bind(element: EmptyModel) {
        emptyState.setPrimaryCTAClickListener {
            interaction.retryLoading.invoke()
        }
    }

    companion object {
        val LAYOUT = R.layout.paylater_simulation_empty
    }
}
