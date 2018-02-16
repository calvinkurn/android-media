package com.tokopedia.tkpd.campaign.source;


import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class CampaignDataFactory {
    public CampaignAPI mAPI;

    @Inject
    public CampaignDataFactory(CampaignAPI mAPI) {
        this.mAPI = mAPI;
    }

    public CampaignDataStore createCloudCampaignDataStore() {
        return new CampaignDataStore(mAPI);

    }
}
