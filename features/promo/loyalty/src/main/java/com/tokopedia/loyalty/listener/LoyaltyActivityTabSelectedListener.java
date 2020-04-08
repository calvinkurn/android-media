package com.tokopedia.loyalty.listener;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.LoyaltyTracking;
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
            LoyaltyTracking.sendEventMyCouponClicked();
        }
    }

}
