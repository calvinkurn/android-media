package com.tokopedia.vote.domain.mapper;


import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.vote.domain.model.VoteItemDomainModel;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.vote.domain.pojo.SendVotePojo;
import com.tokopedia.vote.domain.pojo.Statistic;
import com.tokopedia.vote.domain.pojo.StatisticOption;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class SendVoteMapper implements Func1<Response<DataResponse<SendVotePojo>>, VoteStatisticDomainModel> {

    @Inject
    public SendVoteMapper() {
    }

    @Override
    public VoteStatisticDomainModel call(Response<DataResponse<SendVotePojo>> dataResponseResponse) {
        SendVotePojo pojo = dataResponseResponse.body().getData();
        return mappingToViewModel(pojo.getStatistic());
    }

    private VoteStatisticDomainModel mappingToViewModel(Statistic statistic) {
        return new VoteStatisticDomainModel(
                String.valueOf(statistic.getTotalVoter()),
                mappingToListOption(statistic.getStatisticOptions())
        );
    }

    private List<VoteItemDomainModel> mappingToListOption(List<StatisticOption> statisticOptions) {
        List<VoteItemDomainModel> list = new ArrayList<>();
        for (StatisticOption option : statisticOptions) {
            list.add(new VoteItemDomainModel(
                    option.getOptionId() != null ? option.getOptionId() : "",
                    option.getOption() != null ? option.getOption() : "",
                    option.getPercentage(),
                    0));
        }
        return list;
    }
}
