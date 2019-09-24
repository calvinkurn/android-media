package com.tokopedia.sellerorder.list.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment

/**
 * Created by fwidjaja on 2019-08-23.
 */

// SOM = Seller Order Management
class SomListActivity: BaseSimpleActivity(), HasComponent<SomListComponent> {
    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): SomListFragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(TAB_ACTIVE, "")
        }
        return SomListFragment.newInstance(bundle)
    }

    override fun getComponent(): SomListComponent =
        DaggerSomListComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}