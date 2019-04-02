package com.tokopedia.kol.feature.postdetail.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;

/**
 * @author by milhamj on 27/07/18.
 */

public class SeeAllCommentsViewModel implements Visitable<KolPostDetailTypeFactory> {

    private int postId;
    private int totalComments;
    private boolean showSeeMore;

    public SeeAllCommentsViewModel(int postId, int totalComments, boolean showSeeMore) {
        this.postId = postId;
        this.totalComments = totalComments;
        this.showSeeMore = showSeeMore;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isShowSeeMore() {
        return showSeeMore;
    }

    @Override
    public int type(KolPostDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
