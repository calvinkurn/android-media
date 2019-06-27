package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class AllBrandsHomeContract {

    public interface View extends CustomerView {

        Activity getActivity();

        android.view.View getRootView();

        void renderCategoryList(List<CategoryItem> categoryItems);

    }

    public interface Presenter extends CustomerPresenter<AllBrandsHomeContract.View> {

        void initialize();

        void onDestroy();

        void getAllCategories();

    }
}
