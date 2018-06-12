package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.contractor.CheckoutDealContractor;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.oms.domain.model.request.cart.CartItem;
import com.tokopedia.oms.domain.model.request.cart.CartItems;
import com.tokopedia.oms.domain.model.request.cart.Configuration;
import com.tokopedia.oms.domain.model.request.cart.MetaData;
import com.tokopedia.oms.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.oms.scrooge.ScroogePGUtil;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class CheckoutDealPresenter
        extends BaseDaggerPresenter<CheckoutDealContractor.View>
        implements CheckoutDealContractor.Presenter {

    private ProfileUseCase profileUseCase;
    private PostPaymentUseCase postPaymentUseCase;
    private String promocode = "";
    private RequestParams paymentparams;
    private String INVALID_EMAIL = "Invalid Email";
    public static String EXTRA_DEALDETAIL = "EXTRA_DEALDETAIL";
    public static String EXTRA_CART = "EXTRA_CART";
    public static String EXTRA_PACKAGEVIEWMODEL = "EXTRA_PACKAGEVIEWMODEL";
    private ProfileModel profileModel;
    private String email;
    private DealsDetailsViewModel dealDetail;
    private PackageViewModel packageViewModel;
    private String cartData;

    @Inject
    public CheckoutDealPresenter(PostPaymentUseCase postPaymentUseCase, ProfileUseCase profileUseCase) {
        this.postPaymentUseCase = postPaymentUseCase;
        this.profileUseCase = profileUseCase;
    }

    @Override
    public void onDestroy() {
        postPaymentUseCase.unsubscribe();
        profileUseCase.unsubscribe();
    }

    @Override
    public void updateEmail(String email) {
        this.email = email;
    }

    @Override
    public void updatePromoCode(String code) {
        this.promocode = code;
        if (code.length() == 0) {
            getView().hideSuccessMessage();
        }
    }

    @Override
    public void getProfile() {
        getView().showProgressBar();

        profileUseCase.execute(com.tokopedia.core.base.domain.RequestParams.EMPTY, new Subscriber<ProfileModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                Intent intent = ((TkpdCoreRouter) getView().getActivity().getApplication()).
                        getLoginIntent(getView().getActivity());
                getView().getActivity().startActivity(intent);
                getView().hideProgressBar();
            }

            @Override
            public void onNext(ProfileModel model) {
                profileModel = model;
                email = profileModel.getProfileData().getUserInfo().getUserEmail();
                getView().setEmailID(profileModel.getProfileData().getUserInfo().getUserEmail());
                getView().hideProgressBar();
            }
        });
    }


    @Override
    public void clickGoToPromo() {
        getView().showProgressBar();
        goToLoyaltyActivity();
    }

    private JsonObject convertPackageToCartItem(PackageViewModel packageViewModel) {
        Configuration config = new Configuration();
        config.setPrice(packageViewModel.getSalesPrice() * packageViewModel.getSelectedQuantity());
        MetaData meta = new MetaData();
        meta.setEntityCategoryId(packageViewModel.getCategoryId());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setTotalTicketPrice(packageViewModel.getSalesPrice() * packageViewModel.getSelectedQuantity());
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


    private void goToLoyaltyActivity() {
        JsonObject requestBody = convertPackageToCartItem(packageViewModel);
        Intent loyaltyIntent = LoyaltyActivity.newInstanceCouponActive(getView().getActivity(), com.tokopedia.digital_deals.view.utils.Utils.Constants.DEALS, com.tokopedia.digital_deals.view.utils.Utils.Constants.DEALS);
        loyaltyIntent.putExtra(com.tokopedia.oms.view.utils.Utils.Constants.CHECKOUTDATA, requestBody.toString());
        loyaltyIntent.putExtra(LoyaltyActivity.EXTRA_PRODUCTID, packageViewModel.getDigitalProductID());
        loyaltyIntent.putExtra(LoyaltyActivity.EXTRA_CATEGORYID, packageViewModel.getDigitalCategoryID());
        getView().navigateToActivityRequest(loyaltyIntent, LoyaltyActivity.LOYALTY_REQUEST_CODE);
    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }

    public void getCheckoutDetails() {
        getView().showProgressBar();
        Intent intent = getView().getActivity().getIntent();
        this.dealDetail = intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_DEALDETAIL);
        this.cartData = intent.getStringExtra(CheckoutDealPresenter.EXTRA_CART);
        this.packageViewModel = intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_PACKAGEVIEWMODEL);
        getView().renderFromDetails(dealDetail, packageViewModel);
    }

    private JsonObject convertCartItemToJson(String cart) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(cart, JsonObject.class);

        for (JsonElement jsonElement: jsonObject.get("cart_items").getAsJsonArray()){
            jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                    .getAsJsonObject().addProperty("email", this.email);
        }
        return jsonObject;
    }

    public void getPaymentLink() {
        paymentparams = RequestParams.create();
        paymentparams.putObject(Utils.Constants.CHECKOUTDATA, convertCartItemToJson(cartData));
        postPaymentUseCase.execute(paymentparams, new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                getView().hideProgressBar();
                if (throwable.getMessage().equalsIgnoreCase(INVALID_EMAIL))
                    getView().showMessage(getView().getActivity().getString(R.string.please_enter_email));
                else {
                    NetworkErrorHelper.showEmptyState(getView().getActivity(),
                            getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPaymentLink();
                                }
                            });
                }
            }

            @Override
            public void onNext(JsonObject checkoutResponse) {
                String paymentData = Utils.transform(checkoutResponse);
                String paymentURL = checkoutResponse.get("url").getAsString();
                ScroogePGUtil.openScroogePage(getView().getActivity(), paymentURL, true, paymentData, "Deal Payment");
                getView().hideProgressBar();

            }
        });
    }
}
