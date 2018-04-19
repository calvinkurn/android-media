package com.tokopedia.kol.feature.comment.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kol.common.network.GraphqlErrorException;
import com.tokopedia.kol.feature.comment.data.pojo.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.GetUserPostComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;

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

    @Inject
    KolGetCommentMapper() {
    }

    @Override
    public KolComments call(Response<GraphqlResponse<GetKolCommentData>> getKolCommentData) {
        GetUserPostComment postKol = getDataOrError(getKolCommentData);
        return null;
    }

    private GetUserPostComment getDataOrError(Response<GraphqlResponse<GetKolCommentData>> getKolCommentData) {
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
}
//
//public class KolGetCommentMapper implements Func1<GetKolComments.Data, KolComments> {
//
//    @Override
//    public KolComments call(GetKolComments.Data data) {
//        if (data != null
//                && data.get_kol_list_comment() != null
//                && data.get_kol_list_comment().data() != null
//                && (data.get_kol_list_comment().error() == null
//                || TextUtils.isEmpty(data.get_kol_list_comment().error()))) {
//            return convertToDomain(data.get_kol_list_comment().data());
//        } else if (data != null
//                && data.get_kol_list_comment() != null
//                && (data.get_kol_list_comment().error() != null
//                && !TextUtils.isEmpty(data.get_kol_list_comment().error()))) {
//            throw new ErrorMessageException(data.get_kol_list_comment().error());
//        } else {
//            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
//                    .default_request_error_unknown));
//        }
//    }
//
//    private KolComments convertToDomain(GetKolComments.Data.Data1 data) {
//        return new KolComments(data.lastcursor() == null ? "" : data.lastcursor(),
//                data.has_next_page() == null ? false : data.has_next_page(),
//                convertToList(data.comment()));
//    }
//
//    private ArrayList<KolCommentViewModel> convertToList(List<GetKolComments.Data.Comment>
//                                                                 comments) {
//        ArrayList<KolCommentViewModel> list = new ArrayList<>();
//        if (comments != null)
//            for (GetKolComments.Data.Comment comment : comments) {
//                list.add(new KolCommentViewModel(
//                        comment.id() == null ? "0" : comment.id().toString(),
//                        comment.userID() == null ? "" : comment.userID().toString(),
//                        comment.userPhoto() == null ? "" : comment.userPhoto(),
//                        comment.userName() == null ? "" : comment.userName(),
//                        comment.comment() == null ? "" : comment.comment(),
//                        TimeConverter.generateTime(comment.create_time() == null ? "" : comment
//                                .create_time()),
//                        comment.isKol() == null ? false : comment.isKol(),
//                        comment.isCommentOwner() == null ? false : comment.isCommentOwner()));
//            }
//        return list;
//    }
//}
