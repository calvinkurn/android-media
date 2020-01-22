package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.data.remote.model.TickerModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

class TickerMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(tickers: List<TickerModel>): List<TickerUiModel> {
        return tickers.map {
            TickerUiModel(
                    redirectUrl = it.redirectUrl.orEmpty(),
                    createdBy = it.createdBy.orEmpty(),
                    createdOn = it.createdOn.orEmpty(),
                    state = it.state.orEmpty(),
                    expireTime = it.expireTime.orEmpty(),
                    id = it.id.orEmpty(),
                    message = it.message.orEmpty(),
                    title = it.title.orEmpty(),
                    target = it.target.orEmpty(),
                    device = it.device.orEmpty(),
                    updatedOn = it.updatedOn.orEmpty(),
                    updatedBy = it.updatedBy.orEmpty(),
                    color = it.color.orEmpty()
            )
        }
    }
}