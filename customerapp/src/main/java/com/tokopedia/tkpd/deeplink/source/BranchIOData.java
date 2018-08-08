package com.tokopedia.tkpd.deeplink.source;

import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public class BranchIOData implements BranchIODataRepository {

    BranchIODataFactory branchIODataFactory;

    @Inject
    public BranchIOData(BranchIODataFactory branchIODataFactory) {
        this.branchIODataFactory = branchIODataFactory;
    }

    @Override
    public Observable<BranchIOAndroidDeepLink> getDeepLink(HashMap<String, Object> params) {
        return branchIODataFactory.createCloudCampaignDataStore().getCampaign(params);
    }
}
