package com.tokopedia.contactus.inboxticket2.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;

import java.lang.reflect.Type;

import butterknife.BindView;

/**
 * Created by pranaymohapatra on 02/07/18.
 */

public class InboxDetailActivity extends InboxBaseActivity
        implements InboxDetailContract.InboxDetailView {
    @BindView(R2.id.tv_ticket_title)
    TextView tvTicketTitle;
    @BindView(R2.id.tv_ticket_status)
    TextView tvTicketStatus;
    @BindView(R2.id.tv_id_num)
    TextView tvIdNum;
    @BindView(R2.id.rv_message_list)
    RecyclerView rvMessageList;
    @BindView(R2.id.rv_selected_images)
    RecyclerView rvSelectedImages;
    @BindView(R2.id.divider_rv)
    View dividerRv;
    @BindView(R2.id.iv_upload_img)
    ImageView ivUploadImg;
    @BindView(R2.id.iv_send_button)
    ImageView ivSendButton;
    @BindView(R2.id.tv_view_transaction)
    TextView viewTransaction;

    @Override
    public void showCollapsedMessages() {

    }

    @Override
    public void hideMessages() {

    }

    @Override
    public void renderMessageList(Tickets ticketDetail) {
        tvTicketTitle.setText(ticketDetail.getSubject());

        if (ticketDetail.getStatus().equalsIgnoreCase("dalam proses")) {
            tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_yellow);
            tvTicketStatus.setText(R.string.on_going);
            tvTicketStatus.setTextColor(getResources().getColor(R.color.black_38));
        } else if (ticketDetail.getStatus().equalsIgnoreCase("closed")
                && !ticketDetail.isShowRating()) {
            tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_grey);
            tvTicketStatus.setTextColor(getResources().getColor(R.color.black_38));
            tvTicketStatus.setText(R.string.closed);
        } else if (ticketDetail.isShowRating()) {
            tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_orange);
            tvTicketStatus.setTextColor(getResources().getColor(R.color.red_150));
            tvTicketStatus.setText(R.string.need_rating);
        }

        tvIdNum.setText(String.format(getString(R.string.invoice_id), ticketDetail.getInvoice()));
        InboxDetailAdapter detailAdapter = new InboxDetailAdapter(this, ticketDetail.getComments(),
                (InboxDetailContract.InboxDetailPresenter) mPresenter);
        rvMessageList.setAdapter(detailAdapter);
    }

    @Override
    public void toggleSearch(int visibility) {

    }

    @Override
    public void updateDataSet() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_ticket_details;
    }

    @Override
    Type getType() {
        return InboxDetailActivity.class;
    }

    @Override
    void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);
    }

    @Override
    int getMenuRes() {
        return R.menu.contactus_menu_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
