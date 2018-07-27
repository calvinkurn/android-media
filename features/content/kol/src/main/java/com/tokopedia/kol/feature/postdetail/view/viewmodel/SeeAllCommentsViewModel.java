package com.tokopedia.kol.feature.postdetail.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;

/**
 * @author by milhamj on 27/07/18.
 */

public class SeeAllCommentsViewModel implements Visitable<KolPostDetailTypeFactory> {

    private int totalComments;

    public SeeAllCommentsViewModel(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    @Override
    public int type(KolPostDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
