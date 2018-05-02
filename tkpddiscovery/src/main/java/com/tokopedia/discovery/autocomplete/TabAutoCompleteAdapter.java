package com.tokopedia.discovery.autocomplete;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.fragment.SearchResultFragment;

public class TabAutoCompleteAdapter extends FragmentPagerAdapter{

    private String[] TITLE;

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabAutoCompleteAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLE = new String[]{
                context.getString(R.string.title_all),
                context.getString(R.string.title_product),
                context.getString(R.string.title_shop)
        };
    }

    @Override
    public Fragment getItem(int position) {
        return SearchResultFragment.newInstance();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }
}
