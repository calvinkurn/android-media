package com.tokopedia.tokopoints.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.contract.TokopointAddPointContract
import com.tokopedia.tokopoints.view.model.addpointsection.RewardPointResponse
import rx.Subscriber
import javax.inject.Inject

class AddPointPresenter @Inject constructor(val rewardUsecase: GraphqlUseCase) : TokopointAddPointContract.Presenter {


    override fun getRewardPoint(resources: Resources) {

        val graphqlRequestPoints = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.tp_gql_add_point_rewards),
                RewardPointResponse::class.java, false)

        rewardUsecase.addRequest(graphqlRequestPoints)

        rewardUsecase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(addPointResponse: GraphqlResponse?) {

                if (addPointResponse != null) {
                    val rewardPointResponse = addPointResponse.getData<RewardPointResponse>(RewardPointResponse::class.java)
                }

            }
        })

    }


    override fun attachView(view: TokopointAddPointContract.View?) {
    }

    override fun detachView() {
    }


}
