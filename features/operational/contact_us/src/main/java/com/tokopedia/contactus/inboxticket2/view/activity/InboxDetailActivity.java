package com.tokopedia.contactus.inboxticket2.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.tokopedia.contactus.inboxticket2.data.model.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.ImageViewerFragment;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.design.component.ToasterNormal;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R2.id.inbox_search_view)
    View searchView;
    @BindView(R2.id.iv_previous_up)
    View ivPrevious;
    @BindView(R2.id.iv_next_down)
    View ivNext;
    @BindView(R2.id.tv_count_total)
    TextView totalRes;
    @BindView(R2.id.tv_count_current)
    TextView currentRes;


    private ImageUploadAdapter imageUploadAdapter;
    private InboxDetailAdapter detailAdapter;
    private LinearLayoutManager layoutManager;

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
    public void renderMessageList(Tickets ticketDetail) {
        List<CommentsItem> commentsItems = ticketDetail.getComments();
        Utils utils = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getUtils();

        edMessage.getText().clear();
        setSubmitButtonEnabled(false);

        //viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);

        int textSizeLabel = 11;
        if (ticketDetail.getStatus().equalsIgnoreCase(utils.SOLVED)
                || ticketDetail.getStatus().equalsIgnoreCase(utils.OPEN)) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.on_going),
                    getResources().getColor(R.color.yellow_110),
                    getResources().getColor(R.color.black_38), textSizeLabel));
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
            if (ticketDetail.isShowRating()) {
                toggleTextToolbar(View.GONE);
                rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
            }

        } else if (ticketDetail.getStatus().equalsIgnoreCase(utils.CLOSED)
                && !ticketDetail.isShowRating()) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.closed),
                    getResources().getColor(R.color.grey_200),
                    getResources().getColor(R.color.black_38), textSizeLabel));
            showIssueClosed();

        } else if (/*Sandeep*/true) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.need_rating),
                    getResources().getColor(R.color.red_30),
                    getResources().getColor(R.color.red_150), textSizeLabel));
            toggleTextToolbar(View.GONE);
            rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
        }

        if (!TextUtils.isEmpty(ticketDetail.getNumber())) {
            tvIdNum.setText(String.format(getString(R.string.invoice_id), ticketDetail.getNumber()));
            tvIdNum.setVisibility(View.VISIBLE);
        } else
            tvIdNum.setVisibility(View.GONE);
        /*if(!TextUtils.isEmpty(ticketDetail.getInvoice())){
            viewTransaction.setText(ticketDetail.getInvoice());
            viewTransaction.setVisibility(View.VISIBLE);
        } else
            viewTransaction.setVisibility(View.GONE);
*/
        if (ticketDetail.getComments() != null && ticketDetail.getComments().size() > 0) {
            detailAdapter = new InboxDetailAdapter(this, ticketDetail.getComments(), ticketDetail.isNeedAttachment(),
                    (InboxDetailContract.InboxDetailPresenter) mPresenter);
            rvMessageList.setAdapter(detailAdapter);
            rvMessageList.setVisibility(View.VISIBLE);
        } else {
            rvMessageList.setVisibility(View.GONE);
        }
        scrollTo(detailAdapter.getItemCount() - 1);
    }

    @Override
    public void updateAddComment() {
        edMessage.getText().clear();
        setSubmitButtonEnabled(false);
        imageUploadAdapter.clearAll();
        imageUploadAdapter.notifyDataSetChanged();
        rvSelectedImages.setVisibility(View.GONE);
        rvMessageList.setPadding(0, 0, 0,
                getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
        detailAdapter.setNeedAttachment(false);
        detailAdapter.notifyItemRangeChanged(detailAdapter.getItemCount() - 2, 2);
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
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);
        editText.setListener(((InboxDetailContract.InboxDetailPresenter) mPresenter).getSearchListener());
    }

    @Override
    int getMenuRes() {
        return R.menu.contactus_menu_details;
    }

    @Override
    int getBottomSheetLayoutRes() {
        return R.layout.layout_bad_csat;
    }

    @Override
    boolean doNeedReattach() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUploadAdapter = new ImageUploadAdapter(this, this);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSelectedImages.setAdapter(imageUploadAdapter);
        edMessage.addTextChangedListener(((InboxDetailContract.InboxDetailPresenter) mPresenter).watcher());
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

    @OnClick(R2.id.iv_upload_img)
    void onClickUpload() {
        if (rvSelectedImages.getVisibility() != View.VISIBLE)
            showImagePickerDialog();
        else {
            rvSelectedImages.setVisibility(View.GONE);
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
        }
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                "");
    }

    @OnClick({R2.id.btn_inactive_1,R2.id.btn_inactive_2,R2.id.btn_inactive_3,R2.id.btn_inactive_4,R2.id.btn_inactive_5,})
    void onEmojiClick(View v) {
            if(v.getId() == R.id.btn_inactive_1) {
                startActivityForResult(ActivityProvideRating.getInstance(this, 1,getIntent().getStringExtra(PARAM_TICKET_ID),rateCommentID),REQUEST_SUBMIT_FEEDBACK);
            }else if (v.getId() == R.id.btn_inactive_2) {
                startActivityForResult(ActivityProvideRating.getInstance(this, 2,getIntent().getStringExtra(PARAM_TICKET_ID),rateCommentID),REQUEST_SUBMIT_FEEDBACK);
            }else if (v.getId() == R.id.btn_inactive_3) {
                startActivityForResult(ActivityProvideRating.getInstance(this, 3,getIntent().getStringExtra(PARAM_TICKET_ID),rateCommentID),REQUEST_SUBMIT_FEEDBACK);
            }else if (v.getId() == R.id.btn_inactive_4) {
                startActivityForResult(ActivityProvideRating.getInstance(this, 4,getIntent().getStringExtra(PARAM_TICKET_ID),rateCommentID),REQUEST_SUBMIT_FEEDBACK);
            }else if (v.getId() == R.id.btn_inactive_5) {
                startActivityForResult(ActivityProvideRating.getInstance(this, 5,getIntent().getStringExtra(PARAM_TICKET_ID),rateCommentID),REQUEST_SUBMIT_FEEDBACK);
            }
    }




    @OnClick(R2.id.iv_send_button)
    void sendMessage() {
        ((InboxDetailContract.InboxDetailPresenter) mPresenter).sendMessage();
        edMessage.setHint(R.string.type_here);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickSubmitReply,
                "");
    }

    @OnClick({
            R2.id.txt_hyper,
            R2.id.tv_view_transaction})
    void onClickListener(View v) {
        int id = v.getId();
        if (id == R.id.txt_hyper) {
            setResult(RESULT_FINISH);
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickHubungi,
                    InboxTicketTracking.Label.TicketClosed);
            finish();
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
            layoutManager.scrollToPositionWithOffset(index, 0);
        }

    }

    @Override
    public void addImage(ImageUpload image) {
        if (rvSelectedImages.getVisibility() != View.VISIBLE) {
            rvSelectedImages.setVisibility(View.VISIBLE);
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_expanded));
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
        if (visibility == View.VISIBLE) {
            viewHelpRate.setVisibility(View.GONE);
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
        } else {
            viewHelpRate.setVisibility(View.VISIBLE);
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.help_rate_height));
        }

        textToolbar.setVisibility(visibility);
    }

    @Override
    public void askCustomReason() {
        ivUploadImg.setVisibility(View.GONE);
        rvSelectedImages.setVisibility(View.GONE);
        edMessage.getText().clear();
        setSubmitButtonEnabled(false);
        edMessage.setHint(R.string.type_here);
       // viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0,
                getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
        isCustomReason = true;
    }

    @Override
    public void showIssueClosed() {
        //viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.GONE);
        viewLinkBottom.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0,
                getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
    }

    @Override
    public void enterSearchMode(String search, int total) {
        textToolbar.setVisibility(View.GONE);
      //  viewHelpRate.setVisibility(View.GONE);
        viewLinkBottom.setVisibility(View.GONE);
        detailAdapter.enterSearchMode(search);
        String placeHolder = "/%s";
        if (total <= 0) {
            if (total == 0) {
                currentRes.setText("0");
                totalRes.setText("/0");
            } else {
                currentRes.setText("");
                totalRes.setText("");
            }
            ivPrevious.setClickable(false);
            ivNext.setClickable(false);
        } else {
            totalRes.setText(String.format(placeHolder, String.valueOf(total)));
            ivPrevious.setClickable(true);
            ivNext.setClickable(true);
            onClickNextPrev(ivPrevious);
        }
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
    public void setCurrentRes(int current) {
        currentRes.setText(String.valueOf(current));
    }

    @Override
    public void updateClosedStatus(String subject) {
        int textSizeLabel = 11;
        Utils utils = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getUtils();
        tvTicketTitle.setText(utils.getStatusTitle(subject + ".   " + getString(R.string.closed),
                getResources().getColor(R.color.grey_200),
                getResources().getColor(R.color.black_38), textSizeLabel));
    }

    @Override
    public boolean isSearchMode() {
        return searchView.getVisibility() == View.VISIBLE;
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
                            scrollToResult(position);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (imageUploadAdapter.getItemCount() > 1 || (textToolbar.getVisibility() == View.VISIBLE &&
                edMessage.isFocused() && edMessage.getText().length() > 0) && getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.inbox_title_dialog_wrong_scan));
            builder.setMessage(R.string.abandon_message_warning);
            builder.setNegativeButton(getString(R.string.inbox_cancel),
                    (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton(getString(R.string.inbox_exit),
                    (dialogInterface, i) -> {
                        ContactUsTracking.sendGTMInboxTicket("",
                                InboxTicketTracking.Category.EventInboxTicket,
                                InboxTicketTracking.Action.EventAbandonReplySubmission,
                                getString(R.string.inbox_cancel));
                        super.onBackPressed();
                    }).create().show();
        } else {
            super.onBackPressed();
        }

    }
}
