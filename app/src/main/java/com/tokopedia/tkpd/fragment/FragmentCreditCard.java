package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.V2BaseFragment;
import com.tokopedia.tkpd.facade.FacadeCreditCard;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.TkpdNetworkURLHandler;
import com.tokopedia.tkpd.payment.interactor.PaymentNetInteractor;
import com.tokopedia.tkpd.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.tkpd.payment.model.responsecartstep1.InstallmentBankOption;
import com.tokopedia.tkpd.payment.model.responsecreditcardstep1.DataCredit;
import com.tokopedia.tkpd.util.CreditCardUtils;
import com.tokopedia.tkpd.var.TkpdUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tkpd_Eka on 5/22/2015.
 */
public class FragmentCreditCard extends V2BaseFragment {

    public boolean isInstallment;
    public boolean isWSV4;
    private PaymentNetInteractorImpl netInteractor;

    public static FragmentCreditCard createInstance(FragmentCreditCard.Model model) {
        FragmentCreditCard fragment = new FragmentCreditCard();
        fragment.model = model;
        return fragment;
    }

    public static FragmentCreditCard createInstanceWSV4(FragmentCreditCard.Model model, boolean installment) {
        FragmentCreditCard fragment = new FragmentCreditCard();
        fragment.model = model;
        fragment.isInstallment = installment;
        fragment.model.ccModel.installment = installment;
        fragment.isWSV4 = true;
        return fragment;
    }

    public static class Model {
        public CreditCardUtils.Model ccModel = new CreditCardUtils.Model();
        public String clientKey;
        public String paymentAmount;
        public String tokenCart;
        public String email;
        public String cardType;
        public String transactionID;
        public ArrayList<String> BankInstallmentOptions = new ArrayList<>();
        public List<InstallmentBankOption> installmentBankOptionList = new ArrayList<>();
    }

    private class ViewHolder {
        FrameLayout form;
        View submit;
        TextView onProgressButton;
    }

    private Model model;
    private CreditCardUtils ccUtil;
    private ViewHolder holder;
    private FacadeCreditCard facadeAction;
    private TkpdProgressDialog progress;
    public static final String sprintAsiaMarker = "sprintAsia";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    private void initVar() {
        holder = new ViewHolder();
        facadeAction = FacadeCreditCard.createInstance(getActivity());
        netInteractor = new PaymentNetInteractorImpl();
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder.form = (FrameLayout) findViewById(R.id.form);
        holder.submit = findViewById(R.id.submit_button);
        holder.onProgressButton = (TextView) findViewById(R.id.credit_card_on_process);
    }

