package com.tokopedia.baselist.adapter.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.tokopedia.baselist.adapter.Visitable;
import com.tokopedia.baselist.adapter.factory.AdapterTypeFactory;
import com.tokopedia.baselist.adapter.viewholders.EmptyResultViewHolder;

/**
 * @author Kulomady on 1/25/17.
 */

public class EmptyModel implements Visitable<AdapterTypeFactory> {

    @DrawableRes
    private int iconRes;
    private String urlRes;
    private String title;
    @StringRes
    private int contentRes;
    private String content;
    private String description;
    @StringRes
    private int buttonTitleRes;
    private String buttonTitle;
    private EmptyResultViewHolder.Callback callback;

    public EmptyModel() {
    }

    @Override
    public int type(AdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
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

    public String getUrlRes() {
        return urlRes;
    }

    public void setUrlRes(String urlRes) {
        this.urlRes = urlRes;
    }
}
