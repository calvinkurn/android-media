package com.tokopedia.feedplus.view.viewmodel.kol;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;

import java.util.List;

/**
 * @author by milhamj on 14/05/18.
 */

public class PollViewModel extends BaseKolViewModel implements Visitable<FeedPlusTypeFactory> {
    private String pollId;
    private String totalVoter;
    private boolean voted;
    private List<PollOptionViewModel> optionViewModels;

    public PollViewModel(int userId, String cardType, String title, String name, String avatar,
                         String label, String kolProfileUrl, boolean followed, String review,
                         boolean liked, int totalLike, int totalComment, int page, int kolId,
                         String time, boolean isShowComment, boolean isShowLike, String pollId,
                         String totalVoter, boolean voted,
                         List<PollOptionViewModel> optionViewModels) {
        super(userId, "", cardType, title, name, avatar, label, kolProfileUrl, followed,
                review, liked, totalLike, totalComment, page, kolId, time, isShowComment,
                isShowLike, false, false, false);
        this.pollId = pollId;
        this.totalVoter = totalVoter;
        this.voted = voted;
        this.optionViewModels = optionViewModels;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getTotalVoter() {
        return totalVoter;
    }

    public void setTotalVoter(String totalVoter) {
        this.totalVoter = totalVoter;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public List<PollOptionViewModel> getOptionViewModels() {
        return optionViewModels;
    }

    public void setOptionViewModels(List<PollOptionViewModel> optionViewModels) {
        this.optionViewModels = optionViewModels;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
