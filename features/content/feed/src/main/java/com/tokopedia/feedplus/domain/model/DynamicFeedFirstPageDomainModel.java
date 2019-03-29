package com.tokopedia.feedplus.domain.model;

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;

/**
 * @author by milhamj on 07/01/19.
 */
public class DynamicFeedFirstPageDomainModel {
    private DynamicFeedDomainModel dynamicFeedDomainModel;
    private WhitelistDomain whitelistDomain;
    private boolean isInterestWhitelist;

    public DynamicFeedFirstPageDomainModel(DynamicFeedDomainModel dynamicFeedDomainModel,
                                           WhitelistDomain whitelistDomain, boolean
                                                   isInterestWhitelist) {
        this.dynamicFeedDomainModel = dynamicFeedDomainModel;
        this.whitelistDomain = whitelistDomain;
        this.isInterestWhitelist = isInterestWhitelist;
    }

    public DynamicFeedDomainModel getDynamicFeedDomainModel() {
        return dynamicFeedDomainModel;
    }

    public void setDynamicFeedDomainModel(DynamicFeedDomainModel dynamicFeedDomainModel) {
        this.dynamicFeedDomainModel = dynamicFeedDomainModel;
    }

    public WhitelistDomain getWhitelistDomain() {
        return whitelistDomain;
    }

    public void setWhitelistDomain(WhitelistDomain whitelistDomain) {
        this.whitelistDomain = whitelistDomain;
    }

    public boolean isInterestWhitelist() {
        return isInterestWhitelist;
    }

    public void setInterestWhitelist(boolean interestWhitelist) {
        isInterestWhitelist = interestWhitelist;
    }
}