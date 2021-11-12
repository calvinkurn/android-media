package com.tokopedia.layanan_finansial.view.utils

import com.tokopedia.layanan_finansial.view.models.LayananSectionModel
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel

interface ViewType {

    fun type(dataModel: LayananSectionModel): Int
    fun type(dataModel: TopAdsImageModel) : Int

}