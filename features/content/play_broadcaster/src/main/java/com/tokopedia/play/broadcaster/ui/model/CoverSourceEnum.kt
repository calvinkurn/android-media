package com.tokopedia.play.broadcaster.ui.model

/**
 * @author by furqan on 07/06/2020
 */
enum class CoverSourceEnum(val value: Int) {
    NONE(0), CAMERA(1), GALLERY(2), PRODUCT(3)
}

enum class CoverStarterEnum(val value: Int) {
    NORMAL(0), CROP_ONLY(1)
}