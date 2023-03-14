package com.tokopedia.play.broadcaster.util.asset

/**
 * Created By : Jonathan Darwin on March 14, 2023
 */
object AssetHelper {

    fun getFileNameFromLink(link: String): String {
        return link.substring(link.lastIndexOf(PATH_SEPARATOR) + 1)
    }

    private const val PATH_SEPARATOR = "/"
}
