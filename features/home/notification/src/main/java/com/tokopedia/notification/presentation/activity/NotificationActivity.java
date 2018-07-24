package com.tokopedia.notification.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.notification.R;
import com.tokopedia.notification.domain.Notification;
import com.tokopedia.notification.presentation.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meta on 20/06/18.
 */
public class NotificationActivity extends AppCompatActivity {

    public static Intent start(Context context) {
        return new Intent(context, NotificationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupToolbar();

        backButton(true);
        setTitle("Notification");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        NotificationAdapter adapter = new NotificationAdapter(this);
        adapter.addAll(listDataNotifications());
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    public void backButton(boolean state) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(state);
        }
    }

    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
