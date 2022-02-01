package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SupervisorUiModel

class PayLaterSupervisorViewHolder(itemView: View) :
    AbstractViewHolder<SupervisorUiModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_supervisor_widget
    }

    override fun bind(element: SupervisorUiModel) {}
}
