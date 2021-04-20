package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/02/21.
 */

data class UserPageAssetConfigDataModel(
        @SerializedName("GetUserPageAssetConfig") @Expose
        val data: UserPageAssetConfig = UserPageAssetConfig()
)

data class UserPageAssetConfig(
        @SerializedName("error_message") @Expose
        val errorMessage: String = "",
        @SerializedName("is_success") @Expose
        val isSuccess: Boolean = false,
        @SerializedName("config") @Expose
        val userPageAssetConfig: List<UserPageAssetConfigData> = arrayListOf(),
)

data class UserPageAssetConfigData(
        @SerializedName("asset_type") @Expose
        val assetType: String = "",
        @SerializedName("enable") @Expose
        val enable: Boolean = false,
        @SerializedName("order") @Expose
        val order: Int = -1
)