package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.adapter.DealDetailsAllLocationsAdapter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;


public class DealDetailsAllRedeemLocationsFragment extends BaseDaggerFragment {


    private DealFragmentCallbacks fragmentCallbacks;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;


    public static Fragment createInstance() {
        Fragment fragment = new DealDetailsAllRedeemLocationsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem_locations, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);

        if (fragmentCallbacks.getOutlets() != null) {
            DealDetailsAllLocationsAdapter adapter = new DealDetailsAllLocationsAdapter(fragmentCallbacks.getOutlets());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DealDetailsActivity)
            fragmentCallbacks = (DealDetailsActivity) activity;
        else {
            fragmentCallbacks = (CheckoutActivity) activity;
        }
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        if (getActivity() instanceof CheckoutActivity) {
            appBarLayout.setVisibility(View.GONE);
        } else {
            ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_deals));
            toolbar.setTitle(getActivity().getResources().getString(R.string.redeem_locations));
        }
        recyclerView = view.findViewById(R.id.recyclerView);

    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
    }
}
