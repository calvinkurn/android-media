package com.tokopedia.talk.common

import android.content.Context
import android.content.Intent

/**
 * @author by nisie on 9/14/18.
 */
interface TalkRouter {

    fun getTopProfileIntent(context: Context, userId: String) : Intent

}