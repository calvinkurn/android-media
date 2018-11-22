package com.tokopedia.checkout.view.feature.addressoptions;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static java.lang.Math.min;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 27/02/18
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private static final int SHORT_LIST_SIZE = 2;
    private static final int PRIME_ADDRESS = 2;

    private static final int DEFAULT_ORDER = 1;
    private static final String DEFAULT_QUERY = "";
    private static final int DEFAULT_PAGE = 1;

    private GetPeopleAddressUseCase mGetPeopleAddressUseCase;
    private RecipientAddressModel mSelectedRecipientAddress;

    @Inject
    public CartAddressChoicePresenter(GetPeopleAddressUseCase getPeopleAddressUseCase) {
        mGetPeopleAddressUseCase = getPeopleAddressUseCase;
    }

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mGetPeopleAddressUseCase.unsubscribe();
    }

    @Override
    public void getAddressShortedList(Context context, final RecipientAddressModel currentAddress, boolean isNewlyCreatedAddress) {
        getView().showLoading();
        mGetPeopleAddressUseCase.execute(mGetPeopleAddressUseCase.getRequestParams(
                context, DEFAULT_ORDER, DEFAULT_QUERY, DEFAULT_PAGE),
                new Subscriber<PeopleAddressModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getView().hideLoading();
                            String message = ErrorHandler.getErrorMessage(getView().getActivityContext(), throwable);
                            getView().showNoConnection(message);
                        }
                    }

                    @Override
                    public void onNext(PeopleAddressModel peopleAddressModel) {
                        if (!peopleAddressModel.getRecipientAddressModelList().isEmpty()) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().setToken(peopleAddressModel.getToken());
                                getView().renderRecipientData(
                                        shortList(peopleAddressModel.getRecipientAddressModelList(),
                                                currentAddress), isNewlyCreatedAddress);
                            }
                        } else {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().renderEmptyRecipientData();
                            }
                        }
                    }
                });
    }

    @Override
    public void setSelectedRecipientAddress(RecipientAddressModel recipientAddress) {
        this.mSelectedRecipientAddress = recipientAddress;
        getView().renderSaveButtonEnabled();
    }

    @Override
    public RecipientAddressModel getSelectedRecipientAddress() {
        return mSelectedRecipientAddress;
    }

    private List<RecipientAddressModel> shortList(List<RecipientAddressModel> addressList,
                                                  RecipientAddressModel currentAddress) {

        if (currentAddress != null) {
            for (RecipientAddressModel recipientAddressModel : addressList) {
                if (recipientAddressModel.getId().equalsIgnoreCase(currentAddress.getId())) {
                    recipientAddressModel.setSelected(true);
                    setSelectedRecipientAddress(recipientAddressModel);
                    break;
                } else if (recipientAddressModel.getAddressName().equals(currentAddress.getAddressName()) &&
                        recipientAddressModel.getDestinationDistrictId().equals(currentAddress.getDestinationDistrictId()) &&
                        recipientAddressModel.getCityId().equals(currentAddress.getCityId()) &&
                        recipientAddressModel.getProvinceId().equals(currentAddress.getProvinceId()) &&
                        recipientAddressModel.getRecipientName().equals(currentAddress.getRecipientName()) &&
                        recipientAddressModel.getRecipientPhoneNumber().equals(currentAddress.getRecipientPhoneNumber()) &&
                        recipientAddressModel.getStreet().equals(currentAddress.getStreet())) {
                    recipientAddressModel.setSelected(true);
                    setSelectedRecipientAddress(recipientAddressModel);
                    break;
                }
            }
        }

        final int shortListSize = min(addressList.size(), SHORT_LIST_SIZE);

        List<RecipientAddressModel> shortList = new ArrayList<RecipientAddressModel>() {{
            addAll(addressList.subList(0, shortListSize));
        }};

        if (mSelectedRecipientAddress != null)
            shortList.set(mSelectedRecipientAddress.getAddressStatus() == PRIME_ADDRESS ?
                    0 : shortListSize - 1, mSelectedRecipientAddress);

        return shortList;
    }
}