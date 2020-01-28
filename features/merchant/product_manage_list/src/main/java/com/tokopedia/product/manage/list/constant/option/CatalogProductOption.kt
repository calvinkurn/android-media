package com.tokopedia.product.manage.list.constant.option

import androidx.annotation.StringDef
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption.Companion.WITHOUT_CATALOG
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption.Companion.WITH_AND_WITHOUT
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption.Companion.WITH_CATALOG

@Retention(AnnotationRetention.SOURCE)
@StringDef(WITH_CATALOG, WITHOUT_CATALOG, WITH_AND_WITHOUT)
annotation class CatalogProductOption {
    companion object {
        const val WITH_CATALOG = "1"
        const val WITHOUT_CATALOG = "2"
        const val WITH_AND_WITHOUT = "-1"
    }
}