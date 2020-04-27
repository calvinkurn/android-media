package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.TrendingDealsAdapter;
import com.tokopedia.digital_deals.view.utils.TrendingDealsCallBacks;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static android.app.Activity.RESULT_OK;

public class TrendingDealsFragment extends BaseDaggerFragment implements DealsCategoryAdapter.INavigateToActivityRequest, TrendingDealsAdapter.INavigateToActivityRequest{

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
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.trending_deals_fragment, container, false);
        setHasOptionsMenu(false);
        setViewIds(view);

        TrendingDealsAdapter trendingDealsAdapter = new TrendingDealsAdapter(getContext(), mAdapterCallbacks, this, trendingDealsCallBacks.getTrendingDealsUrl(), trendingDealsCallBacks.getToolBarTitle(), false, true, trendingDealsCallBacks.getHomePosition());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(trendingDealsAdapter);
        trendingDealsAdapter.startDataLoading();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DealsHomeActivity)
            trendingDealsCallBacks = (DealsHomeActivity) activity;
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar);
        appBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.app_bar_layout);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.digital_deals.R.drawable.toolbar_back_black));
        if (!TextUtils.isEmpty(trendingDealsCallBacks.getToolBarTitle())) {
            toolbar.setTitle(trendingDealsCallBacks.getToolBarTitle());
        } else {
            toolbar.setTitle("Trending Deals");
        }
        recyclerView = view.findViewById(com.tokopedia.digital_deals.R.id.rv_trending_deals);
    }

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
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
                            if (recyclerView.getAdapter() != null) {
                                ((DealsCategoryAdapter) recyclerView.getAdapter()).setLike(adapterPosition);
                            }
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    AdapterCallback mAdapterCallbacks = new AdapterCallback() {
        @Override
        public void onRetryPageLoad(int pageNumber) {

        }

        @Override
        public void onEmptyList(Object rawObject) {
        }

        @Override
        public void onStartFirstPageLoad() {
        }

        @Override
        public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {
        }

        @Override
        public void onStartPageLoad(int pageNumber) {

        }

        @Override
        public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {

        }

        @Override
        public void onError(int pageNumber) {
        }
    };

    @Override
    public void onDestroy() {
        try {
            if (recyclerView.getAdapter() != null) {
                ((TrendingDealsAdapter) recyclerView.getAdapter()).unsubscribeUseCase();
            }
        } catch (Exception e) {

        }
        super.onDestroy();
    }
}
