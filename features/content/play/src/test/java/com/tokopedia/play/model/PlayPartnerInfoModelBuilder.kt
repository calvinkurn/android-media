package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.widget.ui.model.PartnerType

/**
 * Created by jegul on 09/02/21
 */
class PlayPartnerInfoModelBuilder {

    fun buildPlayPartnerInfo(
            id: Long = 123L,
            name: String = "haha stag",
            type: PartnerType = PartnerType.Shop
    ) = PlayPartnerInfo(
            id = id,
            name = name,
            type = type
    )
}
