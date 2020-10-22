package com.tokopedia.favorite.data

import com.tokopedia.favorite.domain.model.FavoritShopResponseData
import com.tokopedia.network.data.model.response.GraphqlResponse
import retrofit2.Response
import rx.functions.Action1

/**
 * Created by naveengoyal on 5/11/18.
 */
object FavoriteShopResponseValidator {

    private const val HTTP_ERROR_500 = 500
    private const val HTTP_ERROR_600 = 600
    private const val HTTP_ERROR_400 = 400

    fun validate(listener: HttpValidationListener): Action1<Response<GraphqlResponse<FavoritShopResponseData>>> {
        return Action1 { stringResponse ->
            if (stringResponse.code() >= HTTP_ERROR_500
                    && stringResponse.code() < HTTP_ERROR_600) {
                throw RuntimeException("Server Error!")
            } else if (stringResponse.code() >= HTTP_ERROR_400
                    && stringResponse.code() < HTTP_ERROR_500) {
                throw RuntimeException("Client Error!")
            } else {
                listener.OnPassValidation(stringResponse)
            }
        }
    }

    interface HttpValidationListener {
        fun OnPassValidation(response: Response<GraphqlResponse<FavoritShopResponseData>>)
    }

}
