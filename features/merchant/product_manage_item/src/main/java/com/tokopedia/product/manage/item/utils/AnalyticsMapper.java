package com.tokopedia.product.manage.item.utils;

import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.utils.constant.ProductStockTypeDef;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.analytics.AppEventTracking.AddProduct.FIELDS_MANDATORY_CATEGORY;
import static com.tokopedia.core.analytics.AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE;
import static com.tokopedia.core.analytics.AppEventTracking.AddProduct.FIELDS_MANDATORY_PRODUCT_NAME;
import static com.tokopedia.core.analytics.AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS;
import static com.tokopedia.core.analytics.AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT;

/**
 * Created by zulfikarrahman on 5/10/17.
 */

public class AnalyticsMapper {

    public static List<String> mapViewToAnalytic(ProductViewModel viewModel, boolean isShare) {
        List<String> listOfFields = new ArrayList<>();
        if (viewModel.getProductPictureViewModelList().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE);
        }
        if (viewModel.getProductWholesale() != null && viewModel.getProductWholesale().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_WHOLESALE);
        }
        if (viewModel.getProductStock() == ProductStockTypeDef.TYPE_ACTIVE) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_STOCK_MANAGEMENT);
        }
        if( !viewModel.getProductSku().isEmpty()){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_SKU_PRODUCT);
        }
        if (viewModel.isProductFreeReturn()) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_FREE_RETURN);
        }
        if (StringUtils.isNotBlank(viewModel.getProductDescription())) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_DESCRIPTION);
        }
        if (viewModel.getProductVideo() != null && viewModel.getProductVideo().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PRODUCT_VIDEO);
        }
        if (viewModel.getProductEtalase().getEtalaseId() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_ETALASE);
        }
        if (viewModel.isProductMustInsurance()) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_INSURANCE);
        }
        if (viewModel.getProductPreorder().getPreorderStatus() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
            if (viewModel.getProductPreorder().getPreorderProcessTime() > 0) {
                listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER_PROCESS_TIME);
            }
            if (viewModel.getProductPreorder().getPreorderTimeUnit() > 0) {
                listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER_TIME_UNIT);
            }
        }
        if (isShare) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_SHARE);
        }
        if (null != viewModel.getProductSizeChart()) {
            if (!viewModel.getProductSizeChart().getFilePath().isEmpty())
                listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PRODUCT_SIZE_CHART);
        }
        if (viewModel.hasVariant()) {
            boolean hasCustomVariantLv1 = false;
            boolean hasCustomVariantLv2 = false;
            ProductVariantViewModel productVariantViewModel = viewModel.getProductVariant();
            ProductVariantOptionParent productVariantOptionParentLv1 = productVariantViewModel.getVariantOptionParent(0);
            ProductVariantOptionParent productVariantOptionParentLv2 = productVariantViewModel.getVariantOptionParent(1);
            if (productVariantOptionParentLv1 != null && productVariantOptionParentLv1.hasProductVariantOptionChild()) {
                List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParentLv1.getProductVariantOptionChild();
                for (ProductVariantOptionChild productVariantOptionChild : productVariantOptionChildList) {
                    if (productVariantOptionChild.isCustomVariant()) {
                        hasCustomVariantLv1 = true;
                        listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1_CUSTOM);
                        break;
                    }
                }
                if (!hasCustomVariantLv1) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1);
                }

            }
            if (productVariantOptionParentLv2 != null && productVariantOptionParentLv2.hasProductVariantOptionChild()) {
                List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParentLv2.getProductVariantOptionChild();
                for (ProductVariantOptionChild productVariantOptionChild : productVariantOptionChildList) {
                    if (productVariantOptionChild.isCustomVariant()) {
                        hasCustomVariantLv2 = true;
                        listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2_CUSTOM);
                        break;
                    }
                }
                if (!hasCustomVariantLv2) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2);
                }
            }
        }

        if (listOfFields.isEmpty()) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_EMPTY);
        }

        return listOfFields;
    }

    public static List<String> getAllInvalidateDataFieldFromViewModel(ProductViewModel viewModel){
        List<String> listInvalidDataField = new ArrayList<>();
        if (TextUtils.isEmpty(viewModel.getProductName())) {
            listInvalidDataField.add(FIELDS_MANDATORY_PRODUCT_NAME);
        }
        if (viewModel.getProductCategory().getCategoryId() <= 0) {
            listInvalidDataField.add(FIELDS_MANDATORY_CATEGORY);
        }
        if (viewModel.getProductPrice() <= 0) {
            listInvalidDataField.add(FIELDS_MANDATORY_PRICE);
        }
        if (viewModel.getProductWeight() <= 0) {
            listInvalidDataField.add(FIELDS_MANDATORY_WEIGHT);
        }
        if (!viewModel.isProductStatusActive()){
            listInvalidDataField.add(FIELDS_MANDATORY_STOCK_STATUS);
        }
        return listInvalidDataField;
    }
}
