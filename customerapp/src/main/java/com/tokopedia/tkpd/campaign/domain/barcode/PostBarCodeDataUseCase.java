package com.tokopedia.tkpd.campaign.domain.barcode;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public class PostBarCodeDataUseCase extends UseCase<CampaignResponseEntity> {

    CampaignDataRepository campaignDataRepository;

    public static final String CAMPAIGN_ID ="tkp_campaign_id";
    public static final String CAMPAIGN_NAME = "tkp_campaign_name";


    public PostBarCodeDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CampaignDataRepository campaignDataRepository) {
        super(threadExecutor,postExecutionThread);
        this.campaignDataRepository = campaignDataRepository;
    }

    @Override
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {
        return campaignDataRepository.getCompaignData(requestParams.getParameters());
    }
}
