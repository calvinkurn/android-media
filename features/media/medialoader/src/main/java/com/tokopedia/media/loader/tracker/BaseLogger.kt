package com.tokopedia.media.loader.tracker

import android.content.Context
import com.tokopedia.media.loader.internal.MediaSettingPreferences

interface BaseLogger {

    /**
     * Convert the quality settings from "Kualitas Gambar" menu in user accounts into readable name.
     */
    fun Context.getQualitySetting(): String {
        val preferences = MediaSettingPreferences.instance(this)
        return when (preferences.qualitySettings()) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }

    /**
     * A sourceId extraction from image url.
     *
     * Currently we have a lot of type of image url across Tokopedia. But in particular pages already
     * used a internal CDN as well as already registered their sourceId, thus we are able to validate it.
     *
     * For example, a product image:
     * https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/3/16/bfda7d33-ded1-460a-984d-2dceac321ac0.jpg
     *
     * The source Id based on image url above is "VqbcmM"
     *
     * But in other cases, we still having a pristine image with/without sourceId:
     * with source id:
     * https://images.tokopedia.net/img/nYdSnP/2023/10/19/b375cce0-034a-4d87-98f8-1a846402642d.jpg
     *
     * without source id:
     * https://assets.tokopedia.net/assets-tokopedia-lite/v2/atreus/kratos/80fd6d32.png
     *
     * Hence we have to extract the source id gracefully.
     *
     * One of the sourceId's identifier is that, the source id shouldn't more than 6 char, thus
     * we could use that variable to validate whether the source id valid or not.
     */
    fun getSourceId(url: String): Pair<String, String> {
        val baseImgUrl = "https://images.tokopedia.net/img/"

        if (!url.startsWith(baseImgUrl)) return Pair("", "")

        val segments = url.split("/")
        val startIndex = segments.indexOf("img")

        if (startIndex == -1) return Pair("", "")

        // these indexes are position of sourceId based on type
        val sourceIdCachedIndex = 3
        val sourceIdPristineIndex = 1

        return when {
            // caches: /img/cache/{size}/{source_id}
            url.contains("cache") && segments.size > startIndex + sourceIdCachedIndex -> {
                Pair("cache", filterSourceId(segments[startIndex + sourceIdCachedIndex]))
            }

            // pristine: /img/{source_id}
            segments.size > startIndex + sourceIdPristineIndex -> {
                Pair("pristine", filterSourceId(segments[startIndex + sourceIdPristineIndex]))
            }

            else -> Pair("", "")
        }
    }

    // a source id should be not exceed more than 6 character.
    private fun filterSourceId(id: String): String {
        if (id.length > SOURCE_ID_MAX_LENGTH) return ""

        return id
    }
}
