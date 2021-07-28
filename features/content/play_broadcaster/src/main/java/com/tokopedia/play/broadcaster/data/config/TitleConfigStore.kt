package com.tokopedia.play.broadcaster.data.config

/**
 * Created by jegul on 13/07/20
 */
interface TitleConfigStore {

    /**
     * Setter
     */
    fun setMaxTitleChars(count: Int)

    /**
     * Getter
     */
    fun getMaxTitleChars(): Int
}