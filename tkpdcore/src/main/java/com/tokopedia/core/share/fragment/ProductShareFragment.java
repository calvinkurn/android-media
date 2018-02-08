package com.tokopedia.core.share.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.share.listener.ShareView;
import com.tokopedia.core.share.presenter.ProductSharePresenter;
import com.tokopedia.core.share.presenter.ProductSharePresenterImpl;
import com.tokopedia.core.var.TkpdState;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.IS_ADDING_PRODUCT;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 * Modified by Alvarisi on 17/12/2016
 */
public class ProductShareFragment extends BasePresenterFragment<ProductSharePresenter> implements ShareView {
    public static final String TAG = "ProductShareFragment";
    private static final String ARGS_SHARE_DATA = "ARGS_SHARE_DATA";


    private ShareData shareData;
    @BindView(R2.id.text_line)
    TextView tvTitle;

    @BindView(R2.id.image_error)
    ImageView errorImage;

    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R2.id.loading_add_product)
    TextView loadingAddProduct;

    @BindView(R2.id.bbm_share)
    TextView bbmShare;

    @BindView(R2.id.whatsapp_share)
    TextView whatsappShare;

    @BindView(R2.id.line_share)
    TextView lineShare;

    @BindView(R2.id.instagram_share)
    TextView instagramShare;

    @BindView(R2.id.facebook_share)
    TextView facebookShare;

    @BindView(R2.id.twitter_share)
    TextView twitterShare;

    @BindView(R2.id.pinterest_share)
    TextView pinterestShare;

    @BindView(R2.id.google_plus_share)
    TextView gplusShare;

    @BindView(R2.id.copy_url)
    TextView copyUrl;

    @BindView(R2.id.more_share)
    TextView moreShare;

    @BindView(R2.id.text_subtitle)
    TextView subtitle;
    private BroadcastReceiver addProductReceiver;
    private boolean isAdding = false;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    public static ProductShareFragment newInstance(@NonNull ShareData shareData, boolean isAddingProduct) {
        ProductShareFragment fragment = new ProductShareFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_SHARE_DATA, shareData);
        args.putBoolean(IS_ADDING_PRODUCT, isAddingProduct);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * added for add product from product share
     *
     * @return
     */
    public static ProductShareFragment newInstance(boolean isAddingProduct) {
        ProductShareFragment fragment = new ProductShareFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_ADDING_PRODUCT, isAddingProduct);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        if (shareData != null) state.putParcelable(ARGS_SHARE_DATA, shareData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (shareData == null) savedState.getParcelable(ARGS_SHARE_DATA);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductSharePresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.shareData = arguments.getParcelable(ARGS_SHARE_DATA);
        this.isAdding = arguments.getBoolean(ProductDetailRouter.IS_ADDING_PRODUCT);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_card_share_prod;
    }

    @Override
    protected void initView(View view) {
        getActivity().invalidateOptionsMenu();
        progressBar.setVisibility(View.GONE);
        loadingAddProduct.setVisibility(View.GONE);
        if (this.shareData != null) {
            if (shareData.getType() != null) {
                subtitle.setText(R.string.product_share_subtitle);
                switch (shareData.getType()) {
                    case ShareData.CATALOG_TYPE:
                        tvTitle.setText(R.string.product_share_catalog);
                        break;
                    case ShareData.SHOP_TYPE:
                        tvTitle.setText(R.string.product_share_shop);
                        break;
                    case ShareData.HOTLIST_TYPE:
                        tvTitle.setText(R.string.product_share_hotlist);
                        break;
                    case ShareData.DISCOVERY_TYPE:
                        tvTitle.setText(R.string.product_share_search);
                        break;
                    case ShareData.PRODUCT_TYPE:
                        tvTitle.setText(R.string.product_share_product);
                        break;
                    case ShareData.RIDE_TYPE:
                        tvTitle.setText(R.string.product_share_ride_trip);
                        break;
                    case ShareData.APP_SHARE_TYPE:
                        tvTitle.setText(R.string.product_share_app);
                        subtitle.setText(R.string.product_share_app_subtitle);
                        break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(addProductReceiver, new IntentFilter(TkpdState.ProductService.BROADCAST_ADD_PRODUCT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(addProductReceiver);
    }

    public void onError(Bundle resultData) {
        String messageError = resultData.getString(TkpdState.ProductService.MESSAGE_ERROR_FLAG);
        progressBar.setVisibility(View.GONE);
        errorImage.setVisibility(View.VISIBLE);
        loadingAddProduct.setText(messageError +
                "\n" + getString(R.string.error_failed_add_product));
        loadingAddProduct.setVisibility(View.VISIBLE);
        setIconShareVisibility(View.GONE);
        setVisibilityTitle(View.GONE);
    }

    private void setIconShareVisibility(int visibility) {
        bbmShare.setVisibility(visibility);
        whatsappShare.setVisibility(visibility);
        lineShare.setVisibility(visibility);
        instagramShare.setVisibility(visibility);
        facebookShare.setVisibility(visibility);
        twitterShare.setVisibility(visibility);
        pinterestShare.setVisibility(visibility);
        gplusShare.setVisibility(visibility);
        copyUrl.setVisibility(visibility);
        moreShare.setVisibility(visibility);
    }

    private void setVisibilityTitle(int visibility) {
        tvTitle.setVisibility(visibility);
        subtitle.setVisibility(visibility);
    }

    public void setData(Bundle data) {
        shareData = new ShareData();
        shareData.setType(ShareData.PRODUCT_TYPE);
        String productName = data.getString(TkpdState.ProductService.PRODUCT_NAME);
        if (StringUtils.isNotBlank(productName)) {
            shareData.setName(productName);
        }
        String imageUri = data.getString(TkpdState.ProductService.IMAGE_URI);
        if (StringUtils.isNotBlank(imageUri)) {
            shareData.setImgUri(imageUri);
        }
        String productDescription = data.getString(TkpdState.ProductService.PRODUCT_DESCRIPTION);
        if (StringUtils.isNotBlank(productDescription)) {
            shareData.setDescription(productDescription);
        }
        String productUri = data.getString(TkpdState.ProductService.PRODUCT_URI);
        if (StringUtils.isNotBlank(productUri)) {
            shareData.setUri(productUri);
        }
        String productId = data.getString(TkpdState.ProductService.PRODUCT_ID);
        if (StringUtils.isNotBlank(productId)) {
            shareData.setId(productId);
        }
    }

    public void addingProduct(boolean isAdding) {
        int visibility;
        if (isAdding) {
            progressBar.setVisibility(View.VISIBLE);
            loadingAddProduct.setVisibility(View.VISIBLE);
            visibility = View.GONE;
        } else {
            progressBar.setVisibility(View.GONE);
            loadingAddProduct.setVisibility(View.GONE);
            visibility = View.VISIBLE;
        }
        setIconShareVisibility(visibility);
    }

    @Override
    protected void setViewListener() {
        addingProduct(isAdding);
        addProductReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                int status = bundle.getInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
                switch (status) {
                    case TkpdState.ProductService.STATUS_DONE:
                        setData(bundle);
                        addingProduct(false);
                        break;
                    case TkpdState.ProductService.STATUS_ERROR:
                    default:
                        addingProduct(false);
                        onError(bundle);
                }
            }
        };
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R2.id.bbm_share)
    void shareBBM() {
        presenter.shareBBM(shareData);
    }

    @OnClick(R2.id.whatsapp_share)
    void shareWhatsApp() {
        presenter.shareWhatsApp(shareData);
    }

    @OnClick(R2.id.line_share)
    void shareLine() {
        presenter.shareLine(shareData);
    }

    @OnClick(R2.id.instagram_share)
    void shareInstagram() {
        presenter.shareInstagram(shareData);
    }

    @OnClick(R2.id.facebook_share)
    void shareFb() {
        presenter.shareFb(shareData);
    }

    @OnClick(R2.id.twitter_share)
    void shareTwitter() {
        presenter.shareTwitter(shareData);
    }

    @OnClick(R2.id.pinterest_share)
    void sharePinterest() {
        presenter.sharePinterest(shareData);
    }

    @OnClick(R2.id.google_plus_share)
    void shareGPlus() {
        presenter.shareGPlus(shareData);
    }

    @OnClick(R2.id.copy_url)
    void shareCopy() {
        presenter.shareCopy(shareData);
    }

    @OnClick(R2.id.more_share)
    void shareMore() {
        presenter.shareMore(shareData);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showDialogShareFb(String shortUrl) {
        if (!isAdded()) {
            return;
        }
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new
                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        SnackbarManager.make(
                                getActivity(),
                                getString(R.string.success_share_product),
                                Snackbar.LENGTH_SHORT).show();
                        presenter.setFacebookCache();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i(TAG, "onError: " + error);
                    }
                });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            if (shareData != null && !TextUtils.isEmpty(shortUrl)) {
                ShareLinkContent.Builder linkBuilder = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shortUrl));
                if (!TextUtils.isEmpty(shareData.getName())) {
                    linkBuilder.setContentTitle(shareData.getName());
                }
                if (!TextUtils.isEmpty(shareData.getTextContent(getActivity()))) {
                    linkBuilder.setContentDescription(shareData.getTextContent(getActivity()));
                }
                if (!TextUtils.isEmpty(shareData.getDescription())) {
                    linkBuilder.setQuote(shareData.getDescription());
                }
                if (!TextUtils.isEmpty(shareData.getImgUri())) {
                    linkBuilder.setImageUrl(Uri.parse(shareData.getImgUri()));
                }
                ShareLinkContent linkContent = linkBuilder.build();
                shareDialog.show(linkContent);
                return;
            }
        }
        NetworkErrorHelper.showSnackbar(getActivity());
    }
}