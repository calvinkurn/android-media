package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.NavCommandProcessor
import com.tokopedia.homenav.common.util.ResultCommandProcessor
import com.tokopedia.homenav.common.util.UpdateNavigationData
import com.tokopedia.homenav.mainnav.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserMembershipUseCase
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val navProcessor: Lazy<NavCommandProcessor>
): BaseViewModel(baseDispatcher.get().io()), ResultCommandProcessor {

    val mainNavLiveData: LiveData<MainNavigationDataModel>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel> = MutableLiveData()

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

    override suspend fun updateWidget(visitable: Visitable<*>, position: Int) {
    }

    override suspend fun addWidget(visitable: Visitable<*>, position: Int) {
    }

    override suspend fun deleteWidget(visitable: Visitable<*>, position: Int) {
    }

    override suspend fun updateNavData(navigationDataModel: MainNavigationDataModel) {
        logChannelUpdate("Update channel: (Update all home data) data: ${navigationDataModel.dataList.map { it.javaClass.simpleName }}")
        withContext(baseDispatcher.get().ui()) {
            _mainNavLiveData.postValue(navigationDataModel)
        }
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }

    // ============================================================================================
    // ================================ Live Data Controller ======================================
    // ============================================================================================

    fun getMainNavData(loginState: Int, shopId: Int) {
        getProfileSection(loginState, shopId)
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
            getUserBadgeImage()
            getShopData(shopId)
            getOvoData()
        }){
            _profileResultListener.postValue(Fail(it))
        }

    }

    fun getShopData(shopId: Int) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            val dataList =  _mainNavLiveData.value?.dataList ?: listOf()
            val accountData = dataList.withIndex().find {
                (_, visitable) -> visitable is AccountHeaderViewModel
            }
            (accountData?.value as AccountHeaderViewModel).shopName = result.shopCore.name
            navProcessor.get().sendWithQueueMethod(UpdateNavigationData(
                    _mainNavLiveData.value!!, this@MainNavViewModel
            ))
        }){
            _shopResultListener.postValue(Fail(it))
        }
    }

    fun getUserBadgeImage() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            val dataList =  _mainNavLiveData.value?.dataList ?: listOf()
            val accountData = dataList.withIndex().find {
                (_, visitable) -> visitable is AccountHeaderViewModel
            }
            (accountData?.value as AccountHeaderViewModel).badge = result.tokopoints.status.tier.eggImageURL
            navProcessor.get().sendWithQueueMethod(UpdateNavigationData(
                    _mainNavLiveData.value!!, this@MainNavViewModel
            ))
        }){
            _membershipResultListener.postValue(Fail(it))
        }
    }

    fun getOvoData() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getWalletUseCase.get().executeOnBackground()
            }

            val dataList =  _mainNavLiveData.value?.dataList ?: listOf()
            val accountData = dataList.withIndex().find {
                (_, visitable) -> visitable is AccountHeaderViewModel
            }
            (accountData?.value as AccountHeaderViewModel).let {
                it.ovoSaldo = result.cashBalance
                it.ovoPoint = result.pointBalance
            }
            navProcessor.get().sendWithQueueMethod(UpdateNavigationData(
                    _mainNavLiveData.value!!, this@MainNavViewModel
            ))
        }){
            //post error get ovo with new livedata
            _ovoResultListener.postValue(Fail(it))
        }
    }

}