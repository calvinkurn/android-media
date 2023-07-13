package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment

class InitialSellerSearchComposeActivity : BaseActivity(), HasComponent<InitialSearchComponent> {

    private var searchSuggestionFragment: SuggestionSearchFragment? = null
    private var initialSearchFragment: InitialSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initFragments()

        setContent {
            NestTheme {
//                InitialSearchActivityScreen()
            }
        }
    }

    override fun getComponent(): InitialSearchComponent {
        return DaggerInitialSearchComponent
            .builder()
            .globalSearchSellerComponent(GlobalSearchSellerComponentBuilder.getComponent(application))
            .initialSearchModule(InitialSearchModule())
            .build()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initFragments() {
        searchSuggestionFragment = SuggestionSearchFragment()
        initialSearchFragment = InitialSearchFragment()

        searchSuggestionFragment?.let { searchSuggestionFragment ->
            initialSearchFragment?.let { initialSearchFragment ->
                supportFragmentManager.beginTransaction()
                    .hide(searchSuggestionFragment)
                    .add(android.R.id.content, searchSuggestionFragment)
                    .add(android.R.id.content, initialSearchFragment)
                    .commit()
            }
        }
    }
}
