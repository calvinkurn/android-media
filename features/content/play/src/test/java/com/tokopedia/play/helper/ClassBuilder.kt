package com.tokopedia.play.helper

import com.tokopedia.play.view.uimodel.mapper.*
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
            channelStatusMapper: PlayChannelStatusMapper = PlayChannelStatusMapper()
    ) = PlayUiModelMapper(
            userSession = userSession,
            productTagMapper = productTagMapper,
            merchantVoucherMapper = merchantVoucherMapper,
            chatMapper = chatMapper,
            channelStatusMapper = channelStatusMapper
    )

    fun getPlayChannelDetailsRecomMapper() = PlayChannelDetailsWithRecomMapper()

    fun getMapperExtraParams(
            channelId: String? = null,
            videoStartMillis: Long? = null
    ) = PlayChannelDetailsWithRecomMapper.ExtraParams(
            channelId = channelId,
            videoStartMillis = videoStartMillis
    )
}