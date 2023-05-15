package com.tokopedia.common_compose.header

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

sealed interface NestHeaderVariant {
    object Default : NestHeaderVariant

    object Transparent : NestHeaderVariant

    // TODO this need add variant for custom alpha
}
