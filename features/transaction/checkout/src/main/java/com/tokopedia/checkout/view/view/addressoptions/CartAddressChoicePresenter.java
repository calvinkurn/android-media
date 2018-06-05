package com.tokopedia.checkout.view.view.addressoptions;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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

    private static final String TAG = CartAddressChoicePresenter.class.getSimpleName();

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
    public void getAddressShortedList(Context context, final RecipientAddressModel currentAddress) {
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
                            String message;
                            if (throwable instanceof UnknownHostException
                                    || throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException) {
                                message = getView().getActivity().getResources().getString(
                                        R.string.msg_no_connection);
                            } else if (throwable instanceof UnProcessableHttpException) {
                                message = TextUtils.isEmpty(throwable.getMessage()) ?
                                        getView().getActivity().getResources().getString(
                                                R.string.msg_no_connection) :
                                        throwable.getMessage();
                            } else {
                                message = getView().getActivity().getResources().getString(
                                        R.string.default_request_error_unknown);
                            }
                            getView().showNoConnection(message);
                        }
                    }

                    @Override
                    public void onNext(PeopleAddressModel peopleAddressModel) {
                        if (!peopleAddressModel.getRecipientAddressModelList().isEmpty()) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().renderRecipientData(
                                        shortList(peopleAddressModel.getRecipientAddressModelList(),
                                                currentAddress));
                                getView().setToken(peopleAddressModel.getToken());
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

    private List<RecipientAddressModel> shortList(final List<RecipientAddressModel> addressList,
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
                        recipientAddressModel.getAddressStreet().equals(currentAddress.getAddressStreet())) {
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