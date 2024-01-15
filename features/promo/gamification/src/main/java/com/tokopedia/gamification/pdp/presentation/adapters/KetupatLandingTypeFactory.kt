package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C1VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C2VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C3VHModel


interface KetupatLandingTypeFactory{

    fun type(model: C1VHModel): Int

    fun type(model: C2VHModel): Int

    fun type(model: C3VHModel): Int
}
