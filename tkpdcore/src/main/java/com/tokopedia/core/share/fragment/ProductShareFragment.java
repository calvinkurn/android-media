package com.tokopedia.core.share.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultSender;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.ProductDB_Table;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.presenter.ProductSharePresenter;
import com.tokopedia.core.share.presenter.ProductSharePresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 * Modified by Alvarisi on 17/12/2016
 */
public class ProductShareFragment extends BasePresenterFragment<ProductSharePresenter> {
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
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    public static ProductShareFragment newInstance(@NonNull ShareData shareData) {
        ProductShareFragment fragment = new ProductShareFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_SHARE_DATA, shareData);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * added for add product from product share
     *
     * @param type
     * @param productId
     * @param stockStatus
     * @return
     */
    public static ProductShareFragment newInstance(@NonNull int type, @NonNull long productId, @NonNull String stockStatus) {
        ProductShareFragment fragment = new ProductShareFragment();
        Bundle args = new Bundle();
        args.putInt(ProductService.TYPE, type);
        args.putLong(ProductService.PRODUCT_DATABASE_ID, productId);
        args.putString(ProductService.STOCK_STATUS, stockStatus);
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

        int type = arguments.getInt(ProductService.TYPE, -1);
        long productId = arguments.getLong(ProductService.PRODUCT_DATABASE_ID, -1);
        String stockStatus = arguments.getString(ProductService.STOCK_STATUS, "");
        // if there is product need to be uploaded
        if (type != -1 && productId != -1 && !stockStatus.equals("")) {
            ((DownloadResultSender) getActivity()).sendDataToInternet(type, arguments);
        }
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
                switch (shareData.getType()) {
                    case ShareData.CATALOG_TYPE:
                        tvTitle.setText("Bagikan Katalog Ini!");
                        break;
                    case ShareData.SHOP_TYPE:
                        tvTitle.setText("Bagikan Toko Ini!");
                        break;
                    case ShareData.HOTLIST_TYPE:
                        tvTitle.setText("Bagikan Hotlist Ini!");
                        break;
                    case ShareData.DISCOVERY_TYPE:
                        tvTitle.setText("Bagikan Pencarian Ini!");
                        break;
                    case ShareData.PRODUCT_TYPE:
                        tvTitle.setText("Bagikan Produk Ini!");
                        break;
                }
            }
        }
    }

    public void onError(int type, Bundle resultData) {
        switch (type) {
            case ProductService.ADD_PRODUCT:
            case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                String messageError = resultData.getString(ProductService.MESSAGE_ERROR_FLAG, ProductService.INVALID_MESSAGE_ERROR);
                if (!messageError.equals(ProductService.INVALID_MESSAGE_ERROR)) {
                    progressBar.setVisibility(View.GONE);
                    errorImage.setVisibility(View.VISIBLE);
                    loadingAddProduct.setText(messageError + "\n" +getString(R.string.error_failed_add_product));
                    loadingAddProduct.setVisibility(View.VISIBLE);
                    setIconShareVisibility(View.GONE);
                    setVisibilityTitle(View.GONE);
                }
                break;
        }

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

    public void setData(int type, Bundle data) {
        switch (type) {
            case ProductService.ADD_PRODUCT:
            case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                final long productServerId = data.getLong(ProductService.PRODUCT_DATABASE_ID);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProductDB ProductDB = new Select()
                                .from(ProductDB.class)
                                .where(ProductDB_Table.productId.is((int) productServerId))
                                .querySingle();
                        if (ProductDB!= null && ProductDB.getImages() != null)
                            ProductDB.setPictureDBs(ProductDB.getImages());
                        if (ProductDB!= null && ProductDB.getWholeSales() != null)
                            ProductDB.setWholesalePriceDBs(ProductDB.getWholeSales());
                        shareData = new ShareData();
                        if (ProductDB!= null && ProductDB.getNameProd() != null)
                            shareData.setName(ProductDB.getNameProd());

                        if (ProductDB!= null && ProductDB.getPictureDBs()!= null
                                && CommonUtils.checkCollectionNotNull(ProductDB.getPictureDBs()))
                            shareData.setImgUri(ProductDB.getPictureDBs().get(0).getPath());

                        if (ProductDB!= null && ProductDB.getProductUrl() != null)
                            shareData.setUri(ProductDB.getProductUrl());
                        if (ProductDB!= null && ProductDB.getDescProd() != null)
                            shareData.setDescription(ProductDB.getDescProd());
                        if (ProductDB!= null)
                            shareData.setPrice(ProductDB.getPriceProd() + "");
                    }
                }, 100);//[END] move to manage product
                break;
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

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.card_share_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_close) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId()==R.id.home) {
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

    public void showDialogShareFb() {
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new
                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        SnackbarManager.make(getActivity(),getString(R.string.success_share_product)
                                , Snackbar.LENGTH_SHORT).show();
                        presenter.setFacebookCache();
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.i(TAG, "onError: "+error);
                    }
                });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setImageUrl(Uri.parse(shareData.getImgUri()))
                    .setContentTitle(shareData.getName())
                    .setContentDescription(shareData.getUri())
                    .setContentUrl(Uri.parse(shareData.getUri()))
                    .setQuote(shareData.getDescription())
                    .build();

            shareDialog.show(linkContent);
        }
    }
}
