package com.tokopedia.logisticinputreceiptshipment;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticinputreceiptshipment.network.MyShopOrderActService;
import com.tokopedia.logisticinputreceiptshipment.network.mapper.OrderDetailMapper;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */
@Module
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
    MyShopOrderActService provideOrderActService() {
        return new MyShopOrderActService();
    }

    @Provides
    @OrderCourierScope
    OrderDetailService provideOrderDetailService() {
        return new OrderDetailService();
    }

    @Provides
    @OrderCourierScope
    MyShopOrderService provideOrderService() {
        return new MyShopOrderService();
    }

    @Provides
    @OrderCourierScope
    OrderCourierRepository provideOrderCourierRepository() {
        return new OrderCourierRepository(
                provideOrderDetailMapper(),
                provideOrderService(),
                provideOrderActService(),
                provideOrderDetailService());
    }

    @Provides
    @OrderCourierScope
    OrderCourierInteractorImpl provideOrderCourierInteractor() {
        return new OrderCourierInteractorImpl(provideCompositeSubsrciption(),
                provideOrderCourierRepository());
    }

    @Provides
    @OrderCourierScope
    OrderCourierPresenterImpl provideOrderCourierPresenter() {
        return new OrderCourierPresenterImpl(provideOrderCourierInteractor());
    }

    @Provides
    @OrderCourierScope
    SalesShippingAnalytics provideSalesShippingAnalytics(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return new SalesShippingAnalytics(((AbstractionRouter) context).getAnalyticTracker());
        }
        return null;
    }

}
