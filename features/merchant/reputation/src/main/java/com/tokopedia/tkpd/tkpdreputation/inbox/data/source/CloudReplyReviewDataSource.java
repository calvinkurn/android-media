package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/28/17.
 */

public class CloudReplyReviewDataSource {
    private final ReputationService reputationService;
    private final ReplyReviewMapper replyReviewMapper;
    private UserSessionInterface userSession;

    public CloudReplyReviewDataSource(ReputationService reputationService,
                                      ReplyReviewMapper replyReviewMapper,
                                      UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.replyReviewMapper = replyReviewMapper;
        this.userSession = userSession;
    }

    public Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams) {
        return reputationService.getApi()
                .insertReviewResponse(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(replyReviewMapper);
    }
}
