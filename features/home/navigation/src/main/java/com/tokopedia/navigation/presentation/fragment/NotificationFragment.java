package com.tokopedia.navigation.presentation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation_common.model.NotifcenterUnread;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.navigation.domain.model.DrawerNotification;
import com.tokopedia.navigation.presentation.adapter.NotificationAdapter;
import com.tokopedia.navigation.presentation.base.BaseParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.NotificationPresenter;
import com.tokopedia.navigation.presentation.view.NotificationView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.navigation.GlobalNavConstant.*;

/**
 * Created by meta on 24/07/18.
 */
public class NotificationFragment extends BaseParentFragment implements NotificationView {

    private static final String IS_ENABLE_NOTIF_CENTER = "mainapp_enable_notif_center";

    private SwipeRefreshLayout swipeRefreshLayout;
    private View emptyLayout;

    private NotificationAdapter adapter;

    @Inject NotificationPresenter presenter;
    @Inject GlobalNavAnalytics globalNavAnalytics;

    private boolean isHasAdded = false;

    @Override
    public int resLayout() {
        return R.layout.fragment_notification;
    }

    private void initInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    @Override
    public void initView(View view) {
        this.initInjector();
        presenter.setView(this);

        swipeRefreshLayout = parentView.findViewById(R.id.swipe);
        RecyclerView recyclerView = parentView.findViewById(R.id.recyclerview);
        emptyLayout = parentView.findViewById(R.id.empty_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);

        adapter = new NotificationAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getDrawerNotification());

        adapter.setOnNotifClickListener((parent, child) -> {
            sendTracking(parent, child);
            DrawerNotification item = adapter.getItem(parent);
            if (getActivity() != null && item != null
                    && item.getChilds() != null
                    && item.getChilds().get(child) != null) {
                RouteManager.route(getActivity(), item.getChilds().get(child).getApplink());
            }
        });

        adapter.addAll(getData());
    }

    @Override
    public void loadData() { }

    @Override
    public void onStartLoading() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String message) {
        emptyLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), emptyLayout, message, () ->
                presenter.getDrawerNotification());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.notifications);
    }

    @Override
    public void renderNotification(NotificationsModel data, NotifcenterUnread unread,
                                   boolean isHasShop) {
        emptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        if (!isHasAdded) {
            if (isHasShop) {
                adapter.add(sellerData(), PENJUALAN);
            }
            adapter.add(complain(isHasShop));
            isHasAdded = !isHasAdded;
        }
        adapter.updateValue(data, unread);
    }

    private List<DrawerNotification> getData() {
        List<DrawerNotification> notifications = new ArrayList<>();

        addUpdateNotification(notifications);

        DrawerNotification buyer = new DrawerNotification();
        buyer.setId(PEMBELIAN);
        buyer.setTitle(getString(R.string.pembelian));

        List<DrawerNotification.ChildDrawerNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_PEMBAYARAN,
                getString(R.string.menunggu_pembayaran), ApplinkConst.PMS));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_KONFIRMASI,
                getString(R.string.menunggu_konfirmasi), ApplinkConst.PURCHASE_CONFIRMED));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(PESANAN_DIPROSES,
                getString(R.string.pesanan_diproses), ApplinkConst.PURCHASE_PROCESSED));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
                getString(R.string.sedang_dikirim), ApplinkConst.PURCHASE_SHIPPED));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
                getString(R.string.sampai_tujuan), ApplinkConst.PURCHASE_DELIVERED));

        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        return notifications;
    }

    private void addUpdateNotification(List<DrawerNotification> notifications) {
        if (shouldAddUserInfo()) {
            DrawerNotification update = new DrawerNotification();
            update.setId(UPDATE);
            update.setTitle(getString(R.string.update));

            List<DrawerNotification.ChildDrawerNotification> childUpdate = new ArrayList<>();
            childUpdate.add(new DrawerNotification.ChildDrawerNotification(NEWEST_INFO,
                    getString(R.string.info_terbaru), ApplinkConst.BUYER_INFO));

            update.setChilds(childUpdate);
            notifications.add(update);
        }
    }

    private DrawerNotification complain(boolean isHasShop) {
        DrawerNotification complain = new DrawerNotification();
        complain.setId(KOMPLAIN);
        complain.setTitle(getString(R.string.komplain_saya));

        List<DrawerNotification.ChildDrawerNotification> childComplain = new ArrayList<>();
        childComplain.add(new DrawerNotification.ChildDrawerNotification(BUYER,
                getString(R.string.sebagai_pembeli), ApplinkConst.RESCENTER_BUYER));
        if (isHasShop) {
            childComplain.add(new DrawerNotification.ChildDrawerNotification(SELLER,
                    getString(R.string.sebagai_penjual), ApplinkConst.RESCENTER_SELLER));
        }
        complain.setChilds(childComplain);

        return complain;
    }

    private DrawerNotification sellerData() {
        DrawerNotification seller = new DrawerNotification();
        seller.setId(PENJUALAN);
        seller.setTitle(getString(R.string.penjualan));

        List<DrawerNotification.ChildDrawerNotification> childSeller = new ArrayList<>();
        childSeller.add(new DrawerNotification.ChildDrawerNotification(PESANAN_BARU,
                getString(R.string.pesanan_baru), ApplinkConst.SELLER_NEW_ORDER));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SIAP_DIKIRIM,
                getString(R.string.siap_dikirim), ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
                getString(R.string.sedang_dikirim), ApplinkConst.SELLER_PURCHASE_SHIPPED));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
                getString(R.string.sampai_tujuan), ApplinkConst.SELLER_PURCHASE_DELIVERED));
        seller.setChilds(childSeller);
        return seller;
    }

    private void sendTracking(int parent, int child) {
        DrawerNotification parentItem = adapter.getItem(parent);
        if (parentItem != null && parentItem.getChilds() != null) {
            DrawerNotification.ChildDrawerNotification childItem =
                    parentItem.getChilds().get(child);

            String section = "";
            if (parentItem.getTitle() != null)
                section = parentItem.getTitle();

            if (childItem != null) {
                globalNavAnalytics.eventNotificationPage(section.toLowerCase(),
                        childItem.getTitle().toLowerCase());
            }
        }

    }

    private boolean shouldAddUserInfo() {
        return getActivity()!= null
                && getActivity().getApplicationContext() instanceof GlobalNavRouter
                && ((GlobalNavRouter) getActivity().getApplicationContext())
                .getBooleanRemoteConfig(IS_ENABLE_NOTIF_CENTER, Boolean.TRUE);
    }
}
