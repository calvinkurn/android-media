package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play.broadcaster.shorts.domain.model.PlayShortsConfig
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsUiMapper @Inject constructor(
    private val gson: Gson,
) : PlayShortsMapper {

    override fun mapAuthorList(response: WhitelistQuery): List<ContentAccountUiModel> {
        return response.whitelist.authors.map {
            ContentAccountUiModel(
                id = it.id,
                name = it.name,
                iconUrl = it.thumbnail,
                badge = it.badge,
                type = it.type,
                hasUsername = it.shortVideo.hasUsername,
                enable = it.shortVideo.enable,
            )
        }
    }

    override fun mapShortsConfig(response: GetBroadcasterAuthorConfigResponse): PlayShortsConfigUiModel {
        val config = gson.fromJson<PlayShortsConfig>(
            response.authorConfig.config,
            object : TypeToken<List<PlayShortsConfig>>() {}.type
        )

        return PlayShortsConfigUiModel(
            shortsId = if(config.draftShortsId == 0) "" else config.draftShortsId.toString(),
            shortsAllowed = response.authorConfig.shortVideoAllowed,
            tncList = response.authorConfig.tnc.map {
                TermsAndConditionUiModel(desc = it.description)
            },
            maxTitleCharacter = config.maxTitleCharacter,
            maxTaggedProduct = config.maxTaggedProduct
        )
    }
}
