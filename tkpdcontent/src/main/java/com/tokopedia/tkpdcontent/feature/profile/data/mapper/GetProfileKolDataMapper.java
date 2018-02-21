package com.tokopedia.tkpdcontent.feature.profile.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKol;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.ProfileKolData;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder.KolViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataMapper
        implements Func1<Response<GraphqlResponse<ProfileKolData>>, List<KolViewModel>> {

    @Inject
    public GetProfileKolDataMapper() {
    }

    @Override
    public List<KolViewModel> call(Response<GraphqlResponse<ProfileKolData>> graphqlResponse) {
        ProfileKolData profileKolData = getDataOrError(graphqlResponse);
        ArrayList<KolViewModel> kolViewModels = new ArrayList<>();
        for (PostKol postKol : profileKolData.postKol) {
            //TODO milhamj fill these field
            KolViewModel kolViewModel = new KolViewModel(
                    "This is title",
                    postKol.userName,
                    postKol.userPhoto,
                    "This is label",
                    postKol.isFollow,
                    postKol.content.get(0).imageurl,
                    postKol.content.get(0).tags.get(0).caption,
                    "This is review",
                    postKol.isLiked,
                    postKol.likeCount,
                    postKol.commentCount,
                    0,
                    "",
                    postKol.content.get(0).tags.get(0).id,
                    0,
                    postKol.createTime,
                    "This is title content name",
                    postKol.content.get(0).tags.get(0).price,
                    false,
                    postKol.content.get(0).tags.get(0).type,
                    postKol.content.get(0).tags.get(0).link,
                    0,
                    true,
                    "This is card type"
            );
            kolViewModels.add(kolViewModel);
        }
        return kolViewModels;
    }

    private ProfileKolData getDataOrError(Response<GraphqlResponse<ProfileKolData>>
                                                  graphqlResponse) {
        if (graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null) {
            if (graphqlResponse.isSuccessful()) {
                if (!TextUtils.isEmpty(graphqlResponse.body().getData().error)) {
                    return graphqlResponse.body().getData();
                } else {
                    throw new RuntimeException("Server error");
                }
            } else {
                throw new RuntimeException("Network call failed");
            }
        } else {
            throw new RuntimeException("Response is empty");
        }
    }
}
