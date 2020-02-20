package com.tokopedia.qrscanner.branchio;

import com.tokopedia.qrscanner.branchio.api.BranchIOAPI;

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
