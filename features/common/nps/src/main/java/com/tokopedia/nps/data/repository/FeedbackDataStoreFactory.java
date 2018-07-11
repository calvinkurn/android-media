package com.tokopedia.nps.data.repository;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.nps.data.mapper.FeedbackEntityMapper;
import com.tokopedia.nps.data.net.NpsApi;
import com.tokopedia.nps.data.net.NpsService;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by meta on 28/06/18.
 */
public class FeedbackDataStoreFactory {

    private Context context;

    @Inject
    public FeedbackDataStoreFactory(Context context) {
        this.context = context;
    }

    public FeedbackDataStore createDataStore() {
        final FeedbackEntityMapper mapper = new FeedbackEntityMapper();
        final NetworkRouter networkRouter = (NetworkRouter) context.getApplicationContext();
        final UserSession userSession = new UserSession(context);
        final NpsService service = new NpsService(networkRouter, userSession, context);

        return new CloudFeedbackDataStore(service, mapper);
    }
}
