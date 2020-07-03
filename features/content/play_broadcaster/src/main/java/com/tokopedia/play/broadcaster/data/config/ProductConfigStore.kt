package com.tokopedia.play.broadcaster.data.config

/**
 * Created by jegul on 03/07/20
 */
interface ProductConfigStore {

    fun setMaxProduct(count: Int)

    fun setMinProduct(count: Int)

    fun getMaxProduct(): Int

    fun getMinProduct(): Int
}