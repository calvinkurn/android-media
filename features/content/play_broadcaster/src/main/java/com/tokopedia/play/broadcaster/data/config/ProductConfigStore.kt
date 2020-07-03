package com.tokopedia.play.broadcaster.data.config

/**
 * Created by jegul on 03/07/20
 */
interface ProductConfigStore {

    /**
     * Setter
     */
    fun setMaxProduct(count: Int)

    fun setMinProduct(count: Int)

    /**
     * Getter
     */
    fun getMaxProduct(): Int

    fun getMinProduct(): Int
}