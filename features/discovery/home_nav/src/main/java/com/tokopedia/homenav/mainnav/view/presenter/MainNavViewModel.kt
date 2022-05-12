package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_ALL_CATEGORIES
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_HELP_CENTER
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_ORDER_HISTORY
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_WISHLIST
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.common.util.Event
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.*
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_FAILED
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_LOADING
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_SUCCESS
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ErrorStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ShimmerFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ShimmerWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
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
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val accountAdminInfoUseCase: Lazy<AccountAdminInfoUseCase>,
        private val getAffiliateUserUseCase: Lazy<GetAffiliateUserUseCase>,
        private val getFavoriteShopsNavUseCase: Lazy<GetFavoriteShopsNavUseCase>,
        private val getWishlistNavUseCase: Lazy<GetWishlistNavUseCase>
): BaseViewModel(baseDispatcher.get().io) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val INDEX_HOME_BACK_SEPARATOR = 1
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6
        private const val ON_GOING_FAVORITE_SHOPS_TO_SHOW = 5
        private const val INDEX_DEFAULT_ALL_TRANSACTION = 1
        private const val INDEX_DEFAULT_ALL_CATEGORY = 8

        private const val SOURCE = "dave_home_nav"
        private const val MAX_ORDER_TO_SHOW = 6
        private const val MAX_FAVORITE_SHOPS_TO_SHOW = 5
        private const val isMePageUsingRollenceVariant = true
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
    private var mainNavProfileCache: MainNavProfileCache? = null

    private var _mainNavListVisitable = mutableListOf<Visitable<*>>()

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

    val profileDataLiveData: LiveData<AccountHeaderDataModel>
        get() = _profileDataLiveData
    private val _profileDataLiveData: MutableLiveData<AccountHeaderDataModel> = MutableLiveData()

    private var allCategoriesCache = listOf<Visitable<*>>()
    private lateinit var allCategories : HomeNavExpandableDataModel

    private var isMePageUsingRollenceVariant : Boolean = false

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
        if (isLaunchedFromHome()) {
                        removeHomeBackButtonMenu()
        }
        else { addHomeBackButtonMenu() }
    }

    fun setProfileCache(mainNavProfileCache: MainNavProfileCache?) {
        this.mainNavProfileCache = mainNavProfileCache
    }

    private fun isLaunchedFromHome(): Boolean {
        return pageSource == ApplinkConsInternalNavigation.SOURCE_HOME ||
                pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_UOH ||
                pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST
    }

    fun getPageSource(): String {
        return pageSource
    }

    private fun updateAllCategories(menus: List<Visitable<*>>, isExpanded: Boolean = false) {
        if (isMePageUsingRollenceVariant) {
            allCategories.menus = menus
            allCategories.isExpanded = isExpanded
            updateWidget(allCategories, findBuStartIndexPosition() ?: INDEX_DEFAULT_ALL_CATEGORY)
        }
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun setInitialState(): MutableList<Visitable<*>> {
        val initialList = mutableListOf<Visitable<*>>()
        if (userSession.get().isLoggedIn) {
            initialList.add(AccountHeaderDataModel(state = NAV_PROFILE_STATE_LOADING))
        } else {
            initialList.add(AccountHeaderDataModel(loginState = getLoginState(), state = NAV_PROFILE_STATE_SUCCESS))
        }
        initialList.addTransactionMenu()
        initialList.addBUTitle()
        initialList.addUserMenu()
        return initialList
    }

    private fun getLoginState(): Int {
        return when {
            userSession.get().isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
            else -> AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN
        }
    }

    fun setIsMePageUsingRollenceVariant(value: Boolean){
        isMePageUsingRollenceVariant = value
        _mainNavListVisitable = setInitialState()
    }

    fun getMainNavData(useCacheData: Boolean) {
        _networkProcessLiveData.value = false
        launch {
            if (useCacheData) {
                onlyForLoggedInUser { getProfileDataCached() }
                getBuListMenuCached()
            }
            else {
                getBuListMenu()
            }
            //update cached data with cloud data
            onlyForLoggedInUser { getNotification() }
            onlyForLoggedInUser { updateProfileData() }
            onlyForLoggedInUser {
                if (isMePageUsingRollenceVariant) {
                    getOngoingTransactionRevamp()
                } else {
                    getOngoingTransaction()
                }
            }
            if(isMePageUsingRollenceVariant){
                onlyForLoggedInUser { getFavoriteShops() }
                onlyForLoggedInUser { getWishlist() }
            }
        }
    }

    private fun MutableList<Visitable<*>>.addTransactionMenu() {
        if(isMePageUsingRollenceVariant){
            this.addAll(buildTransactionMenuListRevamp())
        } else {
            this.addAll(buildTransactionMenuList())
        }
    }

    private fun MutableList<Visitable<*>>.addUserMenu() {
        this.addAll(buildUserMenuList())
    }

    private fun MutableList<Visitable<*>>.addBUTitle() {
        if (isMePageUsingRollenceVariant) {
            this.add(SeparatorDataModel())
            allCategories = HomeNavExpandableDataModel(id = IDENTIFIER_TITLE_ALL_CATEGORIES)
            this.add(allCategories)
            this.add(SeparatorDataModel())
        } else {
            this.addAll(buildBUTitleList())
            this.add(InitialShimmerDataModel())
        }
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
        viewModelScope.launch {
            try {
                updateAllCategories(listOf(InitialShimmerDataModel()))
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                //PLT network process is finished
                _networkProcessLiveData.postValue(true)

                if (isMePageUsingRollenceVariant) {
                    allCategoriesCache = result
                } else {
                    val shimmeringDataModel = _mainNavListVisitable.find {
                        it is InitialShimmerDataModel
                    }
                    shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
                    findBuStartIndexPosition()?.let {
                        addWidgetList(result, it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            getBuListMenu()
        }
    }

    private fun isAllCategoriesNotContainsShimmer(): Boolean =
        allCategories.menus.firstOrNull { it is InitialShimmerDataModel } == null

    private suspend fun getBuListMenu(isExpanded: Boolean = false) {
        if (isMePageUsingRollenceVariant && isAllCategoriesNotContainsShimmer()) {
            updateAllCategories(listOf(InitialShimmerDataModel()), isExpanded)
        }
        viewModelScope.launch {
            try {
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCloudThenCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                //PLT network process is finished
                _networkProcessLiveData.postValue(true)

                if (isMePageUsingRollenceVariant) {
                    updateAllCategories(result, isExpanded)
                } else {
                    val shimmeringDataModel = _mainNavListVisitable.find {
                        it is InitialShimmerDataModel
                    }
                    shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
                    findBuStartIndexPosition()?.let {
                        if (findExistingEndBuIndexPosition() == null) {
                            addWidgetList(result, it)
                        }
                    }
                }
            } catch (e: Exception) {
                if (isMePageUsingRollenceVariant) {
                    if (allCategoriesCache.isNotEmpty()) {
                        updateAllCategories(allCategoriesCache, isExpanded)
                    } else {
                        updateAllCategories(listOf(ErrorStateBuDataModel()), isExpanded)
                    }
                } else {
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
                        updateWidget(
                            ErrorStateBuDataModel(),
                            _mainNavListVisitable.indexOf(it)
                        )
                    }
                }
                e.printStackTrace()
            }
        }
    }

    private fun getProfileDataCached() {
        try {
            mainNavProfileCache?.let {
                val accountHeaderModel = AccountHeaderDataModel(
                    profileDataModel = ProfileDataModel(
                        userName = it.profileName?:"",
                        userImage = it.profilePicUrl?:""
                    ),
                    profileMembershipDataModel = ProfileMembershipDataModel(
                        badge = it.memberStatusIconUrl?:""
                    ),
                    isCacheData = true,
                    state = NAV_PROFILE_STATE_SUCCESS,
                    profileSellerDataModel = ProfileSellerDataModel(
                        isGetShopLoading = true
                    ),
                    profileAffiliateDataModel = ProfileAffiliateDataModel(
                        isGetAffiliateLoading = true
                    )
                )
                updateWidget(accountHeaderModel, INDEX_MODEL_ACCOUNT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateProfileData() {
        viewModelScope.launch {
            try {
                val accountHeaderModel = getProfileDataUseCase.get().executeOnBackground()
                val adminData = getAdminData()

                accountHeaderModel.apply {
                    adminData.let { (_, canGoToSellerAccount, _) ->
                        val adminRole = null
                        setAdminData(adminRole, canGoToSellerAccount)
                    }
                }.let {
                    it.state = NAV_PROFILE_STATE_SUCCESS
                    updateWidget(it, INDEX_MODEL_ACCOUNT)
                    _profileDataLiveData.postValue(it)
                }
            } catch (e: Exception) {
                val accountModel = _mainNavListVisitable.find {
                    it is AccountHeaderDataModel
                } as? AccountHeaderDataModel

                accountModel?.let { account ->
                    if (account.state == NAV_PROFILE_STATE_LOADING) {
                        updateWidget(
                            account.copy(state = NAV_PROFILE_STATE_FAILED),
                            INDEX_MODEL_ACCOUNT
                        )
                    }
                }
                e.printStackTrace()
            }
        }
    }

    fun refreshBuListData() {
        launchCatchError(coroutineContext, block = {
            if (!isMePageUsingRollenceVariant) {
                findBuStartIndexPosition()?.let {
                    updateWidget(InitialShimmerDataModel(), it)
                }
            }
            getBuListMenu(isExpanded = true)
        }) {
            //no-op
        }
    }

    fun refreshTransactionListData() {
        val transactionPlaceHolder = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateOngoingTransactionModel
        }
        transactionPlaceHolder?.let {
            updateWidget(InitialShimmerTransactionRevampDataModel(), transactionPlaceHolder.index)
        }
        launchCatchError(coroutineContext, block = {
            if (isMePageUsingRollenceVariant) {
                getOngoingTransactionRevamp()
            } else {
                getOngoingTransaction()
            }
        }) {

        }
    }

    private suspend fun getOngoingTransaction() {
        //find error state if availabxle and change to shimmering
        val transactionErrorState = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateOngoingTransactionModel
        }
        transactionErrorState?.let {
            updateWidget(InitialShimmerTransactionDataModel(), it.index)
        }
        try {
            val paymentList = getPaymentOrdersNavUseCase.get().executeOnBackground()
            val orderList = getUohOrdersNavUseCase.get().executeOnBackground()
            val reviewList = listOf<NavReviewOrder>()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty() || reviewList.isNotEmpty()) {
                val othersTransactionCount = orderList.size - MAX_ORDER_TO_SHOW
                val orderListToShow = orderList.take(ON_GOING_TRANSACTION_TO_SHOW)
                val transactionListItemViewModel = TransactionListItemDataModel(
                    NavOrderListModel(orderListToShow, paymentList, reviewList),
                    othersTransactionCount,
                    isMePageUsingRollenceVariant
                )

                //find shimmering and change with result value
                findShimmerPosition<InitialShimmerTransactionDataModel>()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
            } else {
                val emptyTransaction = TransactionListItemDataModel(NavOrderListModel(), isMePageUsingRollenceVariant = isMePageUsingRollenceVariant)
                findShimmerPosition<InitialShimmerTransactionDataModel>()?.let {
                    updateWidget(emptyTransaction, it)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            findShimmerPosition<InitialShimmerTransactionDataModel>()?.let {
                updateWidget(ErrorStateOngoingTransactionModel(), it)
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    private suspend fun getOngoingTransactionRevamp() {
        //find error state if availabxle and change to shimmering
        val transactionErrorState = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateOngoingTransactionModel
        }
        transactionErrorState?.let {
            updateWidget(InitialShimmerTransactionRevampDataModel(), it.index)
        }
        try {
            val paymentList = getPaymentOrdersNavUseCase.get().executeOnBackground()
            val orderList = getUohOrdersNavUseCase.get().executeOnBackground()
            val reviewList = listOf<NavReviewOrder>()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty() || reviewList.isNotEmpty()) {
                val othersTransactionCount = orderList.size - MAX_ORDER_TO_SHOW
                val orderListToShow = orderList.take(ON_GOING_TRANSACTION_TO_SHOW)
                val transactionListItemViewModel = TransactionListItemDataModel(
                    NavOrderListModel(orderListToShow, paymentList, reviewList),
                    othersTransactionCount,
                    isMePageUsingRollenceVariant
                )

                //find shimmering and change with result value
                findShimmerPosition<InitialShimmerTransactionRevampDataModel>()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
            } else {
                val emptyTransaction = TransactionListItemDataModel(NavOrderListModel(), isMePageUsingRollenceVariant = isMePageUsingRollenceVariant)
                findShimmerPosition<InitialShimmerTransactionRevampDataModel>()?.let {
                    updateWidget(emptyTransaction, it)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            findShimmerPosition<InitialShimmerTransactionRevampDataModel>()?.let {
                updateWidget(ErrorStateOngoingTransactionModel(), it)
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    suspend fun getFavoriteShops() {
        //find error state if available and change to shimmering
        val favoriteShopErrorState = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateFavoriteShopDataModel
        }
        favoriteShopErrorState?.let {
            updateWidget(ShimmerFavoriteShopDataModel(), it.index)
        }
        try {
            val favoriteShopList = getFavoriteShopsNavUseCase.get().executeOnBackground()

            if (favoriteShopList.isNotEmpty()) {
                val othersFavoriteShopsCount = favoriteShopList.size - MAX_FAVORITE_SHOPS_TO_SHOW
                val favoriteShopsToShow = favoriteShopList.take(ON_GOING_FAVORITE_SHOPS_TO_SHOW)
                val favoriteShopsItemModel = FavoriteShopListDataModel(favoriteShopsToShow, othersFavoriteShopsCount)

                //find shimmering and change with result value
                findShimmerPosition<ShimmerFavoriteShopDataModel>()?.let {
                    updateWidget(favoriteShopsItemModel, it)
                }
            } else {
                findShimmerPosition<ShimmerFavoriteShopDataModel>()?.let {
                    updateWidget(EmptyStateFavoriteShopDataModel(), it)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            findShimmerPosition<ShimmerFavoriteShopDataModel>()?.let {
                updateWidget(ErrorStateFavoriteShopDataModel(), it)
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    suspend fun getWishlist() {
        //find error state if available and change to shimmering
        val wishlistErrorState = _mainNavListVisitable.withIndex().find {
            it.value is ErrorStateWishlistDataModel
        }
        wishlistErrorState?.let {
            updateWidget(ShimmerWishlistDataModel(), it.index)
        }
        try {
            val wishlist = getWishlistNavUseCase.get().executeOnBackground()

            if (wishlist.isNotEmpty()) {
//                val othersWishlistCount = wishlist.size - MAX_FAVORITE_SHOPS_TO_SHOW
                val othersWishlistCount = 10 //temporary
                val wishlistToShow = wishlist.take(ON_GOING_FAVORITE_SHOPS_TO_SHOW)
                val wishlistModel = WishlistDataModel(wishlistToShow, othersWishlistCount)

//                find shimmering and change with result value
                findShimmerPosition<ShimmerWishlistDataModel>()?.let {
                    updateWidget(wishlistModel, it)
                }
            } else {
                findShimmerPosition<ShimmerWishlistDataModel>()?.let {
                    updateWidget(EmptyStateWishlistDataModel(), it)
                }
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
        } catch (e: Exception) {
            //find shimmering and change with result value
            findShimmerPosition<ShimmerWishlistDataModel>()?.let {
                updateWidget(ErrorStateWishlistDataModel(), it)
            }
            onlyForLoggedInUser { _allProcessFinished.postValue(Event(true)) }
            e.printStackTrace()
        }
    }

    private fun buildBUTitleList(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            return mutableListOf(
                it.getSectionTitle(IDENTIFIER_TITLE_ALL_CATEGORIES, isMePageUsingRollenceVariant)
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
                        it.getSectionTitle(ClientMenuGenerator.IDENTIFIER_TITLE_MY_ACTIVITY),
                        InitialShimmerTransactionRevampDataModel(),
                        it.getMenu(menuId = ClientMenuGenerator.ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER)
                )
            } else {
                transactionDataList = mutableListOf(
                        SeparatorDataModel(),
                        it.getSectionTitle(ClientMenuGenerator.IDENTIFIER_TITLE_MY_ACTIVITY),
                        it.getMenu(menuId = ClientMenuGenerator.ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                        it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER)
                )
            }
            return transactionDataList
        }
        return listOf()
    }

    private fun buildTransactionMenuListRevamp(): List<Visitable<*>> {
        clientMenuGenerator.get()?.let {
            var transactionDataList: MutableList<Visitable<*>> = mutableListOf()
            if (userSession.get().isLoggedIn) {
                transactionDataList = mutableListOf(
                    SeparatorDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_ORDER_HISTORY),
                        InitialShimmerTransactionRevampDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_WISHLIST),
                        ShimmerWishlistDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_FAVORITE_SHOP),
                        ShimmerFavoriteShopDataModel()
                )
            } else {
                transactionDataList = mutableListOf(
                        SeparatorDataModel(),
                        it.getSectionTitle(IDENTIFIER_TITLE_ORDER_HISTORY),
                        it.getSectionTitle(IDENTIFIER_TITLE_WISHLIST),
                        it.getSectionTitle(IDENTIFIER_TITLE_FAVORITE_SHOP)
                )
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
            updateWidget(accountModel.copy(
                profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopLoading = true, isGetShopError = true)
            ), INDEX_MODEL_ACCOUNT)

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
                val (_, canGoToSellerAccount, _) = adminDataCall.await()
                val result = (response.takeIf { it is Success } as? Success<ShopData>)?.data
                result?.let {
                    accountModel.run {
                        val shopName = it.userShopInfo.info.shopName
                        val shopId: String = if(it.userShopInfo.info.shopId.isBlank()) AccountHeaderDataModel.DEFAULT_SHOP_ID_NOT_OPEN else it.userShopInfo.info.shopId
                        val adminRole = null
                        val orderCount = getTotalOrderCount(it.notifications)
                        setUserShopName(shopName, shopId, orderCount)
                        setAdminData(adminRole, canGoToSellerAccount)
                    }
                    updateWidget(accountModel, INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

                val fail = (response.takeIf { it is Fail } )
                fail?.let {
                    updateWidget(accountModel.copy(
                        profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopError = true, isGetShopLoading = false)
                    ), INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

            }){
                updateWidget(accountModel.copy(
                    profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopError = true, isGetShopLoading = false)
                ), INDEX_MODEL_ACCOUNT)
            }
        }
    }

    fun refreshUserAffiliateData() {
        val newAccountData = _mainNavListVisitable.find {
            it is AccountHeaderDataModel
        } as? AccountHeaderDataModel
        newAccountData?.let { accountModel ->
            //set shimmering before getting the data
            updateWidget(accountModel.copy(
                profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateLoading = true, isGetAffiliateError = true)
            ), INDEX_MODEL_ACCOUNT)

            launchCatchError(coroutineContext, block = {
                val call = async {
                    withContext(baseDispatcher.get().io) {
                        getAffiliateUserUseCase.get().executeOnBackground()
                    }
                }
                val response = call.await()
                val result = (response.takeIf { it is Success } as? Success<AffiliateUserDetailData>)?.data
                result?.let {
                    accountModel.run {
                        setAffiliate(
                            isRegistered = it.affiliateUserDetail.isRegistered,
                            affiliateName = it.affiliateUserDetail.title,
                            affiliateAppLink = it.affiliateUserDetail.redirection.android,
                            isLoading = false
                        )
                    }
                    accountModel.profileAffiliateDataModel.isGetAffiliateError = false
                    updateWidget(accountModel, INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

                val fail = (response.takeIf { it is Fail } )
                fail?.let {
                    updateWidget(accountModel.copy(
                        profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateError = true, isGetAffiliateLoading = false)
                    ), INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }
            }){
                updateWidget(accountModel.copy(
                    profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateError = true, isGetAffiliateLoading = false)
                ), INDEX_MODEL_ACCOUNT)
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

    fun findComplainModelPosition(): Int {
        val findComplainModel = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id == ID_TOKOPEDIA_CARE
        }
        return _mainNavListVisitable.indexOf(findComplainModel)
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

    private inline fun <reified T>findShimmerPosition(): Int? {
        val shimmer = _mainNavListVisitable.firstOrNull {
            it is T
        }
        shimmer?.let{
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

    fun findBuStartIndexPosition(): Int? {
        if (isMePageUsingRollenceVariant) {
            val findBU = _mainNavListVisitable.firstOrNull {
                it is HomeNavExpandableDataModel && it.id == IDENTIFIER_TITLE_ALL_CATEGORIES
            }
            findBU.let {
                return _mainNavListVisitable.indexOf(it)
            }
        } else {
            val findBUTitle = _mainNavListVisitable.firstOrNull {
                it is HomeNavTitleDataModel && it.identifier == IDENTIFIER_TITLE_ALL_CATEGORIES
            }
            findBUTitle?.let {
                return _mainNavListVisitable.indexOf(it) + 1
            }
            return null
        }
    }

    private fun findExistingEndBuIndexPosition(): Int? {
        val findHomeMenu = _mainNavListVisitable.findLast {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.BU_ICON
        }
        findHomeMenu?.let {
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