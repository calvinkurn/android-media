package com.tokopedia.shop.sort.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase;
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper;
import com.tokopedia.shop.sort.view.model.ShopProductSortModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortPresenter extends BaseDaggerPresenter<BaseListViewListener<ShopProductSortModel>> {

    private final GetShopProductSortUseCase getShopProductFilterUseCase;
    private final ShopProductSortMapper shopProductFilterMapper;
    private final UserSessionInterface userSession;

    @Inject
    public ShopProductSortPresenter(GetShopProductSortUseCase getShopProductFilterUseCase, ShopProductSortMapper shopProductMapper, UserSessionInterface userSession) {
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
        this.shopProductFilterMapper = shopProductMapper;
        this.userSession = userSession;
    }

    public void getShopFilterList() {
        getShopProductFilterUseCase.execute(new Subscriber<List<ShopProductSort>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<ShopProductSort> dataValue) {
                getView().renderList(shopProductFilterMapper.convertSort(dataValue));
            }
        });
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductFilterUseCase.unsubscribe();
    }
}