package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.config.CoverConfigStore

/**
 * Created by jegul on 25/09/20
 */
class MockCoverConfigStore(
        private var maxTitle: Int
) : CoverConfigStore {

    override fun setMaxTitleChars(count: Int) {
        maxTitle = count
    }

    override fun getMaxTitleChars(): Int {
        return maxTitle
    }
}