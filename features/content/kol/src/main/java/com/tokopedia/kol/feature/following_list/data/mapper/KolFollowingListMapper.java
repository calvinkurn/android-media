package com.tokopedia.kol.feature.following_list.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kol.common.network.GraphqlErrorException;
import com.tokopedia.kol.feature.following_list.data.pojo.GetKolFollowingData;
import com.tokopedia.kol.feature.following_list.data.pojo.GetUserKolFollowing;
import com.tokopedia.kol.feature.following_list.data.pojo.User;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingDomain;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 23/04/18.
 */

public class KolFollowingListMapper
        implements Func1<Response<GraphqlResponse<GetKolFollowingData>>, KolFollowingResultDomain> {

    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Inject
    public KolFollowingListMapper() {
    }

    @Override
    public KolFollowingResultDomain call(Response<GraphqlResponse<GetKolFollowingData>>
                                                     graphqlResponseResponse) {
        GetUserKolFollowing getUserKolFollowing = getDataOrError(graphqlResponseResponse);

        ArrayList<KolFollowingDomain> kolFollowingDomains = new ArrayList<>();

        for (User user : getUserKolFollowing.users) {
            kolFollowingDomains.add(
                    new KolFollowingDomain(
                            user.id,
                            user.name == null ? "" : user.name,
                            user.photo == null ? "" : user.photo,
                            user.userApplink == null ? "" : user.userApplink,
                            user.userUrl == null ? "" : user.userUrl,
                            user.isInfluencer
                    )
            );
        }

        return new KolFollowingResultDomain(
                !TextUtils.isEmpty(getUserKolFollowing.lastCursor),
                getUserKolFollowing.lastCursor,
                kolFollowingDomains,
                "",
                ""
        );
    }

    private GetUserKolFollowing getDataOrError(
            Response<GraphqlResponse<GetKolFollowingData>> graphqlResponseResponse) {
        if (graphqlResponseResponse != null
                && graphqlResponseResponse.body() != null
                && graphqlResponseResponse.body().getData() != null) {
            if (graphqlResponseResponse.isSuccessful()) {
                GetKolFollowingData data = graphqlResponseResponse.body().getData();
                if (TextUtils.isEmpty(data.getUserKolFollowing.error)) {
                    return data.getUserKolFollowing;
                } else {
                    throw new GraphqlErrorException(data.getUserKolFollowing.error);
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }
}
