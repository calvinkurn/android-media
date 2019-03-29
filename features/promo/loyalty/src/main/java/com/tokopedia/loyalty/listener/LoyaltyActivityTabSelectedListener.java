package com.tokopedia.loyalty.listener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.adapter.GlobalMainTabSelectedListener;


/**
 * Created by kris on 12/13/17. Tokopedia
 */

public class LoyaltyActivityTabSelectedListener extends GlobalMainTabSelectedListener {
    private LoyaltyModuleRouter loyaltyModuleRouter;

    public LoyaltyActivityTabSelectedListener(ViewPager mViewPager, LoyaltyModuleRouter loyaltyModuleRouter) {
        super(mViewPager);
        this.loyaltyModuleRouter = loyaltyModuleRouter;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if(tab.getPosition() == 1) {
            loyaltyModuleRouter.sendEventMyCouponClicked();
        }
    }
}
