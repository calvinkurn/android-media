package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

data class SimulationErrorModel(val throwable: Throwable) : ErrorNetworkModel()