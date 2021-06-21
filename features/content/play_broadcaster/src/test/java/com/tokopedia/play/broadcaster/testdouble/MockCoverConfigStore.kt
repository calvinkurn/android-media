package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.config.TitleConfigStore


/**
 * Created by jegul on 25/09/20
 */
class MockTitleConfigStore(
        private var maxTitle: Int
) : TitleConfigStore {

    override fun setMaxTitleChars(count: Int) {
        maxTitle = count
    }

    override fun getMaxTitleChars(): Int {
        return maxTitle
    }
}