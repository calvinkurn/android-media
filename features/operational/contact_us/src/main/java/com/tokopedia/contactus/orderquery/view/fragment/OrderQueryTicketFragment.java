package com.tokopedia.contactus.orderquery.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.di.OrderQueryComponent;
import com.tokopedia.contactus.orderquery.view.PopularFiveArticleActivity;
import com.tokopedia.contactus.orderquery.view.QueryTicketDetailActivity;
import com.tokopedia.contactus.orderquery.view.SubmitTicketActivity;
import com.tokopedia.contactus.orderquery.view.customview.QueryTicketView;
import com.tokopedia.contactus.orderquery.view.presenter.OrderQueryTicketContract;
import com.tokopedia.contactus.orderquery.view.presenter.OrderQueryTicketPresenter;
import com.tokopedia.contactus.orderquery.di.DaggerOrderQueryComponent;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class OrderQueryTicketFragment extends BaseDaggerFragment implements OrderQueryTicketContract.View,HasComponent<OrderQueryComponent> {

    public static final String KEY_BUYER_PURCHASE_LIST = "BUYER_PURCHASE_LIST";

    OrderQueryComponent orderQueryComponent;

    private ImageView imgOrder;
    private TextView txtTicketInvoice;
    private TextView txtTicketTitle;
    private LinearLayout ticketList;

    @Inject
    OrderQueryTicketPresenter presenter;

    public static OrderQueryTicketFragment newInstance(BuyerPurchaseList buyerPurchaseList) {

        Bundle args = new Bundle();
        OrderQueryTicketFragment fragment = new OrderQueryTicketFragment();
        args.putSerializable(KEY_BUYER_PURCHASE_LIST,buyerPurchaseList);
        fragment.setArguments(args);
        return fragment;
    }
    BuyerPurchaseList buyerPurchaseList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_invoice_details, container, false);
        initInjector();
        presenter.attachView(this);
        imgOrder = view.findViewById(R.id.img_order);
        txtTicketInvoice = view.findViewById(R.id.txt_ticket_invoice);
        txtTicketTitle = view.findViewById(R.id.txt_ticket_title);
        ticketList = view.findViewById(R.id.ticket_list);
        buyerPurchaseList = (BuyerPurchaseList) getArguments().getSerializable(KEY_BUYER_PURCHASE_LIST);
        presenter.setBuyerPurchaseList(buyerPurchaseList);
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
    public void addTicket(final QueryTicket ticket) {

        QueryTicketView textView = new QueryTicketView(getContext());
        textView.setQueryTicket(ticket);
        ticketList.addView(textView);
        addPopularArticleDivider();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitTicketInvoiceData invoiceData = new SubmitTicketInvoiceData();
                invoiceData.setBuyerPurchaseList(buyerPurchaseList);
                invoiceData.setQueryTicket(ticket);
                if(!ticket.isIsSkipArticle()) {

                    getContext().startActivity(QueryTicketDetailActivity.getQueryTicketDetailActivity(getContext(), invoiceData));
                }else {
                    getContext().startActivity(SubmitTicketActivity.getSubmitTicketActivity(getContext(),invoiceData));
                }
            }
        });

    }

    @Override
    public void addPopularArticleDivider() {
        View divider = new View(getContext());
        Resources resources = getActivity().getResources();
        divider.setBackgroundColor(resources.getColor(R.color.grey_200));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) resources.getDimension(R.dimen.divider_height_thin));
        ticketList.addView(divider, layoutParams);
    }

    @Override
    public void setInvoiceNumber(String invoiceNumber) {
        txtTicketInvoice.setText(invoiceNumber);
    }

    @Override
    public void setPurchaseTitle(String title) {
        txtTicketTitle.setText(title);
    }

    @Override
    public void setOrderImage(String image) {
        ImageHandler.loadImageThumbs(getContext(),imgOrder,image);
    }

    @Override
    public OrderQueryComponent getComponent() {
        if(orderQueryComponent == null) {
            initInjector();
        }
        return orderQueryComponent;
    }
}
