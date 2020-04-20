package com.tokopedia.shop.address.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.address.view.listener.ShopAddressListView;
import com.tokopedia.shop.address.view.mapper.ShopAddressViewModelMapper;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import javax.inject.Inject;

import kotlin.Unit;
import timber.log.Timber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopAddressListPresenter extends BaseDaggerPresenter<ShopAddressListView> {

    private final GQLGetShopInfoUseCase gqlGetShopInfoUseCase;
    private final ShopAddressViewModelMapper shopAddressViewModelMapper;
    private final UserSessionInterface userSession;

    @Inject
    public ShopAddressListPresenter(GQLGetShopInfoUseCase getShopInfoUseCase, ShopAddressViewModelMapper shopAddressViewModelMapper, UserSessionInterface userSession) {
        this.gqlGetShopInfoUseCase = getShopInfoUseCase;
        this.shopAddressViewModelMapper = shopAddressViewModelMapper;
        this.userSession = userSession;
    }

    public void getShopAddressList(String shopId) {
        ArrayList<Integer> shopIds = new ArrayList<>();
        try {
            shopIds.add(Integer.parseInt(shopId));
        }
        catch (NumberFormatException exception) {
            Timber.d("Failed to convert shop ID to integer");
        }
        gqlGetShopInfoUseCase.setParams(GQLGetShopInfoUseCase.createParams(shopIds, null , GQLGetShopInfoUseCase.getDefaultShopFields(),""));
        gqlGetShopInfoUseCase.execute(
                shopInfo -> {
                    if (isViewAttached()) {
                        getView().renderList(shopAddressViewModelMapper.transform(shopInfo), false);
                    }
                    return Unit.INSTANCE;
                },
                throwable -> {
                    if (isViewAttached()) {
                        getView().showGetListError(throwable);
                    }
                    return Unit.INSTANCE;
                }
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        gqlGetShopInfoUseCase.cancelJobs();
    }
}