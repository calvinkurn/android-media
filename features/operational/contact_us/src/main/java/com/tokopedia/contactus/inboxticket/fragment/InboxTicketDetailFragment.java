package com.tokopedia.contactus.inboxticket.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.contactus.inboxticket.InboxTicketConstant;
import com.tokopedia.contactus.inboxticket.adapter.ImageUploadAdapter;
import com.tokopedia.contactus.inboxticket.adapter.InboxTicketDetailAdapter;
import com.tokopedia.contactus.inboxticket.listener.InboxTicketDetailFragmentView;
import com.tokopedia.contactus.inboxticket.model.InboxTicketParam;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketDetailFragmentPresenter;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketDetailFragmentPresenterImpl;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import butterknife.BindView;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by Nisie on 4/22/16.
 */
public class InboxTicketDetailFragment extends BasePresenterFragment<InboxTicketDetailFragmentPresenter>
        implements InboxTicketDetailFragmentView, InboxTicketConstant {


    public static final int REQUEST_CODE_INBOX_TICKET = 8412;
    private View noView;
    private View yesView;
    private String commentId;
    private View noTView;
    private View yesTView;

    public interface DoActionInboxTicketListener {
        void sendRating(Bundle param);
    }

    @BindView(R2.id.ticket_list)
    RecyclerView listTicket;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.list_image_upload)
    RecyclerView listImage;

    @BindView(R2.id.attach_but)
    ImageView attachButton;

    @BindView(R2.id.send_but)
    ImageView sendButton;

    @BindView(R2.id.new_comment)
    EditText comment;

    @BindView(R2.id.add_comment_area)
    View commentView;

    @BindView(R2.id.menu_help)
    View helpfulView;

    @BindView(R2.id.loading_layout)
    View loading;

    @BindView(R2.id.ticket_notice)
    TextView ticketNotice;

    InboxTicketDetailAdapter adapter;
    ImageUploadAdapter imageAdapter;
    RefreshHandler refreshHandler;
    TkpdProgressDialog progressDialog;

    public static Fragment createInstance(Bundle data) {
        InboxTicketDetailFragment fragment = new InboxTicketDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setCache();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTicketDetailFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_ticket_detail;
    }

    @Override
    protected void initView(View view) {
        noView = view.findViewById(R.id.icon_dislike);
        yesView = view.findViewById(R.id.icon_like);
        noTView = view.findViewById(R.id.counter_dislike);
        yesTView = view.findViewById(R.id.counter_like);

        adapter = InboxTicketDetailAdapter.createAdapter(getActivity(), presenter);
        listTicket.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listTicket.setAdapter(adapter);
        imageAdapter = ImageUploadAdapter.createAdapter(getActivity().getApplicationContext());
        imageAdapter.setListener(onProductImageActionListener());
        listImage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listImage.setAdapter(imageAdapter);
        refreshHandler = new RefreshHandler(getActivity(), swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        });
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public void onImageDeleted(int size) {
                if (size == 0) {
                    listImage.setVisibility(View.GONE);
                }
                attachButton.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    protected void setViewListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), comment);
                presenter.onSendButtonClicked();
            }
        });
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        yesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.commentRating(InboxTicketParam.YES_HELPFUL);
            }
        });

        noView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.commentRating(InboxTicketParam.NO_HELPFUL);
            }
        });

        yesTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.commentRating(InboxTicketParam.YES_HELPFUL);
            }
        });

        noTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.commentRating(InboxTicketParam.NO_HELPFUL);
            }
        });
    }

    private void commentRating(String isHelpful){
        presenter.commentRating(isHelpful);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        setActionsEnabled(true);
        refreshHandler.finishRefresh();
        progressDialog.dismiss();
        loading.setVisibility(View.GONE);
    }

    @Override
    public InboxTicketDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshHandler.setRefreshing(isRefreshing);
    }

    @Override
    public void setReplyVisibility(int visibility) {
        commentView.setVisibility(visibility);
    }

    @Override
    public void onSuccessGetInboxTicketDetail(InboxTicketDetail result) {
        listTicket.setVisibility(View.VISIBLE);
        adapter.setData(result);
        if (result.getTicket().getTicketStatus() == TICKET_OPEN) {
            if(result.isShowRating()){
                int replySize = result.getTicketReply().getTicketReplyData().size();
                commentId = result.getTicketReply().getTicketReplyData().get(replySize-1).getTicketDetailId();
                commentView.setVisibility(View.GONE);
                helpfulView.setVisibility(View.VISIBLE);
            }else {
                commentView.setVisibility(View.VISIBLE);
                helpfulView.setVisibility(View.GONE);
            }
        } else {
            commentView.setVisibility(View.GONE);
            helpfulView.setVisibility(View.GONE);
        }

        setStatus(result.getTicket().getTicketStatus());

    }

    private void setStatus(int ticketStatus) {
        switch (ticketStatus) {
            case TICKET_CLOSED:
                ticketNotice.setVisibility(View.VISIBLE);
                ticketNotice.setText(context.getString(R.string.msg_ticket_close_2));
                break;
            default:
                ticketNotice.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        listTicket.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        attachButton.setEnabled(isEnabled);
        comment.setEnabled(isEnabled);
    }

    @Override
    public void showError(String error) {
        if (error.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), error);
        }
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                null
                , null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_INBOX_TICKET);
    }

    @Override
    public void showRetry(String message, View.OnClickListener onRetry) {
        if (message.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.title_retry), onRetry)
                    .show();
        }
    }

    @Override
    public void onSuccessSetRating(Bundle resultData) {
        presenter.onRefresh();
    }

    @Override
    public void onFailedSetRating(Bundle resultData) {
        finishLoading();
        String error = resultData.getString(EXTRA_ERROR, "");
        showError(error);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();

        progressDialog.showDialog();

    }

    @Override
    public String getComment() {
        return comment.getText().toString();
    }

    @Override
    public String getCommentId() {
        return commentId;
    }

    @Override
    public void showCommentView() {
        commentView.setVisibility(View.VISIBLE);
        helpfulView.setVisibility(View.GONE);
    }

    @Override
    public void showReplyDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.prompt_send_comment)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.sendReply();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (message.equals("")) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
        }
    }

    @Override
    public ImageUploadAdapter getImageAdapter() {
        return imageAdapter;
    }

    @Override
    public void addImage(ImageUpload image) {
        imageAdapter.addImage(image);
        listImage.setVisibility(View.VISIBLE);
        if (imageAdapter.getList().size() == 5) {
            attachButton.setVisibility(View.INVISIBLE);
        } else {
            attachButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessReply(TicketReplyDatum ticketReply) {
        comment.setText("");
        KeyboardHandler.DropKeyboard(getActivity(), comment);
        adapter.addReply(ticketReply);
        adapter.updateView();
        imageAdapter.getList().clear();
        imageAdapter.notifyDataSetChanged();
        listImage.setVisibility(View.GONE);
        attachButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setIsRefreshing(true);
        refreshHandler.setRefreshing(true);
    }

    @Override
    public void showLoadingAll() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void setResultSuccess() {
        getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
    }

    @Override
    public void removeError() {
        NetworkErrorHelper.hideEmptyState(getView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
