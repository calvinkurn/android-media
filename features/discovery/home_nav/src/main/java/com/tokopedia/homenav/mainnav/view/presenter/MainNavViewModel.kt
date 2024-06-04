package com.tokopedia.homenav.mainnav.view.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_BUY_AGAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.data.mapper.BuyAgainMapper
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.model.MainNavProfileCache
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.domain.usecases.GetAffiliateUserUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetNavNotification
import com.tokopedia.homenav.mainnav.domain.usecases.GetPaymentOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetProfileDataUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetReviewProductUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetUohOrdersNavUseCase
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerProfileDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerTransactionRevampDataModel
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
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.ShimmerBuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoGqlParam
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
    private val userSession: Lazy<UserSessionInterface>,
    private val baseDispatcher: Lazy<CoroutineDispatchers>,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
    private val getNavNotification: Lazy<GetNavNotification>,
    private val getUohOrdersNavUseCase: Lazy<GetUohOrdersNavUseCase>,
    private val getPaymentOrdersNavUseCase: Lazy<GetPaymentOrdersNavUseCase>,
    private val getProfileDataUseCase: Lazy<GetProfileDataUseCase>,
    private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
    private val accountAdminInfoUseCase: Lazy<AccountAdminInfoUseCase>,
    private val getAffiliateUserUseCase: Lazy<GetAffiliateUserUseCase>,
    private val getReviewProductUseCase: Lazy<GetReviewProductUseCase>,
    private val getTokopediaPlusUseCase: Lazy<TokopediaPlusUseCase>
) : BaseViewModel(baseDispatcher.get().io) {

    companion object {
        private const val INDEX_MODEL_ACCOUNT = 0
        private const val INDEX_HOME_BACK_SEPARATOR = 1
        private const val MAX_CARD_SHOWN_REVAMP = 5

        private const val SOURCE = "dave_home_nav"
        private const val PAGE_NAME_BUY_AGAIN = "buy_it_again_me"
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

    /**
     * Pair here consist of Boolean and String
     * Boolean is whether there is error
     * String contains the error message
     */
    val onAtcProductState: LiveData<Pair<Boolean, String>> get() = _onAtcProductState
    private val _onAtcProductState: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

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

    private inline fun <reified T> deleteWidget() {
        try {
            val newMainNavList = _mainNavListVisitable.toMutableList()
            newMainNavList.removeAll { it is T }
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
            addMePageProfileSeparator()
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
            }
            // update cached data with cloud data
            onlyForLoggedInUser { getNotification() }
            onlyForLoggedInUser { updateProfileData() }
            onlyForLoggedInUser { getReview() }
            onlyForLoggedInUser { getBuyAgain() }
            onlyForLoggedInUser { getOnGoingTransaction() }
        }
    }

    fun addToCartProduct(productId: String, shopId: String) {
        // set param
        val param = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId
        )

        addToCartUseCase.get().setParams(param)

        viewModelScope.launch {
            try {
                val result = addToCartUseCase.get().executeOnBackground()
                withContext(baseDispatcher.get().main) {
                    _onAtcProductState.value = Pair(
                        result.isStatusError(),
                        result.getAtcErrorMessage().orEmpty()
                    )
                }
            } catch (t: Throwable) {
                _onAtcProductState.value = Pair(true, t.message.orEmpty())
            }
        }
    }

    private fun MutableList<Visitable<*>>.addTransactionMenu() {
        val transactionList = clientMenuGenerator.get().let {
            val isLoggedIn = userSession.get().isLoggedIn
            mutableListOf<Visitable<*>>().apply {
                add(it.getMenu(menuId = ID_ALL_TRANSACTION, sectionId = MainNavConst.Section.ORDER, showCta = isLoggedIn))
                if (isLoggedIn) add(InitialShimmerTransactionRevampDataModel())
                add(it.getMenu(menuId = ID_REVIEW, sectionId = MainNavConst.Section.ORDER, showCta = isLoggedIn))
                if (isLoggedIn) add(ShimmerReviewDataModel())
                add(it.getMenu(menuId = ID_BUY_AGAIN, sectionId = MainNavConst.Section.ORDER, showCta = isLoggedIn))
                if (isLoggedIn) add(ShimmerBuyAgainUiModel())
                add(it.getMenu(menuId = ID_WISHLIST_MENU, sectionId = MainNavConst.Section.ORDER))
                add(it.getMenu(menuId = ID_FAVORITE_SHOP, sectionId = MainNavConst.Section.ORDER))
                add(SeparatorDataModel(sectionId = MainNavConst.Section.ORDER))
            }
        }
        this.addAll(transactionList)
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
        listOfHomeMenuSection.add(clientMenuGenerator.get().getMenu(menuId = ID_HOME, sectionId = MainNavConst.Section.HOME))
        listOfHomeMenuSection.add(SeparatorDataModel(sectionId = MainNavConst.Section.HOME))
        addWidgetList(listOfHomeMenuSection, INDEX_HOME_BACK_SEPARATOR)
    }

    private fun addMePageProfileSeparator() {
        val listOfHomeMenuSection = mutableListOf<Visitable<*>>()
        listOfHomeMenuSection.add(SeparatorDataModel(sectionId = MainNavConst.Section.PROFILE))
        addWidgetList(listOfHomeMenuSection, INDEX_HOME_BACK_SEPARATOR)
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

    fun refreshReviewData() {
        launch {
            getReview()
        }
    }

    fun refreshTransactionListData() {
        launch {
            getOnGoingTransaction()
        }
    }

    fun refreshBuyAgainData() {
        launch {
            getBuyAgain()
        }
    }

    private suspend fun getOnGoingTransaction() {
        try {
            val paymentList = getPaymentOrdersNavUseCase.get().executeOnBackground()
            val orderList = getUohOrdersNavUseCase.get().executeOnBackground()

            if (paymentList.isNotEmpty() || orderList.isNotEmpty()) {
                val paymentListToShow = paymentList.take(MAX_CARD_SHOWN_REVAMP)
                val orderListToShow = orderList.take(MAX_CARD_SHOWN_REVAMP - paymentListToShow.size)
                val otherTransaction = paymentList.size + orderList.size - MAX_CARD_SHOWN_REVAMP

                val transactionListItemViewModel = TransactionListItemDataModel(
                    NavOrderListModel(orderListToShow, paymentListToShow),
                    otherTransaction
                )

                findPosition<InitialShimmerTransactionRevampDataModel>()?.let {
                    updateWidget(transactionListItemViewModel, it)
                }
                updateMenu(ID_ALL_TRANSACTION, showCta = true)
            } else {
                deleteWidget<InitialShimmerTransactionRevampDataModel>()
                updateMenu(ID_ALL_TRANSACTION, showCta = false)
            }
        } catch (_: Exception) {
            deleteWidget<InitialShimmerTransactionRevampDataModel>()
            updateMenu(ID_ALL_TRANSACTION, showCta = false)
        }
    }

    private suspend fun getReview() {
        try {
            val reviewList = getReviewProductUseCase.get().executeOnBackground()

            if (reviewList.isNotEmpty()) {
                val position = findPosition<ShimmerReviewDataModel>() ?: findPosition<ReviewListDataModel>()
                position?.let {
                    updateWidget(
                        ReviewListDataModel(reviewList.take(MAX_CARD_SHOWN_REVAMP)),
                        it
                    )
                }
                updateMenu(ID_REVIEW, showCta = true)
            } else {
                deleteWidget<ShimmerReviewDataModel>()
                updateMenu(ID_REVIEW, showCta = false)
            }
        } catch (_: Exception) {
            deleteWidget<ShimmerReviewDataModel>()
            updateMenu(ID_REVIEW, showCta = false)
        }
    }

    private suspend fun getBuyAgain() {
        val param = GetRecommendationRequestParam(pageName = PAGE_NAME_BUY_AGAIN)

        val recommendation = getRecommendationUseCase.get().getData(param)
        val recommendationItem = recommendation.firstOrNull()?.recommendationItemList

        if (recommendation.isNotEmpty() && recommendationItem?.isNotEmpty() == true) {
            val position = findPosition<ShimmerBuyAgainUiModel>() ?: findPosition<BuyAgainUiModel>()
            val result = BuyAgainMapper.map(recommendationItem)

            position?.let {
                updateWidget(BuyAgainUiModel(result), it)
                updateMenu(ID_BUY_AGAIN, counter = "1", showCta = true)
            }
        } else {
            deleteWidget<ShimmerBuyAgainUiModel>()
            updateMenu(ID_BUY_AGAIN, showCta = false)
        }
    }

    private fun buildUserMenuList(): List<Visitable<*>> {
        return clientMenuGenerator.get().let {
            mutableListOf<Visitable<*>>().apply {
                add(it.getMenu(menuId = ID_COMPLAIN, sectionId = MainNavConst.Section.USER_MENU))
                add(it.getMenu(menuId = ID_TOKOPEDIA_CARE, sectionId = MainNavConst.Section.USER_MENU))
                add(it.getMenu(menuId = ID_QR_CODE, sectionId = MainNavConst.Section.USER_MENU))
            }
        }
    }

    private suspend fun getNotification() {
        launch {
            try {
                val result = getNavNotification.get().executeOnBackground()
                val complainNotification = result.unreadCountComplain
                val inboxTicketNotification = result.unreadCountInboxTicket
                navNotification = NavNotificationModel(
                    unreadCountComplain = complainNotification,
                    unreadCountInboxTicket = inboxTicketNotification
                )
                if (complainNotification.isMoreThanZero()) updateMenu(ID_COMPLAIN, complainNotification.toString())
                if (inboxTicketNotification.isMoreThanZero()) updateMenu(ID_TOKOPEDIA_CARE, inboxTicketNotification.toString())
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
                        setUserShopName(shopName, shopId, orderCount, isShopPending = it.userShopInfo.reserveStatusInfo.isShopPending())
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
                accountAdminInfoUseCase.get()(
                    AccountAdminInfoGqlParam(
                        source = SOURCE,
                        isLocationAdmin = userSession.get().isLocationAdmin
                    )
                )
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

    private inline fun <reified T> findPosition(): Int? {
        val model = _mainNavListVisitable.firstOrNull {
            it is T
        }
        model?.let {
            return _mainNavListVisitable.indexOf(it)
        }
        return null
    }

    private fun updateMenu(
        menuId: Int,
        counter: String? = null,
        showCta: Boolean? = null
    ) {
        val existingMenu = _mainNavListVisitable.find {
            it is HomeNavMenuDataModel && it.id() == menuId
        } as? HomeNavMenuDataModel
        existingMenu?.let {
            val indexOfMenu = _mainNavListVisitable.indexOf(it)
            val newNotifCount = counter ?: it.notifCount
            val newShowCta = showCta ?: it.showCta
            updateWidget(it.copy(notifCount = newNotifCount, showCta = newShowCta), indexOfMenu)
        }
    }
}
