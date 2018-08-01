package com.tokopedia.navigation.presentation.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation_common.NotificationsModel;
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

import static com.tokopedia.navigation.data.GlobalNavConstant.BUYER;
import static com.tokopedia.navigation.data.GlobalNavConstant.KOMPLAIN;
import static com.tokopedia.navigation.data.GlobalNavConstant.MENUNGGU_KONFIRMASI;
import static com.tokopedia.navigation.data.GlobalNavConstant.PENJUALAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.PESANAN_BARU;
import static com.tokopedia.navigation.data.GlobalNavConstant.PESANAN_DIPROSES;
import static com.tokopedia.navigation.data.GlobalNavConstant.SAMPAI_TUJUAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.SELLER;
import static com.tokopedia.navigation.data.GlobalNavConstant.SELLER_INFO;
import static com.tokopedia.navigation.data.GlobalNavConstant.PEMBELIAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.SIAP_DIKIRIM;
import static com.tokopedia.navigation.data.GlobalNavConstant.SEDANG_DIKIRIM;

/**
 * Created by meta on 24/07/18.
 */
public class NotificationFragment extends BaseParentFragment implements NotificationView {

    private SwipeRefreshLayout swipeRefreshLayout;

    private NotificationAdapter adapter;

    @Inject NotificationPresenter presenter;

    @Override
    public int resLayout() {
        return R.layout.fragment_notification;
    }

    private void initInjector() {
        DaggerGlobalNavComponent.builder()
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new NotificationAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getDrawerNotification());

        adapter.setOnNotifClickListener((parent, child) -> {
            Intent intent = getCallingIntent(parent, child);
            if (intent != null) {
                startActivity(intent);
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
        CommonUtils.dumper(message);
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

    boolean isHasAdded = false;

    @Override
    public void renderNotification(NotificationsModel data, boolean isHasShop) {
        if (!isHasAdded) {
            if (isHasShop) {
                adapter.add(sellerInfoData(), SELLER_INFO);
                adapter.add(sellerData(), PENJUALAN);
            }
            adapter.add(complain(isHasShop));
            isHasAdded = !isHasAdded;
        }
        adapter.updateValue(data);
    }

    private List<DrawerNotification> getData() {
        List<DrawerNotification> notifications = new ArrayList<>();

        DrawerNotification buyer = new DrawerNotification();
        buyer.setId(PEMBELIAN);
        buyer.setTitle(getString(R.string.pembelian));

        List<DrawerNotification.ChildDrawerNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_KONFIRMASI, getString(R.string.menunggu_konfirmasi)));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(PESANAN_DIPROSES, getString(R.string.pesanan_diproses)));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM, getString(R.string.sedang_dikirim)));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN, getString(R.string.sampai_tujuan)));
        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        return notifications;
    }

    private DrawerNotification complain(boolean isHasShop) {
        DrawerNotification complain = new DrawerNotification();
        complain.setId(KOMPLAIN);
        complain.setTitle(getString(R.string.komplain_saya));

        List<DrawerNotification.ChildDrawerNotification> childComplain = new ArrayList<>();
        childComplain.add(new DrawerNotification.ChildDrawerNotification(BUYER, getString(R.string.sebagai_pembeli)));
        if (isHasShop) {
            childComplain.add(new DrawerNotification.ChildDrawerNotification(SELLER, getString(R.string.sebagai_penjual)));
        }
        complain.setChilds(childComplain);

        return complain;
    }

    private DrawerNotification sellerInfoData() {
        DrawerNotification inbox = new DrawerNotification();

        List<DrawerNotification.ChildDrawerNotification> childInbox = new ArrayList<>();
        childInbox.add(new DrawerNotification.ChildDrawerNotification(SELLER_INFO, getString(R.string.info_penjual)));
        inbox.setChilds(childInbox);
        return inbox;
    }

    private DrawerNotification sellerData() {
        DrawerNotification seller = new DrawerNotification();
        seller.setId(PENJUALAN);
        seller.setTitle(getString(R.string.penjualan));

        List<DrawerNotification.ChildDrawerNotification> childSeller = new ArrayList<>();
        childSeller.add(new DrawerNotification.ChildDrawerNotification(PESANAN_BARU, getString(R.string.pesanan_baru)));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SIAP_DIKIRIM, getString(R.string.siap_dikirim)));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM, getString(R.string.sedang_dikirim)));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN, getString(R.string.sampai_tujuan)));
        seller.setChilds(childSeller);
        return seller;
    }

    private Intent getCallingIntent(int parentPosition, int childPosition) {
        Intent intent = null;
        DrawerNotification item = adapter.getItem(parentPosition);
        if (item != null && item.getId() != null) {
            DrawerNotification.ChildDrawerNotification child = adapter
                    .getItem(parentPosition).getChilds().get(childPosition);
            switch (item.getId()) {
                case PEMBELIAN:
                    if (child.getId() == MENUNGGU_KONFIRMASI) {
                        RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                    } else if (child.getId() == PESANAN_DIPROSES){
                        RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                    } else if (child.getId() == SEDANG_DIKIRIM){
                        RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                    } else if (child.getId() == SAMPAI_TUJUAN){
                        RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                    }
                    break;
                case PENJUALAN:
                    if (child.getId() == PESANAN_BARU) {
                        RouteManager.route(getActivity(), "tokopedia://seller/payment");
                    } else if (child.getId() == SIAP_DIKIRIM){
                        RouteManager.route(getActivity(), "tokopedia://seller/payment");
                    } else if (child.getId() == SEDANG_DIKIRIM){
                        RouteManager.route(getActivity(), "tokopedia://seller/payment");
                    } else if (child.getId() == SAMPAI_TUJUAN){
                        RouteManager.route(getActivity(), "tokopedia://seller/payment");
                    }
                    break;
                case KOMPLAIN:
                    if (child.getId() == BUYER) {
                        intent = ((GlobalNavRouter)getActivity().getApplication())
                                .getResolutionCenterIntentBuyer(getActivity());
                    } else if (child.getId()  == SELLER) {
                        intent = ((GlobalNavRouter)getActivity().getApplication())
                                .getResolutionCenterIntentSeller(getActivity());
                    }
                    break;
            }
        } else {
            intent = ((GlobalNavRouter)getActivity().getApplication())
                    .getSellerInfoCallingIntent(getActivity());
        }
        return intent;
    }
}
