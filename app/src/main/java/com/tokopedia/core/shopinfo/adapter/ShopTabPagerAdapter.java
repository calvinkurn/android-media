package com.tokopedia.core.shopinfo.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.tokopedia.core.R;
import com.tokopedia.core.shopinfo.fragment.DiscussionList;
import com.tokopedia.core.shopinfo.fragment.NotesList;
import com.tokopedia.core.shopinfo.fragment.ProductList;
import com.tokopedia.core.shopinfo.fragment.ShopReputationList;
import com.tokopedia.core.shopinfo.models.ShopModel;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class ShopTabPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public static int[] TITLES = {R.string.title_product, R.string.title_talk_only, R.string.title_review, R.string.title_notes_menu};

    ArrayList<Fragment> fragments = new ArrayList<>();

    public ShopTabPagerAdapter(FragmentManager fm, ShopModel model) {
        super(fm);
        fragments.add(ProductList.create());
        fragments.add(DiscussionList.create());
        fragments.add(ShopReputationList.create());
        fragments.add(new NotesList());
    }

    public static ShopTabPagerAdapter createAdapter(FragmentManager fm, Context context, String id, String domain) {
        //TODO ganti ke model baru 1 1
        ShopModel model = new ShopModel();
        model.shopId = id;
        model.domain = domain;
        ShopTabPagerAdapter adapter = new ShopTabPagerAdapter(fm, model);
        adapter.context = context;
        return adapter;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TITLES[position]);
    }
}
