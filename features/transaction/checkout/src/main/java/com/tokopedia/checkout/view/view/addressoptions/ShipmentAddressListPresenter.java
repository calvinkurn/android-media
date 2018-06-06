package com.tokopedia.checkout.view.view.addressoptions;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.base.CartMvpPresenter;
import com.tokopedia.checkout.view.utils.PagingHandler;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<RecipientAddressModel>>> {

    private static final String TAG = ShipmentAddressListPresenter.class.getSimpleName();

    private static final String DEFAULT_KEYWORD = "";

    private final GetPeopleAddressUseCase mGetPeopleAddressUseCase;
    private final PagingHandler mPagingHandler;
    private int currentPage = 1;
    private boolean hasNext;

    @Inject
    public ShipmentAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase,
                                        PagingHandler pagingHandler) {
        mGetPeopleAddressUseCase = getPeopleAddressUseCase;
        mPagingHandler = pagingHandler;
    }

    @Override
    public void attachView(ISearchAddressListView<List<RecipientAddressModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    /**
     * @param context
     * @param order
     */
    public void resetAddressList(Context context, int order, RecipientAddressModel currentAddress) {
        getAddressList(context, order, DEFAULT_KEYWORD, currentAddress, true);
    }

    /**
     * @param context
     * @param order
     * @param query
     */
    public void getAddressList(Context context, int order, String query,
                               final RecipientAddressModel currentAddress, boolean resetPage) {
        if (resetPage) {
            currentPage = 1;
        }
        if (!TextUtils.isEmpty(query) || currentPage == 1 || hasNext) {
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
                            if (isViewAttached()) {
                                getMvpView().hideLoading();
                                String message;
                                if (throwable instanceof UnknownHostException ||
                                        throwable instanceof ConnectException ||
                                        throwable instanceof SocketTimeoutException) {
                                    message = getMvpView().getActivity().getResources().getString(
                                            R.string.msg_no_connection);
                                } else if (throwable instanceof UnProcessableHttpException) {
                                    message = TextUtils.isEmpty(throwable.getMessage()) ?
                                            getMvpView().getActivity().getResources().getString(
                                                    R.string.msg_no_connection) :
                                            throwable.getMessage();
                                } else {
                                    message = getMvpView().getActivity().getResources().getString(
                                            R.string.default_request_error_unknown);
                                }
                                getMvpView().showError(message);
                            }
                        }

                        @Override
                        public void onNext(PeopleAddressModel peopleAddressModel) {
                            boolean viewIsAttached = isViewAttached();
                            if (viewIsAttached) {
                                getMvpView().hideLoading();
                                if (peopleAddressModel != null && peopleAddressModel.getPaging() != null) {
                                    hasNext = peopleAddressModel.getPaging().getUriNext() != null &&
                                            !peopleAddressModel.getPaging().getUriNext().equals("0");
                                    if (peopleAddressModel.getRecipientAddressModelList().isEmpty()) {
                                        getMvpView().showListEmpty();
                                    } else {
                                        if (currentAddress != null) {
                                            for (RecipientAddressModel recipientAddressModel : peopleAddressModel.getRecipientAddressModelList()) {
                                                if (recipientAddressModel.getId().equalsIgnoreCase(currentAddress.getId())) {
                                                    recipientAddressModel.setSelected(true);
                                                    break;
                                                }
                                            }
                                        }
                                        if (resetPage) {
                                            getMvpView().showList(peopleAddressModel.getRecipientAddressModelList());
                                        } else {
                                            getMvpView().updateList(peopleAddressModel.getRecipientAddressModelList());
                                        }
                                    }
                                } else {
                                    getMvpView().showListEmpty();
                                }
                            }
                        }
                    });
        }
    }

}