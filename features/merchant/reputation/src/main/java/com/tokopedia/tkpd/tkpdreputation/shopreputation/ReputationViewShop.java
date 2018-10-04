package com.tokopedia.tkpd.tkpdreputation.shopreputation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.ToolTipUtils;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.ActReputationRetrofitInteractor;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.ActReputationRetrofitInteractorImpl;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo.ActResult;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ImageUpload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hangnadi on 8/12/15.
 */
public class ReputationViewShop extends TActivity {

    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_DEFAULT = 0;
    public static final int SMILEY_NETRAL = 1;
    public static final int SMILEY_SMILE = 2;

    public static final String EXTRA_PRODUCT_ID = "product_id";
    private static final int REQUEST_LOGIN = 101;

    public static class Model implements Serializable {
        public String reviewId;
        public Boolean Editable;
        public String username;
        public String userId;
        public String avatarUrl;
        public String date;
        public String comment;
        public String userLabel;
        public int starQuality;
        public int starAccuracy;
        public int smiley;
        public String counterSmiley;
        public int counterLike;
        public int counterDislike;
        public int counterResponse;
        public String shopName;
        public String shopId;
        public String shopAvatarUrl;
        public String responseMessage;
        public String responseDate;
        public String userNameResponder;
        public String avatarUrlResponder;
        public String labelIdResponder;
        public String userLabelResponder;
        public String userIdResponder;
        public String productId;
        public String productName;
        public String shopReputation;
        public int typeMedal;
        public int levelMedal;
        public boolean isGetLikeDislike;
        public int statusLikeDislike;
        public int positive;
        public int negative;
        public int netral;
        public int noReputationUserScore;
        public String productAvatar;
        public String reputationId;
        public List<ImageUpload> imageUploads;
    }
    
    private Model model;
    private ViewHolder holder;
    private ViewHolderComment holderComment;
    private LabelUtils labelHeader;
    private LabelUtils labelResponder;
    private String shopID;
    private String productID;
    private ProgressDialog progressDialog;
    private ImageUploadAdapter imageAdapter;
    private ActReputationRetrofitInteractor networkInteractor;

    private class ViewHolder {
        ImageView avatar;
        ImageView overFlow;
        ImageView starQuality;
        ImageView starAccuracy;
        TextView username;
        TextView counterComment;
        TextView counterLike;
        TextView counterDislike;
        LinearLayout counterSmiley;
        TextView comment;
        TextView date;
        View viewLikeDislike;
        ProgressBar loading;
        ImageView iconDislike;
        ImageView iconLike;
        TextView textPercentage;
        ImageView iconPercentage;
        View viewReputation;
        TextView productName;
        ImageView productAvatar;
        RecyclerView imageHolder;
    }

    private class ViewHolderComment {
        View commentView;
        ImageView commentAvatar;
        LinearLayout commentBadges;
        ImageView buttonOverflow;
        TextView commentUsername;
        TextView commentMessage;
        TextView date;

