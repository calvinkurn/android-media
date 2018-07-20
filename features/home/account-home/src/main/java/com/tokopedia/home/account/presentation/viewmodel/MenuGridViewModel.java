package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryGrid;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridViewModel implements Visitable<AccountTypeFactory> {
    private CategoryGrid categoryGrid;

    public CategoryGrid getCategoryGrid() {
        return categoryGrid;
    }

    public void setCategoryGrid(CategoryGrid categoryGrid) {
        this.categoryGrid = categoryGrid;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
