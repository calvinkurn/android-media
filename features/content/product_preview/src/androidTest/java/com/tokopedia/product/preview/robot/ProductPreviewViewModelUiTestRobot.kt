package com.tokopedia.product.preview.robot

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalyticsImpl
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.view.fragment.ProductFragment
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.view.fragment.ReviewFragment
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.product.preview.factory.ProductPreviewFragmentFactoryUITest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import com.tokopedia.empty_state.R as empty_stateR

internal class ProductPreviewViewModelUiTestRobot(
    private val productPreviewSourceModel: ProductPreviewSourceModel,
    private val repo: ProductPreviewRepository,
    private val userSession: UserSessionInterface,
    private val productPreviewPref: ProductPreviewSharedPreferences
) {

    private val viewModel: ProductPreviewViewModel by lazy {
        ProductPreviewViewModel(
            productPreviewSource = productPreviewSourceModel,
            repo = repo,
            userSessionInterface = userSession,
            productPrevSharedPref = productPreviewPref
        )
    }

    private val viewModelFactory = object : ProductPreviewViewModelFactory.Creator {
        override fun create(
            productPreviewSource: ProductPreviewSourceModel
        ): ProductPreviewViewModelFactory {
            return ProductPreviewViewModelFactory(
                productPreviewSource = productPreviewSource,
                factory = object : ProductPreviewViewModel.Factory {
                    override fun create(
                        productPreviewSource: ProductPreviewSourceModel
                    ): ProductPreviewViewModel {
                        return viewModel
                    }
                }
            )
        }
    }

    private val productPreviewAnalyticsFactory = object : ProductPreviewAnalytics.Factory {
        override fun create(productId: String): ProductPreviewAnalytics {
            return ProductPreviewAnalyticsImpl(
                productId = productId,
                analyticManagerFactory = object : ContentAnalyticManager.Factory {
                    override fun create(
                        businessUnit: String,
                        eventCategory: String
                    ): ContentAnalyticManager {
                        return ContentAnalyticManager(
                            userSession = userSession,
                            businessUnit = "",
                            eventCategory = ""
                        )
                    }
                }
            )
        }
    }

    private val fragmentFactory = ProductPreviewFragmentFactoryUITest(
        mapOf(
            ProductPreviewFragment::class.java to {
                ProductPreviewFragment(
                    viewModelFactory = viewModelFactory,
                    router = mockk(),
                    analyticsFactory = productPreviewAnalyticsFactory
                )
            },
            ProductFragment::class.java to {
                ProductFragment(
                    analyticsFactory = productPreviewAnalyticsFactory
                )
            },
            ReviewFragment::class.java to {
                ReviewFragment(
                    analyticsFactory = productPreviewAnalyticsFactory,
                    router = mockk(),
                    abTestPlatform = mockk()
                )
            }
        )
    )

    private val scenario = launchFragmentInContainer<ProductPreviewFragment>(
        factory = fragmentFactory,
        themeResId = empty_stateR.style.AppTheme
    )

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    private fun chainable(fn: () -> Unit): ProductPreviewViewModelUiTestRobot {
        fn()
        return this
    }
}
