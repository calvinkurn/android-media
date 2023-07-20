package com.tokopedia.autocompletecomponent.universal

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_PAGE
import com.tokopedia.autocompletecomponent.universal.di.DaggerUniversalSearchComponent
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment.Companion.UNIVERSAL_SEARCH_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.universal.presentation.mapper.UniversalSearchModelMapperModule
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModelFactoryModule
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
import javax.inject.Named

open class UniversalSearchActivity : BaseActivity(), HasComponent<BaseAppComponent> {

    private lateinit var searchParameter: SearchParameter
    private lateinit var keyword: String
    private lateinit var dimension90: String

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
        setupToolbar()
    }

    private fun initInjector() {
        val searchParameterMap = searchParameter.getSearchParameterMap()
        dimension90 = Dimension90Utils.getDimension90(searchParameterMap)
        keyword = searchParameter.getSearchQuery()

        DaggerUniversalSearchComponent
            .builder()
            .baseAppComponent(component)
            .universalSearchViewModelFactoryModule(
                UniversalSearchViewModelFactoryModule(searchParameterMap)
            )
            .universalSearchModelMapperModule(
                UniversalSearchModelMapperModule(dimension90, keyword)
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
                R.id.universalSearchContainer,
                UniversalSearchFragment.newInstance(searchParameter),
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

    private fun setupToolbar() {
        val customContentView = View.inflate(
            this,
            R.layout.universal_search_toolbar_custom_layout,
            null,
        )
        val toolbar = findViewById<NavToolbar>(R.id.universalSearchToolbar)
        val toolbarTitle =
            customContentView.findViewById<Typography>(R.id.universalSearchToolbarTitle)
        val toolbarSubtitle =
            customContentView.findViewById<Typography>(R.id.universalSearchToolbarSubtitle)

        toolbarTitle.text = getString(R.string.universal_search_toolbar_title, keyword)
        toolbarSubtitle.text = getString(R.string.universal_search_toolbar_subtitle)

        toolbar.apply {
            this@UniversalSearchActivity.lifecycle.addObserver(this)
            setupToolbarWithStatusBar(
                activity = this@UniversalSearchActivity,
                applyPadding = false,
                applyPaddingNegative = true,
            )
            setToolbarPageName(UNIVERSAL_SEARCH_PAGE)
            setCustomViewContentView(customContentView)
            setToolbarContentType(TOOLBAR_TYPE_CUSTOM)
            setIcon(
                IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.SRP_UNIVERSAL))
                    .addIcon(
                        IconList.ID_NAV_GLOBAL,
                        disableRouteManager = false,
                        disableDefaultGtmTracker = false) { }
            )
        }
    }
}
