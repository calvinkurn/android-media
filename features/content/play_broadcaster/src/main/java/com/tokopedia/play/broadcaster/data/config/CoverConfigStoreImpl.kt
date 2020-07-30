package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 13/07/20
 */
class CoverConfigStoreImpl @Inject constructor(

): CoverConfigStore {

    /**
     * Default Value:
     * Max title chars = 38
     */
    private var mMaxTitleChars: Int = 38

    override fun setMaxTitleChars(count: Int) {
        mMaxTitleChars = count
    }

    override fun getMaxTitleChars(): Int {
        return mMaxTitleChars
    }
}