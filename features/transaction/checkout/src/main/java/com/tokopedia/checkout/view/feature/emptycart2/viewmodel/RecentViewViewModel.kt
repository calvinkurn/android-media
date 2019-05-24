package com.tokopedia.checkout.view.feature.emptycart2.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.checkout.domain.datamodel.recentview.GqlRecentViewResponse
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecentViewViewModel @Inject constructor(private val getRecentViewUseCase: GetRecentViewUseCase,
                                              private val userSessionInterface: UserSessionInterface) : ViewModel() {

    val recentViewData = MutableLiveData<Result<MutableList<RecentView>>>()

    fun unsubscribeSubscription() {
        getRecentViewUseCase.unsubscribe()
    }

    fun getRecentView() {
        getRecentViewUseCase.createObservable(userSessionInterface.userId.toInt(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                recentViewData.value = Fail(RuntimeException())
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                if (graphqlResponse.getData<Any>(GqlRecentViewResponse::class.java) != null) {
                    val gqlRecentViewResponse = graphqlResponse.getData<GqlRecentViewResponse>(GqlRecentViewResponse::class.java)
                    if (gqlRecentViewResponse != null && gqlRecentViewResponse.gqlRecentView != null &&
                            gqlRecentViewResponse.gqlRecentView.recentViewList != null &&
                            gqlRecentViewResponse.gqlRecentView.recentViewList.size > 0) {
                        if (gqlRecentViewResponse.gqlRecentView.recentViewList.size > 2) {
                            recentViewData.value = Success(gqlRecentViewResponse.gqlRecentView.recentViewList.subList(0, 2))
                        } else {
                            recentViewData.value = Success(gqlRecentViewResponse.gqlRecentView.recentViewList)
                        }
                    } else {
                        recentViewData.value = Fail(RuntimeException())
                    }
                } else {
                    recentViewData.value = Fail(RuntimeException())
                }
            }
        })
    }

}