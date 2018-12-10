package com.tokopedia.loyalty.di.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.loyalty.view.adapter.LoyaltyPagerAdapter;
import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 30/11/17.
 */

@Module
public class LoyaltyViewModule {


    private final LoyaltyActivity activity;


    public LoyaltyViewModule(LoyaltyActivity activity) {
        this.activity = activity;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @Provides
    LoyaltyPagerAdapter provideLoyaltyPagerAdapter(FragmentManager fragmentManager) {
        return new LoyaltyPagerAdapter(fragmentManager);
    }

    @Provides
    @LoyaltyScope
    @Named("coupon_active")
    List<LoyaltyPagerItem> provideLoyaltyPagerItemListCouponActive() {
        List<LoyaltyPagerItem> loyaltyPagerItemList = new ArrayList<>();
        loyaltyPagerItemList.add(
                new LoyaltyPagerItem.Builder()
                        .fragment(PromoCodeFragment.newInstance(
                                activity.getPlatformString(),
                                activity.getPlatformPageString(),
                                activity.getCategoryString(),
                                activity.getCartIdString(),
                                activity.getAdditionalDataString(),
                                activity.getTrainReservationId(),
                                activity.getTrainReservationCode())
                        ).position(0)
                        .tabTitle("Kode Promo")
                        .build()
        );
        String events = "events";
        String deals = "deals";
        if (activity.getPlatformString().equals(events) || activity.getPlatformString().equals(deals)) {
            loyaltyPagerItemList.add(
                    new LoyaltyPagerItem.Builder()
                            .fragment(PromoCouponFragment.newInstanceEvent(
                                    activity.getPlatformString(),
                                    activity.getCategoryString(),
                                    activity.getCategoryId(),
                                    activity.getProductId()))
                            .position(0)
                            .tabTitle("Kupon Saya")
                            .build()
            );
        } else if (activity.getPlatformString().equals(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TRAIN_STRING)) {
            loyaltyPagerItemList.add(
                    new LoyaltyPagerItem.Builder()
                            .fragment(PromoCouponFragment.newInstanceTrain(
                                    activity.getPlatformString(),
                                    activity.getCategoryString(),
                                    activity.getTrainReservationId(),
                                    activity.getTrainReservationCode()))
                            .position(0)
                            .tabTitle("Kupon Saya")
                            .build()
            );
        } else {
            loyaltyPagerItemList.add(
                    new LoyaltyPagerItem.Builder()
                            .fragment(PromoCouponFragment.newInstance(
                                    activity.getPlatformString(),
                                    activity.getPlatformPageString(),
                                    activity.getAdditionalDataString(),
                                    activity.getCategoryString(),
                                    activity.getCartIdString(),
                                    activity.getCategoryId(),
                                    activity.getProductId()))
                            .position(0)
                            .tabTitle("Kupon Saya")
                            .build()
            );
        }
        return loyaltyPagerItemList;
    }

    @Provides
    @LoyaltyScope
    @Named("coupon_not_active")
    List<LoyaltyPagerItem> provideLoyaltyPagerItemListCouponNotActive() {
        List<LoyaltyPagerItem> loyaltyPagerItemList = new ArrayList<>();
        loyaltyPagerItemList.add(
                new LoyaltyPagerItem.Builder()
                        .fragment(PromoCodeFragment.newInstance(
                                activity.getPlatformString(),
                                activity.getPlatformPageString(),
                                activity.getCategoryString(),
                                activity.getCartIdString(),
                                activity.getAdditionalDataString(),
                                activity.getTrainReservationId(),
                                activity.getTrainReservationCode()))
                        .position(0)
                        .tabTitle("Kode Promo")
                        .build()
        );
        return loyaltyPagerItemList;
    }

    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(@ApplicationContext Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }

    @Provides
    CheckoutAnalyticsCart provideCheckoutAnalyticsCartPage() {
        AnalyticTracker analyticTracker = null;
        if (activity.getApplication() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) activity.getApplication()).getAnalyticTracker();
        }
        return new CheckoutAnalyticsCart(analyticTracker);
    }

    @Provides
    CheckoutAnalyticsCourierSelection provideCheckoutAnalyticsCourierSelection() {
        AnalyticTracker analyticTracker = null;
        if (activity.getApplication() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) activity.getApplication()).getAnalyticTracker();
        }
        return new CheckoutAnalyticsCourierSelection(analyticTracker);
    }

}
