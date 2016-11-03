/*
 * Created By Kulomady on 10/7/16 1:43 PM
 * Copyright (c) 2016. All Rights Reserved
 *
 * Last Modified 10/7/16 1:43 PM
 */

package com.tokopedia.tkpd.home.model.homeMenu;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
public class CategoryMenuModel {
    private String headerTitle;
    private ArrayList<CategoryItemModel> allItemsInSection;

    @SuppressWarnings("unused")
    public CategoryMenuModel() {

    }
    public CategoryMenuModel(String headerTitle, ArrayList<CategoryItemModel> allItemsInSection) {
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

    public ArrayList<CategoryItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    @SuppressWarnings("unused")
    public void setAllItemsInSection(ArrayList<CategoryItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
