package com.tokopedia.kyc_centralized.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationInfo;
import com.tokopedia.kyc_centralized.view.subscriber.GetUserProjectInfoSubcriber;
import com.tokopedia.kyc_centralized.view.usecase.GetUserProjectInfoUseCase;
import com.tokopedia.user_identification_common.domain.usecase.GetApprovalStatusUseCase;

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
    public void getInfo(int projectId) {
        getUserProjectInfoUseCase.execute(GetUserProjectInfoUseCase.getRequestParam(projectId),
                new GetUserProjectInfoSubcriber(getView().getUserProjectInfoListener()));
    }
}
