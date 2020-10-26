package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.NavCommandProcessor
import com.tokopedia.homenav.common.util.ResultCommandProcessor
import com.tokopedia.homenav.common.util.UpdateWidgetCommand
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import dagger.Lazy
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val navProcessor: Lazy<NavCommandProcessor>,
        private val getMainNavDataUseCase: Lazy<GetMainNavDataUseCase>
): BaseViewModel(baseDispatcher.get().io()), ResultCommandProcessor {

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

    val shopResultListener: LiveData<Result<AccountHeaderViewModel>>
        get() = _shopResultListener
    private val _shopResultListener: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    override suspend fun updateWidget(visitable: HomeNavVisitable, position: Int) {
        val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
        if(newMainLiveData.getOrNull(position)?.id() == visitable.id()){
            newMainLiveData[position] = visitable
        } else {
            newMainLiveData.indexOfFirst { it.id() == visitable.id() }.let { index ->
                newMainLiveData[index] = visitable
            }
        }
        withContext(baseDispatcher.get().ui()){
            _mainNavLiveData.value = _mainNavLiveData.value?.copy(dataList = newMainLiveData)
        }
    }

    override suspend fun addWidget(visitable: HomeNavVisitable, position: Int) {
    }

    override suspend fun deleteWidget(visitable: HomeNavVisitable, position: Int) {
    }

    override suspend fun updateNavData(navigationDataModel: MainNavigationDataModel) {
        withContext(baseDispatcher.get().ui()) {
            _mainNavLiveData.value = navigationDataModel
        }
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun getMainNavData() {
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
            getShopData(accountHeader.shopId.toInt(), accountHeader)
        }
    }

    fun getProfileSection(loginState: Int, shopId: Int) {
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

    fun getUserNameAndPictureData(loginState: Int, shopId: Int) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserInfoUseCase.get().executeOnBackground()
            }
            val accountData = AccountHeaderViewModel(
                    userName = result.profile.name,
                    userImage = result.profile.profilePicture,
                    loginState = loginState)
            _mainNavLiveData.postValue(MainNavigationDataModel(listOf(accountData)))

        }){
            _profileResultListener.postValue(Fail(it))
        }
    }

    fun getShopData(shopId: Int, accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            accountData.shopName = result.shopCore.name
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, this@MainNavViewModel))
        }){
            _shopResultListener.postValue(Fail(it))
        }
    }

    fun getUserBadgeImage(accountData: AccountHeaderViewModel) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            accountData.badge = result.tokopoints.status.tier.eggImageURL
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, this@MainNavViewModel))
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
            navProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(accountData.copy(), 0, this@MainNavViewModel))
        }){
            //post error get ovo with new livedata
            _ovoResultListener.postValue(Fail(it))
        }
    }
}