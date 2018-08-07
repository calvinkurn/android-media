package com.tokopedia.checkout.view.di.module;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsAddToCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.transactionanalytics.OrderAnalyticsOrderTracking;

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
    private CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart;
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
    CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart(AbstractionRouter abstractionRouter) {
        return checkoutAnalyticsAddToCart = mock(CheckoutAnalyticsAddToCart.class);
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

    public CheckoutAnalyticsAddToCart getCheckoutAnalyticsAddToCart() {
        return checkoutAnalyticsAddToCart;
    }

    public CheckoutAnalyticsCourierSelection getCheckoutAnalyticsCourierSelection() {
        return checkoutAnalyticsCourierSelection;
    }

    public OrderAnalyticsOrderTracking getOrderAnalyticsOrderTracking() {
        return orderAnalyticsOrderTracking;
    }
}
