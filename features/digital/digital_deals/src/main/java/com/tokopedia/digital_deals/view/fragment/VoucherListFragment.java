package com.tokopedia.digital_deals.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;

public class VoucherListFragment extends BaseDaggerFragment {



    public static Fragment createInstance() {
        Fragment fragment = new VoucherListFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem_vouchers, container, false);

        return view;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

}
