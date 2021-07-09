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
            interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper = PlayInteractiveLeaderboardMapper()
    ) = PlayUiModelMapper(
            userSession = userSession,
            productTagMapper = productTagMapper,
            merchantVoucherMapper = merchantVoucherMapper,
            chatMapper = chatMapper,
            channelStatusMapper = channelStatusMapper,
            channelInteractiveMapper = channelInteractiveMapper,
            interactiveLeaderboardMapper = interactiveLeaderboardMapper
    )

    fun getPlayChannelDetailsRecomMapper(
            htmlTextTransformer: HtmlTextTransformer = TestHtmlTextTransformer()
    ) = PlayChannelDetailsWithRecomMapper(
            htmlTextTransformer = htmlTextTransformer
    )

    fun getMapperExtraParams(
            channelId: String? = null,
            videoStartMillis: Long? = null
    ) = PlayChannelDetailsWithRecomMapper.ExtraParams(
            channelId = channelId,
            videoStartMillis = videoStartMillis
    )
}