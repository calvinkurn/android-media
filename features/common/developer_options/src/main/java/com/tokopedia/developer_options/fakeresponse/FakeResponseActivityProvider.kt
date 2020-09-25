package com.tokopedia.developer_options.fakeresponse

import android.content.Context
import android.content.Intent


class FakeResponseActivityProvider {
    fun startActivity(context: Context) {
        try {
            val className = "com.tokopedia.fakeresponse.presentation.activities.FakeResponseActivity"
            val intent = Intent(context, Class.forName(className))
            context.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}