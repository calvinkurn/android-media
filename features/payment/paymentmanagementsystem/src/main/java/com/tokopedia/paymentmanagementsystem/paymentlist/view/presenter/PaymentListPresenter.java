package com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.common.Constant;
import com.tokopedia.paymentmanagementsystem.paymentlist.data.model.CancelDetail;
import com.tokopedia.paymentmanagementsystem.paymentlist.data.model.CancelPayment;
import com.tokopedia.paymentmanagementsystem.paymentlist.data.model.DataPaymentList;
import com.tokopedia.paymentmanagementsystem.paymentlist.data.model.PaymentList;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.mapper.PaymentListMapper;
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

    public void getPaymentList(Resources resources, Context context) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.payment_list_query), DataPaymentList.class);
        getPaymentListUseCase.setRequest(graphqlRequest);
        getPaymentListUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showGetListError(e);
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                DataPaymentList paymentList = objects.getData(DataPaymentList.class);
                getView().renderList(paymentListMapper.map(paymentList.getPaymentList().getPaymentList(), context));
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
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionID);
        variables.put(Constant.MERCHANT_CODE, merchantCode);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.get_cancel_detail), CancelDetail.class, variables);
        getCancelDetailUseCase.setRequest(graphqlRequest);
        getCancelDetailUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetCancelDetail(e);
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                CancelDetail cancelDetail = objects.getData(CancelDetail.class);
                getView().showCancelationMessage(cancelDetail.getRefundMessage(), transactionID, merchantCode);
            }
        });
    }

    @Override
    public void cancelPayment(Resources resources, String transactionID, String merchantCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionID);
        variables.put(Constant.MERCHANT_CODE, merchantCode);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.cancel_payment), CancelPayment.class, variables);
        cancelPaymentUseCase.setRequest(graphqlRequest);
        cancelPaymentUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorCancelPayment(e);
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                CancelPayment cancelPayment = objects.getData(CancelPayment.class);
                getView().onResultCancelPayment(cancelPayment.isSuccess());
            }
        });
    }

    public void getHowToPay() {

    }
}
