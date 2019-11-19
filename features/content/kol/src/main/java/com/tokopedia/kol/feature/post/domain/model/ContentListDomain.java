package com.tokopedia.kol.feature.post.domain.model;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * @author by milhamj on 10/10/18.
 */
public class ContentListDomain {
    @NonNull
    private List<Visitable> visitableList;

    @NonNull
    private String lastCursor;

    public ContentListDomain(@NonNull List<Visitable> visitableList, @NonNull String lastCursor) {
        this.visitableList = visitableList;
        this.lastCursor = lastCursor;
    }

    @NonNull
    public List<Visitable> getVisitableList() {
        return visitableList;
    }

    public void setVisitableList(@NonNull List<Visitable> visitableList) {
        this.visitableList = visitableList;
    }

    @NonNull
    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(@NonNull String lastCursor) {
        this.lastCursor = lastCursor;
    }
}
