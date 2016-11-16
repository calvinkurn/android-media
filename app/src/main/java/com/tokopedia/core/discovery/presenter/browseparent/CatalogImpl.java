package com.tokopedia.core.discovery.presenter.browseparent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.core.discovery.fragment.browseparent.CatalogFragment;
import com.tokopedia.core.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.core.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.core.discovery.interfaces.DiscoveryListener;
import com.tokopedia.core.discovery.model.BrowseCatalogModel;
import com.tokopedia.core.discovery.model.ErrorContainer;
import com.tokopedia.core.discovery.model.NetworkParam;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.core.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.core.discovery.view.CatalogView;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;
import com.tokopedia.core.util.PagingHandler;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public class CatalogImpl extends Catalog implements DiscoveryListener {

    private DiscoveryInteractor discoveryInteractor;
    private NetworkParam.Catalog catalog;
    private BrowseCatalogModel catalogModel;

    private DiscoveryActivityPresenter activityPresenter;
    private int index;

    public CatalogImpl(CatalogView view) {
        super(view);
    }

    @Override
    public void callNetwork(DiscoveryActivityPresenter discoveryActivityPresenter) {
        // jika datanya kosong, maka itu dianggap first time.
        this.activityPresenter = discoveryActivityPresenter;
        if (view.getDataSize() <= 0) {

            catalog = new NetworkParam.Catalog();
            catalog.start = 0;
            catalog.q = discoveryActivityPresenter.getProductParam().q;
            catalog.sc = discoveryActivityPresenter.getProductParam().sc;
            catalog.id = discoveryActivityPresenter.getProductParam().id;
            catalog.ob = discoveryActivityPresenter.getProductParam().obCatalog;
            catalog.pmin = discoveryActivityPresenter.getProductParam().pmin;
            catalog.pmax = discoveryActivityPresenter.getProductParam().pmax;
            catalog.terms = discoveryActivityPresenter.getProductParam().terms;// klo terms itu, filter kaya brand, screen dll. kepake d directory product
            catalog.breadcrumb = discoveryActivityPresenter.getProductParam().breadcrumb;
            catalog.extraFilter = discoveryActivityPresenter.getProductParam().extraFilter;
            discoveryInteractor.getCatalogs(NetworkParam.generateCatalogQuery(catalog));
        }
    }

    @Override
    public void loadMore(Context context) {
        int startIndexForQuery = view.getStartIndexForQuery(TAG);
        if (catalog == null)
            return;

        catalog.start = startIndexForQuery;
        discoveryInteractor.getCatalogs(NetworkParam.generateCatalogQuery(catalog));
    }


    @Override
    public void initData(@NonNull Context context) {
        if (!isAfterRotate) {
            view.setupRecyclerView();
        }
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
    }

    @Override
    public void fetchArguments(Bundle argument) {
        index = argument.getInt(CatalogFragment.INDEX, 0);
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {
            view.initAdapter();
        }
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        view.ariseRetry(type, ((ErrorContainer) data.getModel2()).body().getMessage());

    }

    private Pair<List<BrowseCatalogAdapter.CatalogModel>, PagingHandler.PagingHandlerModel> parseBrowseCategoryModel(BrowseCatalogModel browseCatalogModel) {
        List<BrowseCatalogAdapter.CatalogModel> catalogItems = BrowseCatalogModel.Catalogs.toCatalogItemList(browseCatalogModel.result.catalogs);

        String uriNext = browseCatalogModel.result.paging.getUriNext();
        String uriPrevious = browseCatalogModel.result.paging.getUriPrevious();

        PagingHandler.PagingHandlerModel pagingHandlerModel = FragmentDiscoveryPresenterImpl.getPagingHandlerModel(uriNext, uriPrevious);

        return new Pair<>(catalogItems, pagingHandlerModel);
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch (type) {
            case DiscoveryListener.BROWSE_CATALOG:
                Log.i(TAG, getMessageTAG() + " fetch catalog shop " + data.getModel1());
                BrowseCatalogModel.BrowseCatalogContainer browseCatalogContainer = (BrowseCatalogModel.BrowseCatalogContainer) data.getModel2();
                catalogModel = browseCatalogContainer.body();

                Log.d(TAG, getMessageTAG() + catalogModel);
                if (catalogModel == null)
                    return;

                Pair<List<BrowseCatalogAdapter.CatalogModel>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseCategoryModel(catalogModel);
                view.notifyChangeData(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                fetchDynamicAttribut();
                break;
            case DiscoveryListener.DYNAMIC_ATTRIBUTE:
                DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();
                view.setDynamicFilterAtrribute(dynamicFilterContainer.body().getData(), index);
                break;
            default:

                break;
        }
    }

    public BrowseCatalogModel getCatalogModel() {
        return catalogModel;
    }

    @Override
    public void fetchDynamicAttribut() {
        if (activityPresenter.checkHasFilterAttrIsNull(index)) {
            discoveryInteractor.getDynamicAttribute(view.getContext(), DynamicFilterPresenter.SEARCH_CATALOG, activityPresenter.getBrowseProductActivityModel().getDepartmentId());
        }
    }
}
