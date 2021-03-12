package com.tokopedia.review.feature.inbox.buyerreview.view.widgets;

import android.view.View;

public class ShareItem {

    private String icon;
    private String name;
    private View.OnClickListener onClickListener;

    public ShareItem(String icon, String name, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.name = name;
        this.onClickListener = onClickListener;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
