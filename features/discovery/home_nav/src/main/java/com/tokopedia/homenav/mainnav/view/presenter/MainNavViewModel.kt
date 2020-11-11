package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.domain.model.NotificationResolutionModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_OPEN_SHOP_TICKER
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_RECENT_VIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_SUBSCRIPTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.TransactionListItemViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val getSaldoUseCase: Lazy<GetSaldoUseCase>,
        private val getMainNavDataUseCase: Lazy<GetMainNavDataUseCase>,
        private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
        private val getResolutionNotification: Lazy<GetResolutionNotification>,
        private val getUohOrdersNavUseCase: Lazy<GetUohOrdersNavUseCase>,
        private val getPaymentOrdersNavUseCase: Lazy<GetPaymentOrdersNavUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6
    }

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(MainNavigationDataModel())
    private var _mainNavListVisitable = mutableListOf<Visitable<*>>()

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

    private var resolutionNotification: NotificationResolutionModel = NotificationResolutionModel(unreadCount = 0)

    init {
        getMainNavData()
        getNotification()
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun updateWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList[position] = visitable
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        val newMainNavList = _mainNavListVisitable
        if (position == null) {
            newMainNavList.add(visitable)
        } else {
            newMainNavList.add(position, visitable)
        }
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun addWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.addAll(visitables)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidget(visitable: HomeNavVisitable, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.removeAt(position)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    suspend fun updateNavData(navigationDataModel: MainNavigationDataModel) {
        try {
            _mainNavListVisitable = navigationDataModel.dataList.toMutableList()
            _mainNavLiveData.postValue(navigationDataModel.copy(dataList = _mainNavListVisitable))
        } catch (e: Exception) {
            Timber.d("Update nav data failed")
            e.printStackTrace()
        }
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    private fun getMainNavData() {
        launch {
            val p1DataJob = launchCatchError(context = coroutineContext, block = {
                getMainNavContent()
                getUserSection()
            }) {
                Timber.d("P1 error")
                it.printStackTrace()
            }
            p1DataJob.join()

            val p2DataJob = launchCatchError(context = coroutineContext, block = {
                getOngoingTransaction()
                getTransactionMenu()
                getUserMenu()
            }) {
                Timber.d("P2 error")
                it.printStackTrace()
            }
            p2DataJob.join()
        }
    }

    private suspend fun getMainNavContent() {
        val result = getMainNavDataUseCase.get().executeOnBackground()
        updateNavData(result)
    }

    private suspend fun getUserMenu() {
        addWidgetList(buildUserMenuList())
    }

    private suspend fun getTransactionMenu() {
        addWidgetList(buildTransactionMenuList())
    }

    private suspend fun getOngoingTransaction() {
        launchCatchError(coroutineContext, block = {
            val paymentList = async { getPaymentOrdersNavUseCase.get().executeOnBackground() }.await()
            val orderList = async { getUohOrdersNavUseCase.get().executeOnBackground() }.await()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty()) {
                val othersTransactionCount = paymentList.size - 6
                val paymentListToShow = paymentList.take(ON_GOING_TRANSACTION_TO_SHOW)
                val transactionListItemViewModel = TransactionListItemViewModel(
                        NavOrderListModel(orderList, paymentListToShow), othersTransactionCount)

                val firstTransactionMenu = _mainNavListVisitable.find {
                    it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.ORDER
                }
                val indexOfFirstTransactionMenu = _mainNavListVisitable.indexOf(firstTransactionMenu)
                addWidget(transactionListItemViewModel, indexOfFirstTransactionMenu)
            }
        }){
            it.printStackTrace()
        }
    }

    private fun buildUserMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            val firstSectionList = mutableListOf<Visitable<*>>(
                    SeparatorViewModel(),
                    it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_RECENT_VIEW, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_SUBSCRIPTION, sectionId = MainNavConst.Section.USER_MENU)
            )
            val hasShop = userSession.get().hasShop()
            if (!hasShop) firstSectionList.add(it.getTicker(ID_OPEN_SHOP_TICKER))
            firstSectionList.add(SeparatorViewModel())

            val complainNotification = if (resolutionNotification.unreadCount != 0)
                resolutionNotification.unreadCount.toString() else ""

            val secondSectionList = listOf(
                    it.getMenu(menuId = ID_COMPLAIN, notifCount = complainNotification, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_TOKOPEDIA_CARE, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_QR_CODE, sectionId = MainNavConst.Section.USER_MENU)
            )
            val completeList = firstSectionList.plus(secondSectionList)
            return completeList
        }
        return listOf()
    }

    private fun buildTransactionMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            val visitableList = mutableListOf<Visitable<*>>(
                    SeparatorViewModel(),
                    it.getMenu(ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(ID_TICKET, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(ID_REVIEW, sectionId = MainNavConst.Section.ORDER)
            )
            return visitableList
        }
        return listOf()
    }

    private suspend fun getUserSection(){
        launchCatchError(coroutineContext, block = {
            val mainNavigationDataModel: MainNavigationDataModel? = _mainNavLiveData.value
            mainNavigationDataModel?.dataList?.find { it is AccountHeaderViewModel }?.let {
                val accountHeader = (it as? AccountHeaderViewModel ?: AccountHeaderViewModel()).copy()
                if (accountHeader.loginState == AccountHeaderViewModel.LOGIN_STATE_LOGIN) {
                    getUserBadgeImage(accountHeader)
                    getOvoData(accountHeader)
                    getSaldoData(accountHeader)
                    getShopData(accountHeader.shopId.toInt(), accountHeader)
                }
            }
        }) {
            it.printStackTrace()
        }
    }

    private suspend fun getShopData(shopId: Int, accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            val newData = accountData.copy()
            newData.shopName = result.shopCore.name
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
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
            updateWidget(accountData.copy(), INDEX_MODEL_ACCOUNT)
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
            updateWidget(accountData.copy(), INDEX_MODEL_ACCOUNT)
        }){
            //post error get ovo with new livedata

            val newAccountData = accountData.copy(
                    ovoSaldo = AccountHeaderViewModel.ERROR_TEXT,
                    ovoPoint = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, INDEX_MODEL_ACCOUNT)
        }
    }

    private suspend fun getSaldoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getSaldoUseCase.get().executeOnBackground()
            }
            accountData.saldo = convertPriceValueToIdrFormat(result.saldo.buyerUsable + result.saldo.sellerUsable, false) ?: ""
            updateWidget(accountData.copy(), INDEX_MODEL_ACCOUNT)
        }){
            val newAccountData = accountData.copy(
                    saldo = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, INDEX_MODEL_ACCOUNT)
        }
    }

    private fun getNotification() {
        launchCatchError(coroutineContext, block = {
            val result = getResolutionNotification.get().executeOnBackground()
            if (result.unreadCount != 0) {
                resolutionNotification = result
                val findExistingResolutionMenu = _mainNavListVisitable.find {
                    it is HomeNavVisitable && it.id() == ID_COMPLAIN
                }
                findExistingResolutionMenu?.let {
                    val indexOfExistingResolutionMenu = _mainNavListVisitable.indexOf(it)
                    (findExistingResolutionMenu as? HomeNavMenuViewModel)?.notifCount = resolutionNotification?.unreadCount.toString()
                    updateWidget(findExistingResolutionMenu, indexOfExistingResolutionMenu)
                }
            }
        }) {
            it.printStackTrace()
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