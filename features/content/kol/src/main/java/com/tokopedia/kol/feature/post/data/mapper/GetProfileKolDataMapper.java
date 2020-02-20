package com.tokopedia.kol.feature.post.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.kol.feature.post.data.pojo.GetUserKolPostResponse;
import com.tokopedia.kol.feature.post.data.pojo.PostKol;
import com.tokopedia.kol.feature.post.data.pojo.PostKolContent;
import com.tokopedia.kol.feature.post.data.pojo.PostKolData;
import com.tokopedia.kol.feature.post.data.pojo.TagsFeedKol;
import com.tokopedia.kol.feature.post.domain.model.KolProfileModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

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
            List<String> imageList = new ArrayList<>();
            imageList.add(getImageUrl(content));

            KolPostViewModel kolPostViewModel = new KolPostViewModel(
                    postKolData.id != null ? postKolData.id : 0,
                    "",
                    "",
                    postKolData.userName != null ? postKolData.userName : "",
                    postKolData.userPhoto != null ? postKolData.userPhoto : "",
                    postKolData.userInfo != null ? postKolData.userInfo : "",
                    "",
                    true,
                    postKolData.description != null ? postKolData.description : "",
                    postKolData.isLiked != null ? postKolData.isLiked : false,
                    postKolData.likeCount != null ? postKolData.likeCount : 0,
                    postKolData.commentCount != null ? postKolData.commentCount : 0,
                    0,
                    postKolData.id != null ? postKolData.id : 0,
                    postKolData.createTime != null ? postKolData.createTime : "",
                    postKolData.showComment != null ? postKolData.showComment : true,
                    postKolData.showLike != null ? postKolData.showLike : true,
                    imageList,
                    getTagId(tag),
                    "",
                    getTagType(tag),
                    getTagCaption(tag),
                    !TextUtils.isEmpty(getTagLink(tag)) ? getTagLink(tag) : getTagUrl(tag),
                    new ArrayList<>()
            );
            kolPostViewModel.setShowTopShadow(true);
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
                    throw new GraphqlErrorException(graphqlResponse.body().getData().postKol.error);
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

    private String getTagUrl(TagsFeedKol tag) {
        if (tag != null && tag.url != null) {
            return tag.url;
        } else {
            return "";
        }
    }
}
