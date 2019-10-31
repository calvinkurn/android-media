package com.tokopedia.imagesearch.search.fragment.product;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.common.model.SearchParameter;

import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListFragmentView extends CustomerView {

    void launchLoginActivity(Bundle extras);

    boolean isUserHasLogin();

    String getUserId();

    void onLoadMoreEmpty();

    void appendProductList(List<Visitable> list, boolean hasNextPage);

    void disableWishlistButton(String productId);

    void enableWishlistButton(String productId);

    String getQueryKey();

    SearchParameter getSearchParameter();

    void setSearchParameter(SearchParameter searchParameter);

    void backToTop();

    void hideRefreshLayout();

    BaseAppComponent getBaseAppComponent();

    String getEmptyResultMessage();
}
