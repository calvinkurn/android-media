package com.tokopedia.core.loyaltysystem.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.loyaltysystem.facade.FacadeGetLoyaltySystem;
import com.tokopedia.core.loyaltysystem.model.LoyaltyNotification;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;

import com.tkpd.library.kirisame.network.entity.NetError;

/**
 * Created by ricoharisin on 9/21/15.
 */
public class LoyaltyNotificationUtil {

    private Context context;
    private final static String TAG = "Loyalty System";
    private final static String URL_MICROSITE = "https://www.tokopedia.com/lucky-deal/";

    public LoyaltyNotificationUtil(Context context) {
        this.context = context;
    }

    public void checkLoyaltyNotification() {
        if (SessionHandler.isV4Login(context)) getNotificationStatus();
    }

    private void getNotificationStatus() {
        Log.i(TAG, "Get notification status....");
        FacadeGetLoyaltySystem loyaltyGet = new FacadeGetLoyaltySystem(context);
        loyaltyGet.getLoyaltyNotification(getLoyaltyNotificationListener());

    }

    private FacadeGetLoyaltySystem.OnGetNotification getLoyaltyNotificationListener() {
        return new FacadeGetLoyaltySystem.OnGetNotification() {
            @Override
            public void onResult(LoyaltyNotification notification) {
                if (notification.Attr.NotifyBuyer) showDialogBuyer();
                else if (notification.Attr.NotifySeller) showDialogMerchant();
            }

            @Override
            public void onNetworkError(NetError error) {

            }

            @Override
            public void onError(ArrayList<String> MessageError) {

            }
        };
    }

    private void showDialogBuyer() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView = View.inflate(context, R.layout.dialog_loyalty_notification, null);

        TextView loyaltyMsg = (TextView) layoutView.findViewById(R.id.msg_lucky);
        TextView loyaltyMsgDetail = (TextView) layoutView.findViewById(R.id.msg_detail_lucky);
        TextView loyaltyMsgValid = (TextView) layoutView.findViewById(R.id.msg_valid_lucky);
        View backgroundLucky = layoutView.findViewById(R.id.back_lucky);

        backgroundLucky.setBackgroundColor(context.getResources().getColor(R.color.light_blue_300));
        loyaltyMsg.setText(getLoyaltyBuyerMessage());
        loyaltyMsgDetail.setText(getLoyaltyBuyerMessageDetail());
        loyaltyMsgValid.setText(getLoyaltyMessageValid());

        dialogBuilder.setView(layoutView);
        dialogBuilder.setNeutralButton(R.string.title_click_here, getBuyerClickListener());

        dialogBuilder.create().show();

    }

    private void showDialogMerchant() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView = View.inflate(context, R.layout.dialog_loyalty_notification, null);

        TextView loyaltyMsg = (TextView) layoutView.findViewById(R.id.msg_lucky);
        TextView loyaltyMsgDetail = (TextView) layoutView.findViewById(R.id.msg_detail_lucky);
        TextView loyaltyMsgValid = (TextView) layoutView.findViewById(R.id.msg_valid_lucky);
        View backgroundLucky = layoutView.findViewById(R.id.back_lucky);

        backgroundLucky.setBackgroundColor(context.getResources().getColor(R.color.green_400));
        loyaltyMsg.setText(getLoyaltyMerchantMessage());
        loyaltyMsgDetail.setText(getLoyaltyMerchantMessageDetail());
        loyaltyMsgValid.setText(getLoyaltyMessageValid());

        dialogBuilder.setView(layoutView);
        dialogBuilder.setNeutralButton(R.string.title_click_here, getBuyerClickListener());

        dialogBuilder.create().show();

    }

    private String getLoyaltyBuyerMessage() {
        String msg = TrackingUtils.getGtmString(AppEventTracking.GTM.LUCKY_BUYER);
        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.msg_lucky_buyer);
        }
        return msg;
    }

    private String getLoyaltyBuyerMessageDetail() {
        String msg = TrackingUtils.getGtmString(AppEventTracking.GTM.LUCKY_BUYER_DETAIL);
        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.msg_lucky_buyer_detail);
        }
        return msg;
    }

    private String getLoyaltyMessageValid() {
        String msg = TrackingUtils.getGtmString(AppEventTracking.GTM.LUCKY_BUYER_VALID);
        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.msg_lucky_valid);
        }
        return msg;
    }

    private String getLoyaltyMerchantMessage() {
        String msg = TrackingUtils.getGtmString(AppEventTracking.GTM.LUCKY_MERCHANT);
        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.msg_lucky_merchant);
        }
        return msg;
    }

    private String getLoyaltyMerchantMessageDetail() {
        String msg = TrackingUtils.getGtmString(AppEventTracking.GTM.LUCKY_MERCHANT_DETAIL);
        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.msg_lucky_merchant_detail);
        }
        return msg;
    }

    private DialogInterface.OnClickListener getBuyerClickListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToMicrosite();
                dialogInterface.dismiss();
            }
        };
    }

    private void goToMicrosite() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL_MICROSITE));
        context.startActivity(i);
    }

    public void showCustomNotification(LoyaltyNotification notification) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView = View.inflate(context, R.layout.dialog_loyalty_notification, null);

        TextView loyaltyMsg = (TextView) layoutView.findViewById(R.id.msg_lucky);
        TextView loyaltyMsgDetail = (TextView) layoutView.findViewById(R.id.msg_detail_lucky);
        TextView loyaltyMsgValid = (TextView) layoutView.findViewById(R.id.msg_valid_lucky);
        View backgroundLucky = layoutView.findViewById(R.id.back_lucky);

        if (notification.Attr.NotifyBuyer) {
            backgroundLucky.setBackgroundColor(context.getResources().getColor(R.color.light_blue_300));
            setText(loyaltyMsg, notification.Attr.contentBuyer1);
            setText(loyaltyMsgDetail, notification.Attr.contentBuyer2);
            setText(loyaltyMsgValid, notification.Attr.contentBuyer3);
        } else {
            backgroundLucky.setBackgroundColor(context.getResources().getColor(R.color.green_400));
            setText(loyaltyMsg, notification.Attr.contentSeller1);
            setText(loyaltyMsgDetail, notification.Attr.contentSeller2);
            setText(loyaltyMsgValid, notification.Attr.contentSeller3);
        }

        dialogBuilder.setView(layoutView);
        dialogBuilder.setNeutralButton(R.string.title_click_here, getCustomClickedListener(notification.Attr.url));

        dialogBuilder.create().show();
    }

    private void setText(TextView textView, String content) {
        try {
            if (!content.equals("")) {
                textView.setText(content);
            } else {
                textView.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            textView.setVisibility(View.GONE);
        }
    }

    private DialogInterface.OnClickListener getCustomClickedListener(final String url) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToCustomLink(url);
                dialog.dismiss();
            }
        };
    }

    private void goToCustomLink(String url) {
        Intent i = new Intent(context, BannerWebView.class);
        i.putExtra("url", url);
        context.startActivity(i);
    }


}
