package com.tokopedia.profile.following_list.data.mapper;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.profile.following_list.data.pojo.GetKolFollowingData;
import com.tokopedia.profile.following_list.data.pojo.GetUserKolFollowing;
import com.tokopedia.profile.following_list.data.pojo.User;
import com.tokopedia.profile.following_list.domain.model.FollowingDomain;
import com.tokopedia.profile.following_list.domain.model.FollowingResultDomain;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author by milhamj on 23/04/18.
 */

public class FollowingListMapper
        implements Func1<GraphqlResponse, FollowingResultDomain> {

    @Inject
    public FollowingListMapper() {
    }

    @Override
    public FollowingResultDomain call(GraphqlResponse getKolFollowingDataGraphqlResponse) {
        GetKolFollowingData getKolFollowingData = getKolFollowingDataGraphqlResponse.getData(GetKolFollowingData.class);
        GetUserKolFollowing getUserKolFollowing = getKolFollowingData.getUserKolFollowing;

        ArrayList<FollowingDomain> followingDomains = new ArrayList<>();

        for (User user : getUserKolFollowing.users) {
            followingDomains.add(
                    new FollowingDomain(
                            user.id,
                            user.name == null ? "" : user.name,
                            user.photo == null ? "" : user.photo,
                            user.userApplink == null ? "" : user.userApplink,
                            user.userUrl == null ? "" : user.userUrl,
                            user.isInfluencer
                    )
            );
        }

        return new FollowingResultDomain(
                !TextUtils.isEmpty(getUserKolFollowing.lastCursor),
                getUserKolFollowing.lastCursor,
                followingDomains,
                "",
                ""
        );
    }
}
