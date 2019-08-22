package com.tokopedia.product.manage.list.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.ViewUtils;
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder;
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity;
import com.tokopedia.product.manage.item.main.duplicate.activity.ProductDuplicateActivity;
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity;
import com.tokopedia.product.manage.item.stock.view.activity.ProductEditStockActivity;
import com.tokopedia.product.manage.item.stock.view.model.ProductStock;
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.constant.CashbackOption;
import com.tokopedia.product.manage.list.constant.ManageTrackingConstant;
import com.tokopedia.product.manage.list.constant.StatusProductOption;
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType;
import com.tokopedia.product.manage.list.di.DaggerProductManageComponent;
import com.tokopedia.product.manage.list.di.ProductManageModule;
import com.tokopedia.product.manage.list.view.activity.ProductManageFilterActivity;
import com.tokopedia.product.manage.list.view.activity.ProductManageSortActivity;
import com.tokopedia.product.manage.list.view.adapter.ProductManageListAdapter;
import com.tokopedia.product.manage.list.view.adapter.ProductManageListViewHolder;
import com.tokopedia.product.manage.list.view.bottomsheets.EditProductBottomSheet;
import com.tokopedia.product.manage.list.view.factory.ProductManageFragmentFactoryImpl;
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
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.IMG_URL_POWER_MERCHANT_IDLE_POPUP;
import static com.tokopedia.gm.common.constant.GMCommonConstantKt.IMG_URL_REGULAR_MERCHANT_POPUP;
import static com.tokopedia.gm.common.constant.GMCommonConstantKt.URL_POWER_MERCHANT_SCORE_TIPS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST;
import static com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.EXTRA_STOCK;
import static com.tokopedia.product.manage.list.view.fragment.ProductManageSellerFragment.URL_TIPS_TRICK;
import static com.tokopedia.seller.product.manage.constant.ProductManageConstant.ETALASE_PICKER_REQUEST_CODE;
import static com.tokopedia.seller.product.manage.constant.ProductManageConstant.STOCK_EDIT_REQUEST_CODE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageFragment extends BaseSearchListFragment<ProductManageViewModel, ProductManageFragmentFactoryImpl>
        implements ProductManageView,
        BaseListCheckableAdapter.OnCheckableAdapterListener<ProductManageViewModel>,
        MerchantCommonBottomSheet.BottomSheetListener,
        BaseCheckableViewHolder.CheckableInteractionListener,
        ProductManageListViewHolder.ProductManageViewHolderListener,
        EditProductBottomSheet.EditProductInterface {

    public static final String ERROR_CODE_LIMIT_CASHBACK = "422";
    public static final int REQUEST_CODE_ADD_IMAGE = 3859;
    public static final int INSTAGRAM_SELECT_REQUEST_CODE = 3860;


    private List<ProductManageViewModel> productManageViewModels = null;

    @Inject
    ProductManagePresenter productManagePresenter;
    @Inject
    UserSessionInterface userSession;
    private BottomActionView bottomActionView;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    private TopAdsWidgetFreeClaim topAdsWidgetFreeClaim;

    protected ProductManageListAdapter adapter;
    @SortProductOption
    private String sortProductOption;
    private ProductManageFilterModel productManageFilterModel;
    private Boolean goldMerchant;
    private boolean isOfficialStore;
    private String shopDomain;
    private Button btnSubmit;
    private Button btnGoToPdp;
    private TextView txtTipsTrick;
    private Dialog dialog;
    private CheckBox bulkCheckBox;
    private TextView bulkCountTxt;
    private Button btnBulk;
    private LinearLayout containerBtnBulk;
    private LinearLayout containerChechBoxBulk;
    private View checkBoxView;
    private CloseableBottomSheetDialog bulkBottomSheet;
    private EditProductBottomSheet editProductBottomSheet;

    private BroadcastReceiver addProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TkpdState.ProductService.BROADCAST_ADD_PRODUCT) &&
                    intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                    intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                getActivity().runOnUiThread(() -> {
                    String productId = intent.getExtras().getString(TkpdState.ProductService.PRODUCT_ID);
                    productManagePresenter.getPopupsInfo(productId);
                    loadInitialData();
                });

            }
        }
    };

    @Override
    public void loadData(int page) {
        productManagePresenter.getFreeClaim(GraphqlHelper.loadRawString(getResources(), R.raw.gql_get_deposit), userSession.getShopId());

        if (goldMerchant == null) {
            productManagePresenter.getGoldMerchantStatus();
        }

        productManagePresenter.getProductList(page, searchInputView.getSearchText(),
                productManageFilterModel.getCatalogProductOption(), productManageFilterModel.getConditionProductOption(),
                productManageFilterModel.getEtalaseProductOption(), productManageFilterModel.getPictureStatusOption(),
                sortProductOption, productManageFilterModel.getCategoryId());
    }

    @Override
    protected ProductManageFragmentFactoryImpl getAdapterTypeFactory() {
        return new ProductManageFragmentFactoryImpl(this, this);
    }

    @Override
    protected String getScreenName() {
        return "";
    }


    @Override
    public void onSearchTextChanged(String text) {
    }

    @Override
    public void onLoadListEmpty() {
        renderList(new ArrayList<>());
    }

    @Override
    public void renderList(@NonNull List<ProductManageViewModel> list) {
        super.renderList(list);
        if (adapter.getTotalChecked() != 0) {
            List<ProductManageViewModel> listChecked = adapter.getCheckedDataList();
            HashSet<Integer> listPositionChecked = new HashSet<>();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(i) == listChecked.get(j)) {
                        listPositionChecked.add(i);
                    }
                }
            }
            adapter.setCheckedPositionList(listPositionChecked);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean isChecked(int position) {
        return adapter.isChecked(position);
    }

    @Override
    public void updateListByCheck(boolean isChecked, int position) {
        adapter.updateListByCheck(isChecked, position);
    }

    @NonNull
    @Override
    protected com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter<ProductManageViewModel, ProductManageFragmentFactoryImpl> createAdapterInstance() {
        adapter = new ProductManageListAdapter(getAdapterTypeFactory(), this);
        return adapter;
    }

    private Dialog initPopUpDialog(String productId) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_product_add);

        btnSubmit = dialog.findViewById(R.id.btn_submit);
        btnGoToPdp = dialog.findViewById(R.id.btn_product_list);
        txtTipsTrick = dialog.findViewById(R.id.txt_tips_trick);

        btnSubmit.setOnClickListener(v -> {
            trackerManageCourierButton();
            RouteManager.route(getContext(), ApplinkConst.SELLER_SHIPPING_EDITOR);
            getActivity().finish();
        });

        btnGoToPdp.setOnClickListener(v -> {
            trackerSeeProduct();
            goToPDP(productId);
            dialog.dismiss();
        });
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.tkpd_main_green);

        SpannableString spanText = new SpannableString(getString(R.string.popup_tips_trick_clickable));
        spanText.setSpan(new StyleSpan(Typeface.BOLD),
                5, spanText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(backgroundColor),
                5, spanText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                trackerLinkClick();
                RouteManager.route(getContext(), String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TIPS_TRICK));
                getActivity().finish();
            }
        };
        spanText.setSpan(cs, 5, spanText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTipsTrick.setMovementMethod(LinkMovementMethod.getInstance());
        txtTipsTrick.setText(spanText);
        return dialog;
    }

    private void trackerManageCourierButton() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ManageTrackingConstant.EVENT_ADD_PRODUCT,
                ManageTrackingConstant.CATEGORY_ADD_PRODUCT,
                ManageTrackingConstant.ACTION_CLICK_MANAGE_COURIER,
                "");
    }

    private void trackerSeeProduct() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ManageTrackingConstant.EVENT_ADD_PRODUCT,
                ManageTrackingConstant.CATEGORY_ADD_PRODUCT,
                ManageTrackingConstant.ACTION_SEE_PRODUCT,
                "");
    }

    private void trackerLinkClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ManageTrackingConstant.EVENT_ADD_PRODUCT,
                ManageTrackingConstant.CATEGORY_ADD_PRODUCT,
                ManageTrackingConstant.ACTION_LINK,
                "");
    }

    @Override
    protected void initInjector() {
        GraphqlClient.init(getContext());
        DaggerProductManageComponent.builder()
                .productManageModule(new ProductManageModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productManagePresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_manage, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sortProductOption = SortProductOption.POSITION;
        productManageFilterModel = new ProductManageFilterModel();
        productManageFilterModel.reset();
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView(view).addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        searchInputView.clearFocus();
        initView(view);
        setupBottomSheet();
        renderCheckedView();

        bottomActionView.setButton1OnClickListener(v -> {
            Intent intent = ProductManageSortActivity.createIntent(getActivity(), sortProductOption);
            startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_SORT);
        });

        bottomActionView.setButton2OnClickListener(v -> {
            Intent intent = ProductManageFilterActivity.createIntent(getActivity(), productManageFilterModel);
            startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_FILTER);
        });

        bulkCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < productManageViewModels.size(); i++) {
                adapter.updateListByCheck(isChecked, i);
            }
        });

    }

    private void renderCheckedView() {
        if (adapter.getTotalChecked() > 0) {
            containerBtnBulk.setVisibility(View.VISIBLE);
            bulkCountTxt.setVisibility(View.VISIBLE);
            checkBoxView.setVisibility(View.VISIBLE);
        } else {
            containerBtnBulk.setVisibility(View.GONE);
            checkBoxView.setVisibility(View.GONE);
            bulkCountTxt.setVisibility(View.GONE);
        }

        btnBulk.setOnClickListener(v -> {
            bulkBottomSheet.show();
        });
    }

    private void initView(View view) {
        bulkCheckBox = view.findViewById(R.id.bulk_check_box);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        bulkCountTxt = view.findViewById(R.id.bulk_count_txt);
        topAdsWidgetFreeClaim = view.findViewById(R.id.topads_free_claim_widget);
        btnBulk = view.findViewById(R.id.btn_bulk_edit);
        containerBtnBulk = view.findViewById(R.id.container_btn_bulk);
        containerChechBoxBulk = view.findViewById(R.id.container_bulk_check_box);
        checkBoxView = view.findViewById(R.id.line_check_box);
    }

    private void setupBottomSheet() {
        bulkBottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(getContext(),
                () -> editProductBottomSheet.clearAllData());
        bulkBottomSheet.shouldFullScreen = true;
        if (getContext() != null) {
            editProductBottomSheet = new EditProductBottomSheet(getContext(), this);
        }
        bulkBottomSheet.setCustomContentView(editProductBottomSheet, getString(R.string.product_bs_title), true);
    }

    @Override
    public void onProductClicked(ProductManageViewModel productManageViewModel) {
        adapter.notifyDataSetChanged();
        goToPDP(productManageViewModel.getProductId());
        UnifyTracking.eventProductManageClickDetail(getActivity());
    }

    @Override
    public void onSearchSubmitted(String text) {
        UnifyTracking.eventProductManageSearch(getActivity());
        isLoadingInitialData = true;
        ArrayList<ProductManageViewModel> tempShopEtalaseViewModels = new ArrayList<>();
        if (productManageViewModels != null &&
                productManageViewModels.size() > 0) {
            String textLowerCase = text.toLowerCase();
            for (ProductManageViewModel data : productManageViewModels) {
                if (data.getProductName().toLowerCase().contains(textLowerCase)) {
                    tempShopEtalaseViewModels.add(data);
                }
            }
        }
        renderList(tempShopEtalaseViewModels, false);
        showSearchViewWithDataSizeCheck();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccessGetPopUp(boolean isShowPopup, String productId) {
        if (isShowPopup) {
            initPopUpDialog(productId).show();
        }
    }

    @Override
    public void onErrorGetPopUp(Throwable e) {
        onSuccessGetPopUp(false, null);
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
                    loadInitialData();
                    trackingFilter(productManageFilterModel);
                }
                break;
            case ETALASE_PICKER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    int etalaseId = intent.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1);
                    String etalaseNameString = intent.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME);
                    editProductBottomSheet.setResultValue(new BulkBottomSheetType.EtalaseType(etalaseNameString, etalaseId), null);
                }
                break;
            case STOCK_EDIT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    ProductStock productStock = intent.getParcelableExtra(EXTRA_STOCK);
                    String stockValue;
                    if (!productStock.isActive()) {
                        stockValue = getContext().getString(R.string.stock_unavailable);
                    } else {
                        stockValue = "Tersedia";
                    }
                    editProductBottomSheet.setResultValue(null, new BulkBottomSheetType.StockType(stockValue, productStock.getStockCount()));
                }
                break;
            case ProductManageConstant.REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    ProductManageSortModel productManageSortModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_SORT_SELECTED);
                    sortProductOption = productManageSortModel.getId();
                    loadInitialData();
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

    @Override
    public void onItemClicked(ProductManageViewModel productManageViewModel) {

    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private void goToPDP(String productId) {
        if (getContext() != null && productId != null) {
            RouteManager.route(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        }
    }

    @Override
    public void onItemChecked(ProductManageViewModel productManageViewModel, boolean checked) {
        String totalChecked = String.valueOf(adapter.getTotalChecked());
        updateBulkLayout();
        bulkCountTxt.setText(MethodChecker.fromHtml(getString(R.string.product_manage_bulk_count, totalChecked)));
    }

    private void updateBulkLayout() {
        AppBarLayout.LayoutParams containerFlags = (AppBarLayout.LayoutParams) containerChechBoxBulk.getLayoutParams();
        if (adapter.getTotalChecked() == 1) {
            containerBtnBulk.setVisibility(View.VISIBLE);
            btnBulk.setVisibility(View.VISIBLE);
            checkBoxView.setVisibility(View.VISIBLE);
            btnBulk.setText("Ubah");
            containerFlags.setScrollFlags(0);
        } else if (adapter.getTotalChecked() > 1) {
            containerBtnBulk.setVisibility(View.VISIBLE);
            btnBulk.setVisibility(View.VISIBLE);
            checkBoxView.setVisibility(View.VISIBLE);
            btnBulk.setText("Ubah Sekaligus");
            containerFlags.setScrollFlags(0);
        } else {
            containerBtnBulk.setVisibility(View.GONE);
            checkBoxView.setVisibility(View.GONE);
            btnBulk.setVisibility(View.GONE);
            containerFlags.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        }
    }

    @Override
    public void onSuccessGetProductList(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage) {
        productManageViewModels = list;
        renderList(list, hasNextPage);
    }

    @Override
    public void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore, String shopDomain) {
        this.goldMerchant = goldMerchant;
        isOfficialStore = officialStore;
        this.shopDomain = shopDomain;
    }

    @Override
    public void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText) {
        adapter.updatePrice(productId, price, currencyId, currencyText);
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
        adapter.updateCashback(productId, cashback);
    }

    @Override
    public void onErrorSetCashback(Throwable t, final String productId, final int cashback) {
        if (t instanceof MessageErrorException && ((MessageErrorException) t).getErrorCode().equals(ERROR_CODE_LIMIT_CASHBACK)) {
            if (isIdlePowerMerchant()) {
                showIdlePowerMerchantBottomSheet(
                        getString(R.string.product_manage_feature_name_cashback)
                );
            } else if (!isPowerMerchant()) {
                showRegularMerchantBottomSheet(
                        getString(R.string.product_manage_feature_name_cashback)
                );
            } else {
                NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                        ErrorHandler.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                productManagePresenter.setCashback(productId, cashback);
                            }
                        }).showRetrySnackbar();
            }
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

    private boolean isIdlePowerMerchant() {
        return userSession.isPowerMerchantIdle();
    }

    private boolean isPowerMerchant() {
        return userSession.isGoldMerchant();
    }

    private void showRegularMerchantBottomSheet(String featureName) {
        String title = getString(R.string.bottom_sheet_regular_title, featureName);
        String description = getString(R.string.bottom_sheet_regular_desc, featureName);
        String buttonName = getString(R.string.bottom_sheet_regular_btn);
        showBottomSheet(title, IMG_URL_REGULAR_MERCHANT_POPUP, description, buttonName);
    }

    private void showIdlePowerMerchantBottomSheet(String featureName) {
        String title = getString(R.string.bottom_sheet_idle_title, featureName);
        String description = getString(R.string.bottom_sheet_idle_desc, featureName);
        String buttonName = getString(R.string.bottom_sheet_idle_btn);
        showBottomSheet(title, IMG_URL_POWER_MERCHANT_IDLE_POPUP, description, buttonName);
    }

    private void showBottomSheet(String title, String imageUrl, String description, String buttonName) {
        MerchantCommonBottomSheet.BottomSheetModel model = new MerchantCommonBottomSheet.BottomSheetModel(
                title,
                description,
                imageUrl,
                buttonName,
                ""
        );
        MerchantCommonBottomSheet bottomSheet = MerchantCommonBottomSheet.newInstance(model);
        bottomSheet.setListener(this);
        bottomSheet.show(getChildFragmentManager(), "merchant_warning_bottom_sheet");
    }

    @Override
    public void onSuccessMultipleDeleteProduct() {
        loadInitialData();
    }

    @Override
    public void onErrorMultipleDeleteProduct(Throwable t, List<String> productIdDeletedList, final List<String> productIdFailToDeleteList) {
        if (productIdDeletedList.size() > 0) {
            loadInitialData();
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
                    showDialogActionDeleteProduct(productIdList, (dialogInterface, i) -> {
                        UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString() + " - " + getString(R.string.label_delete));
                        productManagePresenter.deleteProduct(productIdList);
                    }, (dialog, which) -> {
                        UnifyTracking.eventProductManageOverflowMenu(getActivity(), item.getTitle().toString() + " - " + getString(R.string.title_cancel));
                        dialog.dismiss();
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
                productManageViewModel.getItemId(), userSession.getUserId(),
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
        return item -> {
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
        };
    }

    public void downloadBitmap(final ProductManageViewModel productManageViewModel) {
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

    private void goToDuplicateProduct(String productId) {
        Intent intent = ProductDuplicateActivity.Companion.createInstance(getActivity(), productId);
        startActivity(intent);
    }

    private void goToEditProduct(String productId) {
        Intent intent = ProductEditActivity.Companion.createInstance(getActivity(), productId);
        startActivity(intent);
    }

    @Override
    public void onBottomSheetButtonClicked() {
        if (isIdlePowerMerchant()) {
            RouteManager.route(getContext(), ApplinkConstInternalGlobal.WEBVIEW, URL_POWER_MERCHANT_SCORE_TIPS);
        } else if (!isPowerMerchant()) {
            RouteManager.route(getContext(), ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE);
        }
    }

    @Override
    public void goToEtalasePicker(int etalaseId) {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_ETALASE_PICKER,
                String.valueOf(etalaseId));
        startActivityForResult(intent, ETALASE_PICKER_REQUEST_CODE);
    }

    @Override
    public void goToEditStock() {
        startActivityForResult(ProductEditStockActivity.Companion.createIntent(getActivity(), new ProductStock(),
                false, false), STOCK_EDIT_REQUEST_CODE);
    }
}