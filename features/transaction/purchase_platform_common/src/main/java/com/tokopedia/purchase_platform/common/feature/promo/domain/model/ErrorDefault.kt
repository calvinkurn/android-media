package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class ErrorDefault (
        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("description")
        val description: String? = null)