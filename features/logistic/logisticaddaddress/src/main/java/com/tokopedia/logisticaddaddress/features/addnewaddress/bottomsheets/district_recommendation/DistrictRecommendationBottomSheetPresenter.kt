package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictRecommendationUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationBottomSheetPresenter @Inject constructor(private val districtRecommendationUseCase: DistrictRecommendationUseCase,
                                                                     private val mapper: DistrictRecommendationMapper)
    : BaseDaggerPresenter<DistrictRecommendationBottomSheetListener>() {

    fun getDistrictRecommendation(keyQuery: String, numPage: Int) {
        districtRecommendationUseCase.setParams(keyQuery, numPage.toString())
        districtRecommendationUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {

            override fun onNext(t: GraphqlResponse?) {
                val model = mapper.map(t)
                view.showData(model, model.hasNext)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        })
    }

    override fun detachView() {
        super.detachView()
        districtRecommendationUseCase.unsubscribe()
    }

    fun clearCacheDistrictRecommendation() {
        districtRecommendationUseCase.clearCache()
    }
}