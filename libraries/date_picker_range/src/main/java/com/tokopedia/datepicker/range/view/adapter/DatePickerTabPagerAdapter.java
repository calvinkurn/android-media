package com.tokopedia.datepicker.range.view.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tokopedia.datepicker.range.R;

import java.util.List;

/**
 * Created by Nathaniel on 01/23/2017.
 */
public class DatePickerTabPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private Context context;


    public DatePickerTabPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
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
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.label_date_period);
            case 1:
                return context.getString(R.string.label_date_custom);
            default:
                return context.getString(R.string.label_date_period);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
