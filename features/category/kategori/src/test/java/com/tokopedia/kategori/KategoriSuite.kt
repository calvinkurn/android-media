package com.tokopedia.kategori

import com.tokopedia.kategori.usecase.CategoryLevelTwoItemsUseCaseTest
import com.tokopedia.kategori.viewmodel.CategoryLevelOneViewModelTest
import com.tokopedia.kategori.viewmodel.CategoryLevelTwoViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        CategoryLevelTwoItemsUseCaseTest::class,
        CategoryLevelOneViewModelTest::class,
        CategoryLevelTwoViewModelTest::class
)
class KategoriSuite {

}