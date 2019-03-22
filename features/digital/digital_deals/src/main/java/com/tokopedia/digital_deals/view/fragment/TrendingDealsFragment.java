package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.utils.TrendingDealsCallBacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static android.app.Activity.RESULT_OK;

public class TrendingDealsFragment extends BaseDaggerFragment implements DealsCategoryAdapter.INavigateToActivityRequest {

    private TrendingDealsCallBacks trendingDealsCallBacks;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private int adapterPosition;
    private static String title;

    public static Fragment createInstance() {
        Fragment fragment = new TrendingDealsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_deals_fragment, container, false);
        setHasOptionsMenu(false);
        setViewIds(view);

        if (trendingDealsCallBacks.getTrendingDeals() != null) {
            DealsCategoryAdapter adapter = new DealsCategoryAdapter(trendingDealsCallBacks.getTrendingDeals(), DealsCategoryAdapter.HOME_PAGE, this, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DealsHomeActivity)
            trendingDealsCallBacks = (DealsHomeActivity) activity;
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back_black));
        if (!TextUtils.isEmpty(trendingDealsCallBacks.getToolBarTitle())) {
            toolbar.setTitle(trendingDealsCallBacks.getToolBarTitle());
        } else {
            toolbar.setTitle("Trending Deals");
        }
        recyclerView = view.findViewById(R.id.rv_trending_deals);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        this.adapterPosition = position;
        navigateToActivityRequest(intent, requestCode);
    }

    private void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    if (getActivity() != null && getActivity().getApplication() != null) {
                        UserSessionInterface userSession = new UserSession(getActivity());
                        if (userSession.isLoggedIn()) {
                            if (adapterPosition == -1) {
//                                startOrderListActivity();
                            } else {
                                if (recyclerView.getAdapter() != null)
                                    ((DealsCategoryAdapter) recyclerView.getAdapter()).setLike(adapterPosition);
                            }
                        }
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
