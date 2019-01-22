package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.VerticalSpaceItemDecoration;
import com.tokopedia.home.explore.di.ExploreComponent;
import com.tokopedia.home.explore.di.DaggerExploreComponent;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.activity.ExploreActivity;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
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
    private static final String NAME_PINJAMAN_ONLINE = "Pinjaman Online";

    @Inject
    UserSession userSession;

    private int TYPE_FRAGMENT;

    private VerticalSpaceItemDecoration spaceItemDecoration;
    private ExploreSectionViewModel data;

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
        ((IHomeRouter) getActivity().getApplication()).openIntermediaryActivity(
                getActivity(),
                String.valueOf(data.getCategoryId()),
                data.getName()
        );
    }

    @Override
    public void onDigitalItemClicked(LayoutRows data) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof IHomeRouter
                && ((IHomeRouter) getActivity().getApplicationContext()).isSupportApplink(data.getApplinks())) {
            ((IHomeRouter) getActivity().getApplication()).onDigitalItemClickFromExploreHome(
                    getActivity(),
                    data.getApplinks(),
                    String.valueOf(data.getCategoryId()),
                    data.getName(),
                    data.getUrl());
        } else {
            onGimickItemClicked(data);
        }
        TrackingUtils.sendMoEngageClickMainCategoryIcon(getActivity(), data.getName());
    }

    @Override
    public void onGimickItemClicked(LayoutRows data) {

        if (NAME_PINJAMAN_ONLINE.equalsIgnoreCase(data.getName())) {
            getActivity().startActivity(((IHomeRouter) getActivity().getApplication()).getInstantLoanIntent(getActivity()));
            return;
        }

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
                        getActivity(),
                        HomePageTracking.BELI_INI_ITU_CLICK,
                        String.format("%s - %s", data.getCategoryId(), data.getName())
                );
                break;
            case TYPE_BAYAR:
                HomePageTracking.eventClickExplorerItem(
                        getActivity(),
                        HomePageTracking.BAYAR_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_PESAN:
                HomePageTracking.eventClickExplorerItem(
                        getActivity(),
                        HomePageTracking.PESAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_AJUKAN:
                HomePageTracking.eventClickExplorerItem(
                        getActivity(),
                        HomePageTracking.AJUKAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_JUAL:
                HomePageTracking.eventClickExplorerItem(
                        getActivity(),
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
            HomePageTracking.eventClickEditShop(getActivity());
            onGoToShopSetting();
        } else {
            HomePageTracking.eventClickOpenShop(getActivity());
            onGoToCreateShop();
        }
    }

    @Override
    public void onApplinkClicked(LayoutRows data) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof IHomeRouter
                && ((IHomeRouter) getActivity().getApplicationContext()).isSupportApplink(data.getApplinks())){
            openApplink(data.getApplinks());
        } else {
            openWebViewURL(data.getUrl(), getActivity());
        }
    }

    private void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            ((IHomeRouter) getActivity().getApplicationContext())
                    .goToApplinkActivity(getActivity(), applink);
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            if (getActivity() != null
                    && getActivity().getApplicationContext() instanceof IHomeRouter) {
                Intent intent = ((IHomeRouter) (getActivity()).getApplication())
                        .getBannerWebViewIntent(
                                getActivity(),
                                url);
                getActivity().startActivity(intent);
                context.startActivity(intent);
            }
        }
    }

    private void openWebViewGimicURL(String url, String label, String title) {
        if (!url.equals("")) {
            if (getActivity() != null
                    && getActivity().getApplicationContext() instanceof IHomeRouter) {
                Intent intent = ((IHomeRouter) (getActivity()).getApplication())
                        .openWebViewGimicURLIntentFromExploreHome(
                                getActivity(),
                                url,
                                title);
                getActivity().startActivity(intent);
                HomePageTracking.eventHomeGimmick(getActivity(), label);
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
                HomePageTracking.eventClickOpenShop(getActivity());
                onGoToCreateShop();
            }
        } else {
            HomePageTracking.eventClickOpenShop(getActivity());
            onGoToLogin();
        }
    }

    private void onGoToLogin() {
        if(getActivity() != null &&
                getActivity().getApplication() instanceof IHomeRouter){
            Intent intent = ((IHomeRouter) getActivity().getApplication()).getLoginIntent(getContext());
            Intent intentHome = ((IHomeRouter) getActivity().getApplication()).getHomeIntent(getContext());
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivities(new Intent[]{intentHome, intent});
            getActivity().finish();
        }
    }

    private void onGoToCreateShop() {
        if(getActivity() != null &&
                getActivity().getApplication() instanceof IHomeRouter){
            Intent intent = ((IHomeRouter) getActivity().getApplication())
                    .getActivityShopCreateEdit(getContext());
            getActivity().startActivity(intent);
        }
    }

    private void onGoToShop(String shopId) {
        Intent intent = ((IHomeRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShopSetting() {
        ((IHomeRouter) getActivity().getApplicationContext()).goToManageShop(getActivity());
    }

    @Override
    public void onDigitalMoreClicked() {

    }

    public void setData(ExploreSectionViewModel data) {
        this.data = data;
        renderData();
    }

    private void renderData() {
        if (data != null && isAdded()) {
            renderList(data.getVisitableList());
        }
    }
}
