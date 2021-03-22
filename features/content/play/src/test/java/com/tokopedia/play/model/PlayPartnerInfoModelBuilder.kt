package com.tokopedia.play.model

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.recom.PlayPartnerBasicInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayPartnerInfoModelBuilder {

    fun buildIncompleteData(
            basicInfo: PlayPartnerBasicInfoUiModel = buildPlayPartnerBasicInfo()
    ) = PlayPartnerInfoUiModel.Incomplete(basicInfo = basicInfo)

    fun buildCompleteData(
            basicInfo: PlayPartnerBasicInfoUiModel = buildPlayPartnerBasicInfo(),
            followInfo: PlayPartnerFollowInfoUiModel = buildPlayPartnerFollowInfo()
    ) = PlayPartnerInfoUiModel.Complete(
            basicInfo = basicInfo,
            followInfo = followInfo
    )

    fun buildPlayPartnerBasicInfo(
            id: Long = 123L,
            name: String = "haha stag",
            type: PartnerType = PartnerType.Shop
    ) = PlayPartnerBasicInfoUiModel(
            id = id,
            name = name,
            type = type
    )

    fun buildPlayPartnerFollowInfo(
            isFollowed: Boolean = false,
            isFollowable: Boolean = true
    ) = PlayPartnerFollowInfoUiModel(
            isFollowed = isFollowed,
            isFollowable = isFollowable
    )
}