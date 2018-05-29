package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.data.entity.response.verifyresponse.Cart;
import com.tokopedia.oms.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;


public class CheckoutDealPresenter
        extends BaseDaggerPresenter<CheckoutDealContractor.View>
        implements CheckoutDealContractor.Presenter {

    private ProfileUseCase profileUseCase;
    private PostPaymentUseCase postPaymentUseCase;
    private String promocode;
    private boolean isPromoCodeCase;
    private ArrayList<String> hints = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
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
                Log.d("ProfileUseCase", "ON ERROR");
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


    private void goToLoyaltyActivity() {
//        JsonObject requestBody = convertPackageToCartItem(checkoutData);
//        Intent loyaltyIntent = LoyaltyActivity.newInstanceCouponActive(getView().getActivity(), Utils.Constants.DEALS, Utils.Constants.DEALS);
//        loyaltyIntent.putExtra(com.tokopedia.oms.view.utils.Utils.Constants.CHECKOUTDATA, requestBody.toString());
//        loyaltyIntent.putExtra(LoyaltyActivity.EXTRA_PRODUCTID, checkoutData.getDigitalProductID());
//        loyaltyIntent.putExtra(LoyaltyActivity.EXTRA_CATEGORYID, checkoutData.getDigitalCategoryID());
//        getView().navigateToActivityRequest(loyaltyIntent, LoyaltyActivity.LOYALTY_REQUEST_CODE);
    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }

//    private JsonObject convertPackageToCartItem(PackageViewModel packageViewModel) {
//        Configuration config = new Configuration();
//        config.setPrice(packageViewModel.getSalesPrice() * checkoutData.getSelectedQuantity());
//        com.tokopedia.events.domain.model.request.cart.SubConfig sub = new com.tokopedia.events.domain.model.request.cart.SubConfig();
//        sub.setName(profileModel.getProfileData().getUserInfo().getUserName());
//        config.setSubConfig(sub);
//        MetaData meta = new MetaData();
//        meta.setEntityCategoryId(packageViewModel.getCategoryId());
//        meta.setEntityCategoryName("");
//        meta.setEntityGroupId(packageViewModel.getProductGroupId());
//        List<EntityPackageItem> entityPackages = new ArrayList<>();
//        EntityPackageItem packageItem = new EntityPackageItem();
//        packageItem.setPackageId(packageViewModel.getId());
//        if (selectedSeatViewModel != null) {
//            packageItem.setAreaCode(selectedSeatViewModel.getAreaCodes());
//            packageItem.setSeatId(selectedSeatViewModel.getSeatIds());
//            packageItem.setSeatRowId(selectedSeatViewModel.getSeatRowIds());
//            packageItem.setSeatPhysicalRowId(selectedSeatViewModel.getPhysicalRowIds());
//            packageItem.setQuantity(selectedSeatViewModel.getQuantity());
//            packageItem.setPricePerSeat(selectedSeatViewModel.getPrice());
//            packageItem.setAreaId(selectedSeatViewModel.getAreaId());
//            packageItem.setActualSeatNos(selectedSeatViewModel.getActualSeatNos());
//        } else {
//            packageItem.setAreaCode(new ArrayList<String>());
//            packageItem.setSeatId(new ArrayList<String>());
//            packageItem.setAreaId("");
//            packageItem.setSeatRowId(new ArrayList<String>());
//            packageItem.setSeatPhysicalRowId(new ArrayList<String>());
//            packageItem.setQuantity(packageViewModel.getSelectedQuantity());
//            packageItem.setPricePerSeat(packageViewModel.getSalesPrice());
//            packageItem.setActualSeatNos(new ArrayList<String>());
//        }
//        packageItem.setDescription(packageViewModel.getDescription());
//
//        packageItem.setSessionId("");
//        packageItem.setProductId(packageViewModel.getProductId());
//        packageItem.setGroupId(packageViewModel.getProductGroupId());
//        packageItem.setScheduleId(packageViewModel.getProductScheduleId());
//        entityPackages.add(packageItem);
//        meta.setEntityPackages(entityPackages);
//        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
//        meta.setEntityProductId(packageViewModel.getProductId());
//        meta.setEntityScheduleId(packageViewModel.getProductScheduleId());
//        List<EntityPassengerItem> passengerItems = new ArrayList<>();
//
//        if (packageViewModel.getForms() != null) {
//            for (Form form : packageViewModel.getForms()) {
//                EntityPassengerItem passenger = new EntityPassengerItem();
//                passenger.setId(form.getId());
//                passenger.setProductId(form.getProductId());
//                passenger.setName(form.getName());
//                passenger.setTitle(form.getTitle());
//                passenger.setValue(form.getValue());
//                passenger.setElementType(form.getElementType());
//                passenger.setRequired(String.valueOf(form.getRequired()));
//                passenger.setValidatorRegex(form.getValidatorRegex());
//                passenger.setErrorMessage(form.getErrorMessage());
//                passengerItems.add(passenger);
//            }
//        }
//
//        meta.setEntityPassengers(passengerItems);
//        EntityAddress address = new EntityAddress();
//        address.setAddress("");
//        address.setName(profileModel.getProfileData().getUserInfo().getUserName());
//        address.setCity("");
//        address.setEmail(this.email);
//        address.setMobile(this.number);
//        address.setLatitude("");
//        address.setLongitude("");
//        meta.setEntityAddress(address);
//        meta.setCitySearched("");
//        meta.setEntityEndTime("");
//        meta.setEntityStartTime("");
//        meta.setTotalTaxAmount(0);
//        meta.setTotalOtherCharges(0);
//        meta.setTotalTicketPrice(packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice());
//        meta.setEntityImage("");
//        List<OtherChargesItem> otherChargesItems = new ArrayList<>();
//        OtherChargesItem otherCharges = new OtherChargesItem();
//        otherCharges.setConvFee(packageViewModel.getConvenienceFee());
//        otherChargesItems.add(otherCharges);
//        meta.setOtherCharges(otherChargesItems);
//        List<TaxPerQuantityItem> taxPerQuantityItems = new ArrayList<>();
//        meta.setTaxPerQuantity(taxPerQuantityItems);
//        List<CartItem> cartItems = new ArrayList<>();
//        CartItem cartItem = new CartItem();
//        cartItem.setMetaData(meta);
//        cartItem.setConfiguration(config);
//        cartItem.setQuantity(packageViewModel.getSelectedQuantity());
//        cartItem.setProductId(packageViewModel.getProductId());
//
//
//        cartItems.add(cartItem);
//        CartItems cart = new CartItems();
//        cart.setCartItems(cartItems);
//        cart.setPromocode(promocode);
//
//        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
//        JsonObject requestBody = jsonElement.getAsJsonObject();
//        return requestBody;
//    }

    @Override
    public void attachView(CheckoutDealContractor.View view) {
        super.attachView(view);
        getView().showProgressBar();
        Intent intent = view.getActivity().getIntent();
        this.dealDetail = intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_DEALDETAIL);
        this.cartData = intent.getStringExtra(CheckoutDealPresenter.EXTRA_CART);
        this.packageViewModel=intent.getParcelableExtra(CheckoutDealPresenter.EXTRA_PACKAGEVIEWMODEL);
        getView().renderFromPackageVM(dealDetail, packageViewModel);
    }

