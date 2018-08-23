package com.tokopedia.contactus.inboxticket2.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.ImageViewerFragment;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pranaymohapatra on 02/07/18.
 */
@RuntimePermissions
public class InboxDetailActivity extends InboxBaseActivity
        implements InboxDetailContract.InboxDetailView, ImageUploadAdapter.OnSelectImageClick {
    @BindView(R2.id.tv_ticket_title)
    TextView tvTicketTitle;
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
    @BindView(R2.id.custom_search)
    CustomEditText editText;
    @BindView(R2.id.close_search)
    View clearSearch;
    @BindView(R2.id.inbox_search_view)
    View searchView;
    @BindView(R2.id.iv_previous_up)
    View ivPrevious;
    @BindView(R2.id.iv_next_down)
    View ivNext;

    private ImageUploadHandler imageUploadHandler;
    private ImageUploadAdapter imageUploadAdapter;
    private InboxDetailAdapter detailAdapter;

    private String rateCommentID;

    private boolean isCustomReason;

    public static final String PARAM_TICKET_ID = "ticket_id";

    @DeepLink(ApplinkConst.TICKET_DETAIL)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent = new Intent(context, InboxListActivity.class);
        String ticketId = bundle.getString(PARAM_TICKET_ID, "");
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(getIntent(context, ticketId));
        return taskStackBuilder;
    }

    public static Intent getIntent(Context context, String ticketId) {
        Intent intent = new Intent(context, InboxDetailActivity.class);
        intent.putExtra(PARAM_TICKET_ID, ticketId);
        return intent;
    }


    @Override
    public void showCollapsedMessages() {

    }

    @Override
    public void hideMessages() {

    }

    @Override
    public void renderMessageList(Tickets ticketDetail) {
        List<CommentsItem> commentsItems = ticketDetail.getComments();
        Utils utils = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getUtils();

        edMessage.getText().clear();

        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);

        if (ticketDetail.getStatus().equalsIgnoreCase("solved")
                || ticketDetail.getStatus().equalsIgnoreCase("open")) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.on_going),
                    getResources().getColor(R.color.yellow_110),
                    getResources().getColor(R.color.black_38), 11));

        } else if (ticketDetail.getStatus().equalsIgnoreCase("closed")
                && !ticketDetail.isShowRating()) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.closed),
                    getResources().getColor(R.color.grey_200),
                    getResources().getColor(R.color.black_38), 11));
            showIssueClosed();

        } else if (ticketDetail.isShowRating()) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.need_rating),
                    getResources().getColor(R.color.red_30),
                    getResources().getColor(R.color.red_150), 11));
            viewHelpRate.setVisibility(View.VISIBLE);
            textToolbar.setVisibility(View.GONE);
            rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
            rvMessageList.setPadding(0, 0, 0, viewHelpRate.getHeight());
        }

        if (!TextUtils.isEmpty(ticketDetail.getNumber())) {
            tvIdNum.setText(String.format(getString(R.string.invoice_id), ticketDetail.getNumber()));
            tvIdNum.setVisibility(View.VISIBLE);
        } else
            tvIdNum.setVisibility(View.GONE);

        if (ticketDetail.getComments() != null && ticketDetail.getComments().size() > 0) {
            detailAdapter = new InboxDetailAdapter(this, ticketDetail.getComments(),
                    (InboxDetailContract.InboxDetailPresenter) mPresenter);
            rvMessageList.setAdapter(detailAdapter);
            rvMessageList.setVisibility(View.VISIBLE);
        } else {
            rvMessageList.setVisibility(View.GONE);
        }
        scrollTo(detailAdapter.getItemCount() - 1);
    }

    @Override
    public void toggleSearch(int visibility) {
        searchView.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            mMenu.findItem(R.id.action_search).setVisible(false);
        } else {
            mMenu.findItem(R.id.action_search).setVisible(true);
        }
    }

    @Override
    public void updateDataSet() {

    }

    @Override
    public void clearSearch() {
        editText.setText("");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_ticket_details_activity;
    }

    @Override
    InboxBaseContract.InboxBasePresenter getPresenter() {
        return component.getInboxDetailPresenter();
    }

    @Override
    void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);
        editText.setListener(((InboxDetailContract.InboxDetailPresenter) mPresenter).getSearchListener());
    }

    @Override
    int getMenuRes() {
        return R.menu.contactus_menu_details;
    }

    @Override
    boolean doNeedReattach() {
        return false;
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
        //imageUploadHandler.actionImagePicker();
    }

    private void showImagePickerDialog() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true,
                null, null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_IMAGE_PICKER)
                && (resultCode == Activity.RESULT_OK)) {
            ArrayList<String> imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS);
            if (imagePathList == null || imagePathList.size() <= 0) {
                return;
            }
            String imagePath = imagePathList.get(0);
            if (!TextUtils.isEmpty(imagePath)) {
                int position = imageUploadAdapter.getItemCount();
                ImageUpload image = new ImageUpload();
                image.setPosition(position);
                image.setImageId("image" + UUID.randomUUID().toString());
                image.setFileLoc(imagePath);
                ((InboxDetailContract.InboxDetailPresenter) mPresenter).onImageSelect(image);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InboxDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R2.id.iv_upload_img)
    void onClickUpload() {
        if (rvSelectedImages.getVisibility() != View.VISIBLE)
            showImagePickerDialog();
        else
            rvSelectedImages.setVisibility(View.GONE);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                "");
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
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickSubmitReply,
                "");
    }

    @OnClick({R2.id.btn_no,
            R2.id.btn_yes,
            R2.id.txt_hyper,
            R2.id.close_search,
            R2.id.tv_view_transaction})
    void onClickListener(View v) {
        int id = v.getId();
        if (id == R.id.btn_yes) {
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).clickRate(R.id.btn_yes, rateCommentID);
        } else if (id == R.id.btn_no) {
            ((InboxDetailContract.InboxDetailPresenter) mPresenter).clickRate(R.id.btn_no, rateCommentID);
        } else if (id == R.id.txt_hyper) {
            setResult(RESULT_FINISH);
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickHubungi,
                    InboxTicketTracking.Label.TicketClosed);
            finish();
        } else if (id == R.id.close_search) {
            mPresenter.clickCloseSearch();
        } else if (id == R.id.tv_view_transaction) {
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickDetailTrasanksi,
                    "");
        }
    }

    @OnClick({R2.id.iv_next_down,
            R2.id.iv_previous_up})
    void onClickNextPrev(View v) {
        int id = v.getId();
        int index;
        if (id == R.id.iv_next_down) {
            index = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getNextResult();
        } else {
            index = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getPreviousResult();
        }
        scrollToResult(index);
    }

    private void scrollToResult(int index) {
        if (index != -1) {
            rvMessageList.smoothScrollToPosition(index);
        }

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
        View view = textToolbar;
        if (visibility == View.VISIBLE) {
            viewHelpRate.setVisibility(View.GONE);
        } else {
            viewHelpRate.setVisibility(View.VISIBLE);
            view = viewHelpRate;
        }
        rvMessageList.setPadding(0, 0, 0, view.getHeight());
        textToolbar.setVisibility(visibility);
    }

    @Override
    public void askCustomReason() {
        ivUploadImg.setVisibility(View.GONE);
        rvSelectedImages.setVisibility(View.GONE);
        edMessage.clearComposingText();
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0, textToolbar.getHeight());
        isCustomReason = true;
    }

    @Override
    public void showIssueClosed() {
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.GONE);
        viewLinkBottom.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0, viewLinkBottom.getHeight());
    }

    @Override
    public void enterSearchMode(String search) {
        textToolbar.setVisibility(View.GONE);
        viewHelpRate.setVisibility(View.GONE);
        viewLinkBottom.setVisibility(View.GONE);
        detailAdapter.enterSearchMode(search);
        rvMessageList.setPadding(0, 0, 0, 0);
    }

    @Override
    public void exitSearchMode() {
        detailAdapter.exitSearchMode();
    }

    @Override
    public void showImagePreview(int position, ArrayList<String> imagesURL) {
        ImageViewerFragment imageViewerFragment = (ImageViewerFragment) getSupportFragmentManager().findFragmentByTag(ImageViewerFragment.TAG);
        if (imageViewerFragment == null) {
            imageViewerFragment = ImageViewerFragment.newInstance(position, imagesURL);
        } else {
            imageViewerFragment.setImageData(position, imagesURL);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, imageViewerFragment, ImageViewerFragment.TAG);
        transaction.addToBackStack(ImageViewerFragment.TAG);
        transaction.commit();
    }

    @Override
    public boolean isSearchEmpty() {
        return editText.getText().length() <= 0;
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

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void scrollTo(int position) {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (((LinearLayoutManager) rvMessageList.getLayoutManager()).findFirstCompletelyVisibleItemPosition() != position)
                            rvMessageList.smoothScrollToPosition(position);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (imageUploadAdapter.getItemCount() > 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.title_dialog_wrong_scan));
            builder.setMessage("Pesan Anda akan hilang jika menutup halaman ini, Anda yakin?");
            builder.setNegativeButton(getString(R.string.batal),
                    (dialog, i) -> {
                        dialog.dismiss();
                        //presenter.onRetryClick();
                    });
            builder.setPositiveButton(getString(R.string.keular),
                    (dialogInterface, i) -> {
                        ContactUsTracking.sendGTMInboxTicket("",
                                InboxTicketTracking.Category.EventInboxTicket,
                                InboxTicketTracking.Action.EventAbandonReplySubmission,
                                getString(R.string.batal));
                        getActivity().finish();
                    }).create().show();
        } else {
            if (textToolbar.getVisibility() == View.VISIBLE && edMessage.isFocused()) {
                ContactUsTracking.sendGTMInboxTicket("",
                        InboxTicketTracking.Category.EventInboxTicket,
                        InboxTicketTracking.Action.EventAbandonReplySubmission,
                        getString(R.string.batal));
            }
        }
        super.onBackPressed();
    }
}
