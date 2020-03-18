package com.tokopedia.wishlist.common.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.R
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetWishlistUseCase @Inject constructor(private val context: Context) : UseCase<GetWishlistResponse>() {

    companion object {
        private val PAGE = "page"
        private val COUNT = "count"
        private val DEFAULT_PAGE = 1
        private val DEFAULT_COUNT = 10
    }

    override fun createObservable(p0: RequestParams?): Observable<GetWishlistResponse> {
        val variables = HashMap<String, Any>()

        variables[PAGE] = DEFAULT_PAGE
        variables[COUNT] = DEFAULT_COUNT

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_get_wishlist),
                GetWishlistResponse::class.java,
                variables
        )

        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    it.getData<GetWishlistResponse>(GetWishlistResponse::class.java)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}
