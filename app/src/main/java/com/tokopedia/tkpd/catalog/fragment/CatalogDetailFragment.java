package com.tokopedia.tkpd.catalog.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.view.ExpandableTextView;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.LinePageIndicator;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.catalog.adapter.CatalogImageAdapter;
import com.tokopedia.tkpd.catalog.adapter.CatalogSpecAdapterHelper;
import com.tokopedia.tkpd.catalog.listener.CatalogImageTouchListener;
import com.tokopedia.tkpd.catalog.listener.ICatalogActionFragment;
import com.tokopedia.tkpd.catalog.listener.IDetailCatalogView;
import com.tokopedia.tkpd.catalog.model.CatalogImage;
import com.tokopedia.tkpd.catalog.model.CatalogInfo;
import com.tokopedia.tkpd.catalog.model.CatalogReview;
import com.tokopedia.tkpd.catalog.model.CatalogSpec;
import com.tokopedia.tkpd.catalog.presenter.CatalogDetailPresenter;
import com.tokopedia.tkpd.catalog.presenter.ICatalogDetailPresenter;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.product.model.share.ShareData;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author anggaprasetiyo on 10/17/16.
 */
public class CatalogDetailFragment extends BasePresenterFragment<ICatalogDetailPresenter>
        implements IDetailCatalogView {
    private static final String ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID";
    private static final String STATE_CATALOG_INFO = "STATE_CATALOG_INFO";
    private static final String STATE_CATALOG_REVIEW = "STATE_CATALOG_REVIEW";
    private static final String STATE_CATALOG_IMAGE_LIST = "STATE_CATALOG_IMAGE_LIST";
    private static final String STATE_CATALOG_DESC = "STATE_CATALOG_DESC";
    private static final String STATE_CATALOG_SPEC_LIST = "STATE_CATALOG_SPEC_LIST";
    private static final String STATE_CATALOG_SHARE_DATA = "STATE_CATALOG_SHARE_DATA";

    @Bind(R.id.holder_btn_buy)
    View holderCatalogBtnBuy;
    @Bind(R.id.btn_buy)
    TextView btnBuy;
    @Bind(R.id.holder_header_info)
    View holderCatalogHeaderInfo;
    @Bind(R.id.tv_catalog_name)
    TextView tvCatalogName;
    @Bind(R.id.tv_catalog_price)
    TextView tvCatalogPrice;
    @Bind(R.id.holder_catalog_desc)
    View holderCatalogDesc;
    @Bind(R.id.expand_text_catalog_desc)
    ExpandableTextView tvCatalogDesc;
    @Bind(R.id.holder_catalog_image)
    View holderCatalogImage;
    @Bind(R.id.vp_catalog_image)
    ViewPager vpCatalogImage;
    @Bind(R.id.indicator)
    LinePageIndicator indicator;
    @Bind(R.id.holder_catalog_review)
    View holderReview;
    @Bind(R.id.tv_review_desc)
    TextView tvReviewDesc;
    @Bind(R.id.tv_review_name)
    TextView tvReviewName;
    @Bind(R.id.tv_review_score)
    TextView tvReviewScore;
    @Bind(R.id.iv_review_logo)
    ImageView ivReviewLogo;
    @Bind(R.id.holder_catalog_spec)
    View holderCatalogSpec;
    @Bind(R.id.catalog_spec_list)
    RecyclerView rvCatalogSpec;
    private String catalogId;
    private ICatalogActionFragment catalogActionFragment;
    private TkpdProgressDialog progressDialog;

    private ShareData stateShareData;
    private CatalogInfo stateCatalogInfo;
    private CatalogReview stateCatalogReview;
    private List<CatalogImage> stateCatalogImageList;
    private String stateCatalogDesc;
    private List<CatalogSpec> stateCatalogSpecList;


    /**
     * instance new CatalogDetailFragment
     *
     * @param catalogId catalog id untuk get detail catalog ke ws
     * @return jelas CatalogDetailFragment doong
     */
    public static CatalogDetailFragment newInstance(String catalogId) {
        CatalogDetailFragment fragment = new CatalogDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.processGetCatalogDetailData(getActivity(), catalogId);
    }

    @Override
    public void onSaveState(Bundle state) {
        saveStateData(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        restoreStateData(savedState);

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new CatalogDetailPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        catalogActionFragment = (ICatalogActionFragment) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        catalogId = arguments.getString(ARG_EXTRA_CATALOG_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_catalog_detail;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(),
                TkpdProgressDialog.MAIN_PROGRESS, getView());
        progressDialog.setLoadingViewId(R.id.include_loading);
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
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
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
    public void renderCatalogInfo(CatalogInfo catalogInfo) {
        this.stateCatalogInfo = catalogInfo;
        holderCatalogHeaderInfo.setVisibility(View.VISIBLE);
        tvCatalogName.setText(catalogInfo.getCatalogName());
        tvCatalogPrice.setText(catalogInfo.getCatalogPrice().getPriceMin()
                + " - " + catalogInfo.getCatalogPrice().getPriceMax());
    }

    @Override
    public void renderCatalogReview(CatalogReview catalogReview) {
        this.stateCatalogReview = catalogReview;
        holderReview.setVisibility(View.VISIBLE);
        tvReviewDesc.setText(Html.fromHtml(catalogReview.getReviewDescription()));
        tvReviewName.setText(catalogReview.getReviewFrom());
        tvReviewScore.setText(MessageFormat.format("Nilai : {0}", catalogReview.getReviewRating()));
        ImageHandler.LoadImage(ivReviewLogo, catalogReview.getReviewFromImage());
    }

    @Override
    public void renderCatalogImage(final List<CatalogImage> catalogImageList) {
        this.stateCatalogImageList = catalogImageList;
        holderCatalogImage.setVisibility(View.VISIBLE);
        vpCatalogImage.setAdapter(new CatalogImageAdapter(getActivity(), catalogImageList));
        indicator.setViewPager(vpCatalogImage);
        vpCatalogImage.setOnTouchListener(new CatalogImageTouchListener(
                new CatalogImageTouchListener.IActionTouch() {
                    @Override
                    public void onCatalogImageClicked() {
                        presenter.processShowCatalogImageFullScreen(getActivity(),
                                vpCatalogImage.getCurrentItem(), catalogImageList);
                    }
                }));
    }

    @Override
    public void renderCatalogDescription(final String catalogDesc) {
        this.stateCatalogDesc = catalogDesc;
        holderCatalogDesc.setVisibility(View.VISIBLE);
        tvCatalogDesc.setText(catalogDesc);
    }

    @Override
    public void renderCatalogSpec(List<CatalogSpec> catalogSpecList) {
        this.stateCatalogSpecList = catalogSpecList;
        holderCatalogSpec.setVisibility(View.VISIBLE);
        CatalogSpecAdapterHelper sectionCatalogSpecHelper = new CatalogSpecAdapterHelper(
                getActivity(), rvCatalogSpec
        );
        for (CatalogSpec catalogSpec : catalogSpecList) {
            sectionCatalogSpecHelper.addSection(catalogSpec.getSpecHeader(),
                    new ArrayList<>(catalogSpec.getSpecChildList()));
        }
        sectionCatalogSpecHelper.notifyDataSetChanged();
    }

    @Override
    public void renderButtonBuy() {
        holderCatalogBtnBuy.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderCatalogShareData(ShareData shareData) {
        this.stateShareData = shareData;
        if (catalogActionFragment != null)
            catalogActionFragment.deliverCatalogShareData(stateShareData);
    }

    @Override
    public void renderErrorGetDetailCatalogData(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processGetCatalogDetailData(getActivity(), catalogId);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void renderErrorTimeoutConnectionGetDetailCatalogData(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processGetCatalogDetailData(getActivity(), catalogId);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void renderErrorNoConnectionGetDetailCatalogData(String message) {
        NetworkErrorHelper.showDialogCustomMSG(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processGetCatalogDetailData(getActivity(), catalogId);
                    }
                }, message);
    }

    @Override
    public void showMainProcessLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideMainProcessLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void cleanAllContent() {
        holderCatalogBtnBuy.setVisibility(View.GONE);
        holderCatalogDesc.setVisibility(View.GONE);
        holderCatalogHeaderInfo.setVisibility(View.GONE);
        holderCatalogSpec.setVisibility(View.GONE);
        holderCatalogImage.setVisibility(View.GONE);
        holderReview.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_buy)
    void actionBuy() {
        if (catalogActionFragment != null)
            catalogActionFragment.navigateToCatalogProductList(catalogId);
    }

    private void saveStateData(Bundle state) {
        if (isCompletedDataState()) {
            state.putParcelable(STATE_CATALOG_INFO, stateCatalogInfo);
            state.putParcelable(STATE_CATALOG_REVIEW, stateCatalogReview);
            state.putString(STATE_CATALOG_DESC, stateCatalogDesc);
            state.putParcelableArrayList(STATE_CATALOG_IMAGE_LIST,
                    new ArrayList<Parcelable>(stateCatalogImageList));
            state.putParcelableArrayList(STATE_CATALOG_SPEC_LIST,
                    new ArrayList<Parcelable>(stateCatalogSpecList));
            state.putParcelable(STATE_CATALOG_SHARE_DATA, stateShareData);
        }
    }

    private void restoreStateData(Bundle savedState) {
        if (!savedState.isEmpty()) {
            stateCatalogSpecList = savedState.getParcelableArrayList(STATE_CATALOG_SPEC_LIST);
            stateCatalogImageList = savedState.getParcelableArrayList(STATE_CATALOG_IMAGE_LIST);
            stateCatalogInfo = savedState.getParcelable(STATE_CATALOG_INFO);
            stateCatalogDesc = savedState.getString(STATE_CATALOG_DESC);
            stateCatalogReview = savedState.getParcelable(STATE_CATALOG_REVIEW);
            stateShareData = savedState.getParcelable(STATE_CATALOG_SHARE_DATA);
        }
        if (isCompletedDataState()) renderAllStateData();
        else presenter.processGetCatalogDetailData(getActivity(), catalogId);
    }

    private void renderAllStateData() {
        renderButtonBuy();
        renderCatalogShareData(stateShareData);
        renderCatalogInfo(stateCatalogInfo);
        renderCatalogDescription(stateCatalogDesc);
        renderCatalogImage(stateCatalogImageList);
        renderCatalogSpec(stateCatalogSpecList);
        if (stateCatalogReview != null) renderCatalogReview(stateCatalogReview);
    }

    private boolean isCompletedDataState() {
        return stateCatalogDesc != null && stateCatalogInfo != null && stateCatalogImageList != null
                && stateCatalogSpecList != null && stateShareData != null;
    }

}
