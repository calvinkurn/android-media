package com.tokopedia.core.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductBreadcrumb;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.MethodChecker;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class DetailInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = DetailInfoView.class.getSimpleName();

    @BindViews({R2.id.tv_category_1, R2.id.tv_category_2, R2.id.tv_category_3})
    List<TextView> tvCategories;
    @BindView(R2.id.tv_weight)
    TextView tvWeight;
    @BindView(R2.id.tv_minimum)
    TextView tvMinOrder;
    @BindView(R2.id.tv_insurance)
    TextView tvInsurance;
    @BindView(R2.id.tv_catalog)
    TextView tvCatalog;
    @BindView(R2.id.tv_etalase)
    TextView tvEtalase;
    @BindView(R2.id.tv_condition)
    TextView tvCondition;
    @BindView(R2.id.tv_returnable)
    TextView tvReturnable;
    @BindView(R2.id.tr_catalog)
    TableRow catalogView;
    @BindView(R2.id.tv_preorder)
    TextView tvPreOrder;
    @BindView(R2.id.tr_returnable)
    TableRow returnableView;
    @BindView(R2.id.tr_preorder)
    TableRow preOrderView;

    public DetailInfoView(Context context) {
        super(context);
    }

    public DetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_detail_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        for (TextView textView : tvCategories) {
            textView.setVisibility(GONE);
        }
        catalogView.setVisibility(GONE);
        returnableView.setVisibility(GONE);
        preOrderView.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        final List<ProductBreadcrumb> productDepartments = data.getBreadcrumb();

        if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                && !data.getPreOrder().getPreorderStatus().equals("0")
                && !data.getPreOrder().getPreorderProcessTime().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
            tvPreOrder.setText(String.format("%s %s %s", "Waktu Proses",
                    data.getPreOrder().getPreorderProcessTime(),
                    data.getPreOrder().getPreorderProcessTimeTypeString()));
            preOrderView.setVisibility(VISIBLE);
        }

        int length = data.getBreadcrumb().size() <= tvCategories.size() ?
                data.getBreadcrumb().size() : tvCategories.size();
        if (length < tvCategories.size()) {
            for (TextView textView : tvCategories) {
                textView.setVisibility(GONE);
            }
        }
        for (int i = 0; i < length; i++) {
            tvCategories.get(i).setText(MethodChecker.fromHtml(productDepartments.get(i).getDepartmentName()));
            tvCategories.get(i).setOnClickListener(new CategoryClick(productDepartments.get(i)));
            tvCategories.get(i).setVisibility(VISIBLE);
        }
        if (data.getInfo().getProductCatalogId() != null
                && data.getInfo().getProductCatalogName() != null
                && data.getInfo().getProductCatalogUrl() != null
                && !data.getInfo().getProductCatalogId().equals("")
                && !data.getInfo().getProductCatalogName().equals("")
                && !data.getInfo().getProductCatalogUrl().equals("")
                && !data.getInfo().getProductCatalogId().equals("0")
                && !data.getInfo().getProductCatalogName().equals("0")
                && !data.getInfo().getProductCatalogUrl().equals("0")) {
            catalogView.setVisibility(VISIBLE);
            tvCatalog.setText(MethodChecker.fromHtml(data.getInfo().getProductCatalogName()));
            tvCatalog.setOnClickListener(new CatalogClick(data));
        } else {
            catalogView.setVisibility(GONE);
        }
        showReturnable(data.getInfo().getProductReturnable(), data.getShopInfo().getShopHasTerms());

        tvWeight.setText(String.format("%s%s",
                data.getInfo().getProductWeight(),
                data.getInfo().getProductWeightUnit()));
        tvMinOrder.setText(data.getInfo().getProductMinOrder().replace(".",""));
        tvInsurance.setText(data.getInfo().getProductInsurance());
        if (data.getInfo().getProductEtalase() != null) {
            tvEtalase.setText(MethodChecker.fromHtml(data.getInfo().getProductEtalase()));
            tvEtalase.setOnClickListener(new EtalaseClick(data));
        }
        tvCondition.setText(data.getInfo().getProductCondition());

        setVisibility(VISIBLE);
    }

    private void showReturnable(int returnableState, int shopHasTerms) {
        if (shopHasTerms != 0) {
            switch (returnableState) {
                case 1:
                    tvReturnable.setText(getContext().getString(R.string.title_yes));
                    returnableView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvReturnable.setText(getContext().getString(R.string.title_no));
                    break;
                default:
                    returnableView.setVisibility(View.GONE);
                    tvReturnable.setText(getContext().getString(R.string.return_no_policy));
                    break;
            }
        } else {
            returnableView.setVisibility(View.GONE);
        }

    }

    private class CategoryClick implements OnClickListener {

        private final ProductBreadcrumb data;

        public CategoryClick(ProductBreadcrumb productBreadcrumb) {
            this.data = productBreadcrumb;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(BrowseProductRouter.DEPARTMENT_ID, data.getDepartmentId());
            bundle.putString(BrowseProductRouter.DEPARTMENT_NAME, data.getDepartmentName());
            bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
            bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_DIRECTORY);
            bundle.putString(BrowseProductRouter.EXTRA_SOURCE, TopAdsApi.SRC_DIRECTORY);
            listener.onProductDepartmentClicked(bundle);
        }
    }

    private class EtalaseClick implements OnClickListener {
        private final ProductDetailData data;

        public EtalaseClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("etalase_name", MethodChecker.fromHtml(data.getInfo().getProductEtalase()).toString());
            bundle.putString("etalase_id", data.getInfo().getProductEtalaseId());
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            listener.onProductEtalaseClicked(bundle);
        }
    }

    private class CatalogClick implements OnClickListener {
        private final ProductDetailData data;

        public CatalogClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductCatalogClicked(data.getInfo().getProductCatalogId());
        }
    }
}
