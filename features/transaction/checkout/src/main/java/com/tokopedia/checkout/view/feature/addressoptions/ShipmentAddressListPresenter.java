package com.tokopedia.checkout.view.feature.addressoptions;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.common.base.CartMvpPresenter;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<RecipientAddressModel>>> {

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

    public boolean hasNext() {
        return hasNext;
    }

    public void resetAddressList(RecipientAddressModel currentAddress, boolean isDisableCorner) {
        getAddressList(DEFAULT_KEYWORD, currentAddress, true, isDisableCorner);
    }

    public void getAddressList(String query, final RecipientAddressModel currentAddress,
                               boolean resetPage, boolean isDisableCorner) {
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
                            .getRequestParams(query, currentPage++),
                    new Subscriber<PeopleAddressModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            if (isViewAttached() && getMvpView().getActivityContext() != null) {
                                getMvpView().hideLoading();
                                getMvpView().showError(throwable);
                                getMvpView().stopTrace();
                            }
                        }

                        @Override
                        public void onNext(PeopleAddressModel peopleAddressModel) {
                            if (isViewAttached()) {
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
                                                    if (recipientAddressModel.isIdentical(currentAddress)) {
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
                                                    if (peopleAddressModel.getCornerAddressModelsList() != null &&
                                                            !peopleAddressModel.getCornerAddressModelsList().isEmpty() &&
                                                            !isDisableCorner) {
                                                        getMvpView().setCorner(peopleAddressModel.getCornerAddressModelsList().get(0));
                                                        getMvpView().populateCorner(peopleAddressModel.getCornerAddressModelsList());
                                                    }
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