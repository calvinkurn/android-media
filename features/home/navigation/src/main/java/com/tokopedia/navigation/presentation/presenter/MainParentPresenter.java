package com.tokopedia.navigation.presentation.presenter;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.data.GlobalNavConstant;
import com.tokopedia.navigation.domain.GetNotificationUseCase;
import com.tokopedia.navigation.domain.subscriber.NotificationSubscriber;
import com.tokopedia.navigation.presentation.view.MainParentView;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by meta on 25/07/18.
 */
public class MainParentPresenter {

    private MainParentView mainParentView;

    private final GetNotificationUseCase getNotificationUseCase;

    @Inject
    MainParentPresenter(GetNotificationUseCase getNotificationUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
    }

    public void setView(MainParentView mainParentView) {
        this.mainParentView = mainParentView;
    }

    public void getNotificationData() {
        this.mainParentView.onStartLoading();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GlobalNavConstant.QUERY,
                GraphqlHelper.loadRawString(this.mainParentView.getContext().getResources(), R.raw.query_notification));
        getNotificationUseCase.execute(requestParams, new NotificationSubscriber(this.mainParentView));
    }

    public void onResume() {
        this.getNotificationData();
    }

    public void onDestroy() {
        this.getNotificationUseCase.unsubscribe();
        this.mainParentView = null;
    }
}
