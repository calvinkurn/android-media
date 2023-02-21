package com.tokopedia.kol.feature.comment.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.feature.comment.data.pojo.send.SendCommentKolData;
import com.tokopedia.kol.feature.comment.data.pojo.send.SendCommentKolGraphql;
import com.tokopedia.kol.feature.comment.data.pojo.send.SendCommentKolUser;
import com.tokopedia.kol.feature.comment.domain.model.KolCommentUserDomain;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.kolcommon.util.TimeConverter;
import com.tokopedia.network.data.model.response.GraphqlResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/3/17.
 */

public class KolSendCommentMapper
        implements Func1<Response<GraphqlResponse<SendCommentKolGraphql>>, SendKolCommentDomain> {

    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    private final Context context;

    @Inject
    KolSendCommentMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public SendKolCommentDomain call(Response<GraphqlResponse<SendCommentKolGraphql>>
                                             graphqlResponseResponse) {
        return convertToDomain(getDataorError(graphqlResponseResponse));
    }

    private SendCommentKolData getDataorError(
            Response<GraphqlResponse<SendCommentKolGraphql>> sendCommentKolResponse) {
        if (sendCommentKolResponse != null
                && sendCommentKolResponse.body() != null
                && sendCommentKolResponse.body().getData() != null) {
            if (sendCommentKolResponse.isSuccessful()) {
                SendCommentKolGraphql data = sendCommentKolResponse.body().getData();
                if (TextUtils.isEmpty(data.getCreateCommentKol().getError())) {
                    return data.getCreateCommentKol().getData();
                } else {
                    throw new GraphqlErrorException(data.getCreateCommentKol().getError());
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }

    private SendKolCommentDomain convertToDomain(SendCommentKolData data) {
        return new SendKolCommentDomain(data.getId(),
                data.getComment(),
                TimeConverter.generateTime(context, data.getCreateTime()),
                createDomainUser(data.getUser()),
                true);
    }


    private KolCommentUserDomain createDomainUser(SendCommentKolUser user) {
        return new KolCommentUserDomain(user.getId(),
                user.getIskol(),
                user.getName(),
                user.getPhoto());
    }
}