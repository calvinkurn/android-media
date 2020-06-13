package com.tokopedia.buyerorder.detail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.fragment.BuyerRequestCancelFragment

/**
 * Created by fwidjaja on 08/06/20.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BuyerRequestCancelActivity: BaseSimpleActivity(), HasComponent<OrderDetailsComponent> {

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(BuyerConsts.PARAM_SHOP_NAME, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE, "")
            bundle.putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, null)
            bundle.putString(BuyerConsts.PARAM_ORDER_ID, "")
        }
        return BuyerRequestCancelFragment.newInstance(bundle)
    }

    override fun getComponent(): OrderDetailsComponent {
        return DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }
}