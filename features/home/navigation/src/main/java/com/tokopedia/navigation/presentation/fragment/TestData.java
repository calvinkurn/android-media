package com.tokopedia.navigation.presentation.fragment;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.DrawerNotification;
import com.tokopedia.navigation.domain.model.PurchaseNotification;
import com.tokopedia.navigation.domain.model.SaleNotification;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.navigation.GlobalNavConstant.MENUNGGU_KONFIRMASI;
import static com.tokopedia.navigation.GlobalNavConstant.MENUNGGU_PEMBAYARAN;
import static com.tokopedia.navigation.GlobalNavConstant.PEMBELIAN;
import static com.tokopedia.navigation.GlobalNavConstant.PESANAN_DIPROSES;
import static com.tokopedia.navigation.GlobalNavConstant.SAMPAI_TUJUAN;
import static com.tokopedia.navigation.GlobalNavConstant.SEDANG_DIKIRIM;

public class TestData {

    public static List<Visitable> getData(Context context) {
        List<Visitable> notifications = new ArrayList<>();
        notifications.addAll(getPembayaranData(context));
        notifications.addAll(getPembelianData(context));
        return notifications;
    }

    private static List<PurchaseNotification> getPembayaranData(Context context) {
        List<PurchaseNotification> notifications = new ArrayList<>();

        PurchaseNotification buyer = new PurchaseNotification();
        buyer.setId(PEMBELIAN);
        buyer.setTitle(context.getString(R.string.pembelian));
        List<DrawerNotification.ChildDrawerNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_PEMBAYARAN,
                context.getString(R.string.menunggu_pembayaran), ApplinkConst.PMS));

        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_KONFIRMASI,
                context.getString(R.string.menunggu_konfirmasi), ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(PESANAN_DIPROSES,
                context.getString(R.string.pesanan_diproses), ApplinkConst.MARKETPLACE_ORDER_PROCESSED));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
                context.getString(R.string.sedang_dikirim), ApplinkConst.MARKETPLACE_SENT));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
                context.getString(R.string.sampai_tujuan), ApplinkConst.MARKETPLACE_DELIVERED));

        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        return notifications;
    }

    private static List<SaleNotification> getPembelianData(Context context) {
        List<SaleNotification> notifications = new ArrayList<>();

        SaleNotification buyer = new SaleNotification();
        buyer.setId(PEMBELIAN);
        buyer.setTitle(context.getString(R.string.pembelian));
        List<DrawerNotification.ChildDrawerNotification> childBuyer = new ArrayList<>();
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_PEMBAYARAN,
                context.getString(R.string.menunggu_pembayaran), ApplinkConst.PMS));

        childBuyer.add(new DrawerNotification.ChildDrawerNotification(MENUNGGU_KONFIRMASI,
                context.getString(R.string.menunggu_konfirmasi), ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(PESANAN_DIPROSES,
                context.getString(R.string.pesanan_diproses), ApplinkConst.MARKETPLACE_ORDER_PROCESSED));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
                context.getString(R.string.sedang_dikirim), ApplinkConst.MARKETPLACE_SENT));
        childBuyer.add(new DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
                context.getString(R.string.sampai_tujuan), ApplinkConst.MARKETPLACE_DELIVERED));

        buyer.setChilds(childBuyer);
        notifications.add(buyer);

        return notifications;
    }

}
