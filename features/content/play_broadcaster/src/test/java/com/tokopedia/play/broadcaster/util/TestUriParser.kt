package com.tokopedia.play.broadcaster.util

import android.net.Uri
import com.tokopedia.play.broadcaster.util.helper.UriParser
import io.mockk.mockk

/**
 * Created by meyta.taliti on 26/07/22.
 */
class TestUriParser : UriParser {

    override fun parse(uriString: String): Uri {
        return mockk(relaxed = true)
    }

}