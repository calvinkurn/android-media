package com.tokopedia.referral;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.Actions.interfaces.ActionExecutor;
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.referral.data.ReferralCodeEntity;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

public class ReferralAction<ACTION_DATA,RESULT_DATA,ERROR_DATA,WAIT_DATA,STOPWAIT_DATA,OUTPUT_DATA,INPUT_DATA>
        implements ActionExecutor<ACTION_DATA,RESULT_DATA, ERROR_DATA,WAIT_DATA, STOPWAIT_DATA>, ActionDataProvider<OUTPUT_DATA,INPUT_DATA> {

    @Override
    public void doAction(int actionId, ACTION_DATA dataObj, ActionCreator<RESULT_DATA,ERROR_DATA> actionCreator, ActionUIDelegate<WAIT_DATA,STOPWAIT_DATA> actionUIDelegate) {
        switch (actionId){
            case Constants.Action.ACTION_GET_REFERRAL_CODE:
                String referralCode = (String) getData(Constants.Action.ACTION_GET_REFERRAL_CODE_IF_EXIST, (INPUT_DATA) dataObj);
                if (!TextUtils.isEmpty(referralCode)) {
                    actionCreator.actionSuccess(Constants.Action.ACTION_GET_REFERRAL_CODE_IF_EXIST, (RESULT_DATA) referralCode);
                    return;
                }
                GetReferralDataUseCase getReferralDataUseCase = new GetReferralDataUseCase();
                if(actionUIDelegate != null) actionUIDelegate.waitForResult(actionId, (WAIT_DATA)"");
                if(dataObj != null) {
                    getReferralDataUseCase.execute(Util.getPostRequestBody(new UserSession((Context) dataObj)), new Subscriber<Map<Type, RestResponse>>() {
                        @Override
                        public void onCompleted() {
                            if (actionUIDelegate != null)
                                actionUIDelegate.stopWaiting(actionId, (STOPWAIT_DATA) "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (actionUIDelegate != null)
                                actionUIDelegate.stopWaiting(actionId, (STOPWAIT_DATA) "");
                            if (actionCreator != null)
                                actionCreator.actionError(actionId, (ERROR_DATA) new Integer(Constants.ErrorCode.REFERRAL_API_ERROR));
                        }

                        @Override
                        public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                            Type token = new TypeToken<DataResponse<ReferralCodeEntity>>() {
                            }.getType();
                            RestResponse res1 = typeRestResponseMap.get(token);
                            DataResponse ticketListResponse = res1.getData();
                            ReferralCodeEntity referralCodeEntity = (ReferralCodeEntity) ticketListResponse.getData();
                            if (referralCodeEntity.getErorMessage() == null && dataObj != null) {
                                LocalCacheHandler localCacheHandler = new LocalCacheHandler((Context) dataObj, Constants.Values.Companion.REFERRAL);
                                localCacheHandler.putString(Constants.Key.Companion.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                                localCacheHandler.applyEditor();
                            }
                            if (actionUIDelegate != null)
                                actionUIDelegate.stopWaiting(actionId, (STOPWAIT_DATA) "");
                            if (actionCreator != null)
                                actionCreator.actionSuccess(actionId, (RESULT_DATA) referralCodeEntity.getPromoContent().getCode());
                        }
                    });
                }
                else {
                    if(actionUIDelegate != null) actionUIDelegate.stopWaiting(actionId, (STOPWAIT_DATA) "");
                    if (actionCreator != null) actionCreator.actionError(actionId, (ERROR_DATA) new Integer(Constants.ErrorCode.REFERRAL_API_ERROR));
                }
                break;
        }
    }

    @Override
    public OUTPUT_DATA getData(int actionId, INPUT_DATA dataObject) {
        switch (actionId){
            case Constants.Action.ACTION_GET_REFERRAL_CODE_IF_EXIST:
                LocalCacheHandler localCacheHandler = new LocalCacheHandler((Context) dataObject, Constants.Values.Companion.REFERRAL);
                return (OUTPUT_DATA)localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE);
        }
        return null;
    }
}