        View postCommentView;
        ImageButton postCommentButton;
        EditText postCommentBox;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_REPUTATION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_reputation_view_shop);
        networkInteractor = new ActReputationRetrofitInteractorImpl();
        model = getIntentModel();
        model.imageUploads = new ArrayList<>();
        model.imageUploads = getIntent().getParcelableArrayListExtra("data_images");
        holder = new ViewHolder();
        holderComment = new ViewHolderComment();
        initView();
        imageAdapter = ImageUploadAdapter.createAdapter(this);
        imageAdapter.setListener(onImageClickedListener());
        holder.imageHolder.setAdapter(imageAdapter);
        setModelToView();
        setListener();
    }

    private ImageUploadAdapter.ProductImageListener onImageClickedListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> listImage = new ArrayList<>();
                        ArrayList<String> listDesc = new ArrayList<>();
                        for (ImageUpload imageUpload : model.imageUploads) {
                            listImage.add(imageUpload.getPicSrcLarge());
                            listDesc.add(imageUpload.getDescription());
                        }

                        Intent intent = new Intent(ReputationViewShop.this, PreviewProductImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("fileloc", listImage);
                        bundle.putStringArrayList("image_desc", listDesc);
                        bundle.putInt("img_pos", position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                };
            }
        };
    }

    private void initView() {
        holder.productAvatar = (ImageView) findViewById(R.id.prod_img);
        holder.productName = (TextView) findViewById(R.id.prod_name);

        holder.avatar = (ImageView) findViewById(R.id.user_avatar);
        holder.username = (TextView) findViewById(R.id.username);
        holder.date = (TextView) findViewById(R.id.date);
        holder.counterSmiley = (LinearLayout) findViewById(R.id.counter_smiley);
        holder.starQuality = (ImageView) findViewById(R.id.star_quality);
        holder.starAccuracy = (ImageView) findViewById(R.id.star_accuracy);
        holder.comment = (TextView) findViewById(R.id.comment);
        holder.counterComment = (TextView) findViewById(R.id.counter_comment);
        holder.counterLike = (TextView) findViewById(R.id.counter_like);
        holder.counterDislike = (TextView) findViewById(R.id.counter_dislike);
        holder.overFlow = (ImageView) findViewById(R.id.btn_overflow);
        labelHeader = LabelUtils.getInstance(this, holder.username);
        holder.viewLikeDislike = findViewById(R.id.view_like_dislike);
        holder.iconDislike = (ImageView) findViewById(R.id.icon_dislike);
        holder.iconLike = (ImageView) findViewById(R.id.icon_like);
        holder.textPercentage = (TextView) findViewById(R.id.rep_rating);
        holder.iconPercentage = (ImageView) findViewById(R.id.rep_icon);
        holder.viewReputation = findViewById(R.id.counter_smiley);
        holder.imageHolder = (RecyclerView) findViewById(R.id.image_holder);
        holder.imageHolder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Comment Area;
        holderComment.commentView = findViewById(R.id.comment_area);
        holderComment.commentAvatar = (ImageView) findViewById(R.id.user_ava_comment);
        holderComment.commentBadges = (LinearLayout) findViewById(R.id.badges);
        holderComment.commentMessage = (TextView) findViewById(R.id.message_comment);
        holderComment.date = (TextView) findViewById(R.id.create_time_comment);
        holderComment.buttonOverflow = (ImageView) findViewById(R.id.but_overflow_comment);
        holderComment.commentUsername = (TextView) findViewById(R.id.comment_username);
        labelResponder = LabelUtils.getInstance(this, holderComment.commentUsername);

        // Comment Post;
        holderComment.postCommentBox = (EditText) findViewById(R.id.reply_box);
        holderComment.postCommentButton = (ImageButton) findViewById(R.id.send_button);
        holderComment.postCommentView = findViewById(R.id.reply_view);
    }

    private void setModelToView() {
        setVisibility();
        ImageHandler.loadImageRounded2(this, holder.productAvatar, model.productAvatar);
//        ImageHandler.LoadImageRounded(holder.productAvatar, model.productAvatar);
        holder.productName.setText(model.productName);

        ImageHandler.loadImageCircle2(this, holder.avatar, model.avatarUrl);
//        ImageHandler.LoadImageCircle(holder.avatar, model.avatarUrl);
        holder.username.setText(MethodChecker.fromHtml(model.username).toString());
        holder.date.setText(model.date);
        holder.comment.setText(MethodChecker.fromHtml(model.comment).toString());
        labelHeader.giveSquareLabel(model.userLabel);
        setCounter();
        setStar();

        if (isResponded()) {
            holderComment.date.setText(model.responseDate);
            holderComment.commentUsername.setText(model.shopName);
            holderComment.commentMessage.setText(model.responseMessage);
            if (!model.shopAvatarUrl.isEmpty())
                ImageHandler.loadImageCircle2(this, holderComment.commentAvatar, model.shopAvatarUrl);
            labelResponder.giveSquareLabel(model.userLabelResponder);
            ReputationLevelUtils.setReputationMedals(this, holderComment.commentBadges, model.typeMedal, model.levelMedal, model.shopReputation);
        }

        imageAdapter.addList(model.imageUploads);
    }

    private void refreshData() {
        holderComment.date.setVisibility(View.GONE);
        holderComment.commentUsername.setVisibility(View.GONE);
        holderComment.commentMessage.setVisibility(View.GONE);
        holderComment.commentAvatar.setVisibility(View.GONE);
        holderComment.commentBadges.setVisibility(View.GONE);
        holder.overFlow.setVisibility(View.GONE);
        holderComment.buttonOverflow.setVisibility(View.GONE);
    }

    private void setListener() {
        holder.username.setOnClickListener(OnUserNameClickListener());
        holder.avatar.setOnClickListener(OnUserNameClickListener());
        holder.overFlow.setOnClickListener(onOverFlowClickListener(R.menu.report_menu));
        holderComment.buttonOverflow.setOnClickListener(onOverFlowClickListener(R.menu.delete_menu));

        holder.counterLike.setOnClickListener(OnLikeClickListener());
        holder.iconLike.setOnClickListener(OnLikeClickListener());
        holder.counterDislike.setOnClickListener(OnDislikeClickListener());
        holder.iconDislike.setOnClickListener(OnDislikeClickListener());
        holder.viewReputation.setOnClickListener(OnViewReputationClickListener());

        holderComment.commentUsername.setOnClickListener(OnShopNameClickListener());
        holderComment.commentAvatar.setOnClickListener(OnShopNameClickListener());
        holder.productName.setOnClickListener(OnProductNameClickListener());
        holder.productAvatar.setOnClickListener(OnProductNameClickListener());
        holderComment.postCommentButton.setOnClickListener(OnPostCommentButtonClickListener());
    }

    private View.OnClickListener OnPostCommentButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderComment.postCommentBox.setError(null);
                if (holderComment.postCommentBox.getText().length() > 4) {
                    PostComment();
                } else if (holderComment.postCommentBox.getText().length() != 0) {
                    holderComment.postCommentBox.setError(getString(R.string.error_min_5_character));
                } else {
                    holderComment.postCommentBox.setError(getString(R.string.error_field_required));
                }
            }
        };
    }

    private void PostComment() {
        KeyboardHandler.DropKeyboard(this,holder.comment.getRootView());
        showProgressDialog();
        networkInteractor.postComment(this, getParamReply(), new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                if (result.getIsSuccess() == 1) {
                    dismissProgressDialog();
                    Intent intent = new Intent();
                    intent.putExtra("is_success", 1);
                    intent.putExtra("last_action", "update_product");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    dismissProgressDialog();
                    showSnackbarMessage(getString(R.string.error_failed_reply_review));
                }
            }

            @Override
            public void onTimeout() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                showSnackbarMessage(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }
        });
    }

    private Map<String, String> getParamReply() {
        ActReviewPass pass = new ActReviewPass();
        pass.setReviewId(model.reviewId);
        pass.setReputationId(model.reputationId);
        pass.setShopId(model.shopId);
        pass.setResponseMessage(holderComment.postCommentBox.getText().toString());
        return pass.getReplyParam();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(ReputationViewShop.this);
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private View.OnClickListener OnProductNameClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductDetailRouter
                        .createInstanceProductDetailInfoActivity(ReputationViewShop.this, model.productId);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener OnUserNameClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseContext().getApplicationContext() instanceof ReputationRouter) {
                    startActivity(((ReputationRouter) getBaseContext().getApplicationContext())
                            .getTopProfileIntent(getBaseContext(),
                                    model.userId));
                }
            }
        };
    }

    private View.OnClickListener OnShopNameClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ((ReputationRouter) getApplication()).getShopPageIntent(ReputationViewShop.this, model.shopId);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener OnViewReputationClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(), v);
            }
        };
    }

    private View setViewToolTip() {
        return ToolTipUtils.setToolTip(this, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText("" + model.positive);
                netral.setText("" + model.netral);
                bad.setText("" + model.negative);
            }

            @Override
            public void setListener() {

            }
        });
    }

    private View.OnClickListener OnLikeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionHandler.isV4Login(ReputationViewShop.this)) {
                    OnLikeConnection();
                } else {
                    showLoginOption();
                }
            }
        };
    }

    private void OnLikeConnection() {
        showProgressDialog();
        Model UnUpdatedModel = model;
        switch (model.statusLikeDislike) {
            case 0:
                setTemporaryActivatedLike();
                break;
            case 3:
                setTemporaryActivatedLike();
                break;
            case 1:
                setTemporaryDeactivatedLike();
                break;
            case 2:
                setTemporaryDeactivatedDislike();
                setTemporaryActivatedLike();
                break;
        }
        updateLikeDislike(UnUpdatedModel);
    }

    private void setTemporaryDeactivatedLike() {
        model.statusLikeDislike = 3;
        model.counterLike = model.counterLike - 1;
        setCounter();
    }

    private void setTemporaryDeactivatedDislike() {
        model.statusLikeDislike = 3;
        model.counterDislike = model.counterDislike - 1;
        setCounter();
    }

    private void setTemporaryActivatedLike() {
        model.statusLikeDislike = 1;
        model.counterLike = model.counterLike + 1;
        setCounter();
    }

    private void setTemporaryActivatedDislike() {
        model.statusLikeDislike = 2;
        model.counterDislike = model.counterDislike + 1;
        setCounter();
    }

    private View.OnClickListener OnDislikeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionHandler.isV4Login(ReputationViewShop.this)) {
                    OnDislikeConnection();
                } else {
                    showLoginOption();
                }
            }
        };
    }

    private void showLoginOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.error_not_logged));
        builder.setPositiveButton(getString(R.string.title_activity_login), OnLoginClickListener());
        builder.setNegativeButton(getString(R.string.title_cancel), OnCancelClickListener());
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private DialogInterface.OnClickListener OnLoginClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = ((ReputationRouter) MainApplication.getAppContext())
                        .getLoginIntent(ReputationViewShop.this);
                startActivityForResult(intent,REQUEST_LOGIN);
            }
        };
    }

    private DialogInterface.OnClickListener OnCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    private void OnDislikeConnection() {
        showProgressDialog();
        Model UnUpdatedModel = model;
        switch (model.statusLikeDislike) {
            case 0:
                setTemporaryActivatedDislike();
                break;
            case 3:
                setTemporaryActivatedDislike();
                break;
            case 1:
                setTemporaryDeactivatedLike();
                setTemporaryActivatedDislike();
                break;
            case 2:
                setTemporaryDeactivatedDislike();
                break;
        }
        updateLikeDislike(UnUpdatedModel);
    }

    private void updateLikeDislike(final Model model) {
        networkInteractor.likeDislikeReview(this, getLikeDislikeParam(), new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                dismissProgressDialog();
                Intent intent = new Intent();
                intent.putExtra("is_success", 1);
                intent.putExtra("last_action", "update_product");
                setResult(Activity.RESULT_OK, intent);
            }

            @Override
            public void onTimeout() {
                dismissProgressDialog();
                revertBack(model);
                showNetworkErrorSnackbar();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                revertBack(model);
                showSnackbarMessage(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                dismissProgressDialog();
                revertBack(model);
                showNetworkErrorSnackbar();
            }
        });
    }

    private Map<String, String> getLikeDislikeParam() {
        ActReviewPass pass = new ActReviewPass();
        pass.setShopId(model.shopId);
        pass.setProductId(model.productId);
        pass.setReviewId(model.reviewId);
        pass.setLikeStatus(String.valueOf(model.statusLikeDislike));
        return pass.getLikeDislikeParam();
    }

    private void revertBack(Model model) {
        this.model = model;
        setCounter();
    }

    private View.OnClickListener onOverFlowClickListener(final int menu) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(menu, v);
            }
        };
    }

    private void showPopUp(int menu, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menu, popup.getMenu());
        popup.setOnMenuItemClickListener(onPopUpMenuClick());
        popup.show();
    }

    private PopupMenu.OnMenuItemClickListener onPopUpMenuClick() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_report) {
                    showDialogReport();
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    deleteComment();
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    private void showDialogReport() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_dialog_report, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.reason);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.action_report),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setNegativeButton(getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (userInput.length() > 0) {
                            String message = userInput.getText().toString();
                            postReport(message);
                            alertDialog.dismiss();
                        } else
                            userInput.setError(getString(R.string.error_field_required));
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void postReport(String message) {
        showProgressDialog();
        networkInteractor.postReport(this, getReportParam(message), new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                dismissProgressDialog();
                showSnackbarMessage(getString(R.string.toast_success_report));
            }

            @Override
            public void onTimeout() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                showSnackbarMessage(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }
        });
    }

    private void showSnackbarMessage(String message) {
        NetworkErrorHelper.showSnackbar(this, message);
    }

    private Map<String, String> getReportParam(String message) {
        ActReviewPass pass = new ActReviewPass();
        pass.setShopId(shopID);
        pass.setReviewId(model.reviewId);
        pass.setReportMessage(message);
        return pass.getReportParam();
    }

    private void deleteComment() {
        showProgressDialog();
        networkInteractor.deleteComment(this, getDeleteCommentParam(),
                new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                dismissProgressDialog();
                CommonUtils.UniversalToast(getBaseContext(), getString(R.string.msg_delete_comment));
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("last_action", "delete_comment_review");
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                refreshData();
                holderComment.postCommentView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTimeout() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                showSnackbarMessage(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                dismissProgressDialog();
                showNetworkErrorSnackbar();
            }
        });
    }

    private void showNetworkErrorSnackbar() {
        NetworkErrorHelper.showSnackbar(this);
    }

    private Map<String, String> getDeleteCommentParam() {
        ActReviewPass param = new ActReviewPass();
        param.setReviewId(model.reviewId);
        param.setReputationId(model.reputationId);
        param.setShopId(model.shopId);
        return param.getDeleteParam();
    }

    private void setVisibility() {
        if (!isResponded() && isProductOwner()) {
            holderComment.postCommentView.setVisibility(View.VISIBLE);
        } else {
            holderComment.postCommentView.setVisibility(View.GONE);
        }

        if (isResponded()) {
            holderComment.commentView.setVisibility(View.VISIBLE);
        } else {
            holderComment.commentView.setVisibility(View.GONE);
        }

        if (SessionHandler.isV4Login(getBaseContext()) && isProductOwner()) {
            holder.overFlow.setVisibility(View.VISIBLE);
            holderComment.buttonOverflow.setVisibility(View.VISIBLE);
        } else {
            holder.overFlow.setVisibility(View.GONE);
            holderComment.buttonOverflow.setVisibility(View.GONE);
        }

        if (model.imageUploads.size() > 0) {
            holder.imageHolder.setVisibility(View.VISIBLE);
        } else {
            holder.imageHolder.setVisibility(View.GONE);

        }
    }

    private boolean isProductOwner() {
        SessionHandler session = new SessionHandler(this);
        return model.userIdResponder.equals(session.getLoginID());
    }

    private boolean isResponded() {
        return model.counterResponse != 0;
    }

    private Model getIntentModel() {
        return ((Model) getIntent().getSerializableExtra("data_model"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        productID = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        shopID = getIntent().getStringExtra("shop_id");
        return super.onCreateOptionsMenu(menu);
    }

    private void setCounter() {
        holder.textPercentage.setText(model.counterSmiley);
        setIconPercentage();
        holder.counterComment.setText(model.counterResponse + " komentar");

        setIconLike();
        setIconDislike();

        holder.counterLike.setText("" + model.counterLike);
        holder.counterDislike.setText("" + model.counterDislike);
        holder.counterComment.setText("" + model.counterResponse);
    }

    private void setIconPercentage() {
        if (allowActiveSmiley()) {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.textPercentage.setVisibility(View.VISIBLE);
        } else {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.textPercentage.setVisibility(View.GONE);
        }
    }

    private boolean allowActiveSmiley() {
        return model.noReputationUserScore == 0;
    }

    private void setIconLike() {
        if (model.statusLikeDislike == 1) {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like_active);
        } else {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like);
        }
    }

    private void setIconDislike() {
        if (model.statusLikeDislike == 2) {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike_active);
        } else {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike);
        }
    }

    private int generateSmiley(int args) {
        int smiley = R.drawable.ic_icon_repsis_smile;
        switch (args) {
            case SMILEY_BAD:
                smiley = R.drawable.ic_icon_repsis_sad_active;
                break;
            case SMILEY_NETRAL:
                smiley = R.drawable.ic_icon_repsis_neutral_active;
                break;
            case SMILEY_SMILE:
                smiley = R.drawable.ic_icon_repsis_smile_active;
                break;
        }
        return smiley;
    }

    private void setStar() {
        holder.starAccuracy.setImageResource(generateStar(model.starAccuracy));
        holder.starQuality.setImageResource(generateStar(model.starQuality));
    }

    private int generateStar(int args) {
        int star = R.drawable.ic_star_none;
        switch (args) {
            case 1:
                star = R.drawable.ic_star_one;
                break;
            case 2:
                star = R.drawable.ic_star_two;
                break;
            case 3:
                star = R.drawable.ic_star_three;
                break;
            case 4:
                star = R.drawable.ic_star_four;
                break;
            case 5:
                star = R.drawable.ic_star_five;
                break;
        }
        return star;
    }

    private int getBadges(int args) {
        int drawable = 0;
        switch (args) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return drawable;
    }

    @Override
    protected void onDestroy() {
        networkInteractor.unSubscribeObservable();
        super.onDestroy();
    }
}
