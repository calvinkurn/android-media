package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.utils.CurrentLocationCallBack;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;

import java.util.Map;

public class CategoryDetailActivity extends DealsBaseActivity implements SelectLocationBottomSheet.SelectedLocationListener, CurrentLocationCallBack {

    private final String BRAND_FRAGMENT = "BRAND_FRAGMENT";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORIES_DATA = "CATEGORIES_DATA";
    public static final String FROM_HOME = "FROM_HOME";
    private String categoryName;
    private boolean isLocationUpdated;
    private CategoryDetailHomeFragment categoryDetailHomeFragment;
    private PermissionCheckerHelper permissionCheckerHelper;

    public Bundle getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {

        Location location = Utils.getSingletonInstance().getLocation(context);
        String searchName = extras.getString("search_name");
        CategoriesModel categoriesModel = new CategoriesModel();
        String categoryId = extras.getString("category_id");
        if (!TextUtils.isEmpty(categoryId)) {
            categoriesModel.setCategoryId(Integer.parseInt(categoryId));
        }
        categoriesModel.setTitle(extras.getString("search_name"));
        String categoryUrl = searchName + "?" + Utils.QUERY_PARAM_CITY_ID + "=" + location.getId();
        categoriesModel.setCategoryUrl(DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_CATEGORY + categoryUrl);

        if (TextUtils.isEmpty(categoriesModel.getTitle())) {
            if (!TextUtils.isEmpty(searchName)) {
                if (searchName.length() > 1)
                    searchName = searchName.substring(0, 1).toUpperCase() + searchName.substring(1);
                else
                    searchName = searchName.toUpperCase();
                categoriesModel.setTitle(searchName);
            }
        }
        extras.putString(CATEGORY_NAME, categoriesModel.getTitle());
        extras.putParcelable(CATEGORIES_DATA, categoriesModel);
        return extras;
    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        categoryName = getIntent().getStringExtra(CATEGORY_NAME);
        permissionCheckerHelper = new PermissionCheckerHelper();
        if (TextUtils.isEmpty(categoryName))
            categoryName = getString(com.tokopedia.digital_deals.R.string.text_deals);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Uri uri = getIntent().getData();
            if (uri != null) {
                Map<String, Object> params = UriUtil.destructureUriToMap(ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY, uri, true);
                for (String key : params.keySet()) {
                    extras.putString(key, (String) params.get(key));
                }
                getInstanceIntentAppLinkBackToHome(this, extras);
                checkForCurrentLocation();
            }
            categoryDetailHomeFragment = CategoryDetailHomeFragment.createInstance(extras, isLocationUpdated);
            return categoryDetailHomeFragment;
        } else {
            return null;
        }
    }

    @Override
    public void onLocationItemUpdated(boolean isLocationUpdated) {
        this.isLocationUpdated = isLocationUpdated;
        categoryDetailHomeFragment.refreshPage(isLocationUpdated);
    }

    @Override
    public void setDefaultLocationOnHomePage() {
        categoryDetailHomeFragment.setDefaultLocation();
    }

    @Override
    public void setCurrentLocation(DeviceLocation deviceLocation) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(this, TkpdCache.DEALS_LOCATION);
        localCacheHandler.putString(Utils.KEY_LOCATION_LAT, String.valueOf(deviceLocation.getLatitude()));
        localCacheHandler.putString(Utils.KEY_LOCATION_LONG, String.valueOf(deviceLocation.getLongitude()));
        localCacheHandler.applyEditor();
        categoryDetailHomeFragment.setCurrentCoordinates();
    }


    private void checkForCurrentLocation() {
        permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(String permissionText) {
                setDefaultLocationOnHomePage();
            }

            @Override
            public void onNeverAskAgain(String permissionText) {
            }

            @Override
            public void onPermissionGranted() {
                Utils.getSingletonInstance().detectAndSendLocation(CategoryDetailActivity.this, permissionCheckerHelper, CategoryDetailActivity.this);
            }
        }, getResources().getString(com.tokopedia.digital_deals.R.string.deals_use_current_location));
    }


}
