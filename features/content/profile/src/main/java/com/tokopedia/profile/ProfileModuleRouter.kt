package com.tokopedia.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileModuleRouter {
    fun getFavoritedShopFragment(userId: String): Fragment
}