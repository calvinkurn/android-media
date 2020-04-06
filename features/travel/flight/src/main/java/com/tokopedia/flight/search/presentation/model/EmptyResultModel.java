package com.tokopedia.flight.search.presentation.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder;

/**
 * Created by alvarisi on 12/22/17.
 */

public class EmptyResultModel implements Visitable<FlightSearchAdapterTypeFactory> {
    @DrawableRes
    private int iconRes;
    private String title;
    @StringRes
    private int contentRes;
    private String content;
    private String description;
    @StringRes
    private int buttonTitleRes;
    private String buttonTitle;
    private EmptyResultViewHolder.Callback callback;

    public EmptyResultModel() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public EmptyResultViewHolder.Callback getCallback() {
        return callback;
    }

    public void setCallback(EmptyResultViewHolder.Callback callback) {
        this.callback = callback;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getContentRes() {
        return contentRes;
    }

    public void setContentRes(@StringRes int contentRes) {
        this.contentRes = contentRes;
    }

    public int getButtonTitleRes() {
        return buttonTitleRes;
    }

    public void setButtonTitleRes(@StringRes int buttonTitleRes) {
        this.buttonTitleRes = buttonTitleRes;
    }

    @Override
    public int type(FlightSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
