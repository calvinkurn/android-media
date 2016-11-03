package com.tokopedia.tkpd.selling.view.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShippingList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 8/5/2016.
 */
public class OrderViewHolder extends BaseSellingViewHolder<OrderShippingList> {
    @Bind(R.id.user_name)
    TextView UserName;
    @Bind(R.id.user_avatar)
    ImageView UserAvatar;
    @Bind(R.id.deadline)
    TextView Deadline;
    @Bind(R.id.deadline_view)
    View DeadlineView;
    @Bind(R.id.invoice)
    TextView Invoice;
    @Bind(R.id.bounty)
    TextView TotalTransaksi;
    @Bind(R.id.main_view)
    View MainView;
    @Bind(R.id.order_date)
    TextView vOrderDate;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindDataModel(Context context, OrderShippingList model) {
        UserName.setText(model.getOrderCustomer().getCustomerName());
        CommonUtils.getProcessDay(context, model.getOrderPayment().getPaymentProcessDayLeft()+"", Deadline, DeadlineView);
        Invoice.setText(model.getOrderDetail().getDetailInvoice());
        vOrderDate.setText(model.getOrderDetail().getDetailOrderDate());
        TotalTransaksi.setText(model.getOrderPayment().getPaymentKomisi());
        ImageHandler.loadImageCircle2(context, UserAvatar, model.getOrderCustomer().getCustomerImage());
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        MainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        });
    }
}
