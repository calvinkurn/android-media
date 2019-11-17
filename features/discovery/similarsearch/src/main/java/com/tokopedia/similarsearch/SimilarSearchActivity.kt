package com.tokopedia.similarsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
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
        val productId = getProductIdFromApplink()

        val baseAppComponent = (application as BaseMainApplication).baseAppComponent
        val similarSearchUseCaseModule = SimilarSearchUseCaseModule(productId)

        DaggerSimilarSearchComponent.builder()
                .baseAppComponent(baseAppComponent)
                .similarSearchUseCaseModule(similarSearchUseCaseModule)
                .build()
                .inject(this)
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

    private fun setupViewModel() {
        ViewModelProviders.of(this, similarSearchViewModelFactory).get(SimilarSearchViewModel::class.java)
    }
}