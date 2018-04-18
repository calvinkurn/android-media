package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.di.OrderQueryComponent;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.di.DaggerOrderQueryComponent;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter.SubmitTicketContract;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter.SubmitTicketPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class SubmitTicketFragment extends BaseDaggerFragment implements SubmitTicketContract.View {

    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";
    @BindView(R2.id.img_product)
    ImageView imgProduct;
    @BindView(R2.id.txt_invoice_no)
    TextView txtInvoiceNo;
    @BindView(R2.id.txt_invoice_title)
    TextView txtInvoiceTitle;
    @BindView(R2.id.txt_query_title)
    TextView txtQueryTitle;
    @BindView(R2.id.edt_query)
    AppCompatEditText edtQuery;

    OrderQueryComponent orderQueryComponent;
    @Inject
    SubmitTicketPresenter presenter;


    public static SubmitTicketFragment newInstance(SubmitTicketInvoiceData submitTicketInvoiceData) {
        Bundle args = new Bundle();
        SubmitTicketFragment fragment = new SubmitTicketFragment();
        args.putSerializable(KEY_QUERY_TICKET, submitTicketInvoiceData);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_invoice_form, container, false);
        initInjector();
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        return view;

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        orderQueryComponent = DaggerOrderQueryComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderQueryComponent.inject(this);
    }

    @Override
    public String getDescription() {
        return edtQuery.getText().toString();
    }

    @Override
    public void setDescriptionError(String error) {

    }

    @Override
    public void setQueryTitle(String title) {
        txtQueryTitle.setText(title);
    }


    @Override
    public SubmitTicketInvoiceData getSubmitTicketInvoiceData() {
        return (SubmitTicketInvoiceData) getArguments().getSerializable(KEY_QUERY_TICKET);
    }

    @Override
    public void setInvoiceNumber(String number) {
        txtInvoiceNo.setText(number);
    }

    @Override
    public void setInvoiceTitle(String invoiceTitle) {
        txtInvoiceTitle.setText(invoiceTitle);
    }

    @Override
    public void setInvoiceImage(String imagePath) {
        ImageHandler.loadImageThumbs(getContext(),imgProduct,imagePath);
    }


}
