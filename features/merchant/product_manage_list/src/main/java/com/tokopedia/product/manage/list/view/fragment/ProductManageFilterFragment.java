package com.tokopedia.product.manage.list.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant;
import com.tokopedia.product.manage.list.R;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.category.view.activity.CategoryDynamicPickerActivity;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.etalase.view.activity.EtalaseDynamicPickerActivity;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.view.model.ProductManageCategoryViewModel;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;

import java.util.ArrayList;

import static com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterFragment extends TkpdBaseV4Fragment {

    private LabelView etalaseLabelView;
    private LabelView categoryLabelView;
    private LabelView conditionLabelView;
    private LabelView catalogLabelView;
    private LabelView productPictureLabelView;
    private Button submitButton;

    private ProductManageFilterModel productManageFilterModel;

    public static Fragment createInstance(ProductManageFilterModel productManageFilterModel) {
        ProductManageFilterFragment productManageFilterFragment = new ProductManageFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FILTER_SELECTED, productManageFilterModel);
        productManageFilterFragment.setArguments(bundle);
        return productManageFilterFragment;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialFilterModel();
    }

    private void initialFilterModel() {
        if (getArguments() != null && getArguments().getParcelable(EXTRA_FILTER_SELECTED) != null) {
            productManageFilterModel = getArguments().getParcelable(EXTRA_FILTER_SELECTED);
        } else {
            productManageFilterModel = new ProductManageFilterModel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.tokopedia.seller.R.layout.fragment_product_manage_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etalaseLabelView = (LabelView) view.findViewById(R.id.etalase);
        categoryLabelView = (LabelView) view.findViewById(R.id.category);
        conditionLabelView = (LabelView) view.findViewById(R.id.condition);
        catalogLabelView = (LabelView) view.findViewById(R.id.catalog);
        productPictureLabelView = (LabelView) view.findViewById(R.id.product_picture);
        submitButton = (Button) view.findViewById(R.id.button_submit);

        updateFilterView();

        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEtalaseOption();
            }
        });
        categoryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryOption();
            }
        });
        conditionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConditionOption();
            }
        });
        catalogLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatalogOption();
            }
        });
        productPictureLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductPictureOption();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitFilter();
            }
        });
    }

    private void updateFilterView() {
        if (TextUtils.isEmpty(productManageFilterModel.getEtalaseProductOptionName())) {
            productManageFilterModel.setEtalaseProductOptionName(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_etalase_all));
        }
        etalaseLabelView.setContent(MethodChecker.fromHtml(productManageFilterModel.getEtalaseProductOptionName()));
        if (TextUtils.isEmpty(productManageFilterModel.getCategoryName())) {
            productManageFilterModel.setCategoryName(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_category_all));
        }
        categoryLabelView.setContent(productManageFilterModel.getCategoryName());
        switch (productManageFilterModel.getConditionProductOption()) {
            case ConditionProductOption.ALL_CONDITION:
                conditionLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_both_condition));
                break;
            case ConditionProductOption.NEW:
                conditionLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_condition_new));
                break;
            case ConditionProductOption.USED:
                conditionLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_condition_old));
                break;
            default:
                conditionLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_both_condition));
                break;
        }
        switch (productManageFilterModel.getCatalogProductOption()) {
            case CatalogProductOption.WITH_AND_WITHOUT:
                catalogLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_both_catalog));
                break;
            case CatalogProductOption.WITH_CATALOG:
                catalogLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_with_catalog));
                break;
            case CatalogProductOption.WITHOUT_CATALOG:
                catalogLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_without_catalog));
                break;
            default:
                catalogLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_both_catalog));
                break;
        }
        switch (productManageFilterModel.getPictureStatusOption()) {
            case PictureStatusProductOption.WITH_AND_WITHOUT:
                productPictureLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_picture_both));
                break;
            case PictureStatusProductOption.WITH_IMAGE:
                productPictureLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_with_picture));
                break;
            case PictureStatusProductOption.WITHOUT_IMAGE:
                productPictureLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_without_picture));
                break;
            default:
                productPictureLabelView.setContent(getString(com.tokopedia.seller.R.string.product_manage_filter_menu_picture_both));
                break;
        }
    }

    private void showEtalaseOption() {
        ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModels = new ArrayList<>();
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_PRODUK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_etalase_all)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_SOLD_PRODUK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_sold)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_EMPTY_STOK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_empty_stok)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PENDING, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_pending)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_FREE_RETURNS, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_free_returns)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PREORDER, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_preorder)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_SHOWCASE, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_all_showcase)));
        Intent intent = EtalaseDynamicPickerActivity.createInstance(getActivity(), productManageFilterModel.getEtalaseProductOption(), myEtalaseItemViewModels);
        startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_ETALASE);
    }

    private void onSubmitFilter() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILTER_SELECTED, productManageFilterModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductManageConstant.REQUEST_CODE_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    long categoryId = data.getLongExtra(CategoryPickerActivity.CATEGORY_RESULT_ID, -1);
                    String categoryName = data.getStringExtra(CategoryPickerActivity.CATEGORY_RESULT_NAME);
                    productManageFilterModel.setCategoryId(String.valueOf(categoryId));
                    productManageFilterModel.setCategoryName(categoryName);
                    updateFilterView();
                }
                break;
            case ProductManageConstant.REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    int etalaseId = data.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1);
                    String etalaseName = data.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME);
                    productManageFilterModel.setEtalaseProductOption(etalaseId);
                    productManageFilterModel.setEtalaseProductOptionName(etalaseName);
                    updateFilterView();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showProductPictureOption() {
        showBottomSheetOption(productPictureLabelView.getTitle(), com.tokopedia.seller.R.menu.menu_product_manage_filter_picture_option,
                productPictureLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == com.tokopedia.seller.R.id.both_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITH_AND_WITHOUT);
                } else if (itemId == com.tokopedia.seller.R.id.without_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITHOUT_IMAGE);
                } else if (itemId == com.tokopedia.seller.R.id.with_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITH_IMAGE);
                }
                updateFilterView();
            }
        });
    }

    private void showCatalogOption() {
        showBottomSheetOption(catalogLabelView.getTitle(), com.tokopedia.seller.R.menu.menu_product_manage_filter_catalog_option,
                catalogLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == com.tokopedia.seller.R.id.both_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITH_AND_WITHOUT);
                } else if (itemId == com.tokopedia.seller.R.id.without_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITHOUT_CATALOG);
                } else if (itemId == com.tokopedia.seller.R.id.with_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITH_CATALOG);
                }
                updateFilterView();
            }
        });
    }

    private void showConditionOption() {
        showBottomSheetOption(conditionLabelView.getTitle(), com.tokopedia.seller.R.menu.menu_product_manage_filter_condition_option,
                conditionLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == com.tokopedia.seller.R.id.both_condtion_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.ALL_CONDITION);
                } else if (itemId == com.tokopedia.seller.R.id.new_condition_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.NEW);
                } else if (itemId == com.tokopedia.seller.R.id.old_condition_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.USED);
                }
                updateFilterView();
            }
        });
    }

    private void showCategoryOption() {
        long categoryId;
        if (productManageFilterModel.getCategoryId() != null && !productManageFilterModel.getCategoryId().isEmpty()) {
            categoryId = Long.parseLong(productManageFilterModel.getCategoryId());
        } else {
            categoryId = 0;
        }
        ArrayList<ProductManageCategoryViewModel> categoryViewModels = new ArrayList<>();
        categoryViewModels.add(new ProductManageCategoryViewModel(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_category_all), ProductManageConstant.FILTER_ALL_CATEGORY, false));
        Intent intent = CategoryDynamicPickerActivity.createIntent(getActivity(), categoryId, categoryViewModels);
        startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_CATEGORY);
    }

    private void showBottomSheetOption(
            String title, @MenuRes int menu, String titleSelected, BottomSheetItemClickListener bottomSheetItemClickListener) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(title)
                .setMenu(menu);

        ((CheckedBottomSheetBuilder) bottomSheetBuilder).setSelection(titleSelected);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(bottomSheetItemClickListener)
                .createDialog();
        bottomSheetDialog.show();
    }

    public void onResetFilter() {
        productManageFilterModel.reset();
        updateFilterView();
    }
}