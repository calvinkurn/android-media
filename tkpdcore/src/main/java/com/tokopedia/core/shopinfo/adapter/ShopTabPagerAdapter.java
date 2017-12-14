package com.tokopedia.core.shopinfo.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.shopinfo.fragment.NotesList;
import com.tokopedia.core.shopinfo.fragment.OfficialShopHomeFragment;
import com.tokopedia.core.shopinfo.fragment.ProductList;
import com.tokopedia.core.shopinfo.fragment.ShopTalkFragment;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class ShopTabPagerAdapter extends FragmentPagerAdapter {

    //    public static int[] TITLES = {R.string.title_product, R.string.title_talk_only, R.string.title_review, R.string.title_notes_menu};
    public static String[] TITLES;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ShopModel shopModel;
    private Context context;

    public ShopTabPagerAdapter(FragmentManager fm, ShopModel model) {
        super(fm);
    }

    public ShopTabPagerAdapter(FragmentManager fm, Context context, ShopModel shopModel) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.context = context;
        this.shopModel = shopModel;
    }

    public void initOfficialShop(ShopModel shopModel) {
        this.shopModel = shopModel;
        TITLES = context.getResources().getStringArray(R.array.official_store_tab_title);
        fragments.add(OfficialShopHomeFragment.newInstance(shopModel.info.shopOfficialTop));
        fragments.add(ProductList.newInstance(shopModel.useAce));
        fragments.add(ShopTalkFragment.createInstance());
        if(context.getApplicationContext() instanceof TkpdCoreRouter) {
            fragments.add(((TkpdCoreRouter)context.getApplicationContext()).getShopReputationFragment());
        }
        fragments.add(new NotesList());
        notifyDataSetChanged();
    }

    public void initRegularShop(ShopModel shopModel) {
        this.shopModel = shopModel;
        TITLES = context.getResources().getStringArray(R.array.regular_store_tab_title);
        fragments.add(ProductList.newInstance(shopModel.useAce));
        fragments.add(ShopTalkFragment.createInstance());
        if(context.getApplicationContext() instanceof TkpdCoreRouter) {
            fragments.add(((TkpdCoreRouter)context.getApplicationContext()).getShopReputationFragment());
        }
        fragments.add(new NotesList());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
