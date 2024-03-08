package com.tokopedia.play.broadcaster.shorts.analytic.interspersing

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on January 15, 2024
 */
interface PlayShortsInterspersingAnalytic {

    fun clickInterspersingToggle(accountUiModel: ContentAccountUiModel, creationId: String, isActive: Boolean)

    fun impressInterspersingError(accountUiModel: ContentAccountUiModel, creationId: String)

    fun clickCloseInterspersingConfirmation(accountUiModel: ContentAccountUiModel, creationId: String)

    fun clickVideoPdpCard(accountUiModel: ContentAccountUiModel, creationId: String)

    fun clickNextInterspersingConfirmation(accountUiModel: ContentAccountUiModel, creationId: String)

    fun clickBackInterspersingConfirmation(accountUiModel: ContentAccountUiModel, creationId: String)
}
