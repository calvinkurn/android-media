package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.tracking.view.OrderAnalyticsOrderTracking;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
@Module
public class TestTrackingAnalyticsModule {
    private CheckoutAnalyticsCart checkoutAnalyticsCart;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    private CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;
    private CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    private OrderAnalyticsOrderTracking orderAnalyticsOrderTracking;

    @Provides
    CheckoutAnalyticsCart checkoutAnalyticsCartPage(AbstractionRouter abstractionRouter) {
        return checkoutAnalyticsCart = mock(CheckoutAnalyticsCart.class);
    }

    @Provides
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress(AbstractionRouter abstractionRouter) {
        return checkoutAnalyticsChangeAddress = mock(CheckoutAnalyticsChangeAddress.class);
    }

    @Provides
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress(AbstractionRouter abstractionRouter) {
        return checkoutAnalyticsMultipleAddress = mock(CheckoutAnalyticsMultipleAddress.class);
    }

    @Provides
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection(AbstractionRouter abstractionRouter) {
        return checkoutAnalyticsCourierSelection = mock(CheckoutAnalyticsCourierSelection.class);
    }

    @Provides
    OrderAnalyticsOrderTracking orderAnalyticsOrderTracking(AbstractionRouter abstractionRouter) {
        return orderAnalyticsOrderTracking = mock(OrderAnalyticsOrderTracking.class);
    }

    public CheckoutAnalyticsCart getCheckoutAnalyticsCart() {
        return checkoutAnalyticsCart;
    }

    public CheckoutAnalyticsChangeAddress getCheckoutAnalyticsChangeAddress() {
        return checkoutAnalyticsChangeAddress;
    }

    public CheckoutAnalyticsMultipleAddress getCheckoutAnalyticsMultipleAddress() {
        return checkoutAnalyticsMultipleAddress;
    }

    public CheckoutAnalyticsCourierSelection getCheckoutAnalyticsCourierSelection() {
        return checkoutAnalyticsCourierSelection;
    }

    public OrderAnalyticsOrderTracking getOrderAnalyticsOrderTracking() {
        return orderAnalyticsOrderTracking;
    }
}
