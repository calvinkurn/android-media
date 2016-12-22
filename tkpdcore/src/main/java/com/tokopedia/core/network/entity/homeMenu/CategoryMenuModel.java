/*
 * Created By Kulomady on 10/7/16 1:43 PM
 * Copyright (c) 2016. All Rights Reserved
 *
 * Last Modified 10/7/16 1:43 PM
 */

package com.tokopedia.core.network.entity.homeMenu;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
public class CategoryMenuModel {
    private String headerTitle;
    private ArrayList<com.tokopedia.core.network.entity.homeMenu.CategoryItemModel> allItemsInSection;

    @SuppressWarnings("unused")
    public CategoryMenuModel() {

    }

    public CategoryMenuModel(
            String headerTitle,
            ArrayList<com.tokopedia.core.network.entity.homeMenu.CategoryItemModel> allItemsInSection) {

        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    @SuppressWarnings("unused")
    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<com.tokopedia.core.network.entity.homeMenu.CategoryItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    @SuppressWarnings("unused")
    public void setAllItemsInSection(
            ArrayList<com.tokopedia.core.network.entity.homeMenu.CategoryItemModel> allItemsInSection) {

        this.allItemsInSection = allItemsInSection;
    }


}
