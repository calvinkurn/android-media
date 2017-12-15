package com.tokopedia.tkpd.home.database;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.model.HomeCategoryMenuModelDb;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.entity.homeMenu.LayoutRow;
import com.tokopedia.core.network.entity.homeMenu.LayoutSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kulomady on 11/30/16.
 */

class HomeCategoryMenuMapper {

    private static final String MARKETPLACE = "Marketplace";
    private static final String DIGITAL = "Digital";
    private final HomeCategoryMenuModelDb homeCategoryMenuModelDb;

    HomeCategoryMenuMapper(HomeCategoryMenuModelDb homeCategoryMenuModelDb) {
        this.homeCategoryMenuModelDb = homeCategoryMenuModelDb;
    }

    List<CategoryMenuModel> convert() {
        return this.convertDataFromDbToListCategoryMenu();
    }

    private List<CategoryMenuModel> convertDataFromDbToListCategoryMenu() {

        if (dataHomeCategoryMenuNotEmpty()) {
            HomeCategoryMenuItem homeCategoryMenuItem = new Gson().fromJson(
                    homeCategoryMenuModelDb.getContentHomeCategory(), HomeCategoryMenuItem.class);

            List<CategoryMenuModel> categoryMenuModels = new ArrayList<>();
            for (LayoutSection layoutSection : homeCategoryMenuItem.getData().getLayoutSections()) {
                categoryMenuModels.add(new CategoryMenuModel(
                        layoutSection.getTitle(),
                        (ArrayList<CategoryItemModel>) getCategoryItemModels(layoutSection)

                ));

            }
            return categoryMenuModels;
        } else {
            return Collections.emptyList();
        }
    }


    @NonNull
    private List<CategoryItemModel> getCategoryItemModels(LayoutSection layoutSection) {
        List<CategoryItemModel> listCategoryItemModels = new ArrayList<>();

        // TODO : this section is for dummie, please remove when api available
        CategoryItemModel temp = new CategoryItemModel();
        temp.setName("Flight");
        temp.setImageUrl("https://ecs7.tokopedia.net/img/category/new/v1/icon_mainan.png");
        temp.setCategoryId("11");
        temp.setRedirectValue("http://tokopedia.com");
        temp.setType(CategoryItemModel.TYPE.DIGITAL);
        temp.setAppLinks("tokopedia://flight");
        listCategoryItemModels.add(temp);
        for (LayoutRow layoutRow : layoutSection.getLayoutRows()) {
            CategoryItemModel categoryItemModel = new CategoryItemModel();
            categoryItemModel.setName(layoutRow.getName());
            categoryItemModel.setImageUrl(layoutRow.getImageUrl());

            if (isCategoryItemMarketPlace(layoutRow)) {
                new CategoryItemMarketPlaceMapper(layoutRow, categoryItemModel).invoke();
            } else if (isCategoryItemDigital(layoutRow)) {
                new CategoryItemDigitalMapper(layoutRow, categoryItemModel).invoke();
            } else {
                new CategoryItemDefaultMapper(layoutRow, categoryItemModel).invoke();
            }
            listCategoryItemModels.add(categoryItemModel);

        }
        return listCategoryItemModels;
    }

    private boolean isCategoryItemMarketPlace(LayoutRow aLayoutRow) {
        return MARKETPLACE.equalsIgnoreCase(aLayoutRow.getType());
    }

    private boolean isCategoryItemDigital(LayoutRow aLayoutRow) {
        return DIGITAL.equalsIgnoreCase(aLayoutRow.getType());
    }


    private boolean dataHomeCategoryMenuNotEmpty() {
        return this.homeCategoryMenuModelDb != null;
    }

    //helper Mapper class
    private class CategoryItemMarketPlaceMapper {
        final private LayoutRow mLayoutRow;
        final private CategoryItemModel mCategoryItemModel;

        CategoryItemMarketPlaceMapper(LayoutRow layoutRow, CategoryItemModel categoryItemModel) {
            mLayoutRow = layoutRow;
            mCategoryItemModel = categoryItemModel;
        }

        void invoke() {
            mCategoryItemModel.setCategoryId(String.valueOf(mLayoutRow.getCategoryId()));
            mCategoryItemModel.setRedirectValue(String.valueOf(mLayoutRow.getCategoryId()));
            mCategoryItemModel.setType(CategoryItemModel.TYPE.CATEGORY);
            mCategoryItemModel.setAppLinks(mLayoutRow.getAppLinks());
        }
    }

    private class CategoryItemDigitalMapper {
        private LayoutRow mLayoutRow;
        private CategoryItemModel mCategoryItemModel;

        CategoryItemDigitalMapper(LayoutRow layoutRow, CategoryItemModel categoryItemModel) {
            mLayoutRow = layoutRow;
            mCategoryItemModel = categoryItemModel;
        }

        void invoke() {
            mCategoryItemModel.setCategoryId(String.valueOf(mLayoutRow.getCategoryId()));
            mCategoryItemModel.setRedirectValue(mLayoutRow.getUrl());
            mCategoryItemModel.setType(CategoryItemModel.TYPE.DIGITAL);
            mCategoryItemModel.setAppLinks(mLayoutRow.getAppLinks());
        }
    }

    private class CategoryItemDefaultMapper {
        private LayoutRow layoutRow;
        private CategoryItemModel categoryItemModel;

        CategoryItemDefaultMapper(LayoutRow layoutRow, CategoryItemModel categoryItemModel) {
            this.layoutRow = layoutRow;
            this.categoryItemModel = categoryItemModel;
        }

        void invoke() {
            categoryItemModel.setCategoryId(String.valueOf(layoutRow.getCategoryId()));
            categoryItemModel.setRedirectValue(layoutRow.getUrl());
            categoryItemModel.setType(CategoryItemModel.TYPE.GIMMIC);
            categoryItemModel.setAppLinks(layoutRow.getAppLinks());
        }
    }
}