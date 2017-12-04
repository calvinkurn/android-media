package com.tokopedia.core.drawer2.data.source;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationSource {

    private final LocalCacheHandler drawerCache;
    private final ChatService chatService;
    private final TopChatNotificationMapper topChatNotificationMapper;

    public TopChatNotificationSource(ChatService chatService,
                                     TopChatNotificationMapper topChatNotificationMapper,
                                     LocalCacheHandler drawerCache) {
        this.chatService = chatService;
        this.topChatNotificationMapper = topChatNotificationMapper;
        this.drawerCache = drawerCache;
    }

    public Observable<TopChatNotificationModel> getNotificationTopChat(TKPDMapParam<String, Object>
                                                                               params) {
        return chatService.getApi()
                .getNotification(params)
                .map(topChatNotificationMapper)
                .doOnNext(saveToCache());
    }

    private Action1<TopChatNotificationModel> saveToCache() {
        return new Action1<TopChatNotificationModel>() {
            @Override
            public void call(TopChatNotificationModel topChatNotificationModel) {
                drawerCache.putInt(DrawerNotification.CACHE_INBOX_MESSAGE,
                        topChatNotificationModel.getNotifUnreads());
                drawerCache.putInt(DrawerNotification.CACHE_TOTAL_NOTIF, drawerCache.getInt
                        (DrawerNotification.CACHE_TOTAL_NOTIF) + topChatNotificationModel.getNotifUnreads());
                drawerCache.applyEditor();
            }
        };
    }
}
