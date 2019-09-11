package com.tokopedia.contactus.inboxticket2.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.inboxticket2.data.model.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.CloseComplainBottomSheet;
import com.tokopedia.contactus.inboxticket2.view.fragment.HelpFullBottomSheet;
import com.tokopedia.contactus.inboxticket2.view.fragment.ImageViewerFragment;
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InboxDetailActivity extends InboxBaseActivity
        implements InboxDetailContract.InboxDetailView, ImageUploadAdapter.OnSelectImageClick, View.OnClickListener, ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet, HelpFullBottomSheet.CloseSHelpFullBottomSheet, CloseComplainBottomSheet.CloseComplainBottomSheetListner {

    public static final String KEY_LIKED = "101";
    public static final String KEY_DIS_LIKED = "102";
    public static final int INT_KEY_LIKED = 101;
    public static final int DELAY_FOUR_MILLIS = 4000;
    public static final String SNACKBAR_OK = "Ok";
    public static final String ROLE_TYPE_AGENT = "agent";
    private TextView tvTicketTitle;
    private TextView tvIdNum;
    private RecyclerView rvMessageList;
    private RecyclerView rvSelectedImages;
    private View dividerRv;
    private ImageView ivUploadImg;
    private ImageView ivSendButton;
    private TextView viewTransaction;
    private EditText edMessage;
    private View sendProgress;
    private View viewHelpRate;
    private View textToolbar;
    private View viewLinkBottom;
    private CustomEditText editText;
    private View searchView;
    private View ivPrevious;
    private View ivNext;
    private TextView totalRes;
    private TextView currentRes;
    private TextView tvPriorityLabel;
    private ImageView btnInactive1, btnInactive2, btnInactive3, btnInactive4, btnInactive5;
    private TextView txtHyper;
    private View noTicketFound;
    private TextView tvNoTicket;
    private TextView tvOkButton;
    private ConstraintLayout rootView;
    private View viewReplyButton;
    private TextView tvReplyButton;
    private ImageUploadAdapter imageUploadAdapter;
    private InboxDetailAdapter detailAdapter;
    private LinearLayoutManager layoutManager;
    private String rateCommentID;
    private boolean isCustomReason;
    public static final String PARAM_TICKET_ID = "ticket_id";
    public static final String PARAM_TICKET_T_ID = "id";
    public static final String IS_OFFICIAL_STORE = "is_official_store";
    private CloseableBottomSheetDialog helpFullBottomSheet, closeComplainBottomSheet,servicePrioritiesBottomSheet;
    List<CommentsItem> commentsItems = new ArrayList<>();

    @DeepLink(ApplinkConst.TICKET_DETAIL)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent = new Intent(context, InboxListActivity.class);
        String ticketId = bundle.getString(PARAM_TICKET_T_ID);
        if (ticketId == null) {
            ticketId = bundle.getString(PARAM_TICKET_ID, "");
        }
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(getIntent(context, ticketId));
        return taskStackBuilder;
    }

    public static Intent getIntent(Context context, String ticketId) {
        Intent intent = new Intent(context, InboxDetailActivity.class);
        intent.putExtra(PARAM_TICKET_ID, ticketId);
        intent.putExtra(IS_OFFICIAL_STORE, false);
        return intent;
    }

    @Override
    public void renderMessageList(Tickets ticketDetail) {
        commentsItems = ticketDetail.getComments();
        Utils utils = ((InboxDetailContract.InboxDetailPresenter) mPresenter).getUtils();

        edMessage.getText().clear();
        setSubmitButtonEnabled(false);

        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);

        int textSizeLabel = 11;
        if (ticketDetail.getStatus().equalsIgnoreCase(utils.SOLVED)
                || ticketDetail.getStatus().equalsIgnoreCase(utils.OPEN)) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.on_going),
                    getResources().getColor(R.color.y_200),
                    getResources().getColor(R.color.orange_500), textSizeLabel));
            rvMessageList.setPadding(0, 0, 0,
                    getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
            if(commentsItems.get(commentsItems.size()-1).getCreatedBy().getRole().equalsIgnoreCase(ROLE_TYPE_AGENT)&&commentsItems.get(commentsItems.size()-1).getRating().equalsIgnoreCase("")){
                viewReplyButton.setVisibility(View.VISIBLE);
                rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
            }
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

        } else if (ticketDetail.isShowRating()) {
            tvTicketTitle.setText(utils.getStatusTitle(ticketDetail.getSubject() + ".   " + getString(R.string.need_rating),
                    getResources().getColor(R.color.r_100),
                    getResources().getColor(R.color.r_400), textSizeLabel));
            toggleTextToolbar(View.GONE);
            rateCommentID = commentsItems.get(commentsItems.size() - 1).getId();
        }

        if (!TextUtils.isEmpty(ticketDetail.getNumber())) {
            tvIdNum.setText(String.format(getString(R.string.invoice_id), ticketDetail.getNumber()));
            tvIdNum.setVisibility(View.VISIBLE);
        } else
            tvIdNum.setVisibility(View.GONE);

        if (ticketDetail.getComments() != null && ticketDetail.getComments().size() > 0) {
            detailAdapter = new InboxDetailAdapter(this, commentsItems, ticketDetail.isNeedAttachment(),
                    (InboxDetailContract.InboxDetailPresenter) mPresenter);
            rvMessageList.setAdapter(detailAdapter);
            rvMessageList.setVisibility(View.VISIBLE);
        } else {
            rvMessageList.setVisibility(View.GONE);
        }
        scrollTo(detailAdapter.getItemCount() - 1);

        if (getIntent() != null && getIntent().getBooleanExtra(IS_OFFICIAL_STORE, false)) {
            tvPriorityLabel.setVisibility(View.VISIBLE);
            tvPriorityLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePrioritiesBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
                    servicePrioritiesBottomSheet.setCustomContentView( new ServicePrioritiesBottomSheet(InboxDetailActivity.this,InboxDetailActivity.this),"", false);
                    servicePrioritiesBottomSheet.show();
                }
            });
        }
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
        findingViewsId();
        rvMessageList.setLayoutManager(layoutManager);
        editText.setListener(((InboxDetailContract.InboxDetailPresenter) mPresenter).getSearchListener());
        tvReplyButton = findViewById(R.id.tv_reply_button);
        viewReplyButton = findViewById(R.id.view_rply_botton_before_csat_rating);
        tvReplyButton.setOnClickListener(this);
        settingClickListner();
    }

    private void findingViewsId() {
        rootView = findViewById(R.id.root_view);
        tvTicketTitle = findViewById(R.id.tv_ticket_title);
        tvIdNum = findViewById(R.id.tv_id_num);
        rvMessageList = findViewById(R.id.rv_message_list);
        rvSelectedImages = findViewById(R.id.rv_selected_images);
        ivUploadImg = findViewById(R.id.iv_upload_img);
        ivSendButton = findViewById(R.id.iv_send_button);
        viewTransaction = findViewById(R.id.tv_view_transaction);
        edMessage = findViewById(R.id.ed_message);
        sendProgress = findViewById(R.id.send_progress);
        viewHelpRate = findViewById(R.id.view_help_rate);
        textToolbar = findViewById(R.id.text_toolbar);
        viewLinkBottom = findViewById(R.id.view_link_bottom);
        editText = findViewById(R.id.custom_search);
        searchView = findViewById(R.id.inbox_search_view);
        ivPrevious = findViewById(R.id.iv_previous_up);
        ivNext = findViewById(R.id.iv_next_down);
        totalRes = findViewById(R.id.tv_count_total);
        currentRes = findViewById(R.id.tv_count_current);
        tvPriorityLabel = findViewById(R.id.tv_priority_label);
        btnInactive1 = findViewById(R.id.btn_inactive_1);
        btnInactive2 = findViewById(R.id.btn_inactive_2);
        btnInactive3 = findViewById(R.id.btn_inactive_3);
        btnInactive4 = findViewById(R.id.btn_inactive_4);
        btnInactive5 = findViewById(R.id.btn_inactive_5);
        txtHyper = findViewById(R.id.txt_hyper);
    }

    private void settingClickListner() {
        btnInactive1.setOnClickListener(this);
        btnInactive2.setOnClickListener(this);
        btnInactive3.setOnClickListener(this);
        btnInactive4.setOnClickListener(this);
        btnInactive5.setOnClickListener(this);
        ivUploadImg.setOnClickListener(this);
        ivSendButton.setOnClickListener(this);
        viewTransaction.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        txtHyper.setOnClickListener(this);
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
        noTicketFound = findViewById(R.id.no_ticket_found);
        tvNoTicket = findViewById(R.id.tv_no_ticket);
        tvOkButton = findViewById(R.id.tv_ok_button);
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

    void onEmojiClick(View v) {
        int id = v.getId();
        InboxDetailContract.InboxDetailPresenter presenter = (InboxDetailContract.InboxDetailPresenter) mPresenter;
        if (id == R.id.btn_inactive_1) {
            presenter.onClickEmoji(1);
        } else if (id == R.id.btn_inactive_2) {
            presenter.onClickEmoji(2);
        } else if (id == R.id.btn_inactive_3) {
            presenter.onClickEmoji(3);
        } else if (id == R.id.btn_inactive_4) {
            presenter.onClickEmoji(4);
        } else if (id == R.id.btn_inactive_5) {
            presenter.onClickEmoji(5);
        }
    }


    @Override
    public String getCommentID() {
        return rateCommentID;
    }

    @Override
    public void showNoTicketView(List<String> messageError) {
        noTicketFound.setVisibility(View.VISIBLE);
        tvNoTicket.setText(messageError.get(0));
        tvOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showErrorMessage(String error) {
        ToasterError.make(getRootView(), error).show();
    }

    void sendMessage() {
        ((InboxDetailContract.InboxDetailPresenter) mPresenter).sendMessage();
        edMessage.setHint(R.string.type_here);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickSubmitReply,
                "");
    }

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
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0,
                getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
        isCustomReason = true;
    }

    @Override
    public void showIssueClosed() {
        viewHelpRate.setVisibility(View.GONE);
        textToolbar.setVisibility(View.GONE);
        viewLinkBottom.setVisibility(View.VISIBLE);
        rvMessageList.setPadding(0, 0, 0,
                getResources().getDimensionPixelSize(R.dimen.text_toolbar_height_collapsed));
    }

    @Override
    public void enterSearchMode(String search, int total) {
        textToolbar.setVisibility(View.GONE);
        viewHelpRate.setVisibility(View.GONE);
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
    public String getTicketID() {
        return getIntent().getStringExtra(PARAM_TICKET_ID);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_upload_img) {
            onClickUpload();
        } else if (id == R.id.btn_inactive_1 || id == R.id.btn_inactive_2 || id == R.id.btn_inactive_3 || id == R.id.btn_inactive_4 || id == R.id.btn_inactive_5) {
            onEmojiClick(view);
        } else if (id == R.id.iv_send_button) {
            sendMessage();
        } else if (id == R.id.txt_hyper || id == R.id.tv_view_transaction) {
            onClickListener(view);
        } else if (id == R.id.iv_next_down || id == R.id.iv_previous_up) {
            onClickNextPrev(view);
        } else {
            String rating = "";
            if (view.getId() == R.id.tv_reply_button) {
                rating = commentsItems.get(commentsItems.size()-1).getRating();
                if (rating != null && (rating.equals(KEY_LIKED) || rating.equals(KEY_DIS_LIKED))) {
                    viewReplyButton.setVisibility(View.GONE);
                    textToolbar.setVisibility(View.VISIBLE);
                } else {
                    helpFullBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
                    helpFullBottomSheet.setCustomContentView(new HelpFullBottomSheet(InboxDetailActivity.this, this), "", true);
                    helpFullBottomSheet.show();
                    viewReplyButton.setVisibility(View.GONE);
                    textToolbar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

        @Override
        public void onClick (boolean agreed){
            CommentsItem item = null;
            int commentPosition = 0;
            for (int i = detailAdapter.getItemCount() - 1; i >= 0; i--) {
                CommentsItem item1 = commentsItems.get(i);
                if (item1.getCreatedBy().getRole().equals("agent")) {
                    item = item1;
                    commentPosition = i;
                    break;
                }
            }
            if (agreed) {
                closeComplainBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
                closeComplainBottomSheet.setCustomContentView(new CloseComplainBottomSheet(InboxDetailActivity.this, this), "", true);
                closeComplainBottomSheet.show();
                viewReplyButton.setVisibility(View.GONE);
                ((InboxDetailContract.InboxDetailPresenter) mPresenter).onClick(true, commentPosition, item.getId());
                helpFullBottomSheet.dismiss();
            } else {
                ((InboxDetailContract.InboxDetailPresenter) mPresenter).onClick(false, commentPosition, item.getId());
                textToolbar.setVisibility(View.VISIBLE);
                helpFullBottomSheet.dismiss();
            }
        }

        @Override
        public void onSuccessSubmitOfRating ( int rating, int commentPosition){
            CommentsItem item = commentsItems.get(commentPosition);
            String rate = rating == INT_KEY_LIKED ? KEY_LIKED : KEY_DIS_LIKED;
            item.setRating(rate);
            detailAdapter.notifyItemChanged(commentPosition, item);
        }

        @Override
        public void onClickComplain (boolean agreed){
            if (agreed) {
                ((InboxDetailContract.InboxDetailPresenter) mPresenter).closeTicket();
                closeComplainBottomSheet.dismiss();

            } else {
                viewReplyButton.setVisibility(View.GONE);
                viewHelpRate.setVisibility(View.GONE);
                textToolbar.setVisibility(View.VISIBLE);
                closeComplainBottomSheet.dismiss();
            }

        }

        @Override
        public void OnSucessfullTicketClose () {
            Observable.timer(DELAY_FOUR_MILLIS,TimeUnit.MILLISECONDS)
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
                            mPresenter.refreshLayout();
                        }
                    });

            ((InboxDetailContract.InboxDetailPresenter) mPresenter).onClickEmoji(0);
        }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
        Toaster.Companion.showNormalWithAction(rootView, message, Snackbar.LENGTH_LONG, SNACKBAR_OK, v1 -> {
        });
    }

    @Override
    public void onClickClose() {
        servicePrioritiesBottomSheet.dismiss();
    }
}

