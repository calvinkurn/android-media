package com.tokopedia.play.broadcaster.util.helper

import android.net.Uri

/**
 * Created by meyta.taliti on 26/07/22.
 */
class DefaultUriParser : UriParser {

    override fun parse(uriString: String): Uri {
        return Uri.parse(uriString)
    }
}