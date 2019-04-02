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
            ArrayList<com.tokopedia.discovery.categorynav.domain.model.Category> categoryList = new ArrayList<>();
            for (Category category: dataResponse.body().getResult().getCategories()) {
                com.tokopedia.discovery.categorynav.domain.model.Category categoryModel = new
                        com.tokopedia.discovery.categorynav.domain.model.Category();
                categoryModel.setId(category.getId());
                categoryModel.setName(category.getName());
                categoryModel.setHasChild(category.getHasChild());
                categoryModel.setIconImageUrl(category.getIconImageUrl());
                categoryModel.setIndentation(1);
                if (category.getChild()!=null && category.getChild().size()>0) {
                    List<com.tokopedia.discovery.categorynav.domain.model.Category> childrenLevel2 = new ArrayList<>();
                    for (Child child: category.getChild()) {
                        com.tokopedia.discovery.categorynav.domain.model.Category childCategoryLevel2 =
                                new com.tokopedia.discovery.categorynav.domain.model.Category();
                        childCategoryLevel2.setId(child.getId());
                        childCategoryLevel2.setName(child.getName());
                        childCategoryLevel2.setHasChild(child.getHasChild());
                        if (child.getChild()!=null && child.getChild().size()>0) {
                            List<com.tokopedia.discovery.categorynav.domain.model.Category> childrenLevel3 =
                                    new ArrayList<>();
                            for (Child childLevel3: child.getChild()) {
                                com.tokopedia.discovery.categorynav.domain.model.Category childCategoryLevel3 =
                                        new com.tokopedia.discovery.categorynav.domain.model.Category();
                                childCategoryLevel3.setId(childLevel3.getId());
                                childCategoryLevel3.setName(childLevel3.getName());
                                childCategoryLevel3.setHasChild(false);
                                childrenLevel3.add(childCategoryLevel3);
                            }
                            childCategoryLevel2.addChildren(childrenLevel3,3);
                        }
                        childrenLevel2.add(childCategoryLevel2);
                    }
                    categoryModel.addChildren(childrenLevel2,2);
                }
                categoryList.add(categoryModel);
            }
            categoryNavDomainModel.setCategories(categoryList);

        }

        return categoryNavDomainModel;
    }
}
