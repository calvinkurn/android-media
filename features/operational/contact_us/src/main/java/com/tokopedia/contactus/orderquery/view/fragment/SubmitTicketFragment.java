package com.tokopedia.contactus.orderquery.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.di.DaggerOrderQueryComponent;
import com.tokopedia.contactus.orderquery.di.OrderQueryComponent;
import com.tokopedia.contactus.orderquery.view.adapter.ImageUploadAdapter;
import com.tokopedia.contactus.orderquery.view.presenter.SubmitTicketContract;
import com.tokopedia.contactus.orderquery.view.presenter.SubmitTicketPresenter;
import com.tokopedia.imagepicker.core.ImagePickerBuilder;
import com.tokopedia.imagepicker.core.ImagePickerResultExtractor;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;


public class SubmitTicketFragment extends BaseDaggerFragment implements SubmitTicketContract.View, ImageUploadAdapter.OnSelectImageClick, View.OnClickListener {

    private static final int REQUEST_CODE_IMAGE = 1001;
    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";

    private ConstraintLayout constraint_layout;
    private ImageView imgProduct;
    private TextView txtInvoiceNo;
    private TextView txtInvoiceTitle;
    private TextView txtQueryTitle;
    private AppCompatEditText edtQuery;
    private TextView sendButton;
    private ImageUploadAdapter imageUploadAdapter;
    private ConstraintLayout toolTipLayout;
    private ConstraintLayout submitSuccess;
    String mInvoiceNumber;

    OrderQueryComponent orderQueryComponent;
    @Inject
    SubmitTicketPresenter presenter;

    private RecyclerView rvSelectedImages;

    public static SubmitTicketFragment newInstance(SubmitTicketInvoiceData submitTicketInvoiceData) {
        Bundle args = new Bundle();
        SubmitTicketFragment fragment = new SubmitTicketFragment();
        args.putSerializable(KEY_QUERY_TICKET, submitTicketInvoiceData);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_invoice_form, container, false);
        initInjector();
        findingViewsId(view);
        presenter.attachView(this);
        imageUploadAdapter = new ImageUploadAdapter(getContext(),this);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSelectedImages.setAdapter(imageUploadAdapter);
        edtQuery.addTextChangedListener(watcher());
        settingClickListener(view);
        return view;

    }

    private void findingViewsId(View view) {
        constraint_layout = view.findViewById(R.id.constraint_layout);
        imgProduct = view.findViewById(R.id.img_product);
        txtInvoiceNo = view.findViewById(R.id.txt_invoice_no);
        txtInvoiceTitle = view.findViewById(R.id.txt_invoice_title);
        txtQueryTitle = view.findViewById(R.id.txt_query_title);
        edtQuery = view.findViewById(R.id.edt_query);
        sendButton = view.findViewById(R.id.btn_send);
        toolTipLayout = view.findViewById(R.id.tooltiplayout);
        submitSuccess = view.findViewById(R.id.submit_success);
        rvSelectedImages = view.findViewById(R.id.rv_selected_images);
    }

    private void settingClickListener(View view) {
        view.findViewById(R.id.img_tooltip).setOnClickListener(this);
        sendButton.setOnClickListener(this);
        view.findViewById(R.id.btn_tutup).setOnClickListener(this);
        view.findViewById(com.tokopedia.design.R.id.btn_ok).setOnClickListener(this);
    }

    private TextWatcher watcher() {
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
        mInvoiceNumber = number;
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
            sendButton.setBackground(getResources().getDrawable(R.drawable.contactus_rounded_rectangle_grey_solid));
        }
    }

    @Override
    public void setSnackBarErrorMessage(String hello) {
        final Snackbar snackbar = Snackbar.make(constraint_layout, hello, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View snackView = inflater.inflate(R.layout.snackbar_error_layout, layout);
        TextView tv = snackView.findViewById(R.id.tv_msg);
        tv.setText(hello);
        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(view -> snackbar.dismiss());
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> imagePathList = ImagePickerResultExtractor.extract(data).getImageUrlOrPathList();
            if (imagePathList.size() <= 0) {
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
        ImagePickerBuilder builder = ImagePickerBuilder.getOriginalImageBuilder(requireContext());
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    public void onToolTipImgClicked() {
        presenter.onToolTipClick();
    }

    public void onSendClick() {
        String invoiceLabel = "With Invoice";
        if(mInvoiceNumber.equals("")){
            invoiceLabel = "Without Invoice";
        }
        ContactUsTracking.eventSuccessClick(getContext(),invoiceLabel);
        presenter.onSendButtonClick();
    }

    public void ontutupClick() {
        toolTipLayout.setVisibility(View.GONE);
    }

    @Override
    public void showToolTip() {
        toolTipLayout.setVisibility(View.VISIBLE);
    }

    public void onOkClick() {
        ContactUsTracking.eventOkClick(getContext());
        submitSuccess.setVisibility(View.GONE);
        getActivity().startActivity(new Intent(getActivity(), InboxListActivity.class));
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.sendBroadcast(new Intent(ContactUsConstant.ACTION_CLOSE_ACTIVITY));
    }

    @Override
    public void showSuccessDialog() {
        submitSuccess.setVisibility(View.VISIBLE);
    }

    public boolean onBackPressed() {
        if (imageUploadAdapter.getItemCount() > 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.inbox_title_dialog_wrong_scan));
            builder.setMessage("Pesan Anda akan hilang jika menutup halaman ini, Anda yakin?");
            builder.setNegativeButton(getString(R.string.inbox_cancel),
                    (dialog, i) -> {
                        dialog.dismiss();
                        //presenter.onRetryClick();
                    });
            builder.setPositiveButton(getString(R.string.inbox_exit),
                    (dialogInterface, i) -> getActivity().finish()).create().show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick() {
        showImagePickerDialog();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.img_tooltip){
            onToolTipImgClicked();
        }else if(id==R.id.btn_send){
            onSendClick();
        }else if(id==R.id.btn_tutup){
            ontutupClick();
        }else if(id==com.tokopedia.design.R.id.btn_ok){
            onOkClick();
        }
    }
}
