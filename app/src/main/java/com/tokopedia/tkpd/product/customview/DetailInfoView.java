package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.tkpd.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductBreadcrumb;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class DetailInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = DetailInfoView.class.getSimpleName();

    @Bind({R2.id.tv_category_1, R2.id.tv_category_2, R2.id.tv_category_3})
    List<TextView> tvCategories;
    @Bind(R2.id.tv_weight)
    TextView tvWeight;
    @Bind(R2.id.tv_minimum)
    TextView tvMinOrder;
    @Bind(R2.id.tv_insurance)
    TextView tvInsurance;
    @Bind(R2.id.tv_catalog)
    TextView tvCatalog;
    @Bind(R2.id.tv_etalase)
    TextView tvEtalase;
    @Bind(R2.id.tv_condition)
    TextView tvCondition;
    @Bind(R2.id.tv_returnable)
    TextView tvReturnable;
    @Bind(R2.id.tr_catalog)
    TableRow catalogView;
    @Bind(R2.id.tv_preorder)
    TextView tvPreOrder;
    @Bind(R2.id.tr_returnable)
    TableRow returnableView;
    @Bind(R2.id.tr_preorder)
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
            tvCategories.get(i).setText(Html.fromHtml(productDepartments.get(i).getDepartmentName()));
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
            tvCatalog.setText(Html.fromHtml(data.getInfo().getProductCatalogName()));
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
            tvEtalase.setText(Html.fromHtml(data.getInfo().getProductEtalase()));
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
            bundle.putString(BrowseProductActivity.DEPARTMENT_ID, data.getDepartmentId());
            bundle.putInt(BrowseProductActivity.FRAGMENT_ID, ProductFragment.FRAGMENT_ID);
            bundle.putString(BrowseProductActivity.AD_SRC, TopAdsApi.SRC_DIRECTORY);
            bundle.putString(BrowseProductActivity.EXTRA_SOURCE, TopAdsApi.SRC_DIRECTORY);
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
            bundle.putString("etalase_name", Html.fromHtml(data.getInfo().getProductEtalase()).toString());
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
