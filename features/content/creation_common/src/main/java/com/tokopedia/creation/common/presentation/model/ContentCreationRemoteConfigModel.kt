package com.tokopedia.creation.common.presentation.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 24/10/23
 */
class ContentCreationRemoteConfigModel(
    @SerializedName("mainapp")
    val mainApp: ContentCreationRemoteConfigItemModel = ContentCreationRemoteConfigItemModel(),
    @SerializedName("sellerapp")
    val sellerApp: ContentCreationRemoteConfigItemModel = ContentCreationRemoteConfigItemModel(),
)

class ContentCreationRemoteConfigItemModel(
    @SerializedName("shop_entry_point_item")
    val shopEntryPointItem: Boolean = true,
    @SerializedName("feed_entry_point_item")
    val feedEntryPointItem: Boolean = true,
    @SerializedName("creation")
    val creation: Boolean = true
)
