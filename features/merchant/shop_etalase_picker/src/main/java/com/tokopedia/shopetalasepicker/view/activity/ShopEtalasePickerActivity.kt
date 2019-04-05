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
    private var isShowDefault: Boolean? = null
    private var isShowZeroProduct: Boolean? = null

    override fun getNewFragment(): Fragment {
        return ShopEtalaseFragment.createInstance(shopId, selectedEtalaseId, isShowDefault, isShowZeroProduct)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_ID)
        selectedEtalaseId = intent.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
        isShowDefault = intent.getBooleanExtra(ShopParamConstant.EXTRA_IS_SHOW_DEFAULT, false)
        isShowZeroProduct = intent.getBooleanExtra(ShopParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
        super.onCreate(savedInstanceState)
    }

    companion object {

        @JvmStatic @JvmOverloads
        fun createIntent(context: Context, shopId: String, selectedEtalaseId: String? = "",
                         isShowDefault : Boolean? = false,
                         isShowZeroProduct : Boolean? = false): Intent {
            val intent = Intent(context, ShopEtalasePickerActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, selectedEtalaseId)
            intent.putExtra(ShopParamConstant.EXTRA_IS_SHOW_DEFAULT, isShowDefault ?: false)
            intent.putExtra(ShopParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, isShowZeroProduct ?: false)
            return intent
        }
    }

}
