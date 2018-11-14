package com.tokopedia.useridentification.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.useridentification.view.domain.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.view.domain.pojo.GetApprovalStatusPojo;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvinatin on 08/11/18.
 */

public class UserIdentificationInfoPresenter extends BaseDaggerPresenter<UserIdentificationInfo.View>
        implements UserIdentificationInfo.Presenter {

    private final GetApprovalStatusUseCase getApprovalStatusUseCase;
    private final Context context;

    @Inject
    public UserIdentificationInfoPresenter(@ApplicationContext Context context,
                                           GetApprovalStatusUseCase getApprovalStatusUseCase
    ) {
        this.getApprovalStatusUseCase = getApprovalStatusUseCase;
        this.context = context;
    }

    @Override
    public void detachView() {
        getApprovalStatusUseCase.unsubscribe();
    }

    @Override
    public void getStatus() {
//        getApprovalStatusUseCase.execute(GetApprovalStatusUseCase.getRequestParam(""), new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if (getView() != null) {
//                    getView().onErrorGetInfo(ErrorHandler.getErrorMessage(context, e));
//                }
//            }
//
//            @Override
//            public void onNext(GraphqlResponse graphqlResponse) {
//                if (getView() != null
//                        && graphqlResponse.getData(GetApprovalStatusPojo.class) != null) {
//
//                    GetApprovalStatusPojo pojo = graphqlResponse.getData(GetApprovalStatusPojo
//                            .class);
//                    getView().onSuccessGetInfo(0);
//                }
//            }
//        });

        getView().onSuccessGetInfo(0);
    }
}
