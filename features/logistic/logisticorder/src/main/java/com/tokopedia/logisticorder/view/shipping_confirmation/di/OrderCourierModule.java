package com.tokopedia.logisticorder.view.shipping_confirmation.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.logisticCommon.data.analytics.SalesShippingAnalytics;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticCommon.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticCommon.data.module.LogisticNetworkModule;
import com.tokopedia.logisticCommon.data.module.qualifier.CourierDataRepositoryQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticMyShopOrderActApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticMyShopOrderApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticOrderDetailApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.OrderCourierScope;
import com.tokopedia.logisticCommon.data.repository.OrderCourierRepository;
import com.tokopedia.logisticorder.view.shipping_confirmation.network.mapper.OrderDetailMapper;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment.OrderCourierInteractorImpl;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment.OrderCourierPresenterImpl;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

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
