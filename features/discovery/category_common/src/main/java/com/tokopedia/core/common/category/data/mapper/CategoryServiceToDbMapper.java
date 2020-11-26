package com.tokopedia.core.common.category.data.mapper;

import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;
import com.tokopedia.core.common.category.domain.model.CategoriesResponse;
import com.tokopedia.core.common.category.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryServiceToDbMapper implements Func1<CategoriesResponse, List<CategoryDataBase>> {
    @Override
    public List<CategoryDataBase> call(CategoriesResponse categoriesResponse) {
        List<Category> categories = categoriesResponse.getCategories().getCategories();

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
        dbModel.setName(category.getName());
        dbModel.setParentId((long)parent);
        boolean hasChild = !category.getChild().isEmpty();
        dbModel.setHasChild(hasChild);
        if (hasChild){
            dbModels.addAll(mapCategories(category.getChild(), Integer.parseInt(category.getId())));
        }
        dbModels.add(dbModel);

        return dbModels;
    }
}
