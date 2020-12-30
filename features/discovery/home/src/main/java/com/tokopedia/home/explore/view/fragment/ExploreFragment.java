package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.VerticalSpaceItemDecoration;
import com.tokopedia.home.explore.di.DaggerExploreComponent;
import com.tokopedia.home.explore.di.ExploreComponent;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.activity.ExploreActivity;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable, TypeFactory> implements CategoryAdapterListener {

    public static final String PARAM_TYPE_FRAGMENT = "PARAM_TYPE_FRAGMENT";
    public static final int TYPE_BELI = 0;
    public static final int TYPE_BAYAR = 1;
    public static final int TYPE_PESAN = 2;
    public static final int TYPE_AJUKAN = 3;
    public static final int TYPE_JUAL = 4;
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "core_web_view_extra_title";
    public static final String DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";

    @Inject
    UserSession userSession;

    private int TYPE_FRAGMENT;

    private VerticalSpaceItemDecoration spaceItemDecoration;
    private ExploreSectionDataModel data;

    public static ExploreFragment newInstance(int position) {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        args.putInt(PARAM_TYPE_FRAGMENT, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        if(getActivity() != null) {
            ExploreComponent component = DaggerExploreComponent.builder().baseAppComponent(
                    ((BaseMainApplication) getActivity().getApplication()
            ).getBaseAppComponent()).build();
            component.inject(this);
        }
    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        spaceItemDecoration = new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_card_home), true, 0);
        recyclerView.addItemDecoration(spaceItemDecoration);
        return recyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE_FRAGMENT = getArguments().getInt(PARAM_TYPE_FRAGMENT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        renderData();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected TypeFactory getAdapterTypeFactory() {
        return new ExploreAdapter(getFragmentManager(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onMarketPlaceItemClicked(LayoutRows data) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(getActivity(), data.getName());
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.DISCOVERY_CATEGORY_DETAIL);
        Bundle bundle = new Bundle();
        bundle.putString(DEPARTMENT_ID, String.valueOf(data.getCategoryId()));
        bundle.putString(DEPARTMENT_NAME, data.getName());
        intent.putExtras(bundle);
    }

    @Override
    public void onDigitalItemClicked(LayoutRows data) {
        if (getActivity() != null
                && RouteManager.isSupportApplink(getActivity(), data.getApplinks())) {
            onDigitalItemClickFromExploreHome(data);
        } else {
            onGimickItemClicked(data);
        }
        TrackingUtils.sendMoEngageClickMainCategoryIcon(getActivity(), data.getName());
    }

    private void onDigitalItemClickFromExploreHome(LayoutRows data){
        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(data.getApplinks())
                .categoryId(String.valueOf(data.getCategoryId()))
                .categoryName(data.getName())
                .url(data.getUrl())
                .build();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData);
        Intent intent = RouteManager.getIntent(getActivity(), data.getApplinks());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onGimickItemClicked(LayoutRows data) {

        String redirectUrl = data.getUrl();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(redirectUrl),
                    userSession.getDeviceId(),
                    userSession.getUserId());
            openWebViewGimicURL(resultGenerateUrl, data.getUrl(), data.getName());
        }
    }

    @Override
    public void trackingItemGridClick(LayoutRows data) {
        switch (TYPE_FRAGMENT) {
            case TYPE_BELI:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.BELI_INI_ITU_CLICK,
                        String.format("%s - %s", data.getCategoryId(), data.getName())
                );
                break;
            case TYPE_BAYAR:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.BAYAR_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_PESAN:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.PESAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_AJUKAN:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.AJUKAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_JUAL:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.JUAL_INI_ITU_CLICK,
                        data.getName()
                );
                break;
        }
    }

    @Override
    public void showNetworkError(String string) {
        if (isAdded() && getActivity() != null) {
            ((ExploreActivity) getActivity()).showNetworkError(string);
        }
    }

    @Override
    public void openShopSetting() {
        String shopId = userSession.getShopId();
        if (!shopId.equals("0")) {
            HomePageTracking.eventClickEditShop();
            onGoToShopSetting();
        } else {
            HomePageTracking.eventClickOpenShop();
            onGoToCreateShop();
        }
    }

    @Override
    public void onApplinkClicked(LayoutRows data) {
        if (getActivity() != null
                && RouteManager.isSupportApplink(getActivity(), data.getApplinks())){
            openApplink(data.getApplinks());
        } else {
            openWebViewURL(data.getUrl(), getActivity());
        }
    }

    private void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink) && RouteManager.isSupportApplink(getActivity(), applink)) {
            RouteManager.route(getActivity(), applink);
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (!url.equals("") && context != null) {
            if (getActivity() != null) {
                Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PROMO);
                intent.putExtra(EXTRA_URL, url);
                startActivity(intent);
            }
        }
    }

    private void openWebViewGimicURL(String url, String label, String title) {
        if (!url.equals("")) {
            if (getActivity() != null) {
                Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.WEBVIEW);
                intent.putExtra(EXTRA_TITLE, title);
                intent.putExtra(EXTRA_URL, url);
                getActivity().startActivity(intent);
                HomePageTracking.eventHomeGimmick(label);
            }
        }
    }

    @Override
    public void openShop() {
        if (userSession.isLoggedIn()) {
            String shopId = userSession.getShopId();
            if (!shopId.equals("0")) {
                onGoToShop(shopId);
            } else {
                HomePageTracking.eventClickOpenShop();
                onGoToCreateShop();
            }
        } else {
            HomePageTracking.eventClickOpenShop();
            onGoToLogin();
        }
    }

    private void onGoToLogin() {
        if(getActivity() != null){
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
            Intent intentHome = RouteManager.getIntent(getActivity(), ApplinkConst.HOME);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivities(new Intent[]{intentHome, intent});
            getActivity().finish();
        }
    }

    private void onGoToCreateShop() {
        if(getActivity() != null){
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.OPEN_SHOP);
            getActivity().startActivity(intent);
        }
    }

    private void onGoToShop(String shopId) {
        RouteManager.route(getActivity(), ApplinkConst.SHOP, shopId);
    }

    private void onGoToShopSetting() {
        RouteManager.route(getActivity(), ApplinkConstInternalMarketplace.STORE_SETTING);
    }

    @Override
    public void onDigitalMoreClicked() {

    }

    public void setData(ExploreSectionDataModel data) {
        this.data = data;
        renderData();
    }

    private void renderData() {
        if (data != null && isAdded()) {
            renderList(data.getVisitableList());
        }
    }
}
