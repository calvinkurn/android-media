package com.tokopedia.home.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.ReserveBalance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletappGetBalance
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.*

/**
 * Created by devarafikry on 25/01/22.
 */

class HomeBalanceWidgetUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `given WalletAppRepository returns reserve balance above 10000 and not linked when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeWalletAppRepository = homeWalletAppRepository
        )
        `given test WalletAppRepository returns reserve balance above 10000 and not linked`(homeWalletAppRepository)
        runBlocking {
            val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
            `then reserve_balance in homeHeaderDataModel should not be empty`(result)
        }
    }

    @Test
    fun `given WalletAppRepository returns reserve balance below 10000 and not linked when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeWalletAppRepository = homeWalletAppRepository
        )
        `given test WalletAppRepository returns reserve balance below 10000 and not linked`(homeWalletAppRepository)
        runBlocking {
            val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
            `then reserve_balance in homeHeaderDataModel should be empty`(result)
        }
    }

    @Test
    fun `given WalletAppRepository returns linked wallet when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeWalletAppRepository = homeWalletAppRepository
        )
        `given test WalletAppRepository returns linked wallet`(homeWalletAppRepository)
        runBlocking {
            val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
            `then reserve_balance in homeHeaderDataModel should be empty`(result)
        }
    }

    private fun `then reserve_balance in homeHeaderDataModel should not be empty`(
        homeHeaderDataModelResult: HomeHeaderDataModel
    ) {
        Assert.assertTrue(
            checkHeaderContainsPemudaPointsReserveBalance(homeHeaderDataModelResult)
        )
    }

    private fun `then reserve_balance in homeHeaderDataModel should be empty`(
        homeHeaderDataModelResult: HomeHeaderDataModel
    ) {
        Assert.assertTrue(
            checkHeaderNotContainsPemudaPointsReserveBalance(homeHeaderDataModelResult)
        )
    }

    fun `given test WalletAppRepository returns reserve balance above 10000 and not linked`(homeWalletAppRepository: HomeWalletAppRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = false,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 11000,
                                amountFmt = "Rp11.000"
                            )
                        )
                    )
                )
            )
        )

        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    fun `given test WalletAppRepository returns reserve balance below 10000 and not linked`(homeWalletAppRepository: HomeWalletAppRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = false,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 9000,
                                amountFmt = "Rp9.000"
                            )
                        )
                    )
                )
            )
        )

        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    fun `given test WalletAppRepository returns linked wallet`(homeWalletAppRepository: HomeWalletAppRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 9000,
                                amountFmt = "Rp9.000"
                            )
                        )
                    )
                )
            )
        )

        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    suspend fun `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase): HomeHeaderDataModel {
        return homeBalanceWidgetUseCase.onGetBalanceWidgetData(HomeHeaderDataModel())
    }

    private fun checkHeaderContainsPemudaPointsReserveBalance(data: HomeHeaderDataModel): Boolean {
        if (data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) return false
        return data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.filter {
            it.value.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED &&
                    it.value.reserveBalance.isNotEmpty()
        } != null
    }

    private fun checkHeaderNotContainsPemudaPointsReserveBalance(data: HomeHeaderDataModel): Boolean {
        if (data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) return false
        return data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.filter {
            it.value.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED &&
                    it.value.reserveBalance.isNotEmpty()
        }?.isEmpty()?:false
    }
}