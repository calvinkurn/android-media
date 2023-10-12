package com.tokopedia.catalog_library

import android.os.Build
import com.tokopedia.catalog_library.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.robolectric.annotation.Config

@RunWith(Suite::class)
@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
@Suite.SuiteClasses(
    CatalogLibraryHomePageViewModelTest::class,
    CatalogLibraryLandingPageViewModelTest::class,
    CatalogLibraryLihatSemuaViewModelTest::class,
    CatalogLibraryPopularBrandsViewModelTest::class,
    CatalogLibraryProductBaseViewModelTest::class
)
class CatalogLibraryTestSuite
