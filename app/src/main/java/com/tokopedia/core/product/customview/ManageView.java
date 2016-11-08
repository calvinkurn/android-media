package com.tokopedia.core.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.prototype.ProductCache;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class ManageView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ManageView.class.getSimpleName();

    @Bind(R2.id.tv_promote)
    TextView tvPromote;
    @Bind(R2.id.tv_edit)
    TextView tvEdit;
    @Bind(R2.id.tv_sold_out)
    TextView tvSoldOut;
    @Bind(R2.id.tv_to_etalase)
    TextView tvToEtalase;
    private ProductDetailData data;

    public ManageView(Context context) {
        super(context);
    }

    public ManageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_manage_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        switch (data.getInfo().getProductStatus()) {
            case "1":
                hideToEtalase();
                break;
            case "3":
                hideToWareHouse();
                break;
            case "-1":
                hideAll();
                break;
        }
        tvPromote.setVisibility(data.getShopInfo().getShopStatus() != 1 ? GONE : VISIBLE);
        setVisibility(data.getShopInfo().getShopIsOwner() == 1
                || data.getShopInfo().getShopIsAllowManage() == 1 ? View.VISIBLE : GONE);
        tvPromote.setOnClickListener(new PromoteClick(data));
        tvEdit.setOnClickListener(new EditClick(data));
        tvToEtalase.setOnClickListener(new ToEtalaseClick(data));
        tvSoldOut.setOnClickListener(new SoldOutClick(data));
    }

    private void getProductPermission(final ProductDetailData data) {
        this.data = data;
        final RetrofitInteractor interactor = new RetrofitInteractorImpl();
        interactor.getProductManagePermissions(getContext(),
                NetworkParam.paramCheckPermission(data),
                new RetrofitInteractor.ProductManagePermissionListener() {
                    @Override
                    public void onSuccess(String productManager) {
                        ProductCache.SetPermission(data.getInfo().getProductId().toString(),
                                productManager, getContext());
                        setVisibility(VISIBLE);
                        interactor.unSubscribeObservable();
                    }

                    @Override
                    public void onError() {
                        setVisibility(GONE);
                        interactor.unSubscribeObservable();
                    }
                });
    }

    public void hideToEtalase() {
        tvToEtalase.setVisibility(GONE);
        tvSoldOut.setVisibility(VISIBLE);
        tvPromote.setVisibility(VISIBLE);
    }

    public void hideToWareHouse() {
        tvToEtalase.setVisibility(VISIBLE);
        tvSoldOut.setVisibility(GONE);
        tvPromote.setVisibility(GONE);
    }

    public void hideAll() {
        tvToEtalase.setVisibility(GONE);
        tvSoldOut.setVisibility(GONE);
        tvPromote.setVisibility(GONE);
        tvEdit.setVisibility(GONE);
    }

    private class PromoteClick implements OnClickListener {
        private final ProductDetailData data;

        public PromoteClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductManagePromoteClicked(data);
        }
    }

    private class EditClick implements OnClickListener {
        private final ProductDetailData data;

        public EditClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("is_edit", true);
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            listener.onProductManageEditClicked(bundle);
        }
    }

    private class ToEtalaseClick implements OnClickListener {
        private final ProductDetailData data;

        public ToEtalaseClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (data.getProductImages().isEmpty()) {
                listener.showToastMessage("Gambar harus diisi");
            } else {
                if (data.getProductImages().get(0).getImageSrc300()
                        .contains("android.resource://" + BuildConfig.APPLICATION_ID +
                                "/drawable/product_no_photo_default")) {
                    listener.showToastMessage("Gambar harus diisi");
                } else {
                    listener.onProductManageToEtalaseClicked(data.getInfo().getProductId());
                }
            }
        }
    }

    private class SoldOutClick implements OnClickListener {
        private final ProductDetailData data;

        public SoldOutClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductManageSoldOutClicked(data.getInfo().getProductId());
        }
    }
}
