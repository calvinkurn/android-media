package com.tokopedia.pms.payment.view.presenter;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.R;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.data.model.DataCancelDetail;
import com.tokopedia.pms.payment.data.model.DataCancelPayment;
import com.tokopedia.pms.payment.data.model.DataPaymentList;
import com.tokopedia.pms.payment.view.mapper.PaymentListMapper;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListPresenter extends BaseDaggerPresenter<PaymentListContract.View> implements PaymentListContract.Presenter {

    private GraphqlUseCase getPaymentListUseCase;
    private PaymentListMapper paymentListMapper;
    private GraphqlUseCase getCancelDetailUseCase;
    private GraphqlUseCase cancelPaymentUseCase;

    public PaymentListPresenter(GraphqlUseCase getPaymentListUseCase, PaymentListMapper paymentListMapper,
                                GraphqlUseCase getCancelDetailUseCase, GraphqlUseCase cancelPaymentUseCase) {
        this.getPaymentListUseCase = getPaymentListUseCase;
        this.paymentListMapper = paymentListMapper;
        this.getCancelDetailUseCase = getCancelDetailUseCase;
        this.cancelPaymentUseCase = cancelPaymentUseCase;
    }

    public void getPaymentList(Resources resources, Context context, String cursor) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.CURSOR, cursor);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.payment_list_query), DataPaymentList.class, variables, false);
        getPaymentListUseCase.clearRequest();
        getPaymentListUseCase.setRequest(graphqlRequest);
        getPaymentListUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                DataPaymentList paymentList = objects.getData(DataPaymentList.class);
                getView().renderList(paymentListMapper.map(paymentList.getPaymentList().getPaymentList(), context),
                        paymentList.getPaymentList().isHasNextPage(), paymentList.getPaymentList().getLastCursor());
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getPaymentListUseCase.unsubscribe();
        getCancelDetailUseCase.unsubscribe();
        cancelPaymentUseCase.unsubscribe();
    }

    @Override
    public void getCancelDetail(Resources resources, String transactionID, String merchantCode) {
        getView().showDialogLoading();
        getCancelDetailUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionID);
        variables.put(Constant.MERCHANT_CODE, merchantCode);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.get_cancel_detail), DataCancelDetail.class, variables, false);
        getCancelDetailUseCase.setRequest(graphqlRequest);
        getCancelDetailUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().hideDialogLoading();
                    getView().onErrorGetCancelDetail(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                getView().hideDialogLoading();
                DataCancelDetail cancelDetail = objects.getData(DataCancelDetail.class);
                getView().showCancelationMessage(cancelDetail.getCancelDetail().getRefundMessage(), transactionID, merchantCode);
            }
        });
    }

    @Override
    public void cancelPayment(Resources resources, String transactionID, String merchantCode) {
        getView().showDialogLoading();
        cancelPaymentUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionID);
        variables.put(Constant.MERCHANT_CODE, merchantCode);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.cancel_payment), DataCancelPayment.class, variables, false);
        cancelPaymentUseCase.setRequest(graphqlRequest);
        cancelPaymentUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onErrorCancelPayment(e);
                    getView().hideDialogLoading();
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                getView().hideDialogLoading();
                DataCancelPayment cancelPayment = objects.getData(DataCancelPayment.class);
                getView().onResultCancelPayment(cancelPayment.getCancelPayment().isSuccess(), cancelPayment.getCancelPayment().getMessage());
            }
        });
    }
}
