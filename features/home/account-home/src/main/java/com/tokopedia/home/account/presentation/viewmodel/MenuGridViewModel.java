package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

import java.util.List;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridViewModel implements Visitable<AccountTypeFactory> {
    private String title;
    private String linkText;
    private String applinkUrl;
    private List<MenuGridItemViewModel> items;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getApplinkUrl() {
        return applinkUrl;
    }

    public void setApplinkUrl(String applinkUrl) {
        this.applinkUrl = applinkUrl;
    }

    public List<MenuGridItemViewModel> getItems() {
        return items;
    }

    public void setItems(List<MenuGridItemViewModel> items) {
        this.items = items;
    }
}
