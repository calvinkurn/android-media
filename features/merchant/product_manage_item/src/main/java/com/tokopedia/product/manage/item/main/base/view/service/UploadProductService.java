package com.tokopedia.product.manage.item.main.base.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.product.manage.item.BuildConfig;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.common.util.AddProductException;
import com.tokopedia.product.manage.item.common.util.ProductStatus;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.product.manage.item.main.base.di.module.AddProductserviceModule;
import com.tokopedia.product.manage.item.main.base.view.listener.ProductSubmitNotificationListener;
import com.tokopedia.product.manage.item.main.base.view.presenter.AddProductServiceListener;
import com.tokopedia.product.manage.item.main.base.view.presenter.AddProductServicePresenter;
import com.tokopedia.product.manage.item.main.draft.view.activity.ProductDraftAddActivity;
import com.tokopedia.product.manage.item.main.draft.view.activity.ProductDraftEditActivity;
import com.tokopedia.product.manage.item.utils.ErrorHandlerAddProduct;
import com.tokopedia.product.manage.item.utils.ProductEditItemComponentInstance;
import com.tokopedia.product.manage.item.utils.constant.AddProductTrackingConstant;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import timber.log.Timber;

import static com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.PRODUCT_VIEW_MODEL;

public class UploadProductService extends BaseService implements AddProductServiceListener {
    public static final String TAG = "upload_product";

    public static final String ACTION_DRAFT_CHANGED = "com.tokopedia.draft.changed";
    private static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private static final String IS_ADD = "IS_ADD";
    private static final String IS_UPLOAD_PRODUCT_FROM_DRAFT = "IS_UPLOAD_PRODUCT_FROM_DRAFT";
    private static final String CACHE_MANAGER_ID = "CACHE_MANAGER_ID";
    private ProductViewModel productViewModel = null;
    private SaveInstanceCacheManager cacheManager = null;
    private boolean isUploadProductFromDraft = true;
    @Inject
    AddProductServicePresenter presenter;

    @Inject
    UserSessionInterface userSession;

    private NotificationManager notificationManager;
    private HashMap<Integer, NotificationCompat.Builder> notificationBuilderMap = new HashMap<>();

