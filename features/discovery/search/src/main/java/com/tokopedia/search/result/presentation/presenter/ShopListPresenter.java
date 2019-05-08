package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetShopUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.subscriber.ToggleFavoriteActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.view.listener.ShopListLoadMoreListener;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import javax.inject.Inject;

final class ShopListPresenter
//        extends SearchSectionPresenter<ShopListSectionContract.View>
//        implements ShopListSectionContract.Presenter
{

//    @Inject
//    GetShopUseCase getShopUseCase;
//    @Inject
//    GetDynamicFilterUseCase getDynamicFilterUseCase;
//    @Inject
//    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
//    @Inject
//    UserSessionInterface userSession;
//
//    private FavoriteActionListener favoriteActionListener;
//
//    @Override
//    public void attachView(ShopListSectionContract.View view, FavoriteActionListener favoriteActionListener) {
//        attachView(view);
//        this.favoriteActionListener = favoriteActionListener;
//
//        //TODO:: Inject dagger here
//    }
//
//    @Override
//    public void handleFavoriteButtonClicked(ShopViewModel.ShopItem shopItem, int adapterPosition) {
//        if (getView().isUserHasLogin()) {
//            getView().disableFavoriteButton(adapterPosition);
//            getView().logDebug(this.toString(),
//                    "Toggle favorite " + shopItem.getShopId() + " " + Boolean.toString(!shopItem.isFavorited()));
//            toggleFavouriteShopUseCase.execute(
//                    ToggleFavouriteShopUseCase.createRequestParam(shopItem.getShopId()),
//                    new ToggleFavoriteActionSubscriber(favoriteActionListener, adapterPosition, !shopItem.isFavorited())
//            );
//        } else {
//            getView().launchLoginActivity(shopItem.getShopId());
//        }
//    }
//
//    /*
//    *
//    * private void launchLoginActivity(String shopId) {
//        Bundle extras = new Bundle();
//        extras.putString("shop_id", shopId);
//        viewListener.launchLoginActivity(extras);
//        }
//    */
//
//    @Override
//    public void loadShop(Map<String, Object> searchParameter, ShopListLoadMoreListener loadMoreListener) {
//
//    }
//
//    @Override
//    protected RequestParams getDynamicFilterParam() {
//        return null;
//    }
//
//    @Override
//    protected void getFilterFromNetwork(RequestParams requestParams) {
////        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
//    }
}
