package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName

data class CentralizedUserAssetDataModel(
    @SerializedName("GetCentralizedUserAssetConfig")
    val data: CentralizedUserAssetConfig = CentralizedUserAssetConfig()
)

data class CentralizedUserAssetConfig(
    @SerializedName("asset_config_vertical")
    val assetConfigVertical: List<AssetConfig> = listOf(),
    @SerializedName("asset_config_horizontal")
    val assetConfigHorizontal: List<AssetConfig> = listOf(),
    @SerializedName("asset_config")
    val assetConfig: List<AssetConfig> = listOf(),
)

data class AssetConfig(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("subtitle_color")
    val subtitleColor: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("weblink")
    val weblink: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("hide_title")
    val hideTitle: Boolean = false
)