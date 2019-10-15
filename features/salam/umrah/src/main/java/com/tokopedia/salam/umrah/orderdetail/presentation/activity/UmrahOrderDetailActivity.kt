package com.tokopedia.salam.umrah.orderdetail.presentation.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.orderdetail.di.DaggerUmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.fragment.UmrahOrderDetailFragment

class UmrahOrderDetailActivity : BaseSimpleActivity(), HasComponent<UmrahOrderDetailComponent> {

    private lateinit var orderId: String

    override fun getComponent(): UmrahOrderDetailComponent =
            DaggerUmrahOrderDetailComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun getNewFragment(): Fragment? =
            UmrahOrderDetailFragment.getInstance(orderId)

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            orderId = uri.lastPathSegment
        } else if (savedInstanceState != null) {
            orderId = savedInstanceState.getString(KEY_ORDER_ID, "")
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_ORDER_ID, orderId)
    }

    companion object {
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
    }

}
