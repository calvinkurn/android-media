package com.tokopedia.talk.common

import android.content.Context
import android.content.Intent

/**
 * @author by nisie on 9/14/18.
 */
interface TalkRouter {

    fun getTopProfileIntent(context: Context, userId: String): Intent

    fun getLoginIntent(context: Context): Intent

    fun getShopPageIntent(context: Context, shopId: String): Intent

    fun goToProductDetailById(activity: Context, productId: String)

    fun getTalkIntent(context: Context) : Intent

}