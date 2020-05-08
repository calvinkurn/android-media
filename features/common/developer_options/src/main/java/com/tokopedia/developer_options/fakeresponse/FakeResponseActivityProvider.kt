package com.tokopedia.developer_options.fakeresponse

import android.content.Context
import android.content.Intent


class FakeResponseActivityProvider {
    fun startActivity(context: Context) {
        try {
            val actionName = context.applicationContext.packageName + ".fakeresponse.gqlTesting"
            val intent = Intent(actionName).addCategory(Intent.CATEGORY_DEFAULT)
            context.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}