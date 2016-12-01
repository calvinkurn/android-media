package com.tokopedia.sellerapp.home.model;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
public class CategoryMenuModel {
    private String headerTitle;
    private ArrayList<CategoryItemModel> allItemsInSection;


    public CategoryMenuModel() {

    }
    public CategoryMenuModel(String headerTitle, ArrayList<CategoryItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<CategoryItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<CategoryItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
