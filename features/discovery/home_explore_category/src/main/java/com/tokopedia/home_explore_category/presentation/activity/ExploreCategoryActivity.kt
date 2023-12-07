package com.tokopedia.home_explore_category.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_explore_category.analytic.ExploreCategoryAnalytics
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.EXTRA_TITLE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.EXTRA_TYPE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TYPE_LAYANAN
import com.tokopedia.home_explore_category.di.DaggerExploreCategoryComponent
import com.tokopedia.home_explore_category.di.ExploreCategoryComponent
import com.tokopedia.home_explore_category.presentation.screen.ExploreCategoryAppBar
import com.tokopedia.home_explore_category.presentation.screen.ExploreCategoryScreen
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.viewmodel.ExploreCategoryViewModel
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.resources.isDarkMode
import javax.inject.Inject
import com.tokopedia.home_explore_category.R as home_explore_categoryR

class ExploreCategoryActivity : BaseActivity(), HasComponent<ExploreCategoryComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var exploreCategoryAnalytics: ExploreCategoryAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(ExploreCategoryViewModel::class.java)
    }

    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        handleIntentFromDeeplink()

        setContent {
            NestTheme {
                val view = LocalView.current
                val isDarkTheme = view.context.isDarkMode()
                val backgroundColor = if (isDarkTheme) {
                    NestNN.dark._0
                } else {
                    NestNN.light._50
                }

                Scaffold(
                    topBar = {
                        ExploreCategoryAppBar(
                            modifier = Modifier.height(48.dp),
                            title = stringResource(id = home_explore_categoryR.string.title_browse_all_category),
                            navigationClick = {
                                finish()
                            },
                            backgroundColor = backgroundColor
                        )
                    },
                    backgroundColor = backgroundColor
                ) { padding ->

                    val uiState by viewModel.exploreCategoryState.collectAsState()

                    ExploreCategoryScreen(
                        modifier = Modifier.padding(padding),
                        uiState = uiState,
                        uiEvent = ::onUiEvent
                    )
                }

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (!view.isInEditMode) {
                        SideEffect {
                            val window = (view.context as? Activity)?.window
                            window?.statusBarColor = backgroundColor.toArgb()
                        }
                    }
                }
            }
        }

        fetchAllCategories()
    }

    override fun getScreenName(): String = ""

    override fun getComponent(): ExploreCategoryComponent {
        return DaggerExploreCategoryComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun onBackPressed() {
        exploreCategoryAnalytics.sendBackButtonClicked()
        super.onBackPressed()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun fetchAllCategories() {
        viewModel.fetchExploreCategory()
    }

    private fun onUiEvent(uiEvent: ExploreCategoryUiEvent) {
        when (uiEvent) {
            is ExploreCategoryUiEvent.OnExploreCategoryItemClicked -> {
                exploreCategoryAnalytics.sendCategoryItemClicked(uiEvent.categoryUiModel.categoryTitle)
                viewModel.toggleSelectedCategory(uiEvent.categoryUiModel.id)
            }

            is ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked -> {
                exploreCategoryAnalytics.sendSubCategoryItemClicked(
                    uiEvent.categoryName,
                    uiEvent.subExploreCategoryUiModel,
                    uiEvent.position
                )
                RouteManager.route(this, uiEvent.subExploreCategoryUiModel.appLink)
            }

            is ExploreCategoryUiEvent.OnSubExploreCategoryItemImpressed -> {
            }

            is ExploreCategoryUiEvent.OnPrimaryButtonErrorClicked -> {
                fetchAllCategories()
            }

            is ExploreCategoryUiEvent.OnSecondaryButtonErrorClicked -> {
                goToNetworkSetting()
            }
        }
    }

    private fun goToNetworkSetting() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    private fun handleIntentFromDeeplink() {
        val data = intent.data

        if (data?.getQueryParameter(EXTRA_TITLE) == null) {
            if (data?.getQueryParameter(EXTRA_TYPE)?.toInt() == TYPE_LAYANAN) {
                title = TITLE_LAYANAN
            }
        }
    }

    companion object {
        const val TITLE_LAYANAN = "Jelajah Tokopedia"
    }
}
