package com.tokopedia.pms.proof.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.imagepicker.common.ImagePickerBuilder;
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor;
import com.tokopedia.imagepicker.common.ImagePickerRouterKt;
import com.tokopedia.pms.R;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.view.model.PaymentListModel;
import com.tokopedia.pms.proof.di.DaggerUploadProofPaymentComponent;
import com.tokopedia.pms.proof.di.UploadProofPaymentModule;
import com.tokopedia.pms.proof.model.PaymentProofResponse;

import java.util.List;

import javax.inject.Inject;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.unifycomponents.Toaster;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentFragment extends BaseDaggerFragment implements UploadProofPaymentContract.View {

    private static final int REQUEST_CODE_IMAGE_PROOF = 845;
    public static final int MAX_FILE_SIZE_IN_KB = 10240;

    @Inject
    UploadProofPaymentPresenter uploadProofPaymentPresenter;

    private PaymentListModel paymentListModel;
    private View containerHelpUploadProof;
    private View containerImageUpload;
    private ImageView buttonActionCloseImage;
    private ImageView imageViewProof;
    private Button buttonSave;
    private Button buttonChooseAnotherImage;
    private TextView titleUploadImage;
    private ProgressDialog progressDialog;
    private String imageUrl = "";
    private boolean isUploaded;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerUploadProofPaymentComponent.builder()
                .uploadProofPaymentModule(new UploadProofPaymentModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        uploadProofPaymentPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        uploadProofPaymentPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        paymentListModel = getArguments().getParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_proof_payment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));
        containerHelpUploadProof = view.findViewById(R.id.container_helper);
        containerImageUpload = view.findViewById(R.id.container_image_helper);
        buttonActionCloseImage = view.findViewById(R.id.iv_action_image);
        buttonChooseAnotherImage = view.findViewById(R.id.button_save_choose_another_image);
        titleUploadImage = view.findViewById(R.id.text_confirmation);
        imageViewProof = view.findViewById(R.id.image_payment);
        buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonUpload();
            }
        });
        buttonActionCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUploaded) {
                    resetImageUrl();
                    invalidateView();
                }
            }
        });
        buttonChooseAnotherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void resetImageUrl() {
        imageUrl = "";
    }

    private void invalidateView() {
        if (!TextUtils.isEmpty(imageUrl)) {
            ImageHandler.LoadImage(imageViewProof, imageUrl);
            containerImageUpload.setVisibility(View.VISIBLE);
            containerHelpUploadProof.setVisibility(View.GONE);
            if (isUploaded) {
                buttonActionCloseImage.setImageDrawable(MethodChecker.getDrawable(getActivity(), R.drawable.ic_check_green_payment));
                buttonSave.setText(R.string.payment_label_finish);
                buttonChooseAnotherImage.setVisibility(View.VISIBLE);
                titleUploadImage.setText(R.string.payment_label_succes_upload_proof);
            } else {
                buttonActionCloseImage.setImageDrawable(MethodChecker.getDrawable(getActivity(), com.tokopedia.design.R.drawable.ic_close_default));
                buttonSave.setText(R.string.payment_label_save_image);
                buttonChooseAnotherImage.setVisibility(View.GONE);
                titleUploadImage.setText(R.string.payment_label_confirmation_upload_image);
            }
        } else {
            buttonSave.setText(R.string.payment_label_choose_image);
            buttonChooseAnotherImage.setVisibility(View.GONE);
            containerHelpUploadProof.setVisibility(View.VISIBLE);
            containerImageUpload.setVisibility(View.GONE);
        }
    }

    public void actionButtonUpload() {
        if (buttonSave.getText().toString().equals(getString(R.string.payment_label_choose_image))) {
            openImagePicker();
        } else if (buttonSave.getText().toString().equals(getString(R.string.payment_label_finish))) {
            getActivity().finish();
        } else {
            uploadProofPaymentPresenter.uploadProofPayment(paymentListModel.getTransactionId(), paymentListModel.getMerchantCode(), imageUrl);
        }
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = ImagePickerBuilder.getOriginalImageBuilder(requireContext());
        Intent intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.IMAGE_PICKER);
        ImagePickerRouterKt.putImagePickerBuilder(intent, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PROOF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PROOF && resultCode == Activity.RESULT_OK && data != null) {
            List<String> imageUrlOrPathList = ImagePickerResultExtractor.extract(data).getImageUrlOrPathList();
            if (imageUrlOrPathList.size() > 0) {
                imageUrl = imageUrlOrPathList.get(0);
            }
            isUploaded = false;
            invalidateView();
        }
    }

    public static Fragment createInstance(PaymentListModel paymentListModel) {
        UploadProofPaymentFragment uploadProofPaymentFragment = new UploadProofPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA, paymentListModel);
        uploadProofPaymentFragment.setArguments(bundle);
        return uploadProofPaymentFragment;
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.hide();
    }


    @Override
    public void onErrorUploadProof(Throwable e) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onResultUploadProof(PaymentProofResponse paymentProofResponse) {
        if (paymentProofResponse.getStatus() != null
                && paymentProofResponse.getStatus().equalsIgnoreCase("OK")) {
            isUploaded = true;
            invalidateView();
            getActivity().setResult(Activity.RESULT_OK);
        } else {
            if (getView() != null && paymentProofResponse.getMessageError() != null)
                Toaster.make(getView(), paymentProofResponse.getMessageError(),
                        Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR);
        }
    }
}
