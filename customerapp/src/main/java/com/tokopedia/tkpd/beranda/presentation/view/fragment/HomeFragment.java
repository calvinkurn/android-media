package com.tokopedia.tkpd.beranda.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.di.DaggerHomeComponent;
import com.tokopedia.tkpd.beranda.di.HomeComponent;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.tkpd.home.ReactNativeActivity;
import com.tokopedia.tkpd.home.fragment.FragmentIndexCategory;
import com.tokopedia.tkpd.remoteconfig.RemoteConfigFetcher;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener {

    @Inject
    HomePresenter presenter;

    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        HomeComponent component = DaggerHomeComponent.builder().appComponent(getComponent(AppComponent.class)).build();
        component.inject(this);
        component.inject(presenter);
    }

    private void fetchRemoteConfig() {
        RemoteConfigFetcher remoteConfigFetcher = new RemoteConfigFetcher(getActivity());
        remoteConfigFetcher.fetch(new RemoteConfigFetcher.Listener() {
            @Override
            public void onComplete(FirebaseRemoteConfig remoteConfig) {
                firebaseRemoteConfig = remoteConfig;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        initRefreshLayout();
        fetchRemoteConfig();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void initRefreshLayout() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.getHomeData();
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new HomeRecycleAdapter(new HomeAdapterFactory(this), new ArrayList<Visitable>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionItemClicked(LayoutSections sections, int parentPosition, int childPosition) {

    }

    @Override
    public void onCategoryItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
        openActivity(data.getRedirectValue(), data.getName());
    }

    @Override
    public void onTopPicksItemClicked(TopPicksItemModel data, int parentPosition, int childPosition) {
        String url = data.getUrl();
        UnifyTracking.eventHomeTopPicksItem(data.getName(), data.getName());
        switch ((DeepLinkChecker.getDeepLinkType(url))) {
            case DeepLinkChecker.BROWSE:
                DeepLinkChecker.openBrowse(url, getActivity());
                break;
            case DeepLinkChecker.HOT:
                DeepLinkChecker.openHot(url, getActivity());
                break;
            case DeepLinkChecker.CATALOG:
                DeepLinkChecker.openCatalog(url, getActivity());
                break;
            default:
                openWebViewTopPicksURL(url);
        }
    }

    @Override
    public void onTopPicksMoreClicked(String url, int pos) {
        openWebViewTopPicksURL(url);
    }

    @Override
    public void onBrandsItemClicked(BrandDataModel data, int parentPosition, int childPosition) {
        UnifyTracking.eventClickOfficialStore(AppEventTracking.EventLabel.OFFICIAL_STORE + data.getShopName());
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(String.valueOf(data.getShopId()), ""));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void onBrandsMoreClicked(int pos) {
        if (SessionHandler.isV4Login(getContext())) {
            UnifyTracking.eventViewAllOSLogin();
        } else {
            UnifyTracking.eventViewAllOSNonLogin();
        }

        if (firebaseRemoteConfig != null
                && firebaseRemoteConfig.getBoolean(MAINAPP_SHOW_REACT_OFFICIAL_STORE)) {
            getActivity().startActivity(
                    ReactNativeActivity.createOfficialStoresReactNativeActivity(
                            getActivity(), ReactConst.Screen.OFFICIAL_STORE,
                            getString(R.string.react_native_banner_official_title)
                    )
            );
        } else {
            openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
        }
    }

    @Override
    public void onDigitalMoreClicked(int pos) {

    }

    @Override
    public void onCloseTicker(int pos) {

    }

    @Override
    public void onRefresh() {
        presenter.getHomeData();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setItems(List<Visitable> items) {
        adapter.setItems(items);
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void removeNetworkError() {

    }


    private void openActivity(String depID, String title) {
        IntermediaryActivity.moveTo(
                getActivity(),
                depID,
                title
        );
        Map<String, String> values = new HashMap<>();
        values.put(getString(R.string.value_category_name), title);
        UnifyTracking.eventHomeCategory(title);
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }

    public void openWebViewTopPicksURL(String url) {
        if (!url.isEmpty()) {
            startActivity(TopPicksWebView.newInstance(getActivity(), url));
        }
    }
}
