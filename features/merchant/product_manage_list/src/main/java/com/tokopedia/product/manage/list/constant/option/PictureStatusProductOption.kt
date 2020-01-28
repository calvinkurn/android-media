package com.tokopedia.product.manage.list.constant.option

import androidx.annotation.StringDef
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption.Companion.WITHOUT_IMAGE
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption.Companion.WITH_AND_WITHOUT
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption.Companion.WITH_IMAGE

@Retention(AnnotationRetention.SOURCE)
@StringDef(WITH_IMAGE, WITHOUT_IMAGE, WITH_AND_WITHOUT)
annotation class PictureStatusProductOption {
    companion object {
        const val WITH_IMAGE = "1"
        const val WITHOUT_IMAGE = "2"
        const val WITH_AND_WITHOUT = "-1"
    }
}
