package com.tokopedia.mitra.homepage.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;

public interface HomepageCategoriesTypeFactory extends AdapterTypeFactory{
    int type(CategoryRow categoryRow);
}
