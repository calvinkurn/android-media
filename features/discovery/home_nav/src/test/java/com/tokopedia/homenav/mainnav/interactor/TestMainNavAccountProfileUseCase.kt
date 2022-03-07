package com.tokopedia.homenav.mainnav.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.domain.usecases.GetSaldoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetTokopointStatusFiltered
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.navigation_common.usecase.pojo.walletapp.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TestMainNavAccountProfileUseCase {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutineTestRule()

    @Test
    fun `When tokopoints response is above zero and saldo response above zero onMapToHeaderModel then tokopoints and saldo not empty`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildSuccessTokopointsResponse(99)

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is above zero and saldo response is zero onMapToHeaderModel then tokopoints is not empty and saldo is not empty`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildSuccessTokopointsResponse(99)

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(0)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is above zero and saldo response is failed onMapToHeaderModel then tokopoints is not empty and saldo is error`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildSuccessTokopointsResponse(99)

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildErrorResponse()

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.isGetSaldoError)
    }

    @Test
    fun `When tokopoints response is zero and saldo response above zero onMapToHeaderModel then tokopoints is zero and saldo is not empty`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildSuccessTokopointsResponse(0)

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount == "0")
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is zero and saldo response is zero onMapToHeaderModel then tokopoints is zero and saldo is zero`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildSuccessTokopointsResponse(0)

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount == "0")
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is failed and saldo response above zero onMapToHeaderModel then tokopoints is failed and saldo is not empty`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildErrorResponse()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.isGetUserMembershipError)
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is failed and saldo response is zero onMapToHeaderModel then tokopoints is failed and saldo is not empty`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildErrorResponse()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(0)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.isGetUserMembershipError)
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
    }

    @Test
    fun `When tokopoints response is failed and saldo response is failed onMapToHeaderModel then tokopoints is failed and saldo is failed`(){
        val getTokopointsStatusFiltered = mockk<GetTokopointStatusFiltered>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getTokopointStatusFiltered = getTokopointsStatusFiltered,
            getSaldoUseCase = getSaldoUseCase
        )
        coEvery {
            getTokopointsStatusFiltered.executeOnBackground()
        } returns buildErrorResponse()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildErrorResponse()

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.isGetUserMembershipError)
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.isGetSaldoError)
    }

    @Test
    fun `When gopay response is unlinked and saldo response is above zero onMapToHeaderModel then gopay is not empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(99, linked = false)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertFalse(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.walletAppActivationCta.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay is above zero and gopay coins is above zero and saldo is above zero onMapToHeaderModel then gopay is not empty and gopay coins is not empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(99, linked = true)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay is zero and gopay coins is above zero and saldo is above zero onMapToHeaderModel then gopay is not empty and gopay coins is not empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(walletAppAmount = 0, walletCoinAmount = 99)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay above zero and gopay coins is zero and saldo is above zero onMapToHeaderModel then gopay is not empty and gopay coins is not empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(walletAppAmount = 99, walletCoinAmount = 0)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay above zero and gopay coins above zero and saldo is zero onMapToHeaderModel then gopay is not empty and gopay coins is not empty and saldo is empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(walletAppAmount = 99, walletCoinAmount = 99)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(0)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When no pemuda wallet and saldo is above zero onMapToHeaderModel then gopay is failed and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppWithEmptyBalanceResponse()

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay is zero and gopay coin is zero and saldo is above zero onMapToHeaderModel then gopay is empty and gopay coins in empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(walletAppAmount = 0, walletCoinAmount = 0)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(99)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }

    @Test
    fun `When gopay is zero and gopay coin is zero and saldo is zero onMapToHeaderModel then gopay is empty and gopay coins in empty and saldo is not empty`(){
        val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)
        val getWalletBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
        val getSaldoUseCase = mockk<GetSaldoUseCase>(relaxed = true)

        val getProfileDataUseCase = createProfileDataUseCase(
            getWalletEligibilityUseCase = getWalletEligibilityUseCase,
            getWalletAppBalanceUseCase = getWalletBalanceUseCase,
            getSaldoUseCase = getSaldoUseCase
        )

        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns buildSuccessWalletAppResponse(walletAppAmount = 0, walletCoinAmount = 0)

        coEvery {
            getWalletEligibilityUseCase.executeOnBackground()
        } returns buildEligible()

        coEvery {
            getSaldoUseCase.executeOnBackground()
        } returns buildSuccessSaldoResponse(0)

        val accountDataModel = runBlocking {
            getProfileDataUseCase.executeOnBackground()
        }
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.isWalletAppLinked)
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileWalletAppDataModel.gopayPointsBalance.isEmpty())
        Assert.assertTrue(accountDataModel.profileSaldoDataModel.saldo.isNotEmpty())
        Assert.assertTrue(accountDataModel.profileMembershipDataModel.tokopointPointAmount.isEmpty())
    }
}

