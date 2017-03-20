package com.tokopedia.discovery.dynamicfilter.presenter;

import android.content.Intent;

import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.Filter;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterPresenterImpl implements DynamicFilterPresenter {
    private static final String TAG = DynamicFilterPresenterImpl.class.getSimpleName();
    WeakReference<DynamicFilterView> view;
    private List<Breadcrumb> breadcrumbs;
    private List<Filter> filterList;
    private String currentCategory;


    public DynamicFilterPresenterImpl(DynamicFilterView dynamicFilterView) {
        view = new WeakReference<DynamicFilterView>(dynamicFilterView);
    }

    @Override
    public void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which activity should be moved after login process done
            List<Breadcrumb> bcr = Parcels.unwrap(
                    intent.getParcelableExtra(EXTRA_PRODUCT_BREADCRUMB_LIST));
            List<Filter> filterList = Parcels.unwrap(
                    intent.getParcelableExtra(EXTRA_FILTER_CATEGORY_LIST));

            currentCategory = intent.getStringExtra(EXTRA_CURRENT_CATEGORY);

            if (bcr != null) {
                breadcrumbs = bcr;
            }
            if (filterList != null) {
                this.filterList = filterList;
            }
            if (!intent.getStringExtra(EXTRA_FILTER_SOURCE).equals("search_shop") &&
                    !filterList.get(0).getTitle().equals(Filter.TITLE_CATEGORY) && !(breadcrumbs==null)) {

                filterList.add(0, Filter.createCategory());
            }
            view.get().setFragmentForFirstTime3(filterList);
        }
    }

    @Override
    public List<Breadcrumb> getBreadCrumb() {
        return breadcrumbs;
    }

    @Override
    public List<Filter> getFilterCategory() {
        return filterList;
    }

    @Override
    public String getCurrentCategory() {
        return currentCategory;
    }
}
