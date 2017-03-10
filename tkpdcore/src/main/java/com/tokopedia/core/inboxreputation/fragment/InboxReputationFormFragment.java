package com.tokopedia.core.inboxreputation.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.activity.InboxReputationDetailActivity;
import com.tokopedia.core.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.inboxreputation.listener.InboxReputationFormView;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenterImpl;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFormFragmentPresenter;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFormFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Nisie on 1/28/16.
 */
@RuntimePermissions
public class InboxReputationFormFragment extends BasePresenterFragment<InboxReputationFormFragmentPresenter>
        implements InboxReputationFormView, InboxReputationConstant {

    private static final java.lang.String PARAM_TOKEN = "token";
    public static final String PARAM_INBOX_REPUTATION = "inbox_reputation";
    public static final String PARAM_INBOX_REPUTATION_DETAIL = "inbox_reputation_detail";
    private static final String DEFAULT_MSG_ERROR = "Mohon maaf, mohon coba kembali";
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    public interface DoActionReputationListener {
        void postReview(Bundle param);

        void editReview(Bundle param);
    }

    @BindView(R2.id.product_name)
    TextView productName;

    @BindView(R2.id.product_avatar)
    ImageView productAvatar;

    @BindView(R2.id.edittext_review)
    EditText editTextReview;

    @BindView(R2.id.image_holder)
    RecyclerView imageHolder;

    @BindView(R2.id.ratingBarQuality)
    RatingBar starQuality;

    @BindView(R2.id.ratingBarAccuracy)
    RatingBar starAccuracy;

    @BindView(R2.id.prod_quality_desc)
    TextView starQualityDescription;

    @BindView(R2.id.prod_accuracy_desc)
    TextView starAccuracyDescription;

    @BindView(R2.id.submit)
    TextView submitButton;

    @BindView(R2.id.accuracy_error_message)
    TextView accuracyError;

    @BindView(R2.id.quality_error_message)
    TextView qualityError;

    @BindView(R2.id.checkbox)
    CheckBox checkBox;

    private InboxReputationFormFragmentPresenter presenter;
    private InboxReputationDetailItem inboxReputationDetail;
    private InboxReputationItem inboxReputation;
    private TkpdProgressDialog progressDialog;
    private ImageUploadAdapter adapter;

    public static InboxReputationFormFragment createInstance(Bundle extras) {
        InboxReputationFormFragment fragment = new InboxReputationFormFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_give_review;
    }

    @Override
    protected void initView(View view) {
        productName.setText(MethodChecker.fromHtml(inboxReputationDetail.getProductName()));
        ImageHandler.LoadImage(productAvatar, inboxReputationDetail.getProductImageUrl());
        adapter = ImageUploadAdapter.createAdapter(getActivity().getApplicationContext());
        adapter.setCanUpload(true);
        adapter.setListener(onImageUploadActionListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        imageHolder.setLayoutManager(layoutManager);
        imageHolder.setAdapter(adapter);

        try {
            if (getActivity().getIntent().getExtras().getString("nav", "")
                    .equals(InboxReputationDetailActivity.NAV_EDIT_PRODUCT)) {
                editTextReview.setText(inboxReputationDetail.getReviewMessage());
                starQuality.setRating(inboxReputationDetail.getProductQualityRating());
                starQualityDescription.setText(presenter.getRatingDescription(
                        inboxReputationDetail.getProductQualityRating()));
                starAccuracy.setRating(inboxReputationDetail.getProductAccuracyRating());
                starAccuracyDescription.setText(presenter.getRatingDescription(
                        inboxReputationDetail.getProductAccuracyRating()));
                submitButton.setText(getActivity().getString(R.string.action_edit));
                adapter.addList(inboxReputationDetail.getImages());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        productAvatar.setOnClickListener(onGoToProduct(inboxReputationDetail));
        productName.setOnClickListener(onGoToProduct(inboxReputationDetail));
        checkBox.setOnClickListener(onTickedCheckbox());
    }

    private View.OnClickListener onTickedCheckbox() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked() ) {
                    processFacebookLogin();
                }else{
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        };
    }

    private void processFacebookLogin() {
        presenter.doFacebookLogin(this, callbackManager);
    }

    private View.OnClickListener onGoToProduct(final InboxReputationDetailItem inboxReputationDetail) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductInfoActivity.createInstance(context, inboxReputationDetail.getProductId());
                context.startActivity(intent);
            }
        };
    }

    private ImageUploadAdapter.ProductImageListener onImageUploadActionListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextReview.clearFocus();
                        KeyboardHandler.DropKeyboard(getActivity(), editTextReview);
                        presenter.onImageUploadClicked(position);
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                        myAlertDialog.setMessage(getActivity().getString(R.string.dialog_upload_option));
                        myAlertDialog.setPositiveButton(getActivity().getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InboxReputationFormFragmentPermissionsDispatcher.actionImagePickerWithCheck(InboxReputationFormFragment.this);
                            }
                        });
                        myAlertDialog.setNegativeButton(getActivity().getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InboxReputationFormFragmentPermissionsDispatcher.actionCameraWithCheck(InboxReputationFormFragment.this);
                            }
                        });
                        Dialog dialog = myAlertDialog.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, final ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextReview.clearFocus();
                        KeyboardHandler.DropKeyboard(getActivity(), editTextReview);
                        presenter.onImageClicked(position, imageUpload);
                    }
                };
            }
        };
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        presenter.openCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        presenter.openImageGallery();
    }

    @Override
    protected void setViewListener() {
        starQuality.setOnRatingBarChangeListener(onRatingChanged(starQualityDescription, qualityError));
        starAccuracy.setOnRatingBarChangeListener(onRatingChanged(starAccuracyDescription, accuracyError));
        submitButton.setOnClickListener(onSubmitClickListener());
    }

    private RatingBar.OnRatingBarChangeListener onRatingChanged(final TextView description, final TextView error) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                description.setText(getActivity().getString(presenter.getRatingDescription(rating)));
                error.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public String getMessageReview() {
        return editTextReview.getText().toString();
    }

    @Override
    public void showMessageReviewError(int resId) {
        editTextReview.setError(getActivity().getString(resId));
    }

    @Override
    public String getAccuracyRating() {
        return String.valueOf(starAccuracy.getRating());
    }

    @Override
    public String getQualityRating() {
        return String.valueOf(starQuality.getRating());
    }

    @Override
    public void showRatingAccuracyError() {
        accuracyError.setVisibility(View.VISIBLE);
        accuracyError.requestFocus();
    }

    @Override
    public void showRatingQualityError() {
        qualityError.setVisibility(View.VISIBLE);
        qualityError.requestFocus();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
        progressDialog.showDialog();
    }

    @Override
    public View.OnClickListener onSubmitClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSubmitReview();
            }
        };
    }

    @Override
    public void setFormFromCache(ActReviewPass review) {
        starAccuracy.setRating(Float.parseFloat(review.getAccuracyRate()));
        starQuality.setRating(Float.parseFloat(review.getQualityRate()));
        editTextReview.setText(review.getReviewMessage());
        adapter.addList(review.getImageUploads());
        adapter.setDeletedList(review.getDeletedImageUploads());

    }

    @Override
    public void onSuccessPostReview(Bundle resultData) {
        setActionsEnabled(true);
        dismissProgressDialog();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_success", 1);
        bundle.putString("action",
                InboxReputationDetailFragmentPresenterImpl.ACTION_UPDATE_PRODUCT);
        intent.putExtras(bundle);
        if(checkBox.isChecked()){
            showDialogShareFb(intent);
        }else {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void onSuccessEditReview(Bundle resultData) {
        setActionsEnabled(true);
        dismissProgressDialog();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_success", 1);
        bundle.putString("action",
                InboxReputationDetailFragmentPresenterImpl.ACTION_UPDATE_PRODUCT);
        intent.putExtras(bundle);
        if(checkBox.isChecked()){
            showDialogShareFb(intent);
        }else {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public String getToken() {
        return getArguments().getString(PARAM_TOKEN, "");
    }

    @Override
    public ImageUploadAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        submitButton.setEnabled(isEnabled);

    }

    @Override
    public void removeError() {
        editTextReview.setError(null);
    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxReputationFormFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        inboxReputation = getArguments().getParcelable(PARAM_INBOX_REPUTATION);
        inboxReputationDetail = getArguments().getParcelable(PARAM_INBOX_REPUTATION_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    public void setImages(ArrayList<ImageUpload> list) {
        adapter.addList(list);
    }

    public InboxReputationDetailItem getInboxReputationDetail() {
        inboxReputationDetail = getArguments().getParcelable(PARAM_INBOX_REPUTATION_DETAIL);
        return inboxReputationDetail;
    }

    public InboxReputationItem getInboxReputation() {
        inboxReputation = getArguments().getParcelable(PARAM_INBOX_REPUTATION);
        return inboxReputation;
    }

    @Override
    public void onFailedPostReview(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if (errorMessage.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                SnackbarManager.make(getActivity(), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        dismissProgressDialog();
        setActionsEnabled(true);
    }

    @Override
    public void onFailedEditReview(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if (errorMessage.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                SnackbarManager.make(getActivity(), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        dismissProgressDialog();
        setActionsEnabled(true);
    }

    @Override
    public void showDialogShareFb(final Intent intent) {
        progressDialog.showDialog();
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        String stringDomain = getString(R.string.domain);
        String contentDescription = editTextReview.getText().toString();
        presenter.prepareDialogShareFb(this, shareDialog, callbackManager, inboxReputationDetail
                , stringDomain, contentDescription, intent);
    }

    @Override
    public void onSuccessSharingFacebook(Intent intent) {
        getActivity().setResult(Activity.RESULT_OK, intent.putExtra(getString(R.string.message),getString(R.string.success_share_review)));
        getActivity().finish();
    }

    @Override
    public void onErrorSharingFacebook(Intent intent) {
        getActivity().setResult(Activity.RESULT_OK, intent.putExtra(getString(R.string.message),getString(R.string.error_share_review)));
        getActivity().finish();
    }

    @Override
    public void onCancelSharingFacebook(Intent intent) {
        getActivity().setResult(Activity.RESULT_OK, null);
        getActivity().finish();
    }

    @Override
    public void unTickCheckBox() {
        checkBox.setChecked(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InboxReputationFormFragmentPermissionsDispatcher.onRequestPermissionsResult(
                InboxReputationFormFragment.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

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

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }
}