//    private JsonObject convertCartItemToJson(Cart cart) {
//        cart.getUser().setEmail(email);
//        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
//        return jsonElement.getAsJsonObject();
//    }

    private JsonObject convertCartItemToJson(String cart) {
        Gson gson = new Gson();
        return gson.fromJson(cart, JsonObject.class);
    }


    public void getPaymentLink() {


        paymentparams.putObject(Utils.Constants.CHECKOUTDATA, convertCartItemToJson(cartData));
        postPaymentUseCase.execute(paymentparams, new Subscriber<CheckoutResponse>() {
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
            public void onNext(CheckoutResponse checkoutResponse) {

                Log.d("CheckoutResponse"," "+ checkoutResponse.toString());

//                com.tokopedia.payment.model.PaymentPassData paymentPassData = new com.tokopedia.payment.model.PaymentPassData();
//                paymentPassData.setQueryString(checkoutResponse.getQueryString());
//                paymentPassData.setRedirectUrl(checkoutResponse.getRedirectUrl());
//                paymentPassData.setCallbackSuccessUrl(checkoutResponse.getCallbackUrlSuccess());
//                paymentPassData.setCallbackFailedUrl(checkoutResponse.getCallbackUrlFailed());
//                paymentPassData.setTransactionId(checkoutResponse.getParameter().getTransactionId());
//                UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PAYMENT, checkoutData.getTitle() + " - "
//                        + checkoutData.getDisplayName() + " - " + checkoutData.getSalesPrice() + " - " + promocode);
//                getView().navigateToActivityRequest(com.tokopedia.payment.activity.TopPayActivity.
//                                createInstance(getView().getActivity().getApplicationContext(), paymentPassData),
//                        PAYMENT_REQUEST_CODE);
                getView().hideProgressBar();

            }
        });
    }


}
