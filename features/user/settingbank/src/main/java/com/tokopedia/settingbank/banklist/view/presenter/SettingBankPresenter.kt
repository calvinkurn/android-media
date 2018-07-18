package com.tokopedia.settingbank.banklist.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.settingbank.banklist.domain.usecase.DeleteBankAccountUseCase
import com.tokopedia.settingbank.banklist.domain.usecase.GetBankAccountListUseCase
import com.tokopedia.settingbank.banklist.domain.usecase.SetDefaultBankAccountUseCase
import com.tokopedia.settingbank.banklist.view.listener.SettingBankContract
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankPresenter(private val userSession: UserSession,
                           private val getBankAccountUseCase: GetBankAccountListUseCase,
                           private val setDefaultBankAccountUseCase: SetDefaultBankAccountUseCase,
                           private val deleteBankAccountUseCase: DeleteBankAccountUseCase) :
        SettingBankContract.Presenter,
        BaseDaggerPresenter<SettingBankContract.View>() {

    var page = 1

    override fun getBankListFirstTime() {
        view.showLoadingFull()
        getBankAccountUseCase.execute(GetBankAccountListUseCase.getParam(
                userSession.userId,
                page
        ), object : Subscriber<BankAccountListViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoadingFull()
                view.onErrorGetListBankFirstTime(ErrorHandler.getErrorMessage(view.getContext(), e))
            }

            override fun onNext(bankAccountList: BankAccountListViewModel) {
                view.hideLoadingFull()
                if (bankAccountList.list?.isNotEmpty()!!) {
                    page++
                    view.onSuccessGetListBank(bankAccountList)
                } else {
                    view.onEmptyList(bankAccountList.enableAddButton, bankAccountList.reason)
                }
            }
        })
    }

    override fun loadMore() {
        view.showLoadingList()
        getBankAccountUseCase.execute(GetBankAccountListUseCase.getParam(
                userSession.userId,
                page
        ), object : Subscriber<BankAccountListViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoadingList()
                view.onErrorGetListBank(ErrorHandler.getErrorMessage(view.getContext(), e))
            }

            override fun onNext(bankAccountList: BankAccountListViewModel) {
                view.hideLoadingList()
                if (bankAccountList.list?.isNotEmpty()!!) {
                    page += 1
                    view.onSuccessGetListBank(bankAccountList)
                }
            }
        })
    }

    override fun setMainAccount(adapterPosition: Int, element: BankAccountViewModel?) {
        view.showLoadingDialog()
        if (element != null) {
            setDefaultBankAccountUseCase.execute(SetDefaultBankAccountUseCase.getParam(
                    userSession.userId,
                    element.accountId!!,
                    userSession.deviceId
            ), object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingDialog()
                    view.onErrorSetDefaultBank(ErrorHandler.getErrorMessage(view.getContext(), e))
                }

                override fun onNext(isSuccess: Boolean) {
                    view.hideLoadingDialog()
                    if(isSuccess) {
                        view.onSuccessSetDefault(adapterPosition)
                    }else{
                        view.onErrorSetDefaultBank("")

                    }
                }
            })
        }
    }

    override fun deleteAccount(adapterPosition: Int, element: BankAccountViewModel?) {
        view.showLoadingDialog()
        if (element != null) {
            deleteBankAccountUseCase.execute(DeleteBankAccountUseCase.getParam(
                    userSession.userId,
                    element.accountId!!,
                    userSession.deviceId
            ), object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingDialog()
                    view.onErrorDeleteAccount(ErrorHandler.getErrorMessage(view.getContext(), e))
                }

                override fun onNext(isSuccess: Boolean) {
                    view.hideLoadingDialog()
                    if (isSuccess)
                        view.onSuccessDeleteAccount(adapterPosition)
                    else
                        view.onErrorDeleteAccount("")

                }
            })

        }
    }

    override fun detachView() {
        super.detachView()
        getBankAccountUseCase.unsubscribe()
        setDefaultBankAccountUseCase.unsubscribe()
        deleteBankAccountUseCase.unsubscribe()
    }

}