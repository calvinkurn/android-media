package com.tokopedia.wallet.ovoactivation.view

import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.wallet.ovoactivation.domain.CheckNumberOvoUseCase
import com.tokopedia.wallet.ovoactivation.provider.WalletTestScheduler
import com.tokopedia.wallet.ovoactivation.view.model.CheckPhoneOvoModel
import com.tokopedia.wallet.ovoactivation.view.model.ErrorModel
import com.tokopedia.wallet.ovoactivation.view.model.PhoneActionModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable

class IntroOvoPresenterTest {

    private var view: IntroOvoContract.View = mockk(relaxed = true)

    private var checkNumberOvoUseCase: CheckNumberOvoUseCase = mockk()
    private var getWalletBalanceUseCase: GetWalletBalanceUseCase = mockk()

    private lateinit var presenter: IntroOvoPresenter
    private var checkNotRegisteredPhoneOvoModel: CheckPhoneOvoModel? = null

    @Before
    fun setUp() {
        presenter =
            IntroOvoPresenter(checkNumberOvoUseCase, getWalletBalanceUseCase, WalletTestScheduler())
        presenter.attachView(view)
    }

    @Test
    fun checkPhoneNumber_UserAllowAndRegistered_Success() {
        // given
        val checkPhoneOvoModel = CheckPhoneOvoModel().apply {
            isAllow = true
            isRegistered = true
        }

        every { checkNumberOvoUseCase.createObservable(any()) } returns Observable.just(
            checkPhoneOvoModel
        )

        // when
        presenter.checkPhoneNumber()

        // then
        verifyOrder {
            view.removeTokoCashCache()
            view.directPageWithApplink(checkPhoneOvoModel.registeredApplink)
        }
    }

    @Test
    fun checkPhoneNumber_UserAllowAndNotRegistered_Success() {
        // given
        val checkPhoneOvoModel = CheckPhoneOvoModel().apply {
            isAllow = true
            isRegistered = false
            registeredApplink = "tokopedia://webview?url=https://www.tokopedia.com/api/v1/activate"
            notRegisteredApplink = "tokopedia://ovo/activation"
            changeMsisdnApplink = "https://m.tokopedia.com/user/profile/edit"
        }

        every { checkNumberOvoUseCase.createObservable(any()) } returns Observable.just(
            checkPhoneOvoModel
        )

        // when
        presenter.checkPhoneNumber()

        // then
        verifyOrder {
            view.removeTokoCashCache()
            view.directPageWithExtraApplink(
                checkPhoneOvoModel.notRegisteredApplink,
                checkPhoneOvoModel.registeredApplink,
                checkPhoneOvoModel.phoneNumber,
                checkPhoneOvoModel.changeMsisdnApplink
            )
        }
    }

    @Test
    fun checkPhoneNumber_UserNotAllowAndMessageIsNotEmpty_Success() {
        // given
        val checkPhoneOvoModel = CheckPhoneOvoModel().apply {
            isAllow = false
            errorModel = ErrorModel().apply {
                message = "Error message"
            }
        }

        every { checkNumberOvoUseCase.createObservable(any()) } returns Observable.just(
            checkPhoneOvoModel
        )

        // when
        presenter.checkPhoneNumber()

        // then
        verifyOrder {
            view.removeTokoCashCache()
            view.showSnackbarErrorMessage(checkPhoneOvoModel.errorModel?.message ?: "")
        }
    }

    @Test
    fun checkPhoneNumber_UserNotAllowAndMessageIsEmpty_Success() {
        // given
        val checkPhoneOvoModel = CheckPhoneOvoModel().apply {
            isAllow = false
            errorModel = ErrorModel().apply {
                message = ""
            }
            phoneActionModel = PhoneActionModel()
        }

        every { checkNumberOvoUseCase.createObservable(any()) } returns Observable.just(
            checkPhoneOvoModel
        )

        // when
        presenter.checkPhoneNumber()

        // then
        verifyOrder {
            view.removeTokoCashCache()
            view.showDialogErrorPhoneNumber(checkPhoneOvoModel.phoneActionModel)
        }
    }

    @Test
    fun checkPhoneNumber_Error() {
        // given
        val exception = MessageErrorException("Error")
        every { checkNumberOvoUseCase.createObservable(any()) } returns Observable.error(exception)

        // when
        presenter.checkPhoneNumber()

        // then
        verifyOrder {
            view.hideProgressBar()
            view.getErrorMessage(exception)
            view.showSnackbarErrorMessage(any())
        }
    }

    @Test
    fun getBalanceWallet_Success() {
        // given
        val walletBalanceModel = WalletBalanceModel().apply {
            helpApplink = "tokopedia://ovo"
            tncApplink = "tokopedia://ovo"
        }
        every { getWalletBalanceUseCase.createObservable(any()) } returns Observable.just(
            walletBalanceModel
        )

        // when
        presenter.getBalanceWallet()

        // then
        verifyOrder {
            view.setApplinkButton(walletBalanceModel.helpApplink, walletBalanceModel.tncApplink)
        }
    }

    @Test
    fun getBalanceWallet_Error() {
        // given
        val exception = MessageErrorException("Error")
        every { getWalletBalanceUseCase.createObservable(any()) } returns Observable.error(
            exception
        )

        // when
        presenter.getBalanceWallet()

        // then
        verifyOrder {
            view.getErrorMessage(exception)
            view.showSnackbarErrorMessage(any())
        }
    }

    @Test
    fun detachViewTest() {
        // given
        val subsciption = Observable.just(true).subscribe()
        presenter.compositeSubscription.add(subsciption)

        // when
        presenter.onDestroyView()

        // then
        val hasSubscription = presenter.compositeSubscription.hasSubscriptions()
        Assert.assertEquals(false, hasSubscription)
    }
}
