package com.tokopedia.oldcatalog.model.datamodel

import android.os.Bundle
import com.tokopedia.oldcatalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.oldcatalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.oldcatalog.model.raw.VideoComponentData

data class CatalogVideoDataModel (val name : String = "", val type : String = "",
                                  val videosList : ArrayList<VideoComponentData> )
    : BaseCatalogDataModel {

    override fun name(): String = name

    override fun type(): String  = type

    override fun type(typeFactory: CatalogDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseCatalogDataModel): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogDataModel): Bundle? {
        return null
    }

}
