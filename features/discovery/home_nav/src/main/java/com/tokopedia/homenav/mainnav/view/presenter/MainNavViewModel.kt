package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
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
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

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


    fun getMainNavData(loginState: Int, shopId: Int) {
        getProfileSection(loginState, shopId)
    }

    fun getProfileSection(loginState: Int, shopId: Int) {
        when (loginState) {
            AccountHeaderViewModel.LOGIN_STATE_LOGIN -> {
                getUserNameAndPictureData(loginState)
                getUserBadgeImage()
                getShopData(shopId)
                getOvoData()
            }
            AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS,
            AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN -> {
                val accountData = AccountHeaderViewModel(loginState = loginState)
                _mainNavLiveData.postValue(MainNavigationDataModel(listOf(accountData)))
            }
        }
    }

    fun getUserNameAndPictureData(loginState: Int) {
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

    fun getShopData(shopId: Int) {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = shopId)
                getShopInfoUseCase.get().executeOnBackground()
            }
            val accountData = _mainNavLiveData.value?.dataList?.firstOrNull()
            (accountData as AccountHeaderViewModel).shopName = result.shopCore.name

            _mainNavLiveData.postValue( _mainNavLiveData.value)
        }){
            _shopResultListener.postValue(Fail(it))
        }
    }

    fun getUserBadgeImage() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            val accountData = _mainNavLiveData.value?.dataList?.firstOrNull()
            (accountData as AccountHeaderViewModel).badge = result.tokopoints.status.tier.eggImageURL
            _mainNavLiveData.postValue(_mainNavLiveData.value)
        }){
            _membershipResultListener.postValue(Fail(it))
        }
    }

    fun getOvoData() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getWalletUseCase.get().executeOnBackground()
            }
            val accountData = _mainNavLiveData.value?.dataList?.firstOrNull()
            (accountData as AccountHeaderViewModel).let {
                it.ovoSaldo = result.cashBalance
                it.ovoPoint = result.pointBalance
            }
            _mainNavLiveData.postValue(_mainNavLiveData.value)
        }){
            //post error get ovo with new livedata
            _ovoResultListener.postValue(Fail(it))
        }
    }

}