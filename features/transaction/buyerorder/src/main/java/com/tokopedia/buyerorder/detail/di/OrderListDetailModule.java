package com.tokopedia.buyerorder.detail.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.buyerorder.detail.domain.ErrorResponse;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.view.OrderDetailRechargeDownloadWebviewAnalytics;
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
public class OrderListDetailModule {

    @Provides
    PostCancelReasonUseCase providePostCancelUseCase(@ApplicationContext Context context) {
        List<Interceptor> interceptorList = new ArrayList<>(2);
        interceptorList.add(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)));
        ErrorResponseInterceptor errorResponseInterceptor = new ErrorResponseInterceptor(ErrorResponse.class);
        interceptorList.add(errorResponseInterceptor);
        return new PostCancelReasonUseCase(interceptorList, context);
    }

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
}
