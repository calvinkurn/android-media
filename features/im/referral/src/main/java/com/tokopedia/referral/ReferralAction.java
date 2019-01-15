package com.tokopedia.referral;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.ActionInterfaces.ActionCreator;
import com.tokopedia.abstraction.ActionInterfaces.ActionDataProvider;
import com.tokopedia.abstraction.ActionInterfaces.ActionExecutor;
import com.tokopedia.abstraction.ActionInterfaces.ActionUIDelegate;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.referral.data.ReferralCodeEntity;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

public class ReferralAction<T,V,W,X,Y,A,B> implements ActionExecutor<T,V,W,X,Y>, ActionDataProvider<A,B> {

    @Override
    public void doAction(int actionId, T dataObj, ActionCreator<V,W> actionCreator, ActionUIDelegate<X,Y> actionUIDelegate) {
        switch (actionId){
            case Constants.Action.ACTION_GET_REFERRAL_CODE:
                GetReferralDataUseCase getReferralDataUseCase = new GetReferralDataUseCase();
                if(actionUIDelegate != null) actionUIDelegate.waitForResult(actionId, (X)"");
                getReferralDataUseCase.execute(Util.getPostRequestBody(new UserSession((Context) dataObj)), new Subscriber<Map<Type, RestResponse>>() {
                    @Override
                    public void onCompleted() {
                        if(actionUIDelegate != null) actionUIDelegate.stopWaiting(actionId, (Y)"");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(actionCreator != null) actionCreator.actionError(actionId, (W) new Integer(Constants.ErrorCode.REFERRAL_API_ERROR));
                    }

                    @Override
                    public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                        Type token = new TypeToken<DataResponse<ReferralCodeEntity>>() {
                        }.getType();
                        RestResponse res1 = typeRestResponseMap.get(token);
                        DataResponse ticketListResponse = res1.getData();
                        ReferralCodeEntity referralCodeEntity = (ReferralCodeEntity) ticketListResponse.getData();
                        if (referralCodeEntity.getErorMessage() == null) {
                            LocalCacheHandler localCacheHandler = new LocalCacheHandler((Context) dataObj, Constants.Values.Companion.REFERRAL);
                            localCacheHandler.putString(Constants.Key.Companion.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                            localCacheHandler.applyEditor();
                        }
                        if(actionUIDelegate != null) actionUIDelegate.stopWaiting(actionId, (Y)"");
                        if(actionCreator != null) actionCreator.actionSuccess(actionId, (V) referralCodeEntity.getPromoContent().getCode());
                    }
                });
                break;
        }
    }

    @Override
    public A getData(int actionId, B dataObject) {
        switch (actionId){
            case Constants.Action.ACTION_GET_REFERRAL_CODE_IF_EXIST:
                LocalCacheHandler localCacheHandler = new LocalCacheHandler((Context) dataObject, Constants.Values.Companion.REFERRAL);
                return (A)localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE);
        }
        return null;
    }
}
