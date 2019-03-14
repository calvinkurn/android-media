package com.tokopedia.product.manage.list.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseRetryDataBinder;
import com.tokopedia.base.list.seller.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.base.list.seller.view.fragment.BaseSearchListFragment;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.ViewUtils;
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder;
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity;
import com.tokopedia.product.manage.item.main.duplicate.activity.ProductDuplicateActivity;
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.constant.CashbackOption;
import com.tokopedia.product.manage.list.constant.StatusProductOption;
import com.tokopedia.product.manage.list.di.DaggerProductManageComponent;
import com.tokopedia.product.manage.list.di.ProductManageModule;
import com.tokopedia.product.manage.list.view.activity.ProductManageFilterActivity;
import com.tokopedia.product.manage.list.view.activity.ProductManageSortActivity;
import com.tokopedia.product.manage.list.view.adapter.ProductManageListAdapter;
import com.tokopedia.product.manage.list.view.listener.ProductManageView;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenter;
import com.tokopedia.product.share.ProductData;
import com.tokopedia.product.share.ProductShare;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.data.model.FreeDeposit;
import com.tokopedia.topads.freeclaim.data.constant.TopAdsFreeClaimConstantKt;
import com.tokopedia.topads.freeclaim.view.widget.TopAdsWidgetFreeClaim;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageFragment extends BaseSearchListFragment<ProductManagePresenter, ProductManageViewModel>
        implements ProductManageView, ProductManageListAdapter.ClickOptionCallback, BaseMultipleCheckListAdapter.CheckedCallback<ProductManageViewModel> {

    public static final String ERROR_CODE_LIMIT_CASHBACK = "422";
    public static final int REQUEST_CODE_ADD_IMAGE = 3859;
    public static final int INSTAGRAM_SELECT_REQUEST_CODE = 3860;

    @Inject
    ProductManagePresenter productManagePresenter;
    private BottomActionView bottomActionView;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    private TopAdsWidgetFreeClaim topAdsWidgetFreeClaim;

    private boolean hasNextPage;
    private boolean filtered;
    @SortProductOption
    private String sortProductOption;
    private ProductManageFilterModel productManageFilterModel;
    private ActionMode actionMode;
    private Boolean goldMerchant;
    private boolean isOfficialStore;
    private String shopDomain;
    private UserSessionInterface userSession;

    private BroadcastReceiver addProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TkpdState.ProductService.BROADCAST_ADD_PRODUCT) &&
                    intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                    intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetPageAndRefresh();
                    }
                });

            }
        }
    };

    @Override
    protected void initInjector() {
        GraphqlClient.init(getContext());
        super.initInjector();
        DaggerProductManageComponent.builder()
                .productManageModule(new ProductManageModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productManagePresenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_manage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        searchInputView.clearFocus();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        bottomActionView = (BottomActionView) view.findViewById(R.id.bottom_action_view);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductManageSortActivity.createIntent(getActivity(), sortProductOption);
                startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_SORT);
            }
        });
        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductManageFilterActivity.createIntent(getActivity(), productManageFilterModel);
                startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_FILTER);
            }
        });
        topAdsWidgetFreeClaim = view.findViewById(R.id.topads_free_claim_widget);
    }

    @Override
    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_variant_empty);
        emptyDataBinder.setEmptyTitleText(getString(R.string.title_no_result));
        emptyDataBinder.setEmptyContentText(getString(R.string.product_manage_label_change_search));
        return emptyDataBinder;
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_featured_product);
        emptyDataBinder.setEmptyTitleText(getString(R.string.product_manage_label_product_list_empty));
        emptyDataBinder.setEmptyContentText(getString(R.string.pml_product_manage_label_add_product_to_sell));
        emptyDataBinder.setEmptyButtonItemText(getString(R.string.pml_product_manage_label_add_product));
        emptyDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                // do nothing
            }

            @Override
            public void onEmptyButtonClicked() {
                startActivity(new Intent(getActivity(), ProductAddNameCategoryActivity.class));
            }
        });
        return emptyDataBinder;
    }

    @Override
    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new BaseRetryDataBinder(adapter, R.drawable.ic_cloud_error);
    }

    @Override
    public void onSearchSubmitted(String text) {
        UnifyTracking.eventProductManageSearch(getActivity());
        super.onSearchSubmitted(text);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        sortProductOption = SortProductOption.POSITION;
        productManageFilterModel = new ProductManageFilterModel();
        productManageFilterModel.reset();
        hasNextPage = false;

        userSession = new UserSession(getActivity());
        productManagePresenter.getFreeClaim(GraphqlHelper.loadRawString(getResources(), R.raw.gql_get_deposit), userSession.getShopId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (GlobalConfig.isCustomerApp()) {
            inflater.inflate(R.menu.menu_product_manage_dark, menu);
        } else {
            inflater.inflate(R.menu.menu_product_manage, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_product_menu) {
            item.getSubMenu().findItem(R.id.label_view_add_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(ProductAddNameCategoryActivity.Companion.createInstance(getActivity()));
                    UnifyTracking.eventProductManageTopNav(getActivity(), item.getTitle().toString());
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_import_from_instagram).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(getContext());
                    startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE);
                    UnifyTracking.eventProductManageTopNav(getActivity(), item.getTitle().toString());
                    return false;
                }
            });
        } else if (itemId == R.id.checklist_product_menu) {
            UnifyTracking.eventProductManageTopNav(getActivity(), item.getTitle().toString());
            getActivity().startActionMode(getCallbackActionMode());
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    protected ActionMode.Callback getCallbackActionMode() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(String.valueOf(((ProductManageListAdapter) adapter).getTotalChecked()));
                actionMode = mode;
                if (GlobalConfig.isCustomerApp()) {
                    getActivity().getMenuInflater().inflate(R.menu.menu_product_manage_action_mode_dark, menu);
                } else {
                    getActivity().getMenuInflater().inflate(R.menu.menu_product_manage_action_mode, menu);
                }
                setAdapterActionMode(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete_product_menu) {
                    final List<String> productIdList = ((ProductManageListAdapter) adapter).getListChecked();
                    showDialogActionDeleteProduct(productIdList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mode.finish();
                            productManagePresenter.deleteProduct(productIdList);
                        }
                    }, null);
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((ProductManageListAdapter) adapter).resetCheckedItemSet();
                setAdapterActionMode(false);
                actionMode = null;
            }
        };
    }

    protected void setAdapterActionMode(boolean isActionMode) {
        ((ProductManageListAdapter) adapter).setActionMode(isActionMode);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case INSTAGRAM_SELECT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK &&
                        intent != null) {
                    ArrayList<String> imageUrls = intent.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    ArrayList<String> imageDescList = intent.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST);
                    if (imageUrls != null && imageUrls.size() > 0) {
                        ProductDraftListActivity.startInstagramSaveBulkFromLocal(getContext(), imageUrls, imageDescList);
                    }
                }
                break;
            case ProductManageConstant.REQUEST_CODE_FILTER:
                if (resultCode == Activity.RESULT_OK) {
                    productManageFilterModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_FILTER_SELECTED);
                    resetPageAndRefresh();
                    filtered = true;
                    setSearchMode(true);
                    trackingFilter(productManageFilterModel);
                }
                break;
            case ProductManageConstant.REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    ProductManageSortModel productManageSortModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_SORT_SELECTED);
                    sortProductOption = productManageSortModel.getId();
                    resetPageAndRefresh();
                    trackingSort(productManageSortModel);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, intent);
                break;
        }
    }

    private void trackingSort(ProductManageSortModel productManageSortModel) {
        UnifyTracking.eventProductManageSortProduct(getActivity(), productManageSortModel.getTitleSort());
    }

    private void trackingFilter(ProductManageFilterModel productManageFilterModel) {
        List<String> filters = new ArrayList<>();
        if (!productManageFilterModel.getCategoryId().equals(String.valueOf(ProductManageConstant.FILTER_ALL_CATEGORY))) {
            filters.add(AppEventTracking.EventLabel.CATEGORY);
        }

        if (productManageFilterModel.getEtalaseProductOption() != ProductManageConstant.FILTER_ALL_PRODUK) {
            filters.add(AppEventTracking.EventLabel.ETALASE);
        }

        if (!productManageFilterModel.getCatalogProductOption().equals(CatalogProductOption.WITH_AND_WITHOUT)) {
            filters.add(AppEventTracking.EventLabel.CATALOG);
        }

        if (!productManageFilterModel.getConditionProductOption().equals(ConditionProductOption.ALL_CONDITION)) {
            filters.add(AppEventTracking.EventLabel.CONDITION);
        }

        if (!productManageFilterModel.getPictureStatusOption().equals(PictureStatusProductOption.WITH_AND_WITHOUT)) {
            filters.add(AppEventTracking.EventLabel.PICTURE_STATUS);
        }

        UnifyTracking.eventProductManageFilterProduct(getActivity(), TextUtils.join(",", filters));
    }

    protected void resetPageAndRefresh() {
        resetPageAndSearch();
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    protected void setSearchMode(boolean searchMode) {
        if (filtered) {
            super.setSearchMode(true);
            return;
        }
        super.setSearchMode(searchMode);
    }

    @Override
    protected void showSearchView(boolean show) {
        super.showSearchView(show);
        bottomActionView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected BaseListAdapter<ProductManageViewModel> getNewAdapter() {
        ProductManageListAdapter productManageListAdapter = new ProductManageListAdapter();
        productManageListAdapter.setClickOptionCallback(this);
        productManageListAdapter.setCheckedCallback(this);
        return productManageListAdapter;
    }

    @Override
    protected void onPullToRefresh() {
        productManagePresenter.getFreeClaim(GraphqlHelper.loadRawString(getResources(), R.raw.gql_get_deposit), userSession.getShopId());
        goldMerchant = null;
        ((ProductManageListAdapter) adapter).setFeaturedProduct(null);
        super.onPullToRefresh();
    }

    @Override
    protected void searchForPage(int page) {
        if (goldMerchant == null) {
            productManagePresenter.getGoldMerchantStatus();
        }
        if (((ProductManageListAdapter) adapter).getFeaturedProduct() == null) {
            productManagePresenter.getListFeaturedProduct();
        }
        productManagePresenter.getListProduct(page, searchInputView.getSearchText(),
                productManageFilterModel.getCatalogProductOption(), productManageFilterModel.getConditionProductOption(),
                productManageFilterModel.getEtalaseProductOption(), productManageFilterModel.getPictureStatusOption(),
                sortProductOption, productManageFilterModel.getCategoryId());
        hasNextPage = false;
    }

    @Override
    public void onItemClicked(ProductManageViewModel productManageViewModel) {
        if (actionMode == null) {
            ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), false);
            adapter.notifyDataSetChanged();
            goToPDP(productManageViewModel.getProductId());
            UnifyTracking.eventProductManageClickDetail(getActivity());
        }
    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private void goToPDP(String productId) {
        if (getContext() != null){
            RouteManager.routeInternal(getContext(),
                    UriUtil.buildUri(ApplinkConstInternal.Marketplace.PRODUCT_DETAIL, productId));
        }
    }

    @Override
    public void onItemChecked(ProductManageViewModel productManageViewModel, boolean checked) {
        if (actionMode != null) {
            int totalChecked = ((ProductManageListAdapter) adapter).getTotalChecked();
            actionMode.setTitle(String.valueOf(totalChecked));
            MenuItem deleteMenuItem = actionMode.getMenu().findItem(R.id.delete_product_menu);
            deleteMenuItem.setVisible(totalChecked > 0);
        } else {
            ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), checked);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage) {
        onSearchLoaded(list, totalItem);
        this.hasNextPage = hasNextPage;
    }

    @Override
    public void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore, String shopDomain) {
        this.goldMerchant = goldMerchant;
        isOfficialStore = officialStore;
        this.shopDomain = shopDomain;
    }

    @Override
    public void onSuccessGetFeaturedProductList(List<String> data) {
        ((ProductManageListAdapter) adapter).setFeaturedProduct(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText) {
        ((ProductManageListAdapter) adapter).updatePrice(productId, price, currencyId, currencyText);
    }

    @Override
    public void onErrorEditPrice(Throwable t, final String productId, final String price, final String currencyId, final String currencyText) {
        NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                ViewUtils.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        productManagePresenter.editPrice(productId, price, currencyId, currencyText);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void onSuccessSetCashback(String productId, int cashback) {
        ((ProductManageListAdapter) adapter).updateCashback(productId, cashback);
    }

    @Override
    public void onErrorSetCashback(Throwable t, final String productId, final int cashback) {
        if (t instanceof MessageErrorException && ((MessageErrorException) t).getErrorCode().equals(ERROR_CODE_LIMIT_CASHBACK)) {
            showDialogActionGoToGMSubscribe();
        } else {
            NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                    ErrorHandler.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            productManagePresenter.setCashback(productId, cashback);
                        }
                    }).showRetrySnackbar();
        }
    }

    @Override
    public void onSuccessMultipleDeleteProduct() {
        resetPageAndSearch();
    }

    @Override
    public void onErrorMultipleDeleteProduct(Throwable t, List<String> productIdDeletedList, final List<String> productIdFailToDeleteList) {
        if (productIdDeletedList.size() > 0) {
            resetPageAndSearch();
        }
        NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                ViewUtils.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        productManagePresenter.deleteProduct(productIdFailToDeleteList);
                    }
                }).showRetrySnackbar();
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(addProductReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        getActivity().registerReceiver(addProductReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productManagePresenter.detachView();
        if (addProductReceiver.isOrderedBroadcast()) {
            getActivity().unregisterReceiver(addProductReceiver);
        }
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        showActionProductDialog(productManageViewModel);
    }

    @Override
    public boolean isActionModeActive() {
        return actionMode != null;
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingProgress() {
        progressDialog.hide();
    }

    @Override
    public void onErrorGetFreeClaim(Throwable throwable) {
        topAdsWidgetFreeClaim.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetFreeClaim(DataDeposit dataDeposit) {
        FreeDeposit freeDeposit = dataDeposit.getFreeDeposit();

        if (freeDeposit.getNominal() > 0 && freeDeposit.getStatus() == 1) {
            topAdsWidgetFreeClaim.setContent(MethodChecker.fromHtml(getString(R.string.free_claim_template, freeDeposit.getNominalFmt(),
                    freeDeposit.getRemainingDays() + "", TopAdsFreeClaimConstantKt.TOPADS_FREE_CLAIM_URL)));
            topAdsWidgetFreeClaim.setVisibility(View.VISIBLE);
        } else {
            topAdsWidgetFreeClaim.setVisibility(View.GONE);
        }

    }

    private void showActionProductDialog(ProductManageViewModel productManageViewModel) {
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());

        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(productManageViewModel.getProductName());
        if (productManageViewModel.getProductStatus().equals(StatusProductOption.EMPTY)) {
            bottomSheetBuilder.setMenu(R.menu.menu_product_manage_action_item_no_topads);
        } else {
            bottomSheetBuilder.setMenu(R.menu.menu_product_manage_action_item);
        }
        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionBottomSheetClicked(productManageViewModel))
                .createDialog();
        bottomSheetDialog.show();
    }

    @NonNull
    private BottomSheetItemClickListener onOptionBottomSheetClicked(final ProductManageViewModel productManageViewModel) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(final MenuItem item) {
                if (productManageViewModel.getProductStatus().equals(StatusProductOption.UNDER_SUPERVISION)) {
                    NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.product_manage_desc_product_on_supervision, productManageViewModel.getProductName()));
                    return;
                }
                int itemId = item.getItemId();
                if (itemId == R.id.edit_product_menu) {
                    goToEditProduct(productManageViewModel.getId());
                    UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString());
                } else if (itemId == R.id.duplicat_product_menu) {
                    goToDuplicateProduct(productManageViewModel.getId());
                    UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString());
                } else if (itemId == R.id.delete_product_menu) {
                    final List<String> productIdList = new ArrayList<>();
                    productIdList.add(productManageViewModel.getId());
                    showDialogActionDeleteProduct(productIdList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString() + " - " + getString(R.string.label_delete));
                            productManagePresenter.deleteProduct(productIdList);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString() + " - " + getString(R.string.title_cancel));
                            dialog.dismiss();
                        }
                    });
                } else if (itemId == R.id.change_price_product_menu) {
                    if (productManageViewModel.isProductVariant()) {
                        showDialogVariantPriceLocked();
                    } else {
                        showDialogChangeProductPrice(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencyId());
                    }
                } else if (itemId == R.id.share_product_menu) {
                    downloadBitmap(productManageViewModel);
                } else if (itemId == R.id.set_cashback_product_menu) {
                    onSetCashbackClicked(productManageViewModel);
                } else if (itemId == R.id.set_promo_ads_product_menu) {
                    onPromoTopAdsClicked(productManageViewModel);
                }
            }
        };
    }

    private void showDialogVariantPriceLocked() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle(getString(R.string.product_price_locked))
                .setMessage(getString(R.string.product_price_locked_manage_desc))
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no op, just dismiss
                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void onPromoTopAdsClicked(ProductManageViewModel productManageViewModel) {
        ((PdpRouter) getActivity().getApplication()).goToCreateTopadsPromo(getActivity(),
                productManageViewModel.getItemId(), productManageViewModel.getProductShopId(),
                GlobalConfig.isSellerApp() ? TopAdsSourceOption.SA_MANAGE_LIST_PRODUCT :
                        TopAdsSourceOption.MA_MANAGE_LIST_PRODUCT);
    }

    private void onSetCashbackClicked(ProductManageViewModel productManageViewModel) {
        if (!GlobalConfig.isSellerApp() && getActivity().getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
            return;
        }
        showOptionCashback(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(),
                productManageViewModel.getProductCurrencySymbol(), productManageViewModel.getProductCashback());
    }

    private void showOptionCashback(String productId, String productPrice, String productPriceSymbol, int productCashback) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.product_manage_cashback_title));

        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_3);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_4);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_5);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_NONE);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionCashbackClicked(productId))
                .createDialog();
        bottomSheetDialog.show();
    }

    private void addCashbackBottomSheetItemMenu(BottomSheetBuilder bottomSheetBuilder,
                                                String productPrice, String productPriceSymbol, int productCashback, @CashbackOption int cashbackOption) {
        if (bottomSheetBuilder instanceof CheckedBottomSheetBuilder) {
            double productPricePlain = Double.parseDouble(productPrice);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(cashbackOption,
                    getCashbackMenuText(cashbackOption, productPriceSymbol, productPricePlain), null, productCashback == cashbackOption);
        }
    }

    private String getCashbackMenuText(int cashback, String productPriceSymbol, double productPricePlain) {
        String cashbackText = getString(R.string.product_manage_cashback_option_none);
        if (cashback > 0) {
            cashbackText = getString(R.string.product_manage_cashback_option, String.valueOf(cashback),
                    productPriceSymbol,
                    KMNumbers.formatDouble2PCheckRound(((double) cashback * productPricePlain / 100f), !productPriceSymbol.equals("Rp")));
        }
        return cashbackText;
    }

    private BottomSheetItemClickListener onOptionCashbackClicked(final String productId) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case CashbackOption.CASHBACK_OPTION_3:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_3);
                        break;
                    case CashbackOption.CASHBACK_OPTION_4:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_4);
                        break;
                    case CashbackOption.CASHBACK_OPTION_5:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_5);
                        break;
                    case CashbackOption.CASHBACK_OPTION_NONE:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_NONE);
                        break;
                    default:
                        break;
                }
                UnifyTracking.eventProductManageOverflowMenu(getActivity(), getString(R.string.product_manage_cashback_title) + " - " + item.getTitle());
            }
        };
    }

    public void downloadBitmap(final ProductManageViewModel productManageViewModel){
        ProductShare productShare = new ProductShare(getActivity(), ProductShare.MODE_IMAGE);

        String price = (productManageViewModel.getProductCurrencyId() == CurrencyTypeDef.TYPE_USD) ? productManageViewModel.getProductPricePlain() : productManageViewModel.getProductPrice();
        ProductData data = new ProductData();
        data.setPriceText(productManageViewModel.getProductCurrencySymbol() + " " + price);
        data.setCashbacktext((productManageViewModel.getProductCashback() > 0) ? getString(R.string.pml_sticker_cashback, productManageViewModel.getProductCashback()) : "");
        data.setCurrencySymbol(productManageViewModel.getProductCurrencySymbol());
        data.setProductId(productManageViewModel.getProductId());
        data.setProductName(productManageViewModel.getProductName());
        data.setProductUrl(productManageViewModel.getProductUrl());
        data.setProductImageUrl(productManageViewModel.getImageFullUrl());
        data.setShopUrl(getString(R.string.pml_sticker_shop_link, shopDomain));

        productShare.share(data, () -> {
            showLoadingProgress();
            return Unit.INSTANCE;
        }, () -> {
            hideLoadingProgress();
            return Unit.INSTANCE;
        });
    }

    private void showDialogChangeProductPrice(final String productId, String productPrice, @CurrencyTypeDef int productCurrencyId) {
        if (!isAdded() || goldMerchant == null) {
            return;
        }
        ProductManageEditPriceDialogFragment productManageEditPriceDialogFragment =
                ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, productCurrencyId, goldMerchant, isOfficialStore);
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(new ProductManageEditPriceDialogFragment.ListenerDialogEditPrice() {
            @Override
            public void onSubmitEditPrice(String productId, String price, String currencyId, String currencyText) {
                productManagePresenter.editPrice(productId, price, currencyId, currencyText);
            }
        });
        productManageEditPriceDialogFragment.show(getActivity().getFragmentManager(), "");
    }

    private void showDialogActionDeleteProduct(final List<String> productIdList, Dialog.OnClickListener onClickListener, Dialog.OnClickListener onCancelListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.label_delete);
        alertDialog.setMessage(R.string.dialog_delete_product);
        alertDialog.setPositiveButton(R.string.label_delete, onClickListener);
        alertDialog.setNegativeButton(R.string.title_cancel, onCancelListener);
        alertDialog.show();
    }

    private void showDialogActionGoToGMSubscribe() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.product_manage_cashback_limited_title);
        alertDialog.setMessage(getString(R.string.product_manage_cashback_limited_desc,
                getString(GMConstant.getGMTitleResource(getContext()))));
        alertDialog.setPositiveButton(R.string.pml_label_subscribe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getActivity().getApplication() instanceof SellerModuleRouter) {
                    ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
                }
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, null);
        alertDialog.show();
    }

    private void goToDuplicateProduct(String productId) {
        Intent intent = ProductDuplicateActivity.Companion.createInstance(getActivity(), productId);
        startActivity(intent);
    }

    private void goToEditProduct(String productId) {
        Intent intent = ProductEditActivity.Companion.createInstance(getActivity(), productId);
        startActivity(intent);
    }
}