package com.tokopedia.sellerorder.list.presentation.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListActivity: BaseSimpleActivity(), HasComponent<SomListComponent> {
    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun getNewFragment(): SomListFragment? {
        /*var fragment: SomListFragment? = null
        if (intent.extras != null) {
            // val bundle = intent.extras
            fragment = SomListFragment.newInstance()
        }*/
        return SomListFragment.newInstance()
    }

    override fun getComponent(): SomListComponent =
        DaggerSomListComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}