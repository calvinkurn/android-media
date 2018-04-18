package com.tokopedia.kol.feature.comment.data.mapper;

/**
 * @author by nisie on 11/3/17.
 */

public class KolSendCommentMapper {

}
//
//public class KolSendCommentMapper implements Func1<CreateKolComment.Data, SendKolCommentDomain> {
//    @Override
//    public SendKolCommentDomain call(CreateKolComment.Data data) {
//        if (data != null
//                && data.create_comment_kol() != null
//                && data.create_comment_kol().data() != null
//                && (data.create_comment_kol().error() == null
//                || TextUtils.isEmpty(data.create_comment_kol().error()))) {
//            return convertToDomain(data.create_comment_kol().data());
//        } else if (data != null
//                && data.create_comment_kol() != null
//                && (data.create_comment_kol().error() != null
//                && !TextUtils.isEmpty(data.create_comment_kol().error()))) {
//            throw new ErrorMessageException(data.create_comment_kol().error());
//        } else {
//            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
//                    .default_request_error_unknown));
//        }
//    }
//
//    private SendKolCommentDomain convertToDomain(CreateKolComment.Data.Data1 data) {
//        return new SendKolCommentDomain(data.id() == null ? "0" : data.id().toString(),
//                data.comment() == null ? "" : data.comment(),
//                TimeConverter.generateTime(data.create_time() == null ? "" : data
//                        .create_time()),
//                createDomainUser(data.user()),
//                true);
//    }
//
//    private KolCommentUserDomain createDomainUser(CreateKolComment.Data.User user) {
//        return new KolCommentUserDomain(user.id() == null ? 0 : user.id(),
//                user.iskol() == null ? false : user.iskol(),
//                user.name() == null ? "" : user.name(),
//                user.photo() == null ? "" : user.photo());
//    }
//}
