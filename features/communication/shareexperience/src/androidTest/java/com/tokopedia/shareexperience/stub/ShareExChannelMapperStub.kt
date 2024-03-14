package com.tokopedia.shareexperience.stub

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.data.mapper.ShareExChannelMapper
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExTelephonyUtil
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShareExChannelMapperStub @Inject constructor(
    @ApplicationContext context: Context,
    resourceProvider: ShareExResourceProvider,
    telephony: ShareExTelephonyUtil,
    remoteConfig: RemoteConfig,
    userSession: UserSessionInterface
) : ShareExChannelMapper(context, resourceProvider, telephony, remoteConfig, userSession) {

    var socialChannel = ShareExChannelModel(
        description = "",
        generateSocialMediaChannelList()
    )

    private var commonChannel = ShareExChannelModel(
        description = "",
        listOf()
    )

    var stubCommonChannel = false

    override fun generateSocialMediaChannel(isDefault: Boolean): ShareExChannelModel {
        return socialChannel
    }

    override fun generateDefaultChannel(): ShareExChannelModel {
        return if (!stubCommonChannel) {
            super.generateDefaultChannel()
        } else {
            commonChannel
        }
    }
}
