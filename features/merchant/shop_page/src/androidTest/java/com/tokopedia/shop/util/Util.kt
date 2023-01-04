package com.tokopedia.shop.util

import android.content.Context
import com.tokopedia.test.application.util.MockResponseList
import java.io.IOException
import java.io.InputStream

object Util {

    fun getRawString(context: Context, res: Int): String {
        val rawResource: InputStream = context.resources.openRawResource(res)
        val content = MockResponseList.streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
        }
        return content
    }
}
