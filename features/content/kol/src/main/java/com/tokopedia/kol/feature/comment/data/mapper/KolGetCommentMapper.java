package com.tokopedia.kol.feature.comment.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.common.network.GraphqlErrorException;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.comment.data.pojo.Comment;
import com.tokopedia.kol.feature.comment.data.pojo.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.GetUserPostComment;
import com.tokopedia.kol.feature.comment.data.pojo.PostKol;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/2/17.
 * Moved to features and removed appolo watcher by milhamj on 19/04/18.
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
        return new KolComments(
                getUserPostComment.getLastCursor() == null ? "" :
                        getUserPostComment.getLastCursor(),
                !TextUtils.isEmpty(getUserPostComment.getLastCursor()),
                convertCommentList(getUserPostComment)
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

        PostKol postKol = getUserPostComment.getPostKol();
        KolCommentHeaderViewModel kolCommentHeaderViewModel = new KolCommentHeaderViewModel(
                postKol.getUserPhoto() == null ? "" : postKol.getUserPhoto(),
                postKol.getUserName() == null ? "" : postKol.getUserName(),
                postKol.getDescription() == null ? "" : postKol.getDescription(),
                postKol.getCreateTime() == null ? "" :
                        TimeConverter.generateTime(context, postKol.getCreateTime()),
                String.valueOf(postKol.getUserId())
        );
        viewModelList.add(kolCommentHeaderViewModel);

        for (Comment comment : getUserPostComment.getComments()) {
            KolCommentViewModel kolCommentViewModel = new KolCommentViewModel(
                    String.valueOf(comment.getId()),
                    String.valueOf(comment.getUserID()),
                    comment.getUserPhoto() == null ? "" : comment.getUserPhoto(),
                    comment.getUserName() == null ? "" : comment.getUserName(),
                    comment.getComment() == null ? "" : comment.getComment(),
                    comment.getCreateTime() == null ? "" :
                            TimeConverter.generateTime(context, comment.getCreateTime()),
                    comment.isKol(),
                    comment.isCommentOwner()
            );
            viewModelList.add(kolCommentViewModel);
        }

        return viewModelList;
    }
}