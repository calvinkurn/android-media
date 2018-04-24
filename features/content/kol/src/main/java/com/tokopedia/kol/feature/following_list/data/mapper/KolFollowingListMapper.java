package com.tokopedia.kol.feature.following_list.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kol.feature.following_list.data.pojo.GetKolFollowingData;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 23/04/18.
 */

public class KolFollowingListMapper
        implements Func1<Response<GraphqlResponse<GetKolFollowingData>>, KolFollowingResultDomain> {

    @Inject
    public KolFollowingListMapper() {
    }

    @Override
    public KolFollowingResultDomain call(Response<GraphqlResponse<GetKolFollowingData>>
                                                     graphqlResponseResponse) {
        return null;
    }
}
