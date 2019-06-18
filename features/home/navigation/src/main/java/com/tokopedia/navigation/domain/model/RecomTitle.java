package com.tokopedia.navigation.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory;

/**
 * Author errysuprayogi on 15,March,2019
 */
public class RecomTitle implements Visitable<InboxTypeFactory> {

    private String title;

    public RecomTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int type(InboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
