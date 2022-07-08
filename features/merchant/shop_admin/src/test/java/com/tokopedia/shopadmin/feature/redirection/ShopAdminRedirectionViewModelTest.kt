package com.tokopedia.shopadmin.feature.redirection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.redirection.presentation.viewmodel.ShopAdminRedirectionViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopAdminRedirectionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAdminTypeUseCaseCase: Lazy<GetAdminTypeUseCaseCase>

    private lateinit var viewModel: ShopAdminRedirectionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopAdminRedirectionViewModel(
            CoroutineTestDispatchersProvider,
            getAdminTypeUseCaseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    @Test
    fun `when fetchAdminType should set live data success`() {
        runBlocking {
            //given
            val adminTypeUiModel =
                AdminTypeUiModel(status = "2", "1234", isShopAdmin = true)
            onGetAdminTypeUseCase_thenReturn(adminTypeUiModel)

            //when
            viewModel.fetchAdminType()

            //then
            verifyGetAdminTypeUseCaseCalled()
            val actualResult = (viewModel.adminType.value as Success).data
            Assert.assertEquals(adminTypeUiModel, actualResult)
            Assert.assertEquals(adminTypeUiModel.shopID, actualResult.shopID)
            Assert.assertEquals(adminTypeUiModel.status, actualResult.status)
        }
    }

    @Test
    fun `when fetchAdminType should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            onGetAdminTypeUseCaseError_thenReturn(errorException)

            //when
            viewModel.fetchAdminType()

            //then
            verifyGetAdminTypeUseCaseCalled()
            val actualResult = (viewModel.adminType.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    private fun onGetAdminTypeUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getAdminTypeUseCaseCase.get().execute() } throws exception
    }

    private fun verifyGetAdminTypeUseCaseCalled() {
        coVerify { getAdminTypeUseCaseCase.get().execute() }
    }

    private fun onGetAdminTypeUseCase_thenReturn(
        adminTypeUiModel: AdminTypeUiModel
    ) {
        coEvery { getAdminTypeUseCaseCase.get().execute() } returns adminTypeUiModel
    }

}