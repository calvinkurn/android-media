package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SaldoViewModel implements Visitable<HomeTypeFactory> {

    List<ItemModel> listItems;

    public SaldoViewModel() {
        listItems = new ArrayList<>();
    }

    public List<ItemModel> getListItems() {
        return listItems;
    }

    public void setListItems(List<ItemModel> listItems) {
        this.listItems = listItems;
    }

    public void addItem(ItemModel item) {
        this.listItems.add(item);
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static class ItemModel {
        int icon;
        String title = "";
        String subtitle = "";
        String applinks = "";
        String redirectUrl = "";

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
    }

}
