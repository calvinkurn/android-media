package com.tokopedia.shop.open.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.shop.open.data.model.response.isreservedomain.UserData;
import com.tokopedia.shop.open.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.di.component.ShopSettingInfoComponent;
import com.tokopedia.shop.open.domain.model.ShopOpenSaveInfoResponseModel;
import com.tokopedia.shop.open.util.ShopErrorHandler;
import com.tokopedia.shop.open.view.listener.ShopOpenInfoView;
import com.tokopedia.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.shop.open.view.presenter.ShopOpenInfoPresenter;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

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
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryInfoFragment extends BaseDaggerFragment implements ShopOpenInfoView {

    public static final int REQUEST_CODE_IMAGE_PICKER = 532;
    private static final String INFO_TOKO = "Info Toko";

    @Inject
    public ShopOpenInfoPresenter presenter;
    private TkpdHintTextInputLayout shopDescTextInputLayout;
    private EditText shopDescEditText;
    private TkpdHintTextInputLayout shopSloganTextInputLayout;
    private EditText shopSloganEditText;
    private View containerImagePicker;
    private ImageView imagePicker;
    private TextView welcomeText;
    private Button buttonNext;
    private TkpdProgressDialog tkpdProgressDialog;
    private String uriPathImage = "";
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    @Inject
    ShopOpenTracking trackingOpenShop;

    public static ShopOpenMandatoryInfoFragment createInstance() {
        return new ShopOpenMandatoryInfoFragment();
    }

    @Override
    protected void initInjector() {
        ShopSettingInfoComponent component = DaggerShopSettingInfoComponent
                .builder()
                .shopOpenDomainComponent(getComponent(ShopOpenDomainComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_info, container, false);
        presenter.attachView(this);
        initView(view);
        setActionVar();
        return view;
    }

    private void initView(View view) {
        shopDescTextInputLayout = view.findViewById(R.id.shop_desc_input_layout);
        shopDescEditText = (EditText) view.findViewById(R.id.shop_desc_input_text);
        shopSloganTextInputLayout = view.findViewById(R.id.shop_slogan_input_layout);
        shopSloganEditText = (EditText) view.findViewById(R.id.shop_slogan_input_text);
        containerImagePicker = view.findViewById(R.id.image_picker_container);
        imagePicker = (ImageView) view.findViewById(R.id.image_picker);
        buttonNext = (Button) view.findViewById(R.id.button_next);
        welcomeText = view.findViewById(R.id.welcome_shop_label);

        if (onShopStepperListener != null) {
            if (onShopStepperListener.getStepperModel().getResponseIsReserveDomain() == null) {
                presenter.getisReserveDomain();
            } else {
                if (onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData() != null) {
                    UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
                    updateView(userData);
                }
            }
        }
        trackingOpenShop.eventMoEngageOpenShop(INFO_TOKO);
    }

    private void updateView(UserData userData) {
        if (userData.getShopName() != null) {
            String helloName = getString(R.string.hello_x, userData.getShopName());
            welcomeText.setText(MethodChecker.fromHtml(helloName));
        }
        shopDescEditText.setText(userData.getShortDesc());
        shopSloganEditText.setText(userData.getTagLine());

        Drawable imgAddPhotoBox = AppCompatResources.getDrawable(getActivity(), R.drawable.ic_camera_add);
        Glide.with(imagePicker.getContext())
                .load(userData.getLogo())
                .dontAnimate()
                .placeholder(imgAddPhotoBox)
                .error(imgAddPhotoBox)
                .centerCrop()
                .into(imagePicker);
    }

    private void setActionVar() {
        containerImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_shop_picture),
                        new int[]{TYPE_GALLERY, TYPE_CAMERA}, com.tokopedia.imagepicker.picker.gallery.type.GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                        DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                        new ImagePickerEditorBuilder(
                                new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                                false,
                                null)
                        ,null);
                Intent intent = ImagePickerActivity.getIntent(getContext(), builder);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });
    }

    protected void onNextButtonClicked() {
        if (onShopStepperListener.getStepperModel().getResponseIsReserveDomain()!=null) {
            if (TextUtils.isEmpty(uriPathImage) && onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData() != null) {
                UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
                presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                        shopDescEditText.getText().toString(), userData.getLogo(),
                        userData.getServerId(), userData.getPhotoObj());
            } else {
                presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                        shopDescEditText.getText().toString(), "", "", "");
            }
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    @Override
    public void onSuccessSaveInfoShop(ShopOpenSaveInfoResponseModel responseModel) {
        if(onShopStepperListener != null && onShopStepperListener.getStepperModel().getResponseIsReserveDomain() != null){
            UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
            userData.setShortDesc(responseModel.getShopDesc());
            userData.setLogo(responseModel.getPicSrc());
            userData.setTagLine(responseModel.getShopTagLine());
        }
        trackingOpenShop.eventOpenShopFormSuccess();
        if (onShopStepperListener != null) {
            onShopStepperListener.goToNextPage(null);
        }
    }

    @Override
    public void onFailedSaveInfoShop(Throwable t) {
        String errorMessage = ShopErrorHandler.getErrorMessage(getActivity(), t);
        trackingOpenShop.eventOpenShopFormError(errorMessage);
        onErrorGetReserveDomain(errorMessage);
    }

    private void onErrorGetReserveDomain(String errorMessage){
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        if (onShopStepperListener != null) {
            onShopStepperListener.getStepperModel().setResponseIsReserveDomain(responseIsReserveDomain);
        }
        updateView(responseIsReserveDomain.getUserData());
    }

    @Override
    public void onErrorGetReserveDomain(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ShopErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        uriPathImage = imageUrlOrPathList.get(0);
                        ImageHandler.loadImageFromFile(getActivity(), imagePicker, new File(uriPathImage));
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }


    protected void onAttachListener(Context context) {
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
