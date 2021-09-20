package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CentralizedUserAssetDataModel(
    @SerializedName("GetCentralizedUserAssetConfig") @Expose
    val data: CentralizedUserAssetConfig = CentralizedUserAssetConfig()
)

data class CentralizedUserAssetConfig(
    @SerializedName("asset_config_vertical") @Expose
    val assetConfigVertical: List<AssetConfig> = listOf(),
    @SerializedName("asset_config_horizontal") @Expose
    val assetConfigHorizontal: List<AssetConfig> = listOf(),
    @SerializedName("asset_config") @Expose
    val assetConfig: List<AssetConfig> = listOf(),
)

data class AssetConfig(
    @SerializedName("id") @Expose
    val id: String = "",
    @SerializedName("title") @Expose
    val title: String = "",
    @SerializedName("subtitle") @Expose
    val subtitle: String = "",
    @SerializedName("subtitle_color") @Expose
    val subtitleColor: String = "",
    @SerializedName("applink") @Expose
    val applink: String = "",
    @SerializedName("weblink") @Expose
    val weblink: String = "",
    @SerializedName("icon") @Expose
    val icon: String = "",
    @SerializedName("is_active") @Expose
    val isActive: Boolean = false,
    @SerializedName("hide_title") @Expose
    val hide_title: Boolean = false
)