package com.tokopedia.reksadana.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.reksadana.R;

public class BuySellTabFragment extends BaseDaggerFragment{

    private static final String BUY_TAB = "BUY";
    private static final String SELL_TAB = "SELL";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_sell_tabs_layout, container, false);
       return view;
    }

    public Fragment getFragment(){
        switch (""){
            case BUY_TAB:
                return BuyFragment.createInstance();
            case SELL_TAB:
                return SellFragment.createInstance();
        }
        return null;
    }

    public static Fragment createInstance(String tabType) {
        Bundle arg = new Bundle();
        arg.putString("TAB_TYPE",tabType);
        Fragment fragment = new BuySellTabFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
