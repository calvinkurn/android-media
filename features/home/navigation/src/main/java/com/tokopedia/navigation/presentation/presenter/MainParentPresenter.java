package com.tokopedia.navigation.presentation.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.subscriber.NotificationSubscriber;
import com.tokopedia.navigation.presentation.view.MainParentView;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by meta on 25/07/18.
 */
public class MainParentPresenter {
    public static final String KEY_FEED = "KEY_FEED";
    public static final String KEY_FEED_FIRSTPAGE_LAST_CURSOR = "KEY_FEED_FIRSTPAGE_LAST_CURSOR";
    public static final String PARAM_FEED_LAST_CURSOR = "cursor";

    private MainParentView mainParentView;

    private final GetDrawerNotificationUseCase getNotificationUseCase;
    private UserSession userSession;

    private boolean isReccuringApplink = false;

    public MainParentPresenter(GetDrawerNotificationUseCase getNotificationUseCase, UserSession userSession) {
        this.getNotificationUseCase = getNotificationUseCase;
        this.userSession = userSession;
    }

    public void setView(MainParentView mainParentView) {
        this.mainParentView = mainParentView;
    }

    public void getNotificationData() {
        if(userSession.isLoggedIn()) {
            this.mainParentView.onStartLoading();
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(GlobalNavConstant.QUERY,
                    GraphqlHelper.loadRawString(this.mainParentView.getContext().getResources(), R.raw.query_notification));
            requestParams.putAll(buildQueryParam().getParameters());
            getNotificationUseCase.execute(requestParams, new NotificationSubscriber(this.mainParentView));
        }
    }

    public RequestParams buildQueryParam() {
        RequestParams requestParams = RequestParams.create();
        LocalCacheHandler cache = new LocalCacheHandler(mainParentView.getContext().getApplicationContext(), KEY_FEED);
        requestParams.putString(PARAM_FEED_LAST_CURSOR,
                cache.getString(KEY_FEED_FIRSTPAGE_LAST_CURSOR , ""));
        return requestParams;
    }

    public void onResume() {
        this.getNotificationData();
    }

    public void onDestroy() {
        this.getNotificationUseCase.unsubscribe();
        this.mainParentView = null;
    }

    public boolean isFirstTimeUser() {
        return mainParentView.isFirstTimeUser();
    }

    public Boolean isRecurringApplink() {
        return isReccuringApplink;
    }

    public void setIsRecurringApplink(Boolean isReccuringApplink) {
        this.isReccuringApplink = isReccuringApplink;
    }

    public boolean isUserLogin(){
        return mainParentView.isUserLogin();
    }
}
