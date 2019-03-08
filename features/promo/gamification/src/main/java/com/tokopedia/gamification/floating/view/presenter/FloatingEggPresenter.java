package com.tokopedia.gamification.floating.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;
import com.tokopedia.gamification.floating.view.contract.FloatingEggContract;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 02/04/18.
 */

public class FloatingEggPresenter extends BaseDaggerPresenter<FloatingEggContract.View>
        implements FloatingEggContract.Presenter {

    private GraphqlUseCase getTokenTokopointsUseCase;
    private UserSession userSession;

    @Inject
    public FloatingEggPresenter(GraphqlUseCase getTokenTokopointsUseCase,
                                UserSession userSession) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getGetTokenTokopoints() {
        getTokenTokopointsUseCase.clearRequest();
        GraphqlRequest tokenTokopointsRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.token_tokopoint_query),
                ResponseTokenTokopointEntity.class);
        getTokenTokopointsUseCase.addRequest(tokenTokopointsRequest);
        getTokenTokopointsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetToken(e);

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ResponseTokenTokopointEntity responseTokenTokopointEntity = graphqlResponse.getData(ResponseTokenTokopointEntity.class);
                if (responseTokenTokopointEntity != null && responseTokenTokopointEntity.getTokopointsToken() != null)
                    getView().onSuccessGetToken(responseTokenTokopointEntity.getTokopointsToken());

            }
        });
    }

    @Override
    public boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public void detachView() {
        super.detachView();
        getTokenTokopointsUseCase.unsubscribe();
    }
}