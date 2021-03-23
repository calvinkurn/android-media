package com.tokopedia.digital_deals.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.digital_deals.view.contractor.CheckoutDealContractor;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.PackageViewModel;
import com.tokopedia.digital_deals.view.model.cart.CartItem;
import com.tokopedia.digital_deals.view.model.cart.CartItems;
import com.tokopedia.digital_deals.view.model.cart.Configuration;
import com.tokopedia.digital_deals.view.model.cart.MetaData;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.oms.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.oms.scrooge.ScroogePGUtil;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment.COUPON_EXTRA_IS_USE;
import static com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment.EXTRA_KUPON_CODE;
import static com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment.EXTRA_PROMO_CODE;


public class CheckoutDealPresenter
        extends BaseDaggerPresenter<CheckoutDealContractor.View>
        implements CheckoutDealContractor.Presenter {

    private PostPaymentUseCase postPaymentUseCase;
    private String promocode = "";
    private RequestParams paymentparams;
    private String INVALID_EMAIL = "Invalid Email";
    public static String EXTRA_DEALDETAIL = "EXTRA_DEALDETAIL";
    public static String EXTRA_CART = "EXTRA_CART";
    public static String EXTRA_PACKAGEVIEWMODEL = "EXTRA_PACKAGEVIEWMODEL";
    private String email;
    private DealsDetailsResponse dealDetail;
    private PackageViewModel packageViewModel;
    private String cartData;

    @Inject
    public CheckoutDealPresenter(PostPaymentUseCase postPaymentUseCase) {
        this.postPaymentUseCase = postPaymentUseCase;
    }

    @Override
    public void onDestroy() {
        postPaymentUseCase.unsubscribe();
    }

    @Override
    public void updateEmail(String email) {
        this.email = email;
    }

    @Override
    public void updatePromoCode(String code) {
        this.promocode = code;
    }


    @Override
    public void clickGoToPromo(Context context) {
        goToPromoCheckOutListDealsActivity(context);
    }

    @Override
    public void clickGoToDetailPromo(Context context, String couponCode) {
        goToPromoCheckoutDetailDealsActivity(context, couponCode);
    }

    @Override
    public void clickGotToListPromoApplied(Context context, String promoCode) {
        goToPromoCheckoutListDealsActivity(context, promoCode);
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

    private void goToPromoCheckoutDetailDealsActivity(Context context, String couponCode) {
        JsonObject requestBody = convertPackageToCartItem(packageViewModel);
        Intent dealsIntent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_DETAIL_DEALS);
        dealsIntent.putExtra(com.tokopedia.oms.view.utils.Utils.Constants.CHECKOUTDATA, requestBody.toString());
        dealsIntent.putExtra(COUPON_EXTRA_IS_USE, true);
        dealsIntent.putExtra(EXTRA_KUPON_CODE, couponCode);
        getView().navigateToActivityRequest(dealsIntent, CheckoutHomeFragment.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    private void goToPromoCheckoutListDealsActivity(Context context, String promoCode) {
        JsonObject requestBody = convertPackageToCartItem(packageViewModel);
        Intent dealsIntent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS);
        dealsIntent.putExtra(com.tokopedia.oms.view.utils.Utils.Constants.CHECKOUTDATA, requestBody.toString());
        dealsIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PRODUCTID, packageViewModel.getDigitalProductID());
        dealsIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORYID, packageViewModel.getDigitalCategoryID());
        dealsIntent.putExtra(EXTRA_PROMO_CODE, promoCode);
        getView().navigateToActivityRequest(dealsIntent, CheckoutHomeFragment.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    private void goToPromoCheckOutListDealsActivity(Context context) {
        JsonObject requestBody = convertPackageToCartItem(packageViewModel);
        Intent dealsIntent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS);
        dealsIntent.putExtra(com.tokopedia.oms.view.utils.Utils.Constants.CHECKOUTDATA, requestBody.toString());
        dealsIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PRODUCTID, packageViewModel.getDigitalProductID());
        dealsIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORYID, packageViewModel.getDigitalCategoryID());
        getView().navigateToActivityRequest(dealsIntent, CheckoutHomeFragment.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    public void getCheckoutDetails() {
        UserSession userSession = new UserSession(getView().getActivity());
        Intent intent = getView().getActivity().getIntent();
        this.dealDetail = intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_DEALDETAIL);
        this.cartData = intent.getStringExtra(CheckoutDealPresenter.EXTRA_CART);
        this.packageViewModel = intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_PACKAGEVIEWMODEL);
        getView().renderFromDetails(dealDetail, packageViewModel);
        getView().setEmailIDPhoneNumber(userSession.getEmail(), userSession.getPhoneNumber());
    }

    private JsonObject convertCartItemToJson(String cart) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(cart, JsonObject.class);

        if (!(jsonObject.get("error_code") == null))
            return null;
        for (JsonElement jsonElement : jsonObject.get("cart_items").getAsJsonArray()) {
            jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                    .getAsJsonObject().addProperty("email", this.email);
            List<Outlet> outlets = dealDetail.getOutlets();
            if (outlets != null && outlets.size() != 0) {
                if (outlets.size() > 1) {
                    jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                            .getAsJsonObject().addProperty("name",
                            String.format(getView().getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.text_available_locations), outlets.size()));
                } else if (outlets.get(0) != null)
                    if (!TextUtils.isEmpty(outlets.get(0).getDistrict()))
                        jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                                .getAsJsonObject().addProperty("name", outlets.get(0).getDistrict());
                    else
                        jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                                .getAsJsonObject().addProperty("name", "");

            } else {
                jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().get("entity_address")
                        .getAsJsonObject().addProperty("name", "");
            }
            if (dealDetail.getBrand() != null && !TextUtils.isEmpty(dealDetail.getBrand().getTitle()))
                jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().addProperty("entity_brand_name", dealDetail.getBrand().getTitle());
            else
                jsonElement.getAsJsonObject().get("meta_data").getAsJsonObject().addProperty("entity_brand_name", "");
        }
        jsonObject.addProperty("promocode", promocode);
        jsonObject.addProperty("order_title", dealDetail.getDisplayName());
        return jsonObject;
    }

    public void getPaymentLink() {
        paymentparams = RequestParams.create();
        try {
            JsonObject jsonObject = convertCartItemToJson(cartData);
            if (jsonObject == null) {
                getView().showFailureMessage(getView().getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.product_expired));
                return;
            } else
                paymentparams.putObject(Utils.Constants.CHECKOUTDATA, jsonObject);
        } catch (Exception e) {
            NetworkErrorHelper.showEmptyState(getView().getActivity(),
                    getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPaymentLink();
                        }
                    });
        }
        getView().showProgressBar();
        postPaymentUseCase.execute(paymentparams, new Subscriber<JsonObject>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                getView().hideProgressBar();
                if (throwable.getMessage() != null && throwable.getMessage().equalsIgnoreCase(INVALID_EMAIL))
                    getView().showMessage(getView().getActivity().getString(com.tokopedia.digital_deals.R.string.please_enter_email));
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
                if (checkoutResponse.get("url") == null && checkoutResponse.get("error") != null) {
                    getView().showFailureMessage(checkoutResponse.get("error").getAsString());
                } else {
                    String paymentURL = checkoutResponse.get("url").getAsString();
                    ScroogePGUtil.openScroogePage(getView().getActivity(), paymentURL, true, paymentData, getView().getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.deal_payment));
                }
                getView().hideProgressBar();

            }
        });
    }

    public void updateAmount(long discountAmount) {
        getView().updateAmount(com.tokopedia.digital_deals.view.utils.Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity() +
                packageViewModel.getCommission() - discountAmount));

    }

    public List<Outlet> getOutlets() {
        return dealDetail.getOutlets();
    }
}
