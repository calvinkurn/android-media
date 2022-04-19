package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDismissReviewSuggestedRepository
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource
import java.lang.IllegalStateException
import javax.inject.Inject

class HomeRechargeBuWidgetUseCase @Inject constructor(
        private val getRechargeBUWidgetUseCase: GetRechargeBUWidgetUseCase) {
    suspend fun onGetRechargeBuWidgetFromHolder(widgetSource: WidgetSource, currentRechargeBuWidget: RechargeBUWidgetDataModel): RechargeBUWidgetDataModel {
        getRechargeBUWidgetUseCase.setParams(widgetSource)
        val data = getRechargeBUWidgetUseCase.executeOnBackground()
        if (data.items.isNotEmpty()) {
            val newFindRechargeBUWidget = currentRechargeBuWidget.copy(data = data)
            return newFindRechargeBUWidget
        } else {
            throw IllegalStateException("Recharge bu is empty")
        }
    }
}