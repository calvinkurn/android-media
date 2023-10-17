package com.tokopedia.logisticaddaddress.features.addeditaddress.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addeditaddress.AddEditAddressComponent
import com.tokopedia.logisticaddaddress.di.addeditaddress.DaggerAddEditAddressComponent

class SearchPageActivity : BaseSimpleActivity(), HasComponent<AddEditAddressComponent> {

    override fun getComponent(): AddEditAddressComponent {
        return DaggerAddEditAddressComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getLayoutRes() = R.layout.activity_search_address
    override fun getParentViewResourceID() = R.id.container_activity_search_address
    override fun getNewFragment(): Fragment = SearchPageFragment.newInstance(intent?.extras ?: Bundle())
}
