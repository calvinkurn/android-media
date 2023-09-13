package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent

class SearchPageActivity : BaseSimpleActivity(), HasComponent<AddNewAddressRevampComponent> {

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getLayoutRes() = R.layout.activity_search_address
    override fun getParentViewResourceID() = R.id.container_activity_search_address
    override fun getNewFragment(): Fragment = SearchPageFragment.newInstance(intent?.extras ?: Bundle())
}
