package com.tokopedia.topchat.attachproduct.view.activity

import android.content.Context
import android.content.Intent

class BroadcastMessageAttachProductActivity {

    companion object {
        @JvmStatic
        fun createInstance(context: Context, shopId: String, shopName: String, isSeller: Boolean): Intent = Intent()

        @JvmStatic
        fun createInstance(context: Context, shopId: String, shopName: String,
                           isSeller: Boolean, ids: List<Int>, hashProducts: ArrayList<HashMap<String, String>>): Intent = Intent()
    }

}