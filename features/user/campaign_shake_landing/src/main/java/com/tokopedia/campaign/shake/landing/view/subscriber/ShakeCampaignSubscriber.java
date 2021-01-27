package com.tokopedia.campaign.shake.landing.view.subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.campaign.shake.landing.R;
import com.tokopedia.campaign.shake.landing.analytics.CampaignTracking;
import com.tokopedia.campaign.shake.landing.data.entity.CampaignGqlResponse;
import com.tokopedia.campaign.shake.landing.data.entity.CampaignResponseEntity;
import com.tokopedia.campaign.shake.landing.data.entity.ValidCampaignPojo;
import com.tokopedia.campaign.shake.landing.data.model.CampaignException;
import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectContract;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.shakedetect.ShakeDetectManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

public class ShakeCampaignSubscriber extends Subscriber<GraphqlResponse> {

    private ShakeDetectContract.View view;
    private Context context;

    public ShakeCampaignSubscriber (ShakeDetectContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onCompleted() {
        view.finish();
    }

    @Override
    public void onError(Throwable e) {
        CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");

        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof CampaignException) {
            if (((CampaignException) e).isMissingAuthorizationCredentials()) {
                redirectToLoginPage();
            } else {
                view.showErrorGetInfo();
                return;
            }

        } else if (e instanceof ResponseDataNullException) {
            view.showErrorNetwork(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.showErrorNetwork(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            view.showErrorNetwork(ErrorHandler.getErrorMessage(context, e));
        } else {
            view.showErrorGetInfo();
            return;
        }

        view.finish();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse.getError(CampaignGqlResponse.class) != null
                && graphqlResponse.getError(CampaignGqlResponse.class).size() > 0) {
            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
            view.showMessage(graphqlResponse.getError(CampaignGqlResponse.class).get(0).getMessage() != null ?
                    graphqlResponse.getError(CampaignGqlResponse.class).get(0).getMessage() : "");
            return;
        }

        CampaignGqlResponse response =
                graphqlResponse.getData(CampaignGqlResponse.class);

        if (response != null && response.getCampaignResponseEntity() != null) {
            CampaignResponseEntity s = response.getCampaignResponseEntity();
            if (s.getValidCampaignPojos().size() > 0) {
                ValidCampaignPojo campaign = s.getValidCampaignPojos().get(0);

                if (!campaign.isValid()) {
                    CampaignTracking.eventShakeShake("shake shake disable", ShakeDetectManager.sTopActivity, "", "");
                    return;
                }

                if ((campaign.getErrorMessage()) != null
                        && !campaign.getErrorMessage().isEmpty()
                        && campaign.getUrl() != null && campaign.getUrl().isEmpty()) {
                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                    view.showMessage(campaign.getErrorMessage());
                    return;
                }

                Intent intentFromRouter = RouteManager.getIntentNoFallback(context, campaign.getUrl());
                if(intentFromRouter == null){
                    view.showErrorNetwork(context.getString(R.string.shake_landing_shake_shake_wrong_deeplink));
                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                    return;
                }

                // Vibrate for 500 milliseconds
                if (campaign.getVibrate() == 1) {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                }

                redirectToCampaignUrl(campaign.getUrl());

                CampaignTracking.eventShakeShake("success",
                        ShakeDetectManager.sTopActivity, "", campaign.getUrl());

                //Open next activity based upon the result from server

            }
        } else {
            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
            view.showErrorGetInfo();
        }
    }

    private void redirectToLoginPage() {
        final Intent intent = RouteManager.getIntent(context, ApplinkConst.LOGIN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }, 500);
    }

    private void redirectToCampaignUrl(String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        final Intent intent = RouteManager.getIntent(context, url);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }, 500);
    }
}
