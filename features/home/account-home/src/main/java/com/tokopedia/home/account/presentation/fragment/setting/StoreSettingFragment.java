package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

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
                    if (getActivity().getApplication() instanceof AccountHomeRouter) {
                        router.goToManageShopEtalase(getActivity());
                    }
                    break;
                case SettingConstant.SETTING_SHOP_LOCATION_ID:
                    accountAnalytics.eventClickShopSetting(LOCATION);
                    router.goToManageShopLocation(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_SHIPMENT_ID:
                    accountAnalytics.eventClickShopSetting(SHIPPING);
                    router.goToManageShopShipping(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_NOTE_ID:
                    accountAnalytics.eventClickShopSetting(NOTES);
                    router.goTotManageShopNotes(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_INFO_ID:
                    accountAnalytics.eventClickShopSetting(INFORMATION);
                    router.goToShopEditor(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_PRODUCT_ID:
                    accountAnalytics.eventClickShopSetting(PRODUCT);
                    router.goToManageShopProduct(getActivity());
                    break;
                default:
                    break;
            }
        }
    }
}
