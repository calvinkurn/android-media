package com.tokopedia.play.helper

import com.tokopedia.play.view.uimodel.mapper.*
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by jegul on 10/02/21
 */
class ClassBuilder {

    fun getPlayUiModelMapper(
            userSession: UserSessionInterface = mockk(relaxed = true),
            productTagMapper: PlayProductTagUiMapper = PlayProductTagUiMapper(),
            merchantVoucherMapper: PlayMerchantVoucherUiMapper = PlayMerchantVoucherUiMapper(),
            chatMapper: PlayChatUiMapper = PlayChatUiMapper(userSession),
            channelStatusMapper: PlayChannelStatusMapper = PlayChannelStatusMapper(),
            channelInteractiveMapper: PlayChannelInteractiveMapper = PlayChannelInteractiveMapper(),
            interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper = PlayInteractiveLeaderboardMapper(),
            cartMapper: PlayCartMapper = PlayCartMapper()
    ) = PlayUiModelMapper(
            productTagMapper = productTagMapper,
            merchantVoucherMapper = merchantVoucherMapper,
            chatMapper = chatMapper,
            channelStatusMapper = channelStatusMapper,
            channelInteractiveMapper = channelInteractiveMapper,
            interactiveLeaderboardMapper = interactiveLeaderboardMapper,
            cartMapper = cartMapper
    )

    fun getPlayChannelDetailsRecomMapper(
        htmlTextTransformer: HtmlTextTransformer = TestHtmlTextTransformer(),
        realTimeNotificationMapper: PlayRealTimeNotificationMapper = getPlayRealTimeNotificationMapper(),
        multipleLikesMapper: PlayMultipleLikesMapper = getPlayMultipleLikesMapper(),
    ) = PlayChannelDetailsWithRecomMapper(
        htmlTextTransformer = htmlTextTransformer,
        realTimeNotificationMapper = realTimeNotificationMapper,
        multipleLikesMapper = multipleLikesMapper,
    )

    private fun getPlayRealTimeNotificationMapper(
            userSession: UserSessionInterface = mockk(relaxed = true),
            htmlTextTransformer: HtmlTextTransformer = TestHtmlTextTransformer()
    ) = PlayRealTimeNotificationMapper(
            userSession = userSession,
            htmlTextTransformer = htmlTextTransformer,
    )

    private fun getPlayMultipleLikesMapper() = PlayMultipleLikesMapper()

    fun getMapperExtraParams(
            channelId: String? = null,
            videoStartMillis: Long? = null,
            shouldTrack: Boolean = true,
            sourceType: String = "",
    ) = PlayChannelDetailsWithRecomMapper.ExtraParams(
            channelId = channelId,
            videoStartMillis = videoStartMillis,
            shouldTrack = shouldTrack,
            sourceType = sourceType,
    )
}