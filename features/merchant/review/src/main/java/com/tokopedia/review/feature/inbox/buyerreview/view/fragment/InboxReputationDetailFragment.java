package com.tokopedia.review.feature.inbox.buyerreview.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.header.HeaderUnify;
import com.tokopedia.imagepreview.ImagePreviewActivity;
import com.tokopedia.review.R;
import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationDetailAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactoryImpl;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShareReviewDialog;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationDetailPresenter;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailPassModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ShareModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailFragment extends BaseDaggerFragment
        implements InboxReputationDetail.View, ReputationAdapter.ReputationListener {

    private static final int REQUEST_GIVE_REVIEW = 101;
    private static final int REQUEST_EDIT_REVIEW = 102;
    private static final int REQUEST_REPORT_REVIEW = 103;

    private static final int PUAS_SCORE = 2; // FROM API

    private RecyclerView listProduct;
    private SwipeToRefresh swipeToRefresh;
    private InboxReputationDetailAdapter adapter;
    private ShareReviewDialog shareReviewDialog;
    private CallbackManager callbackManager;
    private View mainView;

    private ProgressDialog progressDialog;

    @Inject
    InboxReputationDetailPresenter presenter;

    @Inject
    PersistentCacheManager persistentCacheManager;

    @Inject
    UserSessionInterface userSession;

    @Inject
    ReputationTracking reputationTracking;

    private String reputationId = "0";
    private String orderId = "0";
    private int role = 0;

    public static InboxReputationDetailFragment createInstance(int tab,
                                                               boolean isFromApplink,
                                                               String reputationId) {
        InboxReputationDetailFragment fragment = new InboxReputationDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        bundle.putBoolean(InboxReputationDetailActivity.ARGS_IS_FROM_APPLINK, isFromApplink);
        bundle.putString(InboxReputationDetailActivity.REPUTATION_ID, reputationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_DETAIL;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent = ((BaseMainApplication) requireContext().getApplicationContext()).getBaseAppComponent();
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .baseAppComponent(baseAppComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    private void initVar() {
        if (getArguments().getBoolean(InboxReputationDetailActivity.ARGS_IS_FROM_APPLINK, false)) {
            reputationId = getArguments().getString(InboxReputationDetailActivity.REPUTATION_ID, "0");
        } else if (persistentCacheManager != null) {
            try {
                InboxReputationDetailPassModel passModel = persistentCacheManager.get(
                    InboxReputationDetailActivity.CACHE_PASS_DATA,
                    InboxReputationDetailPassModel.class
                );
                reputationId = passModel.getReputationId();
                role = passModel.getRole();
                setToolbar(passModel.getInvoice(), passModel.getCreateTime());
            } catch (Exception e) {
                // Ignore cache expired exception
            }
        }

        callbackManager = CallbackManager.Factory.create();
        InboxReputationDetailTypeFactory typeFactory = new InboxReputationDetailTypeFactoryImpl
                (this);
        adapter = new InboxReputationDetailAdapter(typeFactory);
    }

    private void setToolbar(String title, String subtitle) {
        if (getActivity() != null) {
            if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }
            HeaderUnify toolbar = getActivity().findViewById(R.id.headerInboxReputationDetail);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setTitle(title);
            toolbar.setHeaderSubTitle(subtitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_detail, container,
                false);
        mainView = parentView.findViewById(R.id.main);
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_layout);
        listProduct = parentView.findViewById(R.id.product_list);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    @SuppressLint("WrongConstant")
    private void prepareView() {
        listProduct.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listProduct.setAdapter(adapter);
        swipeToRefresh.setOnRefreshListener(onRefresh());
        initProgressDialog();
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        };
    }

    private void initProgressDialog() {
        if(getContext() != null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("");
            progressDialog.setMessage(getContext().getString(R.string.progress_dialog_loading));
            progressDialog.setCancelable(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(reputationId)) {
            presenter.getInboxDetail(
                    reputationId,
                    getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1)
            );
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetInboxDetail(Throwable throwable) {
        if (getActivity() != null && mainView != null)
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, ReviewErrorHandler.getErrorMessage(getContext(), throwable),
                    () -> presenter.getInboxDetail(
                            reputationId,
                            getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1)
                    ));
    }

    @Override
    public void finishLoading() {
        if (progressDialog != null && getActivity() != null) {
            adapter.removeLoading();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessGetInboxDetail(InboxReputationItemUiModel inboxReputationItemUiModel,
                                        List<Visitable> list) {

        role = inboxReputationItemUiModel.getRole();
        if (!list.isEmpty() && list.get(0) instanceof InboxReputationDetailItemUiModel) {
            orderId = ((InboxReputationDetailItemUiModel) list.get(0)).getOrderId();
        }
        setToolbar(inboxReputationItemUiModel.getInvoice(), inboxReputationItemUiModel.getCreateTime());

        adapter.clearList();
        adapter.addHeader(createHeaderModel(inboxReputationItemUiModel));
        adapter.addList(list);
        adapter.notifyDataSetChanged();

        reputationTracking.onSeeSellerFeedbackPage(orderId);
    }

    @Override
    public void onErrorSendSmiley(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showLoadingDialog() {
        if (!progressDialog.isShowing() && getActivity() != null)
            progressDialog.show();
    }

    @Override
    public void finishLoadingDialog() {
        if (progressDialog.isShowing() && progressDialog != null && getContext() != null)
            progressDialog.dismiss();
    }

    @Override
    public void showRefresh() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void onErrorRefreshInboxDetail(Throwable throwable) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), ReviewErrorHandler.getErrorMessage(getContext(), throwable));
    }

    @Override
    public void onSuccessRefreshGetInboxDetail(InboxReputationItemUiModel inboxReputationViewModel,
                                               List<Visitable> list) {
        if (!list.isEmpty() && list.get(0) instanceof InboxReputationDetailItemUiModel) {
            orderId = ((InboxReputationDetailItemUiModel) list.get(0)).getOrderId();
        }
        adapter.clearList();
        adapter.addHeader(createHeaderModel(inboxReputationViewModel));
        adapter.addList(list);
        adapter.notifyDataSetChanged();
        getActivity().setResult(Activity.RESULT_OK);
    }

    private InboxReputationDetailHeaderUiModel createHeaderModel(
            InboxReputationItemUiModel inboxReputationViewModel) {
        return new InboxReputationDetailHeaderUiModel(
                inboxReputationViewModel.getRevieweePicture(),
                inboxReputationViewModel.getRevieweeName(),
                getTextDeadline(inboxReputationViewModel),
                inboxReputationViewModel.getReputationDataUiModel(),
                inboxReputationViewModel.getRole(),
                inboxReputationViewModel.getRevieweeBadgeCustomerUiModel(),
                inboxReputationViewModel.getRevieweeBadgeSellerUiModel(),
                inboxReputationViewModel.getShopId(),
                inboxReputationViewModel.getUserId());
    }

    private String getTextDeadline(InboxReputationItemUiModel element) {
        return getContext().getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                getContext().getString(R.string.deadline_suffix);
    }

    @Override
    public void finishRefresh() {
        swipeToRefresh.setRefreshing(false);

    }

    @Override
    public void goToPreviewImage(int position, ArrayList<ImageUpload> list) {
        ArrayList<String> listLocation = new ArrayList<>();
        ArrayList<String> listDesc = new ArrayList<>();

        for (ImageUpload image : list) {
            listLocation.add(image.getPicSrcLarge());
            listDesc.add(image.getDescription());
        }

        startActivity(ImagePreviewActivity.getCallingIntent(getContext(),
                listLocation,
                listDesc,
                position));
    }

    @Override
    public int getTab() {
        return getArguments().getInt(InboxReputationDetailActivity
                .ARGS_TAB);
    }

    @Override
    public void onGoToReportReview(int shopId, String reviewId) {
        startActivityForResult(InboxReputationReportActivity.getCallingIntent(
                getActivity(),
                shopId,
                reviewId),
                REQUEST_REPORT_REVIEW);
    }

    @Override
    public void onSuccessSendSmiley(int score) {
        refreshPage();
    }

    @Override
    public void onErrorFavoriteShop(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessFavoriteShop() {
        adapter.getHeader().getRevieweeBadgeSellerUiModel().setIsFavorited(
                adapter.getHeader().getRevieweeBadgeSellerUiModel().getIsFavorited() == 1 ? 0 : 1
        );
        adapter.notifyItemChanged(0);
    }

    @Override
    public void onDeleteReviewResponse(InboxReputationDetailItemUiModel element) {
        presenter.deleteReviewResponse(element.getReviewId(),
                element.getProductId(),
                String.valueOf(element.getShopId()),
                String.valueOf(element.getReputationId())
        );
    }

    @Override
    public void onErrorDeleteReviewResponse(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessDeleteReviewResponse() {
        refreshPage();
    }

    @Override
    public void onSendReplyReview(InboxReputationDetailItemUiModel element, String replyReview) {
        presenter.sendReplyReview(element.getReputationId(), element.getProductId(),
                element.getShopId(), element.getReviewId(), replyReview);
    }

    @Override
    public void onErrorReplyReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar

                (getActivity(), errorMessage);
    }

    @Override
    public void onSuccessReplyReview() {
        refreshPage();
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.reply_response_send));
    }

    @Override
    public void onShareReview(InboxReputationDetailItemUiModel element, int adapterPosition) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (shareReviewDialog == null && callbackManager != null) {
            shareReviewDialog = new ShareReviewDialog(getActivity(), callbackManager,
                    this);
        }

        if (shareReviewDialog != null) {
            shareReviewDialog.setModel(new ShareModel(
                    element.getProductName(),
                    element.getReview(),
                    element.getProductUrl(),
                    element.getProductAvatar()
            ));
            shareReviewDialog.show();
        }

        reputationTracking.onClickShareMenuReviewTracker(
                element.getOrderId(),
                element.getProductId(),
                adapterPosition
        );
    }

    @Override
    public void onGoToProductDetail(String productId, String productAvatar, String productName) {
        if (getContext() != null) {
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onSmoothScrollToReplyView(int adapterPosition) {
        if (adapterPosition > -1 && adapterPosition < adapter.getList().size()
                && adapter.getList().get(adapterPosition) instanceof InboxReputationDetailItemUiModel) {
            listProduct.smoothScrollToPosition(adapterPosition);
        }
    }

    @Override
    public void onGoToProfile(int reviewerId) {
        startActivity(RouteManager.getIntent(getActivity(), ApplinkConst.PROFILE, String.valueOf(reviewerId)));
    }

    @Override
    public void onGoToShopInfo(int shopId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, String.valueOf(shopId));
        startActivity(intent);
    }

    @Override
    public void onReputationSmileyClicked(String name, final String score) {
        if (!TextUtils.isEmpty(score)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getReputationSmileyMessage(name));
            builder.setPositiveButton(getString(R.string.submit_review),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.sendSmiley(reputationId, score, role);
                        }
                    });
            builder.setNegativeButton(getString(R.string.title_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int param) {
                            dialog.dismiss();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            reputationTracking.onClickSmileyShopReviewTracker(name, orderId);
        }
    }

    @Override
    public void onClickToggleReply(InboxReputationDetailItemUiModel element, int adapterPosition) {
        reputationTracking.onClickToggleReplyReviewTracker(
                element.getOrderId(),
                element.getProductId(),
                adapterPosition
        );
    }

    @Override
    public void onGoToShopDetail(int shopId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, String.valueOf(shopId));
        startActivity(intent);
    }

    @Override
    public void onGoToPeopleProfile(int userId) {
        startActivity(RouteManager.getIntent(getActivity(), ApplinkConst.PROFILE, String.valueOf(userId)));
    }

    @Override
    public void onClickReviewOverflowMenu(InboxReputationDetailItemUiModel inboxReputationDetailItemUiModel, int adapterPosition) {
        reputationTracking.onClickReviewOverflowMenuTracker(
                inboxReputationDetailItemUiModel.getOrderId(),
                inboxReputationDetailItemUiModel.getProductId(),
                adapterPosition
        );
    }

    @Override
    public UserSessionInterface getUserSession() {
        return userSession;
    }

    private String getReputationSmileyMessage(String name) {
        return getString(R.string.smiley_prompt_prefix) + " " + name
                + " " + getSmileySuffixMessage();
    }

    private String getSmileySuffixMessage() {
        return getString(R.string.smiley_prompt_suffix_shop);
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_GIVE_REVIEW && resultCode == Activity.RESULT_OK) {
            refreshPage();
            getActivity().setResult(Activity.RESULT_OK);
        } else if (requestCode == REQUEST_REPORT_REVIEW && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                    .success_report_review));
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshPage() {
        presenter.refreshPage(reputationId,
                getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null)
            presenter.detachView();
        callbackManager = null;
    }
}
