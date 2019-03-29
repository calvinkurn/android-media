package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.contractor.SelectQuantityContract;
import com.tokopedia.digital_deals.view.model.PackageViewModel;
import com.tokopedia.digital_deals.view.model.cart.CartItem;
import com.tokopedia.digital_deals.view.model.cart.CartItems;
import com.tokopedia.digital_deals.view.model.cart.Configuration;
import com.tokopedia.digital_deals.view.model.cart.MetaData;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class SelectQuantityPresenter
        extends BaseDaggerPresenter<SelectQuantityContract.View>
        implements SelectQuantityContract.Presenter {

    private PostVerifyCartUseCase postVerifyCartUseCase;
    private String promocode = "";
    private PackageViewModel checkoutData;
    private DealsDetailsResponse dealDetails;
    private UserSessionInterface userSession;

    @Inject
    public SelectQuantityPresenter(PostVerifyCartUseCase postVerifyCartUseCase) {
        this.postVerifyCartUseCase = postVerifyCartUseCase;
    }

    @Override
    public void initialize(DealsDetailsResponse detailsViewModel) {
        userSession = new UserSession(getView().getActivity());
        this.dealDetails = detailsViewModel;
        getView().renderFromDetails(dealDetails);
    }

    @Override
    public void onDestroy() {
        postVerifyCartUseCase.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == getView().getRequestCode()) {

            if (userSession.isLoggedIn()) {
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
        if (!userSession.isLoggedIn()) {
            Intent intent = ((DealsModuleRouter) getView().getActivity().getApplication()).
                    getLoginIntent(getView().getActivity());
            getView().navigateToActivityRequest(intent, getView().getRequestCode());
        } else {
            verifyMyCart();
        }
    }

    public void verifyCart(PackageViewModel checkoutData) {
        this.checkoutData = checkoutData;
        getProfile();

    }

    public void verifyMyCart() {
        getView().showProgressBar();
        final RequestParams params = RequestParams.create();
        params.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(checkoutData));
        postVerifyCartUseCase.execute(params, new Subscriber<VerifyMyCartResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
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
                getView().hideProgressBar();
                try {
                    if (verifyCartResponse.getStatus().getResult().equalsIgnoreCase("failure")) {

                        for (JsonElement jsonElement : verifyCartResponse.getCart().get("cart_items").getAsJsonArray()) {
                            if (!TextUtils.isEmpty(jsonElement.getAsJsonObject().get("error").getAsString())) {
                                getView().showFailureMessage(jsonElement.getAsJsonObject().get("error").getAsString());
                            }
                        }
                    } else {
                        Intent intent = new Intent(getView().getActivity(), CheckoutActivity.class);
                        intent.putExtra(CheckoutDealPresenter.EXTRA_PACKAGEVIEWMODEL, checkoutData);
                        intent.putExtra(CheckoutDealPresenter.EXTRA_CART, verifyCartResponse.getCart().toString());
                        intent.putExtra(CheckoutDealPresenter.EXTRA_DEALDETAIL, dealDetails);
                        getView().navigateToActivity(intent);
                    }
                } catch (Exception e) {

                }

            }
        });
    }


}
