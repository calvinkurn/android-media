package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoHistoryContract;
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlBuyerDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse;
import com.tokopedia.saldodetails.response.model.SummaryDepositParam;
import com.tokopedia.saldodetails.usecase.GetAllTransactionUsecase;
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static android.content.ContentValues.TAG;

public class SaldoHistoryPresenter extends BaseDaggerPresenter<SaldoHistoryContract.View>
        implements SaldoHistoryContract.Presenter {

    private static final int DEFAULT_PAGE = 1;
    @Inject
    GetDepositSummaryUseCase getDepositSummaryUseCase;
    @Inject
    GetAllTransactionUsecase getAllTransactionUsecase;

    private PagingHandler paging;
//    private DepositCacheInteractor depositCacheInteractor;

    private static final long SEC_TO_DAY_CONVERSION = 24 * 60 * 60 * 1000;
    private static final long MAX_DAYS_DIFFERENCE = 31;
    private static final long SEARCH_DELAY = 500;
    private static final java.lang.String DATE_FORMAT_VIEW = "dd MMM yyyy";
    public static final int REQUEST_WITHDRAW_CODE = 1;
    private String paramStartDate;
    private String paramEndDate;
    private static final String DATE_FORMAT_WS = "yyyy/MM/dd";
    private boolean isSeller;

    @Inject
    public SaldoHistoryPresenter(@ApplicationContext Context context) {
//        depositCacheInteractor = new DepositCacheInteractorImpl(context);
        this.paging = new PagingHandler();
    }


    @Override
    public void setFirstDateParameter() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String startDate = dateFormat.format(cal.getTime());
        String endDate = dateFormat.format(date);
        getView().setStartDate(startDate);
        getView().setEndDate(endDate);
        paramStartDate = startDate;
        paramEndDate = endDate;
    }

    @Override
    public void onSearchClicked() {
        paramStartDate = getView().getStartDate();
        paramEndDate = getView().getEndDate();
        getSummaryDeposit();
    }

    private void setData(GqlAllDepositSummaryResponse data) {
        if (!isViewAttached() || getView().getAdapter() == null || data == null) {
            return;
        }


        getView().getAllHistoryAdapter().addElement(data.getAllDepositHistory().getDepositHistoryList());
        ((BaseListFragment) getView().getAllSaldoHistoryTabItem().getFragment()).
                updateScrollListenerState(data.getAllDepositHistory().isHaveNextPage());
        if (getView().getAllHistoryAdapter().getItemCount() == 0) {
            getView().getAllHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
        }

        getView().getBuyerHistoryAdapter().addElement(data.getBuyerDepositHistory().getDepositHistoryList());
        ((BaseListFragment) getView().getBuyerSaldoHistoryTabItem().getFragment()).
                updateScrollListenerState(data.getBuyerDepositHistory().isHaveNextPage());
        if (getView().getBuyerHistoryAdapter().getItemCount() == 0) {
            getView().getBuyerHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
        }

        getView().getSellerHistoryAdapter().addElement(data.getSellerDepositHistory().getDepositHistoryList());
        ((BaseListFragment) getView().getSellerSaldoHistoryTabItem().getFragment()).
                updateScrollListenerState(data.getSellerDepositHistory().isHaveNextPage());
        if (getView().getSellerHistoryAdapter().getItemCount() == 0) {
            getView().getSellerHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
        }

        /*if (paging.CheckNextPage()) {
            showLoading();
        } else {
            hideLoading();
        }*/
    }

    private void setData(GqlCompleteTransactionResponse data) {
        if (!isViewAttached() || getView().getAllHistoryAdapter() == null || data == null) {
            return;
        }

        getView().getAllHistoryAdapter().addElement(data.getAllDepositHistory().getDepositHistoryList());
        ((BaseListFragment) getView().getAllSaldoHistoryTabItem().getFragment()).
                updateScrollListenerState(data.getAllDepositHistory().isHaveNextPage());
        if (getView().getAllHistoryAdapter().getItemCount() == 0) {
            getView().getAllHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
        }

        /*if (paging.CheckNextPage()) {
            showLoading();
        } else {
            hideLoading();
        }*/
    }

    private void setData(GqlBuyerDepositSummaryResponse data) {
        if (!isViewAttached() || getView().getBuyerHistoryAdapter() == null || data == null) {
            return;
        }

        getView().getBuyerHistoryAdapter().addElement(data.getBuyerDepositHistory().getDepositHistoryList());
        ((BaseListFragment) getView().getSingleHistoryTabItem().getFragment()).
                updateScrollListenerState(data.getBuyerDepositHistory().isHaveNextPage());
        if (getView().getBuyerHistoryAdapter().getItemCount() == 0) {
            getView().getBuyerHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
        }

        /*if (paging.CheckNextPage()) {
            showLoading();
        } else {
            hideLoading();
        }*/
    }

    private void showLoading() {
        if (isViewAttached() && getView().getAdapter() != null) {
            getView().getAdapter().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached() && getView().getAdapter() != null) {
            getView().getAdapter().hideLoading();
            getView().finishLoading();
        }
    }

    @Override
    public void onEndDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getEndDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setEndDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, SEARCH_DELAY);
        });
    }

    @Override
    public void onStartDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getStartDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setStartDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, SEARCH_DELAY);

        });
    }

    private String getDate(int year, int month, int day) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        return dateFormat.format(cal.getTime());
    }

    private int getStartYear(String date) {
        String year = date.substring(6, 10);
        return Integer.parseInt(year);
    }

    private int getStartMonth(String date) {
        String month = date.substring(3, 5);
        return Integer.parseInt(month);
    }

    private int getDay(String date) {
        String day = date.substring(0, 2);
        return Integer.parseInt(day);
    }

    private String dateFormatter(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedStart = null;
        try {
            formattedStart = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf_ws.format(formattedStart);

    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (paging.CheckNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && canLoadMore()) {
            paging.nextPage();
            getSummaryDeposit();
        }
    }

    private boolean canLoadMore() {
        return !getDepositSummaryUseCase.isRequesting();
    }

    private boolean isOnLastPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }

    @Override
    public void getSummaryDeposit() {
        if (!isViewAttached()) {
            return;
        }
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);

            getDepositSummaryUseCase.setIsSeller(isSeller());
            getDepositSummaryUseCase.setRequesting(true);
            getDepositSummaryUseCase.setRequestVariables(getSummaryDepositParam());

            getDepositSummaryUseCase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    getDepositSummaryUseCase.setRequesting(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.toString());
                    hideLoading();
                    ErrorHandler.getErrorMessage(getView().getContext(), e);
                    if (e instanceof UnknownHostException ||
                            e instanceof SocketTimeoutException) {
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else {
                        getView().setActionsEnabled(true);
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    hideLoading();
                    onDepositSummaryFetched(graphqlResponse);
                }
            });

        } else {
            getView().finishLoading();
        }
    }

    private void onDepositSummaryFetched(GraphqlResponse graphqlResponse) {
        if (!isViewAttached()) {
            return;
        }
        getView().setActionsEnabled(true);

        if (isSeller()) {
            if (graphqlResponse != null &&
                    graphqlResponse.getData(GqlAllDepositSummaryResponse.class) != null) {

                GqlAllDepositSummaryResponse gqlDepositSummaryResponse =
                        graphqlResponse.getData(GqlAllDepositSummaryResponse.class);

                if (gqlDepositSummaryResponse != null &&
                        !gqlDepositSummaryResponse.getAllDepositHistory().isHaveError()) {

                    if (paging.getPage() == 1) {
                        getView().getAllHistoryAdapter().clearAllElements();
                        getView().getBuyerHistoryAdapter().clearAllElements();
                        getView().getSellerHistoryAdapter().clearAllElements();
                    }
                    setData(gqlDepositSummaryResponse);
                } else {
                    if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.
                            getAllDepositHistory() != null) {
                        if (getView().getAllHistoryAdapter().getItemCount() == 0) {
                            getView().showEmptyState(gqlDepositSummaryResponse.
                                    getAllDepositHistory().getMessage());
                        } else {
                            getView().setRetry(gqlDepositSummaryResponse.
                                    getAllDepositHistory().getMessage());
                        }
                    }

                    if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.
                            getBuyerDepositHistory() != null) {
                        if (getView().getBuyerHistoryAdapter().getItemCount() == 0) {
                            getView().showEmptyState(gqlDepositSummaryResponse.
                                    getBuyerDepositHistory().getMessage());
                        } else {
                            getView().setRetry(gqlDepositSummaryResponse.
                                    getBuyerDepositHistory().getMessage());
                        }
                    }

                    if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.
                            getSellerDepositHistory() != null) {
                        if (getView().getSellerHistoryAdapter().getItemCount() == 0) {
                            getView().showEmptyState(gqlDepositSummaryResponse.
                                    getSellerDepositHistory().getMessage());
                        } else {
                            getView().setRetry(gqlDepositSummaryResponse.
                                    getSellerDepositHistory().getMessage());
                        }
                    }
                }

            } else {
                if (getView().getAdapter() != null && getView().getAdapter().getItemCount() == 0) {
                    getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                } else {
                    getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                }

            }
        } else {
            if (graphqlResponse != null &&
                    graphqlResponse.getData(GqlBuyerDepositSummaryResponse.class) != null) {

                GqlBuyerDepositSummaryResponse gqlDepositSummaryResponse =
                        graphqlResponse.getData(GqlBuyerDepositSummaryResponse.class);

                if (gqlDepositSummaryResponse != null &&
                        !gqlDepositSummaryResponse.getBuyerDepositHistory().isHaveError()) {

                    if (paging.getPage() == 1) {
                        getView().getSingleTabAdapter().clearAllElements();
//                    depositCacheInteractor.setSummaryDepositCache(gqlDepositSummaryResponse);
                    }
                    paging.setHasNext(gqlDepositSummaryResponse.getBuyerDepositHistory()
                            .isHaveNextPage());
                    setData(gqlDepositSummaryResponse);
                } else {
                    if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.
                            getBuyerDepositHistory() != null) {
                        if (getView().getSingleTabAdapter().getItemCount() == 0) {
                            getView().showEmptyState(gqlDepositSummaryResponse.
                                    getBuyerDepositHistory().getMessage());
                        } else {
                            getView().setRetry(gqlDepositSummaryResponse.
                                    getBuyerDepositHistory().getMessage());
                        }
                    }
                }

            } else {
                if (getView().getSingleTabAdapter() != null && getView().getSingleTabAdapter().getItemCount() == 0) {
                    getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                } else {
                    getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                }

            }
        }

        finishLoading();
    }

    private void finishLoading() {
        if (isViewAttached()) {
            getView().finishLoading();
        }
    }

    @Override
    public void onRefresh() {
        paging.resetPage();
        getSummaryDeposit();
    }

    private String getDateParam(String date) {
        return date.replace("/", "-");
    }

    private Map<String, Object> getSummaryDepositParam() {
        SummaryDepositParam param = new SummaryDepositParam();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat(DATE_FORMAT_WS);
        try {
            Date formattedStart = sdf.parse(paramStartDate);
            Date formattedEnd = sdf.parse(paramEndDate);
            param.setStartDate(getDateParam(sdf_ws.format(formattedStart)));
            param.setEndDate(getDateParam(sdf_ws.format(formattedEnd)));
        } catch (ParseException e) {
            getView().showErrorMessage(getView().getString(R.string.sp_error_invalid_date));
        }

        param.setPage(paging.getPage());
        param.setSeller(isSeller());
        return param.getParamSummaryDeposit();

    }

    private Map<String, Object> getSummaryDepositParam(int page, int saldoType) {
        SummaryDepositParam param = new SummaryDepositParam();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat(DATE_FORMAT_WS);
        try {
            Date formattedStart = sdf.parse(paramStartDate);
            Date formattedEnd = sdf.parse(paramEndDate);
            param.setStartDate(getDateParam(sdf_ws.format(formattedStart)));
            param.setEndDate(getDateParam(sdf_ws.format(formattedEnd)));
        } catch (ParseException e) {
            getView().showErrorMessage(getView().getString(R.string.sp_error_invalid_date));
        }

        param.setPage(page);
        param.setSaldoType(saldoType);
        return param.getParamSummaryDeposit();

    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    private boolean isValid() {
        Boolean isValid = true;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        try {
            Date endDate = sdf.parse(paramEndDate);
            Date startDate = sdf.parse(paramStartDate);
            if (endDate.getTime() - startDate.getTime() < 0) {
                isValid = false;
                getView().showInvalidDateError(getView().getString(R.string.sp_error_invalid_date));
            }

            if ((endDate.getTime() - startDate.getTime()) / SEC_TO_DAY_CONVERSION >= MAX_DAYS_DIFFERENCE) {
                isValid = false;
                getView().showInvalidDateError(getView().getString(R.string.sp_title_max_day));
            }
        } catch (ParseException e) {
            isValid = false;
            getView().showInvalidDateError(getView().getString(R.string.sp_error_invalid_date));
        }
        return isValid;
    }

    public void detachView() {
        super.detachView();
        try {
            getDepositSummaryUseCase.unsubscribe();
        } catch (Exception e) {

        }
    }

    public void loadMoreAllTransaction(int lastItemPosition, int type) {
        // TODO: 12/2/19 update scroll listener state
        if (!isViewAttached()) {
            return;
        }
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);

            getAllTransactionUsecase.setIsSeller(isSeller());
            getAllTransactionUsecase.setRequesting(true);
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type));
            getAllTransactionUsecase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    getAllTransactionUsecase.setRequesting(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.toString());
                    hideLoading();
                    ErrorHandler.getErrorMessage(getView().getContext(), e);
                    if (e instanceof UnknownHostException ||
                            e instanceof SocketTimeoutException) {
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else {
                        getView().setActionsEnabled(true);
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    hideLoading();
                    onAllTransactionsFetched(graphqlResponse, lastItemPosition);
                }

            });

        } else {
            finishLoading();
        }
    }

    private void onAllTransactionsFetched(GraphqlResponse graphqlResponse, int page) {

        if (!isViewAttached()) {
            return;
        }
        getView().setActionsEnabled(true);

        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlAllDepositSummaryResponse.class) != null) {

            GqlCompleteTransactionResponse gqlCompleteTransactionResponse =
                    graphqlResponse.getData(GqlCompleteTransactionResponse.class);

            if (gqlCompleteTransactionResponse != null &&
                    !gqlCompleteTransactionResponse.getAllDepositHistory().isHaveError()) {
                setData(gqlCompleteTransactionResponse);
            } else {
                if (gqlCompleteTransactionResponse != null && gqlCompleteTransactionResponse.
                        getAllDepositHistory() != null) {
                    if (getView().getAllHistoryAdapter().getItemCount() == 0) {
                        getView().showEmptyState(gqlCompleteTransactionResponse.
                                getAllDepositHistory().getMessage());
                    } else {
                        getView().setRetry(gqlCompleteTransactionResponse.
                                getAllDepositHistory().getMessage());
                    }
                }
            }
        } else {
            if (getView().getAdapter() != null && getView().getAdapter().getItemCount() == 0) {
                getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
            } else {
                getView().setRetry(getView().getString(R.string.sp_empty_state_error));
            }
        }
        finishLoading();
    }

    public void loadMoreSellerTransaction(int lastItemPosition, int type) {
        // TODO: 12/2/19 update scroll listener state
        if (!isViewAttached()) {
            return;
        }
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);

            getAllTransactionUsecase.setIsSeller(isSeller());
            getAllTransactionUsecase.setRequesting(true);
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type));
            getAllTransactionUsecase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    getAllTransactionUsecase.setRequesting(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.toString());
                    hideLoading();
                    ErrorHandler.getErrorMessage(getView().getContext(), e);
                    if (e instanceof UnknownHostException ||
                            e instanceof SocketTimeoutException) {
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else {
                        getView().setActionsEnabled(true);
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    hideLoading();
                    if (!isViewAttached()) {
                        return;
                    }
                    getView().setActionsEnabled(true);

                    if (graphqlResponse != null &&
                            graphqlResponse.getData(GqlAllDepositSummaryResponse.class) != null) {

                        GqlCompleteTransactionResponse gqlCompleteTransactionResponse =
                                graphqlResponse.getData(GqlCompleteTransactionResponse.class);

                        if (gqlCompleteTransactionResponse != null &&
                                !gqlCompleteTransactionResponse.getAllDepositHistory().isHaveError()) {
                            if (!isViewAttached() || getView().getSellerHistoryAdapter() == null) {
                                return;
                            }

                            ((BaseListFragment) getView().getSellerSaldoHistoryTabItem().getFragment()).
                                    updateScrollListenerState(gqlCompleteTransactionResponse.getAllDepositHistory().isHaveNextPage());

                            getView().getSellerHistoryAdapter().addElement(
                                    gqlCompleteTransactionResponse.getAllDepositHistory().getDepositHistoryList());
                            if (getView().getSellerHistoryAdapter().getItemCount() == 0) {
                                getView().getSellerHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
                            }
                        } else {
                            if (gqlCompleteTransactionResponse != null && gqlCompleteTransactionResponse.
                                    getAllDepositHistory() != null) {
                                if (getView().getSellerHistoryAdapter().getItemCount() == 0) {
                                    getView().showEmptyState(gqlCompleteTransactionResponse.
                                            getAllDepositHistory().getMessage());
                                } else {
                                    getView().setRetry(gqlCompleteTransactionResponse.
                                            getAllDepositHistory().getMessage());
                                }
                            }
                        }
                    } else {
                        if (getView().getAdapter() != null && getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                    finishLoading();
                }

            });

        } else {
            finishLoading();
        }
    }

    public void loadMoreBuyerTransaction(int lastItemPosition, int type) {
// TODO: 12/2/19 update scroll listener state
        if (!isViewAttached()) {
            return;
        }
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);

            getAllTransactionUsecase.setIsSeller(isSeller());
            getAllTransactionUsecase.setRequesting(true);
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type));
            getAllTransactionUsecase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    getAllTransactionUsecase.setRequesting(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.toString());
                    hideLoading();
                    ErrorHandler.getErrorMessage(getView().getContext(), e);
                    if (e instanceof UnknownHostException ||
                            e instanceof SocketTimeoutException) {
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else {
                        getView().setActionsEnabled(true);
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    hideLoading();
                    if (!isViewAttached()) {
                        return;
                    }
                    getView().setActionsEnabled(true);

                    if (graphqlResponse != null &&
                            graphqlResponse.getData(GqlCompleteTransactionResponse.class) != null) {

                        GqlCompleteTransactionResponse gqlCompleteTransactionResponse =
                                graphqlResponse.getData(GqlCompleteTransactionResponse.class);

                        if (gqlCompleteTransactionResponse != null &&
                                !gqlCompleteTransactionResponse.getAllDepositHistory().isHaveError()) {
                            if (!isViewAttached() || getView().getBuyerHistoryAdapter() == null) {
                                return;
                            }

                            getView().getBuyerHistoryAdapter().addElement(
                                    gqlCompleteTransactionResponse.getAllDepositHistory().getDepositHistoryList());

                            ((BaseListFragment) getView().getBuyerSaldoHistoryTabItem().getFragment()).
                                    updateScrollListenerState(gqlCompleteTransactionResponse.getAllDepositHistory().isHaveNextPage());

                            if (getView().getBuyerHistoryAdapter().getItemCount() == 0) {
                                getView().getBuyerHistoryAdapter().addElement(getView().getDefaultEmptyViewModel());
                            }
                        } else {
                            if (gqlCompleteTransactionResponse != null && gqlCompleteTransactionResponse.
                                    getAllDepositHistory() != null) {
                                if (getView().getBuyerHistoryAdapter().getItemCount() == 0) {
                                    getView().showEmptyState(gqlCompleteTransactionResponse.
                                            getAllDepositHistory().getMessage());
                                } else {
                                    getView().setRetry(gqlCompleteTransactionResponse.
                                            getAllDepositHistory().getMessage());
                                }
                            }
                        }
                    } else {
                        if (getView().getAdapter() != null && getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                    finishLoading();
                }

            });

        } else {
            finishLoading();
        }
    }
}
