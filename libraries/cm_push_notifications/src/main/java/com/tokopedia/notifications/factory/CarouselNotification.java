package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.common.CarousalUtilities;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.Carousal;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CarouselNotification extends BaseNotification {


    CarouselNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setSmallIcon(getDrawableIcon());
        builder.setAutoCancel(false);
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        if (baseNotificationModel.getCarousalList() == null || baseNotificationModel.getCarousalList().size() == 0)
            return null;
        builder.setContentIntent(getImagePendingIntent(baseNotificationModel.getCarousalList().get(baseNotificationModel.getCarousalIndex()),
                getRequestCode()));

        CarousalUtilities.downloadImages(context, baseNotificationModel.getCarousalList());
        RemoteViews remoteViews = getCarousalRemoteView(baseNotificationModel.getCarousalList(), baseNotificationModel.getCarousalIndex());
        builder.setCustomBigContentView(remoteViews);
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        return builder.build();
    }

    /*
     * create RemoteViews using BaseNotificationModel
     *
     * */
    private RemoteViews getCarousalRemoteView(List<Carousal> carousalList, int index) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.carousal_layout);
        Carousal carousal = carousalList.get(index);
        carousal.setIndex(index);
        if (!TextUtils.isEmpty(carousal.getText())) {
            remoteView.setViewVisibility(R.id.tvTitle, View.VISIBLE);
            remoteView.setTextViewText(R.id.tvTitle, CMNotificationUtils.getSpannedTextFromStr(carousal.getText()));
        } else {
            remoteView.setViewVisibility(R.id.tvTitle, View.GONE);
        }
        if (index == 0) {
            remoteView.setViewVisibility(R.id.ivArrowLeft, View.GONE);
            remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE);
            remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getArrowClickPendingIntent(carousalList, getRequestCode(), CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK, index));

        } else if (index == carousalList.size() - 1) {
            remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE);
            remoteView.setViewVisibility(R.id.ivArrowRight, View.GONE);
            remoteView.setOnClickPendingIntent(R.id.ivArrowLeft, getArrowClickPendingIntent(carousalList, getRequestCode(), CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK, index));

        } else {
            remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE);
            remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE);
            remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getArrowClickPendingIntent(carousalList, getRequestCode(), CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK, index));
            remoteView.setOnClickPendingIntent(R.id.ivArrowLeft, getArrowClickPendingIntent(carousalList, getRequestCode(), CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK, index));

        }
        remoteView.setImageViewBitmap(R.id.iv_banner, CarousalUtilities.carousalLoadImageFromStorage(carousal.getFilePath()));
        return remoteView;
    }

    private PendingIntent getArrowClickPendingIntent(List<Carousal> carousalList, int requestCode, String action, int index) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(action);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.PayloadKeys.CHANNEL, baseNotificationModel.getChannelName());
        intent.putExtra(CMConstant.PayloadKeys.UPDATE, true);
        intent.putExtra(CMConstant.PayloadKeys.CAROUSEL_INDEX, index);
        intent.putParcelableArrayListExtra(CMConstant.ReceiverExtraData.CAROUSAL_DATA, (ArrayList<? extends Parcelable>) carousalList);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

    private PendingIntent getImagePendingIntent(Carousal carousal, int requestCode) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_CAROUSAL_IMAGE_CLICK);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.ReceiverExtraData.CAROUSAL_DATA_ITEM, carousal);
        intent.putExtras(getBundle(baseNotificationModel));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

}
