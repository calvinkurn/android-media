package com.tokopedia.wishlist.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import javax.inject.Inject

/**
 * Created by fwidjaja on 17/10/21.
 */
class WishlistV2LayoutPreference  @Inject constructor(val context: Context) {
    companion object {
        const val USER_LAYOUT_WISHLIST = "user_layout_wishlist"
        const val USER_LAYOUT_WISHLIST_TYPE = "user_layout_wishlist_type"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(USER_LAYOUT_WISHLIST, Context.MODE_PRIVATE)
    }

    // 0 = LIST, 1 = GRID
    fun setTypeLayout(type: Int) {
        sharedPrefs?.edit()?.putInt(USER_LAYOUT_WISHLIST_TYPE, type)?.apply()
    }

    fun getTypeLayout(): String {
        val typeLayout = sharedPrefs?.getInt(USER_LAYOUT_WISHLIST_TYPE, 0)
        return if (typeLayout == 0) {
            TYPE_LIST
        } else {
            TYPE_GRID
        }
    }
}