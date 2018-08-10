package com.tokopedia.shop.settings.basicinfo.view.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.ShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;

import javax.inject.Inject;

public class ShopSettingsInfoFragment extends BaseDaggerFragment implements ShopSettingsInfoPresenter.View {

    @Inject
    ShopSettingsInfoPresenter shopSettingsInfoPresenter;
    private View loadingView;
    private View scrollViewContent;
    private TextView tvShopName;
    private TextView tvShopDomain;
    private TextView tvShopSlogan;
    private TextView tvShopDescription;
    private LabelView lvShopStatus;
    private ImageView ivShopMembership;
    private TextView tvMembershipName;
    private TextView tvMembershipDescription;
    private View vgShopSettingsBasic;

    public static ShopSettingsInfoFragment newInstance() {
        return new ShopSettingsInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_settings_info, container, false);
        loadingView = view.findViewById(R.id.loadingView);
        scrollViewContent = view.findViewById(R.id.scrollViewContent);
        tvShopName = view.findViewById(R.id.tvShopName);
        tvShopDomain = view.findViewById(R.id.tvShopDomain);
        tvShopSlogan = view.findViewById(R.id.tvShopSlogan);
        tvShopDescription = view.findViewById(R.id.tvShopDescription);
        lvShopStatus = view.findViewById(R.id.lvShopStatus);
        ivShopMembership = view.findViewById(R.id.ivShopMembership);
        tvMembershipName = view.findViewById(R.id.tvMembershipName);
        tvMembershipDescription = view.findViewById(R.id.tvMembershipDescription);

        vgShopSettingsBasic = view.findViewById(R.id.vgShopSettingsBasic);
        vgShopSettingsBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "cahnge basic info", Toast.LENGTH_SHORT).show();
            }
        });

        lvShopStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "status cahnge", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void showLoading() {
        scrollViewContent.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        scrollViewContent.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadShopBasicData();
    }

    private void loadShopBasicData() {
        showLoading();
        shopSettingsInfoPresenter.getShopBasicData();
    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        shopSettingsInfoPresenter.attachView(this);
    }

    @Override
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        hideLoading();
        setUIShopBasicData(shopBasicDataModel);
        setUIStatus(shopBasicDataModel);
        setUIMembership(shopBasicDataModel);
    }

    private void setUIShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        tvShopName.setText(shopBasicDataModel.getName());
        tvShopDomain.setText(shopBasicDataModel.getDomain());
        tvShopSlogan.setText(shopBasicDataModel.getTagline());
        tvShopDescription.setText(shopBasicDataModel.getDescription());
    }

    private void setUIStatus(ShopBasicDataModel shopBasicDataModel) {
        if (shopBasicDataModel.isOpen()) {
            lvShopStatus.setSubTitle(getString(R.string.label_open));
        } else {
            lvShopStatus.setSubTitle(getString(R.string.label_close));
        }
    }

    private void setUIMembership(ShopBasicDataModel shopBasicDataModel) {
        if (shopBasicDataModel.isRegular()) {
            ivShopMembership.setImageResource(R.drawable.ic_badge_shop_gm);
            ivShopMembership.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorGray), PorterDuff.Mode.SRC_IN);
            ivShopMembership.setPadding(0, 0, 0, 0);
            tvMembershipName.setText(getString(R.string.label_regular_merchant));
            tvMembershipDescription.setText(getString(R.string.shop_settings_gold_merchant_invite));
        } else {
            if (shopBasicDataModel.isOfficialStore()) {
                ivShopMembership.setImageResource(R.drawable.ic_badge_shop_official);
                int padding = getResources().getDimensionPixelOffset(R.dimen.dp_8);
                ivShopMembership.setPadding(padding, padding, padding, padding);
                tvMembershipName.setText(getString(R.string.label_official_store));
                tvMembershipDescription.setText(getString(R.string.valid_until_x, shopBasicDataModel.getExpired()));
            } else if (shopBasicDataModel.isGold()) {
                ivShopMembership.setImageResource(R.drawable.ic_badge_shop_gm);
                ivShopMembership.setPadding(0, 0, 0, 0);
                tvMembershipName.setText(getString(R.string.label_gold_merchant));
                tvMembershipDescription.setText(getString(R.string.valid_until_x, shopBasicDataModel.getExpired()));
            }
            ivShopMembership.clearColorFilter();
        }
    }


    @Override
    public void onErrorGetShopBasicData(Throwable throwable) {
        hideLoading();
        String message = ErrorHandler.getErrorMessage(getContext(), throwable);
        NetworkErrorHelper.showEmptyState(getContext(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadShopBasicData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopSettingsInfoPresenter != null) {
            shopSettingsInfoPresenter.detachView();
        }
    }

}
