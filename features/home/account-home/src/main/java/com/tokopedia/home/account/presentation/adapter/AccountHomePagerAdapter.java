package com.tokopedia.home.account.presentation.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomePagerAdapter extends FragmentStatePagerAdapter {
    private List<AccountFragmentItem> items;

    public AccountHomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.items = new ArrayList<>();
    }

    public void setItems(List<AccountFragmentItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }
}
