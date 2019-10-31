package com.tokopedia.feedplus.domain.model;

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;

/**
 * @author by milhamj on 07/01/19.
 */
public class DynamicFeedFirstPageDomainModel {
    private DynamicFeedDomainModel dynamicFeedDomainModel;
    private boolean isInterestWhitelist;

    public DynamicFeedFirstPageDomainModel(DynamicFeedDomainModel dynamicFeedDomainModel,
                                           boolean isInterestWhitelist) {
        this.dynamicFeedDomainModel = dynamicFeedDomainModel;
        this.isInterestWhitelist = isInterestWhitelist;
    }

    public DynamicFeedDomainModel getDynamicFeedDomainModel() {
        return dynamicFeedDomainModel;
    }

    public void setDynamicFeedDomainModel(DynamicFeedDomainModel dynamicFeedDomainModel) {
        this.dynamicFeedDomainModel = dynamicFeedDomainModel;
    }

    public boolean isInterestWhitelist() {
        return isInterestWhitelist;
    }

    public void setInterestWhitelist(boolean interestWhitelist) {
        isInterestWhitelist = interestWhitelist;
    }
}