package com.tokopedia.salam.umrah.orderdetail.presentation.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.orderdetail.di.DaggerUmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.fragment.UmrahOrderDetailFragment

class UmrahOrderDetailActivity : BaseSimpleActivity(), HasComponent<UmrahOrderDetailComponent> {

    override fun getComponent(): UmrahOrderDetailComponent =
            DaggerUmrahOrderDetailComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun getNewFragment(): Fragment? =
            UmrahOrderDetailFragment.getInstance("")

}
