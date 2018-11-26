package com.tokopedia.gallery.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

public interface TypeFactory extends AdapterTypeFactory {
    int type(ImageReviewItem viewModel);
}
