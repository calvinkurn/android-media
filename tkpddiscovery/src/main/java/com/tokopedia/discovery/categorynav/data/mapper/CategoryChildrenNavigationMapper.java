package com.tokopedia.discovery.categorynav.data.mapper;

import com.tokopedia.core.network.entity.categories.Category;
import com.tokopedia.core.network.entity.categories.Data;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alifa on 7/18/17.
 */

public class CategoryChildrenNavigationMapper implements Func1<Response<Data>,List<com.tokopedia.discovery.categorynav.domain.model.Category>> {

    @Override
    public List<com.tokopedia.discovery.categorynav.domain.model.Category> call(Response<Data> dataResponse) {

        List<com.tokopedia.discovery.categorynav.domain.model.Category> children = new ArrayList<>();

        if (dataResponse.body()!=null && dataResponse.body().getResult() !=null
                && dataResponse.body().getResult().getCategories() !=null) {
            for (Category category: dataResponse.body().getResult().getCategories()) {
                com.tokopedia.discovery.categorynav.domain.model.Category childCategory = new
                        com.tokopedia.discovery.categorynav.domain.model.Category();
                childCategory.setId(category.getId());
                childCategory.setName(category.getName());
                childCategory.setHasChild(category.getHasChild());
                children.add(childCategory);
            }
        }
        return children;
    }
}
