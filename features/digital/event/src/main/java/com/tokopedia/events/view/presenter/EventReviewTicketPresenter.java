package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.data.entity.response.Form;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.Cart;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.model.CouponModel;
import com.tokopedia.events.domain.model.request.cart.CartItem;
import com.tokopedia.events.domain.model.request.cart.CartItems;
import com.tokopedia.events.domain.model.request.cart.Configuration;
import com.tokopedia.events.domain.model.request.cart.EntityAddress;
import com.tokopedia.events.domain.model.request.cart.EntityPackageItem;
import com.tokopedia.events.domain.model.request.cart.EntityPassengerItem;
import com.tokopedia.events.domain.model.request.cart.MetaData;
import com.tokopedia.events.domain.model.request.cart.OtherChargesItem;
import com.tokopedia.events.domain.model.request.cart.TaxPerQuantityItem;
import com.tokopedia.events.domain.postusecase.CheckoutPaymentUseCase;
import com.tokopedia.events.domain.postusecase.PostInitCouponUseCase;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.oms.scrooge.ScroogePGUtil;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;

import static com.tokopedia.events.view.activity.ReviewTicketActivity.PAYMENT_REQUEST_CODE;
import static com.tokopedia.events.view.utils.Utils.Constants.CHECKOUTDATA;
import static com.tokopedia.events.view.utils.Utils.Constants.EXTRA_VERIFY_RESPONSE;

/**
 * Created by pranaymohapatra on 29/11/17.
 */


