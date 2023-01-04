package com.tokopedia.find_native

import com.tokopedia.find_native.repository.FindNavRepositoryTest
import com.tokopedia.find_native.viewmodel.FindNavViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(FindNavRepositoryTest::class,
        FindNavViewModelTest::class)
class FindNativeTestSuite