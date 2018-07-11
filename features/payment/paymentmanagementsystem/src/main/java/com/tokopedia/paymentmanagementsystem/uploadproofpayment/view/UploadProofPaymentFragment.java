package com.tokopedia.paymentmanagementsystem.uploadproofpayment.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.common.Constant;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;
import com.tokopedia.paymentmanagementsystem.uploadproofpayment.di.DaggerUploadProofPaymentComponent;
import com.tokopedia.paymentmanagementsystem.uploadproofpayment.di.UploadProofPaymentModule;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentFragment extends BaseDaggerFragment implements UploadProofPaymentContract.View {

    private static final int REQUEST_CODE_IMAGE_PROOF = 845;
    @Inject
    UploadProofPaymentPresenter uploadProofPaymentPresenter;

    private PaymentListModel paymentListModel;
    private ImageView imageViewProof;
    private Button buttonSave;
    private String imageUrl = "";

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
    public void onCreate(Bundle savedInstanceState) {
        paymentListModel = getArguments().getParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_proof_payment, container, false);
        imageViewProof = view.findViewById(R.id.iv_proof_payment);
        buttonSave = view.findViewById(R.id.button_save);

        imageViewProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProofPaymentPresenter.uploadProofPayment(paymentListModel.getTransactionId(), paymentListModel.getMerchantCode(), imageUrl);
            }
        });
        return view;
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                null
                , null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PROOF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PROOF && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                imageUrl = imageUrlOrPathList.get(0);
            }
            ImageHandler.LoadImage(imageViewProof, imageUrl);
        }
    }

    public static Fragment createInstance(PaymentListModel paymentListModel) {
        UploadProofPaymentFragment uploadProofPaymentFragment = new UploadProofPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA, paymentListModel);
        uploadProofPaymentFragment.setArguments(bundle);
        return uploadProofPaymentFragment;
    }
}
