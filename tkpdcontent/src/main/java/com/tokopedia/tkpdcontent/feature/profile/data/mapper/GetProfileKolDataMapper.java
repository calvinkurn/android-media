package com.tokopedia.tkpdcontent.feature.profile.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.GetUserKolPostResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKol;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKolContent;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.PostKolData;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.TagsFeedKol;
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
    private static final String ERROR_SERVER = "ERROR_SERVER";
    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Inject
    public GetProfileKolDataMapper() {
    }

    @Override
    public KolProfileModel call(Response<GraphqlResponse<GetUserKolPostResponse>>
                                        graphqlResponse) {
        PostKol postKol = getDataOrError(graphqlResponse);
        ArrayList<KolPostViewModel> kolPostViewModels = new ArrayList<>();
        for (PostKolData postKolData : postKol.postKolData) {
            PostKolContent content = getPostKolContent(postKolData);
            TagsFeedKol tag = getKolTag(content);

            KolPostViewModel kolPostViewModel = new KolPostViewModel(
                    "",
                    postKolData.userName != null ? postKolData.userName : "",
                    postKolData.userPhoto != null ? postKolData.userPhoto : "",
                    postKolData.userInfo != null ? postKolData.userInfo : "",
                    true,
                    getImageUrl(content),
                    getTagCaption(tag),
                    postKolData.description != null ? postKolData.description : "",
                    postKolData.isLiked != null ? postKolData.isLiked : false,
                    postKolData.likeCount != null ? postKolData.likeCount : 0,
                    postKolData.commentCount != null ? postKolData.commentCount : 0,
                    0,
                    "",
                    getTagId(tag),
                    postKolData.id != null ? postKolData.id : 0,
                    postKolData.createTime != null ? postKolData.createTime : "",
                    "",
                    getTagPrice(tag),
                    false,
                    getTagType(tag),
                    getTagLink(tag),
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
                    throw new RuntimeException(ERROR_SERVER);
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }

    private TagsFeedKol getKolTag(PostKolContent content) {
        try {
            return content.tags.get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private PostKolContent getPostKolContent(PostKolData postKolData) {
        try {
            return postKolData.content.get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private String getImageUrl(PostKolContent content) {
        if (content != null && content.imageurl != null) {
            return content.imageurl;
        } else {
            return "";
        }
    }

    private String getTagCaption(TagsFeedKol tag) {
        if (tag != null && tag.caption != null) {
            return tag.caption;
        } else {
            return "";
        }
    }

    private int getTagId(TagsFeedKol tag) {
        if (tag != null && tag.id != null) {
            return tag.id;
        } else {
            return 0;
        }
    }

    private String getTagPrice(TagsFeedKol tag) {
        if (tag != null && tag.price != null) {
            return tag.price;
        } else {
            return "";
        }
    }

    private String getTagType(TagsFeedKol tag) {
        if (tag != null && tag.type != null) {
            return tag.type;
        } else {
            return "";
        }
    }

    private String getTagLink(TagsFeedKol tag) {
        if (tag != null && tag.link != null) {
            return tag.link;
        } else {
            return "";
        }
    }
}
