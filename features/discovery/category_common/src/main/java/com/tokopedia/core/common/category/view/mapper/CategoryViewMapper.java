package com.tokopedia.core.common.category.view.mapper;

import android.support.annotation.NonNull;

import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewMapper {

    public static List<CategoryLevelViewModel> mapLevel(List<CategoryLevelDomainModel> categoryLevelDomainModels) {
        List<CategoryLevelViewModel> levelViewModels = new ArrayList<>();
        for (int i = 0; i < categoryLevelDomainModels.size(); i ++) {
            CategoryLevelDomainModel categoryLevelDomainModel = categoryLevelDomainModels.get(i);
            List<CategoryViewModel> listViewModel = mapList(categoryLevelDomainModel.getCategoryModels());
            CategoryLevelViewModel levelViewModel = new CategoryLevelViewModel(listViewModel, i);
            levelViewModel.setCategoryId(categoryLevelDomainModel.getSelectedCategoryId());
            levelViewModels.add(levelViewModel);
        }
        return levelViewModels;
    }

    public static List<CategoryViewModel> mapList(List<CategoryDomainModel> domainModels) {
        List<CategoryViewModel> viewModels = new ArrayList<>();
        for (CategoryDomainModel domainModel : domainModels){
            viewModels.add(getCategoryViewModel(domainModel));
        }
        return viewModels;
    }

    @NonNull
    private static CategoryViewModel getCategoryViewModel(CategoryDomainModel domainModel) {
        CategoryViewModel viewModel = new CategoryViewModel();
        viewModel.setName(domainModel.getName());
        viewModel.setId(domainModel.getId());
        viewModel.setHasChild(domainModel.isHasChild());
        return viewModel;
    }
}
