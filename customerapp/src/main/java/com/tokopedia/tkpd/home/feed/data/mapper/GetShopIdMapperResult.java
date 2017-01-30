package com.tokopedia.tkpd.home.feed.data.mapper;

import com.tokopedia.core.base.common.response.GetListFaveShopIdResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class GetShopIdMapperResult
        implements Func1<Response<GetListFaveShopIdResponse>, List<String>> {

    @Override
    public List<String> call(Response<GetListFaveShopIdResponse> getListFaveShopIdResponseResponse) {
        return mappingResponse(getListFaveShopIdResponseResponse);
    }

    private List<String> mappingResponse(Response<GetListFaveShopIdResponse> response) {

        if (response.isSuccessful() && response.body() != null
                && response.body().getData() != null
                && response.body().getData().getShop_id_list() != null) {

            return response.body().getData().getShop_id_list();
        } else {
            return Collections.emptyList();
        }
    }

}
