package com.tokopedia.tkpd.reputationproduct;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.listener.GlobalMainTabSelectedListener;
import com.tokopedia.tkpd.review.fragment.ProductReviewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 7/12/15.
 */
public class ReputationProduct extends TActivity {

    public static String ALL_TIME_REPUTATION = "all-time";
    public static String SIX_MONTH_REPUTATION = "six-month";

    private String[] CONTENT;

    SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TabLayout indicator;

    private List<ProductReviewFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_reputation_product);
        initView();
        initVariable();
        setAdapter();
    }

    private void initVariable(){
        String productId = getIntent().getExtras().getString("product_id");
        String shopId	 = getIntent().getExtras().getString("shop_id");
        fragmentList = new ArrayList<>();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        fragmentList.add(ProductReviewFragment.createInstance(SIX_MONTH_REPUTATION, productId, shopId));
        fragmentList.add(ProductReviewFragment.createInstance(ALL_TIME_REPUTATION, productId, shopId));
        CONTENT = new String[] {
                getString(R.string.title_6_month),
                getString(R.string.all_time)
        };
        for (String aCONTENT : CONTENT) indicator.addTab(indicator.newTab().setText(aCONTENT));
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
    }

    private void setAdapter(){
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
