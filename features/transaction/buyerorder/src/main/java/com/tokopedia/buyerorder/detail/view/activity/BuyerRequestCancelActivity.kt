package com.tokopedia.buyerorder.detail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.view.fragment.BuyerRequestCancelFragment

/**
 * Created by fwidjaja on 08/06/20.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BuyerRequestCancelActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(BuyerConsts.PARAM_SHOP_NAME, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE, "")
            bundle.putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, null)
        }
        return BuyerRequestCancelFragment.newInstance(bundle)
    }
}