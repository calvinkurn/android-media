package com.tokopedia.feedplus.view.viewmodel.kol;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.List;

/**
 * @author by milhamj on 14/05/18.
 */

public class PollViewModel implements Visitable<FeedPlusTypeFactory> {
    private String pollId;
    private String totalVoter;
    private List<PollOptionViewModel> optionViewModels;

    public PollViewModel(String pollId, String totalVoter,
                         List<PollOptionViewModel> optionViewModels) {
        this.pollId = pollId;
        this.totalVoter = totalVoter;
        this.optionViewModels = optionViewModels;
    }

    public String getPollId() {
        return pollId;
    }

    public String getTotalVoter() {
        return totalVoter;
    }

    public List<PollOptionViewModel> getOptionViewModels() {
        return optionViewModels;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
