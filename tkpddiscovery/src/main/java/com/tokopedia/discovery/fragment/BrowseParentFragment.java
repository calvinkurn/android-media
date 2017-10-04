package com.tokopedia.discovery.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.browseparent.BrowserSectionsPagerAdapter;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.browseparent.BrowseProductParent;
import com.tokopedia.discovery.presenter.browseparent.BrowseProductParentImpl;
import com.tokopedia.discovery.view.BrowseProductParentView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseParentFragment extends BaseFragment<BrowseProductParent> implements BrowseProductParentView {
    public static final String FRAGMENT_TAG = BrowseParentFragment.class.getSimpleName();
    public static final String SEARCH_QUERY = "SEARCH_QUERY";
    public static final String SORT_EXTRA = "SORT_EXTRA";
    public static final String SOURCE_EXTRA = "SOURCE_EXTRA";
    public static final String FILTER_EXTRA = "FILTER_EXTRA";
    public static final String POSITION_EXTRA = "POSITION_EXTRA";
    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.tabs)
    TabLayout tabLayout;
    @BindView(R2.id.discovery_ticker)
    TextView discoveryTicker;
    public static final String TAG = BrowseParentFragment.class.getSimpleName();
    @BindView(R2.id.tab_container)
    LinearLayout tabContainer;
    private String source;
    private String formatKey = "%d_%s";

    private BrowserSectionsPagerAdapter browserSectionsPagerAdapter;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public List<Breadcrumb> getProductBreadCrumb() {
        try {
            Fragment fragment = browserSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
            switch (viewPager.getCurrentItem()) {
                case 0:
                    if (fragment instanceof ProductFragment) {
                        return presenter.getBreadCrumb();
                    }
                case 1:
                    if (fragment instanceof CatalogFragment) {
                        BrowseCatalogModel catalogModel = ((CatalogFragment) fragment).getDataModel();
                        return catalogModel.result.breadcrumb;
                    }
                default:
                    return new ArrayList<Breadcrumb>();

            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean checkHasFilterAttrIsNull(int activeTab) {
        return ((BrowseView) getActivity()).checkHasFilterAttrIsNull(activeTab);
    }

    @Override
    public BrowseProductModel getDataForBrowseProduct() {
        return presenter.getDataForBrowseProduct();
    }

    @Override
    public NetworkParam.Product getProductParam() {
        return presenter.getProductParam();
    }

    @Override
    public void setDefaultGridTypeFromNetwork(Integer viewType) {
        ((BrowseView) getActivity()).setDefaultGridTypeFromNetwork(viewType);
    }

    @Override
    public void setOfficialStoreBanner(BannerOfficialStoreModel model) {
        for (int i=0; i< browserSectionsPagerAdapter.getCount(); i++) {
            if (browserSectionsPagerAdapter.getItem(i) instanceof ProductFragment) {
                ProductFragment productFragment = (ProductFragment) browserSectionsPagerAdapter.getItem(i);
                productFragment.showOfficialStoreBanner(model);
                break;
            }
        }
    }

    public static BrowseParentFragment newInstance(BrowseProductActivityModel browseProductActivityModel) {
        return newInstance(browseProductActivityModel, 0);
    }

    public static BrowseParentFragment newInstance(BrowseProductActivityModel browseProductActivityModel, int activeTab) {
        Bundle args = new Bundle();
        args.putParcelable(BROWSE_PRODUCT_ACTIVITY_MODEL, Parcels.wrap(browseProductActivityModel));
        args.putInt(POSITION_EXTRA, activeTab);
        BrowseParentFragment fragment = new BrowseParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void initDiscoveryTicker() {
        try {
            if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_SEARCH).equalsIgnoreCase("true")) {
                String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_SEARCH_TEXT);
                showTickerGTM(message);
            } else {
                showTickerGTM(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showTickerGTM(String message) {
        if (discoveryTicker != null) {
            if (message != null) {
                discoveryTicker.setText(MethodChecker.fromHtml(message));
                discoveryTicker.setVisibility(View.VISIBLE);
                discoveryTicker.setAutoLinkMask(0);
                Linkify.addLinks(discoveryTicker, Linkify.WEB_URLS);
            } else {
                discoveryTicker.setVisibility(View.GONE);
            }
        }
    }

    public Fragment getActiveFragment() {
        if (browserSectionsPagerAdapter != null) {
            return browserSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
        } else {
            return null;
        }
    }

    @Override
    public void initSectionAdapter(ArrayMap<String, String> visibleTab) {
        browserSectionsPagerAdapter = new BrowserSectionsPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(browserSectionsPagerAdapter);
        browserSectionsPagerAdapter.setSectionViewPager(visibleTab);
    }

    @Override
    public String getProductShareUrl() {
        String shareUrl = "";
        try {
            Fragment fragment = browserSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
            switch (viewPager.getCurrentItem()) {
                case 0:
                    //TODO Return Product Model for Catalog
                    if (fragment instanceof ProductFragment) {
                        shareUrl = ((ProductFragment) fragment).getDataModel().result.shareUrl;
                    }
                case 1:
                    //TODO Return Product Model for Product
                    if (fragment instanceof CatalogFragment) {
                        shareUrl = ((CatalogFragment) fragment).getDataModel().result.shareUrl;
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return shareUrl;
        }
    }

    @Override
    public void setLoadingProgress(boolean isLoading) {
        ((BrowseProductActivity) getActivity()).showLoading(isLoading);
    }

    @Override
    public void setNetworkStateError() {
        if (browserSectionsPagerAdapter == null) {
            ((BrowseProductActivity) getActivity()).showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.subscribe();
                    presenter.fetchFromNetwork(getActivity());
                }
            });
        }
    }

    @Override
    public void setDynamicFilterAtrribute(DataValue filterAtrribute, int activeTab) {
        Log.d(TAG, filterAtrribute.toString());
        if (filterAtrribute.getSort() != null) {
            filterAtrribute.setSelected(filterAtrribute.getSort().get(0).getName());
        }
        ((BrowseView) getActivity()).setFilterAttribute(filterAtrribute, activeTab);
    }

    @Override
    public void showTabLayout() {
        if(browserSectionsPagerAdapter != null && browserSectionsPagerAdapter.getCount()>1){
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void redirectUrl(BrowseProductModel productModel) {
        String uri = productModel.result.redirect_url;
        if (uri.contains("/hot/")) {
            if (getActivity() !=null && getActivity() instanceof BrowseProductActivity) {
                BrowseProductActivity browseProductActivity = (BrowseProductActivity) getActivity();
                browseProductActivity.resetBrowseProductActivityModel();
                BrowseProductActivityModel model = browseProductActivity.getBrowseProductActivityModel();
                Uri myurl = Uri.parse(uri);
                uri = myurl.getPathSegments().get(1);
                browseProductActivity.sendHotlist(uri, model.getQ());
            }
        }
        if (uri.contains("/p/")) {
            if (getActivity() !=null && getActivity() instanceof BrowseProductActivity) {
                BrowseProductActivity browseProductActivity = (BrowseProductActivity) getActivity();
                browseProductActivity.resetBrowseProductActivityModel();
                BrowseProductActivityModel model = browseProductActivity.getBrowseProductActivityModel();
                model.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
                model.setDepartmentId(productModel.result.departmentId);
                ((BrowseProductActivity) getActivity()).setFragment(BrowseParentFragment.newInstance(model), BrowseParentFragment.FRAGMENT_TAG);
            }
        }
        if (uri.contains("/catalog/")) {
            URLParser urlParser = new URLParser(uri);
            getActivity().startActivity(DetailProductRouter.getCatalogDetailActivity(getActivity(),
                    urlParser.getHotAlias()));
            getActivity().finish();
        }
    }

    @Override
    public void setupWithTabViewPager() {
        ((BrowseProductActivity) getActivity()).removeEmptyState();
        setupWithViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setupWithViewPager() {
        Log.d(TAG, "setupWithViewPager source " + source);
        /**
         * For called first time
         */
        sendTabClickGTM();
        tabLayout.setVisibility(View.GONE);
        tabContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        BrowseProductActivity productActivity = (BrowseProductActivity) getActivity();
        productActivity.changeBottomBar(source);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, MESSAGE_TAG + " >> position >> " + position);
                if (getActivity() !=null && getActivity() instanceof BrowseProductActivity) {

                }

                fetchData(position);
                sendTabClickGTM();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setCurrentTabs(final int pos) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (viewPager != null) {
                    viewPager.setCurrentItem(pos);
                }
            }
        });
    }

    private void fetchData(int position) {
        Fragment fragment = (Fragment) browserSectionsPagerAdapter.instantiateItem(viewPager, position);
        /**
         * hit fragment browse shop tab at page selected for the first time
         */
        if (fragment != null && fragment instanceof ShopFragment) {
            ((ShopFragment) fragment).onCallNetwork();
            if (source.startsWith("search")) {
                source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP;
            }
        }

        /**
         * hit fragment browse shop tab at page selected for the first time
         */
        if (fragment != null && fragment instanceof CatalogFragment) {
            ((CatalogFragment) fragment).onCallNetwork();
            if (source.startsWith("search")) {
                source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG;
            }
        }

        if (fragment != null && fragment instanceof ProductFragment) {
            ((ProductFragment) fragment).onCallNetwork();
            if (source.startsWith("search")) {
                source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
            }
        }
        Log.d(TAG, "source " + source);
        BrowseProductActivity productActivity = (BrowseProductActivity) getActivity();
        productActivity.getBrowseProductActivityModel().setActiveTab(position);
        productActivity.changeBottomBar(source);
    }

    public int getActiveTab() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initPresenter() {
        presenter = new BrowseProductParentImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_parent;
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        CommonUtils.UniversalToast(getActivity(), (String) data[0]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void sendTabClickGTM() {
        switch (source) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
                UnifyTracking.eventDiscoverySearchCatalog();
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                UnifyTracking.eventDiscoverySearchShop();
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
                UnifyTracking.eventDiscoverySearchCatalog();
                break;
        }
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }
}
