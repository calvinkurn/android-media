package com.tokopedia.core.discovery.fragment.browseparent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.Html;
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
import com.tokopedia.core.discovery.activity.BrowseProductActivity;
import com.tokopedia.core.discovery.adapter.browseparent.BrowserSectionsPagerAdapter;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.BrowseProductActivityModel;
import com.tokopedia.core.discovery.model.BrowseProductModel;
import com.tokopedia.core.discovery.model.NetworkParam;
import com.tokopedia.core.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.core.discovery.presenter.browseparent.BrowseProductParent;
import com.tokopedia.core.discovery.presenter.browseparent.BrowseProductParentImpl;
import com.tokopedia.core.discovery.view.BrowseProductParentView;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.DiscoveryRouter;
import com.tokopedia.core.session.base.BaseFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
    @Bind(R2.id.pager)
    ViewPager viewPager;
    @Bind(R2.id.tabs)
    TabLayout tabLayout;
    @Bind(R2.id.discovery_ticker)
    TextView discoveryTicker;
    public static final String TAG = BrowseParentFragment.class.getSimpleName();
    @Bind(R2.id.tab_container)
    LinearLayout tabContainer;
    private String source;
    private String formatKey = "%d_%s";
    public DiscoveryActivityPresenter discoveryActivityPresenter = new DiscoveryActivityPresenter.DiscoveryActivityPresenterImpl() {
        @Override
        public BrowseProductActivityModel getBrowseProductActivityModel() {
            FragmentActivity activity = BrowseParentFragment.this.getActivity();
            if (activity != null && activity instanceof DiscoveryActivityPresenter) {
                return ((DiscoveryActivityPresenter) activity).getBrowseProductActivityModel();
            }
            return null;
        }

        @Override
        public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
            return presenter.getDataForBrowseProduct(firstTimeOnly);
        }

        @Override
        public NetworkParam.Product getProductParam() {
            return presenter.getProductParam();
        }

        @Override
        public List<Breadcrumb> getProductBreadCrumb() {
            try {
                Fragment fragment = mSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        if (fragment instanceof ProductFragment) {
                            return presenter.getBreadCrumb();
                        }
                    case 1:
                        if (fragment instanceof CatalogFragment) {
                            return ((CatalogFragment) fragment).getDataModel().result.breadcrumb;
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
            return ((BrowseProductActivity) getActivity()).checkHasFilterAttrIsNull(activeTab);
        }
    };
    //    @Bind(R2.id.progressBar)
//    ProgressBar progressBar;
    private BrowserSectionsPagerAdapter mSectionsPagerAdapter;

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
                discoveryTicker.setText(Html.fromHtml(message));
                discoveryTicker.setVisibility(View.VISIBLE);
                discoveryTicker.setAutoLinkMask(0);
                Linkify.addLinks(discoveryTicker, Linkify.WEB_URLS);
            } else {
                discoveryTicker.setVisibility(View.GONE);
            }
        }
    }

    public Fragment getActiveFragment() {
        if (mSectionsPagerAdapter != null) {
            return mSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
        } else {
            return null;
        }
    }

    @Override
    public void initSectionAdapter(ArrayMap<String, String> visibleTab) {
        mSectionsPagerAdapter = new BrowserSectionsPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.setSectionViewPager(visibleTab);
    }

    @Override
    public String getProductShareUrl() {
        String shareUrl = "";
        try {
            Fragment fragment = mSectionsPagerAdapter.getItem(viewPager.getCurrentItem());
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
        if (mSectionsPagerAdapter == null) {
            ((BrowseProductActivity) getActivity()).showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.subscribe();
                    presenter.fetchRotationData(null);
                    presenter.initData(getActivity());
                    presenter.fetchFromNetwork(getActivity());
                }
            });
        }
    }

    @Override
    public void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab) {
        Log.d(TAG, filterAtrribute.toString());
        if (filterAtrribute.getSort() != null) {
            filterAtrribute.setSelected(filterAtrribute.getSort().get(0).getName());
        }
        ((BrowseProductActivity) getActivity()).setFilterAttribute(filterAtrribute, activeTab);
    }

    @Override
    public void redirectUrl(BrowseProductModel productModel) {
        String uri = productModel.result.redirect_url;
        if (uri.contains("/hot/")) {
            Uri myurl = Uri.parse(uri);
            uri = myurl.getPathSegments().get(1);
            ((BrowseProductActivity) getActivity()).sendHotlist(uri);
        }
        if (uri.contains("/p/")) {
            BrowseProductActivity browseProductActivity = (BrowseProductActivity) getActivity();
            BrowseProductActivityModel model = browseProductActivity.getBrowseProductActivityModel();
            model.setSource(DynamicFilterPresenter.DIRECTORY);
            model.setDepartmentId(productModel.result.departmentId);
            ((BrowseProductActivity) getActivity()).setFragment(BrowseParentFragment.newInstance(model), BrowseParentFragment.FRAGMENT_TAG);
        }
        if (uri.contains("/catalog/")) {
            URLParser urlParser = new URLParser(uri);
//            Intent intent = new Intent(getActivity(), Catalog.class);
//            intent.putExtra(HotList.CATALOG_ID_KEY, urlParser.getHotAlias());
//            getActivity().startActivity(intent);
            getActivity().startActivity(DiscoveryRouter.getCatalogDetailActivity(getActivity(),
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
                ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel().setActiveTab(position);
                ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel().setSource(source);
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
        Fragment fragment = (Fragment) mSectionsPagerAdapter.instantiateItem(viewPager, position);
        /**
         * hit fragment browse shop tab at page selected for the first time
         */
        if (fragment != null && fragment instanceof ShopFragment) {
            ((ShopFragment) fragment).onCallNetwork();
            if (source.startsWith("search")) {
                source = DynamicFilterPresenter.SEARCH_SHOP;
            }
        }

        /**
         * hit fragment browse shop tab at page selected for the first time
         */
        if (fragment != null && fragment instanceof CatalogFragment) {
            ((CatalogFragment) fragment).onCallNetwork();
            if (source.startsWith("search")) {
                source = DynamicFilterPresenter.SEARCH_CATALOG;
            }
        }

        if (fragment != null && fragment instanceof ProductFragment) {
            if (source.startsWith("search")) {
                source = DynamicFilterPresenter.SEARCH_PRODUCT;
            }
        }
        Log.d(TAG, "source " + source);
        BrowseProductActivity productActivity = (BrowseProductActivity) getActivity();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void sendTabClickGTM() {
        switch (source) {
            case DynamicFilterPresenter.SEARCH_SHOP:
                UnifyTracking.eventDiscoverySearchShop();
                break;
            case DynamicFilterPresenter.SEARCH_CATALOG:
                UnifyTracking.eventDiscoverySearchCatalog();
                break;
        }
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public DiscoveryActivityPresenter getActivityPresenter() {
        return discoveryActivityPresenter;
    }
}