private fun buildSuccessTokopointsResponse(pointsAmount: Int): Result<TokopointsStatusFilteredPojo> {
    return Success(TokopointsStatusFilteredPojo(
        TokopointsStatusFilteredPojo.TokopointsStatusFiltered(
            TokopointsStatusFilteredPojo.StatusFilteredData(
                points = TokopointsStatusFilteredPojo.Points(
                    pointsAmount = pointsAmount,
                    pointsAmountStr = pointsAmount.toString(),
                    externalCurrencyAmount = pointsAmount,
                    externalCurrencyAmountStr = pointsAmount.toString()
                )
            )
        )
    ))
}

private fun buildErrorResponse(): Result<Nothing> {
    return Fail(Throwable())
}

private fun buildEligible(): WalletStatus {
    return WalletStatus(isGoPointsEligible = true)
}

private fun buildNotEligible(): WalletStatus {
    return WalletStatus(isGoPointsEligible = false)
}

private fun buildSuccessSaldoResponse(saldoAmount: Int): Result<SaldoPojo> {
    return Success(SaldoPojo(
        saldo = SaldoPojo.Saldo(
            buyerHold = saldoAmount.toLong(),
            buyerHoldFmt = saldoAmount.toString(),
            buyerUsable = saldoAmount.toLong(),
            buyerUsableFmt = saldoAmount.toString()
        )
    ))
}

private fun buildSuccessWalletAppResponse(walletAppAmount: Int, walletCoinAmount: Int = 99, linked: Boolean = true): WalletAppData {
    return WalletAppData(
        walletappGetBalance = WalletappGetBalance(
            balances = listOf(
                Balances(
                    activationCta = "Test",
                    isLinked = linked,
                    walletName = "Gopay",
                    balance = listOf(
                        Balance(
                            active = true,
                            amount = walletAppAmount,
                            amountFmt = walletAppAmount.toString(),
                            walletCode = "PEMUDA"
                        ),
                        Balance(
                            active = true,
                            amount = walletCoinAmount,
                            amountFmt = walletCoinAmount.toString(),
                            walletCode = "PEMUDAPOINTS"
                        )
                    ),
                    globalMenuText = GlobalMenuText(
                        id = "test"
                    )
                )
            )
        )
    )
}

private fun buildSuccessWalletAppWithEmptyCoinsResponse(walletAppAmount: Int): WalletAppData {
    return WalletAppData(
        walletappGetBalance = WalletappGetBalance(
            balances = listOf(
                Balances(
                    activationCta = "Test",
                    isLinked = true,
                    walletName = "Gopay",
                    balance = listOf(
                        Balance(
                            active = true,
                            amount = walletAppAmount,
                            amountFmt = walletAppAmount.toString(),
                            walletCode = "PEMUDA"
                        )
                    ),
                    globalMenuText = GlobalMenuText(
                        id = "test"
                    )
                )
            )
        )
    )
}

private fun buildSuccessWalletAppWithEmptyGopayResponse(walletAppAmount: Int): WalletAppData {
    return WalletAppData(
        walletappGetBalance = WalletappGetBalance(
            balances = listOf(
                Balances(
                    activationCta = "Test",
                    isLinked = true,
                    walletName = "Gopay",
                    balance = listOf(
                        Balance(
                            active = true,
                            amount = walletAppAmount,
                            amountFmt = walletAppAmount.toString(),
                            walletCode = "PEMUDAPOINTS"
                        )
                    ),
                    globalMenuText = GlobalMenuText(
                        id = "test"
                    )
                )
            )
        )
    )
}

private fun buildSuccessWalletAppWithEmptyBalanceResponse(): WalletAppData {
    return WalletAppData(
        walletappGetBalance = WalletappGetBalance(
            balances = listOf(
                Balances(
                    activationCta = "Test",
                    isLinked = true,
                    walletName = "Gopay",
                    balance = listOf(),
                    globalMenuText = GlobalMenuText(
                        id = "test"
                    )
                )
            )
        )
    )
}