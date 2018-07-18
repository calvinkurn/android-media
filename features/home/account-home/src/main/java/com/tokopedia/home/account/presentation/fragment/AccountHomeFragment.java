package com.tokopedia.home.account.presentation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.AccountHomePagerAdapter;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomeFragment extends TkpdBaseV4Fragment {
    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    private PagerAdapter adapter;

    private boolean isLoaded = false;

    public static Fragment newInstance() {
        return new AccountHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        if (!isLoaded) {
            loadData();
            isLoaded = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_home, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_setting) {
            Toast.makeText(getContext(), "Setting", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.menu_notification) {
            Toast.makeText(getContext(), "Notif", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {

    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    private void initView(View view) {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        setToolbar(view);
//        tabLayout = view.findViewById(R.id.tab_home_account);
//        viewPager = view.findViewById(R.id.pager_home_account);
    }

    private void setToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_account);
        if(getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    private void setAdapter() {
        String[] titles = {
                getContext().getString(R.string.label_account_buyer),
                getContext().getString(R.string.label_account_seller)
        };
        adapter = new AccountHomePagerAdapter(getChildFragmentManager(), titles);
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}