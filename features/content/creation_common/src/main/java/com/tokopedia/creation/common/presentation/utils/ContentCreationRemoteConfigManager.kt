package com.tokopedia.creation.common.presentation.utils

import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.presentation.model.ContentCreationRemoteConfigModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_CONTENT_CREATION_STORIES_CONFIG
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 24/10/23
 */
class ContentCreationRemoteConfigManager @Inject constructor(private val remoteConfig: RemoteConfig) {

    fun isShowingShopEntryPoint(): Boolean = if (GlobalConfig.isSellerApp())
        getSellerAppConfigData().shopEntryPointItem
    else getMainAppConfigData().shopEntryPointItem

    fun isShowingFeedEntryPoint(): Boolean = getMainAppConfigData().feedEntryPointItem

    fun isShowingCreation(): Boolean = if (GlobalConfig.isSellerApp())
        getSellerAppConfigData().creation
    else getMainAppConfigData().creation

    private fun getConfigData(): ContentCreationRemoteConfigModel = try {
        val rawString = remoteConfig.getString(APP_CONTENT_CREATION_STORIES_CONFIG)
        Gson().fromJson(rawString, ContentCreationRemoteConfigModel::class.java)
    } catch (_: Throwable) {
        ContentCreationRemoteConfigModel()
    }

    private fun getMainAppConfigData() = getConfigData().mainApp

    private fun getSellerAppConfigData() = getConfigData().sellerApp

}
