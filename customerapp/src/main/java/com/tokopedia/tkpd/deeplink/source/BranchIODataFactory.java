package com.tokopedia.tkpd.deeplink.source;

import com.tokopedia.tkpd.deeplink.source.api.BranchIOAPI;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public class BranchIODataFactory {

    BranchIOAPI mAPI;

    @Inject
    public BranchIODataFactory(BranchIOAPI mAPI) {
        this.mAPI = mAPI;
    }

    public BranchIODataStore createCloudCampaignDataStore() {
        return new BranchIODataStore(mAPI);

    }
}
