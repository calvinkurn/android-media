package com.tokopedia.profile.following_list.data.mapper;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.profile.following_list.data.pojo.GetKolFollowingData;
import com.tokopedia.profile.following_list.data.pojo.GetUserKolFollowing;
import com.tokopedia.profile.following_list.data.pojo.User;
import com.tokopedia.profile.following_list.domain.model.KolFollowingDomain;
import com.tokopedia.profile.following_list.domain.model.KolFollowingResultDomain;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author by milhamj on 23/04/18.
 */

public class KolFollowingListMapper
        implements Func1<GraphqlResponse, KolFollowingResultDomain> {

    @Inject
    public KolFollowingListMapper() {
    }

    @Override
    public KolFollowingResultDomain call(GraphqlResponse getKolFollowingDataGraphqlResponse) {
        GetKolFollowingData getKolFollowingData = getKolFollowingDataGraphqlResponse.getData(GetKolFollowingData.class);
        GetUserKolFollowing getUserKolFollowing = getKolFollowingData.getUserKolFollowing;

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
}
