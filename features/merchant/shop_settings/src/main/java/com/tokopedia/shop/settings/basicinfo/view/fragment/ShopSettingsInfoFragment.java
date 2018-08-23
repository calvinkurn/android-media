package com.tokopedia.shop.settings.basicinfo.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.router.ShopSettingRouter;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity;
import com.tokopedia.shop.settings.basicinfo.view.presenter.ShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.common.util.ShopDateUtil;

import javax.inject.Inject;

public class ShopSettingsInfoFragment extends BaseDaggerFragment implements ShopSettingsInfoPresenter.View {

    private static final int REQUEST_EDIT_BASIC_INFO = 781;
    private static final int REQUEST_EDIT_SCHEDULE = 782;
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
    private boolean needReload;
    private View vgShopInfoContainer;
    private ShopBasicDataModel shopBasicDataModel;
    private ImageView ivShopLogo;

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
        vgShopInfoContainer = view.findViewById(R.id.vgShopInfoContainer);
        ivShopLogo = view.findViewById(R.id.ivShopLogo);

        vgShopInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShopEditBasicInfoActivity.createIntent(getContext(), shopBasicDataModel);
                startActivityForResult(intent, REQUEST_EDIT_BASIC_INFO);
            }
        });

        lvShopStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShopEditScheduleActivity.createIntent(getContext(), shopBasicDataModel);
                startActivityForResult(intent, REQUEST_EDIT_SCHEDULE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_EDIT_BASIC_INFO:
            case REQUEST_EDIT_SCHEDULE:
                if (resultCode == Activity.RESULT_OK) {
                    needReload = true;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReload) {
            loadShopBasicData();
            needReload = false;
        }
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
        this.shopBasicDataModel = shopBasicDataModel;
        hideLoading();
        setUIShopBasicData(shopBasicDataModel);
        setUIStatus(shopBasicDataModel);
        setUIMembership(shopBasicDataModel);
    }

    private void setUIShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        tvShopName.setText(MethodChecker.fromHtml(shopBasicDataModel.getName()));
        tvShopDomain.setText(shopBasicDataModel.getDomain());
        tvShopSlogan.setText(shopBasicDataModel.getTagline());
        tvShopDescription.setText(shopBasicDataModel.getDescription());
        String logoUrl = shopBasicDataModel.getLogo();
        if (TextUtils.isEmpty(logoUrl)) {
            ivShopLogo.setImageResource(R.drawable.ic_default_shop_ava);
        } else {
            ImageHandler.LoadImage(ivShopLogo, logoUrl);
        }
    }

    private void setUIStatus(ShopBasicDataModel shopBasicDataModel) {
        if (shopBasicDataModel.isOpen()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getString(R.string.label_open));

            String closeScheduleUnixString = shopBasicDataModel.getCloseSchedule();
            if (!TextUtils.isEmpty(closeScheduleUnixString)) {
                String closeString = ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE, closeScheduleUnixString);
                stringBuilder.append(", ");
                stringBuilder.append(getString(R.string.closed_schedule, closeString));
            }

            String openScheduleUnixString = shopBasicDataModel.getOpenSchedule();
            if (!TextUtils.isEmpty(openScheduleUnixString)) {
                String openString = ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE, openScheduleUnixString);
                stringBuilder.append(" - ");
                stringBuilder.append(openString);
            }
            lvShopStatus.setSubTitle(stringBuilder.toString());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getString(R.string.label_close));
            String openScheduleUnixString = shopBasicDataModel.getOpenSchedule();
            if (!TextUtils.isEmpty(openScheduleUnixString)) {
                String openString = ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE_TIME, openScheduleUnixString);
                stringBuilder.append(", ");
                stringBuilder.append(getString(R.string.reopen_at, openString));
            }
            lvShopStatus.setSubTitle(stringBuilder.toString());
        }
    }

    private void setUIMembership(ShopBasicDataModel shopBasicDataModel) {
        if (shopBasicDataModel.isRegular()) {
            ivShopMembership.setImageResource(R.drawable.ic_badge_shop_regular);
            ivShopMembership.setPadding(0, 0, 0, 0);
            tvMembershipName.setText(getString(R.string.label_regular_merchant));

            String goldMerchantInviteString = getString(R.string.shop_settings_gold_merchant_invite);
            String goldMerchantString = getString(R.string.label_gold_merchant);
            Spannable spannable = new SpannableString(goldMerchantInviteString);
            int indexStart = goldMerchantInviteString.indexOf(goldMerchantString);
            int indexEnd = indexStart + goldMerchantString.length();

            int color = ContextCompat.getColor(getContext(), R.color.tkpd_main_green);
            spannable.setSpan(new ForegroundColorSpan(color), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    navigateToAboutGM();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(color);
                }
            };
            spannable.setSpan(clickableSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMembershipDescription.setMovementMethod(LinkMovementMethod.getInstance());
            tvMembershipDescription.setText(spannable);

        } else if (shopBasicDataModel.isOfficialStore()) {
            ivShopMembership.setImageResource(R.drawable.ic_badge_shop_official);
            int padding = getResources().getDimensionPixelOffset(R.dimen.dp_8);
            ivShopMembership.setPadding(padding, padding, padding, padding);
            tvMembershipName.setText(getString(R.string.label_official_store));
            tvMembershipDescription.setText(getString(R.string.valid_until_x,
                    ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE, shopBasicDataModel.getExpired())));
        } else if (shopBasicDataModel.isGold()) {
            ivShopMembership.setImageResource(R.drawable.ic_badge_shop_gm);
            ivShopMembership.setPadding(0, 0, 0, 0);
            tvMembershipName.setText(getString(R.string.label_gold_merchant));
            tvMembershipDescription.setText(getString(R.string.valid_until_x,
                    ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE, shopBasicDataModel.getExpired())));
            //TODO nvaigate to GM?
        }
    }

    private void navigateToAboutGM() {
        ((ShopSettingRouter) getActivity().getApplication()).goToMerchantRedirect(getActivity());
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
