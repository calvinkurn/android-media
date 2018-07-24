package com.tokopedia.shop.info.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedV2
import com.tokopedia.shop.note.view.model.ShopNoteViewModel

interface ShopInfoView: CustomerView {
    fun renderListNote(notes: List<ShopNoteViewModel>)
    fun showListNoteError(throwable: Throwable?)
    fun onErrorGetReputation(throwable: Throwable)
    fun onSuccessGetReputation(reputationSpeed: ReputationSpeedV2)

}