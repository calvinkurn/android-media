package com.tokopedia.discovery.categorynav;

import android.content.Context;

import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.entity.categories.Child;
import com.tokopedia.core.util.Pair;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;

import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationPresenterImpl implements CategoryNavigationPresenter, DiscoveryListener {

    Context context;
    DiscoveryInteractor discoveryInteractor;

    public CategoryNavigationPresenterImpl(Context context, CategoryNavigationView view) {
        this.context = context;
        discoveryInteractor = new DiscoveryInteractorImpl();
        super(view);
    }

    @Override
    public void getRootCategory(String departementId) {
        discoveryInteractor.getRootCategory(departementId);
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {

    }
}
