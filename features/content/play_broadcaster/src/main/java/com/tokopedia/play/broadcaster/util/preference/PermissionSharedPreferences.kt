package com.tokopedia.play.broadcaster.util.preference

/**
 * Created by jegul on 10/07/20
 */
interface PermissionSharedPreferences {

    fun hasBeenAsked(permission: String): Boolean

    fun setHasBeenAsked(permission: String)
}