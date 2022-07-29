package com.tokopedia.promogamification.common.floating.view.presenter;

import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.promogamification.common.R;
import com.tokopedia.promogamification.common.floating.data.entity.FloatingButtonResponseEntity;
import com.tokopedia.promogamification.common.floating.data.entity.GamiFloatingCloseClickResponse;
import com.tokopedia.promogamification.common.floating.view.contract.FloatingEggContract;
import com.tokopedia.promotion.common.idling.TkpdIdlingResource;
import com.tokopedia.promotion.common.idling.TkpdIdlingResourceProvider;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;

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
        getTokenTokopointsUseCase.execute(getFloatingEggSubscriber());
    }


    public Subscriber<GraphqlResponse> getFloatingEggSubscriber(){
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

    public Subscriber<GraphqlResponse> getClickCloseSubscriber(){
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewNotAttached())
                    return;
                getView().onErrorClickClose(e);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewNotAttached())
                    return;
                GamiFloatingCloseClickResponse closeClickResponse = graphqlResponse.getData(GamiFloatingCloseClickResponse.class);
                if(closeClickResponse != null && closeClickResponse.getGamiFloatingClick() != null){
                    getView().onSuccessClickClose(closeClickResponse.getGamiFloatingClick());
                }

            }
        };
    }

    @Override
    public boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public void clickCloseButton(int floatingId) {
        getTokenTokopointsUseCase.clearRequest();
        if(getView() != null) {
            GraphqlRequest closeClickRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.core_gami_floating_close_click_query),
                    GamiFloatingCloseClickResponse.class, false);
            HashMap map = new HashMap();
            map.put("floatingID", floatingId);
            closeClickRequest.setVariables(map);
            getTokenTokopointsUseCase.addRequest(closeClickRequest);
            getTokenTokopointsUseCase.execute(getClickCloseSubscriber());
        }
    }

    @Override
    public void detachView() {
        getTokenTokopointsUseCase.unsubscribe();
        super.detachView();
    }
}