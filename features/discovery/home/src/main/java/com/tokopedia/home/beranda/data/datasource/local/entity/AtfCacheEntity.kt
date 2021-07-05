package com.tokopedia.home.beranda.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.home.constant.AtfKey

@Entity
data class AtfCacheEntity(
        @PrimaryKey
        val id: Int = 0,
        val name: String = "",
        val component: String = "",
        val param: String = "",
        val isOptional: Boolean = false,
        var content: String? = "",
        var status: Int = AtfKey.STATUS_SUCCESS
)