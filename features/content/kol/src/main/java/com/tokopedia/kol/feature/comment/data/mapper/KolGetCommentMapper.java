package com.tokopedia.kol.feature.comment.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.feature.comment.data.pojo.get.Comment;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetUserPostComment;
import com.tokopedia.kol.feature.comment.data.pojo.get.PostKol;
import com.tokopedia.kol.feature.comment.data.type.SourceType;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.kolcommon.util.TimeConverter;
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

    private final Context context;

    @Inject
    KolGetCommentMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public KolComments call(Response<GraphqlResponse<GetKolCommentData>> getKolCommentData) {
        GetUserPostComment getUserPostComment = getDataOrError(getKolCommentData);

        PostKol postKol = getUserPostComment.getPostKol();
        KolCommentHeaderNewModel kolCommentHeaderNewModel;
        KolCommentHeaderViewModel kolCommentHeaderViewModel = new KolCommentHeaderViewModel(
                postKol.getUserPhoto() == null ? "" : postKol.getUserPhoto(),
                postKol.getUserName() == null ? "" : postKol.getUserName(),
                postKol.getDescription() == null ? "" : postKol.getDescription(),
                postKol.getCreateTime() == null ? "" :
                        postKol.getCreateTime(),
                String.valueOf(postKol.getUserId()),
                postKol.getUserUrl(),
                getTagsLink(postKol),
                !postKol.getUserBadges().isEmpty() ? postKol.getUserBadges().get(0) : "",
                postKol.getSource().getType() == SourceType.SHOP.getTypeInt()
        );
        kolCommentHeaderNewModel = new KolCommentHeaderNewModel(
                postKol.getUserPhoto() == null ? "" : postKol.getUserPhoto(),
                postKol.getUserName() == null ? "" : postKol.getUserName(),
                postKol.getDescription() == null ? "" : postKol.getDescription(),
                postKol.getCreateTime() == null ? "" : postKol.getCreateTime(),
                String.valueOf(postKol.getUserId()),
                postKol.getUserUrl(),
                getTagsLink(postKol),
                !postKol.getUserBadges().isEmpty() ? postKol.getUserBadges().get(0) : "",
                postKol.getSource().getType() == SourceType.SHOP.getTypeInt()
        );

        return new KolComments(
                getUserPostComment.getLastCursor() == null ? "" :
                        getUserPostComment.getLastCursor(),
                !TextUtils.isEmpty(getUserPostComment.getLastCursor()),
                convertCommentList(getUserPostComment),
                convertCommentNewList(getUserPostComment),
                kolCommentHeaderViewModel,
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

    private ArrayList<KolCommentViewModel> convertCommentList(
            GetUserPostComment getUserPostComment) {
        ArrayList<KolCommentViewModel> viewModelList = new ArrayList<>();

        for (Comment comment : getUserPostComment.getComments()) {
            KolCommentViewModel kolCommentViewModel = new KolCommentViewModel(
                    String.valueOf(comment.getId()),
                    String.valueOf(comment.getUserID()),
                    null,
                    comment.getUserPhoto() == null ? "" : comment.getUserPhoto(),
                    comment.getUserName() == null ? "" : comment.getUserName(),
                    comment.getComment() == null ? "" : comment.getComment(),
                    comment.getCreateTime() == null ? "" :
                            TimeConverter.generateTime(context, comment.getCreateTime()),
                    comment.isKol(),
                    comment.isCommentOwner(),
                    comment.getUserBadges(),
                    comment.isShop()
            );
            viewModelList.add(kolCommentViewModel);
        }

        return viewModelList;
    }

    private ArrayList<KolCommentNewModel> convertCommentNewList(
            GetUserPostComment getUserPostComment) {
        ArrayList<KolCommentNewModel> viewModelList = new ArrayList<>();

        for (Comment comment : getUserPostComment.getComments()) {
            KolCommentNewModel kolCommentViewModel = new KolCommentNewModel(
                    String.valueOf(comment.getId()),
                    String.valueOf(comment.getUserID()),
                    null,
                    comment.getUserPhoto() == null ? "" : comment.getUserPhoto(),
                    comment.getUserName() == null ? "" : comment.getUserName(),
                    comment.getComment() == null ? "" : comment.getComment(),
                    comment.getCreateTime() == null ? "" :
                            comment.getCreateTime(),
                    comment.isKol(),
                    comment.isCommentOwner(),
                    comment.getUserBadges(),
                    comment.isShop()
            );
            viewModelList.add(kolCommentViewModel);
        }

        return viewModelList;
    }


    private String getTagsLink(PostKol postKol) {
        try {
            return postKol.getContent().get(0).getTags().get(0).getLink();
        } catch (NullPointerException|IndexOutOfBoundsException e) {
            return "";
        }
    }
}