package com.tokopedia.tkpdcontent.feature.profile.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.GetUserKolPostResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKol;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKolData;
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
        implements Func1<Response<GraphqlResponse<GetUserKolPostResponse>>, KolProfileModel> {

    @Inject
    public GetProfileKolDataMapper() {
    }

    @Override
    public KolProfileModel call(Response<GraphqlResponse<GetUserKolPostResponse>>
                                               graphqlResponse) {
        PostKol postKol = getDataOrError(graphqlResponse);
        ArrayList<KolPostViewModel> kolPostViewModels = new ArrayList<>();
        for (PostKolData postKolData : postKol.postKolData) {
            KolPostViewModel kolPostViewModel = new KolPostViewModel(
                    "",
                    postKolData.userName != null ? postKolData.userName  : "",
                    postKolData.userPhoto != null ? postKolData.userPhoto  : "",
                    postKolData.userInfo != null ? postKolData.userInfo : "",
                    true,
                    getImageUrl(postKolData),
                    getTagCaption(postKolData),
                    postKolData.description != null ? postKolData.description : "",
                    postKolData.isLiked != null ? postKolData.isLiked : false,
                    postKolData.likeCount != null ? postKolData.likeCount : 0,
                    postKolData.commentCount != null ? postKolData.commentCount : 0,
                    0,
                    "",
                    getTagId(postKolData),
                    postKolData.id != null ? postKolData.id : 0,
                    postKolData.createTime != null ? postKolData.createTime : "",
                    "",
                    getTagPrice(postKolData),
                    false,
                    getTagType(postKolData),
                    getTagLink(postKolData),
                    postKolData.userId != null ? postKolData.userId : 0,
                    postKolData.showComment != null ? postKolData.showComment : true,
                    ""
            );
            kolPostViewModels.add(kolPostViewModel);
        }
        return new KolProfileModel(kolPostViewModels, postKol.lastCursor);
    }

    private PostKol getDataOrError(Response<GraphqlResponse<GetUserKolPostResponse>>
                                                  graphqlResponse) {
        if (graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null) {
            if (graphqlResponse.isSuccessful()) {
                if (TextUtils.isEmpty(graphqlResponse.body().getData().postKol.error)) {
                    return graphqlResponse.body().getData().postKol;
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

    private String getImageUrl(PostKolData postKolData) {
        try {
            return postKolData.content.get(0).imageurl != null ?
                    postKolData.content.get(0).imageurl : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagCaption(PostKolData postKolData) {
        try  {
            return postKolData.content.get(0).tags.get(0).caption != null ?
                    postKolData.content.get(0).tags.get(0).caption : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private int getTagId(PostKolData postKolData) {
        try  {
            return postKolData.content.get(0).tags.get(0).id != null ?
                    postKolData.content.get(0).tags.get(0).id : 0;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private String getTagPrice(PostKolData postKolData) {
        try  {
            return postKolData.content.get(0).tags.get(0).price != null ?
                    postKolData.content.get(0).tags.get(0).price : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagType(PostKolData postKolData) {
        try  {
            return postKolData.content.get(0).tags.get(0).type != null ?
                    postKolData.content.get(0).tags.get(0).type : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getTagLink(PostKolData postKolData) {
        try  {
            return postKolData.content.get(0).tags.get(0).link != null ?
                    postKolData.content.get(0).tags.get(0).link : "";
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }
}
