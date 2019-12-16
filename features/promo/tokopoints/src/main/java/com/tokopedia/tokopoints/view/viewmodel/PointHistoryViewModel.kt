package com.tokopedia.tokopoints.view.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.PointHistoryBase
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryRepository
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointHistoryViewModel @Inject constructor(val mUserRepository: PointHistoryRepository) : BaseViewModel(Dispatchers.Main), AdapterCallback {


    val data = MutableLiveData<Resources<TokoPointEntity>>()
    val listLoading = MutableLiveData<Resources<PointHistoryBase>>()

    init {
        hitApi()
    }

    private fun hitApi() {
        launch {
            listLoading.value = Loading()
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

    fun loadData(currentPageIndex: Int) {
        launch {
            mUserRepository.getPointList(currentPageIndex, listLoading)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mUserRepository.onCleared()
    }

    override fun onRetryPageLoad(pageNumber: Int) {

    }

    override fun onEmptyList(rawObject: Any) {

    }

    override fun onStartFirstPageLoad() {
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
    }

    override fun onStartPageLoad(pageNumber: Int) {

    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {

    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            listLoading.value = ErrorMessage("n/a")
        }
    }
}