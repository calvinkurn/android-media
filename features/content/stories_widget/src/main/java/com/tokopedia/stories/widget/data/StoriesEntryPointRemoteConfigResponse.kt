package com.tokopedia.stories.widget.data

import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
data class StoriesEntryPointRemoteConfigResponse(
    @SerializedName("disabled_entry_points")
    val disabledEntryPoints: List<String> = emptyList()
)
