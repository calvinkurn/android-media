package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_ALL_CATEGORIES
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_HELP_CENTER
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_MY_ACTIVITY
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
import com.tokopedia.homenav.common.util.isABNewTokopoint
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionAdminData
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionShopData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val baseDispatcher: Lazy<CoroutineDispatchers>,
        private val getCategoryGroupUseCase: Lazy<GetCategoryGroupUseCase>,
        private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
        private val getNavNotification: Lazy<GetNavNotification>,
        private val getUohOrdersNavUseCase: Lazy<GetUohOrdersNavUseCase>,
        private val getPaymentOrdersNavUseCase: Lazy<GetPaymentOrdersNavUseCase>,
        private val getProfileDataUseCase: Lazy<GetProfileDataUseCase>,
        private val getProfileDataCacheUseCase: Lazy<GetProfileDataCacheUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val accountAdminInfoUseCase: Lazy<AccountAdminInfoUseCase>
): BaseViewModel(baseDispatcher.get().io) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val INDEX_HOME_BACK_SEPARATOR = 1
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6
        private const val INDEX_DEFAULT_BU_POSITION = 1
        private const val INDEX_DEFAULT_ALL_TRANSACTION = 1

        private const val SOURCE = "dave_home_nav"
    }


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

    private fun updateWidget(visitable: Visitable<*>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList[position] = visitable
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    private fun addWidgetList(visitables: List<Visitable<*>>, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.addAll(position, visitables)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
    }

    private fun deleteWidget(visitable: Visitable<*>) {
        val newMainNavList = _mainNavListVisitable.toMutableList()
        newMainNavList.remove(visitable)
        _mainNavListVisitable = newMainNavList
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    private fun deleteWidgetList(visitables: List<Visitable<*>>) {
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

    fun getPageSource(): String {
        return pageSource
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun setInitialState(): MutableList<Visitable<*>> {
        val initialList = mutableListOf<Visitable<*>>()
        if (userSession.get().isLoggedIn) {
            initialList.add(InitialShimmerProfileDataModel())
        } else {
            initialList.add(AccountHeaderDataModel(loginState = getLoginState()))
        }
        initialList.addTransactionMenu()
        initialList.addBUTitle()
        initialList.add(InitialShimmerDataModel())
        initialList.addUserMenu()
        return initialList
    }

    private fun getLoginState(): Int {
        return when {
            userSession.get().isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
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

    private fun MutableList<Visitable<*>>.addTransactionMenu() {
        this.addAll(buildTransactionMenuList())
    }

    private fun MutableList<Visitable<*>>.addUserMenu() {
        this.addAll(buildUserMenuList())
    }

    private fun MutableList<Visitable<*>>.addBUTitle() {
        this.addAll(buildBUTitleList())
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
                findBuStartIndexPosition()?.let {
                    addWidgetList(result, it)
                }
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
                findBuStartIndexPosition()?.let {
                    if (findExistingEndBuIndexPosition() == null) {
                        addWidgetList(result, it)
                    }
                }

                onlyForNonLoggedInUser {
                    delay(1000)
                    _allProcessFinished.postValue(Event(true))
                }
            } catch (e: Exception) {
                //if bu cache is already exist in list
                //then error state is not needed
                val isBuExist = findExistingEndBuIndexPosition()
                if (isBuExist == null) {
                    findBuStartIndexPosition()?.let {
                        updateWidget(ErrorStateBuDataModel(), it)
                    }
                }

                val buShimmering = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                buShimmering?.let {
                    updateWidget(ErrorStateBuDataModel(),
                            _mainNavListVisitable.indexOf(it)
                    )
                }
                onlyForNonLoggedInUser {
                    delay(1000)
                    _allProcessFinished.postValue(Event(true))
                }
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
            val accountHeaderModel = async { getProfileDataUseCase.get().executeOnBackground() }
            val adminData = async { getAdminData() }

            accountHeaderModel.await().apply {
                adminData.await().let { (adminRoleText, canGoToSellerAccount, isShopActive) ->
                    val adminRole =
                            if (isShopActive) {
                                adminRoleText
                            } else {
                                null
                            }
                    setAdminData(adminRole, canGoToSellerAccount)
                }
            }.let {
                updateWidget(it, INDEX_MODEL_ACCOUNT)
            }
        } catch (e: Exception) {
            val accountModel = _mainNavListVisitable.find {
                it is AccountHeaderDataModel
            } as? AccountHeaderDataModel

            accountModel?.let { account ->
                if (account.isProfileLoading) {
                    updateWidget(account.copy(
                            isTokopointExternalAmountError = isABNewTokopoint(),
                            isGetShopError = true,
                            isProfileLoading = false,
                            isGetOvoError = !isABNewTokopoint(),
                            isGetSaldoError = !isABNewTokopoint(),
                            isGetUserMembershipError = true,
                            isGetUserNameError = true
                    ), INDEX_MODEL_ACCOUNT)
                }
            }
            e.printStackTrace()
        }
    }

    fun refreshBuListdata() {
        findBuStartIndexPosition()?.let {
            updateWidget(InitialShimmerDataModel(), it)
            launchCatchError(coroutineContext, block = {
                getBuListMenu()
            }) {

            }
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

    suspend fun getOngoingTransaction() {
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
                findTransactionShimmer()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
            } else {
                findTransactionShimmer()?.let {
                    deleteWidget(InitialShimmerTransactionDataModel())
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            findTransactionShimmer()?.let {
                updateWidget(ErrorStateOngoingTransactionModel(), it)
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    private fun buildBUTitleList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            return mutableListOf(
                    it.getSectionTitle(IDENTIFIER_TITLE_ALL_CATEGORIES)
            )
        }
        return listOf()
    }

    private fun buildUserMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            val complainNotification = if (navNotification.unreadCountComplain.isMoreThanZero())
                navNotification.unreadCountComplain.toString() else ""

            val inboxTicketNotification = if (navNotification.unreadCountInboxTicket.isMoreThanZero())
                navNotification.unreadCountInboxTicket.toString() else ""

            val firstSectionList = mutableListOf<Visitable<*>>(
                    it.getSectionTitle(IDENTIFIER_TITLE_HELP_CENTER),
                    it.getMenu(menuId = ID_COMPLAIN, notifCount = complainNotification, sectionId = MainNavConst.Section.USER_MENU),
                    it.getMenu(menuId = ID_TOKOPEDIA_CARE, notifCount = inboxTicketNotification, sectionId = MainNavConst.Section.USER_MENU)
            )
            firstSectionList.add(SeparatorDataModel())

            val secondSectionList = listOf(
                    it.getMenu(menuId = ID_QR_CODE, sectionId = MainNavConst.Section.USER_MENU)
            )
            val completeList = firstSectionList.plus(secondSectionList)
            return completeList
        }
        return listOf()
    }

    private fun buildTransactionMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            var transactionDataList: MutableList<Visitable<*>> = mutableListOf()
            if (userSession.get().isLoggedIn) {
                transactionDataList = mutableListOf(
                        SeparatorDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_MY_ACTIVITY),
                        InitialShimmerTransactionDataModel(),
                        it.getMenu(menuId = ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER))
            } else {
                transactionDataList = mutableListOf(
                        SeparatorDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_MY_ACTIVITY),
                        it.getMenu(menuId = ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER))
            }
            return transactionDataList
        }
        return listOf()
    }

    private suspend fun getNotification() {
        launch {
            try {
                val result = getNavNotification.get().executeOnBackground()
                val complainNotification = result.unreadCountComplain
                val inboxTicketNotification = result.unreadCountInboxTicket
                val reviewNotification = result.unreadCountReview
                navNotification = NavNotificationModel(
                        unreadCountComplain = complainNotification,
                        unreadCountInboxTicket = inboxTicketNotification,
                        unreadCountReview = reviewNotification
                )
                if (complainNotification.isMoreThanZero()) findMenu(ID_COMPLAIN)?.updateBadgeCounter(complainNotification.toString())
                if (inboxTicketNotification.isMoreThanZero()) findMenu(ID_TOKOPEDIA_CARE)?.updateBadgeCounter(inboxTicketNotification.toString())
                if (reviewNotification.isMoreThanZero()) findMenu(ID_REVIEW)?.updateBadgeCounter(reviewNotification.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getTotalOrderCount(notificationPojo: ShopData.NotificationPojo): Int {
        return notificationPojo.sellerOrderStatus.newOrderCount
                .plus(notificationPojo.sellerOrderStatus.readyToShipOrderCount)
                .plus(notificationPojo.sellerOrderStatus.inResolution)
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
                    withContext(baseDispatcher.get().io) {
                        getShopInfoUseCase.get().executeOnBackground()
                    }
                }
                val adminDataCall = async {
                    withContext(baseDispatcher.get().io) {
                        getAdminData()
                    }
                }
                val response = call.await()
                val (adminRoleText, canGoToSellerAccount, isShopActive) = adminDataCall.await()
                val result = (response.takeIf { it is Success } as? Success<ShopData>)?.data
                result?.let {
                    accountModel.run {
                        val shopName: String
                        val shopId: String
                        val adminRole: String?
                        val orderCount: Int
                        if (isShopActive) {
                            shopName = it.userShopInfo.info.shopName
                            shopId = it.userShopInfo.info.shopId
                            orderCount = getTotalOrderCount(it.notifications)
                            adminRole = adminRoleText
                        } else {
                            shopName = ""
                            shopId = ""
                            adminRole = null
                            orderCount = 0
                        }
                        setUserShopName(shopName, shopId, orderCount)
                        setAdminData(adminRole, canGoToSellerAccount)
                    }
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

    /**
     * Check for account admin info if is not shop owner
     * and update shop related user session values accordingly
     *
     * @return  Triple of admin role text (if is admin), boolean to determine if seller can go to
     *          account page, and boolean if shop is active
     */
    private suspend fun getAdminData(): Triple<String?, Boolean, Boolean> {
        val (adminDataResponse, refreshedShopData) =
                if (userSession.get().isShopOwner) {
                    Pair(null, null)
                } else {
                    accountAdminInfoUseCase.get().run {
                        requestParams = AccountAdminInfoUseCase.createRequestParams(SOURCE)
                        isLocationAdmin = userSession.get().isLocationAdmin
                        setStrategyCloudThenCache()
                        executeOnBackground()
                    }
                }
        val isShopActive = adminDataResponse?.data?.isShopActive() == true
        adminDataResponse?.let {
            userSession.get().refreshUserSessionAdminData(it)
        }
        refreshedShopData?.let {
            val shopId =
                    if(isShopActive) {
                        it.shopId
                    } else {
                        ""
                    }
            userSession.get().refreshUserSessionShopData(it.copy(shopId = shopId))
        }
        val canGoToSellerAccount: Boolean = adminDataResponse?.data?.detail?.roleType?.isLocationAdmin?.not() ?: true
        val adminRoleText: String? = adminDataResponse?.data?.adminTypeText
        return Triple(adminRoleText, canGoToSellerAccount, isShopActive)
    }

    fun findComplainModelPosition(): Int? {
        val findComplainModel = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id == ID_TOKOPEDIA_CARE
        }
        findComplainModel?.let{
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }


    //all transaction menu start index should after back home button or position 1
    fun findAllTransactionModelPosition(): Int? {
        val findHomeMenu = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id == ID_HOME
        }
        findHomeMenu?.let{
            //if home menu is exist, then the position of all transaction menu is after home menu
            return _mainNavListVisitable.indexOf(it) + 1
        }
        return INDEX_DEFAULT_ALL_TRANSACTION
    }

    private fun findTransactionShimmer(): Int? {
        val transactionShimmer = _mainNavListVisitable.firstOrNull {
            it is InitialShimmerTransactionDataModel
        }
        transactionShimmer?.let{
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }

    fun findHeaderModelPosition(): Int? {
        val findHeaderModel = _mainNavListVisitable.find {
            it is AccountHeaderDataModel
        }
        findHeaderModel?.let{
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }

    private fun findBuStartIndexPosition(): Int? {
        val findBUTitle = _mainNavListVisitable.firstOrNull {
            it is HomeNavTitleDataModel && it.identifier == IDENTIFIER_TITLE_ALL_CATEGORIES
        }
        findBUTitle?.let{
            return _mainNavListVisitable.indexOf(it) + 1
        }
        return null
    }

    private fun findExistingEndBuIndexPosition(): Int? {
        val findHomeMenu = _mainNavListVisitable.findLast {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.BU_ICON
        }
        findHomeMenu?.let{
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }

    fun findMenu(menuId: Int): HomeNavMenuDataModel? {
        val findExistingMenu = _mainNavListVisitable.find {
            it is HomeNavVisitable && it.id() == menuId
        }
        return if (findExistingMenu is HomeNavMenuDataModel) findExistingMenu
        else null
    }

    private fun HomeNavMenuDataModel.updateBadgeCounter(counter: String) {
        val indexOfMenu = _mainNavListVisitable.indexOf(this)
        updateWidget(this.copy(notifCount = counter), indexOfMenu)
    }
}