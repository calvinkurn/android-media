package com.tokopedia.tokopoints.view.pointhistory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.PointHistoryBase
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rx.Subscriber
import javax.inject.Inject

@TokoPointScope
class PointHistoryViewModel @Inject constructor(val mUserRepository: PointHistoryRepository) : BaseViewModel(Dispatchers.Main), AdapterCallback {


    val data = MutableLiveData<Resources<TokoPointEntity>>()
    val listLoading = MutableLiveData<Resources<PointHistoryBase>>()

    init {
        hitApi()
    }

    private fun hitApi() {
        launchCatchError(block = {
            listLoading.value = Loading()
            data.postValue(Loading())
            onPointDetailNext(mUserRepository.getPointsDetail())
        } ){
            data.postValue(ErrorMessage(it.toString()))
        }
    }

    private fun onPointDetailNext(response: GraphqlResponse) {
        val data = response.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
        this@PointHistoryViewModel.data.setValue(Success(data.tokoPoints))
    }

    fun onErrorButtonClicked(toString: String, context: Context) {
        if (toString.equals(context.getString(R.string.tp_history_btn_action), ignoreCase = true)) {
            RouteManager.route(context, ApplinkConst.HOME)
        } else {
            hitApi()
        }
    }

    fun loadData(currentPageIndex: Int) {
        launchCatchError(block = {
           onPointListNext(mUserRepository.getPointList(currentPageIndex))
        }){
            listLoading.value = ErrorMessage(it.toString())
        }
    }


    private fun onPointListNext(graphqlResponse: GraphqlResponse) {
        val data = graphqlResponse.getData<PointHistoryBase>(PointHistoryBase::class.java)
        if (data != null) {
            listLoading.value = Success(data)
        } else {
            listLoading.value = ErrorMessage("data Error")
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {

    }

    override fun onEmptyList(rawObject: Any) {
      listLoading.value = ErrorMessage("")
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