package com.tokopedia.core.purchase.presenter;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.purchase.listener.TxSummaryViewListener;
import com.tokopedia.core.purchase.model.TxSummaryItem;
import com.tokopedia.core.var.NotificationVariable;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryPresenterImpl implements TxSummaryPresenter {
    private static final String TAG = TxSummaryPresenterImpl.class.getSimpleName();
    private final TxSummaryViewListener viewListener;

    public TxSummaryPresenterImpl(TxSummaryViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void getNotificationPurcase(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
        List<Integer> countList = cache.getArrayListInteger(TkpdCache.Key.PURCHASE_COUNT);
        List<TxSummaryItem> summaryItemList = new ArrayList<>();
        summaryItemList.add(new TxSummaryItem(context.getString(R.string.payment_confirm),
                context.getString(R.string.payment_confirm_desc), countList.get(0)));
        summaryItemList.add(new TxSummaryItem(context.getString(R.string.payment_verify),
                context.getString(R.string.payment_verification_desc), countList.get(1)));
        summaryItemList.add(new TxSummaryItem(context.getString(R.string.order_status),
                context.getString(R.string.order_status_desc), countList.get(2)));
        summaryItemList.add(new TxSummaryItem(context.getString(R.string.delivery_confirm),
                context.getString(R.string.title_receive_confirmation_dashboard_desc),
                countList.get(3)));
        summaryItemList.add(new TxSummaryItem(context.getString(R.string.reorder),
                context.getString(R.string.title_transaction_list_desc), countList.get(4)));
        viewListener.renderPurchaseSummary(summaryItemList);
    }

    @Override
    public void getNotificationFromNetwork(final Context context) {
        NotificationVariable notif = MainApplication.getNotifInstance();
        notif.setContext((Activity) context);
        notif.GetNotif();
        notif.SetOnNotifRefresh(new NotificationVariable.OnNotifRefreshListener() {

            @Override
            public void OnNotifRefresh() {
                getNotificationPurcase(context);
            }

            @Override
            public void OnNotifRefreshStart() {

            }
        });
    }

    @Override
    public void onDestroyView() {

    }
}
