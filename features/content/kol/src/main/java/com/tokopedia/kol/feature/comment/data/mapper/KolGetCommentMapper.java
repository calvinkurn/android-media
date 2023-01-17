package com.tokopedia.kol.feature.comment.data.mapper;

import android.text.TextUtils;

import com.tokopedia.kol.feature.comment.data.pojo.get.Comment;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetUserPostComment;
import com.tokopedia.kol.feature.comment.data.pojo.get.PostKol;
import com.tokopedia.kol.feature.comment.data.type.SourceType;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.network.data.model.response.GraphqlResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/2/17.
 */

public class KolGetCommentMapper
        implements Func1<Response<GraphqlResponse<GetKolCommentData>>, KolComments> {
    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Inject
    KolGetCommentMapper() {
    }

    @Override
    public KolComments call(Response<GraphqlResponse<GetKolCommentData>> getKolCommentData) {
        GetUserPostComment getUserPostComment = getDataOrError(getKolCommentData);

        PostKol postKol = getUserPostComment.getPostKol();
        KolCommentHeaderNewModel kolCommentHeaderNewModel;
        kolCommentHeaderNewModel = new KolCommentHeaderNewModel(
                postKol.getUserPhoto(),
                postKol.getUserName(),
                postKol.getDescription(),
                postKol.getCreateTime(),
                postKol.getUserId(),
                postKol.getUserUrl(),
                getTagsLink(postKol),
                !postKol.getUserBadges().isEmpty() ? postKol.getUserBadges().get(0) : "",
                postKol.getSource().getType() == SourceType.SHOP.getTypeInt()
        );

        return new KolComments(
                getUserPostComment.getLastCursor(),
                !TextUtils.isEmpty(getUserPostComment.getLastCursor()),
                convertCommentNewList(getUserPostComment),
                kolCommentHeaderNewModel
        );
    }

    private GetUserPostComment getDataOrError(
            Response<GraphqlResponse<GetKolCommentData>> getKolCommentData) {
        if (getKolCommentData != null
                && getKolCommentData.body() != null
                && getKolCommentData.body().getData() != null) {
            if (getKolCommentData.isSuccessful()) {
                GetKolCommentData data = getKolCommentData.body().getData();
                if (TextUtils.isEmpty(data.getGetUserPostComment().getError())) {
                    return data.getGetUserPostComment();
                } else {
                    throw new GraphqlErrorException(data.getGetUserPostComment().getError());
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }

    private ArrayList<KolCommentNewModel> convertCommentNewList(
            GetUserPostComment getUserPostComment) {
        ArrayList<KolCommentNewModel> viewModelList = new ArrayList<>();

        for (Comment comment : getUserPostComment.getComments()) {
            KolCommentNewModel kolCommentViewModel = new KolCommentNewModel(
                    comment.getId(),
                    comment.getUserID(),
                    null,
                    comment.getUserPhoto(),
                    comment.getUserName(),
                    comment.getComment(),
                    comment.getCreateTime(),
                    comment.isKol(),
                    comment.isCommentOwner(),
                    comment.getUserBadges(),
                    comment.isShop(),
                    comment.getAllowReport()
            );
            viewModelList.add(kolCommentViewModel);
        }

        return viewModelList;
    }


    private String getTagsLink(PostKol postKol) {
        try {
            return postKol.getContent().get(0).getTags().get(0).getLink();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "";
        }
    }
}