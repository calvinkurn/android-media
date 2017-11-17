package com.tokopedia.core.common.category.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class CategoryLevelViewModel {
    public static final int UNSELECTED = -1;

    private List<CategoryViewModel> viewModels;
    private final int level;
    private long categoryId;

    public CategoryLevelViewModel(List<CategoryViewModel> map, int level) {
        viewModels = map;
        categoryId = UNSELECTED;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public List<CategoryViewModel> getViewModels() {
        return viewModels;
    }

    public void setViewModels(List<CategoryViewModel> categoryViewModels){
        viewModels = categoryViewModels;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getSelectedPosition() {
        int selectedPositionFromIndex = UNSELECTED;
        for (int i = 0; i < viewModels.size(); i ++){
            if (viewModels.get(i).getId() == categoryId){
                selectedPositionFromIndex = i;
                break;
            }
        }
        return selectedPositionFromIndex;
    }

    public CategoryViewModel getSelectedModel() throws RuntimeException{
        for (CategoryViewModel viewModel : viewModels){
            if (viewModel.getId() == categoryId){
                return viewModel;
            }
        }
        return null;
    }


}
