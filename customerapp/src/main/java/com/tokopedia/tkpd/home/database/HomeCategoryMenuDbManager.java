/*
 * Created By Kulomady on 10/12/16 11:37 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 10/12/16 11:37 PM
 */

package com.tokopedia.tkpd.home.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.CategoryItemModelDb;
import com.tokopedia.core.database.model.CategoryMenuModelDb;
import com.tokopedia.core.database.model.HomeCategoryMenuModelDb;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.entity.homeMenu.LayoutRow;
import com.tokopedia.core.network.entity.homeMenu.LayoutSection;
import com.tokopedia.tkpd.home.presenter.HomeCatMenuPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 11/12/16
 */
public class HomeCategoryMenuDbManager implements DbFlowOperation<HomeCategoryMenuModelDb> {

    private static final String TAG = HomeCategoryMenuDbManager.class.getSimpleName();

    @Override
    public void store() {

    }

    @Override
    public void store(HomeCategoryMenuModelDb data) {

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

    public void store(String content) {
        HomeCategoryMenuModelDb homeCategoryMenuModelDb = new HomeCategoryMenuModelDb();
        homeCategoryMenuModelDb.setId(1);
        homeCategoryMenuModelDb.setContentHomeCategory(content);
        homeCategoryMenuModelDb.setLastUpdated(System.currentTimeMillis());
        homeCategoryMenuModelDb.save();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {
        Delete.tables(HomeCategoryMenuModelDb.class);
//        Delete.tables(CategoryMenuModelDb.class, CategoryItemModelDb.class);
//        new Delete().from(CategoryMenuModelDb.class).query()

    }


    @Override
    public boolean isExpired(final long currentTime) {
        try {
            CategoryMenuModelDb categoryMenuModelDb = SQLite.select()
                    .from(CategoryMenuModelDb.class)
                    .querySingle();

            if (categoryMenuModelDb == null) {
                return true;
            } else {

                long oldTime = categoryMenuModelDb.getLastUpdated();
                long oneHour = 1000 * 60 * 60;
                Log.d("TAG", "isHomeCategoryMenuStillValid: oneHour : "
                        + oneHour + " oldtime : " + oldTime
                        + " System.currentTimeMillis() "
                        + String.valueOf(System.currentTimeMillis()));

                return currentTime - oldTime >= oneHour;
            }
        } catch (Exception e) {
            Log.e(TAG, "isExpired: ", e);
            return true;
        }
    }

    public boolean isAlreadyExpired(final long currentTime) {
        try {
            HomeCategoryMenuModelDb categoryMenuModelDb = SQLite.select()
                    .from(HomeCategoryMenuModelDb.class)
                    .querySingle();

            if (categoryMenuModelDb == null) {
                return true;
            } else {

                long oldTime = categoryMenuModelDb.getLastUpdated();
//                long oneHour = 1000 * 60 * 60;
                long oneHour = 1000 * 60;
                Log.d("TAG", "isHomeCategoryMenuStillValid: oneHour : "
                        + oneHour + " oldtime : " + oldTime
                        + " System.currentTimeMillis() "
                        + String.valueOf(System.currentTimeMillis()));

                return currentTime - oldTime >= oneHour;
            }
        } catch (Exception e) {
            Log.e(TAG, "isExpired: ", e);
            return true;
        }
    }

    @Nullable
    @Override
    public HomeCategoryMenuModelDb getData(String key) {
        return null;
    }

    @Override
    public List<HomeCategoryMenuModelDb> getDataList(String key) {
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

    public List<CategoryMenuModel> getDataHomeCategoryMenu() {
        List<CategoryMenuModel> categoryMenuModels = new ArrayList<>();

        HomeCategoryMenuModelDb homeCategoryMenuModelDb = SQLite.select().
                from(HomeCategoryMenuModelDb.class).querySingle();


        if (homeCategoryMenuModelDb != null) {
            HomeCategoryMenuItem homeCategoryMenuItem = new Gson().fromJson(
                    homeCategoryMenuModelDb.getContentHomeCategory(), HomeCategoryMenuItem.class);

            for (LayoutSection layoutSection : homeCategoryMenuItem.getData().getLayoutSections()) {
                List<CategoryItemModel> categoryItemModels = new ArrayList<>();
                for (LayoutRow layoutRow : layoutSection.getLayoutRows()) {
                    CategoryItemModel categoryItemModel = new CategoryItemModel();
                    categoryItemModel.setName(layoutRow.getName());
                    categoryItemModel.setImageUrl(layoutRow.getImageUrl());

                    if ("Marketplace".equalsIgnoreCase(layoutRow.getType())) {
                        categoryItemModel.setRedirectValue(String.valueOf(layoutRow.getCategoryId()));
                        categoryItemModel.setType(CategoryItemModel.TYPE.CATEGORY);
                    } else if ("Digital".equalsIgnoreCase(layoutRow.getType())) {
                        categoryItemModel.setRedirectValue(layoutRow.getUrl());
                        categoryItemModel.setType(CategoryItemModel.TYPE.GIMMIC);
                    }
                    categoryItemModels.add(categoryItemModel);

                }
                categoryMenuModels.add(new CategoryMenuModel(
                        layoutSection.getTitle(),
                        (ArrayList<CategoryItemModel>) categoryItemModels

                ));

            }
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