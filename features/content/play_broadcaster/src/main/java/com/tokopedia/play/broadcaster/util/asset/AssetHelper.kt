package com.tokopedia.play.broadcaster.util.asset

/**
 * Created By : Jonathan Darwin on March 14, 2023
 */
object AssetHelper {

    fun getFileNameFromLink(link: String): String {
        return link.substring(link.lastIndexOf(PATH_SEPARATOR) + 1)
    }

    fun getFileNameFromLinkWithoutExtension(link: String): String {
        val fileName = getFileNameFromLink(link)
        return fileName.substring(0, fileName.lastIndexOf(EXTENSION_SEPARATOR))
    }

    private const val PATH_SEPARATOR = "/"
    private const val EXTENSION_SEPARATOR = "."
}
