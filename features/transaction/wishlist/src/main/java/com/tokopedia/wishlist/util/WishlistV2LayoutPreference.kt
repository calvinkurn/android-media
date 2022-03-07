package com.tokopedia.wishlist.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST_INT
import javax.inject.Inject

class WishlistV2LayoutPreference  @Inject constructor(val context: Context) {
    companion object {
        const val USER_LAYOUT_WISHLIST_V2 = "user_layout_wishlist_v2"
        const val USER_LAYOUT_WISHLIST_V2_TYPE = "user_layout_wishlist_v2_type"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(USER_LAYOUT_WISHLIST_V2, Context.MODE_PRIVATE)
    }

    // 0 = LIST, 1 = GRID
    fun setTypeLayout(type: Int) {
        sharedPrefs?.edit()?.putInt(USER_LAYOUT_WISHLIST_V2_TYPE, type)?.apply()
    }

    fun getTypeLayout(): String {
        val typeLayout = sharedPrefs?.getInt(USER_LAYOUT_WISHLIST_V2_TYPE, 1)
        return if (typeLayout == TYPE_LIST_INT) {
            TYPE_LIST
        } else {
            TYPE_GRID
        }
    }
}