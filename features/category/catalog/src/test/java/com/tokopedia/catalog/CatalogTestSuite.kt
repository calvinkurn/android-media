package com.tokopedia.catalog

import com.tokopedia.catalog.usecase.CatalogDetailUseCaseTest
import com.tokopedia.catalog.viewmodel.CatalogProductListingViewModelTest
import com.tokopedia.catalog.viewmodel.CatalogViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        CatalogViewModelTest::class,
        CatalogProductListingViewModelTest::class,
        CatalogDetailUseCaseTest::class
)
class CatalogTestSuite {

}