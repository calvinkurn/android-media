package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory;

/**
 * @author by nisie on 9/13/17.
 */

public class EmptySearchModel implements Visitable<InboxReputationTypeFactory> {

    String title;
    String buttonText;
    View.OnClickListener buttonListener;

    public EmptySearchModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public View.OnClickListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(View.OnClickListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    @Override
    public int type(InboxReputationTypeFactory inboxReputationTypeFactory) {
        return inboxReputationTypeFactory.type(this);
    }
}