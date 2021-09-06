package com.tokopedia.play.model

import com.tokopedia.play.helper.TestHtmlTextTransformer
import com.tokopedia.play.view.uimodel.mapper.*
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by jegul on 24/08/21
 */
class PlayMapperBuilder {

    fun buildSocketMapper(
            productTagMapper: PlayProductTagUiMapper = buildProductTagMapper(),
            merchantVoucherMapper: PlayMerchantVoucherUiMapper = buildMerchantVoucherMapper(),
            chatMapper: PlayChatUiMapper = buildChatMapper(),
            channelStatusMapper: PlayChannelStatusMapper = buildChannelStatusMapper(),
            channelInteractiveMapper: PlayChannelInteractiveMapper = buildChannelInteractiveMapper(),
            realTimeNotificationMapper: PlayRealTimeNotificationMapper = buildRealTimeNotificationMapper(),
    ) = PlaySocketToModelMapper(
            productTagMapper = productTagMapper,
            merchantVoucherMapper = merchantVoucherMapper,
            chatMapper = chatMapper,
            channelStatusMapper = channelStatusMapper,
            channelInteractiveMapper = channelInteractiveMapper,
            realTimeNotificationMapper = realTimeNotificationMapper,
    )

    fun buildProductTagMapper() = PlayProductTagUiMapper()

    fun buildMerchantVoucherMapper() = PlayMerchantVoucherUiMapper()

    fun buildChatMapper(
            userSession: UserSessionInterface = mockk(relaxed = true),
    ) = PlayChatUiMapper(
            userSession = userSession
    )

    fun buildChannelStatusMapper() = PlayChannelStatusMapper()

    fun buildChannelInteractiveMapper() = PlayChannelInteractiveMapper()

    fun buildRealTimeNotificationMapper(
            userSession: UserSessionInterface = mockk(relaxed = true),
            htmlTextTransformer: HtmlTextTransformer = TestHtmlTextTransformer()
    ) = PlayRealTimeNotificationMapper(
            userSession = userSession,
            htmlTextTransformer = htmlTextTransformer,
    )
}