package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReplyReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
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
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(replyReviewMapper);
    }
}
