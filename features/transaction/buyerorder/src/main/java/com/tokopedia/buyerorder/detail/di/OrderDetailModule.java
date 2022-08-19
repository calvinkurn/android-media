package com.tokopedia.buyerorder.detail.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.view.OrderDetailRechargeDownloadWebviewAnalytics;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

@Module
public class OrderDetailModule {

    @Provides
    SendEventNotificationUseCase providesSendEventNotificationUseCase(@ApplicationContext Context context) {
        List<Interceptor> interceptorList = new ArrayList<>(1);
        interceptorList.add(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)));
        return new SendEventNotificationUseCase(interceptorList, context);
    }

    @Provides
    UserSessionInterface providesUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    OrderDetailRechargeDownloadWebviewAnalytics provideOrderDetailRechargeDownloadWebviewAnalytics() {
        return new OrderDetailRechargeDownloadWebviewAnalytics();
    }

    @Provides
    GraphqlRepository provideRepository(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

}
