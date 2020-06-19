package com.tokopedia.officialstore.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.officialstore.TestDispatcherProvider
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.category.domain.GetOfficialStoreCategoriesUseCase
import com.tokopedia.officialstore.category.presentation.viewmodel.OfficialStoreCategoryViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OfficialStoreCategoryViewModelTest {

    @RelaxedMockK
    lateinit var getOfficialStoreCategoriesUseCase: GetOfficialStoreCategoriesUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModelStore: OfficialStoreCategoryViewModel by lazy {
        OfficialStoreCategoryViewModel(
                getOfficialStoreCategoriesUseCase,
                TestDispatcherProvider()
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetOfficialStoreCategoriesSuccess() {
        coEvery {
            getOfficialStoreCategoriesUseCase.executeOnBackground()
        } returns OfficialStoreCategories()
        viewModelStore.getOfficialStoreCategories()
        coVerify {
            getOfficialStoreCategoriesUseCase.executeOnBackground()
        }
        assertTrue(viewModelStore.officialStoreCategoriesResult.value is Success)
    }

    @Test
    fun testGetOfficialStoreCategoriesError() {
        coEvery {
            getOfficialStoreCategoriesUseCase.executeOnBackground()
        } throws Throwable()
        viewModelStore.getOfficialStoreCategories()
        coVerify {
            getOfficialStoreCategoriesUseCase.executeOnBackground()
        }
        assertTrue(viewModelStore.officialStoreCategoriesResult.value is Fail)
    }

}

