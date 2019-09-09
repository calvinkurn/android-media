package com.tokopedia.datepicker.range.view.listener;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by normansyahputa on 1/24/17.
 */

public class DatePickerTabListener extends GlobalMainTabSelectedListener {

    public interface Callback {
        void onSelected(int positon);
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public DatePickerTabListener(ViewPager mViewPager) {
        super(mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if (mCallback != null) {
            mCallback.onSelected(tab.getPosition());
        }
    }
}
