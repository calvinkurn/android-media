package com.tokopedia.contactus.orderquery.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.di.OrderQueryComponent;
import com.tokopedia.contactus.orderquery.view.SubmitTicketActivity;
import com.tokopedia.contactus.orderquery.view.presenter.QueryTicketDetailContract;
import com.tokopedia.contactus.orderquery.view.presenter.QueryTicketDetailPresenter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.contactus.orderquery.di.DaggerOrderQueryComponent;

import javax.inject.Inject;


/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class QueryTicketDetailFragment extends BaseDaggerFragment implements QueryTicketDetailContract.View {

    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";
    private TextView txtTitle;
    private WebView txtDetail;

    @Inject
    QueryTicketDetailPresenter presenter;

    OrderQueryComponent orderQueryComponent;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static QueryTicketDetailFragment newInstance(SubmitTicketInvoiceData queryTicket) {
        Bundle args = new Bundle();
        QueryTicketDetailFragment fragment = new QueryTicketDetailFragment();
        args.putSerializable(KEY_QUERY_TICKET, queryTicket);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        orderQueryComponent = DaggerOrderQueryComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderQueryComponent.inject(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_invoice_detail_article, container, false);
        initInjector();
        txtTitle = view.findViewById(R.id.txt_title);
        txtDetail = view.findViewById(R.id.txt_detail);
        presenter.attachView(this);
        view.findViewById(R.id.txt_hyper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });
        return view;

    }


    @Override
    public void setDetail(String details) {
        txtDetail.loadDataWithBaseURL(null, details, "text/html", "utf-8", null);
    }

    @Override
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    @Override
    public SubmitTicketInvoiceData getSubmitTicketInvoiceData() {
        return (SubmitTicketInvoiceData) getArguments().getSerializable(KEY_QUERY_TICKET);
    }

    public void onViewClicked() {
        ContactUsTracking.eventArticleHubungiKamiClick();
        getContext().startActivity(SubmitTicketActivity.getSubmitTicketActivity(getContext(), getSubmitTicketInvoiceData()));
    }
}
