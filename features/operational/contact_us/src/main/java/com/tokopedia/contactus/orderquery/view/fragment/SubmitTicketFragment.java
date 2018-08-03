package com.tokopedia.contactus.orderquery.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket.activity.InboxTicketActivity;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.di.DaggerOrderQueryComponent;
import com.tokopedia.contactus.orderquery.di.OrderQueryComponent;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.contactus.orderquery.view.presenter.SubmitTicketContract;
import com.tokopedia.contactus.orderquery.view.presenter.SubmitTicketPresenter;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sandeepgoyal on 16/04/18.
 */
public class SubmitTicketFragment extends BaseDaggerFragment implements SubmitTicketContract.View, ImageUploadAdapter.OnSelectImageClick {

    private static final int REQUEST_CODE_IMAGE = 1001;
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
    @BindView(R2.id.tooltiplayout)
    ConstraintLayout toolTipLayout;
    @BindView(R2.id.submit_success)
    ConstraintLayout submitSuccess;


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
        imageUploadAdapter = new ImageUploadAdapter(getContext(),this);
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
                if (s.length() >= 30) {
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
    public void addimage(ImageUpload image) {
        imageUploadAdapter.addImage(image);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    ProgressDialog progress;

    @Override
    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.setIndeterminate(true);
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }


    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        sendButton.setClickable(enabled);
        if (enabled) {
            sendButton.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_greenbutton_solid));
        } else {
            sendButton.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey_solid));
        }
    }

    @Override
    public void setSnackBarErrorMessage(String hello) {
        final Snackbar snackbar = Snackbar.make(constraint_layout, hello, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS);
            if (imagePathList == null || imagePathList.size() <= 0) {
                return;
            }
            String imagePath = imagePathList.get(0);
            if (!TextUtils.isEmpty(imagePath)) {
                if (rvSelectedImages != null && rvSelectedImages.getVisibility() == View.GONE) {
                    rvSelectedImages.setVisibility(View.VISIBLE);
                }
                int position = imageUploadAdapter.getItemCount();
                ImageUpload image = new ImageUpload();
                image.setPosition(position);
                image.setImageId("image" + UUID.randomUUID().toString());
                image.setFileLoc(imagePath);
                presenter.onImageSelect(image);
            }
        }

    }

    private void showImagePickerDialog() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true,
                null, null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @OnClick(R2.id.img_tooltip)
    public void onToolTipImgClicked() {
        presenter.onToolTipClick();
    }

    @OnClick(R2.id.btn_send)
    public void onSendClick() {
        presenter.onSendButtonClick();
    }

    @OnClick(R2.id.btn_tutup)
    public void ontutupClick() {
        toolTipLayout.setVisibility(View.GONE);
    }

    @Override
    public void showToolTip() {
        toolTipLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.btn_ok)
    public void onOkClick() {
        submitSuccess.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), InboxTicketActivity.class);
        getActivity().startActivity(new Intent(getActivity(), InboxTicketActivity.class));
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.sendBroadcast(new Intent(ContactUsModuleRouter.ACTION_CLOSE_ACTIVITY));
    }

    @Override
    public void showSuccessDialog() {
        submitSuccess.setVisibility(View.VISIBLE);
    }

    public boolean onBackPressed() {
        if (imageUploadAdapter.getItemCount() > 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.title_dialog_wrong_scan));
            builder.setMessage("Pesan Anda akan hilang jika menutup halaman ini, Anda yakin?");
            builder.setNegativeButton(getString(R.string.batal),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            //presenter.onRetryClick();
                        }
                    });
            builder.setPositiveButton(getString(R.string.keular),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    }).create().show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick() {
        showImagePickerDialog();
    }
}
