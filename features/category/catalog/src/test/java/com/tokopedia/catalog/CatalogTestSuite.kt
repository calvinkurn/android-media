package com.tokopedia.catalog

import android.os.Build
import com.tokopedia.catalog.utils.CatalogUtilsTestCase
import com.tokopedia.catalog.viewmodel.CatalogProductListingViewModelTest
import com.tokopedia.catalog.viewmodel.CatalogViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.robolectric.annotation.Config

@RunWith(Suite::class)
@Config(sdk = [Build.VERSION_CODES.P])
@Suite.SuiteClasses(
        CatalogViewModelTest::class,
        CatalogProductListingViewModelTest::class,
        CatalogUtilsTestCase::class
)
class CatalogTestSuite {

}