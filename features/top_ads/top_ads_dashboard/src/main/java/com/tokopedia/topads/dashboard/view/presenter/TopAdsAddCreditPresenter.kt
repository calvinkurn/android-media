package com.tokopedia.topads.dashboard.view.presenter


import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import javax.inject.Inject

/**
 * Created by Nisie on 5/9/16.
 */
class TopAdsAddCreditPresenter @Inject
constructor(private val useCase: GraphqlUseCase<TkpdProducts>,
            private val autoStatusUseCase: GraphqlUseCase<AutoTopUpData.Response>) : BaseDaggerPresenter<TopAdsAddCreditView>() {

    var autoTopUpStatus = MutableLiveData<String>()

    companion object {
        const val TKPD_PRODUCT = """query topadsGetTkpdProduct(${'$'}shop_id: String!, ${'$'}source: String!) {
  topadsGetTkpdProduct(shop_id: ${'$'}shop_id, source: ${'$'}source) {
    data {
      credit {
        product_id
        product_type
        product_price
        product_url
        default
        product_name
        min_credit
      }
      extra_credit_percent
    }
  }
}
"""
    }

    fun getAutoTopUpStatus(shopId: String, rawQuery: String) {
        val params = mapOf(TopAdsDashboardConstant.SHOP_ID to shopId)
        autoStatusUseCase.setTypeClass(AutoTopUpData.Response::class.java)
        autoStatusUseCase.setRequestParams(params)
        autoStatusUseCase.setGraphqlQuery(rawQuery)
        autoStatusUseCase.execute({
            autoTopUpStatus.value = it.response?.data?.status
        }
                , {
            it.printStackTrace()
        })

    }

    @GqlQuery("CategoryList", TKPD_PRODUCT)
    fun populateCreditList(shopId: String) {
        val params = mapOf(ParamObject.SHOP_id to shopId,
                ParamObject.SOURCE to TopAdsDashboardConstant.SOURCE_DASH)
        useCase.setTypeClass(TkpdProducts::class.java)
        useCase.setRequestParams(params)
        useCase.setGraphqlQuery(CategoryList.GQL_QUERY)
        useCase.execute({
            view?.onCreditListLoaded(it.tkpdProduct.creditResponse)
        }
                , {
            view?.onLoadCreditListError()
        }
        )
    }

    override fun detachView() {
        super.detachView()
        useCase.cancelJobs()
    }
}