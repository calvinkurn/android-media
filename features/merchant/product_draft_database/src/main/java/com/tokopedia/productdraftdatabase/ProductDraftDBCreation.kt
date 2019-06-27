package com.tokopedia.productdraftdatabase

import android.content.Context

object ProductDraftDBCreation {

    @JvmStatic
    fun getProductDraftDao(context: Context) = ProductDraftDB.getInstance(context).getProductDraftDao()

    @JvmStatic
    fun init(context: Context){
        ProductDraftDB.getInstance(context)
    }
}