package com.tokopedia.tkpdcontent.feature.profile.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.tkpdcontent.common.util.TimeConverter;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.GetProfileKolResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKol;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.ProfileKolData;
import com.tokopedia.tkpdcontent.feature.profile.domain.model.KolProfileModel;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataMapper
        implements Func1<Response<GraphqlResponse<GetProfileKolResponse>>, KolProfileModel> {

    @Inject
    public GetProfileKolDataMapper() {
    }

    @Override
    public KolProfileModel call(Response<GraphqlResponse<GetProfileKolResponse>>
                                               graphqlResponse) {
        ProfileKolData profileKolData = getDataOrError(graphqlResponse);
        ArrayList<KolPostViewModel> kolPostViewModels = new ArrayList<>();
        for (PostKol postKol : profileKolData.postKol) {
            KolPostViewModel kolPostViewModel = new KolPostViewModel(
                    "",
                    postKol.userName != null ? postKol.userName  : "",
                    postKol.userPhoto != null ? postKol.userPhoto  : "",
                    postKol.userInfo != null ? postKol.userInfo : "",
                    true,
                    getImageUrl(postKol),
                    getTagCaption(postKol),
                    postKol.description != null ? postKol.description : "",
                    postKol.isLiked != null ? postKol.isLiked : false,
                    postKol.likeCount != null ? postKol.likeCount : 0,
                    postKol.commentCount != null ? postKol.commentCount : 0,
                    0,
                    "",
                    getTagId(postKol),
                    postKol.id != null ? postKol.id : 0,
                    postKol.createTime != null ?
                            TimeConverter.generateTime(postKol.createTime) : "",
                    "",
                    getTagPrice(postKol),
                    false,
                    getTagType(postKol),
                    getTagLink(postKol),
                    postKol.userId != null ? postKol.userId : 0,
                    true,
                    ""
            );
            kolPostViewModels.add(kolPostViewModel);
        }
        return new KolProfileModel(kolPostViewModels, profileKolData.lastCursor);
    }

    private ProfileKolData getDataOrError(Response<GraphqlResponse<GetProfileKolResponse>>
                                                  graphqlResponse) {
        if (graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null) {
            if (graphqlResponse.isSuccessful()) {
                if (TextUtils.isEmpty(graphqlResponse.body().getData().profileKolData.error)) {
                    return graphqlResponse.body().getData().profileKolData;
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

    private String getImageUrl(PostKol postKol) {
        try {
            return postKol.content.get(0).imageurl != null ?
                    postKol.content.get(0).imageurl : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagCaption(PostKol postKol) {
        try  {
            return postKol.content.get(0).tags.get(0).caption != null ?
                    postKol.content.get(0).tags.get(0).caption : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private int getTagId(PostKol postKol) {
        try  {
            return postKol.content.get(0).tags.get(0).id != null ?
                    postKol.content.get(0).tags.get(0).id : 0;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private String getTagPrice(PostKol postKol) {
        try  {
            return postKol.content.get(0).tags.get(0).price != null ?
                    postKol.content.get(0).tags.get(0).price : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagType(PostKol postKol) {
        try  {
            return postKol.content.get(0).tags.get(0).type != null ?
                    postKol.content.get(0).tags.get(0).type : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagLink(PostKol postKol) {
        try  {
            return postKol.content.get(0).tags.get(0).link != null ?
                    postKol.content.get(0).tags.get(0).link : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }
}
