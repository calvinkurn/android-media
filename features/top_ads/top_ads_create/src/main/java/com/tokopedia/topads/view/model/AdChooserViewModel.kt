package com.tokopedia.topads.view.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.SourceConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdChooserViewModel @Inject constructor(
    private val context: Context,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers,
    private val repository: GraphqlRepository,
    private val queryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase,
) : BaseViewModel(dispatcher.main) {

    private val CHANNEL = "topchat"
    private val SOURCE = "one_click_promo"

    private val _autoAdsData : MutableLiveData<Result<TopAdsAutoAdsModel>> = MutableLiveData()
    val autoAdsData : LiveData<Result<TopAdsAutoAdsModel>> = _autoAdsData

    fun getAdsState(onSuccess: ((AdCreationOption) -> Unit)) {
        launchCatchError(
            block = {
                val data = withContext(dispatcher.io) {
                    val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(
                        context.resources,
                        com.tokopedia.topads.common.R.raw.query_autoads_shop_info),
                        AdCreationOption::class.java,
                        hashMapOf(SHOP_Id to userSession.shopId, ParamObject.SOURCE to SourceConstant.SOURCE_ANDROID_AD_CHOOSER))
                    val cacheStrategy = RequestHelper.getCacheStrategy()
                    repository.response(listOf(request), cacheStrategy)
                }
                data.getSuccessData<AdCreationOption>().let {
                    onSuccess(it)
                }
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun postAutoAds(toggle_status: String, budget: Int) {
        val param = AutoAdsParam(AutoAdsParam.Input(
            toggle_status, CHANNEL, budget, userSession.shopId, SOURCE
        ))

        queryPostAutoadsUseCase.executeQuery(param) {
            _autoAdsData.postValue(it)
        }
    }

    fun getAutoAdsStatus(onSuccess: ((AutoAdsResponse) -> Unit)) {
        launchCatchError(
            block = {
                val data = withContext(dispatcher.io) {
                    val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(
                        context.resources,
                        com.tokopedia.topads.common.R.raw.query_auto_ads_status),
                        AutoAdsResponse::class.java,
                        hashMapOf(SHOP_Id to userSession.shopId, ParamObject.SOURCE to "android.topads_ad_chooser"))
                    val cacheStrategy = RequestHelper.getCacheStrategy()
                    repository.response(listOf(request), cacheStrategy)
                }
                data.getSuccessData<AutoAdsResponse>().let {
                    onSuccess(it)
                }
            },
            onError = {
                it.printStackTrace()
            }
        )
    }
}
