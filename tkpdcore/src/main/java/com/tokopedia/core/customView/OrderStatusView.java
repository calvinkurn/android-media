package com.tokopedia.core.customView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.util.MethodChecker;

public class OrderStatusView {


    private View mView;
    private TextView mActor;
    private TextView mDate;
    private TextView mState;
    private TextView mComments;
    private LayoutInflater inflater;

    public static OrderStatusView createInstance(Context context, OrderHistory status) {
        OrderStatusView view = null;
            String actor = status.getHistoryActionBy();
            String date = status.getHistoryStatusDate();
            String state = status.getHistorySellerStatus();
            String comment = status.getHistoryComments();
            view = new OrderStatusView(context, actor, date, state, comment);

        return view;
    }

    public OrderStatusView(Context context, OrderHistory orderHistory) {
        this(context, orderHistory.getHistoryActionBy(),
                orderHistory.getHistoryStatusDateFull(),
                orderHistory.getHistoryBuyerStatus(),
                orderHistory.getHistoryComments());
    }


    public OrderStatusView(Context context, String ActorList, String DateList, String StateList,
                           String CommentList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.listview_order_status, null);
        mActor = (TextView) mView.findViewById(R.id.actor);
        mDate = (TextView) mView.findViewById(R.id.date);
        mState = (TextView) mView.findViewById(R.id.state);
        mComments = (TextView) mView.findViewById(R.id.comment);

        switch (ActorList) {
            case "Tokopedia":
            case "System-Tracker":
                mActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_gray));
                break;
            case "Buyer":
                mActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_orange));
                break;
            case "Seller":
                mActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));
                break;
        }
        mComments.setText(MethodChecker.fromHtml(CommentList));
        mComments.setVisibility(CommentList == null || CommentList.equals("null")
                || CommentList.equals("0") ? View.GONE : View.VISIBLE);
        mActor.setText(MethodChecker.fromHtml(ActorList));
        mDate.setText(MethodChecker.fromHtml(DateList));
        mState.setText(MethodChecker.fromHtml(StateList));
    }

    public View getView() {
        return mView;
    }
}
