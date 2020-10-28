package com.tokopedia.recentview.view.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.recentview.di.RecentViewDispatcherProvider
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.listener.RecentView
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author by yoasfs on 13/08/20
 */

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class RecentViewViewModel @Inject constructor(
        private val baseDispatcher: RecentViewDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val recentViewUseCase: RecentViewUseCase
): BaseViewModel(baseDispatcher.ui()) {


    val recentViewResp : LiveData<Result<ArrayList<RecentViewDetailProductViewModel>>>
        get() = _recentViewResp
    private val _recentViewResp : MutableLiveData<Result<ArrayList<RecentViewDetailProductViewModel>>> = MutableLiveData()

    fun getRecentView() {
        recentViewUseCase.apply {
            getParam(userSession.userId)
        }.execute({
            _recentViewResp.value = Success(it)
        },{
            _recentViewResp.value = Fail(it)
        })
    }

}