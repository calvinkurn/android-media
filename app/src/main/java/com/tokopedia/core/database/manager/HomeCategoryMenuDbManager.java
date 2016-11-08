/*
 * Created By Kulomady on 10/12/16 11:37 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 10/12/16 11:37 PM
 */

package com.tokopedia.core.database.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.CategoryItemModelDb;
import com.tokopedia.core.database.model.CategoryMenuModelDb;
import com.tokopedia.core.home.model.homeMenu.CategoryItemModel;
import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;
import com.tokopedia.core.home.model.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.home.model.homeMenu.LayoutRow;
import com.tokopedia.core.home.model.homeMenu.LayoutSection;
import com.tokopedia.core.home.presenter.HomeCatMenuPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 11/12/16
 */
public class HomeCategoryMenuDbManager implements DbFlowOperation<CategoryMenuModelDb> {

    private static final String TAG = HomeCategoryMenuDbManager.class.getSimpleName();

    @Override
    public void store() {

    }

    @Override
    public void store(CategoryMenuModelDb data) {

    }

    public void store(HomeCategoryMenuItem homeCategoryMenuItem) {
        for (LayoutSection layoutSection : homeCategoryMenuItem.getData().getLayoutSections()) {
            CategoryMenuModelDb db = new CategoryMenuModelDb();
            db.setHeaderTitle(layoutSection.getTitle());
            db.setId(layoutSection.getId());
            db.setLastUpdated(System.currentTimeMillis());
            db.save();
            for (LayoutRow layoutRow : layoutSection.getLayoutRows()) {
                CategoryItemModelDb categoryItemModelDb = new CategoryItemModelDb();
                categoryItemModelDb.setId((int) layoutRow.getId().longValue());
                categoryItemModelDb.setDescription(layoutRow.getAdditionalInfo());
                categoryItemModelDb.setImageUrl(layoutRow.getImageUrl());

                categoryItemModelDb.setName(layoutRow.getName());
                if (layoutRow.getType().equals(HomeCatMenuPresenterImpl.MARKETPLACE_TYPE)) {
                    categoryItemModelDb.setType(0);
                    categoryItemModelDb.setRedirectValue(String.valueOf(layoutRow.getCategoryId()));
                } else {
                    categoryItemModelDb.setType(1);
                    categoryItemModelDb.setRedirectValue(layoutRow.getUrl());
                }

                categoryItemModelDb.setCategoryMenuModelDb(db);
                categoryItemModelDb.save();

            }

        }
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {
        Delete.tables(CategoryMenuModelDb.class, CategoryItemModelDb.class);
    }

    @Override
    public boolean isExpired(final long currentTime) {
        try {
            CategoryMenuModelDb categoryMenuModelDb = SQLite.select().from(CategoryMenuModelDb.class).querySingle();
            if (categoryMenuModelDb == null) {
                return true;
            } else {
//            long oneDay = 1000 * 60 * 60 ; // one minute
                long oneDay = 1000 * 60 * 60 * 24;
                long oldTime = categoryMenuModelDb.getLastUpdated();
                Log.d("TAG", "isHomeCategoryMenuStillValid: oneDay : " + oneDay + " oldtime : " + oldTime
                        + " System.currentTimeMillis() "
                        + String.valueOf(System.currentTimeMillis()));

                if (currentTime - oldTime < oneDay) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "isExpired: ", e);
            return true;
        }
    }

    @Nullable
    @Override
    public CategoryMenuModelDb getData(String key) {
        return null;
    }

    @Override
    public List<CategoryMenuModelDb> getDataList(String key) {
        return null;
    }

    @Override
    public String getValueString(String key) {
        return null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        return null;
    }

    public List<CategoryMenuModel> getDataCategoryMenu() {
        List<CategoryMenuModel> categoryMenuModels = new ArrayList<>();

        List<CategoryMenuModelDb> categoryMenuModelDbList = SQLite.select().
                from(CategoryMenuModelDb.class).queryList();
        for (CategoryMenuModelDb categoryMenuModelDb : categoryMenuModelDbList) {
            List<CategoryItemModel> categoryItemModels = new ArrayList<>();
            for (CategoryItemModelDb categoryItemModelDb : categoryMenuModelDb.getAllItemsInSection()) {
                CategoryItemModel categoryItemModel = convertDbValueToModelView(categoryItemModelDb);
                categoryItemModels.add(categoryItemModel);
            }
            categoryMenuModels.add(new CategoryMenuModel(
                    categoryMenuModelDb.getHeaderTitle(),
                    (ArrayList<CategoryItemModel>) categoryItemModels)
            );
        }
        return categoryMenuModels;

    }


    @NonNull
    private CategoryItemModel convertDbValueToModelView(CategoryItemModelDb categoryItemModelDb) {
        CategoryItemModel categoryItemModel = new CategoryItemModel();
        categoryItemModel.setName(categoryItemModelDb.getName());
        categoryItemModel.setRedirectValue(categoryItemModelDb.getRedirectValue());
        categoryItemModel.setImageUrl(categoryItemModelDb.getImageUrl());
        categoryItemModel.setType(
                categoryItemModelDb.getType() == 0 ?
                        CategoryItemModel.TYPE.CATEGORY
                        : CategoryItemModel.TYPE.GIMMIC);
        return categoryItemModel;
    }

}