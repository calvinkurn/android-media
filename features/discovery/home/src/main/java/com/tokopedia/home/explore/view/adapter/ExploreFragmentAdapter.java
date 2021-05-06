package com.tokopedia.home.explore.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ExploreSectionDataModel> modelList;

    public ExploreFragmentAdapter(FragmentManager fm) {
        super(fm);
        modelList = new ArrayList<>();
    }

    public void setData(List<ExploreSectionDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ExploreFragment fragment = ExploreFragment.newInstance(position);
        fragment.setData(modelList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
