package com.tokopedia.core.common.category.view.mapper;

import androidx.annotation.NonNull;

import androidx.annotation.NonNull;

import com.tokopedia.core.common.category.domain.model.Category;
import com.tokopedia.core.common.category.domain.model.CategoryModel;
import com.tokopedia.core.common.category.domain.model.CategorySelectedModel;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saidfaisal on 28/7/20.
 */

public class CategoryViewMapper {

    public static List<CategoryLevelViewModel> mapLevel(List<CategorySelectedModel> categorySelectedModels) {
        List<CategoryLevelViewModel> levelViewModels = new ArrayList<>();
        for (int i = 0; i < categorySelectedModels.size(); i ++) {
            CategorySelectedModel categorySelectedModel = categorySelectedModels.get(i);
            List<CategoryViewModel> listViewModel = mapCategoryModelsToCategoryViewModels(mapCategoriesToCategoryModels(categorySelectedModel.getChild()));
            CategoryLevelViewModel levelViewModel = new CategoryLevelViewModel(listViewModel, i);
            levelViewModel.setCategoryId(categorySelectedModel.getCategoryId());
            levelViewModels.add(levelViewModel);
        }
        return levelViewModels;
    }

    public static List<CategoryViewModel> mapCategoryModelsToCategoryViewModels(List<CategoryModel> categories) {
        List<CategoryViewModel> viewModels = new ArrayList<>();
        for (CategoryModel categoryModel: categories) {
            viewModels.add(getCategoryViewModel(categoryModel));
        }
        return viewModels;
    }

    public static List<CategoryModel> mapCategoriesToCategoryModels(List<Category> categories) {
        List<CategoryModel> categoryModels = new ArrayList<>();
        for (Category category: categories) {
            categoryModels.add(getCategoryModel(category));
        }
        return categoryModels;
    }

    @NonNull
    private static CategoryViewModel getCategoryViewModel(CategoryModel category) {
        CategoryViewModel viewModel = new CategoryViewModel();
        viewModel.setName(category.getName());
        viewModel.setId(category.getId());
        viewModel.setHasChild(category.getHasChild());
        return viewModel;
    }

    @NonNull
    public static CategoryModel getCategoryModel(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(Long.parseLong(category.getId()));
        categoryModel.setName(category.getName());
        boolean hasChild = false;
        if (!category.getChild().isEmpty()) {
            hasChild = true;
        }
        categoryModel.setHasChild(hasChild);
        return categoryModel;
    }
}

