package com.tokopedia.settingbank.choosebank.view.presenter


import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.settingbank.addeditaccount.view.listener.ChooseBankContract
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListDBUseCase
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListWSUseCase
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 7/2/18.
 */
class ChooseBankPresenter @Inject constructor(private val userSession: UserSessionInterface,
                          private val getBankListDBUseCase: GetBankListDBUseCase,
                          private val getBankListWSUseCase: GetBankListWSUseCase,
                          private val bankCache: LocalCacheHandler) :
        ChooseBankContract.Presenter,
        BaseDaggerPresenter<ChooseBankContract.View>() {

    private val BANK_CACHE_DURATION = 86400
    var currentPage = 1
    var hasNextPage = true
    var listBank = ArrayList<BankViewModel>()

    override fun getBankList() {
        currentPage = 1
        view.showLoading()
        getBankListFromDB()

    }

    private fun getBankListFromDB() {
        getBankListDBUseCase.execute(GetBankListDBUseCase.getParam(""),
                object : Subscriber<BankListViewModel>
                () {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.hideLoading()
                        val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                        view.onErrorGetBankList(errorMessage)
                    }

                    override fun onNext(listBankViewModel: BankListViewModel) {

                        if (!bankCache.isExpired
                                && listBankViewModel.list != null
                                && !listBankViewModel.list.isEmpty()) {
                            view.hideLoading()
                            view.onSuccessGetBankList(listBankViewModel.list)
                        } else {
                            getBankListFromWS()
                        }

                    }
                })
    }


    private fun getBankListFromWS() {
        getBankListWSUseCase.execute(GetBankListWSUseCase.getParam("", currentPage,
                userSession.userId),
                object : Subscriber<BankListViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        currentPage = 1
                        view.hideLoading()
                        val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                        view.onErrorGetBankList(errorMessage)
                    }

                    override fun onNext(listBankViewModel: BankListViewModel) {
                        if (currentPage == 1
                                && listBankViewModel.list != null
                                && listBankViewModel.list.isEmpty()) {
                            view.hideLoading()
                            view.onErrorGetBankList("")
                        } else if (listBankViewModel.list != null
                                && !listBankViewModel.list.isEmpty()) {
                            currentPage += 1
                            hasNextPage = listBankViewModel.hasNextPage
                            listBank.addAll(listBankViewModel.list)

                            if (!hasNextPage) {
                                bankCache.setExpire(BANK_CACHE_DURATION)
                                view.hideLoading()
                                view.onSuccessGetBankList(listBank)
                            } else {
                                getBankListFromWS()
                            }
                        } else {
                            view.hideLoading()
                            view.onErrorGetBankList("")
                        }
                    }
                })

    }

    override fun searchBank(query: String) {
        getBankListDBUseCase.execute(GetBankListDBUseCase.getParam(query),
                object : Subscriber<BankListViewModel>
                () {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.hideLoading()
                        val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                        view.onErrorGetBankList(errorMessage)
                    }

                    override fun onNext(listBankViewModel: BankListViewModel) {

                        if (listBankViewModel.list != null
                                && !listBankViewModel.list.isEmpty()) {
                            view.onSuccessSearchBank(listBankViewModel.list)
                        } else {
                            view.onEmptySearchBank()
                        }

                    }
                })
    }

    override fun detachView() {
        super.detachView()
        getBankListDBUseCase.unsubscribe()
        getBankListWSUseCase.unsubscribe()
    }
}