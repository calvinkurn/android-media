package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.NavCommandProcessor
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.common.util.UpdateWidgetCommand
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val getSaldoUseCase: Lazy<GetSaldoUseCase>,
        private val navProcessor: Lazy<NavCommandProcessor>,
        private val getMainNavDataUseCase: Lazy<GetMainNavDataUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    private var mainNavLiveDataController: MainNavLiveDataController


    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData()

    val businessListLiveData: LiveData<Result<List<HomeNavVisitable>>>
        get() = _businessListLiveData
    private val _businessListLiveData: MutableLiveData<Result<List<HomeNavVisitable>>> = MutableLiveData()

    val accountLiveData: LiveData<Result<AccountHeaderViewModel>>
        get() = _accountLiveData
    private val _accountLiveData: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    val profileResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _profileResultListener
    private val _profileResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    val membershipResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _membershipResultListener
    private val _membershipResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    val ovoResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _ovoResultListener
    private val _ovoResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    val saldoResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _saldoResultListener
    private val _saldoResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    val shopResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _shopResultListener
    private val _shopResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    init {
        getMainNavData()
        mainNavLiveDataController = MainNavLiveDataController(_mainNavLiveData, baseDispatcher)
    }

    private fun getMainNavData() {
        launchCatchError(coroutineContext, block = {
           val result = getMainNavDataUseCase.get().executeOnBackground()
            _mainNavLiveData.postValue(result)
            getUserSection(result)
        }){
            //apply global error for mainnav
        }
    }



    private fun getUserSection(mainNavigationDataModel: MainNavigationDataModel){
        mainNavigationDataModel.dataList.find { it is AccountHeaderViewModel }?.let {
            val accountHeader = (it as AccountHeaderViewModel).copy()
            getUserBadgeImage(accountHeader)
            getOvoData(accountHeader)
            getSaldoData(accountHeader)
            getShopData(userSession.get().shopId.toInt(), accountHeader)
        }
    }

    fun getShopData(shopId: Int, accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            accountData.shopName = result.shopCore.name
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, mainNavLiveDataController))
        }){

            val newAccountData = accountData.copy(
                    shopName = AccountHeaderViewModel.ERROR_TEXT
            )
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newAccountData, 0, mainNavLiveDataController))
        }
    }

    fun getUserBadgeImage(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            accountData.badge = result.tokopoints.status.tier.eggImageURL
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, mainNavLiveDataController))
        }){
            _membershipResultListener.postValue(Fail(it))
        }
    }

    fun getOvoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getWalletUseCase.get().executeOnBackground()
            }
            accountData.ovoSaldo = result.cashBalance
            accountData.ovoPoint = result.pointBalance
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, mainNavLiveDataController))
        }){
            //post error get ovo with new livedata

            val newAccountData = accountData.copy(
                    ovoSaldo = AccountHeaderViewModel.ERROR_TEXT,
                    ovoPoint = AccountHeaderViewModel.ERROR_TEXT
            )
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newAccountData, 0, mainNavLiveDataController))
        }
    }

    fun getSaldoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getSaldoUseCase.get().executeOnBackground()
            }
            accountData.saldo = convertPriceValueToIdrFormat(result.saldo.buyerUsable + result.saldo.sellerUsable, false) ?: ""
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, mainNavLiveDataController))
        }){
            val newAccountData = accountData.copy(
                    saldo = AccountHeaderViewModel.ERROR_TEXT
            )
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newAccountData, 0, mainNavLiveDataController))
        }
    }

    fun reloadMainNavAfterLogin() {
        getMainNavData()
    }

}