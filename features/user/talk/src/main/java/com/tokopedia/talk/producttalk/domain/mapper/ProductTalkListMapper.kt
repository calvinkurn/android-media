package com.tokopedia.talk.producttalk.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkListViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by Steven.
 */
class ProductTalkListMapper : Func1<Response<DataResponse<String>>,
        ProductTalkListViewModel> {


    override fun call(response: Response<DataResponse<String>>):
            ProductTalkListViewModel? {
        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            return null

        } else {
            throw MessageErrorException(response.body().header.messages[0])

        }
    }


}