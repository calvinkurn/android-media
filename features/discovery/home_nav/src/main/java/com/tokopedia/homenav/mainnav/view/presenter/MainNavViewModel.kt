package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
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
import com.tokopedia.homenav.common.util.Event
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getCategoryGroupUseCase: Lazy<GetCategoryGroupUseCase>,
        private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
        private val getNavNotification: Lazy<GetNavNotification>,
        private val getUohOrdersNavUseCase: Lazy<GetUohOrdersNavUseCase>,
        private val getPaymentOrdersNavUseCase: Lazy<GetPaymentOrdersNavUseCase>,
        private val getProfileDataUseCase: Lazy<GetProfileDataUseCase>,
        private val getProfileDataCacheUseCase: Lazy<GetProfileDataCacheUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val INDEX_HOME_BACK_SEPARATOR = 1
        private const val INDEX_HOME_BACK_ICON = 2
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6
        private const val INDEX_DEFAULT_BU_POSITION = 1
    }

    private var haveLogoutData: Boolean? = false

    //network process live data, false if it is processing and true if it is finished
    val networkProcessLiveData: LiveData<Boolean>
        get() = _networkProcessLiveData
    private val _networkProcessLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var navNotification: NavNotificationModel = NavNotificationModel(
            unreadCountInboxTicket = 0,
            unreadCountComplain = 0
    )

    private var pageSource: String = ""
    private var pageSourceDefault: String = "Default"

    private var _mainNavListVisitable = setInitialState()

    val allProcessFinished: LiveData<Event<Boolean>>
        get() = _allProcessFinished
    private val _allProcessFinished: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    val businessListLiveData: LiveData<Result<List<HomeNavVisitable>>>
        get() = _businessListLiveData
    private val _businessListLiveData: MutableLiveData<Result<List<HomeNavVisitable>>> = MutableLiveData()

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(MainNavigationDataModel(
            dataList = _mainNavListVisitable
    ))

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun updateWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList[position] = visitable
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    fun addWidgetList(visitables: List<Visitable<*>>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.addAll(position, visitables)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        val newMainNavList = _mainNavListVisitable
        if (position == null) {
            newMainNavList.add(visitable)
        } else {
            newMainNavList.add(position, visitable)
        }
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = _mainNavListVisitable))
    }

    fun addWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.addAll(visitables)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = _mainNavListVisitable))
    }

    fun addInitialWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _mainNavListVisitable.toMutableList()
        newMainNavList.addAll(visitables)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidget(position: Int) {
        val newMainNavList = _mainNavListVisitable.toMutableList()
        newMainNavList.removeAt(position)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidget(visitable: Visitable<*>) {
        val newMainNavList = _mainNavListVisitable.toMutableList()
        newMainNavList.remove(visitable)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun deleteWidgetList(visitables: List<Visitable<*>>) {
        val newMainNavList = _mainNavListVisitable.toMutableList()
        newMainNavList.removeAll(visitables)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun setPageSource(pageSource: String = pageSourceDefault) {
        this.pageSource = pageSource
        if (pageSource == ApplinkConsInternalNavigation.SOURCE_HOME) { removeHomeBackButtonMenu() }
        else { addHomeBackButtonMenu() }
    }

    fun setUserHaveLogoutData(haveLogoutData: Boolean) {
        this.haveLogoutData = haveLogoutData
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

    private fun setInitialState(): MutableList<Visitable<*>> {
        val initialList = mutableListOf<Visitable<*>>()
        if (userSession.get().isLoggedIn) {
            initialList.add(InitialShimmerProfileDataModel())
        } else {
            initialList.add(AccountHeaderDataModel(loginState = getLoginState()))
        }
        initialList.add(InitialShimmerDataModel())
        initialList.addTransactionMenu()
        initialList.addUserMenu()
        return initialList
    }

    private fun getLoginState(): Int {
        return when {
            userSession.get().isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
            haveLogoutData?:false -> AccountHeaderDataModel.LOGIN_STATE_LOGIN_AS
            else -> AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN
        }
    }

    fun getMainNavData(useCacheData: Boolean) {
        _networkProcessLiveData.value = false
        launch {
            if (useCacheData) {
                onlyForLoggedInUser { getProfileDataCached() }
                getBuListMenuCached()
            }
            //update cached data with cloud data
            getBuListMenu()
            onlyForLoggedInUser { getNotification() }
            onlyForLoggedInUser { updateProfileData() }
            onlyForLoggedInUser { getOngoingTransaction() }
        }
    }

    private fun MutableList<Visitable<*>>.addHomeBackButtonMenu() {
        if (pageSource != ApplinkConsInternalNavigation.SOURCE_HOME) {
            this.add(clientMenuGenerator.get().getMenu(menuId = ID_HOME, sectionId = MainNavConst.Section.HOME))
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
            (it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.HOME) ||
                    (it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME)
        }
        deleteWidgetList(listOfHomeMenuSection)
    }

    private fun addHomeBackButtonMenu() {
        val listOfHomeMenuSection = mutableListOf<Visitable<*>>()
        listOfHomeMenuSection.add(SeparatorDataModel(sectionId = MainNavConst.Section.HOME))
        listOfHomeMenuSection.add(clientMenuGenerator.get().getMenu(menuId = ID_HOME, sectionId = MainNavConst.Section.HOME))
        addWidgetList(listOfHomeMenuSection, INDEX_HOME_BACK_SEPARATOR)
    }

    private suspend fun getBuListMenuCached() {
        withContext(coroutineContext, block = {
            try {
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                //PLT network process is finished
                _networkProcessLiveData.postValue(true)

                val shimmeringDataModel = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
                addWidgetList(result, findBuStartIndexPosition())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private suspend fun getBuListMenu() {
        withContext(coroutineContext, block = {
            try {
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCloudThenCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                //PLT network process is finished
                _networkProcessLiveData.postValue(true)
                val shimmeringDataModel = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
                if (findExistingEndBuIndexPosition() == null) {
                    addWidgetList(result, findBuStartIndexPosition())
                }
                onlyForNonLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            } catch (e: Exception) {
                //if bu cache is already exist in list
                //then error state is not needed
                val isBuExist = findExistingEndBuIndexPosition()
                if (isBuExist == null) {
                    updateWidget(ErrorStateBuDataModel(), findBuStartIndexPosition())
                }

                val buShimmering = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                buShimmering?.let {
                    updateWidget(ErrorStateBuDataModel(),
                            _mainNavListVisitable.indexOf(it)
                    )
                }
                onlyForNonLoggedInUser { _allProcessFinished.postValue(Event(true)) }
                e.printStackTrace()
            }
        })
    }

    suspend fun getProfileDataCached() {
        try {
            val accountHeaderModel = getProfileDataCacheUseCase.get().executeOnBackground()
            updateWidget(accountHeaderModel, INDEX_MODEL_ACCOUNT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateProfileData() {
        try {
            val accountHeaderModel = getProfileDataUseCase.get().executeOnBackground()
            updateWidget(accountHeaderModel, INDEX_MODEL_ACCOUNT)
        } catch (e: Exception) {
            val accountModel = _mainNavListVisitable.find {
                it is AccountHeaderDataModel
            } as? AccountHeaderDataModel

            accountModel?.let { account ->
                if (account.isProfileLoading) {
                    updateWidget(account.copy(
                            isGetShopError = true,
                            isProfileLoading = false,
                            isGetOvoError = true,
                            isGetSaldoError = true,
                            isGetUserMembershipError = true,
                            isGetUserNameError = true
                    ), INDEX_MODEL_ACCOUNT)
                }
            }
            e.printStackTrace()
        }
    }

    fun refreshBuListdata() {
        updateWidget(InitialShimmerDataModel(), findBuStartIndexPosition())
        launchCatchError(coroutineContext, block = {
            getBuListMenu()
        }) {

        }
    }

    fun refreshTransactionListData() {
        val transactionPlaceHolder = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateOngoingTransactionModel
        }
        transactionPlaceHolder?.let {
            updateWidget(InitialShimmerTransactionDataModel(), transactionPlaceHolder.index)
        }
        launchCatchError(coroutineContext, block = {
            getOngoingTransaction()
        }) {

        }
    }

    private suspend fun getOngoingTransaction() {
        //find error state if available and change to shimmering
        val transactionErrorState = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateOngoingTransactionModel
        }
        transactionErrorState?.let {
            updateWidget(InitialShimmerTransactionDataModel(), it.index)
        }
        try {
            val paymentList = getPaymentOrdersNavUseCase.get().executeOnBackground()
            val orderList = getUohOrdersNavUseCase.get().executeOnBackground()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty()) {
                val othersTransactionCount = orderList.size - 6
                val orderListToShow = orderList.take(ON_GOING_TRANSACTION_TO_SHOW)
                val transactionListItemViewModel = TransactionListItemDataModel(
                        NavOrderListModel(orderListToShow, paymentList), othersTransactionCount)

                //find shimmering and change with result value
                findExistingEndBuIndexPosition()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            val transactionShimmering = _mainNavListVisitable.withIndex().find {
                it.value is InitialShimmerTransactionDataModel
            }
            if (transactionShimmering != null) {
                transactionShimmering.let {
                    updateWidget(ErrorStateOngoingTransactionModel(), it.index)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    private fun buildUserMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            val firstSectionList = mutableListOf<Visitable<*>>(
                    SeparatorDataModel(),
                    it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_RECENT_VIEW, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_SUBSCRIPTION, sectionId = MainNavConst.Section.USER_MENU)
            )
            val showOpenShopTicker = userSession.get().isLoggedIn && !userSession.get().hasShop()
            if (showOpenShopTicker) firstSectionList.add(it.getTicker(ID_OPEN_SHOP_TICKER))
            firstSectionList.add(SeparatorDataModel())

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
                    SeparatorDataModel(),
                    it.getMenu(ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(ID_TICKET, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(ID_REVIEW, sectionId = MainNavConst.Section.ORDER)
            )
            return visitableList
        }
        return listOf()
    }

    private suspend fun getNotification() {
        try {
            val result = getNavNotification.get().executeOnBackground()
            val complainNotification = result.unreadCountComplain
            val inboxTicketNotification = result.unreadCountInboxTicket
            navNotification = NavNotificationModel(
                    unreadCountComplain = complainNotification,
                    unreadCountInboxTicket = inboxTicketNotification
            )
            if (complainNotification.isMoreThanZero()) _mainNavListVisitable.findMenu(ID_COMPLAIN)?.updateBadgeCounter(complainNotification.toString())
            if (inboxTicketNotification.isMoreThanZero()) _mainNavListVisitable.findMenu(ID_TOKOPEDIA_CARE)?.updateBadgeCounter(inboxTicketNotification.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun refreshUserShopData() {
        val newAccountData = _mainNavListVisitable.find {
            it is AccountHeaderDataModel
        } as? AccountHeaderDataModel
        newAccountData?.let { accountModel ->
            //set shimmering before getting the data
            updateWidget(accountModel.copy(isGetShopLoading = true, isGetShopError = true), INDEX_MODEL_ACCOUNT)

            launchCatchError(coroutineContext, block = {
                val call = async {
                    withContext(baseDispatcher.get().io()) {
                        getShopInfoUseCase.get().executeOnBackground()
                    }
                }
                val response = call.await()
                val result = (response.takeIf { it is Success } as? Success<ShopInfoPojo>)?.data
                result?.let {
                    accountModel.setUserShopName(it.info.shopName, it.info.shopId)
                    updateWidget(accountModel, INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

                val fail = (response.takeIf { it is Fail } )
                fail?.let {
                    updateWidget(accountModel.copy(isGetShopError = true, isGetShopLoading = false), INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

            }){
                updateWidget(accountModel.copy(isGetShopError = true, isGetShopLoading = false), INDEX_MODEL_ACCOUNT)
            }
        }
    }

    fun reloadMainNavAfterLogin() {
        getMainNavData(false)
    }

    fun refreshProfileData() {
        updateWidget(InitialShimmerProfileDataModel(), INDEX_MODEL_ACCOUNT)
        launchCatchError(coroutineContext, block = {
            updateProfileData()
        }) {

        }
    }

    private suspend fun onlyForLoggedInUser(function: suspend ()-> Unit) {
        if (userSession.get().isLoggedIn) function.invoke()
    }

    private suspend fun onlyForNonLoggedInUser(function: suspend ()-> Unit) {
        if (!userSession.get().isLoggedIn) function.invoke()
    }

    private fun onlyForLoggedInUserUi(function: ()-> Unit) {
        if (userSession.get().isLoggedIn) function.invoke()
    }

    //bu menu start index should after back home button or position 1
    private fun findBuStartIndexPosition(): Int {
        val findHomeMenu = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME
        }
        findHomeMenu?.let{
            //if home menu is exist, then the position of bu menu is after home menu
            return _mainNavListVisitable.indexOf(it)+1
        }
        return INDEX_DEFAULT_BU_POSITION
    }

    private fun findExistingEndBuIndexPosition(): Int? {
        val findHomeMenu = _mainNavListVisitable.findLast {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.BU_ICON
        }
        findHomeMenu?.let{
            //if home menu is exist, then the position of bu menu is after home menu
            return _mainNavListVisitable.indexOf(it)+1
        }
        return null
    }

    private fun List<Visitable<*>>.findMenu(menuId: Int): HomeNavMenuDataModel? {
        val findExistingMenu = _mainNavListVisitable.find {
            it is HomeNavVisitable && it.id() == menuId
        }
        return if (findExistingMenu is HomeNavMenuDataModel) findExistingMenu
        else null
    }

    private fun HomeNavMenuDataModel.updateBadgeCounter(counter: String) {
        val indexOfMenu = _mainNavListVisitable.indexOf(this)
        this.notifCount = counter
        updateWidget(this, indexOfMenu)
    }

    private fun isFromHomePage(): Boolean {
        return pageSource == ApplinkConsInternalNavigation.SOURCE_HOME
    }
}