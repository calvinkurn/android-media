package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationErrorModel

class SimulationLoadingFailedViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
        AbstractViewHolder<SimulationErrorModel>(itemView) {

    private val globalError: GlobalError = itemView.findViewById(R.id.globalErrorSimulation)

    override fun bind(element: SimulationErrorModel) {
        if (element.throwable is MessageErrorException) {
            globalError.setType(GlobalError.SERVER_ERROR)
        } else {
            globalError.setType(GlobalError.NO_CONNECTION)
        }
        globalError.setActionClickListener {
            interaction.retryLoading.invoke()
        }
    }

    companion object {
        val LAYOUT = R.layout.paylater_simulation_loading_failed
    }
}
