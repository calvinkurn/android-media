package com.tokopedia.autocompletecomponent.universal

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.universal.di.DaggerUniversalSearchComponent
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment.Companion.UNIVERSAL_SEARCH_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModelFactoryModule
import com.tokopedia.discovery.common.model.SearchParameter
import javax.inject.Inject
import javax.inject.Named

open class UniversalSearchActivity : BaseActivity(), HasComponent<BaseAppComponent> {

    private lateinit var searchParameter: SearchParameter

    @Inject
    @Named(UniversalConstant.UNIVERSAL_SEARCH_VIEW_MODEL_FACTORY)
    internal lateinit var universalSearchViewModelFactory: ViewModelProvider.Factory

    private lateinit var universalSearchViewModel: UniversalSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.universal_search_activity_layout)

        initSearchParameter()
        initInjector()
        initViewModel()
        initFragment()
    }

    private fun initInjector() {
        DaggerUniversalSearchComponent
            .builder()
            .baseAppComponent(component)
            .universalSearchViewModelFactoryModule(
                UniversalSearchViewModelFactoryModule(searchParameter.getSearchParameterMap())
            )
            .build()
            .inject(this)
    }

    private fun initViewModel() {
        universalSearchViewModel = ViewModelProvider(this, universalSearchViewModelFactory)
                .get(UniversalSearchViewModel::class.java)
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