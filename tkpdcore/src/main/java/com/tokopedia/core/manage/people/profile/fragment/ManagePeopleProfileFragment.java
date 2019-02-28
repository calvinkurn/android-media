package com.tokopedia.core.manage.people.profile.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
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
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import butterknife.BindView;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManagePeopleProfileFragment extends BasePresenterFragment<ManagePeopleProfileFragmentPresenter>
        implements ManagePeopleProfileFragmentView {

    @SuppressWarnings("unused")
    private static final String TAG = ManagePeopleProfileFragment.class.getSimpleName();
    private static final String IMAGE_PATH_DATA = "IMAGE_PATH_DATA";
    private static final String PROFILE_DATA = "PROFILE_DATA";
    public static final int REQUEST_VERIFY_PHONE = 123;
    public static final int REQUEST_CHANGE_PHONE_NUMBER = 13;
    public static final int RESULT_EMAIL_SENT = 111;

    public static final int REQUEST_ADD_EMAIL = 1001;
    public static final int REQUEST_CHANGE_NAME = 1002;

    public static final int REQUEST_CODE_PROFILE_PICTURE = 1202;


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
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_profile_picture),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(
                        new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false,
                        null)
                , null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_PROFILE_PICTURE);
    }

    @Override
    public void showEmailVerificationDialog(String userEmail) {
        UserSessionInterface userSession = new UserSession(getActivity());
        if (userSession.hasPassword()) {
            DialogFragment fragment = EmailVerificationDialogFragment.createInstance(userEmail,
                    new EmailVerificationDialogFragment.EmailChangeConfirmationListener() {
                        @Override
                        public void onEmailChanged() {
                            presenter.setOnNotifiedEmailChanged(getActivity());
                        }
                    });
            fragment.show(getFragmentManager(), EmailVerificationDialogFragment.class.getSimpleName());
        } else {
            showChangeEmailNoPassword(getActivity());
        }
    }

    private void showChangeEmailNoPassword(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.error_changeemail_no_password_title));
        builder.setMessage(context.getResources().getString(R.string.error_changeemail_no_password_content));
        builder.setPositiveButton(context.getResources().getString(R.string.error_no_password_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intentToAddPassword(context);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.error_no_password_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(context, R.color.black_54));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    private void intentToAddPassword(Context context) {
        context.startActivity(
                ((TkpdCoreRouter) context.getApplicationContext())
                        .getAddPasswordIntent(context));
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
        if (requestCode == REQUEST_CHANGE_PHONE_NUMBER) {
            if (resultCode == Activity.RESULT_OK) {
                getProfileData().getDataUser().setUserPhone(SessionHandler.getPhoneNumber());
                renderData();
                NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.success_change_phone_number));
                UnifyTracking.eventSuccessChangePhoneNumber(getActivity());
            }

            if (resultCode == RESULT_EMAIL_SENT) {
                contactSection.checkEmailInfo.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == REQUEST_ADD_EMAIL || requestCode == REQUEST_CHANGE_NAME) {
            if (resultCode == Activity.RESULT_OK)
                presenter.setOnFirstTimeLaunch(getActivity());
        } else if (resultCode == Activity.RESULT_OK &&
                requestCode == REQUEST_VERIFY_PHONE &&
                SessionHandler.isMsisdnVerified()) {
            getProfileData().getDataUser().setUserPhone(SessionHandler.getPhoneNumber());
            renderData();
        } else if (requestCode == REQUEST_CODE_PROFILE_PICTURE){
            if (resultCode == Activity.RESULT_OK && data!= null) {
                ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
                    presenter.setOnUserFinishPickImage(imageUrlOrPathList.get(0));
                }
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
        if (contactSection.tvPhone.getVisibility() == View.VISIBLE) {
            return String.valueOf(contactSection.tvPhone.getText());
        }
        return String.valueOf(contactSection.phone.getText());
    }

    @Override
    public void setPhoneError(String errorMessage) {
        contactSection.phone.setError(errorMessage);
        contactSection.phone.requestFocus();
    }

    @Override
    public String getEmail() {
        if (contactSection.tvEmail.getVisibility() == View.VISIBLE) {
            return String.valueOf(contactSection.tvEmail.getText());
        }
        return String.valueOf(contactSection.email.getText());
    }

    @Override
    public void setEmailError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setVerificationError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setBirthDayError(String errorMessage) {
        detailSection.birthDate.setError(errorMessage);
        detailSection.birthDate.requestFocus();
    }

    @Override
    public String getVerifiedPhone() {
        if (contactSection.tvPhone.getVisibility() == View.VISIBLE) {
            return String.valueOf(contactSection.tvPhone.getText());
        }
        return String.valueOf(contactSection.phone.getText());
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
    public void setActivityResultSuccess() {
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void startChangePhoneNumber() {
        if (getActivity() != null && getActivity().getApplicationContext() != null
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter) {
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
    }

    @Override
    public void showDialogChangePhoneNumberEmptyEmail() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.error_change_number_no_email_title));
        builder.setMessage(getResources().getString(R.string.error_change_number_no_email_content));
        builder.setPositiveButton(getResources().getString(R.string.error_change_number_no_email_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startAddEmailActivity();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.error_change_number_no_email_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void startAddEmailActivity() {
        startActivityForResult(
                ((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getAddEmailIntent(getActivity()), REQUEST_ADD_EMAIL);
    }

    @Override
    public void startChangeNameActivity() {
        startActivityForResult(
                ((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getChangeNameIntent(getActivity()), REQUEST_CHANGE_NAME);
    }

    @Override
    public void storeImageToUserSession(String userImage) {
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        sessionHandler.setProfilePicture(userImage);
    }
}
