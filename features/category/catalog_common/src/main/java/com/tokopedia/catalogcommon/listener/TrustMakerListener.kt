package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel

interface TrustMakerListener {

    fun onTrustMakerImpression(
        currentVisibleTrustMaker: List<TrustMakerUiModel.ItemTrustMakerUiModel>,
        widgetName: String
    )

}
