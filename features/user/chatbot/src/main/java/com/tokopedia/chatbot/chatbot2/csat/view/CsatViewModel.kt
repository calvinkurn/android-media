package com.tokopedia.chatbot.chatbot2.csat.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CsatViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _csatDataStateFlow = MutableStateFlow(CsatModel())
    val csatDataStateFlow = _csatDataStateFlow.asStateFlow()


    fun initializeData(csatModel: CsatModel) {
        _csatDataStateFlow.update {
            it.copy(
                title = csatModel.title,
                points = csatModel.points,
                selectedPoint = csatModel.selectedPoint
            )
        }
    }

    fun setSelectedScore(pointModel: PointModel) {
        _csatDataStateFlow.update {
            it.copy(
                selectedPoint = pointModel
            )
        }
    }

}
