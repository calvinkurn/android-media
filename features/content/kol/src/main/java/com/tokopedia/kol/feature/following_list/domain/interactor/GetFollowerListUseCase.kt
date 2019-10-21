package com.tokopedia.kol.feature.following_list.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.following_list.data.mapper.KolFollowerMapper
import com.tokopedia.kol.feature.following_list.data.pojo.DataItem
import com.tokopedia.kol.feature.following_list.data.pojo.FollowerListData
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.ArrayList

/**
 * @author by yoasfs on 2019-08-22
 */
class GetFollowerListUseCase constructor(@ApplicationContext private val context: Context,
                                         val useCase: GraphqlUseCase,
                                         val mapData: KolFollowerMapper
): UseCase<KolFollowingResultViewModel>() {


    override fun createObservable(p0: RequestParams): Observable<KolFollowingResultViewModel> {

        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_get_kol_followers_list)
        val request = GraphqlRequest(query,
                FollowerListData::class.java,
                p0.parameters)
        useCase.clearRequest()
        useCase.addRequest(request)
        return useCase.createObservable(RequestParams.EMPTY).map(mapData)
    }

    companion object {
        val PARAM_ID = "userID"
        val PARAM_CURSOR = "cursor"
        val PARAM_LIMIT = "limit"

        val DEFAULT_LIMIT = 10

        @JvmStatic
        fun getFollowerListParam(id: Int, cursor: String): RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_ID, id)
            params.putString(PARAM_CURSOR, cursor)
            params.putInt(PARAM_LIMIT, DEFAULT_LIMIT)
            return params
        }
    }

}