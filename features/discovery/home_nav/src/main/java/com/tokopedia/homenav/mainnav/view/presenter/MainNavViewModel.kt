package com.tokopedia.homenav.mainnav.view.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.*
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.launch
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

    init {
        getMainNavData()
    }

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(MainNavigationDataModel())

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

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun updateWidget(visitable: HomeNavVisitable, position: Int) {
        navProcessor.get().sendWithQueueMethod(
                UpdateWidgetCommand(visitable, position) { homeNavVisitable: HomeNavVisitable, position: Int ->
                    val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
                    newMainLiveData[position] = visitable
                    _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainLiveData))
                }
        )
    }

    fun addWidget(visitable: HomeNavVisitable, position: Int) {
        navProcessor.get().sendWithQueueMethod(
                AddWidgetCommand(visitable, position) { homeNavVisitable: HomeNavVisitable, position: Int ->
                    val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
                    newMainLiveData.add(visitable)
                    _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainLiveData))
                }
        )
    }

    suspend fun addWidgetList(visitables: List<HomeNavVisitable>) {
        navProcessor.get().sendWithQueueMethod(
                AddWidgetListCommand(visitables, visitables.lastIndex) { list: List<HomeNavVisitable>, position: Int ->
                    val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
                    newMainLiveData.addAll(visitables)
                    _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainLiveData))
                })
    }

    fun deleteWidget(visitable: HomeNavVisitable, position: Int) {
        navProcessor.get().sendWithQueueMethod(
                DeleteWidgetCommand(visitable, 0) { homeNavVisitable: HomeNavVisitable, position: Int ->
                    val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
                    newMainLiveData.removeAt(position)
                    _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainLiveData))
                })
    }

    suspend fun updateNavData(navigationDataModel: MainNavigationDataModel) {
        navProcessor.get().sendWithQueueMethod(
                UpdateNavigationData(navigationDataModel) { navigationDataModel ->
                    _mainNavLiveData.postValue(navigationDataModel)
                })
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    private fun getMainNavData() {
        launchCatchError(coroutineContext, block = {
            getMainNavContent()
            getUserSection()
//            getUserMenu()
        }){
            //apply global error for mainnav
            Log.d("Error","Error ya")
        }
    }

    private suspend fun getMainNavContent() {
        val result = getMainNavDataUseCase.get().executeOnBackground()
        updateNavData(result)
    }

    private suspend fun getUserMenu() {
        addWidgetList(buildUserMenuList())
    }

    private fun buildUserMenuList(): List<HomeNavVisitable> {
        val ID_USER_MENU = 901
        val ID_FAVORITE_SHOP = 902
        val ID_RECENT_VIEW = 903
        val ID_SUBSCRIPTION = 904
        val ID_COMPLAIN = 905
        val ID_TOKOPEDIA_CARE = 906
        val ID_QR_CODE = 907

        val userMenuWishlist = HomeNavMenuViewModel(
                id = ID_USER_MENU,
                srcIconId = IconUnify.BELL,
                itemTitle = "Test"
        )
        val userMenuWishlist1 = HomeNavMenuViewModel(
                id = ID_USER_MENU,
                srcIconId = IconUnify.BELL,
                itemTitle = "Test"
        )
        val userMenuWishlist2 = HomeNavMenuViewModel(
                id = ID_USER_MENU,
                srcIconId = IconUnify.BELL,
                itemTitle = "Test"
        )
        val userMenuWishlist3 = HomeNavMenuViewModel(
                id = ID_USER_MENU,
                srcIconId = IconUnify.BELL,
                itemTitle = "Test"
        )

        return listOf(
                userMenuWishlist,
                userMenuWishlist1,
                userMenuWishlist2,
                userMenuWishlist3
        )
    }

    private suspend fun getUserSection(){
        val mainNavigationDataModel: MainNavigationDataModel? = _mainNavLiveData.value
        mainNavigationDataModel?.dataList?.find { it is AccountHeaderViewModel }?.let {
            val accountHeader = (it as AccountHeaderViewModel).copy()
            if (accountHeader.loginState.equals(AccountHeaderViewModel.LOGIN_STATE_LOGIN)) {
                getUserBadgeImage(accountHeader)
                getOvoData(accountHeader)
                getSaldoData(accountHeader)
                getShopData(accountHeader.shopId.toInt(), accountHeader)
            }
        }
    }

    private suspend fun getShopData(shopId: Int, accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            accountData.shopName = result.shopCore.name
            updateWidget(accountData.copy(), 0)
        }){
            _shopResultListener.postValue(Fail(it))
        }
    }

    private suspend fun getUserBadgeImage(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            accountData.badge = result.tokopoints.status.tier.eggImageURL
            updateWidget(accountData.copy(), 0)
        }){
            _membershipResultListener.postValue(Fail(it))
        }
    }

    private suspend fun getOvoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getWalletUseCase.get().executeOnBackground()
            }
            accountData.ovoSaldo = result.cashBalance
            accountData.ovoPoint = result.pointBalance
            updateWidget(accountData.copy(), 0)
        }){
            //post error get ovo with new livedata

            val newAccountData = accountData.copy(
                    ovoSaldo = AccountHeaderViewModel.ERROR_TEXT,
                    ovoPoint = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, 0)
        }
    }

    private suspend fun getSaldoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getSaldoUseCase.get().executeOnBackground()
            }
            accountData.saldo = convertPriceValueToIdrFormat(result.saldo.buyerUsable + result.saldo.sellerUsable, false) ?: ""
            updateWidget(accountData.copy(), 0)
        }){
            val newAccountData = accountData.copy(
                    saldo = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, 0)
        }
    }

    fun reloadMainNavAfterLogin() {
        getMainNavData()
    }

    fun reloadOvoData(accountData: AccountHeaderViewModel) {
        launch(coroutineContext, block = {
            getOvoData(accountData)
        })
    }

    fun reloadSaldoData(accountData: AccountHeaderViewModel) {
        launch(coroutineContext, block = {
            getSaldoData(accountData)
        })
    }

    fun reloadShopData(shopId: Int,accountData: AccountHeaderViewModel) {
        launch(coroutineContext, block = {
            getShopData(shopId, accountData)
        })
    }

}