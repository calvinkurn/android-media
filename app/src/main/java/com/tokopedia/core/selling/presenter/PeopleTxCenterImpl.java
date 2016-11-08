package com.tokopedia.core.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;

/**
 * Created by Toped10 on 7/15/2016.
 */
public class PeopleTxCenterImpl extends PeopleTxCenter {

    private Bundle bundle;

    public PeopleTxCenterImpl(PeopleTxCenterView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(@NonNull Context context) {
        view.loadData();
        view.initView();
    }

    @Override
    public void fetchArguments(Bundle argument) {
        this.bundle = argument;
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        view.initHandlerAndAdapter();
        checkCondition();
        checkValidationToSendGoogleAnalytic(view.getVisibleUserHint(), context);
    }

    private void checkCondition() {
        if(bundle.getString("type").equals("people")){
            view.setCondition1();
        }else{
            view.setCondition2();
        }
    }

    @Override
    public void setLocalyticFlow(Activity context) {
        try {
            String screenName = context.getString(R.string.transaction_buy_page);

            switch (view.getState()) {
                case "people":
                    screenName = context.getString(R.string.transaction_buy_page);
                    break;
                case "shop":
                    screenName = context.getString(R.string.transaction_sell_page);
                    break;
            }

            sendToGTM(screenName, context);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }
    }

    private void sendToGTM(String screenName, Context context){

    }

    @Override
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {
        }
    }

}
