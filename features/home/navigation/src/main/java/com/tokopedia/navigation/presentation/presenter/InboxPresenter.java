package com.tokopedia.navigation.presentation.presenter;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.data.GlobalNavConstant;
import com.tokopedia.navigation.domain.GetNotificationUseCase;
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by meta on 25/07/18.
 */
public class InboxPresenter {

    private InboxView inboxView;

    private final GetNotificationUseCase getNotificationUseCase;

    @Inject InboxPresenter(GetNotificationUseCase getNotificationUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
    }

    public void setView(InboxView inboxView) {
        this.inboxView = inboxView;
    }

    public void getInboxData() {
        this.inboxView.onStartLoading();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GlobalNavConstant.QUERY,
                GraphqlHelper.loadRawString(this.inboxView.getContext().getResources(), R.raw.query_notification));
        getNotificationUseCase.execute(requestParams, new InboxSubscriber(this.inboxView));
    }

    public void onResume() { }

    public void onDestroy() {
        this.getNotificationUseCase.unsubscribe();
        this.inboxView = null;
    }
}
