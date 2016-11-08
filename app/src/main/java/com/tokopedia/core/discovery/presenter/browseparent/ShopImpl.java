package com.tokopedia.core.discovery.presenter.browseparent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.core.discovery.interfaces.DiscoveryListener;
import com.tokopedia.core.discovery.model.NetworkParam;
import com.tokopedia.core.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.core.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.core.discovery.model.BrowseShopModel;
import com.tokopedia.core.discovery.model.ErrorContainer;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.core.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.core.discovery.view.ShopView;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;
import com.tokopedia.core.util.PagingHandler;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by Erry on 6/30/2016.
 */
public class ShopImpl extends Shop implements DiscoveryListener {
    DiscoveryInteractor discoveryInteractor;
    WeakReference<Context> context;
    NetworkParam.Shop shop;

    public ShopImpl(ShopView view) {
        super(view);
    }

    @Override
    public void callNetwork(DiscoveryActivityPresenter discoveryActivityPresenter) {
        // jika datanya kosong, maka itu dianggap first time.
        Log.d(TAG, "callNetwork "+view.getDataSize());
        if(view.getDataSize()<=0) {
            shop = new NetworkParam.Shop();
            shop.floc = discoveryActivityPresenter.getProductParam().floc;
            shop.q = discoveryActivityPresenter.getProductParam().q;
            shop.fshop = discoveryActivityPresenter.getProductParam().fshop;
            shop.start = 0;
            shop.extraFilter = discoveryActivityPresenter.getProductParam().extraFilter;
            discoveryInteractor.getShops(NetworkParam.generateShopQuery(shop));
            view.setLoading(true);
        }
    }

    @Override
    public void loadMore(Context context) {
        int startIndexForQuery = view.getStartIndexForQuery(TAG);
        if(shop == null)
            return;

        shop.start = startIndexForQuery;
        discoveryInteractor.getShops(NetworkParam.generateShopQuery(shop));
    }

    @Override
    public void initData(@NonNull Context context) {
        if(!isAfterRotate) {
            view.setupRecyclerView();
        }
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
    }

    @Override
    public void fetchArguments(Bundle argument) {

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
        if(!isAfterRotate) {
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
        view.ariseRetry(type, ((ErrorContainer)data.getModel2()).body().getMessage());
    }

    private Pair<List<BrowseShopAdapter.ShopModel>, PagingHandler.PagingHandlerModel> parseBrowseShopModel(BrowseShopModel browseShopModel) {
        List<BrowseShopAdapter.ShopModel> shopItems = BrowseShopModel.Shops.toShopItemList(browseShopModel.result.shops);

        String uriNext = browseShopModel.result.paging.getUriNext();
        String uriPrevious = browseShopModel.result.paging.getUriPrevious();

        PagingHandler.PagingHandlerModel pagingHandlerModel = FragmentDiscoveryPresenterImpl.getPagingHandlerModel(uriNext, uriPrevious);

        return new Pair<>(shopItems, pagingHandlerModel);
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch(type){
            case DiscoveryListener.BROWSE_SHOP:
                Log.i(TAG, getMessageTAG()+" fetch browse shop "+ data.getModel1());
                BrowseShopModel.BrowseShopContainer browseShopContainer
                        = (BrowseShopModel.BrowseShopContainer)data.getModel2();
                BrowseShopModel browseShopModel = browseShopContainer.body();

                Log.d(TAG, getMessageTAG()+browseShopModel);
                if(browseShopModel==null)
                    return;

                Pair<List<BrowseShopAdapter.ShopModel>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseShopModel(browseShopModel);
                view.setLoading(false);
                view.onCallProductServiceLoadMore(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                break;
            default:

                break;
        }
    }
}
