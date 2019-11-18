package com.tokopedia.v2.home.model.vo

import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType

/**
 * Created by Lukas on 16/11/19
 */
class GeoLocationDataModel(
        val title: String = "",
        val description: String = ""
): ModelViewType{
    override fun getPrimaryKey(): Int {
        return 4
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return true
    }

}