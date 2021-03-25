package com.tokopedia.promogamification.common.floating.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.promogamification.common.R;
import com.tokopedia.promogamification.common.floating.data.entity.FloatingButtonResponseEntity;
import com.tokopedia.promogamification.common.floating.view.contract.FloatingEggContract;
import com.tokopedia.promotion.common.idling.TkpdIdlingResource;
import com.tokopedia.promotion.common.idling.TkpdIdlingResourceProvider;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 02/04/18.
 */

public class FloatingEggPresenter extends BaseDaggerPresenter<FloatingEggContract.View>
        implements FloatingEggContract.Presenter {

    private GraphqlUseCase getTokenTokopointsUseCase;
    private UserSessionInterface userSession;
    private final TkpdIdlingResource idlingResource = TkpdIdlingResourceProvider.INSTANCE.provideIdlingResource("FloatingEgg");

    @Inject
    public FloatingEggPresenter(GraphqlUseCase getTokenTokopointsUseCase,
                                UserSessionInterface userSession) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getGetTokenTokopoints() {
        if(idlingResource!=null) {
            idlingResource.increment();
        }
        getTokenTokopointsUseCase.clearRequest();
        GraphqlRequest tokenTokopointsRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.core_gami_floating_query),
                FloatingButtonResponseEntity.class, false);
        getTokenTokopointsUseCase.addRequest(tokenTokopointsRequest);
        getTokenTokopointsUseCase.execute(getSubscriber());
    }

    public Subscriber<GraphqlResponse> getSubscriber(){
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewNotAttached())
                    return;
                getView().onErrorGetToken(e);
                if(idlingResource!=null) {
                    idlingResource.decrement();
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewNotAttached())
                    return;
                FloatingButtonResponseEntity responseTokenTokopointEntity = graphqlResponse.getData(FloatingButtonResponseEntity.class);
                if (responseTokenTokopointEntity != null && responseTokenTokopointEntity.getGamiFloatingButtonEntity() != null)
                    getView().onSuccessGetToken(responseTokenTokopointEntity.getGamiFloatingButtonEntity());

                if(idlingResource!=null) {
                    idlingResource.decrement();
                }
            }
        };
    }

    @Override
    public boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public void detachView() {
        getTokenTokopointsUseCase.unsubscribe();
        super.detachView();
    }
}