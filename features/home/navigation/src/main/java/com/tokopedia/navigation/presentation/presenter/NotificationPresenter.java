package com.tokopedia.navigation.presentation.presenter;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.subscriber.DrawerNotificationSubscriber;
import com.tokopedia.navigation.presentation.view.NotificationView;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by meta on 26/07/18.
 */
public class NotificationPresenter {

    private NotificationView notificationView;

    private GetDrawerNotificationUseCase getDrawerNotificationUseCase;

    @Inject NotificationPresenter(GetDrawerNotificationUseCase getDrawerNotificationUseCase) {
        this.getDrawerNotificationUseCase = getDrawerNotificationUseCase;
    }

    public void setView(NotificationView notificationView) {
        this.notificationView = notificationView;
    }

    public void getDrawerNotification() {
        if (this.notificationView == null)
            return;

        this.notificationView.onStartLoading();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GlobalNavConstant.QUERY,
                GraphqlHelper.loadRawString(this.notificationView.getContext().getResources(), R.raw.query_drawer_notification));
        getDrawerNotificationUseCase.execute(requestParams, new DrawerNotificationSubscriber(this.notificationView));
    }

    public void onResume() {
        getDrawerNotification();
    }

    public void onDestroy() {
        this.notificationView = null;
        getDrawerNotificationUseCase.unsubscribe();
    }
}
