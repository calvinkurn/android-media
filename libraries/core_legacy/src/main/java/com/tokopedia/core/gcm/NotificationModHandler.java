package com.tokopedia.core.gcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;

import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationByCategoryAndServerIdUseCase;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationByCategoryUseCase;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationUseCase;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

public class NotificationModHandler {
    public static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    public static final String PACKAGE_CONSUMER_APP = "com.tokopedia.tkpd";

    private Context mContext;
    private NotificationManager mNotificationManager;

    public NotificationModHandler(Context context, int code) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mContext = context;
    }

    public NotificationModHandler(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mContext = context;
    }

    public static void clearCacheIfFromNotification(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                new LocalCacheHandler(context, TkpdCache.GCM_NOTIFICATION).clearCache(context, TkpdCache.GCM_NOTIFICATION);
            }
        }
    }

    public static void clearCacheAllNotification(Context activity) {
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(activity);
        DeleteSavedPushNotificationUseCase deleteUseCase =
                new DeleteSavedPushNotificationUseCase(
                        pushNotificationRepository
                );
        deleteUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean)
                    CommonUtils.dumper("Success Clear Storage Notification");
                else
                    CommonUtils.dumper("Failed Clear Storage Notification");
            }
        });
    }

    public void dismissAllActivedNotifications() {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void clearCacheIfFromNotification(Context context, String category) {
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(context);
        DeleteSavedPushNotificationByCategoryUseCase deleteUseCase =
                new DeleteSavedPushNotificationByCategoryUseCase(
                        pushNotificationRepository
                );
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DeleteSavedPushNotificationByCategoryUseCase.PARAM_CATEGORY, category);
        deleteUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    CommonUtils.dumper("Success Clear Storage Notification");
                } else {
                    CommonUtils.dumper("Failed Clear Storage Notification");
                }
            }
        });
    }

    public static void clearCacheIfFromNotification(Context context, String category, String serverId) {
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(context);
        DeleteSavedPushNotificationByCategoryAndServerIdUseCase deleteUseCase =
                new DeleteSavedPushNotificationByCategoryAndServerIdUseCase(
                        pushNotificationRepository
                );
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DeleteSavedPushNotificationByCategoryAndServerIdUseCase.PARAM_CATEGORY, category);
        requestParams.putString(DeleteSavedPushNotificationByCategoryAndServerIdUseCase.PARAM_SERVER_ID, serverId);
        deleteUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    CommonUtils.dumper("Success Clear Storage Notification");
                } else {
                    CommonUtils.dumper("Failed Clear Storage Notification");
                }
            }
        });

        int notificationId = -1;
        switch (category) {
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                notificationId = Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_ID;
                break;
            case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                notificationId = Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION_ID;
                break;
        }

        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean isActive = false;
            StatusBarNotification[] statusBarNotification = notificationManager.getActiveNotifications();
            for (StatusBarNotification aStatusBarNotification : statusBarNotification) {
                if (aStatusBarNotification.getId() == notificationId) {
                    isActive = true;
                    break;
                }
            }
            if (isActive) {
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BC_RESET_APPLINK);
                intent.putExtra(Constants.EXTRA_APPLINK_CATEGORY, category);
                intent.putExtra(Constants.EXTRA_APPLINK_RESET, true);
                LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
            }
        } else {
            notificationManager.cancel(notificationId);
        }
    }

    public void cancelNotif() {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(100);
        LocalCacheHandler.clearCache(mContext, TkpdCache.GCM_NOTIFICATION);
    }

    public static void cancelNotif(Context context, int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    public static void showDialogNotificationIfNotShowing(final Activity context, Intent intent) {
        if (!FCMCacheManager.isDialogNotificationSettingShowed(context) && RouterUtils.getRouterFromContext(context).legacySessionHandler().isV4Login()) {
            if (GlobalConfig.isSellerApp()) {
                if (appInstalledOrNot(PACKAGE_CONSUMER_APP, context)) {
                    new AlertDialog.Builder(context)
                            .setTitle(null)
                            .setMessage(context.getString(R.string.notification_dialog_content_seller_app))
                            .setPositiveButton(context.getString(R.string.notification_dialog_positive_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(context.getString(R.string.notification_dialog_negative_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            } else {
                if (appInstalledOrNot(PACKAGE_SELLER_APP, context)) {
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.notification_dialog_content_main_app))
                            .setPositiveButton(context.getString(R.string.notification_dialog_positive_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(context.getString(R.string.notification_dialog_negative_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    FCMCacheManager.setDialogNotificationSetting(context);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }
    }

    private static boolean appInstalledOrNot(String uri, Activity context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getApplicationInfo(uri, PackageManager.GET_META_DATA);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
            e.printStackTrace();
        }
        return app_installed;
    }
}
