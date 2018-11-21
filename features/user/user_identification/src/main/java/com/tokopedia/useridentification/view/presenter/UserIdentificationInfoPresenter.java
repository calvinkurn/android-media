package com.tokopedia.useridentification.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;
import com.tokopedia.user_identification_common.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;

import javax.inject.Inject;

/**
 * @author by alvinatin on 08/11/18.
 */

public class UserIdentificationInfoPresenter extends BaseDaggerPresenter<UserIdentificationInfo.View>
        implements UserIdentificationInfo.Presenter {

    private final GetApprovalStatusUseCase getApprovalStatusUseCase;
    private final Context context;

    @Inject
    public UserIdentificationInfoPresenter(@ApplicationContext Context context,
                                           GetApprovalStatusUseCase getApprovalStatusUseCase) {
        this.getApprovalStatusUseCase = getApprovalStatusUseCase;
        this.context = context;
    }

    @Override
    public void detachView() {
        getApprovalStatusUseCase.unsubscribe();
    }

    @Override
    public void getStatus() {
        getApprovalStatusUseCase.execute(GetApprovalStatusUseCase.getRequestParam(),
                new GetApprovalStatusSubscriber(context, getView().getApprovalStatusListener()));
    }
}
