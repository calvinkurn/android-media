package com.tokopedia.vote.domain.model;

import java.util.List;

/**
 * @author by nisie on 3/1/18.
 */

public class VoteStatisticDomainModel {

    private List<VoteItemDomainModel> listOptions;
    private String totalParticipants;

    public VoteStatisticDomainModel(String totalParticipants, List<VoteItemDomainModel> listOptions) {
        this.totalParticipants = totalParticipants;
        this.listOptions = listOptions;
    }

    public VoteStatisticDomainModel() {}

    public List<VoteItemDomainModel> getListOptions() {
        return listOptions;
    }

    public String getTotalParticipants() {
        return totalParticipants;
    }
}
