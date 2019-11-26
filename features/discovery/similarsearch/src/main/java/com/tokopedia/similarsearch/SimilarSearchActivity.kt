package com.tokopedia.similarsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant.SimilarSearch.QUERY
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCaseModule
import javax.inject.Inject
import javax.inject.Named

internal class SimilarSearchActivity: BaseSimpleActivity() {

    @field:[Inject Named(SIMILAR_SEARCH_VIEW_MODEL_FACTORY)]
    lateinit var similarSearchViewModelFactory: ViewModelProvider.Factory

    override fun getNewFragment(): Fragment? {
        return SimilarSearchFragment.getInstance()
    }

    override fun setupFragment(savedInstance: Bundle?) {
        injectDependencies()

        setupViewModel()

        super.setupFragment(savedInstance)
    }

    private fun injectDependencies() {
        DaggerSimilarSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .similarSearchUseCaseModule(createSimilarSearchUseCaseModule())
                .similarSearchViewModelFactoryModule(createSimilarSearchViewModelFactoryModule())
                .build()
                .inject(this)
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun createSimilarSearchUseCaseModule(): GetSimilarProductsUseCaseModule {
        val productId = getProductIdFromApplink()

        return GetSimilarProductsUseCaseModule(productId)
    }

    private fun getProductIdFromApplink(): String {
        val uri = intent.data
        if (uri != null) {
            val paths = UriUtil.destructureUri(ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, uri)
            if (paths != null && paths.isNotEmpty()) {
                return paths[0]
            }
        }

        return ""
    }

    private fun createSimilarSearchViewModelFactoryModule(): SimilarSearchViewModelFactoryModule {
        val similarSearchQuery = getSimilarSearchQueryFromIntent()

        return SimilarSearchViewModelFactoryModule(similarSearchQuery)
    }

    private fun getSimilarSearchQueryFromIntent(): String {
        return intent.getStringExtra(QUERY)
    }

    private fun setupViewModel() {
        ViewModelProviders.of(this, similarSearchViewModelFactory).get(SimilarSearchViewModel::class.java)
    }
}