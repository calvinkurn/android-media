package com.tokopedia.productdraftdatabase

import android.content.Context

object ProductDraftDBCreation {

    @JvmStatic
    fun init(context: Context){
        ProductDraftDB.getInstance(context)
    }
}