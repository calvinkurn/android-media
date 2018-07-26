package com.tokopedia.notification.presentation.fragment;

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
import com.tokopedia.notification.R;
import com.tokopedia.notification.domain.Notification;
import com.tokopedia.notification.presentation.adapter.BaseListAdapter;
import com.tokopedia.notification.presentation.adapter.NotificationAdapter;
import com.tokopedia.notification.presentation.view.NotificationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meta on 24/07/18.
 */
public class NotificationFragment extends Fragment implements NotificationView {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_notification, container, false);

        swipeRefreshLayout = parentView.findViewById(R.id.swipe);
        RecyclerView recyclerView = parentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        NotificationAdapter adapter = new NotificationAdapter(getActivity());
        adapter.addAll(getData());
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

        return parentView;
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

    private List<Notification> getData() {
        List<Notification> notifications = new ArrayList<>();

        Notification inbox = new Notification();

        List<Notification.ChildNotification> childInbox = new ArrayList<>();
        childInbox.add(new Notification.ChildNotification(getString(R.string.info_penjual)));
        inbox.setChilds(childInbox);
        notifications.add(inbox);

        Notification buyer = new Notification();
        buyer.setTitle(getString(R.string.pembelian));

        List<Notification.ChildNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new Notification.ChildNotification(getString(R.string.belum_dibayar)));
        childBuyer.add(new Notification.ChildNotification(getString(R.string.sedang_dikirim)));
        childBuyer.add(new Notification.ChildNotification(getString(R.string.sampai_tujuan)));
        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        Notification seller = new Notification();
        seller.setTitle(getString(R.string.penjualan));

        List<Notification.ChildNotification> childSeller = new ArrayList<>();
        childSeller.add(new Notification.ChildNotification(getString(R.string.pesanan_baru)));
        childSeller.add(new Notification.ChildNotification(getString(R.string.siap_dikirim)));
        childSeller.add(new Notification.ChildNotification(getString(R.string.sampai_tujuan)));
        seller.setChilds(childSeller);
        notifications.add(seller);

        Notification complain = new Notification();
        complain.setTitle(getString(R.string.komplain_saya));

        List<Notification.ChildNotification> childComplain = new ArrayList<>();
        childComplain.add(new Notification.ChildNotification(getString(R.string.sebagai_penjual)));
        childComplain.add(new Notification.ChildNotification(getString(R.string.sebagai_pembeli)));
        complain.setChilds(childComplain);
        notifications.add(complain);
        return notifications;
    }
}
