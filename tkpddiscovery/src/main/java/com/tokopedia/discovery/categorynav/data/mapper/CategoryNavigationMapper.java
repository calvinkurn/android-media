package com.tokopedia.discovery.categorynav.data.mapper;

import com.tokopedia.core.network.entity.categories.Category;
import com.tokopedia.core.network.entity.categories.Child;
import com.tokopedia.core.network.entity.categories.Data;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationMapper implements Func1<Response<Data>,CategoryNavDomainModel> {

    @Override
    public CategoryNavDomainModel call(Response<Data> dataResponse) {

        CategoryNavDomainModel categoryNavDomainModel = new CategoryNavDomainModel();

        if (dataResponse.body()!=null && dataResponse.body().getResult() !=null
                && dataResponse.body().getResult().getCategories() !=null) {
            List<com.tokopedia.discovery.categorynav.domain.model.Category> categoryList = new ArrayList<>();
            for (Category category: dataResponse.body().getResult().getCategories()) {
                com.tokopedia.discovery.categorynav.domain.model.Category categoryModel = new
                        com.tokopedia.discovery.categorynav.domain.model.Category();
                categoryModel.setId(category.getId());
                categoryModel.setName(category.getName());
                categoryModel.setHasChild(category.getHasChild());
                categoryModel.setIconImageUrl(category.getIconImageUrl());
                if (category.getChild()!=null && category.getChild().size()>0) {
                    List<ChildCategory> childCategories = new ArrayList<>();
                    for (Child child: category.getChild()) {
                        ChildCategory childCategory = new ChildCategory();
                        childCategory.setId(child.getId());
                        childCategory.setName(child.getName());
                        childCategory.setHasChild(child.getHasChild());
                        childCategories.add(childCategory);
                    }
                }
                categoryList.add(categoryModel);
            }
            categoryList.addAll(categoryList);

        }

        return categoryNavDomainModel;
    }
}
