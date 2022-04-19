package com.tokopedia.wishlist.common.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.R
import com.tokopedia.wishlist.common.request.WishlistAdditionalParamRequest
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import com.tokopedia.wishlist.common.toEmptyStringIfZero
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetWishlistUseCase @Inject constructor(private val context: Context) : UseCase<GetWishlistResponse>() {

    companion object {
        const val PAGE = "page"
        const val COUNT = "count"
        const val FILTER = "filter"
        const val SOURCE = "source"
        const val DEFAULT_PAGE = 1
        const val DEFAULT_COUNT = 10
        const val SOURCE_CART = "cart"
        const val ADDITIONAL_PARAMS = "additionalParams"
    }

    override fun createObservable(requestParams: RequestParams): Observable<GetWishlistResponse> {
        ChooseAddressUtils.getLocalizingAddressData(context.applicationContext)?.apply {
            requestParams.putObject(ADDITIONAL_PARAMS, WishlistAdditionalParamRequest(district_id.toEmptyStringIfZero(), city_id.toEmptyStringIfZero(),
                    lat.toEmptyStringIfZero(), long.toEmptyStringIfZero(),
                    postal_code.toEmptyStringIfZero(), address_id.toEmptyStringIfZero()))
        }

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.get_wishlist_query),
                GetWishlistResponse::class.java,
                requestParams.parameters
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
