package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.homenav.MePageRollenceController.isUsingMePageRollenceVariant
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_ALL_CATEGORIES
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_HELP_CENTER
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.IDENTIFIER_TITLE_MY_ACTIVITY
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.model.MainNavProfileCache
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.usecases.GetAffiliateUserUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetNavNotification
import com.tokopedia.homenav.mainnav.domain.usecases.GetPaymentOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetProfileDataUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetReviewProductUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetUohOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetWishlistNavUseCase
import com.tokopedia.homenav.mainnav.view.datamodel.ErrorStateBuDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.ErrorStateOngoingTransactionModel
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerProfileDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerTransactionDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavigationDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.TransactionListItemDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_FAILED
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_LOADING
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_SUCCESS
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileMembershipDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.TokopediaPlusDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionAdminData
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionShopData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusCons
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
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
    private val getTokopediaPlusUseCase: Lazy<TokopediaPlusUseCase>
) : BaseViewModel(baseDispatcher.get().io) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val INDEX_HOME_BACK_SEPARATOR = 1
        private const val ON_GOING_TRANSACTION_TO_SHOW = 6

        private const val INDEX_DEFAULT_ALL_CATEGORY = 8

        private const val SOURCE = "dave_home_nav"
    }

    // network process live data, false if it is processing and true if it is finished
    val networkProcessLiveData: LiveData<Boolean>
        get() = _networkProcessLiveData
    private val _networkProcessLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var navNotification: NavNotificationModel = NavNotificationModel()

    private var pageSource: NavSource = NavSource.DEFAULT
    private var mainNavProfileCache: MainNavProfileCache? = null

    private var _mainNavListVisitable = mutableListOf<Visitable<*>>()

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(
        MainNavigationDataModel(
            dataList = _mainNavListVisitable
        )
    )

    val profileDataLiveData: LiveData<AccountHeaderDataModel>
        get() = _profileDataLiveData
    private val _profileDataLiveData: MutableLiveData<AccountHeaderDataModel> = MutableLiveData()

    private var allCategoriesCache = listOf<Visitable<*>>()

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    private fun updateWidget(visitable: Visitable<*>, position: Int) {
        try {
            val newMainNavList = _mainNavListVisitable
            newMainNavList[position] = visitable
            _mainNavListVisitable = newMainNavList
            _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
        } catch (_: Exception) { }
    }

    private fun addWidgetList(visitables: List<Visitable<*>>, position: Int) {
        try {
            val newMainNavList = _mainNavListVisitable
            newMainNavList.addAll(position, visitables)
            _mainNavListVisitable = newMainNavList
            _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList.toMutableList()))
        } catch (_: Exception) { }
    }

    private fun deleteWidget(visitable: Visitable<*>) {
        try {
            val newMainNavList = _mainNavListVisitable.toMutableList()
            newMainNavList.remove(visitable)
            _mainNavListVisitable = newMainNavList
            _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
        } catch (_: Exception) { }
    }

    private fun deleteWidgetList(visitables: List<Visitable<*>>) {
        try {
            val newMainNavList = _mainNavListVisitable.toMutableList()
            newMainNavList.removeAll(visitables)
            _mainNavListVisitable = newMainNavList
            _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
        } catch (_: Exception) { }
    }

    fun setPageSource(pageSource: NavSource = NavSource.DEFAULT) {
        this.pageSource = pageSource
        if (isLaunchedFromHome()) {
            removeHomeBackButtonMenu()
        } else { addHomeBackButtonMenu() }
    }

    fun setProfileCache(mainNavProfileCache: MainNavProfileCache?) {
        this.mainNavProfileCache = mainNavProfileCache
    }

    private fun isLaunchedFromHome(): Boolean {
        return pageSource == NavSource.HOME ||
            pageSource == NavSource.HOME_UOH ||
            pageSource == NavSource.HOME_WISHLIST ||
            pageSource == NavSource.SOS
    }

    fun getPageSource(): NavSource {
        return pageSource
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun setInitialState() {
        val initialList = mutableListOf<Visitable<*>>()
        if (userSession.get().isLoggedIn) {
            initialList.add(AccountHeaderDataModel(state = NAV_PROFILE_STATE_LOADING))
        } else {
            initialList.add(AccountHeaderDataModel(loginState = getLoginState(), state = NAV_PROFILE_STATE_SUCCESS))
        }
        initialList.addTransactionMenu()
        initialList.addBUTitle()
        initialList.addUserMenu()
        _mainNavListVisitable = initialList
        _mainNavLiveData.postValue(MainNavigationDataModel(_mainNavListVisitable))
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
            } else {
                getBuListMenu()
            }
            // update cached data with cloud data
            onlyForLoggedInUser { getNotification() }
            onlyForLoggedInUser { updateProfileData() }
            onlyForLoggedInUser { getOnGoingTransaction() }
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
        this.add(InitialShimmerDataModel())
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
        listOfHomeMenuSection.add(clientMenuGenerator.get().getMenu(menuId = ID_HOME, sectionId = MainNavConst.Section.HOME))
        listOfHomeMenuSection.add(SeparatorDataModel(sectionId = MainNavConst.Section.HOME, isMePageVariant = true))
        addWidgetList(listOfHomeMenuSection, INDEX_HOME_BACK_SEPARATOR)
    }

    private suspend fun getBuListMenuCached() {
        viewModelScope.launch {
            try {
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                // PLT network process is finished
                _networkProcessLiveData.postValue(true)
                allCategoriesCache = result
                val shimmeringDataModel = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
                findBuStartIndexPosition()?.let {
                    addWidgetList(result, it)
                }
            } catch (_: Exception) { }
            val shimmeringDataModel = _mainNavListVisitable.find {
                it is InitialShimmerDataModel
            }
            shimmeringDataModel?.let { deleteWidget(shimmeringDataModel) }
            getBuListMenu()
        }
    }

    private fun getBuListMenu() {
        addWidgetList(listOf(InitialShimmerDataModel()), findBuStartIndexPosition() ?: INDEX_DEFAULT_ALL_CATEGORY)
        viewModelScope.launch {
            try {
                getCategoryGroupUseCase.get().createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.get().setStrategyCloudThenCache()
                val result = getCategoryGroupUseCase.get().executeOnBackground()

                // PLT network process is finished
                _networkProcessLiveData.postValue(true)
                val shimmeringDataModel = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                shimmeringDataModel?.let { deleteWidget(it) }
                findBuStartIndexPosition()?.let {
                    if (findExistingEndBuIndexPosition() == null) {
                        addWidgetList(result, it)
                    }
                }
            } catch (e: Exception) {
                val buShimmering = _mainNavListVisitable.find {
                    it is InitialShimmerDataModel
                }
                buShimmering?.let {
                    if (allCategoriesCache.isNotEmpty()) {
                        addWidgetList(allCategoriesCache, _mainNavListVisitable.indexOf(it))
                        deleteWidget(buShimmering)
                    } else {
                        updateWidget(ErrorStateBuDataModel(), _mainNavListVisitable.indexOf(it))
                    }
                }
            }
        }
    }

    private fun getProfileDataCached() {
        mainNavProfileCache?.let {
            val accountHeaderModel = AccountHeaderDataModel(
                profileDataModel = ProfileDataModel(
                    userName = it.profileName ?: "",
                    userImage = it.profilePicUrl ?: ""
                ),
                profileMembershipDataModel = ProfileMembershipDataModel(
                    badge = it.memberStatusIconUrl ?: ""
                ),
                isCacheData = true,
                state = NAV_PROFILE_STATE_SUCCESS,
                profileSellerDataModel = ProfileSellerDataModel(
                    isGetShopLoading = true
                ),
                profileAffiliateDataModel = ProfileAffiliateDataModel(
                    isGetAffiliateLoading = true
                ),
                tokopediaPlusDataModel = TokopediaPlusDataModel(
                    isGetTokopediaPlusLoading = true
                )
            )
            updateWidget(accountHeaderModel, INDEX_MODEL_ACCOUNT)
        }
    }

    private suspend fun updateProfileData() {
        viewModelScope.launch {
            try {
                val accountHeaderModel = getProfileDataUseCase.get().executeOnBackground()
                val adminData = getAdminData()

                accountHeaderModel.apply {
                    adminData?.let { adminData ->
                        setAdminData(adminData.data)
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
                            account.copy(
                                state = NAV_PROFILE_STATE_FAILED
                            ),
                            INDEX_MODEL_ACCOUNT
                        )
                    }
                }
            }
        }
    }

    fun refreshBuListData() {
        launchCatchError(coroutineContext, block = {
            findBuStartIndexPosition()?.let {
                updateWidget(InitialShimmerDataModel(), it)
            }
            getBuListMenu()
        }) { }
    }

    fun refreshTransactionListData() {
        launch {
            getOnGoingTransaction()
        }
    }

    private fun getOrderHistory(
        paymentList: List<NavPaymentOrder>,
        productOrderList: List<NavProductOrder>
    ): Pair<List<NavPaymentOrder>, List<NavProductOrder>> {
        return Pair(
            paymentList,
            productOrderList.take(ON_GOING_TRANSACTION_TO_SHOW)
        )
    }

    private suspend fun getOnGoingTransaction() {
        // find error state if available and change to shimmering
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
                val othersTransactionCount = orderList.size - ON_GOING_TRANSACTION_TO_SHOW

                val (paymentListToShow, orderListToShow) = getOrderHistory(paymentList, orderList)

                val transactionListItemViewModel = TransactionListItemDataModel(
                    NavOrderListModel(orderListToShow, paymentListToShow),
                    othersTransactionCount
                )

                findShimmerPosition<InitialShimmerTransactionDataModel>()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
            } else {
                deleteWidget(InitialShimmerTransactionDataModel())
            }
        } catch (e: Exception) {
            // find shimmering and change with result value
            findShimmerPosition<InitialShimmerTransactionDataModel>()?.let {
                updateWidget(ErrorStateOngoingTransactionModel(), it)
            }
        }
    }

    private fun buildBUTitleList(): List<Visitable<*>> {
        clientMenuGenerator.get().let {
            return mutableListOf(
                it.getSectionTitle(IDENTIFIER_TITLE_ALL_CATEGORIES)
            )
        }
    }

    private fun buildUserMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get().let {
            val firstSectionList = mutableListOf<Visitable<*>>(
                it.getSectionTitle(IDENTIFIER_TITLE_HELP_CENTER),
                it.getMenu(menuId = ID_COMPLAIN, sectionId = MainNavConst.Section.USER_MENU),
                it.getMenu(menuId = ID_TOKOPEDIA_CARE, sectionId = MainNavConst.Section.USER_MENU)
            )
            firstSectionList.add(SeparatorDataModel(isMePageVariant = true))

            val secondSectionList = listOf(
                it.getMenu(menuId = ID_QR_CODE, sectionId = MainNavConst.Section.USER_MENU)
            )
            val completeList = firstSectionList.plus(secondSectionList)
            return completeList
        }
    }

    private fun buildTransactionMenuList(): List<Visitable<*>> {
        clientMenuGenerator.get().let {
            var transactionDataList: MutableList<Visitable<*>> = mutableListOf()
            if (userSession.get().isLoggedIn) {
                transactionDataList = mutableListOf(
                    it.getSectionTitle(IDENTIFIER_TITLE_MY_ACTIVITY),
                    InitialShimmerTransactionDataModel(),
                    it.getMenu(menuId = ClientMenuGenerator.ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER)
                )
            } else {
                transactionDataList = mutableListOf(
                    it.getSectionTitle(IDENTIFIER_TITLE_MY_ACTIVITY),
                    it.getMenu(menuId = ClientMenuGenerator.ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER),
                    it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER)
                )
            }
            return transactionDataList
        }
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
            } catch (_: Exception) { }
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
            // set shimmering before getting the data
            updateWidget(
                accountModel.copy(
                    profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopLoading = true, isGetShopError = true)
                ),
                INDEX_MODEL_ACCOUNT
            )

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
                val adminData = adminDataCall.await()
                val result = (response.takeIf { it is Success } as? Success<ShopData>)?.data
                result?.let {
                    accountModel.run {
                        val shopName = it.userShopInfo.info.shopName
                        val shopId: String = if (it.userShopInfo.info.shopId.isBlank()) AccountHeaderDataModel.DEFAULT_SHOP_ID_NOT_OPEN else it.userShopInfo.info.shopId
                        val orderCount = getTotalOrderCount(it.notifications)
                        setUserShopName(shopName, shopId, orderCount)
                        setAdminData(adminData?.data)
                    }
                    updateWidget(accountModel, INDEX_MODEL_ACCOUNT)
                    return@launchCatchError
                }

                val fail = (response.takeIf { it is Fail })
                fail?.let {
                    updateWidget(
                        accountModel.copy(
                            profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopError = true, isGetShopLoading = false)
                        ),
                        INDEX_MODEL_ACCOUNT
                    )
                    return@launchCatchError
                }
            }) {
                updateWidget(
                    accountModel.copy(
                        profileSellerDataModel = accountModel.profileSellerDataModel.copy(isGetShopError = true, isGetShopLoading = false)
                    ),
                    INDEX_MODEL_ACCOUNT
                )
            }
        }
    }

    fun refreshUserAffiliateData() {
        val newAccountData = _mainNavListVisitable.find {
            it is AccountHeaderDataModel
        } as? AccountHeaderDataModel
        newAccountData?.let { accountModel ->
            // set shimmering before getting the data
            updateWidget(
                accountModel.copy(
                    profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateLoading = true, isGetAffiliateError = true)
                ),
                INDEX_MODEL_ACCOUNT
            )

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

                val fail = (response.takeIf { it is Fail })
                fail?.let {
                    updateWidget(
                        accountModel.copy(
                            profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateError = true, isGetAffiliateLoading = false)
                        ),
                        INDEX_MODEL_ACCOUNT
                    )
                    return@launchCatchError
                }
            }) {
                updateWidget(
                    accountModel.copy(
                        profileAffiliateDataModel = accountModel.profileAffiliateDataModel.copy(isGetAffiliateError = true, isGetAffiliateLoading = false)
                    ),
                    INDEX_MODEL_ACCOUNT
                )
            }
        }
    }

    fun refreshTokopediaPlusData() {
        val newAccountData = _mainNavListVisitable.find {
            it is AccountHeaderDataModel
        } as? AccountHeaderDataModel
        newAccountData?.let { accountModel ->
            // set shimmering before getting the data
            updateWidget(
                accountModel.copy(
                    tokopediaPlusDataModel = accountModel.tokopediaPlusDataModel.copy(isGetTokopediaPlusLoading = true)
                ),
                INDEX_MODEL_ACCOUNT
            )

            launchCatchError(coroutineContext, block = {
                val response = getTokopediaPlusUseCase.get().invoke(
                    mapOf(
                        TokopediaPlusUseCase.PARAM_SOURCE to TokopediaPlusCons.SOURCE_GLOBAL_MENU
                    )
                )
                val result = response.tokopediaPlus

                accountModel.setTokopediaPlus(
                    tokopediaPlusParam = TokopediaPlusParam(TokopediaPlusCons.SOURCE_GLOBAL_MENU, result),
                    isLoading = false,
                    error = null
                )

                updateWidget(accountModel, INDEX_MODEL_ACCOUNT)
            }) {
                updateWidget(
                    accountModel.copy(
                        tokopediaPlusDataModel = accountModel.tokopediaPlusDataModel.copy(
                            isGetTokopediaPlusLoading = false,
                            tokopediaPlusError = it
                        )
                    ),
                    INDEX_MODEL_ACCOUNT
                )
            }
        }
    }

    fun reloadMainNavAfterLogin() {
        getMainNavData(false)
    }

    fun refreshProfileData() {
        updateWidget(InitialShimmerProfileDataModel(), INDEX_MODEL_ACCOUNT)
        launch {
            updateProfileData()
        }
    }

    private suspend fun onlyForLoggedInUser(function: suspend () -> Unit) {
        if (userSession.get().isLoggedIn) function.invoke()
    }

    /**
     * Check for account admin info if is not shop owner
     * and update shop related user session values accordingly
     *
     * @return Triple of admin role text (if is admin), boolean to determine if seller can go to
     *          account page, and boolean if shop is active
     */
    private suspend fun getAdminData(): AdminDataResponse? {
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
                if (isShopActive) {
                    it.shopId
                } else {
                    ""
                }
            userSession.get().refreshUserSessionShopData(it.copy(shopId = shopId))
        }

        return adminDataResponse
    }

    private inline fun <reified T> findShimmerPosition(): Int? {
        val shimmer = _mainNavListVisitable.firstOrNull {
            it is T
        }
        shimmer?.let {
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }

    private fun findBuStartIndexPosition(): Int? {
        val findBUTitle = _mainNavListVisitable.firstOrNull {
            it is HomeNavTitleDataModel && it.identifier == IDENTIFIER_TITLE_ALL_CATEGORIES
        }
        findBUTitle?.let {
            return _mainNavListVisitable.indexOf(it) + 1
        }
        return null
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

    private fun findMenu(menuId: Int): HomeNavMenuDataModel? {
        val findExistingMenu = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id() == menuId
        } as? HomeNavMenuDataModel
        return findExistingMenu
    }

    private fun HomeNavMenuDataModel.updateBadgeCounter(counter: String) {
        val indexOfMenu = _mainNavListVisitable.indexOf(this)
        updateWidget(this.copy(notifCount = counter), indexOfMenu)
    }
}
