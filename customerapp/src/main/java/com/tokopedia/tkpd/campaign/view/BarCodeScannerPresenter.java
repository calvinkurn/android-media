package com.tokopedia.tkpd.campaign.view;


import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;
import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_NAME;


/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class BarCodeScannerPresenter extends BaseDaggerPresenter<BarCodeScannerContract.View> implements BarCodeScannerContract.Presenter {


    PostBarCodeDataUseCase postBarCodeDataUseCase;

    @Inject
    public BarCodeScannerPresenter(PostBarCodeDataUseCase postBarCodeDataUseCase) {
        this.postBarCodeDataUseCase = postBarCodeDataUseCase;
    }

    @Override
    public void onBarCodeScanComplete() {
        RequestParams requestParams = RequestParams.create();
        Gson gson = new Gson();
        CampaignResponseEntity campaignResponseEntity = gson.fromJson(getView().getBarCodeData(),CampaignResponseEntity.class);
        requestParams.putInt(CAMPAIGN_ID, campaignResponseEntity.getCampaignId());
        requestParams.putString(CAMPAIGN_NAME,campaignResponseEntity.getCampaignName());
        postBarCodeDataUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                Log.e("toko_barcode","onCompleted ");
                getView().finish();
                 }

            @Override
            public void onError(Throwable e) {
                Log.e("toko_barcode","onError ");
                getView().finish();
            }

            @Override
            public void onNext(CampaignResponseEntity s) {
                Log.e("toko_barcode","onNext ");
                getView().finish();
                //Open next activity based upon the result from server
            }
        });

    }
}
