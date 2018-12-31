package com.tokopedia.posapp.product.productdetail.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartResult;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.posapp.PosApplication;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.cart.view.AddToCart;
import com.tokopedia.posapp.cart.view.activity.LocalCartActivity;
import com.tokopedia.posapp.cart.view.presenter.AddToCartPresenter;
import com.tokopedia.posapp.payment.process.ReactInstallmentActivity;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.common.di.DaggerProductComponent;
import com.tokopedia.posapp.product.common.di.ProductComponent;
import com.tokopedia.posapp.product.productdetail.view.Product;
import com.tokopedia.posapp.product.productdetail.view.activity.PosDescriptionActivity;
import com.tokopedia.posapp.product.productdetail.view.presenter.ProductPresenter;
import com.tokopedia.posapp.product.productdetail.view.widget.DescriptionView;
import com.tokopedia.posapp.product.productdetail.view.widget.HeaderInfoView;
import com.tokopedia.posapp.product.productdetail.view.widget.InstallmentSimulationView;
import com.tokopedia.posapp.product.productdetail.view.widget.PictureView;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductPicture;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.courier.CourierViewData;
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.viewmodel.AffiliateInfoViewModel;
import com.tokopedia.tkpdpdp.revamp.ProductViewData;

import java.util.ArrayList;
import java.util.List;

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
    private DescriptionView descriptionView;
    private Button buttonBuy;
    private Button buttonAddToCart;
    private LinearLayout buttonContainer;
    private ProgressBar progressBar;
    
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
            try{
                onSuccessGetProduct(responseToProductDetailData(getArguments().getString("extras")));
            } catch (Exception e){
                productPresenter.getProduct(productPass);
                e.printStackTrace();
            }
        }
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    private ProductDetailData responseToProductDetailData(String bundle) {

        ProductDetail productDetail = new Gson().fromJson(bundle, ProductDetail.class);

        ProductInfo info = new ProductInfo();
        info.setProductId((int)productDetail.getProductId());
        info.setProductName(productDetail.getProductName());
        info.setProductPrice(productDetail.getProductPrice());
        info.setProductPriceUnformatted((int)productDetail.getProductPriceUnformatted());
        info.setProductOriginalPrice((int)productDetail.getProductPriceOriginal());
        info.setProductDescription(productDetail.getProductDescription());

        List<ProductImage> productImages = new ArrayList<>();
        for(ProductPicture productPicture : productDetail.getPictures()){
            ProductImage image = new ProductImage();
            image.setImageSrc(productPicture.getUrlOriginal());
            image.setImageSrc300(productPicture.getUrlThumbnail());
            image.setImageId(productPicture.getPicId());
            productImages.add(image);
        }

        ProductDetailData data = new ProductDetailData();
        data.setInfo(info);
        data.setProductImages(productImages);

        return data;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        ProductComponent component = DaggerProductComponent.builder()
                .posAppComponent(((PosApplication) getActivity().getApplication()).getPosAppComponent())
                .build();
        component.inject(this);
    }

    @Override
    public void onSuccessGetProduct(ProductDetailData data) {
        this.productData = data;
        pictureView.renderData(data);
        headerInfoView.renderData(data);
        priceSimulationView.renderData(data);
        descriptionView.renderData(data);
        progressBar.setVisibility(View.GONE);
        pictureView.setVisibility(View.VISIBLE);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorAddToCart(String message) {
        CommonUtils.dumper(message);
    }

    @Override
    public void onSuccessAddToCart(String message) {
        CommonUtils.dumper(message);
        listener.onAddToCart();
    }

    @Override
    public void onSuccessATCPayment(String message) {
        goToPaymentCheckout();
    }

    private void goToPaymentCheckout() {
        startActivity(new Intent(getContext(), LocalCartActivity.class));
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
    public void onByMeClicked(AffiliateInfoViewModel affiliate, boolean isRegularPdp) {

    }


    @Override
    public void renderAffiliateButton(AffiliateInfoViewModel affiliate) {

    }

    @Override
    public void showErrorAffiliate(String message) {

    }

    @Override
    public void onWishlistCountLoaded(String wishlistCountText) {

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
    public void onProductShareClicked(@NonNull LinkerData data) {

    }

    @Override
    public void onProductRatingClicked(String productId, String shopId, String productName) {

    }

    @Override
    public void onCourierClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onCourierClicked(@NonNull String productId, @Nullable ArrayList<CourierViewData> arrayList) {

    }

    @Override
    public void onWholesaleClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void openVariantPage(int source) {

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
        Intent intent = new Intent(context, PosDescriptionActivity.class);
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
    public void onProductDetailLoaded(@NonNull ProductDetailData successResult, ProductViewData viewData) {

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
    public void moveToEditFragment(boolean isEdit) {

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
//        headerInfoView.renderProductCampaign(productData.getCampaign());
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
    public void setVariantFalse() {

    }

    @Override
    public void addProductStock(Child productStock) {

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
    public void loadPromo() {

    }

    @Override
    public boolean isSellerApp() {
        return false;
    }

    @Override
    public void renderAddToCartSuccess(AddToCartResult addToCartResult) {

    }

    @Override
    public void renderAddToCartSuccessOpenCart(AddToCartResult addToCartResult) {

    }

    @Override
    public void openLoginPage() {

    }

    @Override
    public int generateStateVariant(String source) {
        return 0;
    }

    @Override
    public void updateButtonBuyListener() {

    }

    @Override
    public void trackingEnhanceProductDetail() {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void onSuccesLoadRateEstimaion(RatesModel ratesModel) {

    }

    private void initView(View view) {
        pictureView = view.findViewById(R.id.view_picture);
        headerInfoView = view.findViewById(R.id.view_header);
        priceSimulationView = view.findViewById(R.id.view_price_simulation);
        descriptionView = view.findViewById(R.id.view_description);
        progressBar = view.findViewById(R.id.progress_bar);
        buttonBuy = view.findViewById(R.id.button_buy);
        buttonAddToCart = view.findViewById(R.id.button_add_to_cart);
        buttonContainer = view.findViewById(R.id.linear_button_container);
        buttonContainer.setVisibility(View.GONE);
    }

    private void initListener() {
        pictureView.setListener(this);
        headerInfoView.setListener(this);
        priceSimulationView.setListener(this);
        descriptionView.setListener(this);

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartPresenter.add(productData, 1);
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartPresenter.addAndCheckout(productData, 1);
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
        return imageSources;
    }

    @Override
    public void onProductBuySessionLogin(@NonNull ProductCartPass data, String source) {

    }

    @Override
    public void renderAddToCartSuccessOpenCheckout(AddToCartResult addToCartResult) {

    }

    @Override
    public boolean isFromExploreAffiliate() {
        return false;
    }

    @Override
    public void moveToEstimationDetail() {

    }
}
