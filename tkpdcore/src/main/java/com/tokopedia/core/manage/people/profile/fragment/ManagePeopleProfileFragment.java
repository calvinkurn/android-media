package com.tokopedia.core.manage.people.profile.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.manage.people.profile.customdialog.UploadImageDialog;
import com.tokopedia.core.manage.people.profile.customview.AvatarView;
import com.tokopedia.core.manage.people.profile.customview.ContactView;
import com.tokopedia.core.manage.people.profile.customview.DetailView;
import com.tokopedia.core.manage.people.profile.listener.ManagePeopleProfileFragmentView;
import com.tokopedia.core.manage.people.profile.listener.ManagePeopleProfileView;
import com.tokopedia.core.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileFragmentImpl;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

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
 * A placeholder fragment containing a simple view.
 */
@RuntimePermissions
public class ManagePeopleProfileFragment extends BasePresenterFragment<ManagePeopleProfileFragmentPresenter>
        implements ManagePeopleProfileFragmentView {

    @SuppressWarnings("unused")
    private static final String TAG = ManagePeopleProfileFragment.class.getSimpleName();
    private static final String IMAGE_PATH_DATA = "IMAGE_PATH_DATA";
    private static final String PROFILE_DATA = "PROFILE_DATA";
    public static final int REQUEST_VERIFY_PHONE = 123;
    public static final int REQUEST_CHANGE_PHONE_NUMBER = 13;
    public static final int RESULT_EMAIL_SENT = 111;

    @BindView(R2.id.layout_main)
    View layoutMain;
    @BindView(R2.id.layout_manage_people_profile_avatar_view)
    AvatarView avatarSection;
    @BindView(R2.id.layout_manage_people_profile_detail_view)
    DetailView detailSection;
    @BindView(R2.id.layout_manage_people_profile_contact_view)
    ContactView contactSection;
    @BindView(R2.id.save_button)
    View saveButton;

    private ManagePeopleProfileView listener;
    private TkpdProgressDialog loading;
    private UploadImageDialog uploadDialog;
    private String imagePathData;
    private Profile profileData;

    public static Fragment newInstance() {
        ManagePeopleProfileFragment fragment = new ManagePeopleProfileFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ManagePeopleProfileFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunch(getActivity());
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(IMAGE_PATH_DATA, getImagePath());
        state.putParcelable(PROFILE_DATA, getProfileData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setImagePath(savedState.getString(IMAGE_PATH_DATA));
        setProfileData((Profile) savedState.getParcelable(PROFILE_DATA));
        if (getProfileData() != null)
            presenter.setOnRequestSuccess(getProfileData());
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePeopleProfileFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (ManagePeopleProfileView) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_people_profile;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        avatarSection.setPresenter(presenter);
        contactSection.setPresenter(presenter);
        detailSection.setPresenter(presenter);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSaveButtonClicked(getActivity());
            }
        });
    }

    @Override
    protected void initialVar() {
        loading = new TkpdProgressDialog(
                getActivity(),
                TkpdProgressDialog.MAIN_PROGRESS,
                getActivity().getWindow().getDecorView().getRootView()
        );
        loading.setLoadingViewId(R.id.include_loading);
        uploadDialog = new UploadImageDialog(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onDestroy() {
        presenter.setOnFragmentDestroyed(getActivity());
        super.onDestroy();
    }

    @Override
    public void setLoadingView(boolean isVisible) {
        if (isVisible) {
            loading.showDialog();
        } else {
            loading.dismiss();
        }
    }

    @Override
    public void setMainView(boolean isVisible) {
        if (isVisible) {
            layoutMain.setVisibility(View.VISIBLE);
        } else {
            layoutMain.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTimeOutView(RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void setErrorView(String messageError) {
        if (messageError != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageError, null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        }
    }

    @Override
    public void setProfileData(Profile result) {
        this.profileData = result;
    }

    @Override
    public Profile getProfileData() {
        return profileData;
    }

    @Override
    public void renderData() {
        if (getProfileData() != null) {
            avatarSection.renderData(getProfileData());
            detailSection.renderData(getProfileData());
            contactSection.renderData(getProfileData());
        }
    }

    @Override
    public void callListenerToSave(PeopleProfilePass paramPass) {
        if (listener != null) {
            listener.callServiceToSaveProfile(paramPass);
        }
    }

    @Override
    public void resetError() {
        detailSection.userName.setError(null);
        detailSection.birthDate.setError(null);
        detailSection.hobby.setError(null);
        contactSection.email.setError(null);
        contactSection.phone.setError(null);
    }


    @Override
    public void showUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagePeopleProfileFragmentPermissionsDispatcher.actionImagePickerWithCheck(ManagePeopleProfileFragment.this);

            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagePeopleProfileFragmentPermissionsDispatcher.actionCameraWithCheck(ManagePeopleProfileFragment.this);
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        uploadDialog.openImagePicker();

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadDialog.openCamera();

    }

    @Override
    public void showEmailVerificationDialog(String userEmail) {
        DialogFragment fragment = EmailVerificationDialogFragment.createInstance(userEmail,
                new EmailVerificationDialogFragment.EmailChangeConfirmationListener() {
                    @Override
                    public void onEmailChanged() {
                        presenter.setOnNotifiedEmailChanged(getActivity());
                    }
                });
        fragment.show(getFragmentManager(), EmailVerificationDialogFragment.class.getSimpleName());
    }

    @Override
    public void showPhoneVerificationDialog(String userPhone) {
        SessionHandler.setPhoneNumber(userPhone);
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                    .getPhoneVerificationProfileIntent(getActivity());
            startActivityForResult(intent, REQUEST_VERIFY_PHONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE) {
            if (requestCode == REQUEST_VERIFY_PHONE &&
                    SessionHandler.isMsisdnVerified()) {
                getProfileData().getDataUser().setUserPhone(SessionHandler.getPhoneNumber());
                renderData();
            } else {
                uploadDialog.onResult(
                        requestCode,
                        resultCode,
                        data,
                        new UploadImageDialog.UploadImageDialogListener() {
                            @Override
                            public void onSuccess(String data) {
                                presenter.setOnUserFinishPickImage(data);
                            }

                            @Override
                            public void onFailed() {
                                showSnackBarView(getActivity().getString(R.string.error_gallery_valid));
                            }
                        });
            }
        }

        if (requestCode == REQUEST_CHANGE_PHONE_NUMBER) {
            if (resultCode == Activity.RESULT_OK) {
                getProfileData().getDataUser().setUserPhone(SessionHandler.getPhoneNumber());
                renderData();
                NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.success_change_phone_number));
                UnifyTracking.eventSuccessChangePhoneNumber();
            }

            if (resultCode == RESULT_EMAIL_SENT) {
                contactSection.checkEmailInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setReceiveResult(int resultCode, Bundle resultData) {
        presenter.processResult(getActivity(), resultCode, resultData);
    }

    @Override
    public void showSnackBarView(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public String getPhone() {
        return String.valueOf(contactSection.phone.getText());
    }

    @Override
    public void setPhoneError(String errorMessage) {
        contactSection.phone.setError(errorMessage);
        contactSection.phone.requestFocus();
    }

    @Override
    public String getEmail() {
        return String.valueOf(contactSection.email.getText());
    }

    @Override
    public void setEmailError(String errorMessage) {
        contactSection.email.setError(errorMessage);
        contactSection.email.requestFocus();
    }

    @Override
    public void setVerificationError(String errorMessage) {
        contactSection.verification.setError(errorMessage);
        contactSection.verification.requestFocus();
    }

    @Override
    public void setBirthDayError(String errorMessage) {
        detailSection.birthDate.setError(errorMessage);
        detailSection.birthDate.requestFocus();
    }

    @Override
    public String getVerifiedPhone() {
        return String.valueOf(contactSection.verification.getText());
    }

    @Override
    public String getMessanger() {
        return String.valueOf(contactSection.messenger.getText());
    }

    @Override
    public String getHobby() {
        return String.valueOf(detailSection.hobby.getText());
    }

    @Override
    public String getUserName() {
        return String.valueOf(detailSection.userName.getText());
    }

    @Override
    public String getBirthDay() {
        return String.valueOf(detailSection.birthDate.getText());
    }

    @Override
    public int getGender() {
        return detailSection.genderRadioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void prepareToUploadAvatar(String data) {
        avatarSection.loadTemporaryPickedImage(data);
    }

    @Override
    public String getImagePath() {
        return imagePathData;
    }

    @Override
    public void setImagePath(String imagePathData) {
        this.imagePathData = imagePathData;
    }

    @Override
    public void dismissKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void storeImageToDrawer(String userImage100) {
        LocalCacheHandler userCache = new LocalCacheHandler(context,
                TkpdState.CacheName.CACHE_USER);
        userCache.putString("user_pic_uri", userImage100);
        userCache.applyEditor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ManagePeopleProfileFragmentPermissionsDispatcher.onRequestPermissionsResult(
                ManagePeopleProfileFragment.this, requestCode, grantResults);

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

    @Override
    public void setActivityResultSuccess() {
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void startChangePhoneNumber() {
        startActivityForResult(
                ((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getChangePhoneNumberIntent(
                                getActivity(),
                                profileData.getDataUser().getUserEmail(),
                                profileData.getDataUser().getUserPhone()
                        ),
                REQUEST_CHANGE_PHONE_NUMBER
        );
    }

    @Override
    public void startAddEmailActivity() {
        getActivity().startActivity(((TkpdCoreRouter)getActivity().getApplicationContext()).getAddEmailIntent(getActivity()));
    }
}
