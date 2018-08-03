package com.tokopedia.contactus.inboxticket2.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.adapter.AttachmentAdapter;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by pranaymohapatra on 02/07/18.
 */
@RuntimePermissions
public class InboxDetailActivity extends InboxBaseActivity
        implements InboxDetailContract.InboxDetailView, ImageUploadAdapter.OnSelectImageClick {
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
    @BindView(R2.id.ed_message)
    EditText edMessage;
    @BindView(R2.id.send_progress)
    View sendProgress;
    @BindView(R2.id.view_help_rate)
    View viewHelpRate;
    @BindView(R2.id.text_toolbar)
    View textToolbar;
    @BindView(R2.id.view_link_bottom)
    View viewLinkBottom;

    private ImageUploadHandler imageUploadHandler;
    private ImageUploadAdapter imageUploadAdapter;

    private String rateCommentID;

    private AttachmentAdapter attachmentAdapter;
    private boolean isCustomReason;


    @Override
    public void showCollapsedMessages() {

    }

    @Override
    public void hideMessages() {

    }

    @Override
    public void renderMessageList(Tickets ticketDetail) {
        List<CommentsItem> commentsItems = ticketDetail.getComments();
        tvTicketTitle.setText(ticketDetail.getSubject());

        if (ticketDetail.isShowRating()) {
            viewHelpRate.setVisibility(View.VISIBLE);
            textToolbar.setVisibility(View.GONE);
            rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
        }

        if (ticketDetail.getStatus().equalsIgnoreCase("solved")) {
            tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_yellow);
            tvTicketStatus.setText(R.string.on_going);
            tvTicketStatus.setTextColor(getResources().getColor(R.color.black_38));
        } else if (ticketDetail.getStatus().equalsIgnoreCase("closed")
                && !ticketDetail.isShowRating()) {
            tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_grey);
            tvTicketStatus.setTextColor(getResources().getColor(R.color.black_38));
            tvTicketStatus.setText(R.string.closed);
            showIssueClosed();
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
            attachmentAdapter = new AttachmentAdapter(this, ticketDetail.getAttachment());
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
        imageUploadHandler = ImageUploadHandler.createInstance(this);
        imageUploadAdapter = new ImageUploadAdapter(this, this);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSelectedImages.setAdapter(imageUploadAdapter);
        edMessage.addTextChangedListener(((InboxDetailContract.InboxDetailPresenter) mPresenter).watcher());
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        imageUploadHandler.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(com.tokopedia.core.R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InboxDetailActivityPermissionsDispatcher.actionImagePickerWithCheck(
                        (InboxDetailActivity) getActivity());
            }
        });
        myAlertDialog.setNegativeButton(getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InboxDetailActivityPermissionsDispatcher.actionCameraWithCheck(
                        (InboxDetailActivity) getActivity());
            }


        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {
            int position = imageUploadAdapter.getItemCount();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId("image" + UUID.randomUUID().toString());

            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    image.setFileLoc(data.getStringExtra(ImageGallery.EXTRA_URL));
                    break;
                case Activity.RESULT_OK:
                    image.setFileLoc(imageUploadHandler.getCameraFileloc());
                    break;
                default:
                    break;
            }
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).onImageSelect(image);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InboxDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R2.id.iv_upload_img)
    void onClickUpload() {
        showImagePickerDialog();
    }

    @OnClick(R2.id.iv_send_button)
    void sendMessage() {
        if (!isCustomReason)
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).sendMessage();
        else {
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).sendCustomReason(edMessage.getText().toString().trim());
            isCustomReason = false;
            ivUploadImg.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.holder_top)
    void onClickHolderTop() {
        if (isTopCollapsed()) {
            toggleTop(View.VISIBLE);
        } else {
            toggleTop(View.GONE);
        }
    }

    @OnClick({R2.id.btn_no,
            R2.id.btn_yes,
            R2.id.txt_hyper})
    void onClickRate(View v) {
        if (v.getId() == R.id.btn_yes) {
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).clickRate(R.id.btn_yes, rateCommentID);
        } else if (v.getId() == R.id.btn_no) {
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).clickRate(R.id.btn_no, rateCommentID);
        } else if (v.getId() == R.id.txt_hyper) {
            setResult(RESULT_FINISH);
            finish();
        }
    }

    private void toggleTop(int visibility) {
        ivProfile.setVisibility(visibility);
        tvName.setVisibility(visibility);
        tvMsgTime.setVisibility(visibility);
        tvMessage.setVisibility(visibility);
        if (visibility != View.VISIBLE) {
            tvTicketTitle.setMaxLines(1);
            tvTicketTitle.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            tvTicketTitle.setMaxLines(Integer.MAX_VALUE);
            tvTicketTitle.setEllipsize(null);
        }
        if (attachmentAdapter != null && attachmentAdapter.getItemCount() > 0 && visibility == View.VISIBLE)
            rvAttachment.setVisibility(View.VISIBLE);
        else
            rvAttachment.setVisibility(View.GONE);
    }

    private boolean isTopCollapsed() {
        boolean collapsed;
        collapsed = ivProfile.getVisibility() == View.GONE;
        return collapsed;
    }


    @Override
    public void addimage(ImageUpload image) {
        if (rvSelectedImages.getVisibility() != View.VISIBLE) {
            rvSelectedImages.setVisibility(View.VISIBLE);
        }
        imageUploadAdapter.addImage(image);
    }

    @Override
    public void showSendProgress() {
        sendProgress.setVisibility(View.VISIBLE);
        ivSendButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideSendProgress() {
        sendProgress.setVisibility(View.GONE);
        ivSendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleTextToolbar(int visibility) {
        if (visibility == View.VISIBLE)
            viewHelpRate.setVisibility(View.GONE);
        else
            viewHelpRate.setVisibility(View.VISIBLE);
        textToolbar.setVisibility(visibility);
    }

    @Override
    public void askCustomReason() {
        ivUploadImg.setVisibility(View.GONE);
        rvSelectedImages.setVisibility(View.GONE);
        edMessage.clearComposingText();
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);
        isCustomReason = true;
    }

    @Override
    public void showIssueClosed() {
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.GONE);
        tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_grey);
        tvTicketStatus.setTextColor(getResources().getColor(R.color.black_38));
        tvTicketStatus.setText(R.string.closed);
        viewLinkBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        ivSendButton.setClickable(enabled);
        if (enabled) {
            ivSendButton.setColorFilter(getResources().getColor(R.color.green_nob));
        } else {
            ivSendButton.setColorFilter(getResources().getColor(R.color.grey_300));
        }
    }

    @Override
    public List<ImageUpload> getImageList() {
        return imageUploadAdapter.getImageUpload();
    }

    @Override
    public String getUserMessage() {
        return edMessage.getText().toString();
    }

    @Override
    public void onClick() {
        showImagePickerDialog();
    }
}
