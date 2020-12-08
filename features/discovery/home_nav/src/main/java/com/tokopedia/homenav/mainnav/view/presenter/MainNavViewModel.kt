package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_OPEN_SHOP_TICKER
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_RECENT_VIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_SUBSCRIPTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val getSaldoUseCase: Lazy<GetSaldoUseCase>,
        private val getCategoryGroupUseCase: Lazy<GetCategoryGroupUseCase>,
        private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
        private val getNavNotification: Lazy<GetNavNotification>,
        private val getUohOrdersNavUseCase: Lazy<GetUohOrdersNavUseCase>,
        private val getPaymentOrdersNavUseCase: Lazy<GetPaymentOrdersNavUseCase>,
        private val getProfileDataUseCase: Lazy<GetProfileDataUseCase>,
        private val getProfileDataCacheUseCase: Lazy<GetProfileDataUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6
        private const val INDEX_START_BU_MENU = 1
    }

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(MainNavigationDataModel())
    private var _mainNavListVisitable = mutableListOf<Visitable<*>>()

    val onboardingListLiveData: LiveData<List<Visitable<*>>>
        get() = _onboardingListLiveData
    private val _onboardingListLiveData: MutableLiveData<List<Visitable<*>>> = MutableLiveData()

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

    //network process live data, false if it is processing and true if it is finished
    val networkProcessLiveData: LiveData<Boolean>
        get() = _networkProcessLiveData
    private val _networkProcessLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var navNotification: NavNotificationModel = NavNotificationModel(
            unreadCountInboxTicket = 0,
            unreadCountComplain = 0
    )

    private var pageSource: String = ""

    init {
        setInitialState()
        getMainNavData()
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun updateWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList[position] = visitable
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    fun updateWidgetList(visitables: List<Visitable<*>>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.removeAt(position)
        newMainNavList.addAll(position, visitables)
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

    fun deleteWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.removeAt(position)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidget(visitable: Visitable<*>) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.remove(visitable)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.removeAll(visitables)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun setPageSource(pageSource: String = "") {
        this.pageSource = pageSource
        if (pageSource == ApplinkConsInternalNavigation.SOURCE_HOME) removeHomeBackButtonMenu()
    }

    fun setOnboardingSuccess(isSuccess: Boolean) {
        if (!isSuccess) {
            launch {
                delay(500)
                _onboardingListLiveData.postValue(_mainNavListVisitable)
            }
        }
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

    private fun setInitialState() {
        val initialList = mutableListOf<Visitable<*>>(
                InitialShimmerProfileDataModel(),
                InitialShimmerDataModel()
        )
        initialList.addHomeBackButtonMenu()
        initialList.addTransactionMenu()
        initialList.addUserMenu()
        addWidgetList(initialList)
    }

    private fun removeInitialStateData() {
        launch{
            _mainNavListVisitable.find { it is InitialShimmerDataModel }?.let {
                deleteWidget(it)
            }
        }
    }

    private fun getMainNavData() {
        _networkProcessLiveData.value = false
        launch {
            val p1DataJob = launchCatchError(context = coroutineContext, block = {
                getProfileDataCached()
                getBuListMenu()
            }) {
                Timber.d("P1 error")
                it.printStackTrace()
            }
            p1DataJob.join()

            val p2DataJob = launchCatchError(context = coroutineContext, block = {
                onlyForLoggedInUser {
                    getOngoingTransaction()
                    getNotification()
                }
            }) {
                Timber.d("P2 error")
                it.printStackTrace()
            }
            p2DataJob.join()
            _onboardingListLiveData.postValue(_mainNavListVisitable)

            //update cached data with cloud data
            updateProfileData()
        }
    }

    private fun MutableList<Visitable<*>>.addHomeBackButtonMenu() {
        if (pageSource != ApplinkConsInternalNavigation.SOURCE_HOME) {
            this.add(1, SeparatorViewModel(sectionId = MainNavConst.Section.HOME))
            this.add(2, clientMenuGenerator.get().getMenu(menuId = ID_HOME, sectionId = MainNavConst.Section.HOME))
        }
    }

    private fun MutableList<Visitable<*>>.addTransactionMenu() {
        this.addAll(buildTransactionMenuList())
    }

    private fun MutableList<Visitable<*>>.addUserMenu() {
        this.addAll(buildUserMenuList())
    }

    private fun removeHomeBackButtonMenu() {
        val listOfHomeMenuSection = _mainNavListVisitable.filter {
            (it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.HOME) ||
                    (it is SeparatorViewModel && it.sectionId == MainNavConst.Section.HOME)
        }
        deleteWidgetList(listOfHomeMenuSection)
    }

    private suspend fun getBuListMenu() {
        launchCatchError(coroutineContext, block = {
            getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
            val result = getCategoryGroupUseCase.get().executeOnBackground()

            //PLT network process is finished
            _networkProcessLiveData.postValue(true)

            updateWidgetList(result, INDEX_START_BU_MENU)
        }) {
            it.printStackTrace()
            updateWidget(ErrorStateBuViewModel(), INDEX_START_BU_MENU)
        }
    }

    suspend fun getProfileDataCached() {
        getProfileDataCacheUseCase.get().setCallCacheData()
        val accountHeaderModel = getProfileDataCacheUseCase.get().executeOnBackground()
        updateWidget(accountHeaderModel, 0)
    }

    suspend fun updateProfileData() {
        val accountHeaderModel = getProfileDataUseCase.get().executeOnBackground()
        updateWidget(accountHeaderModel, 0)
    }

    fun refreshBuListdata() {
        updateWidget(InitialShimmerDataModel(), INDEX_START_BU_MENU)
        launchCatchError(coroutineContext, block = {
            getBuListMenu()
        }) {

        }
    }

    private suspend fun getOngoingTransaction() {
        launchCatchError(coroutineContext, block = {
            val paymentList = async { getPaymentOrdersNavUseCase.get().executeOnBackground() }.await()
            val orderList = async { getUohOrdersNavUseCase.get().executeOnBackground() }.await()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty()) {
                val othersTransactionCount = orderList.size - 6
                val orderListToShow = orderList.take(ON_GOING_TRANSACTION_TO_SHOW)
                val transactionListItemViewModel = TransactionListItemViewModel(
                        NavOrderListModel(orderListToShow, paymentList), othersTransactionCount)

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
            val showOpenShopTicker = userSession.get().isLoggedIn && !userSession.get().hasShop()
            if (showOpenShopTicker) firstSectionList.add(it.getTicker(ID_OPEN_SHOP_TICKER))
            firstSectionList.add(SeparatorViewModel())

            val complainNotification = if (navNotification.unreadCountComplain.isMoreThanZero())
                navNotification.unreadCountComplain.toString() else ""

            val inboxTicketNotification = if (navNotification.unreadCountInboxTicket.isMoreThanZero())
                navNotification.unreadCountInboxTicket.toString() else ""

            val secondSectionList = listOf(
                    it.getMenu(menuId = ID_COMPLAIN, notifCount = complainNotification, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_TOKOPEDIA_CARE, notifCount = inboxTicketNotification, sectionId = MainNavConst.Section.USER_MENU),
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
            val call = async {
                withContext(baseDispatcher.get().io()) {
                    getShopInfoUseCase.get().executeOnBackground()
                }
            }
            val newData = accountData.copy()
            val result = (call.await().takeIf { it is Success } as? Success<ShopInfoPojo>)?.data
            result?.let {
                newData.setUserShopName(it.info.shopName, it.info.shopId)
            }
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
        }){
            _shopResultListener.postValue(Fail(it))
        }
    }

    private suspend fun getUserBadgeImage(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val call = async {
                withContext(baseDispatcher.get().io()) {
                    getUserMembershipUseCase.get().executeOnBackground()
                }
            }
            val newData = accountData.copy()
            val result = (call.await().takeIf { it is Success } as? Success<MembershipPojo>)?.data
            result?.let {
                newData.setUserBadge(it.tokopoints.status.tier.imageUrl)
            }
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
        }){
            _membershipResultListener.postValue(Fail(it))
        }
    }

    private suspend fun getUserData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val call = async {
                withContext(baseDispatcher.get().io()) {
                    getUserInfoUseCase.get().executeOnBackground()
                }
            }
            val newData = accountData.copy()
            val result = (call.await().takeIf { it is Success } as? Success<UserPojo>)?.data
            result?.let {
                newData.setProfileData(it.profile.name, it.profile.profilePicture, AccountHeaderViewModel.LOGIN_STATE_LOGIN)
            }
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
        }){
            //post error get ovo with new livedata

            val newAccountData = accountData.copy(
                    ovoSaldo = AccountHeaderViewModel.ERROR_TEXT,
                    ovoPoint = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, INDEX_MODEL_ACCOUNT)
        }
    }

    private suspend fun getOvoData(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val call = async {
                withContext(baseDispatcher.get().io()) {
                    getWalletUseCase.get().executeOnBackground()
                }
            }
            val newData = accountData.copy()
            val result = (call.await().takeIf { it is Success } as? Success<WalletBalanceModel>)?.data
            result?.let {
                newData.setWalletData(it.cashBalance, it.pointBalance)
            }
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
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
            val call =  async {
                withContext(baseDispatcher.get().io()) {
                    getSaldoUseCase.get().executeOnBackground()
                }
            }
            val newData = accountData.copy()
            val result = (call.await().takeIf { it is Success } as? Success<SaldoPojo>)?.data
            result?.let {
                newData.setSaldoData(
                        convertPriceValueToIdrFormat(result.saldo.buyerUsable + result.saldo.sellerUsable, false) ?: "")
            }
            updateWidget(newData, INDEX_MODEL_ACCOUNT)
        }){
            val newAccountData = accountData.copy(
                    saldo = AccountHeaderViewModel.ERROR_TEXT
            )
            updateWidget(newAccountData, INDEX_MODEL_ACCOUNT)
        }
    }

    private fun getNotification() {
        launchCatchError(coroutineContext, block = {
            val result = getNavNotification.get().executeOnBackground()
            val complainNotification = result.unreadCountComplain
            val inboxTicketNotification = result.unreadCountInboxTicket
            navNotification = NavNotificationModel(
                    unreadCountComplain = complainNotification,
                    unreadCountInboxTicket = inboxTicketNotification
            )
            if (complainNotification.isMoreThanZero()) _mainNavListVisitable.findMenu(ID_COMPLAIN)?.updateBadgeCounter(complainNotification.toString())
            if (inboxTicketNotification.isMoreThanZero()) _mainNavListVisitable.findMenu(ID_TOKOPEDIA_CARE)?.updateBadgeCounter(inboxTicketNotification.toString())
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

    fun reloadUserData(accountData: AccountHeaderViewModel) {
        launch(coroutineContext, block = {
            getUserData(accountData)
        })
    }

    private suspend fun onlyForLoggedInUser(function: suspend ()-> Unit) {
        if (userSession.get().isLoggedIn) function.invoke()
    }

    private fun List<Visitable<*>>.findMenu(menuId: Int): HomeNavMenuViewModel? {
        val findExistingMenu = _mainNavListVisitable.find {
            it is HomeNavVisitable && it.id() == menuId
        }
        return if (findExistingMenu is HomeNavMenuViewModel) findExistingMenu
        else null
    }

    private fun HomeNavMenuViewModel.updateBadgeCounter(counter: String) {
        val indexOfMenu = _mainNavListVisitable.indexOf(this)
        this.notifCount = counter
        updateWidget(this, indexOfMenu)
    }
}