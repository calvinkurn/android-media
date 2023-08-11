package com.tokopedia.catalog

import android.os.Build
import com.tokopedia.catalog.viewmodel.CatalogForYouViewModelTest
import com.tokopedia.oldcatalog.utils.CatalogUtilsTestCase
import com.tokopedia.oldcatalog.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.robolectric.annotation.Config

@RunWith(Suite::class)
@Config(sdk = [Build.VERSION_CODES.P])
@Suite.SuiteClasses(
        CatalogViewModelTest::class,
        CatalogProductListingViewModelTest::class,
        CatalogUtilsTestCase::class,
        CatalogComparisonViewModelTest::class,
        CatalogForYouViewModelTest::class,
        CatalogReviewViewModelTest::class,
)
class CatalogTestSuite
{

}
