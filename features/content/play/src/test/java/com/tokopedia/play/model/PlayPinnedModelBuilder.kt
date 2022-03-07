package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PinnedProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayPinnedModelBuilder {

    private val productTagsBuilder = PlayProductTagsModelBuilder()

    fun buildInfo(
        pinnedMessage: PinnedMessageUiModel = buildPinnedMessage(),
    ) = PlayPinnedInfoUiModel(
        pinnedMessage = pinnedMessage,
    )

    fun buildPinnedMessage(
        id: String = "1",
        appLink: String = "https://www.tokopedia.com",
        title: String = "Ayo jelajahi"
    ) = PinnedMessageUiModel(
            id = id,
            appLink = appLink,
            title = title
    )

    fun buildPinnedProduct(
        partnerName: String = "haha stag",
        title: String = "Obral",
        hasPromo: Boolean = true,
        shouldShow: Boolean = true,
        productTags: PlayProductTagsUiModel = productTagsBuilder.buildCompleteData()
    ) = PinnedProductUiModel(
        partnerName = partnerName,
        title = title,
        hasPromo = hasPromo,
        shouldShow = shouldShow,
        productTags = productTags
    )
}