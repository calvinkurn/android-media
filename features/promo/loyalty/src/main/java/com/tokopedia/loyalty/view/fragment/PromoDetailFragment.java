package com.tokopedia.loyalty.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter;
import com.tokopedia.loyalty.view.analytics.PromoDetailAnalytics;
import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;
import com.tokopedia.loyalty.view.data.mapper.PromoDataMapper;
import com.tokopedia.loyalty.view.presenter.PromoDetailPresenter;
import com.tokopedia.loyalty.view.view.IPromoDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailFragment extends BaseDaggerFragment implements
        IPromoDetailView,
        RefreshHandler.OnRefreshHandlerListener,
        PromoDetailAdapter.OnAdapterActionListener {

    private static final String ARG_EXTRA_PROMO_FLAG = "flag";
    private static final String ARG_EXTRA_PROMO_DATA = "promo_data";
    private static final String ARG_EXTRA_PROMO_SLUG = "slug";
    private static final String ARG_EXTRA_PROMO_POSITION = "position";
    private static final String ARG_EXTRA_PROMO_PAGE = "page";

    private static final int DETAIL_PROMO_FROM_DATA = 0;
    private static final int DETAIL_PROMO_FROM_SLUG = 1;

    private static final String FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_PROMO_DETAIL = "mp_promo_detail";

    private RefreshHandler refreshHandler;

    private RelativeLayout rlContainerLayout;
    private TextView tvPromoDetailAction;
    private RecyclerView rvPromoDetailView;
    private LinearLayout llPromoDetailBottomLayout;
    private BottomSheetView bottomSheetInfoPromoCode;

    private String promoSlug;
    private OnFragmentInteractionListener actionListener;

    private int page = 0;
    private int position = 0;

    @Inject
    PromoDetailAnalytics promoDetailAnalytics;
    @Inject
    PromoDetailPresenter promoDetailPresenter;
    @Inject
    PromoDetailAdapter promoDetailAdapter;
    @Inject
    PromoDataMapper promoDataMapper;
    @Inject
    CompositeSubscription compositeSubscription;
    @Inject
    PerformanceMonitoring performanceMonitoring;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(PromoDetailComponent.class).inject(this);
    }

    public PromoDetailFragment() {
        // Required empty public constructor
    }

    public static PromoDetailFragment newInstance(PromoData promoData, int page, int position) {
        PromoDetailFragment fragment = new PromoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXTRA_PROMO_FLAG, DETAIL_PROMO_FROM_DATA);
        args.putInt(ARG_EXTRA_PROMO_PAGE, page);
        args.putInt(ARG_EXTRA_PROMO_POSITION, position);
        args.putParcelable(ARG_EXTRA_PROMO_DATA, promoData);
        fragment.setArguments(args);
        return fragment;
    }

    public static PromoDetailFragment newInstance(String slug) {
        PromoDetailFragment fragment = new PromoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXTRA_PROMO_FLAG, DETAIL_PROMO_FROM_SLUG);
        args.putString(ARG_EXTRA_PROMO_SLUG, slug);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int flag = getArguments().getInt(ARG_EXTRA_PROMO_FLAG);

            if (flag == DETAIL_PROMO_FROM_DATA) {
                PromoData promoData = getArguments().getParcelable(ARG_EXTRA_PROMO_DATA);
                this.page = getArguments().getInt(ARG_EXTRA_PROMO_PAGE);
                this.position = getArguments().getInt(ARG_EXTRA_PROMO_POSITION);
                if (promoData != null) this.promoSlug = promoData.getSlug();
            } else if (flag == DETAIL_PROMO_FROM_SLUG) {
                this.promoSlug = getArguments().getString(ARG_EXTRA_PROMO_SLUG);
            }
        }
        performanceMonitoring.startTrace(FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_PROMO_DETAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_detail, container, false);

        this.refreshHandler = new RefreshHandler(getActivity(), view, this);

        this.rlContainerLayout = view.findViewById(R.id.container);
        this.llPromoDetailBottomLayout = view.findViewById(R.id.ll_promo_detail_bottom_layout);
        this.tvPromoDetailAction = view.findViewById(R.id.tv_promo_detail_action);
        this.rvPromoDetailView = view.findViewById(R.id.rv_promo_detail_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.rvPromoDetailView.setAdapter(promoDetailAdapter);
        this.rvPromoDetailView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rvPromoDetailView.setHasFixedSize(true);

        this.promoDetailAdapter.setAdapterListener(this);
        this.promoDetailPresenter.attachView(this);

        this.refreshHandler.startRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PromoDetailFragment.OnFragmentInteractionListener) {
            this.actionListener = (PromoDetailFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.actionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.compositeSubscription.unsubscribe();
    }

    @Override
    public void renderPromoDetail(PromoData promoData) {
        this.promoDetailAnalytics.userViewPromo(
                promoData.getTitle(),
                promoData.getId(),
                String.valueOf(this.page),
                String.valueOf(this.position + 1),
                promoData.getTitle(),
                promoData.getThumbnailImage(),
                parsePromoCodes(promoData)
        );

        this.refreshHandler.finishRefresh();

        View errorView = this.rlContainerLayout.findViewById(R.id.main_retry);
        if (errorView != null) errorView.setVisibility(View.GONE);

        this.promoDetailAdapter.setPromoDetail(promoDataMapper.convert(promoData));
        this.promoDetailAdapter.notifyDataSetChanged();
        setFragmentLayout(promoData);
        performanceMonitoring.stopTrace();
    }

    @Override
    public void renderErrorShowingPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorHttpGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }


    @Override
    public void onItemPromoShareClicked(PromoData promoData) {
        this.promoDetailAnalytics.userSharePromo("");
        this.actionListener.onSharePromo(promoData);
    }

    @Override
    public void onItemPromoCodeCopyClipboardClicked(String promoName, String promoCode) {
        this.promoDetailAnalytics.userClickCopyIcon(promoName);
        String message = getString(R.string.voucher_code_copy_to_clipboard);

        if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        ClipboardManager clipboard = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("CLIP_DATA_LABEL_VOUCHER_PROMO", promoCode);

        if (clipboard != null) clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onItemPromoCodeTooltipClicked() {
        this.promoDetailAnalytics.userClickTooltip();

        if (this.bottomSheetInfoPromoCode == null) {
            this.bottomSheetInfoPromoCode = new BottomSheetView(getActivity());

            this.bottomSheetInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle(getString(R.string.bottom_sheet_title_promo_tooltips))
                    .setBody(getString(R.string.bottom_sheet_body_promo_tooltips))
                    .setImg(R.drawable.ic_promo)
                    .build());

            this.bottomSheetInfoPromoCode.setBtnCloseOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promoDetailAnalytics.userCloseTooltip();
                    bottomSheetInfoPromoCode.dismiss();
                }
            });
        }

        this.bottomSheetInfoPromoCode.show();
    }

    @Override
    public void onWebViewLinkClicked(String url) {
        if (getActivity().getApplication() instanceof LoyaltyModuleRouter) {
            LoyaltyModuleRouter loyaltyModuleRouter = (LoyaltyModuleRouter) getActivity().getApplication();
            loyaltyModuleRouter.actionOpenGeneralWebView(getActivity(), url);
        }
    }


    private void setFragmentLayout(final PromoData promoData) {
        this.llPromoDetailBottomLayout.setVisibility(View.VISIBLE);

        this.tvPromoDetailAction.setText(promoData.getCtaText());
        this.tvPromoDetailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoDetailAnalytics.userClickCta(
                        promoData.getTitle(),
                        promoData.getId(),
                        String.valueOf(page),
                        String.valueOf(position + 1),
                        promoData.getTitle(),
                        promoData.getThumbnailImage(),
                        parsePromoCodes(promoData)
                );

                String appLink = promoData.getAppLink();
                String redirectUrl = promoData.getPromoLink();

                if (getActivity().getApplication() instanceof LoyaltyModuleRouter) {
                    LoyaltyModuleRouter loyaltyModuleRouter = (LoyaltyModuleRouter) getActivity().getApplication();

                    if (!TextUtils.isEmpty(appLink) && RouteManager.isSupportApplink(getActivity(),appLink)) {
                        RouteManager.route(getActivity(), appLink);
                    } else {
                        loyaltyModuleRouter.actionOpenGeneralWebView(getActivity(), redirectUrl);
                    }
                }
            }
        });
    }

    private void handleErrorEmptyState(String message) {
        if (this.refreshHandler.isRefreshing()) this.refreshHandler.finishRefresh();

        NetworkErrorHelper.showEmptyState(getActivity(), this.rlContainerLayout, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    private String parsePromoCodes(PromoData promoData) {
        List<String> promoCodes = new ArrayList<>();

        if (!promoData.getPromoCodeList().isEmpty()) {
            for (PromoCodeViewModel promoCode : promoData.getPromoCodeList()) {
                for (SingleCodeViewModel groupCode : promoCode.getGroupCode()) {
                    promoCodes.add(groupCode.getSingleCode());
                }
            }
        } else {
            promoCodes.add(promoData.getPromoCode());
        }

        return TextUtils.join(",", promoCodes);
    }

    @Override
    public void onRefresh(View view) {
        this.promoDetailPresenter.getPromoDetail(promoSlug);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onSharePromo(PromoData promoData);

    }
}