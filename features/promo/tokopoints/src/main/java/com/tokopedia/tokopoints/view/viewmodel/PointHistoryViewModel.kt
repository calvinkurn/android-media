package com.tokopedia.tokopoints.view.viewmodel

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity
import com.tokopedia.tokopoints.view.respository.PointHistoryRepository
import com.tokopedia.tokopoints.view.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointHistoryViewModel @Inject constructor(val mUserRepository: PointHistoryRepository) : BaseViewModel(Dispatchers.Main) {


    val data = MutableLiveData<Resources<TokoPointEntity>>()
    val listLoading = MutableLiveData<Boolean>()

    init {
        hitApi()
    }

    private fun hitApi() {
        launch {
            listLoading.value = true
            mUserRepository.getPointsDetail(data)
        }
    }


    fun onErrorButtonClicked(toString: String, context: Context) {
        if (toString.equals(context.getString(R.string.tp_history_btn_action), ignoreCase = true)) {
            RouteManager.route(context, ApplinkConst.HOME)
        } else {
            hitApi()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mUserRepository.onCleared()
    }
}