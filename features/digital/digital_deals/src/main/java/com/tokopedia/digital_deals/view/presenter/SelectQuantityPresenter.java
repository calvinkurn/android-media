package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.contractor.SelectQuantityContract;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.model.request.cart.CartItem;
import com.tokopedia.oms.domain.model.request.cart.CartItems;
import com.tokopedia.oms.domain.model.request.cart.Configuration;
import com.tokopedia.oms.domain.model.request.cart.MetaData;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class SelectQuantityPresenter
        extends BaseDaggerPresenter<SelectQuantityContract.View>
        implements SelectQuantityContract.Presenter {

    private PostVerifyCartUseCase postVerifyCartUseCase;
    private String promocode="";
    private PackageViewModel checkoutData;
    private DealsDetailsViewModel dealDetails;


    @Inject
    public SelectQuantityPresenter(PostVerifyCartUseCase postVerifyCartUseCase) {
        this.postVerifyCartUseCase = postVerifyCartUseCase;
    }

    @Override
    public void initialize(DealsDetailsViewModel detailsViewModel) {
        this.dealDetails=detailsViewModel;
        getView().renderFromDetails(dealDetails);
    }

    @Override
    public void onDestroy() {
        postVerifyCartUseCase.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == getView().getRequestCode()) {
            if (SessionHandler.isV4Login(getView().getActivity())) {
                getProfile();
            } else {
                getView().hideProgressBar();
            }
        }
    }


    private JsonObject convertPackageToCartItem(PackageViewModel packageViewModel) {
        Configuration config = new Configuration();
        config.setPrice(packageViewModel.getSalesPrice());
        MetaData meta = new MetaData();
        meta.setEntityCategoryId(packageViewModel.getCategoryId());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setTotalTicketPrice(packageViewModel.getSalesPrice());


        meta.setEntityStartTime("");


        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setMetaData(meta);
        cartItem.setConfiguration(config);
        cartItem.setQuantity(packageViewModel.getSelectedQuantity());
        cartItem.setProductId(packageViewModel.getDigitalProductID());


        cartItems.add(cartItem);
        CartItems cart = new CartItems();
        cart.setCartItems(cartItems);
        cart.setPromocode(promocode);

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return requestBody;
    }


    private void getProfile() {
        if (!SessionHandler.isV4Login(getView().getActivity())) {
            Intent intent = ((TkpdCoreRouter) getView().getActivity().getApplication()).
                    getLoginIntent(getView().getActivity());
            getView().navigateToActivityRequest(intent, getView().getRequestCode());
        } else {
            verifyMyCart();
        }

    }

    public void verifyCart(PackageViewModel checkoutData) {
        this.checkoutData=checkoutData;
        getProfile();

    }

    public void verifyMyCart(){
        getView().showProgressBar();
        final RequestParams params = RequestParams.create();
        params.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(checkoutData));
        postVerifyCartUseCase.execute(params, new Subscriber<VerifyMyCartResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ReviewTicketPresenter", "onError");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                verifyCart(checkoutData);
                            }
                        });
            }

            @Override
            public void onNext(VerifyMyCartResponse verifyCartResponse) {
                Log.d("ReviewTicketPresenter", verifyCartResponse.toString());


                Intent intent=new Intent(getView().getActivity(), CheckoutActivity.class);
                intent.putExtra(CheckoutDealPresenter.EXTRA_PACKAGEVIEWMODEL, checkoutData);
                intent.putExtra(CheckoutDealPresenter.EXTRA_CART, verifyCartResponse.getCart().toString());
                intent.putExtra(CheckoutDealPresenter.EXTRA_DEALDETAIL, dealDetails);
                getView().navigateToActivity(intent);
                getView().hideProgressBar();


            }
        });
    }



}
