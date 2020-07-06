package com.tokopedia.home.account.presentation.fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalMechant;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.home.account.AccountConstants.Analytics.ETALASE;
import static com.tokopedia.home.account.AccountConstants.Analytics.INFORMATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.LOCATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.NOTES;
import static com.tokopedia.home.account.AccountConstants.Analytics.PRODUCT;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHIPPING;

public class StoreSettingFragment extends BaseGeneralSettingFragment{

    private static final String TAG = StoreSettingFragment.class.getSimpleName();

    private AccountAnalytics accountAnalytics;

    public static Fragment createInstance() {
        return new StoreSettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_INFO_ID,
                getString(R.string.title_shop_info_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_ETALASE_ID,
                getString(R.string.title_shop_etalase_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_PRODUCT_ID,
                getString(R.string.title_shop_product_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_LOCATION_ID,
                getString(R.string.title_shop_location_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_SHIPMENT_ID,
                getString(R.string.title_shop_shipment_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_NOTE_ID,
                getString(R.string.title_shop_note_setting)));

        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            AccountHomeRouter router = (AccountHomeRouter) getActivity().getApplication();
            switch (settingId) {
                case SettingConstant.SETTING_SHOP_ETALASE_ID:
                    accountAnalytics.eventClickShopSetting(ETALASE);
                    RouteManager.route(getActivity(), ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST);
                    // RouteManager.route(getActivity(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE);
                    break;
                case SettingConstant.SETTING_SHOP_LOCATION_ID:
                    accountAnalytics.eventClickShopSetting(LOCATION);
                    RouteManager.route(getActivity(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS);
                    break;
                case SettingConstant.SETTING_SHOP_SHIPMENT_ID:
                    accountAnalytics.eventClickShopSetting(SHIPPING);
                    accountAnalytics.eventManageShopShipping();
                    RouteManager.route(getContext(), ApplinkConst.SELLER_SHIPPING_EDITOR);
                    break;
                case SettingConstant.SETTING_SHOP_NOTE_ID:
                    accountAnalytics.eventClickShopSetting(NOTES);
                    RouteManager.route(getContext(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES);
                    break;
                case SettingConstant.SETTING_SHOP_INFO_ID:
                    accountAnalytics.eventClickShopSetting(INFORMATION);
                    RouteManager.route(getContext(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO);
                    break;
                case SettingConstant.SETTING_SHOP_PRODUCT_ID:
                    accountAnalytics.eventClickShopSetting(PRODUCT);
                    Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.PRODUCT_MANAGE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