    @Override
    protected void setListener() {
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSubmit();
            }
        });
    }

    private void actionStep2(String token) {
        if (getActivity() != null)
            progress.showDialog();
        facadeAction.actionVeritransStep2(token, model, new FacadeCreditCard.OnVeritransStep2Listener() {
            @Override
            public void onSuccess(String result) {
                onSuccessVeritransStep2(result);
            }

            @Override
            public void onFailure() {
                CreditCardFailed();
            }
        });
    }

    private void onSuccessVeritransStep2(String result) {
        progress.dismiss();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void actionSubmit() {
        try {
            hideKeyboard();
            progress = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            model.ccModel = ccUtil.getModel();
            progress.showDialog();
            holder.submit.setVisibility(View.GONE);
            holder.onProgressButton.setVisibility(View.VISIBLE);
            postCreditCardPayment(model.ccModel);
            //           facadeAction.actionProcess(model.ccModel, onActionSubmitListener());
        } catch (CreditCardUtils.CreditCardFormException e) {
            e.printStackTrace();
        }
    }

    private void postCreditCardPayment(final CreditCardUtils.Model ccModel) {
        Map<String, String> param = new HashMap<>();
        param.put("first_name", ccModel.forename);
        param.put("last_name", ccModel.surname);
        param.put("city", ccModel.city);
        param.put("postal_code", ccModel.postCode);
        param.put("address_street", ccModel.address);
        param.put("phone", ccModel.phone);
        param.put("state", ccModel.province);
        param.put("card_number", ccModel.ccNumber);
        param.put("credit_card_edit_flag", ccModel.ccEdited + "");
        if (ccModel.installment) {
            param.put("installment_term", ccModel.term);
            param.put("installment_bank", ccModel.bankId);
        }
        netInteractor.postStep1CreditCard(getActivity(), param,
                new PaymentNetInteractor.OnStep1CreditCard() {
                    @Override
                    public void onSuccessSprintAsia(DataCredit dataCredit) {
                        progress.dismiss();
                        model.email = dataCredit.getUserEmail();
                        model.transactionID = dataCredit.getPaymentId();
                        model.cardType = dataCredit.getCcType();
                        String sprintAsiaURL = TkpdNetworkURLHandler.generateURL(getActivity(), TkpdUrl.TX_PAYMENT_SPRINTASIA);
                        FragmentSprintAsiaWebView fragmentSprintAsia = FragmentSprintAsiaWebView.createInstance(getActivity(), sprintAsiaURL, model, facadeAction);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_view, fragmentSprintAsia);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onSuccessVeritrans(DataCredit dataCredit) {
                        progress.dismiss();
                        FragmentVeritransView fragmentVeritransView = FragmentVeritransView.createInstance(getActivity());
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_view, fragmentVeritransView);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        if (model.ccModel.installment) {
                            ccUtil.actionVeritransInstallment(model.ccModel, model.clientKey,
                                    model.paymentAmount, fragmentVeritransView);
                        } else {
                            model.ccModel.bankTypeName = dataCredit.getCcCardBankType();
                            ccUtil.actionVeritrans(model.ccModel, model.clientKey,
                                    model.paymentAmount, fragmentVeritransView);
                        }
                    }


                    @Override
                    public void onError(String message) {
                        progress.dismiss();
                        holder.submit.setVisibility(View.VISIBLE);
                        holder.onProgressButton.setVisibility(View.GONE);
                        NetworkErrorHelper.showSnackbar(getActivity(), message);
                    }

                    @Override
                    public void onTimeout(String message) {
                        progress.dismiss();
                        holder.submit.setVisibility(View.VISIBLE);
                        holder.onProgressButton.setVisibility(View.GONE);
                        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        actionSubmit();
                                    }
                                }).showRetrySnackbar();
                    }

                    @Override
                    public void onNoConnection() {
                        progress.dismiss();
                        holder.submit.setVisibility(View.VISIBLE);
                        holder.onProgressButton.setVisibility(View.GONE);
                        NetworkErrorHelper.showDialog(getActivity(),
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        actionSubmit();
                                    }
                                });
                    }
                });
    }

    private FacadeCreditCard.OnActionProcessListener onActionSubmitListener() {
        return new FacadeCreditCard.OnActionProcessListener() {
            @Override
            public void onVeritrans(String ccBank) {
                progress.dismiss();
                FragmentVeritransView fragmentVeritransView = FragmentVeritransView.createInstance(getActivity());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragmentVeritransView);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (model.ccModel.installment) {
                    ccUtil.actionVeritransInstallment(model.ccModel, model.clientKey, model.paymentAmount, fragmentVeritransView);
                } else {
                    model.ccModel.bankTypeName = ccBank;
                    ccUtil.actionVeritrans(model.ccModel, model.clientKey, model.paymentAmount, fragmentVeritransView);
                }
            }

            @Override
            public void onSprintAsia(String email, String transactionID, String ccType) {
                progress.dismiss();
                model.email = email;
                model.transactionID = transactionID;
                model.cardType = ccType;
                String sprintAsiaURL = TkpdNetworkURLHandler.generateURL(getActivity(), TkpdUrl.TX_PAYMENT_SPRINTASIA);
                FragmentSprintAsiaWebView fragmentSprintAsia = FragmentSprintAsiaWebView.createInstance(getActivity(), sprintAsiaURL, model, facadeAction);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragmentSprintAsia);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
/*                facadeAction.actionSprintAsia(model, new FacadeCreditCard.OnSprintAsiaFinish() {
                    @Override
                    public void onTransactionSuccess() {
                        SuccessSprintAsia();
                    }

                    @Override
                    public void onTransactionFailed() {
                        CreditCardFailed();
                    }
                });*/// TODO PARAMSSSSSS
            }

            @Override
            public void onFailure() {
                progress.dismiss();
                holder.submit.setVisibility(View.VISIBLE);
                holder.onProgressButton.setVisibility(View.GONE);
            }
        };
    }

    private void CreditCardFailed() {
        progress.dismiss();
        Intent intent = new Intent();
        intent.putExtra("result", "Failed");
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().finish();
    }

    @Override
    protected void onCreateView() {
        if (isWSV4) {
            ccUtil = isInstallment ? CreditCardUtils.createInstallmentInstanceWSV4(getActivity(),
                    holder.form, model.ccModel, model.installmentBankOptionList)
                    : CreditCardUtils.createInstanceWSV4(getActivity(), holder.form, model.ccModel);
            ccUtil.insertFormCreditInformation();
            ccUtil.setListener(onCCListener());
        } else {
            ccUtil = CreditCardUtils.createInstance(getActivity(), holder.form, model.ccModel);
            if (!model.BankInstallmentOptions.isEmpty()) {
                ccUtil = CreditCardUtils.createInstallmentInstance(getActivity(), holder.form, model.ccModel, model.BankInstallmentOptions);
            }
            ccUtil.insertFormCreditInformation();
            ccUtil.setListener(onCCListener());
        }

    }

    private CreditCardUtils.CreditCardUtilListener onCCListener() {
        return new CreditCardUtils.CreditCardUtilListener() {
            @Override
            public void onGetVeritransToken(String token) {
                actionStep2(token);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_credit_card;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
