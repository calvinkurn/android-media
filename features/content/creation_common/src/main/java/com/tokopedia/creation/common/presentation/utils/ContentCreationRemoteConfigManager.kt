package com.tokopedia.creation.common.presentation.utils

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.presentation.model.ContentCreationRemoteConfigModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_CONTENT_CREATION_STORIES_CONFIG

/**
 * Created By : Muhammad Furqan on 24/10/23
 */
class ContentCreationRemoteConfigManager(context: Context) {

    private val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    fun isShowingShopEntryPoint(): Boolean = if (GlobalConfig.isSellerApp())
        getSellerAppConfigData().shopEntryPointItem
    else getMainAppConfigData().shopEntryPointItem

    fun isShowingFeedEntryPoint(): Boolean = getMainAppConfigData().shopEntryPointItem

    fun isShowingCreation(): Boolean = if (GlobalConfig.isSellerApp())
        getSellerAppConfigData().creation
    else getMainAppConfigData().creation

    private fun getConfigData(): ContentCreationRemoteConfigModel = try {
        val rawString = remoteConfig.getString(APP_CONTENT_CREATION_STORIES_CONFIG)
        Gson().fromJson(rawString, ContentCreationRemoteConfigModel::class.java)
    } catch (t: Throwable) {
        ContentCreationRemoteConfigModel()
    }

    private fun getMainAppConfigData() = getConfigData().mainApp

    private fun getSellerAppConfigData() = getConfigData().sellerApp

}
