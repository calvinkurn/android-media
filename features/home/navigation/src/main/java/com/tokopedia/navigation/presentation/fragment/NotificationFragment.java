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
import com.tokopedia.navigation.presentation.adapter.BaseListAdapter;
import com.tokopedia.navigation.presentation.adapter.NotificationAdapter;
import com.tokopedia.navigation.presentation.base.BaseParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.NotificationPresenter;
import com.tokopedia.navigation.presentation.view.NotificationView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.navigation.data.GlobalNavConstant.BELUM_DIBAYAR;
import static com.tokopedia.navigation.data.GlobalNavConstant.BUYER;
import static com.tokopedia.navigation.data.GlobalNavConstant.KOMPLAIN;
import static com.tokopedia.navigation.data.GlobalNavConstant.PENJUALAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.PESANAN_BARU;
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

    private Intent getCallingIntent(int parentPosition, int childPosition) {
        Intent intent = null;
        switch (parentPosition) {
            case SELLER_INFO:
                intent = ((GlobalNavRouter)getActivity().getApplication())
                        .getSellerInfoCallingIntent(getActivity());
                break;
            case PEMBELIAN:
                if (childPosition == SEDANG_DIKIRIM) {
                    RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                } else if (childPosition == SAMPAI_TUJUAN){
                    RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                }
                break;
            case PENJUALAN:
                if (childPosition == PESANAN_BARU) {
                    RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                } else if (childPosition == SIAP_DIKIRIM){
                    RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                } else if (childPosition == SAMPAI_TUJUAN){
                    RouteManager.route(getActivity(), "tokopedia://buyer/payment");
                }
                break;
            case KOMPLAIN:
                if (childPosition == BUYER) {
                    intent = ((GlobalNavRouter)getActivity().getApplication())
                            .getResolutionCenterIntentBuyer(getActivity());
                } else if (childPosition == SELLER) {
                    intent = ((GlobalNavRouter)getActivity().getApplication())
                            .getResolutionCenterIntentSeller(getActivity());
                }
                break;
        }
        return intent;
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

    @Override
    public void renderNotification(NotificationsModel data) {
        adapter.updateValue(data);
    }

    private List<DrawerNotification> getData() {
        List<DrawerNotification> notifications = new ArrayList<>();

        DrawerNotification inbox = new DrawerNotification();

        List<DrawerNotification.ChildDrawerNotification> childInbox = new ArrayList<>();
        childInbox.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.info_penjual)));
        inbox.setChilds(childInbox);
        notifications.add(inbox);

        DrawerNotification buyer = new DrawerNotification();
        buyer.setTitle(getString(R.string.pembelian));

        List<DrawerNotification.ChildDrawerNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.belum_dibayar)));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sedang_dikirim)));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sampai_tujuan)));
        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        DrawerNotification seller = new DrawerNotification();
        seller.setTitle(getString(R.string.penjualan));

        List<DrawerNotification.ChildDrawerNotification> childSeller = new ArrayList<>();
        childSeller.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.pesanan_baru)));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.siap_dikirim)));
        childSeller.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sampai_tujuan)));
        seller.setChilds(childSeller);
        notifications.add(seller);

        DrawerNotification complain = new DrawerNotification();
        complain.setTitle(getString(R.string.komplain_saya));

        List<DrawerNotification.ChildDrawerNotification> childComplain = new ArrayList<>();
        childComplain.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sebagai_pembeli)));
        childComplain.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sebagai_penjual)));
        complain.setChilds(childComplain);
        notifications.add(complain);
        return notifications;
    }
}
