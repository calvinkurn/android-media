package com.tokopedia.reksadana.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.view.activities.ReksaDanaHomeActivity;
import com.tokopedia.reksadana.view.data.initdata.DropdownValues;
import com.tokopedia.reksadana.view.data.initdata.FieldData;
import com.tokopedia.reksadana.view.data.signimageurl.ImageDetails;
import com.tokopedia.reksadana.view.data.submit.UserDetails;
import com.tokopedia.reksadana.di.DaggerReksaDanaComponent;
import com.tokopedia.reksadana.di.ReksaDanaComponent;
import com.tokopedia.reksadana.view.presenter.ReksaDanaContract;
import com.tokopedia.reksadana.view.presenter.ReksaDanaPresenter;
import com.tokopedia.reksadana.view.utils.Signature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ReksaDanaHomeFragment extends BaseDaggerFragment implements ReksaDanaContract.View {

    ReksaDanaComponent reksaDanaComponent;
    private TextView name;
    private EditText email;
    private TextView educationText;
    private Spinner educationSpinner;
    private TextView educationError;
    private TextView incomeText;
    private Spinner incomeSpinner;
    private TextView incomeError;
    private TextView incomeSourceText;
    private Spinner incomeSourceSpinner;
    private TextView incomeSourceError;
    private TextView investmentText;
    private Spinner investmentSpinner;
    private TextView investmentError;
    private TextView occupationText;
    private Spinner occupationSpinner;
    private TextView occupationError;
    private LinearLayout imageViewLayout;
    private ImageView uploadedImage;
    private LinearLayout addImage;
    private RelativeLayout mainSignLayout;
    private LinearLayout signLayout;
    private ImageView retrySign;
    private CheckBox checkBox;
    private TextView checkBoxText;
    private TextView submitBtn;
    private ScrollView scrollView;
    private LinearLayout progressLayout;
    private TextView progressText;
    private static final String UPLOAD_ID = "UPLOAD_ID";
    private static final String PARAM_UPLOAD_TYPE = "PARAM_UPLOAD_TYPE";
    String uploadType;
    Model model = new Model();
    String fileName;
    String fileLoc;
    ImageDetails imageDetails;
    List<String> occupationKeys;
    List<String> educationKeys;
    List<String> incomeKeys;
    List<String> incomeSourceKeys;
    List<String> investmentKeys;
    @Inject
    ReksaDanaPresenter presenter;
    private Bitmap bitmap;
    private Signature mSignature;

    public static Fragment newInstance() {
        return new ReksaDanaHomeFragment();
    }

    @Override
    protected void initInjector() {
        reksaDanaComponent = DaggerReksaDanaComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        GraphqlClient.init(getActivity());
        reksaDanaComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            uploadType = savedInstanceState.getString(PARAM_UPLOAD_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reksa_dana_home, container, false);
        presenter.attachView(this);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        educationText = view.findViewById(R.id.education_key);
        educationSpinner = view.findViewById(R.id.education_spinner);
        educationError = view.findViewById(R.id.education_error);
        incomeText = view.findViewById(R.id.income_key);
        incomeSpinner = view.findViewById(R.id.income_spinner);
        incomeError = view.findViewById(R.id.income_error);
        incomeSourceText = view.findViewById(R.id.income_source_key);
        incomeSourceSpinner = view.findViewById(R.id.income_source_spinner);
        incomeSourceError = view.findViewById(R.id.income_source_error);
        investmentText = view.findViewById(R.id.investment_key);
        investmentSpinner = view.findViewById(R.id.investment_spinner);
        investmentError = view.findViewById(R.id.investment_error);
        occupationText = view.findViewById(R.id.occupation_key);
        occupationSpinner = view.findViewById(R.id.occupation_spinner);
        occupationError = view.findViewById(R.id.occupation_error);
        imageViewLayout = view.findViewById(R.id.upload_image);
        uploadedImage = view.findViewById(R.id.uploaded_image);
        addImage = view.findViewById(R.id.add_img);
        signLayout = view.findViewById(R.id.canvas_layout);
        mSignature = new Signature(getActivity(), null);
        scrollView = view.findViewById(R.id.scroll_view);
        progressLayout = view.findViewById(R.id.progress_layout);
        progressText = view.findViewById(R.id.progress_text);
        mSignature.setBackgroundColor(Color.GRAY);
        signLayout.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainSignLayout = view.findViewById(R.id.main_sign_layout);
        retrySign = view.findViewById(R.id.retry_sign);
        checkBox = view.findViewById(R.id.checkbox);
        checkBoxText = view.findViewById(R.id.checkbox_text);
        submitBtn = view.findViewById(R.id.submit_btn);
        occupationKeys = new ArrayList<>();
        educationKeys = new ArrayList<>();
        incomeKeys = new ArrayList<>();
        incomeSourceKeys = new ArrayList<>();
        investmentKeys = new ArrayList<>();
        String text = checkBoxText.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf("syarat");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                /*if(!checkBox.isChecked())
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);*/
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.green_250)); // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + "syarat dan ketentuan".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBoxText.setText(spannableString);
        setClickListener();
        presenter.fetchData();
        return view;
    }

    private void setClickListener() {
        imageViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                myAlertDialog.setMessage(getActivity().getString(com.tokopedia.core.R.string.dialog_upload_option));
                myAlertDialog.setPositiveButton(getActivity().getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReksaDanaHomeFragmentPermissionsDispatcher.uploadIdImageGalleryWithCheck(ReksaDanaHomeFragment.this);
                    }
                });
                myAlertDialog.setNegativeButton(getActivity().getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReksaDanaHomeFragmentPermissionsDispatcher.uploadIdImageCameraWithCheck(ReksaDanaHomeFragment.this);

                    }


                });
                Dialog dialog = myAlertDialog.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
        retrySign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature.clear();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitData(imageDetails);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    submitBtn.setTextColor(Color.BLACK);
                    submitBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.green_250));
                } else {
                    submitBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_200));

                }
            }
        });

    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void disableProgressVisibility() {
        progressLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressVisility() {
        progressLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

    }

    @Override
    public UserDetails getRegistrationData(String publicUrl) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        UserDetails details = null;
        if (email.getText() != null && educationSpinner.getSelectedItem() != null &&
                occupationSpinner.getSelectedItem() != null && incomeSpinner.getSelectedItem() != null
                && incomeSourceSpinner.getSelectedItem() != null && investmentSpinner.getSelectedItem() != null) {
            details = new UserDetails(email.getText().toString(), educationKeys.get(educationSpinner.getSelectedItemPosition() - 1),
                    occupationKeys.get(occupationSpinner.getSelectedItemPosition() - 1),
                    incomeKeys.get(incomeSpinner.getSelectedItemPosition() - 1),
                    incomeSourceKeys.get(incomeSourceSpinner.getSelectedItemPosition() - 1),
                    investmentKeys.get(investmentSpinner.getSelectedItemPosition() - 1), publicUrl,
                    Base64.encodeToString(byteArray, Base64.DEFAULT));
        }
        return details;
    }

    @Override
    public void setProgressText(String text) {
        progressText.setText(text);
    }

    @Override
    public void saveSignature() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(signLayout.getWidth(), signLayout.getHeight(), Bitmap.Config.RGB_565);
        }
    }

    @Override
    public void setEducation(FieldData educationData) {
        educationText.setText(educationData.itemName());
        educationSpinner.setAdapter(getSpinnerArray(educationData.defaultText(), educationData.dropdownValues(), educationKeys));
    }

    @Override
    public void setIncome(FieldData incomeData) {
        incomeText.setText(incomeData.itemName());
        incomeSpinner.setAdapter(getSpinnerArray(incomeData.defaultText(), incomeData.dropdownValues(), incomeKeys));

    }

    @Override
    public void setIncomeSource(FieldData incomeSourceData) {
        incomeSourceText.setText(incomeSourceData.itemName());
        incomeSourceSpinner.setAdapter(getSpinnerArray(incomeSourceData.defaultText(), incomeSourceData.dropdownValues(), incomeSourceKeys));
    }

    @Override
    public void setInvestment(FieldData investmentData) {
        investmentText.setText(investmentData.itemName());
        investmentSpinner.setAdapter(getSpinnerArray(investmentData.defaultText(), investmentData.dropdownValues(), investmentKeys));
    }

    @Override
    public void setOccupation(FieldData occupationData) {
        occupationText.setText(occupationData.itemName());
        occupationSpinner.setAdapter(getSpinnerArray(occupationData.defaultText(), occupationData.dropdownValues(), occupationKeys));
    }

    private ArrayAdapter<String> getSpinnerArray(String defaultText, List<DropdownValues> dropdownValuesList, List<String> keys) {
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(defaultText);
        for (DropdownValues dropdownValues : dropdownValuesList) {
            keys.add(dropdownValues.key());
            spinnerArray.add(dropdownValues.value());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) return false;
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        return arrayAdapter;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void uploadIdImageCamera() {
        uploadType = UPLOAD_ID;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivityForResult(intent, ImageUploadHandler.REQUEST_CODE);

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void uploadIdImageGallery() {
        uploadType = UPLOAD_ID;
        Intent imageGallery = new Intent(getActivity(), GalleryBrowser.class);
        startActivityForResult(imageGallery, ImageGallery.TOKOPEDIA_GALLERY);

    }

    public Uri getOutputMediaFileUri() {
        return MethodChecker.getUri(getActivity(), getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        String imageCode = uniqueCode();
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + imageCode + ".jpg");
        model.cameraFileLoc = Environment.getExternalStorageDirectory() + File.separator
                + "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";
        return mediaFile;
    }

    private String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }

    public class Model {
        public String cameraFileLoc;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }

    @Override
    public void onRegistrationComplete() {
        ((ReksaDanaHomeActivity)getActivity()).moveToDashboard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (model.cameraFileLoc != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == Activity.RESULT_OK)
                && uploadType.equals(UPLOAD_ID)) {
            addImage.setVisibility(View.GONE);
            uploadedImage.setVisibility(View.VISIBLE);
            loadImageToImageView(uploadedImage, model.cameraFileLoc);
        } else if (data != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == GalleryBrowser.RESULT_CODE)
                && uploadType.equals(UPLOAD_ID)) {
            addImage.setVisibility(View.GONE);
            uploadedImage.setVisibility(View.VISIBLE);
            loadImageToImageView(uploadedImage, data.getStringExtra(ImageGallery.EXTRA_URL));
            //fileLoc = data.getStringExtra(ImageGallery.EXTRA_URL);
        }
    }

    private void loadImageToImageView(ImageView idImage, String fileLoc) {
        if (fileLoc == null) {
            ImageHandler.LoadImage(idImage, model.cameraFileLoc);
            this.fileLoc = model.cameraFileLoc;
        } else {
            ImageHandler.loadImageFromFile(getActivity(), idImage, new File(fileLoc));
            this.fileLoc = fileLoc;
        }
        fileName = fileLoc.substring(fileLoc.lastIndexOf("/") + 1);
        imageDetails = new ImageDetails(fileName, 1, "image/jpeg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ReksaDanaHomeFragmentPermissionsDispatcher.onRequestPermissionsResult(ReksaDanaHomeFragment.this,
                requestCode, grantResults);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    public void save(View v, String StoredPath) {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(signLayout.getWidth(), signLayout.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        try {
            // Output the file
            FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
            v.draw(canvas);
            mFileOutStream.flush();
            mFileOutStream.close();

        } catch (Exception e) {

        }

    }
}
