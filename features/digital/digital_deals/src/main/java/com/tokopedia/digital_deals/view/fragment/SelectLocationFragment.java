package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class SelectLocationFragment extends BottomSheets implements DealsLocationContract.View, DealsLocationAdapter.ActionListener {

    public static final String EXTRA_CALLBACK_LOCATION = "EXTRA_CALLBACK_LOCATION";


    private FlexboxLayout mainContent;
    private RecyclerView rvSearchResults;
    @Inject
    public DealsLocationPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;

    ActionListener actionListener;


    public static Fragment createInstance() {
        Fragment fragment = new SelectLocationFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof ActionListener) {
            actionListener = (ActionListener) getParentFragment();

        }
    }

    @Override
    protected String title() {
        return "Pilih Kota Terdekatmu untuk Pengalaman Terbaik";
    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }


    @Override
    public void renderFromSearchResults(List<Location> locationList, boolean isTopLocations, String... searchText) {

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        rvSearchResults.setLayoutManager(layoutManager);
//        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        rvSearchResults.setAdapter(new DealsLocationAdapter(locationList, this));
        SelectLocationFragment.this.updateHeight(convertDpToPixel(getContext().getResources().getDimensionPixelOffset(R.dimen.location_bottomsheet_height)));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);
    }

    public int convertDpToPixel(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int px = dp * ((int)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        actionListener.onLocationItemSelected(locationUpdated);
        dismiss();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_change_location;
    }

    @Override
    public void initView(View view) {
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        mainContent = view.findViewById(R.id.mainContent);
        DaggerDealsComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build().inject(this);
        mPresenter.attachView(this);
        mPresenter.getLocations();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }


    public interface ActionListener {
        void onLocationItemSelected(boolean locationUpdated);
    }
}
