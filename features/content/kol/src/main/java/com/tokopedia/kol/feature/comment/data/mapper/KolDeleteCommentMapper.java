package com.tokopedia.kol.feature.comment.data.mapper;

/**
 * @author by nisie on 11/10/17.
 * Moved to features and removed appolo watcher by milhamj on 19/04/18.
 */

public class KolDeleteCommentMapper {

}
//
//public class KolDeleteCommentMapper implements Func1<DeleteKolComment.Data, DeleteKolCommentDomain> {
//    @Override
//    public DeleteKolCommentDomain call(DeleteKolComment.Data data) {
//        if (data != null
//                && data.delete_comment_kol() != null
//                && data.delete_comment_kol().data() != null
//                && (data.delete_comment_kol().error() == null
//                || TextUtils.isEmpty(data.delete_comment_kol().error()))) {
//            return convertToDomain(data.delete_comment_kol().data());
//        } else if (data != null
//                && data.delete_comment_kol() != null
//                && (data.delete_comment_kol().error() != null
//                && !TextUtils.isEmpty(data.delete_comment_kol().error()))) {
//            throw new ErrorMessageException(data.delete_comment_kol().error());
//        } else {
//            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
//                    .default_request_error_unknown));
//        }
//    }
//
//    private DeleteKolCommentDomain convertToDomain(DeleteKolComment.Data.Data1 data) {
//        return new DeleteKolCommentDomain(data.success() != null && data.success() == 1);
//    }
//}
