package com.tokopedia.homenav.mainnav.view.presenter

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response.error
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        @ApplicationContext private val context: Context,
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val userSession: Lazy<UserSessionInterface>,
        private val getUserInfoUseCase: Lazy<GetUserInfoUseCase>,
        private val getUserMembershipUseCase: Lazy<GetUserMembershipUseCase>,
        private val getShopInfoUseCase: Lazy<GetShopInfoUseCase>,
        private val getWalletUseCase: Lazy<GetCoroutineWalletBalanceUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    val mainNavLiveData: LiveData<Result<MainNavigationDataModel>>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<Result<MainNavigationDataModel>> = MutableLiveData()

    val accountLiveData: LiveData<Result<AccountHeaderViewModel>>
        get() = _accountLiveData
    private val _accountLiveData: MutableLiveData<Result<AccountHeaderViewModel>> = MutableLiveData()

    fun getMainNavData() {
        getProfileSection()

//        launchCatchError(coroutineContext, block = {
//            mainNavUseCase.get().getMainNavData(userSession.get().shopId.toInt()).flowOn(baseDispatcher.get().io()).collect {
//                _mainNavLiveData.postValue(Success(it))
//            }
//        }){
//            _mainNavLiveData.postValue(Fail(it))
//        }
    }

    private fun getProfileSection() {
        getUserNameAndPictureData()
        getUserBadgeImage()
        getShopData()
        getOvoData()
    }

    private fun getUserNameAndPictureData() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserInfoUseCase.get().executeOnBackground()
            }
            _accountLiveData.postValue(Success(AccountHeaderViewModel(
                    userName = result.profile.name,
                    userImage = result.profile.profilePicture,
                    loginState = when {
                        userSession.get().isLoggedIn -> AccountHeaderViewModel.LOGIN_STATE_LOGIN
                        haveUserLogoutData() -> AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
                        else -> AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN
                    })))
        }){
            _accountLiveData.postValue(Fail(it))
        }

    }

    private fun getShopData() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getShopInfoUseCase.get().params = GetShopInfoUseCase.createParam(partnerId = userSession.get().shopId.toInt())
                getShopInfoUseCase.get().executeOnBackground()
            }
            _accountLiveData.postValue(Success(AccountHeaderViewModel(shopName = result.shopCore.name)))
        }){
            _accountLiveData.postValue(Fail(it))
        }
    }

    private fun getUserBadgeImage() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getUserMembershipUseCase.get().executeOnBackground()
            }
            _accountLiveData.postValue(Success(AccountHeaderViewModel(badge = result.tokopoints.status.tier.eggImageURL)))
        }){
            _accountLiveData.postValue(Fail(it))
        }
    }

    private fun getOvoData() {
        launchCatchError(coroutineContext, block = {
            val result = withContext(baseDispatcher.get().io()) {
                getWalletUseCase.get().executeOnBackground()
            }
            _accountLiveData.postValue(Success(AccountHeaderViewModel(ovoSaldo = result.cashBalance, ovoPoint = result.pointBalance)))
        }){
            _accountLiveData.postValue(Fail(it))
        }
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderViewModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }
}