package com.tokopedia.sellerpersona.data.local

/**
 * Created by @ilhamsuaib on 12/10/23.
 */

interface PersonaSharedPrefInterface {

    fun isFirstVisit(): Boolean

    fun setIsFirstVisit(isFirstVisit: Boolean)
}