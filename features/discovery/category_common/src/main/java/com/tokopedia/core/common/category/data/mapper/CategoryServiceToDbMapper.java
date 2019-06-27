package com.tokopedia.core.common.category.data.mapper;

import com.tokopedia.core.common.category.data.source.cloud.model.Category;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryServiceModel;
import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryServiceToDbMapper implements Func1<CategoryServiceModel, List<CategoryDataBase>> {
    @Override
    public List<CategoryDataBase> call(CategoryServiceModel serviceModel) {
        List<Category> categories = serviceModel.getData().getCategories();

        return mapCategories(categories, CategoryDataBase.LEVEL_ONE_PARENT);
    }

    private List<CategoryDataBase> mapCategories(List<Category> categories, int parent) {
        List<CategoryDataBase> dbModels = new ArrayList<>();

        for(Category category : categories){
            List<CategoryDataBase> dbChildModels = mapCategory(category, parent);
            dbModels.addAll(dbChildModels);
        }

        return dbModels;
    }

    private List<CategoryDataBase> mapCategory(Category category, int parent) {
        List<CategoryDataBase> dbModels = new ArrayList<>();

        CategoryDataBase dbModel = new CategoryDataBase();
        dbModel.setId(Long.parseLong(category.getId()));
        dbModel.setIdentifier(category.getIdentifier());
        dbModel.setName(category.getName());
        dbModel.setParentId(parent);
        dbModel.setWeight(category.getWeight());
        boolean hasChild = category.getChild() != null && !category.getChild().isEmpty();
        dbModel.setHasChild(hasChild);
        if (hasChild){
            dbModels.addAll(mapCategories(category.getChild(), Integer.parseInt(category.getId())));
        }
        dbModels.add(dbModel);

        return dbModels;
    }
}
