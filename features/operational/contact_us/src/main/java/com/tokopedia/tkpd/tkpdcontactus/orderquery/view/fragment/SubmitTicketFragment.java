package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.contactus.model.ImageUpload;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.di.DaggerOrderQueryComponent;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.di.OrderQueryComponent;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter.SubmitTicketContract;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter.SubmitTicketPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sandeepgoyal on 16/04/18.
 */
@RuntimePermissions
public class SubmitTicketFragment extends BaseDaggerFragment implements SubmitTicketContract.View {

    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";
    @BindView(R2.id.constraint_layout)
    ConstraintLayout constraint_layout;
    @BindView(R2.id.img_product)
    ImageView imgProduct;
    @BindView(R2.id.txt_invoice_no)
    TextView txtInvoiceNo;
    @BindView(R2.id.txt_invoice_title)
    TextView txtInvoiceTitle;
    @BindView(R2.id.txt_query_title)
    TextView txtQueryTitle;
    @BindView(R2.id.edt_query)
    AppCompatEditText edtQuery;
    @BindView(R2.id.btn_send)
    TextView sendButton;
    ImageUploadAdapter imageUploadAdapter;

    OrderQueryComponent orderQueryComponent;
    @Inject
    SubmitTicketPresenter presenter;

    ImageUploadHandler imageUploadHandler;
    @BindView(R2.id.rv_selected_images)
    RecyclerView rvSelectedImages;

    public static SubmitTicketFragment newInstance(SubmitTicketInvoiceData submitTicketInvoiceData) {
        Bundle args = new Bundle();
        SubmitTicketFragment fragment = new SubmitTicketFragment();
        args.putSerializable(KEY_QUERY_TICKET, submitTicketInvoiceData);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_invoice_form, container, false);
        initInjector();
        ButterKnife.bind(this, view);
        imageUploadHandler = ImageUploadHandler.createInstance(this);
        presenter.attachView(this);
        imageUploadAdapter = new ImageUploadAdapter(getContext());
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSelectedImages.setAdapter(imageUploadAdapter);
        edtQuery.addTextChangedListener(watcher(edtQuery));
        return view;

    }
    private TextWatcher watcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 30){
                    setSubmitButtonEnabled(true);
                } else {
                    setSubmitButtonEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        orderQueryComponent = DaggerOrderQueryComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderQueryComponent.inject(this);
    }

    @Override
    public String getDescription() {
        return edtQuery.getText().toString();
    }

    @Override
    public void setDescriptionError(String error) {

    }

    @Override
    public void setQueryTitle(String title) {
        txtQueryTitle.setText(title);
    }


    @Override
    public SubmitTicketInvoiceData getSubmitTicketInvoiceData() {
        return (SubmitTicketInvoiceData) getArguments().getSerializable(KEY_QUERY_TICKET);
    }

    @Override
    public ArrayList<ImageUpload> getImageList() {
        return imageUploadAdapter.getImageUpload();
    }

    @Override
    public void setInvoiceNumber(String number) {
        txtInvoiceNo.setText(number);
    }

    @Override
    public void setInvoiceTitle(String invoiceTitle) {
        txtInvoiceTitle.setText(invoiceTitle);
    }

    @Override
    public void setInvoiceImage(String imagePath) {
        ImageHandler.loadImageThumbs(getContext(), imgProduct, imagePath);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        sendButton.setClickable(enabled);
        if(enabled) {
            sendButton.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_green_solid));
        } else {
            sendButton.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey_solid));
        }
    }

    @Override
    public void setSnackBarErrorMessage(String hello) {
        final Snackbar snackbar = Snackbar.make(constraint_layout,hello,Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        //layout.setBackgroundColor(getResources().getColor(R.color.red_100));
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

// Inflate our custom view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View snackView = inflater.inflate(R.layout.snackbar_error_layout, null);
        TextView tv = snackView.findViewById(R.id.tv_msg);
        tv.setText(hello);
        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        imageUploadHandler.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {

            if(rvSelectedImages != null && rvSelectedImages.getVisibility() == View.GONE){
                rvSelectedImages.setVisibility(View.VISIBLE);
            }
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
            imageUploadAdapter.addImage(image);

        }

    }

    private void showImagePickerDialog() {
        Context context = getContext();
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(com.tokopedia.core.R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(context.getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SubmitTicketFragmentPermissionsDispatcher.actionImagePickerWithCheck(
                        SubmitTicketFragment.this);
            }
        });
        myAlertDialog.setNegativeButton(context.getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SubmitTicketFragmentPermissionsDispatcher.actionCameraWithCheck(
                        SubmitTicketFragment.this);
            }


        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }


    @OnClick(R2.id.iv_upload)
    public void onViewClicked() {
        showImagePickerDialog();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SubmitTicketFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }




    @OnClick(R2.id.btn_send)
    public void onSendClick() {
        presenter.onSendButtonClick();
    }
}
