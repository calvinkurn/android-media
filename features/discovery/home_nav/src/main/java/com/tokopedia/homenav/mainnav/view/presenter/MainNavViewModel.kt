package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.mainnav.domain.interactor.MainNavUseCase
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response.error
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
        private val baseDispatcher: Lazy<NavDispatcherProvider>,
        private val userSession: Lazy<UserSessionInterface>,
        private val mainNavUseCase: Lazy<MainNavUseCase>
): BaseViewModel(baseDispatcher.get().io()) {

    val mainNavLiveData: LiveData<Result<MainNavigationDataModel>>
        get() = _mainNavLiveData
    private val _mainNavLiveData: MutableLiveData<Result<MainNavigationDataModel>> = MutableLiveData()

    fun getMainNavData() {
        launchCatchError(coroutineContext, block = {
            mainNavUseCase.get().getMainNavData(userSession.get().shopId.toInt()).flowOn(baseDispatcher.get().io()).collect {
                _mainNavLiveData.postValue(Success(it))
            }
        }){
            _mainNavLiveData.postValue(Fail(it))
        }
    }
}