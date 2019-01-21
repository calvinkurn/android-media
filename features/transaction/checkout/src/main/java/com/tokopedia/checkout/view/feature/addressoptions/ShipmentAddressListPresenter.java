package com.tokopedia.checkout.view.feature.addressoptions;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.common.base.CartMvpPresenter;
import com.tokopedia.checkout.view.common.utils.PagingHandler;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<RecipientAddressModel>>> {

    private static final String TAG = ShipmentAddressListPresenter.class.getSimpleName();

    private static final String DEFAULT_KEYWORD = "";

    private final GetPeopleAddressUseCase mGetPeopleAddressUseCase;
    private int currentPage = 1;
    private boolean hasNext;
    private String lastQueryKeyword = "";
    private boolean resetPage;

    @Inject
    public ShipmentAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase) {
        mGetPeopleAddressUseCase = getPeopleAddressUseCase;
    }

    @Override
    public void attachView(ISearchAddressListView<List<RecipientAddressModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void resetAddressList(Context context, int order, RecipientAddressModel currentAddress) {
        getAddressList(context, order, DEFAULT_KEYWORD, currentAddress, true);
    }

    public void getAddressFromNewCreated(Context context, final RecipientAddressModel newAddress) {
        getAddressList(context, 1, "", newAddress, true);
    }

    public void getAddressList(Context context, int order, String query,
                               final RecipientAddressModel currentAddress, boolean resetPage) {
        if (!TextUtils.isEmpty(query)) {
            resetPage = !lastQueryKeyword.equals(query);
        }
        if (resetPage) {
            currentPage = 1;
            getMvpView().resetPagination();
        }
        lastQueryKeyword = query;
        this.resetPage = resetPage;
        if (currentPage == 1 || hasNext) {
            getMvpView().showLoading();
            mGetPeopleAddressUseCase.execute(mGetPeopleAddressUseCase
                            .getRequestParams(context, order, query, currentPage++),
                    new Subscriber<PeopleAddressModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            if (isViewAttached() && getMvpView().getActivityContext() != null) {
                                getMvpView().hideLoading();
                                String message = ErrorHandler.getErrorMessage(getMvpView().getActivityContext(), throwable);
                                getMvpView().showError(message);
                                getMvpView().stopTrace();
                            }
                        }

                        @Override
                        public void onNext(PeopleAddressModel peopleAddressModel) {
                            boolean viewIsAttached = isViewAttached();
                            if (viewIsAttached) {
                                getMvpView().hideLoading();
                                if (peopleAddressModel != null) {
                                    if (peopleAddressModel.getToken() != null) {
                                        getMvpView().setToken(peopleAddressModel.getToken());
                                    }

                                    if (peopleAddressModel.getPaging() != null) {
                                        hasNext = peopleAddressModel.getPaging().getUriNext() != null &&
                                                !peopleAddressModel.getPaging().getUriNext().equals("0");
                                        if (peopleAddressModel.getRecipientAddressModelList().isEmpty()) {
                                            getMvpView().showListEmpty();
                                            getMvpView().stopTrace();
                                        } else {
                                            RecipientAddressModel newlyCreatedAddress = null;
                                            if (currentAddress != null) {
                                                for (RecipientAddressModel recipientAddressModel : peopleAddressModel.getRecipientAddressModelList()) {
                                                    if (recipientAddressModel.getId().equalsIgnoreCase(currentAddress.getId()) ||
                                                            (recipientAddressModel.getDestinationDistrictId().equals(currentAddress.getDestinationDistrictId()) &&
                                                                    recipientAddressModel.getCityId().equals(currentAddress.getCityId()) &&
                                                                    recipientAddressModel.getProvinceId().equals(currentAddress.getProvinceId()) &&
                                                                    recipientAddressModel.getStreet().equals(currentAddress.getStreet()) &&
                                                                    recipientAddressModel.getAddressName().equals(currentAddress.getAddressName()) &&
                                                                    recipientAddressModel.getPostalCode().equals(currentAddress.getPostalCode()) &&
                                                                    recipientAddressModel.getRecipientPhoneNumber().equals(currentAddress.getRecipientPhoneNumber()) &&
                                                                    recipientAddressModel.getRecipientName().equals(currentAddress.getRecipientName()))
                                                            ) {
                                                        newlyCreatedAddress = recipientAddressModel;
                                                        recipientAddressModel.setSelected(true);
                                                        break;
                                                    }
                                                }
                                            }
                                            if (ShipmentAddressListPresenter.this.resetPage) {
                                                if (currentAddress != null && currentAddress.getId() == null && newlyCreatedAddress != null) {
                                                    getMvpView().navigateToCheckoutPage(newlyCreatedAddress);
                                                    getMvpView().stopTrace();
                                                } else {
                                                    getMvpView().showList(peopleAddressModel.getRecipientAddressModelList());
                                                    getMvpView().stopTrace();
                                                }
                                            } else {
                                                getMvpView().updateList(peopleAddressModel.getRecipientAddressModelList());
                                                getMvpView().stopTrace();
                                            }
                                        }
                                    } else {
                                        getMvpView().showListEmpty();
                                        getMvpView().stopTrace();
                                    }
                                } else {
                                    getMvpView().showListEmpty();
                                    getMvpView().stopTrace();
                                }
                            }
                        }
                    });
        }
    }

}