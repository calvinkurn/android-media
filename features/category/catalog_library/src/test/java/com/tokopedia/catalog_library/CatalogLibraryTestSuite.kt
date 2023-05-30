package com.tokopedia.catalog_library

import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.robolectric.annotation.Config

@RunWith(Suite::class)
@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
@Suite.SuiteClasses(
    CatalogLibraryHomePageVMTest::class,
    CatalogLibraryLandingPageVMTest::class,
    CatalogLibraryLihatSemuaVMTest::class,
    CatalogLibraryPopularBrandsVMTest::class,
    CatalogLibraryProductBaseVMTest::class
)
class CatalogLibraryTestSuite
