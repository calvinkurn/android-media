package com.tokopedia.mitra.homepage.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.mitra.homepage.adapter.viewholder.HomepageCategoryViewHolder;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;


public class HomepageCategoriesAdapterTypeFactory extends BaseAdapterTypeFactory implements HomepageCategoriesTypeFactory {
    private HomepageCategoryClickListener homepageCategoryClickListener;

    public HomepageCategoriesAdapterTypeFactory(HomepageCategoryClickListener homepageCategoryClickListener) {
        this.homepageCategoryClickListener = homepageCategoryClickListener;
    }

    @Override
    public int type(CategoryRow categoryRow) {
        return HomepageCategoryViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        switch (type) {
            case HomepageCategoryViewHolder.LAYOUT:
                return new HomepageCategoryViewHolder(parent, homepageCategoryClickListener);
            default:
                return super.createViewHolder(parent, type);
        }

    }
}
