package com.tokopedia.product.edit.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.product.edit.R;
import com.tokopedia.product.edit.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.product.edit.di.module.AddProductserviceModule;
import com.tokopedia.product.edit.domain.listener.ProductSubmitNotificationListener;
import com.tokopedia.product.common.model.edit.ProductViewModel;
import com.tokopedia.product.common.util.ProductStatus;
import com.tokopedia.product.edit.util.ProductEditModuleRouter;
import com.tokopedia.product.edit.view.presenter.AddProductServiceListener;
import com.tokopedia.product.edit.view.presenter.AddProductServicePresenter;

import java.util.HashMap;

import javax.inject.Inject;

public class UploadProductService extends BaseService implements AddProductServiceListener {
    public static final String TAG = "upload_product";

    public static final String ACTION_DRAFT_CHANGED = "com.tokopedia.draft.changed";
    private static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private static final String IS_ADD = "IS_ADD";

    @Inject
    AddProductServicePresenter presenter;

    private NotificationManager notificationManager;
    private HashMap<Integer, NotificationCompat.Builder> notificationBuilderMap = new HashMap<>();

    public static Intent getIntent(Context context, long draftProductId, boolean isAdd) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(DRAFT_PRODUCT_ID, draftProductId);
        intent.putExtra(IS_ADD, isAdd);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        DaggerAddProductServiceComponent
                .builder()
                .productComponent(((ProductEditModuleRouter) getApplication()).getProductComponent())
                .addProductserviceModule(new AddProductserviceModule())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final long draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, Long.MIN_VALUE);
        boolean isAdd = intent.getBooleanExtra(IS_ADD, true);
        presenter.uploadProduct(draftProductId, new ProductSubmitNotificationListener(
                (int) draftProductId, isAdd ? ProductStatus.ADD : ProductStatus.EDIT) {
            @Override
            public void addProgress() {
                super.addProgress();
                updateNotification(getId(), getCurrentCount(), getMaxCount());
            }

            @Override
            public void setProductViewModel(ProductViewModel productViewModel) {
                super.setProductViewModel(productViewModel);
                createNotification(getId(), productViewModel.getProductName());
            }
        });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSuccessAddProduct(ProductSubmitNotificationListener notificationCountListener) {
        NotificationCompat.Builder notificationBuilder = notificationBuilderMap.get(notificationCountListener.getId());
        if (notificationBuilder == null) {
            return;
        }
        Notification notification = notificationBuilder.setContentText(getString(R.string.product_notification_complete_upload_product))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.product_notification_complete_upload_product)))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(TAG, notificationCountListener.getId(), notification);
        removeNotificationFromList(notificationCountListener.getId());
        sendSuccessBroadcast(notificationCountListener.getProductViewModel());
    }

    @Override
    public void onFailedAddProduct(Throwable t, ProductSubmitNotificationListener productSubmitNotificationListener) {
        String errorMessage = ErrorHandler.getErrorMessage(getApplicationContext(), t);
        Notification notification = buildFailedNotification(errorMessage, productSubmitNotificationListener.getId(), productSubmitNotificationListener.getSubmitStatus());
        notificationManager.notify(TAG, productSubmitNotificationListener.getId(), notification);
        removeNotificationFromList(productSubmitNotificationListener.getId());
		if (!GlobalConfig.DEBUG) {
        		Crashlytics.logException(t);
        }
        UnifyTracking.eventAddProductErrorServer(errorMessage);
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
        bundle.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, errorMessage);
        result.putExtras(bundle);
        sendBroadcast(result);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(new Intent(ACTION_DRAFT_CHANGED));
    }

    private void removeNotificationFromList(int notificationId) {
        notificationBuilderMap.remove(notificationId);
        if (notificationBuilderMap.size() <= 0) {
            stopSelf();
        }
    }

    public void createNotification(int notificationId, String productName) {
        NotificationCompat.Builder builder = buildBaseNotification(productName);
        Notification notification = builder
                .setContentText(getString(R.string.product_notification_start_upload_product))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.product_notification_start_upload_product)))
                .build();
        notificationManager.notify(TAG, notificationId, notification);
        notificationBuilderMap.put(notificationId, builder);
    }

    public void updateNotification(int notificationId, int currentCount, int maxProgress) {
        Notification notification = notificationBuilderMap.get(notificationId)
                .setContentText(getString(R.string.product_notification_progress_upload_product))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.product_notification_progress_upload_product)))
                .setProgress(maxProgress, currentCount, false)
                .build();
        notificationManager.notify(TAG, notificationId, notification);
    }

    public void sendSuccessBroadcast(ProductViewModel productViewModel) {
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE);
        bundle.putString(TkpdState.ProductService.PRODUCT_NAME, productViewModel.getProductName());
        bundle.putString(TkpdState.ProductService.PRODUCT_URI, productViewModel.getProductUrl());
        bundle.putString(TkpdState.ProductService.PRODUCT_ID, productViewModel.getProductId() + "");
        result.putExtras(bundle);
        sendBroadcast(result);
    }

    private NotificationCompat.Builder buildBaseNotification(String productName) {
        String title = getString(R.string.product_title_notification_upload_product) + " " + productName;
//        Intent pendingIntent = new Intent(this, ProductManageActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
//        int largeIconRes = R.drawable.ic_stat_notify2;
//        if (!GlobalConfig.isSellerApp()) {
//            largeIconRes = R.drawable.ic_stat_notify;
//        }
//        return new NotificationCompat.Builder(this, NotificationChannelId.GENERAL)
//                .setContentTitle(title)
//                .setSmallIcon(R.drawable.ic_stat_notify_white)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIconRes))
//                .setContentIntent(pIntent)
//                .setGroup(getString(R.string.product_group_notification))
//                .setOnlyAlertOnce(true);
        return null;
    }

    private Notification buildFailedNotification(String errorMessage, int notificationId, @ProductStatus int productStatus) {
//        Intent pendingIntent = ProductDraftAddActivity.createInstance(this, notificationId);
//        if (productStatus == ProductStatus.EDIT) {
//            pendingIntent = ProductDraftEditActivity.createInstance(this, notificationId);
//        }
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (notificationBuilderMap.get(notificationId) == null) {
//            createNotification(notificationId, "");
//        }
//        return notificationBuilderMap.get(notificationId)
//                .setContentText(errorMessage)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(errorMessage))
//                .setContentIntent(pIntent)
//                .setProgress(0, 0, false)
//                .setOngoing(false)
//                .setAutoCancel(true)
//                .build();
        return null;
    }
}