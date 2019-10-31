package com.tokopedia.useridentification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.user_identification_common.domain.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.domain.usecase.GetUserProjectInfoUseCase;
import com.tokopedia.useridentification.subscriber.GetApprovalStatusSubscriber;
import com.tokopedia.useridentification.subscriber.GetUserProjectInfoSubcriber;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;

import javax.inject.Inject;

/**
 * @author by alvinatin on 08/11/18.
 */

public class UserIdentificationInfoPresenter extends BaseDaggerPresenter<UserIdentificationInfo.View>
        implements UserIdentificationInfo.Presenter {

    private final GetApprovalStatusUseCase getApprovalStatusUseCase;
    private final GetUserProjectInfoUseCase getUserProjectInfoUseCase;

    @Inject
    public UserIdentificationInfoPresenter(GetUserProjectInfoUseCase getUserProjectInfoUseCase, GetApprovalStatusUseCase getApprovalStatusUseCase) {
        this.getUserProjectInfoUseCase = getUserProjectInfoUseCase;
        this.getApprovalStatusUseCase = getApprovalStatusUseCase;
    }

    @Override
    public void detachView() {
        getUserProjectInfoUseCase.unsubscribe();
        getApprovalStatusUseCase.unsubscribe();
    }

    @Override
    public void getInfo() {
        getUserProjectInfoUseCase.execute(GetUserProjectInfoUseCase.getRequestParam(),
                new GetUserProjectInfoSubcriber(getView().getUserProjectInfoListener()));
    }

    @Override
    public void getStatus() {
        getApprovalStatusUseCase.execute(GetApprovalStatusUseCase.getRequestParam(),
                new GetApprovalStatusSubscriber(getView().getApprovalStatusListener()));
    }
}
