package com.tokopedia.sellerorder.detail.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailActivity: BaseSimpleActivity(), HasComponent<SomDetailComponent> {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
        }
        return SomDetailFragment.newInstance(bundle)
    }

    override fun getComponent(): SomDetailComponent =
        DaggerSomDetailComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}