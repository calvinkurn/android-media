package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.*
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_OPEN_SHOP_TICKER
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_QR_CODE
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_RECENT_VIEW
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_SUBSCRIPTION
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_TOKOPEDIA_CARE
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val getMainNavDataUseCase: Lazy<GetMainNavDataUseCase>,
        private val clientMenuGenerator: Lazy<ClientMenuGenerator>,
        private val userSession: Lazy<UserSessionInterface>
): BaseViewModel(baseDispatcher.get().io()) {
    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData(MainNavigationDataModel())
    private var _mainNavListVisitable = mutableListOf<HomeNavVisitable>()

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

    val shopResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _shopResultListener
    private val _shopResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    init {
        getMainNavData()
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun updateWidget(visitable: HomeNavVisitable, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList[position] = visitable
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun addWidget(visitable: HomeNavVisitable, position: Int) {
        val newMainNavList = _mainNavListVisitable
        newMainNavList.add(visitable)
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainNavList))
    }

    fun addWidgetList(visitables: List<HomeNavVisitable>) {
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

    fun setUserMenuList() {

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

    private fun buildUserMenuList(): List<HomeNavVisitable> {
        clientMenuGenerator.get()?.let {
            val firstSectionList = mutableListOf<HomeNavVisitable>(
                    it.getMenu(ID_WISHLIST_MENU),
                    it.getMenu(ID_FAVORITE_SHOP),
                    it.getMenu(ID_RECENT_VIEW),
                    it.getMenu(ID_SUBSCRIPTION)
            )
            val hasShop = userSession.get().hasShop()
            if (!hasShop) firstSectionList.add(it.getMenu(ID_OPEN_SHOP_TICKER))
            firstSectionList.add(SeparatorViewModel())

            val secondSectionList = listOf(
                    it.getMenu(ID_COMPLAIN),
                    it.getMenu(ID_TOKOPEDIA_CARE),
                    it.getMenu(ID_QR_CODE)
            )
            val completeList = firstSectionList.plus(secondSectionList)
            return completeList
        }
        return listOf()
    }

    private suspend fun getUserSection(){
        val mainNavigationDataModel: MainNavigationDataModel? = _mainNavLiveData.value
        mainNavigationDataModel?.dataList?.find { it is AccountHeaderViewModel }?.let {
            val accountHeader = (it as AccountHeaderViewModel).copy()
            getUserBadgeImage(accountHeader)
            getOvoData(accountHeader)
            getShopData(accountHeader.shopId.toInt(), accountHeader)
        }
    }

    private suspend fun getProfileSection(loginState: Int, shopId: Int) {
        when (loginState) {
            AccountHeaderViewModel.LOGIN_STATE_LOGIN -> {
                getUserNameAndPictureData(loginState, shopId)
            }
            AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS,
            AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN -> {
                val accountData = AccountHeaderViewModel(loginState = loginState)
                _mainNavLiveData.postValue(MainNavigationDataModel(listOf(accountData)))
            }
        }
    }

    private suspend fun getUserNameAndPictureData(loginState: Int, shopId: Int) {
        val result = getUserInfoUseCase.get().executeOnBackground()
        val accountData = AccountHeaderViewModel(
                userName = result.profile.name,
                userImage = result.profile.profilePicture,
                loginState = loginState)
        _mainNavLiveData.postValue(MainNavigationDataModel(listOf(accountData)))
    }

    private suspend fun getShopData(shopId: Int, accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            accountData.shopName = result.shopCore.name
            updateWidget(accountData.copy(), 0)
        }) {
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
        }) {
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
            //apply global error for mainnav
            _ovoResultListener.postValue(Fail(it))
        }
    }
}