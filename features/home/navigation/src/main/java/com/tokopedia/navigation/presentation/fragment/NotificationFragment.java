package com.tokopedia.navigation.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.DrawerNotification;
import com.tokopedia.navigation.presentation.adapter.BaseListAdapter;
import com.tokopedia.navigation.presentation.adapter.NotificationAdapter;
import com.tokopedia.navigation.presentation.base.BaseParentFragment;
import com.tokopedia.navigation.presentation.view.NotificationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meta on 24/07/18.
 */
public class NotificationFragment extends BaseParentFragment implements NotificationView {

    private SwipeRefreshLayout swipeRefreshLayout;

    private NotificationAdapter adapter;

    @Override
    public int resLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    public void initView(View view) {
        swipeRefreshLayout = parentView.findViewById(R.id.swipe);
        RecyclerView recyclerView = parentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new NotificationAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        adapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    public void loadData() {
        adapter.addAll(getData());
    }

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
        childComplain.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sebagai_penjual)));
        childComplain.add(new DrawerNotification.ChildDrawerNotification(getString(R.string.sebagai_pembeli)));
        complain.setChilds(childComplain);
        notifications.add(complain);
        return notifications;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.notifications);
    }
}
