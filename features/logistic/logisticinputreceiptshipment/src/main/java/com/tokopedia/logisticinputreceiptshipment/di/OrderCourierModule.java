package com.tokopedia.logisticinputreceiptshipment.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticdata.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule;
import com.tokopedia.logisticdata.data.module.qualifier.CourierDataRepositoryQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticMyShopOrderActApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticMyShopOrderApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticOrderDetailApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.OrderCourierScope;
import com.tokopedia.logisticdata.data.repository.OrderCourierRepository;
import com.tokopedia.logisticinputreceiptshipment.network.mapper.OrderDetailMapper;
import com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.OrderCourierInteractorImpl;
import com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.OrderCourierPresenterImpl;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by kris on 1/3/18. Tokopedia
 */
@Module(includes = LogisticNetworkModule.class)
public class OrderCourierModule {

    public OrderCourierModule() {
    }

    @Provides
    @OrderCourierScope
    CompositeSubscription provideCompositeSubsrciption() {
        return new CompositeSubscription();
    }

    @Provides
    @OrderCourierScope
    OrderDetailMapper provideOrderDetailMapper() {
        return new OrderDetailMapper();
    }


    @Provides
    @OrderCourierScope
    @CourierDataRepositoryQualifier
    OrderCourierRepository provideOrderCourierRepository(
            @LogisticMyShopOrderApiQualifier MyShopOrderApi myShopOrderApi,
            @LogisticMyShopOrderActApiQualifier MyShopOrderActApi myShopOrderActApi,
            @LogisticOrderDetailApiQualifier OrderDetailApi orderDetailApi) {
        return new OrderCourierRepository(
                myShopOrderApi,
                myShopOrderActApi,
                orderDetailApi
        );
    }

    @Provides
    @OrderCourierScope
    OrderCourierInteractorImpl provideOrderCourierInteractor(
            @CourierDataRepositoryQualifier OrderCourierRepository courierRepository) {
        return new OrderCourierInteractorImpl(provideCompositeSubsrciption(),
                courierRepository, provideOrderDetailMapper());
    }

    @Provides
    @OrderCourierScope
    OrderCourierPresenterImpl provideOrderCourierPresenter(@ApplicationContext Context context,
                                                           OrderCourierInteractorImpl orderCourierInteractor) {
        return new OrderCourierPresenterImpl(orderCourierInteractor, new UserSession(context));
    }

    @Provides
    @OrderCourierScope
    SalesShippingAnalytics provideSalesShippingAnalytics() {
        return new SalesShippingAnalytics();
    }

}
