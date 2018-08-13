package com.tokopedia.shop.settings.basicinfo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.ShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;

import javax.inject.Inject;

public class ShopEditBasicInfoFragment extends BaseDaggerFragment implements UpdateShopSettingsInfoPresenter.View {

    public static final String SAVED_IMAGE_PATH = "saved_img_path";

    @Inject
    UpdateShopSettingsInfoPresenter updateShopSettingsInfoPresenter;

    private OnShopEditBasicInfoFragmentListener onShopEditBasicInfoFragmentListener;
    private ImageView ivLogo;
    private TextView tvBrowseFile;
    private TkpdHintTextInputLayout tilShopSlogan;
    private EditText etShopSlogan;
    private TkpdHintTextInputLayout tilShopDesc;
    private EditText etShopDesc;

    public interface OnShopEditBasicInfoFragmentListener {
        void onDataLoaded();
    }

    public static ShopEditBasicInfoFragment newInstance() {
        return new ShopEditBasicInfoFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(getContext());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_settings_edit_basic_info, container, false);
        ivLogo = view.findViewById(R.id.ivLogo);
        tvBrowseFile = view.findViewById(R.id.tvBrowseFile);
        tilShopSlogan = view.findViewById(R.id.tilShopSlogan);
        etShopSlogan = view.findViewById(R.id.etShopSlogan);
        tilShopDesc = view.findViewById(R.id.tilShopDesc);
        etShopDesc = view.findViewById(R.id.etShopDesc);
        view.requestFocus();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadShopBasicData();
    }

    private void loadShopBasicData() {
        updateShopSettingsInfoPresenter.getShopBasicData();
    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        updateShopSettingsInfoPresenter.attachView(this);
    }

    @Override
    public void onSuccessUpdateShopBasicData(String successMessage) {

    }

    @Override
    public void onErrorUpdateShopBasicData(Throwable throwable) {

    }

    @Override
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        setUIShopBasicData(shopBasicDataModel);
        onShopEditBasicInfoFragmentListener.onDataLoaded();
    }

    private void setUIShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        String logoUrl = shopBasicDataModel.getLogo();
        if (TextUtils.isEmpty(logoUrl)) {
            ivLogo.setImageResource(R.drawable.ic_camera_add);
        } else {
            ImageHandler.loadImageAndCache(ivLogo, logoUrl);
        }
        etShopSlogan.setText(MethodChecker.fromHtml(shopBasicDataModel.getTagline()));
        etShopSlogan.setSelection(etShopSlogan.getText().length());

        etShopDesc.setText(MethodChecker.fromHtml(shopBasicDataModel.getDescription()));
        etShopDesc.setSelection(etShopDesc.getText().length());
    }

    @SuppressLint("Range")
    @Override
    public void onErrorGetShopBasicData(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getContext(), throwable);
        SnackbarManager.makeRed(getView(), message, Snackbar.LENGTH_LONG).setAction(
                getView().getContext().getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadShopBasicData();
                    }
                }
        ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateShopSettingsInfoPresenter != null) {
            updateShopSettingsInfoPresenter.detachView();
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onShopEditBasicInfoFragmentListener = (OnShopEditBasicInfoFragmentListener) context;
    }
}
