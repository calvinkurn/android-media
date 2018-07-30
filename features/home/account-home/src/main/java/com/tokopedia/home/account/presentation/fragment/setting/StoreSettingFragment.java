package com.tokopedia.home.account.presentation.fragment.setting;

import android.support.v4.app.Fragment;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreSettingFragment extends BaseGeneralSettingFragment{
    private static final String TAG = StoreSettingFragment.class.getSimpleName();

    public static Fragment createInstance() {
        return new StoreSettingFragment();
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
                    if (getActivity().getApplication() instanceof AccountHomeRouter) {
                        router.goToManageShopEtalase(getActivity());
                    }
                    break;
                case SettingConstant.SETTING_SHOP_LOCATION_ID:
                    router.goToManageShopLocation(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_SHIPMENT_ID:
                    router.goToManageShopShipping(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_NOTE_ID:
                    router.goTotManageShopNotes(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_INFO_ID:
                    router.goToShopEditor(getActivity());
                    break;
                case SettingConstant.SETTING_SHOP_PRODUCT_ID:
                    router.goToManageShopProduct(getActivity());
                    break;
                default:
                    break;
            }
        }
    }
}
