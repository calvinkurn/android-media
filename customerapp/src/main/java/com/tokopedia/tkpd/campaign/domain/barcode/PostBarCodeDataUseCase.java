package com.tokopedia.tkpd.campaign.domain.barcode;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public class PostBarCodeDataUseCase extends UseCase<CampaignResponseEntity> {

    private CampaignDataRepository campaignDataRepository;

    public static final String CAMPAIGN_ID ="tkp_campaign_id";
    public static final String CAMPAIGN_NAME = "tkp_campaign_name";

    public PostBarCodeDataUseCase(CampaignDataRepository campaignDataRepository) {
        this.campaignDataRepository = campaignDataRepository;
    }

    @Override
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {
        return campaignDataRepository.getCompaignData(requestParams.getParameters());
    }
}
