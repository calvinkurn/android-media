package com.tokopedia.contactus.inboxticket2.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.adapter.AttachmentAdapter;
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
    @BindView(R2.id.tv_message)
    TextView tvMessage;
    @BindView(R2.id.iv_profile)
    ImageView ivProfile;
    @BindView(R2.id.tv_message_time)
    TextView tvMsgTime;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.rv_attached_image)
    RecyclerView rvAttachment;


    @Override
    public void showCollapsedMessages() {

    }

    @Override
    public void hideMessages() {

    }

    @Override
    public void renderMessageList(Tickets ticketDetail) {
        tvTicketTitle.setText(ticketDetail.getSubject());

        if (ticketDetail.getStatus().equalsIgnoreCase("solved")) {
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
        if (!TextUtils.isEmpty(ticketDetail.getInvoice())) {
            tvIdNum.setText(String.format(getString(R.string.invoice_id), ticketDetail.getInvoice()));
            tvIdNum.setVisibility(View.VISIBLE);
        } else
            tvIdNum.setVisibility(View.GONE);

        tvMsgTime.setText(ticketDetail.getCreateTime());
        tvMsgTime.setVisibility(View.VISIBLE);
        tvMessage.setText(ticketDetail.getMessage());
        tvMessage.setVisibility(View.VISIBLE);
        tvName.setText(ticketDetail.getCreatedBy().getName());
        tvName.setVisibility(View.VISIBLE);
        ImageHandler imageHandler = new ImageHandler(this);
        ivProfile.setVisibility(View.VISIBLE);
        imageHandler.loadImage(ivProfile, ticketDetail.getCreatedBy().getPicture());

        if (ticketDetail.getAttachment() != null && ticketDetail.getAttachment().size() > 0) {
            AttachmentAdapter attachmentAdapter = new AttachmentAdapter(this, ticketDetail.getAttachment());
            rvAttachment.setAdapter(attachmentAdapter);
            rvAttachment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvAttachment.setVisibility(View.VISIBLE);
        }

        if (ticketDetail.getComments() != null && ticketDetail.getComments().size() > 0) {
            InboxDetailAdapter detailAdapter = new InboxDetailAdapter(this, ticketDetail.getComments(),
                    (InboxDetailContract.InboxDetailPresenter) mPresenter);
            rvMessageList.setAdapter(detailAdapter);
            rvMessageList.setVisibility(View.VISIBLE);
        } else {
            rvMessageList.setVisibility(View.GONE);
        }
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
