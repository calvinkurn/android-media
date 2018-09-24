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

    fun getProductPageIntent(context: Context, productId: String): Intent

    fun getTalkIntent(context: Context): Intent

    fun getProductTalk(context: Context, productId: String): Intent

    fun getShopTalkIntent(context: Context, shopId: String): Intent

    fun getTalkDetailIntent(context: Context, talkId: String, shopId: String): Intent

    fun getAskSellerIntent(context: Context, toShopId: String, shopName: String,
                           customSubject: String, customMessage: String, source: String, avatar: String): Intent
}