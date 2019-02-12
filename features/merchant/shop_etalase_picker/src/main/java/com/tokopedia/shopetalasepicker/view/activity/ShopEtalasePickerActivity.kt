package com.tokopedia.shopetalasepicker.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.fragment.ShopEtalaseFragment

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalasePickerActivity : BaseSimpleActivity() {

    private var shopId: String? = null
    private var selectedEtalaseId: String? = null

    override fun getNewFragment(): Fragment {
        return ShopEtalaseFragment.createInstance(shopId, selectedEtalaseId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_ID)
        selectedEtalaseId = intent.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
        super.onCreate(savedInstanceState)
    }

    companion object {

        @JvmStatic @JvmOverloads
        fun createIntent(context: Context, shopId: String, selectedEtalaseId: String? = ""): Intent {
            val intent = Intent(context, ShopEtalasePickerActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, selectedEtalaseId)
            return intent
        }
    }

}
