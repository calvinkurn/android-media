package com.tokopedia.autocompletecomponent.universal

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment.Companion.UNIVERSAL_SEARCH_FRAGMENT_TAG
import com.tokopedia.discovery.common.model.SearchParameter

open class UniversalSearchActivity : BaseActivity(), HasComponent<BaseAppComponent> {

    private lateinit var searchParameter: SearchParameter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.universal_search_activity_layout)

        initFragment()
        initSearchParameter()
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.universal_search_container,
                UniversalSearchFragment.newInstance(null),
                UNIVERSAL_SEARCH_FRAGMENT_TAG
            )
            .commit()
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun initSearchParameter() {
        this.searchParameter = getSearchParameterFromIntent()
    }

    private fun getSearchParameterFromIntent(): SearchParameter {
        val uri = intent?.data

        val searchParameter = if (uri == null) SearchParameter()
        else SearchParameter(uri.toString())

        searchParameter.cleanUpNullValuesInMap()

        return searchParameter
    }
}