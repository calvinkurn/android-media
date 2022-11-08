package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsUiMapper @Inject constructor(

) : PlayShortsMapper {

    override fun mapAuthorList(response: WhitelistQuery): List<ContentAccountUiModel> {
        return response.whitelist.authors.map {
            ContentAccountUiModel(
                id = it.id,
                name = it.name,
                iconUrl = it.thumbnail,
                badge = it.badge,
                type = it.type,
                hasUsername = it.livestream.hasUsername,
                hasAcceptTnc = it.livestream.enable,
            )
        }
    }
}
