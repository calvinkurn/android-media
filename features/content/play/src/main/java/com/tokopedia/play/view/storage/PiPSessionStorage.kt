package com.tokopedia.play.view.storage

import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created by jegul on 30/03/21
 */
@PlayScope
class PiPSessionStorage @Inject constructor() {

    fun hasRequestedPiPBrowsing() = mHasRequestedPiPBrowsing

    fun setHasRequestedPiPBrowsing(hasRequested: Boolean) {
        mHasRequestedPiPBrowsing = hasRequested
    }

    companion object {

        private var mHasRequestedPiPBrowsing = false
    }
}