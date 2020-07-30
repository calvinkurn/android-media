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
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(BuyerConsts.PARAM_SHOP_NAME, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE, "")
            bundle.putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, null)
            bundle.putString(BuyerConsts.PARAM_ORDER_ID, "")
            bundle.putString(BuyerConsts.PARAM_URI, "")
            bundle.putBoolean(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, false)
            bundle.putString(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, "")
            bundle.putString(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, "")
            bundle.putInt(BuyerConsts.PARAM_SHOP_ID, -1)
            bundle.putString(BuyerConsts.PARAM_BOUGHT_DATE, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE_URL, "")
            bundle.putString(BuyerConsts.PARAM_STATUS_ID, "")
            bundle.putString(BuyerConsts.PARAM_STATUS_INFO, "")
            bundle.putBoolean(BuyerConsts.PARAM_IS_WAIT_TO_CANCEL, false)
            bundle.putString(BuyerConsts.PARAM_WAIT_MSG, "")
        }
        return BuyerRequestCancelFragment.newInstance(bundle)
    }

    override fun getComponent(): OrderDetailsComponent {
        return DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }
}