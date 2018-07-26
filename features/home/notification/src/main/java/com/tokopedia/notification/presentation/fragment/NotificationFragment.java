package com.tokopedia.notification.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.notification.R;
import com.tokopedia.notification.domain.Notification;
import com.tokopedia.notification.presentation.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meta on 24/07/18.
 */
public class NotificationFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_notification, container, false);

        RecyclerView recyclerView = parentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        NotificationAdapter adapter = new NotificationAdapter(getActivity());
        adapter.addAll(listDataNotifications());
        recyclerView.setAdapter(adapter);

        return parentView;
    }

    private List<Notification> listDataNotifications() {
        List<Notification> notifications = new ArrayList<>();

        Notification inbox = new Notification();

        List<Notification.ChildNotification> childInbox = new ArrayList<>();
        childInbox.add(new Notification.ChildNotification("Info Penjual", false, 2));
        inbox.setChilds(childInbox);
        notifications.add(inbox);

        Notification buyer = new Notification();
        buyer.setId("2");
        buyer.setTitle("Pembelian");

        List<Notification.ChildNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new Notification.ChildNotification("Pesanan Dibatalkan", false, 0));
        childBuyer.add(new Notification.ChildNotification("Konfirmasi Penerimaan", true, 0));
        childBuyer.add(new Notification.ChildNotification("Daftar Pembelian", false, 0));
        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        Notification seller = new Notification();
        seller.setId("3");
        seller.setTitle("Penjualan");

        List<Notification.ChildNotification> childSeller = new ArrayList<>();
        childSeller.add(new Notification.ChildNotification("Pesanan Baru", false, 5));
        childSeller.add(new Notification.ChildNotification("Status Pengiriman", false, 0));
        childSeller.add(new Notification.ChildNotification("Daftar Penjualan", false, 0));
        seller.setChilds(childSeller);
        notifications.add(seller);

        Notification complain = new Notification();
        complain.setId("4");
        complain.setTitle("Komplain Saya");

        List<Notification.ChildNotification> childComplain = new ArrayList<>();
        childComplain.add(new Notification.ChildNotification("Sebagai Penjual", false, 0));
        childComplain.add(new Notification.ChildNotification("Sebagai Pembeli", false, 0));
        complain.setChilds(childComplain);
        notifications.add(complain);
        return notifications;
    }
}
