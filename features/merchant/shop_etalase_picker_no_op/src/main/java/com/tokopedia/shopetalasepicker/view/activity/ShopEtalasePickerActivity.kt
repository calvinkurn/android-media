package com.tokopedia.shopetalasepicker.view.activity

import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalasePickerActivity() {

    companion object {

        @JvmStatic @JvmOverloads
        fun createIntent(context: Context, shopId: String, selectedEtalaseId: String? = ""): Intent {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(startMain)
        }
    }

}
