package com.tokopedia.posapp.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.di.component.DaggerProductComponent;
import com.tokopedia.posapp.view.AddToCart;
import com.tokopedia.posapp.view.Product;
import com.tokopedia.posapp.view.activity.LocalCartActivity;
import com.tokopedia.posapp.view.activity.ReactInstallmentActivity;
import com.tokopedia.posapp.view.presenter.AddToCartPresenter;
import com.tokopedia.posapp.view.presenter.ProductPresenter;
import com.tokopedia.posapp.view.widget.HeaderInfoView;
import com.tokopedia.posapp.view.widget.InstallmentSimulationView;
import com.tokopedia.posapp.view.widget.PosAlertDialog;
import com.tokopedia.tkpdpdp.DescriptionActivity;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.customview.PictureView;
import com.tokopedia.tkpdpdp.customview.VideoDescriptionLayout;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/8/17.
 */

public class ProductDetailFragment extends BaseDaggerFragment
        implements Product.View, ProductDetailView, AddToCart.View {
    public static final String PRODUCT_PASS = "PRODUCT_PASS";

    private PictureView pictureView;
    private HeaderInfoView headerInfoView;
    private InstallmentSimulationView priceSimulationView;
    private VideoDescriptionLayout videoDescriptionLayout;
    private Button buttonBuy;
    private Button buttonAddToCart;

    private ProductPass productPass;
    private ProductDetailData productData;

    private ProductDetailFragmentListener listener;

    @Inject
    ProductPresenter productPresenter;

    @Inject
    AddToCartPresenter addToCartPresenter;

    Context context;

    public static ProductDetailFragment newInstance(Bundle bundle) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        productPass = getArguments().getParcelable(PRODUCT_PASS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initView(parentView);
        initListener();
        productPresenter.attachView(this);
        addToCartPresenter.attachView(this);
        return parentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ProductDetailFragmentListener) {
            listener = (ProductDetailFragmentListener) context;
        } else {
            throw new RuntimeException("Need to implement ProductDetailFragmentListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (productPass != null) {
            productPresenter.getProduct(productPass);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerProductComponent daggerProductComponent =
                (DaggerProductComponent) DaggerProductComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerProductComponent.inject(this);
    }

    @Override
    public void onSuccessGetProduct(ProductDetailData data) {
        this.productData = data;
        pictureView.renderData(data);
        headerInfoView.renderData(data);
        priceSimulationView.renderData(data);
        videoDescriptionLayout.renderData(data);
    }

    @Override
    public void onErrorAddToCart(String message) {
        CommonUtils.dumper(message);
    }

    @Override
    public void onSuccessAddToCart(String message) {
        AlertDialog dialog = new PosAlertDialog(getContext())
                .setTitle(getString(R.string.pdp_add_to_cart_title))
                .setMessage(getString(R.string.pdp_atc_success_message))
                .setPositiveButton(getString(R.string.pdp_pay_label), new PosAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface) {
                        goToPaymentCheckout();
                    }
                })
                .setNegativeButton(getString(R.string.pdp_continue_shopping_label), new PosAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();

        dialog.show();
    }

    @Override
    public void onSuccessATCPayment(String message) {
        goToPaymentCheckout();
    }

    private void goToPaymentCheckout() {
        startActivity(new Intent(getContext(), LocalCartActivity.class));
        getActivity().finish();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void closeView() {

    }

    @Override
    public void onProductDepartmentClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductCatalogClicked(@NonNull String catalogId) {

    }

    @Override
    public void onProductEtalaseClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductTalkClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductReviewClicked(String productId, String shopId, String productName) {

    }

    @Override
    public void onProductManagePromoteClicked(ProductDetailData productData) {

    }

    @Override
    public void onBuyClick(String source) {

    }

    @Override
    public void onImageZoomClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PreviewProductImageDetail.FILELOC, getImageURIPaths());
        bundle.putString("product_name", MethodChecker.fromHtml(productData.getInfo().getProductName()).toString());
        bundle.putString("product_price", MethodChecker.fromHtml(productData.getInfo().getProductPrice()).toString());
        bundle.putInt(PreviewProductImageDetail.IMG_POSITION, position);
        productPresenter.processToPicturePreview(context, bundle);
    }

    @Override
    public void onProductShopAvatarClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductOtherClicked(@NonNull ProductPass productPass) {

    }

    @Override
    public void onProductShopNameClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductManageToEtalaseClicked(int productId) {

    }

    @Override
    public void onProductManageEditClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onProductManageSoldOutClicked(int productId) {

    }

    @Override
    public void onProductShareClicked(@NonNull ShareData data) {

    }

    @Override
    public void onProductRatingClicked(String productId, String shopId, String productName) {

    }

    @Override
    public void onCourierClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onWholesaleClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onVariantClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onInstallmentClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, ReactInstallmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onDescriptionClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, DescriptionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onProductShopInfoError() {

    }

    @Override
    public void onProductStatusError() {

    }

    @Override
    public void onProductNewShopClicked() {

    }

    @Override
    public void onProductBuySessionLogin(@NonNull ProductCartPass data) {

    }

    @Override
    public void onProductBuySessionNotLogin(@NonNull Bundle bundle) {

    }

    @Override
    public void renderTempProductData(ProductPass productPass) {

    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData successResult) {

    }

    @Override
    public void onOtherProductLoaded(List<ProductOther> productOthers) {

    }

    @Override
    public void onProductShopMessageClicked(@NonNull Intent intent) {

    }

    @Override
    public void onProductHasEdited() {

    }

    @Override
    public void onProductTalkUpdated() {

    }

    @Override
    public void onShopFavoriteUpdated(int statFave) {

    }

    @Override
    public void onProductShopFaveClicked(String shopId, Integer productId) {

    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {

    }

    @Override
    public void finishLoadingWishList() {

    }

    @Override
    public void loadingWishList() {

    }

    @Override
    public void updateWishListStatus(int status) {

    }

    @Override
    public void loadVideo(VideoData data) {

    }

    @Override
    public void refreshMenu() {

    }

    @Override
    public void showProductDetailRetry() {

    }

    @Override
    public void showErrorVariant() {

    }

    @Override
    public void showProductOthersRetry() {

    }

    @Override
    public void showFaveShopRetry() {

    }

    @Override
    public void showWishListRetry(String errorMessage) {

    }

    @Override
    public void showPromoteRetry() {

    }

    @Override
    public void onNullData() {

    }

    @Override
    public void showReportDialog() {

    }

    @Override
    public void onProductReportClicked() {

    }

    @Override
    public void showTickerGTM(String message) {

    }

    @Override
    public void hideTickerGTM() {

    }

    @Override
    public void showFullScreenError() {

    }

    @Override
    public void moveToEditFragment(boolean isEdit, String productId) {

    }

    @Override
    public void showSuccessWishlistSnackBar() {

    }

    @Override
    public void showPromoWidget(PromoAttributes promoAttributes) {

    }

    @Override
    public void onPromoWidgetCopied() {

    }

    @Override
    public void showProductCampaign() {
        headerInfoView.renderProductCampaign(productData.getCampaign());
    }

    @Override
    public void showMostHelpfulReview(List<Review> reviews) {

    }

    @Override
    public void showLatestTalkView(LatestTalkViewModel discussion) {

    }

    @Override
    public void addProductVariant(ProductVariant productVariant) {

    }

    @Override
    public void actionSuccessAddToWishlist(Integer productId) {

    }

    @Override
    public void actionSuccessRemoveFromWishlist(Integer productId) {

    }

    @Override
    public void actionSuccessAddFavoriteShop(String shopId) {

    }

    @Override
    public void showDinkSuccess(String productName) {

    }

    @Override
    public void showDinkFailed(String productName, String expired) {

    }

    @Override
    public void onPromoAdsClicked() {

    }

    @Override
    public void restoreIsAppBarCollapsed(boolean isAppBarCollapsed) {

    }

    @Override
    public boolean isSellerApp() {
        return false;
    }

    @Override
    public void updateButtonBuyListener() {

    }

    private void initView(View view) {
        pictureView = view.findViewById(R.id.view_picture);
        headerInfoView = view.findViewById(R.id.view_header);
        priceSimulationView = view.findViewById(R.id.view_price_simulation);
        videoDescriptionLayout = view.findViewById(R.id.video_layout);
        buttonBuy = view.findViewById(R.id.button_buy);
        buttonAddToCart = view.findViewById(R.id.button_add_to_cart);
    }

    private void initListener() {
        pictureView.setListener(this);
        headerInfoView.setListener(this);
        priceSimulationView.setListener(this);
        videoDescriptionLayout.setListener(this);
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartPresenter.add(Integer.parseInt(productPass.getProductId()), 1);
                listener.onAddToCart();
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartPresenter.addAndCheckout(Integer.parseInt(productPass.getProductId()), 1);
            }
        });
    }

    public interface ProductDetailFragmentListener {
        void onAddToCart();
    }

    public ArrayList<String> getImageURIPaths() {
        ArrayList<String> imageSources = new ArrayList<>();
        for (ProductImage productImage : productData.getProductImages()) {
            imageSources.add(productImage.getImageSrc());
        }
//        if (productData.getInfo().getHasVariant() && productVariant!=null && productVariant.getChildren()!=null) {
//            for (Child child: productVariant.getChildren()) {
//                if (!TextUtils.isEmpty(child.getPicture().getOriginal()) && child.getProductId()!=productData.getInfo().getProductId()) {
//                    arrayList.add(child.getPicture().getOriginal());
//                }
//            }
//            Set<String> imagesSet = new LinkedHashSet<>(arrayList);
//            ArrayList<String> finalImage = new ArrayList<>();
//            finalImage.addAll(imagesSet);
//            return finalImage;
//        }
        return imageSources;
    }
}
