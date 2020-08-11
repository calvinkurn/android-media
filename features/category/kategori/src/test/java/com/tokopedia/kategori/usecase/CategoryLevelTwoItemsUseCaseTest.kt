package com.tokopedia.kategori.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kategori.repository.KategoriRepository
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryLevelTwoItemsUseCaseTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    val mKategoriRepository: KategoriRepository = mockk(relaxed = true)

    private val categoryLevelTwoItemsUseCase: CategoryLevelTwoItemsUseCase by lazy {
        spyk(CategoryLevelTwoItemsUseCase(mKategoriRepository))
    }

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun getCategoryListItems() {
        val categoryRequestParams = RequestParams.create()
        categoryRequestParams.putBoolean("isTrending", true)
        categoryRequestParams.putInt("depth", 2)
        categoryRequestParams.putString("id", "0")

        val slot = slot<Map<String, Any>>()

        runBlocking {

            coEvery {
                mKategoriRepository.getCategoryListItems(capture(slot))
            } returns mockk()

            coEvery {
                categoryLevelTwoItemsUseCase.createChildList(any(), any())
            } returns mockk()

            categoryLevelTwoItemsUseCase.getCategoryListItems(categoryRequestParams)

            assertEquals(slot.captured["depth"], 2)
            assertEquals(slot.captured["isTrending"], true)

        }
    }
}