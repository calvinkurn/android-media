package com.tokopedia.discovery.catalog.fragment;

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
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.catalog.adapter.CatalogImageAdapter;
import com.tokopedia.discovery.catalog.adapter.CatalogSpecAdapterHelper;
import com.tokopedia.discovery.catalog.listener.CatalogImageTouchListener;
import com.tokopedia.discovery.catalog.listener.IDetailCatalogView;
import com.tokopedia.discovery.catalog.model.CatalogImage;
import com.tokopedia.discovery.catalog.model.CatalogInfo;
import com.tokopedia.discovery.catalog.model.CatalogReview;
import com.tokopedia.discovery.catalog.model.CatalogSpec;
import com.tokopedia.discovery.catalog.presenter.CatalogDetailPresenter;
import com.tokopedia.discovery.catalog.presenter.ICatalogDetailPresenter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

    @BindView(R2.id.holder_btn_buy)
    View holderCatalogBtnBuy;
    @BindView(R2.id.btn_buy)
    TextView btnBuy;
    @BindView(R2.id.holder_header_info)
    View holderCatalogHeaderInfo;
    @BindView(R2.id.tv_catalog_name)
    TextView tvCatalogName;
    @BindView(R2.id.tv_catalog_price)
    TextView tvCatalogPrice;
    @BindView(R2.id.holder_catalog_desc)
    View holderCatalogDesc;
    @BindView(R2.id.expand_text_catalog_desc)
    ExpandableTextView tvCatalogDesc;
    @BindView(R2.id.holder_catalog_image)
    View holderCatalogImage;
    @BindView(R2.id.vp_catalog_image)
    ViewPager vpCatalogImage;
    @BindView(R2.id.indicator)
    LinePageIndicator indicator;
    @BindView(R2.id.holder_catalog_review)
    View holderReview;
    @BindView(R2.id.tv_review_desc)
    TextView tvReviewDesc;
    @BindView(R2.id.tv_review_name)
    TextView tvReviewName;
    @BindView(R2.id.tv_review_score)
    TextView tvReviewScore;
    @BindView(R2.id.iv_review_logo)
    ImageView ivReviewLogo;
    @BindView(R2.id.holder_catalog_spec)
    View holderCatalogSpec;
    @BindView(R2.id.catalog_spec_list)
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
    protected String getScreenName() {
        return null;
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
        tvReviewDesc.setText(MethodChecker.fromHtml(catalogReview.getReviewDescription()));
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

    @OnClick(R2.id.btn_buy)
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