public class EventReviewTicketPresenter
        extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventReviewTicketsContractor.EventReviewTicketPresenter {

    private EventsDetailsViewModel eventsDetailsViewModel;
    private PackageViewModel checkoutData;
    private VerifyCartUseCase verifyCartUseCase;
    private PostVerifyCartUseCase postVerifyCartUseCase;
    private CheckoutPaymentUseCase checkoutPaymentUseCase;
    private PostPaymentUseCase postPaymentUseCase;
    private PostInitCouponUseCase postInitCouponUseCase;
    private String promoCode = "";
    private String email;
    private String number;
    private boolean isBook;
    private JsonObject verifiedSeatResponse;
    private SelectedSeatViewModel selectedSeatViewModel;
    private ArrayList<String> hints = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
    private RequestParams paymentParams;
    private String INVALID_EMAIL = "Invalid Email";
    private EventReviewTicketsContractor.EventReviewTicketsView mView;
    private EventsAnalytics eventsAnalytics;

    public EventReviewTicketPresenter(VerifyCartUseCase usecase, CheckoutPaymentUseCase payment,
                                      PostInitCouponUseCase couponUseCase, PostVerifyCartUseCase postVerifyCartUseCase, PostPaymentUseCase postPaymentUseCase, EventsAnalytics eventsAnalytics) {
        this.verifyCartUseCase = usecase;
        this.checkoutPaymentUseCase = payment;
        this.postInitCouponUseCase = couponUseCase;
        this.postVerifyCartUseCase = postVerifyCartUseCase;
        this.postPaymentUseCase = postPaymentUseCase;
        this.eventsAnalytics = eventsAnalytics;
    }

    @Override
    public boolean onClickOptionMenu(int id) {
        mView.getActivity().onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void proceedToPayment() {
        if (mView.validateAllFields()) {
            //Check if Seating Event :: By pass Verify Call for Seating Events
            if (selectedSeatViewModel != null && verifiedSeatResponse != null) {
                isBook = false;
                JsonObject checkoutBody;
                checkoutBody = convertCartItemToJson(verifiedSeatResponse);
                paymentParams = RequestParams.create();
                paymentParams.putObject(CHECKOUTDATA, checkoutBody);
                getPaymentLink();
                //Non Seating Event :: Call Verify Book = True
            } else {
                isBook = true;
                verifyCart();
            }
        } else {
            mView.showSnackBar(mView.getViewResources().getString(R.string.enter_additional_data), false);
        }
    }

    @Override
    public void updatePromoCode(String code) {
        this.promoCode = code;
        if (code.length() == 0) {
            mView.hideSuccessMessage();
        }
    }

    @Override
    public boolean validateEditText(EditText view) {
        String regex = (String) view.getTag();
        int index = hints.indexOf(view.getHint().toString());
        if (view.getText() == null || view.getText().length() == 0 || !validateStringWithRegex(view.getText().toString(), regex)) {
            view.setError(errors.get(index));
            return false;
        } else {
            updateForm(view.getHint().toString(), view.getText().toString());
            return true;
        }
    }

    @Override
    public void updateEmail(String mail) {
        this.email = mail;
    }

    @Override
    public void updateNumber(String umber) {
        this.number = umber;
    }

    @Override
    public void getProfile() {
        mView.showProgressBar();
        email = Utils.getUserSession(mView.getActivity()).getEmail();
        number = ((EventModuleRouter) mView.getActivity().getApplication()).getUserPhoneNumber();
        mView.setEmailID(email);
        mView.setPhoneNumber(number);
        autoApplyCoupon();
        mView.hideProgressBar();
    }

    @Override
    public void clickEmailIcon() {
        mView.showEmailTooltip();
    }

    @Override
    public void clickMoreinfoIcon() {
        mView.showMoreinfoTooltip();
    }

    @Override
    public void clickDismissTooltip() {
        mView.hideTooltip();
    }

    @Override
    public void clickGoToPromo() {
        mView.showProgressBar();
        goToLoyaltyActivity();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CHECK_PROMO, promoCode);
    }

    private void goToLoyaltyActivity() {
        JsonObject requestBody = convertPackageToCartItem(checkoutData);
        Intent loyaltyIntent = ((EventModuleRouter) getView().getActivity().getApplication()).
                tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(getView().getActivity(),
                        Utils.Constants.EVENTS,
                        Utils.Constants.EVENTS,
                        "");
        loyaltyIntent.putExtra(Utils.Constants.CHECKOUTDATA, requestBody.toString());
        loyaltyIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PRODUCTID,
                checkoutData.getDigitalProductID());
        loyaltyIntent.putExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORYID,
                checkoutData.getDigitalCategoryID());
        mView.navigateToActivityRequest(loyaltyIntent,
                IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_CHECKOUT_PAGE;
    }

    private JsonObject convertPackageToCartItem(PackageViewModel packageViewModel) {
        Configuration config = new Configuration();
        config.setPrice(packageViewModel.getSalesPrice() * packageViewModel.getSelectedQuantity());
        com.tokopedia.events.domain.model.request.cart.SubConfig sub = new com.tokopedia.events.domain.model.request.cart.SubConfig();
        sub.setName(Utils.getUserSession(mView.getActivity()).getName());
        config.setSubConfig(sub);
        MetaData meta = new MetaData();
        meta.setEntityCategoryId(packageViewModel.getCategoryId());
        meta.setEntityCategoryName("");
        meta.setEntityGroupId(packageViewModel.getProductGroupId());
        List<EntityPackageItem> entityPackages = new ArrayList<>();
        EntityPackageItem packageItem = new EntityPackageItem();
        packageItem.setPackageId(packageViewModel.getId());
        if (selectedSeatViewModel != null) {
            packageItem.setAreaCode(selectedSeatViewModel.getAreaCodes());
            packageItem.setSeatId(selectedSeatViewModel.getSeatIds());
            packageItem.setSeatRowId(selectedSeatViewModel.getSeatRowIds());
            packageItem.setSeatPhysicalRowId(selectedSeatViewModel.getPhysicalRowIds());
            packageItem.setQuantity(selectedSeatViewModel.getQuantity());
            packageItem.setPricePerSeat(selectedSeatViewModel.getPrice());
            packageItem.setAreaId(selectedSeatViewModel.getAreaId());
            packageItem.setActualSeatNos(selectedSeatViewModel.getActualSeatNos());
        } else {
            packageItem.setAreaCode(new ArrayList<>());
            packageItem.setSeatId(new ArrayList<>());
            packageItem.setAreaId("");
            packageItem.setSeatRowId(new ArrayList<>());
            packageItem.setSeatPhysicalRowId(new ArrayList<>());
            packageItem.setQuantity(packageViewModel.getSelectedQuantity());
            packageItem.setPricePerSeat(packageViewModel.getSalesPrice());
            packageItem.setActualSeatNos(new ArrayList<>());
        }
        packageItem.setDescription(packageViewModel.getDescription());

        packageItem.setSessionId("");
        packageItem.setProductId(packageViewModel.getProductId());
        packageItem.setGroupId(packageViewModel.getProductGroupId());
        packageItem.setScheduleId(packageViewModel.getProductScheduleId());
        entityPackages.add(packageItem);
        meta.setEntityPackages(entityPackages);
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setEntityScheduleId(packageViewModel.getProductScheduleId());
        List<EntityPassengerItem> passengerItems = new ArrayList<>();

        if (packageViewModel.getForms() != null) {
            for (Form form : packageViewModel.getForms()) {
                EntityPassengerItem passenger = new EntityPassengerItem();
                passenger.setId(form.getId());
                passenger.setProductId(form.getProductId());
                passenger.setName(form.getName());
                passenger.setTitle(form.getTitle());
                passenger.setValue(form.getValue());
                passenger.setElementType(form.getElementType());
                passenger.setRequired(String.valueOf(form.getRequired()));
                passenger.setValidatorRegex(form.getValidatorRegex());
                passenger.setErrorMessage(form.getErrorMessage());
                passengerItems.add(passenger);
            }
        }

        meta.setEntityPassengers(passengerItems);
        EntityAddress address = new EntityAddress();
        address.setAddress("");
        address.setName(Utils.getUserSession(mView.getActivity()).getName());
        address.setCity("");
        address.setEmail(this.email);
        address.setMobile(this.number);
        address.setLatitude("");
        address.setLongitude("");
        meta.setEntityAddress(address);
        meta.setCitySearched("");
        meta.setEntityEndTime("");
        meta.setEntityStartTime("");
        meta.setTotalTaxAmount(0);
        meta.setTotalOtherCharges(0);
        meta.setTotalTicketPrice(packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice());
        meta.setEntityImage("");
        List<OtherChargesItem> otherChargesItems = new ArrayList<>();
        OtherChargesItem otherCharges = new OtherChargesItem();
        otherCharges.setConvFee(packageViewModel.getConvenienceFee());
        otherChargesItems.add(otherCharges);
        meta.setOtherCharges(otherChargesItems);
        List<TaxPerQuantityItem> taxPerQuantityItems = new ArrayList<>();
        meta.setTaxPerQuantity(taxPerQuantityItems);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setMetaData(meta);
        cartItem.setConfiguration(config);

        if (isEventOmsEnabled()) {
            cartItem.setQuantity(1);
            cartItem.setProductId(packageViewModel.getDigitalProductID());
        } else {
            cartItem.setProductId(packageViewModel.getProductId());
            cartItem.setQuantity(packageViewModel.getSelectedQuantity());
        }


        cartItems.add(cartItem);
        CartItems cart = new CartItems();
        cart.setCartItems(cartItems);
        cart.setPromocode(promoCode);

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
        return jsonElement.getAsJsonObject();
    }

    private void verifyCart() {
        mView.showProgressBar();
        RequestParams params = RequestParams.create();
        params.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(checkoutData));
        params.putBoolean(Utils.Constants.BOOK, isBook);
        if (isEventOmsEnabled()) {
            postVerifyCartUseCase.execute(params, new Subscriber<VerifyMyCartResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    mView.hideProgressBar();
                    NetworkErrorHelper.showEmptyState(mView.getActivity(),
                            mView.getRootView(), () -> verifyCart());
                }

                @Override
                public void onNext(VerifyMyCartResponse verifyCartResponse) {
                    Gson gson = new Gson();
                    Cart cart = gson.fromJson(String.valueOf(verifyCartResponse.getCart()), Cart.class);

                    if ("failure".equals(verifyCartResponse.getStatus().getResult())) {
                        mView.hideProgressBar();
                        mView.showSnackBar(cart.getError(), false);
                    } else {
                        paymentParams = RequestParams.create();
                        paymentParams.putObject(CHECKOUTDATA, convertCartItemToJson(verifyCartResponse.getCart()));
                        getPaymentLink();
                    }

                }
            });
        } else {
            verifyCartUseCase.execute(params, new Subscriber<VerifyCartResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    mView.hideProgressBar();
                    NetworkErrorHelper.showEmptyState(mView.getActivity(),
                            mView.getRootView(), () -> verifyCart());
                }

                @Override
                public void onNext(VerifyCartResponse verifyCartResponse) {
                    if ("failure".equals(verifyCartResponse.getStatus().getResult())) {
                        mView.hideProgressBar();
                        mView.showSnackBar(verifyCartResponse.getStatus().getResult(), false);
                    } else {
                        paymentParams = RequestParams.create();
                        paymentParams.putObject(CHECKOUTDATA, verifyCartResponse.getCart());
                        getPaymentLink();
                    }
                }
            });
        }
    }

    private JsonObject convertCartItemToJson(JsonObject cart) {
        for (JsonElement jsonElement : cart.get(com.tokopedia.oms.view.utils.Utils.Constants.CART_ITEMS).getAsJsonArray()) {
            JsonObject metadata = jsonElement.getAsJsonObject().get(com.tokopedia.oms.view.utils.Utils.Constants.META_DATA).getAsJsonObject();
            metadata.get(com.tokopedia.oms.view.utils.Utils.Constants.ENTITY_ADDRESS)
                    .getAsJsonObject().addProperty(com.tokopedia.oms.view.utils.Utils.Constants.EMAIL, this.email);
            if (eventsDetailsViewModel.getSchedulesViewModels().size() > 0)
                jsonElement.getAsJsonObject().get(com.tokopedia.oms.view.utils.Utils.Constants.META_DATA).getAsJsonObject().get(com.tokopedia.oms.view.utils.Utils.Constants.ENTITY_ADDRESS)
                        .getAsJsonObject().addProperty("name", eventsDetailsViewModel.getAddress());
            jsonElement.getAsJsonObject().get(com.tokopedia.oms.view.utils.Utils.Constants.META_DATA).getAsJsonObject().addProperty(com.tokopedia.oms.view.utils.Utils.Constants.ENTITY_BRAND_NAME, "");
            metadata.remove("entity_passengers");
            List<EntityPassengerItem> passengerItemList = getEntityPassengers(checkoutData);
            new JsonParser().parse(new Gson().toJson(passengerItemList));
            metadata.add("entity_passengers", new JsonParser().parse(new Gson().toJson(passengerItemList)));
        }
        cart.addProperty(com.tokopedia.oms.view.utils.Utils.Constants.PROMO, promoCode);
        cart.addProperty(com.tokopedia.oms.view.utils.Utils.Constants.ORDER_TITLE, checkoutData.getDisplayName());
        return cart;
    }

    private void getPaymentLink() {
        if (isEventOmsEnabled()) {
            mView.showProgressBar();
            postPaymentUseCase.execute(paymentParams, new Subscriber<JsonObject>() {

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
                    mView.hideProgressBar();
                    if (throwable.getMessage().equalsIgnoreCase(INVALID_EMAIL))
                        mView.showSnackBar(mView.getActivity().getString(R.string.please_enter_email), false);
                    else {
                        NetworkErrorHelper.showEmptyState(mView.getActivity(),
                                mView.getRootView(), () -> getPaymentLink());
                    }
                }

                @Override
                public void onNext(JsonObject checkoutResponse) {
                    String paymentData = com.tokopedia.oms.view.utils.Utils.transform(checkoutResponse);
                    String paymentURL = checkoutResponse.get("url").getAsString();
                    ScroogePGUtil.openScroogePage(mView.getActivity(), paymentURL, true, paymentData, mView.getActivity().getResources().getString(R.string.pembayaran));
                    mView.hideProgressBar();
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PAYMENT, checkoutData.getTitle().toLowerCase() + " - "
                            + checkoutData.getDisplayName().toLowerCase() + " - " + checkoutData.getSalesPrice() + " - " + promoCode);

                }
            });
        } else {
            checkoutPaymentUseCase.execute(paymentParams, new Subscriber<CheckoutResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    mView.hideProgressBar();
                    if (throwable.getMessage().equalsIgnoreCase(INVALID_EMAIL))
                        mView.showSnackBar(mView.getActivity().getString(R.string.please_enter_email), false);
                    else {
                        NetworkErrorHelper.showEmptyState(mView.getActivity(),
                                mView.getRootView(), () -> getPaymentLink());
                    }
                }

                @Override
                public void onNext(CheckoutResponse checkoutResponse) {

                    PaymentPassData paymentPassData = new com.tokopedia.payment.model.PaymentPassData();
                    paymentPassData.setQueryString(checkoutResponse.getQueryString());
                    paymentPassData.setRedirectUrl(checkoutResponse.getRedirectUrl());
                    paymentPassData.setCallbackSuccessUrl(checkoutResponse.getCallbackUrlSuccess());
                    paymentPassData.setCallbackFailedUrl(checkoutResponse.getCallbackUrlFailed());
                    paymentPassData.setTransactionId(checkoutResponse.getParameter().getTransactionId());
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PAYMENT, checkoutData.getTitle() + " - "
                            + checkoutData.getDisplayName() + " - " + checkoutData.getSalesPrice() + " - " + promoCode);
                    mView.navigateToActivityRequest(com.tokopedia.payment.activity.TopPayActivity.
                                    createInstance(mView.getActivity().getApplicationContext(), paymentPassData),
                            PAYMENT_REQUEST_CODE);
                    mView.hideProgressBar();

                }
            });
        }
    }

    private void getAndInitForms() {
        List<Form> forms = checkoutData.getForms();
        if (forms == null)
            return;
        ArrayList<String> regex = new ArrayList<>();
        for (Form form : forms) {
            hints.add(form.getTitle());
            regex.add(form.getValidatorRegex());
            errors.add(form.getErrorMessage());
        }
        String[] t = new String[1];
        String[] u = new String[1];
        String[] hint = hints.toArray(t);
        String[] validatorRegex = regex.toArray(u);
        mView.initForms(hint, validatorRegex);
    }


    private boolean validateStringWithRegex(String field, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }

    private void updateForm(String key, String value) {
        List<Form> forms = checkoutData.getForms();
        for (Form form : forms) {
            if (form.getTitle().equals(key))
                form.setValue(value);
        }
    }

    private void autoApplyCoupon() {
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(checkoutData));
        postInitCouponUseCase.execute(requestParams, new Subscriber<CouponModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showSnackBar(mView.getActivity().getResources().getString(R.string.autocoupon_fail), false);
            }

            @Override
            public void onNext(CouponModel couponModel) {
                String errorMsg = couponModel.getPromocodeFailureMessage();
                if (errorMsg != null &&
                        errorMsg.length() > 0) {
                    mView.hideProgressBar();
                    mView.hideSuccessMessage();
                    mView.showPromoSuccessMessage(errorMsg,
                            mView.getActivity().getResources().getColor(R.color.red_a700));
                    promoCode = "";
                } else {
                    String successMsg = couponModel.getPromocodeSuccessMessage();
                    if (successMsg != null && successMsg.length() > 0) {
                        mView.hideProgressBar();
                        mView.showPromoSuccessMessage(successMsg,
                                mView.getActivity().getResources().getColor(R.color.green_nob));
                        promoCode = couponModel.getPromocode();
                    }
                }
            }
        });
    }

    private boolean isEventOmsEnabled() {
        return ((EventModuleRouter) mView.getActivity().getApplication())
                .getBooleanRemoteConfig(Utils.Constants.EVENT_OMS, false);
    }

    private List<EntityPassengerItem> getEntityPassengers(PackageViewModel packageViewModel) {
        List<EntityPassengerItem> passengerItems = new ArrayList<>();

        if (packageViewModel.getForms() != null) {
            for (Form form : packageViewModel.getForms()) {
                EntityPassengerItem passenger = new EntityPassengerItem();
                passenger.setId(form.getId());
                passenger.setProductId(form.getProductId());
                passenger.setName(form.getName());
                passenger.setTitle(form.getTitle());
                passenger.setValue(form.getValue());
                passenger.setElementType(form.getElementType());
                passenger.setRequired(String.valueOf(form.getRequired()));
                passenger.setValidatorRegex(form.getValidatorRegex());
                passenger.setErrorMessage(form.getErrorMessage());
                passengerItems.add(passenger);
            }
        }
        return passengerItems;
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventReviewTicketsContractor.EventReviewTicketsView) view;
        mView.showProgressBar();
        Intent intent = view.getActivity().getIntent();
        this.eventsDetailsViewModel = intent.getParcelableExtra("event_detail");
        this.checkoutData = intent.getParcelableExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL);
        this.selectedSeatViewModel = intent.getParcelableExtra(Utils.Constants.EXTRA_SEATSELECTEDMODEL);
        String jsonResponse = intent.getStringExtra(EXTRA_VERIFY_RESPONSE);
        if (!StringUtils.isBlank(jsonResponse))
            this.verifiedSeatResponse = (JsonObject) new JsonParser().parse(jsonResponse);
        mView.renderFromPackageVM(checkoutData, selectedSeatViewModel);
        getAndInitForms();
    }
}
