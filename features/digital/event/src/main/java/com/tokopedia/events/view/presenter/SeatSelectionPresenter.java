package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.data.entity.response.Form;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.model.request.cart.CartItem;
import com.tokopedia.events.domain.model.request.cart.CartItems;
import com.tokopedia.events.domain.model.request.cart.Configuration;
import com.tokopedia.events.domain.model.request.cart.EntityAddress;
import com.tokopedia.events.domain.model.request.cart.EntityPackageItem;
import com.tokopedia.events.domain.model.request.cart.EntityPassengerItem;
import com.tokopedia.events.domain.model.request.cart.MetaData;
import com.tokopedia.events.domain.model.request.cart.OtherChargesItem;
import com.tokopedia.events.domain.model.request.cart.TaxPerQuantityItem;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;


import rx.Subscriber;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatSelectionPresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements SeatSelectionContract.SeatSelectionPresenter {


    private EventsDetailsViewModel eventsDetailsViewModel;
    private SeatLayoutViewModel seatLayoutViewModel;
    private VerifyCartUseCase verifyCartUseCase;
    private PostVerifyCartUseCase postVerifyCartUseCase;
    private PackageViewModel selectedpkgViewModel;
    private String email;
    private String number;
    private SelectedSeatViewModel mSelectedSeatViewModel;
    private SeatSelectionContract.SeatSelectionView mView;


    public SeatSelectionPresenter(VerifyCartUseCase verifyCartUseCase, PostVerifyCartUseCase postVerifyCartUseCase) {
        this.verifyCartUseCase = verifyCartUseCase;
        this.postVerifyCartUseCase = postVerifyCartUseCase;
    }

    public void getProfile() {
        mView.showProgressBar();
        if (!Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
            Intent intent = ((EventModuleRouter) mView.getActivity().getApplication()).
                    getLoginIntent(mView.getActivity());
            mView.navigateToActivityRequest(intent, 1099);
        } else {
            email = Utils.getUserSession(mView.getActivity()).getEmail();
            number = ((EventModuleRouter) mView.getActivity().getApplication()).getUserPhoneNumber();
            mView.hideProgressBar();
        }
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (SeatSelectionContract.SeatSelectionView) view;
        eventsDetailsViewModel = mView.getActivity().getIntent().getParcelableExtra("event_detail");
        selectedpkgViewModel = mView.getActivity().getIntent().getParcelableExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL);
        seatLayoutViewModel = mView.getActivity().getIntent().getParcelableExtra(Utils.Constants.EXTRA_SEATLAYOUTVIEWMODEL);
        mView.setEventTitle(mView.getActivity().getIntent().getStringExtra("EventTitle"));
        getProfile();
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


    public void getSeatSelectionDetails() {
        mView.renderSeatSelection(selectedpkgViewModel.getSalesPrice(),
                selectedpkgViewModel.getSelectedQuantity(), seatLayoutViewModel);
        mView.setTicketPrice(selectedpkgViewModel.getSelectedQuantity());
    }

    @Override
    public void validateSelection() {

    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == 1099) {
            if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
                getProfile();
            } else {
                mView.hideProgressBar();
            }
        }
    }

    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_SEAT_SELECTIONPAGE;
    }

    public void setSelectedSeatText(List<String> selectedSeatList, List<String> rowIds, List<String> actualSeatNo) {
        mView.initializeSeatLayoutModel(selectedSeatList, rowIds, actualSeatNo);
    }

    public void setSeatData() {
        mView.setSelectedSeatText();
    }

    public void verifySeatSelection(SelectedSeatViewModel selectedSeatViewModel) {
        mView.showProgressBar();
        this.mSelectedSeatViewModel = selectedSeatViewModel;
        RequestParams params = RequestParams.create();
        params.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(selectedpkgViewModel));
        params.putBoolean(Utils.Constants.BOOK, true);
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
                            mView.getRootView(), () -> verifySeatSelection(mSelectedSeatViewModel));
                }

                @Override
                public void onNext(VerifyMyCartResponse verifyCartResponse) {
                    JsonObject cart = verifyCartResponse.getCart();
                    if (cart != null && cart.size() > 0) {
                        if (cart.has("error")) {
                            mView.showSnackBar(cart.get("error").getAsString(), false);
                        } else {
                            String cartJsonString;
                            Gson gson = new Gson();
                            cartJsonString = gson.toJson(cart);
                            Intent reviewTicketIntent = new Intent(mView.getActivity(), ReviewTicketActivity.class);
                            reviewTicketIntent.putExtra("event_detail", eventsDetailsViewModel);
                            reviewTicketIntent.putExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL, selectedpkgViewModel);
                            reviewTicketIntent.putExtra(Utils.Constants.EXTRA_SEATSELECTEDMODEL, mSelectedSeatViewModel);
                            reviewTicketIntent.putExtra(Utils.Constants.EXTRA_VERIFY_RESPONSE, cartJsonString);
                            mView.navigateToActivityRequest(reviewTicketIntent, 100);
                            mView.hideProgressBar();
                        }
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
                    mView.hideProgressBar();
                    NetworkErrorHelper.showEmptyState(mView.getActivity(),
                            mView.getRootView(), () -> verifySeatSelection(mSelectedSeatViewModel));
                }

                @Override
                public void onNext(VerifyCartResponse verifyCartResponse) {
                    if (!StringUtils.isBlank(verifyCartResponse.getCart().getError())) {
                        Intent reviewTicketIntent = new Intent(mView.getActivity(), ReviewTicketActivity.class);
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL, selectedpkgViewModel);
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_SEATSELECTEDMODEL, mSelectedSeatViewModel);
                        Gson gson = new Gson();
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_VERIFY_RESPONSE, gson.toJson(verifyCartResponse.getCart()));
                        mView.navigateToActivityRequest(reviewTicketIntent, Utils.Constants.REVIEW_REQUEST);
                    } else {
                        mView.showSnackBar(verifyCartResponse.getCart().getError(), false);
                    }
                }
            });
        }
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
        packageItem.setAreaCode(mSelectedSeatViewModel.getAreaCodes());
        packageItem.setAreaId(mSelectedSeatViewModel.getAreaId());
        packageItem.setSeatPhysicalRowId(mSelectedSeatViewModel.getPhysicalRowIds());
        packageItem.setSeatRowId(mSelectedSeatViewModel.getSeatRowIds());
        packageItem.setDescription("");
        packageItem.setQuantity(mSelectedSeatViewModel.getQuantity());
        packageItem.setPricePerSeat(mSelectedSeatViewModel.getPrice());
        packageItem.setSeatId(mSelectedSeatViewModel.getSeatIds());
        packageItem.setSeatRowId(mSelectedSeatViewModel.getSeatRowIds());
        packageItem.setActualSeatNos(mSelectedSeatViewModel.getActualSeatNos());
        packageItem.setSeatPhysicalRowId(mSelectedSeatViewModel.getPhysicalRowIds());
        packageItem.setSessionId("");
        packageItem.setProductId(packageViewModel.getProductId());
        packageItem.setGroupId(packageViewModel.getProductGroupId());
        packageItem.setScheduleId(packageViewModel.getProductScheduleId());
        entityPackages.add(packageItem);
        meta.setEntityPackages(entityPackages);
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setEntityScheduleId(packageViewModel.getProductScheduleId());
        if (packageViewModel.getForms() != null) {
            List<EntityPassengerItem> passengerItems = new ArrayList<>();

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

            meta.setEntityPassengers(passengerItems);
        }
        EntityAddress address = new EntityAddress();
        address.setAddress("");
        address.setName("");
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
        cart.setPromocode("");

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
        return jsonElement.getAsJsonObject();
    }

    public String getTicketCategory() {
        return selectedpkgViewModel.getTitle();
    }

    private boolean isEventOmsEnabled() {
        return ((EventModuleRouter) mView.getActivity().getApplication()).getBooleanRemoteConfig(Utils.Constants.EVENT_OMS, false);
    }

}