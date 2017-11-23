package com.tokopedia.core.common.category.data.mapper;

import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;
import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryDataToDomainMapper implements Func1<List<CategoryDataBase>, List<CategoryDomainModel>> {
    @Override
    public List<CategoryDomainModel> call(List<CategoryDataBase> categoryDataBases) {

        return mapDomainModels(categoryDataBases);
    }

    public static List<CategoryDomainModel> mapDomainModels(List<CategoryDataBase> categoryDataBases) {
        List<CategoryDomainModel> domainModels = new ArrayList<>();
        for (CategoryDataBase categoryDataBase : categoryDataBases) {
            domainModels.add(mapDomainModel(categoryDataBase));
        }
        return domainModels;
    }

    private static CategoryDomainModel mapDomainModel(CategoryDataBase categoryDataBase) {
        CategoryDomainModel domainModel = new CategoryDomainModel();
        domainModel.setName(categoryDataBase.getName());
        domainModel.setId(categoryDataBase.getId());
        domainModel.setIdentifier(categoryDataBase.getIdentifier());
        domainModel.setHasChild(categoryDataBase.isHasChild());
        return domainModel;
    }

}
