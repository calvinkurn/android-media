package com.tokopedia.play.model.mapper

import com.tokopedia.play.view.uimodel.mapper.PlayCartMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChannelStatusMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChatUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayMerchantVoucherUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayMultipleLikesMapper
import com.tokopedia.play.view.uimodel.mapper.PlayProductTagUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayRealTimeNotificationMapper
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUserReportReasoningMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUserWinnerStatusMapper
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
class MapperBuilder {

    fun createSocketMapper(
        userSession: UserSessionInterface = mockk(relaxed = true),
        htmlTextTransformer: HtmlTextTransformer = DefaultHtmlTextTransformer(),
    ): PlaySocketToModelMapper {
        return PlaySocketToModelMapper(
            productTagMapper = PlayProductTagUiMapper(),
            merchantVoucherMapper = PlayMerchantVoucherUiMapper(),
            chatMapper = PlayChatUiMapper(userSession),
            channelStatusMapper = PlayChannelStatusMapper(),
            interactiveMapper = PlayInteractiveMapper(htmlTextTransformer),
            realTimeNotificationMapper = PlayRealTimeNotificationMapper(
                userSession,
                htmlTextTransformer,
            ),
            multipleLikesMapper = PlayMultipleLikesMapper(),
            userWinnerStatusMapper = PlayUserWinnerStatusMapper(),
        )
    }

    fun createUiMapper(
        userSession: UserSessionInterface = mockk(relaxed = true),
        htmlTextTransformer: HtmlTextTransformer = DefaultHtmlTextTransformer(),
    ): PlayUiModelMapper {
        return PlayUiModelMapper(
            productTagMapper = PlayProductTagUiMapper(),
            merchantVoucherMapper = PlayMerchantVoucherUiMapper(),
            chatMapper = PlayChatUiMapper(userSession),
            channelStatusMapper = PlayChannelStatusMapper(),
            channelInteractiveMapper = PlayChannelInteractiveMapper(),
            interactiveLeaderboardMapper = PlayInteractiveLeaderboardMapper(htmlTextTransformer),
            cartMapper = PlayCartMapper(),
            playUserReportMapper = PlayUserReportReasoningMapper(),
            interactiveMapper = PlayInteractiveMapper(htmlTextTransformer),
        )
    }
}