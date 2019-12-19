package com.tokopedia.autocomplete.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.TabAutoCompleteCallback;
import com.tokopedia.autocomplete.fragment.SearchResultFragment;

/**
 * @author erry on 23/02/17.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    private String[] TITLE;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public SearchPageAdapter(FragmentManager fm,
                             Context context,
                             ItemClickListener clickListener,
                             TabAutoCompleteCallback tabAutoCompleteListener) {
        super(fm);
        TITLE = new String[]{
                context.getString(R.string.title_all),
                context.getString(R.string.title_product),
                context.getString(R.string.title_shop)
        };
        for (int i = 0; i < 3; i++) {
            registeredFragments.put(i, SearchResultFragment.newInstance(TITLE[i], i, clickListener, tabAutoCompleteListener));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return registeredFragments.get(position);
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

    public SearchResultFragment getRegisteredFragment(int position) {
        return (SearchResultFragment) registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }
}