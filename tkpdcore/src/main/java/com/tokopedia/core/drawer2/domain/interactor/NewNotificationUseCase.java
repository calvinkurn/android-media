package com.tokopedia.core.drawer2.domain.interactor;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.InfoPenjualNotification;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * @author by nisie on 11/23/17.
 */

public class NewNotificationUseCase extends UseCase<NotificationModel> {

    NotificationUseCase notificationUseCase;
    GetChatNotificationUseCase getChatNotificationUseCase;
    DeleteNotificationCacheUseCase deleteNotificationCacheUseCase;
    GetInfoPenjualNotificationUseCase getInfoPenjualNotificationUseCase;
    private LocalCacheHandler drawerCache;

    private boolean isRefresh = false;

    public NewNotificationUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  NotificationUseCase notificationUseCase,
                                  DeleteNotificationCacheUseCase deleteNotificationCacheUseCase,
                                  GetChatNotificationUseCase getChatNotificationUseCase,
                                  GetInfoPenjualNotificationUseCase getInfoPenjualNotificationUseCase,
                                  LocalCacheHandler drawerCache) {
        super(threadExecutor, postExecutionThread);
        this.notificationUseCase = notificationUseCase;
        this.getChatNotificationUseCase = getChatNotificationUseCase;
        this.deleteNotificationCacheUseCase = deleteNotificationCacheUseCase;
        this.getInfoPenjualNotificationUseCase = getInfoPenjualNotificationUseCase;
        this.drawerCache = drawerCache;
    }

    @Override
    public Observable<NotificationModel> createObservable(RequestParams requestParams) {
        if (isRefresh) {
            getChatNotificationUseCase.setRefresh(true);
            getInfoPenjualNotificationUseCase.setRefresh(true);
            return deleteNotificationCacheUseCase.createObservable()
                    .flatMap(result -> getNotification(requestParams));
        } else {
            getChatNotificationUseCase.setRefresh(false);
            getInfoPenjualNotificationUseCase.setRefresh(false);
            return getNotification(requestParams);
        }
    }

    private Observable<NotificationModel> getNotification(RequestParams requestParams) {
        Observable<NotificationModel> notif = notificationUseCase.createObservable(requestParams);
        Observable<TopChatNotificationModel> notifTopChat = getChatNotificationUseCase.createObservable(com.tokopedia.usecase.RequestParams.EMPTY);
        Observable<InfoPenjualNotification> infoPenjualNotification = getInfoPenjualNotificationUseCase.createObservable(
                GetInfoPenjualNotificationUseCase.createParams(2)
        );

        return Observable.zip(notif, notifTopChat, infoPenjualNotification, (notificationModel, chatNotificationModel, infoPenjualNotif) -> {
            NotificationData data = notificationModel.getNotificationData();
            data.setTotalNotif(data.getTotalNotif() - data.getInbox().getInboxMessage() +
                    chatNotificationModel.getNotifUnreadsSeller() + infoPenjualNotif.getNotifUnreadInt().intValue());
            data.getInbox().setInboxMessage(chatNotificationModel.getNotifUnreadsSeller());
            notificationModel.setNotificationData(data);
            int notifUnreadsSeller = chatNotificationModel.getNotifUnreadsSeller();
            int notifInfoPenjual = infoPenjualNotif.getNotifUnreadInt().intValue();
            drawerCache.putInt(DrawerNotification.CACHE_INBOX_MESSAGE, notifUnreadsSeller);
            drawerCache.putInt(DrawerNotification.CACHE_INBOX_SELLER_INFO, notifInfoPenjual);
            drawerCache.putInt(DrawerNotification.CACHE_TOTAL_NOTIF, data.getTotalNotif());
            drawerCache.applyEditor();
            return notificationModel;
        });
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
