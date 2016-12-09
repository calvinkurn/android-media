package com.tokopedia.core.product.dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.report.ReportProductPass;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.session.model.network.ReportType;
import com.tokopedia.core.session.model.network.ReportTypeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by stevenfredian on 7/4/16.
 */
public class ReportProductDialogFragment extends DialogFragment implements ReportProductDialogView{

    ProductDetailData productDetailData;

    @BindView(R2.id.cancel_but)
    TextView cancelButton;
    @BindView(R2.id.report)
    TextView reportButton;
    @BindView(R2.id.report_desc)
    EditText reportDesc;
    @BindView(R2.id.wrapper)
    TextInputLayout wrapper;
    @BindView(R2.id.report_type)
    Spinner reportTypeSpinner;
    @BindView(R2.id.error_spinner)
    TextView errorSpinner;
    @BindView(R2.id.dummy_spinner)
    TextView dummySpinner;
    @BindView(R2.id.redirect_text)
    TextView redirectText;
    @BindView(R2.id.scrollView)
    View mainLayout;
    @BindView(R2.id.action_layout)
    View actionLayout;
    @BindView(R2.id.progressBar)
    ProgressBar progressBar;

    List<ReportType> reportTypeList;
    ProductDetailFragment listener;
    ArrayAdapter<String> reportTypeName;
    private Bundle recentBundle;
    private CacheInteractor cacheInteractor;
    private RetrofitInteractor retrofitInteractor;

    public static ReportProductDialogFragment createInstance(ProductDetailData productData, Bundle recentBundle) {
        ReportProductDialogFragment fragment = new ReportProductDialogFragment();
        fragment.productDetailData = productData;
        fragment.recentBundle = recentBundle;
        fragment.retrofitInteractor = new RetrofitInteractorImpl();
        fragment.cacheInteractor = new CacheInteractorImpl();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.dialog_report_product, container);
        ButterKnife.bind(this, view);
        setContent();
        return view;
    }

    private void setContent() {
        showLayout(false);
        cacheInteractor.loadReportTypeFromCache(this);
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @OnClick(R2.id.cancel_but)
    public void dismissDialog() {
        dismiss();
    }

    @OnClick(R2.id.report)
    public void report() {
        resetError();
        if (isValidForm()) {
            doReport();
            reportButton.setEnabled(false);
        }
    }

    private boolean isValidForm() {
        if (reportDesc.getText().toString().trim().length() > 0) {
            return true;
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(getString(R.string.empty_desc));
            return false;
        }
    }

    @OnItemSelected(R2.id.report_type)
    public void onItemSpinnerSelected() {
        errorSpinner.setVisibility(View.GONE);
        ReportType reportChose = reportTypeList.get(reportTypeSpinner.getSelectedItemPosition());
        int response = reportChose.getReportResponse();

        if (response == 0) {
            wrapper.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);
            redirectText.setVisibility(View.VISIBLE);
            String link = reportChose.getReportUrl();
            setRedirect(reportTypeName.getItem(reportTypeSpinner.getSelectedItemPosition()), false, link);
        }else {
            wrapper.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.VISIBLE);
            redirectText.setVisibility(View.GONE);
        }
    }

    private void setRedirect(String item, boolean status, String link) {
        String string = getResources().getString(R.string.redirect_report_product);

        String caseReplace = "kasus";
        SpannableString stringNoResult = new SpannableString(string.replace(caseReplace, item));

        String linkStatus = "link ini";
        if(link.equals("https://www.tokopedia.com/contact-us.pl")){
            status=true;
        }

        Intent intent = InboxRouter.getContactUsActivityIntent(getActivity());

        stringNoResult.setSpan(redirect(intent, status, link)
                , stringNoResult.toString().indexOf(linkStatus)
                , stringNoResult.toString().indexOf(linkStatus) + linkStatus.length(), 0);

        redirectText.setMovementMethod(LinkMovementMethod.getInstance());
        redirectText.setText(stringNoResult);
    }

    private ClickableSpan redirect(final Intent intent, final boolean status, final String link) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                intent.putExtra("PARAM_REDIRECT", status);
                intent.putExtra("PARAM_URL", link);
                getActivity().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getActivity(), R.color.blue_link));
            }
        };
    }

    @OnClick(R2.id.dummy_spinner)
    public void onDummySpinnerClicked() {
        dummySpinner.setVisibility(View.GONE);
        reportTypeSpinner.setVisibility(View.VISIBLE);
        reportTypeSpinner.performClick();
    }

    private void resetError() {
        errorSpinner.setVisibility(View.GONE);
        wrapper.setErrorEnabled(false);
        wrapper.setError(null);
    }

    private void doReport() {
        ReportProductPass pass = new ReportProductPass();
        pass.setProductID(String.valueOf(productDetailData.getInfo().getProductId()));
        int type = reportTypeList.get(reportTypeSpinner.getSelectedItemPosition()).getReportId();
        pass.setReportType(String.valueOf(type));
        pass.setDesc(reportDesc.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReportProductPass.TAG, pass);
        ((ProductInfoActivity) getActivity()).doReport(bundle);
        listener.setRecentBundle(bundle);
    }

    public void onErrorAction(String error) {
        if (error.toLowerCase().contains("kategori")) {
            errorSpinner.setVisibility(View.VISIBLE);
            errorSpinner.setText(error);
        } else if (error.toLowerCase().contains("deskripsi")) {
            wrapper.setErrorEnabled(true);
            wrapper.setError(error);
        } else {
            SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
            dismiss();
        }
    }


    public void saveToCache(String data) {
        cacheInteractor.saveReportTypeToCache(data);
    }


    @Override
    public void downloadReportType() {
        retrofitInteractor.downloadReportType(getActivity(),productDetailData.getInfo().getProductId(),this);
    }

    public void setReportAdapterFromCache(String data) {
        ReportTypeModel reportTypeModel = new GsonBuilder().create().fromJson(data, ReportTypeModel.class);
        reportTypeList = reportTypeModel.getReportType();
        setReportAdapterFromNetwork(reportTypeList);
    }

    @Override
    public void showError(String errorString) {
        SnackbarManager.make(getActivity(),errorString, Snackbar.LENGTH_LONG).show();
        dismiss();
    }

    public void setReportAdapterFromNetwork(List<ReportType> reportTypeList) {
        showLayout(true);
        List<String> reportNameList = new ArrayList<>();
        List<Integer> reportTypeValue = new ArrayList<>();
        for (ReportType reportType : reportTypeList) {
            reportNameList.add(reportType.getReportTitle());
            reportTypeValue.add(reportType.getReportId());
        }
        this.reportTypeList = reportTypeList;
        reportTypeName = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, reportNameList);
        reportTypeSpinner.setAdapter(reportTypeName);
        if (recentBundle != null) {
            ReportProductPass pass = (ReportProductPass) recentBundle.get(ReportProductPass.TAG);
            reportDesc.setText(pass.getDesc());
            int index = reportTypeValue.indexOf(Integer.parseInt(pass.getReportType()));
            reportTypeSpinner.setSelection(index);
        }
    }

    private void showLayout(boolean status) {
        if(status) {
            mainLayout.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else {
            mainLayout.setVisibility(View.GONE);
            actionLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setListener(ProductDetailFragment productDetailFragment) {
        listener = productDetailFragment;
    }
}
