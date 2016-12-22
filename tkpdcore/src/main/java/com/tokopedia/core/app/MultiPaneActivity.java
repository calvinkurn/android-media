package com.tokopedia.core.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 1/13/2015.
 */
public abstract class MultiPaneActivity extends TkpdActivity implements MultiPaneFragment.MultiPaneInterface{

    protected ViewPager detailPager;
    public DetailsPagerAdapter pagerAdapter;

    private Menu menuOptions;
    private int menuID;

    private List<Fragment> fragmentList = new ArrayList<>();

    protected abstract List<Fragment> getFragmentList();

    protected void setTkpdContentView(int resID, int location) {
        inflateView(resID);
        drawer.setDrawerPosition(location);
        initDetailPager();
    }

    protected  void initDetailPager() {
        detailPager = (ViewPager)findViewById(R.id.detail_pager); // TODO validasi jika detail pager nulll
        if(pagerAdapter == null)
        pagerAdapter = new DetailsPagerAdapter(getFragmentManager());
        detailPager.setAdapter(pagerAdapter);
        detailPager.setVisibility(View.GONE);
    }

    protected void createDetailView(int page) {
        pagerAdapter = new DetailsPagerAdapter(getFragmentManager());
        detailPager.setAdapter(pagerAdapter);
        detailPager.setVisibility(View.VISIBLE);
        fragmentList.clear();
        fragmentList.addAll(getFragmentList());
        pagerAdapter.notifyDataSetChanged();
        detailPager.setCurrentItem(page);
//        setBackButton(); TODO Needs work
    }


    private void setBackButton(){
        MenuItem back = menuOptions.add(0, 1, 4 , "Back");
        back.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        try {
            back.setIcon(R.drawable.ic_new_action_back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuOptions = menu;
        return super.onCreateOptionsMenu(menu);
    }

    protected void createDetailMenu(int menuID){
        this.menuID = menuID;
        getMenuInflater().inflate(menuID, menuOptions);
    }

    private void destroyDetailMenu(){
        menuOptions.removeItem(menuID);
        invalidateOptionsMenu();
    }

    public DetailsPagerAdapter getFragmentAdapter() {
        return pagerAdapter;
    }

    public class DetailsPagerAdapter extends FragmentStatePagerAdapter{

        public DetailsPagerAdapter(FragmentManager fm) {
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

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentList.size() > 0) {
//            destroyDetailMenu();
            fragmentList.clear();
            pagerAdapter.notifyDataSetChanged();
            detailPager.setVisibility(View.GONE);
        }
        else {
            fragmentList.clear();
            pagerAdapter.notifyDataSetChanged();
            super.onBackPressed();
        }
    }

    public Fragment getFragment(int position) {
        Fragment fragment = (Fragment) pagerAdapter.instantiateItem(detailPager, position);
        return fragment;
    }

}
