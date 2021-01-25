package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.adapter.DealDetailsAllLocationsAdapter;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;


public class DealDetailsAllRedeemLocationsFragment extends BaseDaggerFragment implements SearchInputView.Listener {


    private DealFragmentCallbacks fragmentCallbacks;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private SearchInputView searchInputView;
    private LinearLayout noContentLayout;
    private DealDetailsAllLocationsAdapter adapter;
    List<Outlet> outlets = new ArrayList<>();


    public static Fragment createInstance() {
        Fragment fragment = new DealDetailsAllRedeemLocationsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_redeem_locations, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);

        if (fragmentCallbacks.getOutlets() != null) {
            outlets.addAll(fragmentCallbacks.getOutlets());
            adapter = new DealDetailsAllLocationsAdapter(outlets);
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
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar);
        noContentLayout = view.findViewById(com.tokopedia.digital_deals.R.id.no_content);
        appBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.app_bar_layout);
        searchInputView = view.findViewById(com.tokopedia.digital_deals.R.id.search_input_view);
        searchInputView.setSearchHint(getResources().getString(com.tokopedia.digital_deals.R.string.search_input_hint_deals_outlets));
        searchInputView.setSearchTextSize(getResources().getDimension(com.tokopedia.design.R.dimen.sp_16));
        searchInputView.setSearchImageView(MethodChecker.getDrawable(getActivity(),com.tokopedia.digital_deals.R.drawable.ic_search_deal));
        searchInputView.setListener(this);
        if (getActivity() instanceof CheckoutActivity) {
            appBarLayout.setVisibility(View.GONE);
        } else {
            ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.digital_deals.R.drawable.ic_close_deals));
            toolbar.setTitle(getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.redeem_locations));
        }
        recyclerView = view.findViewById(com.tokopedia.digital_deals.R.id.recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyboardHandler.hideSoftKeyboard(getActivity());
            }
        });

    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {
        if (!TextUtils.isEmpty(text) && fragmentCallbacks.getOutlets() != null) {
            outlets.clear();
            for (Outlet outlet : fragmentCallbacks.getOutlets()) {
                if ((!TextUtils.isEmpty(outlet.getName()) && outlet.getName().trim().toLowerCase().contains(text.trim().toLowerCase())) ||
                        (!TextUtils.isEmpty(outlet.getDistrict()) && outlet.getDistrict().trim().toLowerCase().contains(text.trim().toLowerCase()))) {
                    outlets.add(outlet);
                }
            }
            if(adapter != null) {
                adapter.notifyDataSetChanged();
            }
            if (outlets.size() == 0) {
                noContentLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            noContentLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            outlets.clear();
            if(fragmentCallbacks.getOutlets() != null) {
                outlets.addAll(fragmentCallbacks.getOutlets());
            }
            if(adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
