package com.tokopedia.play.broadcaster.ui.model

/**
 * @author by furqan on 07/06/2020
 */
sealed class CoverSource {

    object None : CoverSource()
    object Camera : CoverSource()
    object Gallery : CoverSource()
    data class Product(val id: Long) : CoverSource()
}