    public static Intent getIntent(Context context, long draftProductId, boolean isAdd) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(DRAFT_PRODUCT_ID, draftProductId);
        intent.putExtra(IS_ADD, isAdd);
        return intent;
    }

    public static Intent getIntentUploadProductWithoutSaveToDraft(Context context, boolean isAdd, String cacheManagerId) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(IS_ADD, isAdd);
        intent.putExtra(IS_UPLOAD_PRODUCT_FROM_DRAFT, false);
        intent.putExtra(CACHE_MANAGER_ID, cacheManagerId);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        DaggerAddProductServiceComponent
                .builder()
                .productComponent(ProductEditItemComponentInstance.getComponent(getApplication()))
                .addProductserviceModule(new AddProductserviceModule())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isAdd = intent.getBooleanExtra(IS_ADD, true);
        isUploadProductFromDraft = intent.getBooleanExtra(IS_UPLOAD_PRODUCT_FROM_DRAFT, true);
        if (isUploadProductFromDraft) {
            final long draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, Long.MIN_VALUE);
            presenter.uploadProduct(draftProductId, getProductSubmitNotificationListener((int) draftProductId, isAdd));
        } else {
            String cacheId = intent.getStringExtra(CACHE_MANAGER_ID);
            if (null != cacheId) {
                cacheManager = new SaveInstanceCacheManager(this, cacheId);
                int notificationId = new Random().nextInt();
                productViewModel = cacheManager.get(PRODUCT_VIEW_MODEL, ProductViewModel.class);
                presenter.uploadProduct(productViewModel, getProductSubmitNotificationListener(notificationId, isAdd));
            }
        }
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
        if (!isUploadProductFromDraft)
            cacheManager.put(PRODUCT_VIEW_MODEL, productViewModel);
        String errorMessage = ErrorHandlerAddProduct.getErrorMessage(getApplicationContext(), t);
        Notification notification = buildFailedNotification(errorMessage, productSubmitNotificationListener.getId(), productSubmitNotificationListener.getSubmitStatus());
        notificationManager.notify(TAG, productSubmitNotificationListener.getId(), notification);
        removeNotificationFromList(productSubmitNotificationListener.getId());
        eventAddProductErrorServer(errorMessage);
        eventServerValidationAddProduct(userSession.getShopId(), errorMessage);
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
        bundle.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, errorMessage);
        result.putExtras(bundle);
        sendBroadcast(result);

        logException(t);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(new Intent(ACTION_DRAFT_CHANGED));
    }

    public void eventAddProductErrorServer(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR_SERVER,
                label);
    }

    private void eventServerValidationAddProduct(String shopId, String errorText) {
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                AddProductTrackingConstant.Event.CLICK_ADD_PRODUCT,
                AddProductTrackingConstant.Category.ADD_PRODUCT_PAGE,
                AddProductTrackingConstant.Action.CLICK_ADD_ERROR_SERVER_VALIDATION,
                errorText
        );
        mapEvent.put(AddProductTrackingConstant.Key.SHOP_ID, shopId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapEvent);
    }

    private void logException(Throwable t) {
        try {
            if (!BuildConfig.DEBUG) {
                String errorMessage = String.format(
                        "\"Error upload product.\",\"userId: %s\",\"userEmail: %s \",\"errorMessage: %s\"",
                        userSession.getUserId(),
                        userSession.getEmail(),
                        getExceptionMessage(t));
                AddProductException exception = new AddProductException(errorMessage, t);
                Crashlytics.logException(exception);

                Timber.w("P2#PRODUCT_UPLOAD#%s", errorMessage);
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private String getExceptionMessage(Throwable t) {
        if (t instanceof ResponseV4ErrorException
                && ((ResponseV4ErrorException) t).getErrorList().size() > 0) {
            return ((ResponseV4ErrorException) t).getErrorList().get(0);
        } else {
            return t.getLocalizedMessage();
        }
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
        int largeIconRes = R.drawable.ic_stat_notify2;
        if (!GlobalConfig.isSellerApp()) {
            largeIconRes = R.drawable.ic_stat_notify;
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationChannelId.GENERAL)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIconRes))
                .setGroup(getString(R.string.product_group_notification))
                .setOnlyAlertOnce(true);

        Intent pendingIntent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST);
        if (pendingIntent != null) {
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
            notificationBuilder.setContentIntent(pIntent);
        }
        return notificationBuilder;
    }

    private Notification buildFailedNotification(String errorMessage, int notificationId, @ProductStatus int productStatus) {
        Intent pendingIntent;
        if(isUploadProductFromDraft){
            pendingIntent = ProductDraftAddActivity.Companion.createInstance(this, notificationId);
            if (productStatus == ProductStatus.EDIT) {
                pendingIntent = ProductDraftEditActivity.Companion.createInstance(this, notificationId);
            }
        }else{
            String cacheId = cacheManager.getId() != null ? cacheManager.getId() : "";
            pendingIntent = ProductDraftAddActivity.Companion.createInstance(this, cacheId);
            if (productStatus == ProductStatus.EDIT) {
                pendingIntent = ProductDraftEditActivity.Companion.createInstance(this, cacheId);
            }
        }

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (notificationBuilderMap.get(notificationId) == null) {
            createNotification(notificationId, "");
        }
        return notificationBuilderMap.get(notificationId)
                .setContentText(errorMessage)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(errorMessage))
                .setContentIntent(pIntent)
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();
    }

    private ProductSubmitNotificationListener getProductSubmitNotificationListener(int notificationId, boolean isAdd) {
        return new ProductSubmitNotificationListener(
                notificationId, isAdd ? ProductStatus.ADD : ProductStatus.EDIT) {
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
        };
    }
}