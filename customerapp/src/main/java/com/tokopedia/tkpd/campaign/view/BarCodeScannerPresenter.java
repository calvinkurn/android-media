package com.tokopedia.tkpd.campaign.view;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;
import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_TYPE;


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
        requestParams.putString(CAMPAIGN_TYPE, "qr");
        requestParams.putString(CAMPAIGN_ID, getView().getBarCodeData());
        postBarCodeDataUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                getView().finish();
            }

            @Override
            public void onNext(CampaignResponseEntity s) {
                getView().finish();
                //Open next activity based upon the result from server
            }
        });

    }
}
