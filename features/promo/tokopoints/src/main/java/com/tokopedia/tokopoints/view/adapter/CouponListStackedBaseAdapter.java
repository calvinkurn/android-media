package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.activity.CouponDetailActivity;
import com.tokopedia.tokopoints.view.fragment.CouponInStackBottomSheet;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.presenter.CouponListingStackedPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import rx.Subscriber;

public class CouponListStackedBaseAdapter extends BaseAdapter<CouponValueEntity> {

    private Context mContext;
    private int mCategoryId = 0;
    private CouponListingStackedPresenter mPresenter;

    public class ViewHolder extends BaseVH {
        TextView label, value, tvMinTxnValue, tvMinTxnLabel, tvStackCount;
        ImageView imgBanner, imgLabel, ivMinTxn;
        public boolean isVisited = false;
        /*This section is exclusively for handling timer*/
        public CountDownTimer timer;
        public ProgressBar progressTimer;
        public View viewCouponNew;
        public CardView cvShadow1, cvShadow2, cvData;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_time_label);
            value = view.findViewById(R.id.text_time_value);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_time);
            ivMinTxn = view.findViewById(R.id.iv_rp);
            tvMinTxnValue = view.findViewById(R.id.tv_min_txn_value);
            tvMinTxnLabel = view.findViewById(R.id.tv_min_txn_label);
            tvStackCount = view.findViewById(R.id.text_stack_count);
            progressTimer = view.findViewById(R.id.progress_timer);
            viewCouponNew = view.findViewById(R.id.view_coupon_new);
            cvShadow1 = view.findViewById(R.id.cv_shadow_1);
            cvShadow2 = view.findViewById(R.id.cv_shadow_2);
            cvData = view.findViewById(R.id.cv_data);
        }

        @Override
        public void bindView(CouponValueEntity item, int position) {
            setData(this, item);

        }
    }

    public CouponListStackedBaseAdapter(CouponListingStackedPresenter presenter, AdapterCallback callback, Context context, int categoryId) {
        super(callback);
        this.mPresenter = presenter;
        this.mContext = context;
        this.mCategoryId = categoryId;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder vh) {
        super.onViewAttachedToWindow(vh);

        if (vh instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) vh;

            CouponValueEntity data = getItems().get(holder.getAdapterPosition());
            if (data == null) {
                return;
            }

            if (!holder.isVisited) {
                Map<String, String> item = new HashMap<>();
                item.put("id", String.valueOf(data.getCatalogId()));
                item.put("name", data.getTitle());
                item.put("position", String.valueOf(holder.getAdapterPosition()));
                item.put("creative", data.getTitle());
                item.put("creative_url", data.getImageUrlMobile());
                item.put("promo_code", data.getCode());

                Map<String, List<Map<String, String>>> promotions = new HashMap<>();
                promotions.put("promotions", Arrays.asList(item));

                Map<String, Map<String, List<Map<String, String>>>> promoView = new HashMap<>();
                promoView.put("promoView", promotions);

                AnalyticsTrackerUtil.sendECommerceEvent(holder.value.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                        data.getTitle(), promoView);

                holder.isVisited = true;
            }
        }
    }

    private void sendClickEvent(Context context, CouponValueEntity data, int position) {
        Map<String, String> item = new HashMap<>();
        item.put("id", String.valueOf(data.getCatalogId()));
        item.put("name", data.getTitle());
        item.put("position", String.valueOf(position));
        item.put("creative", data.getTitle());
        item.put("creative_url", data.getImageUrlMobile());
        item.put("promo_code", data.getCode());

        Map<String, List<Map<String, String>>> promotions = new HashMap<>();
        promotions.put("promotions", Arrays.asList(item));

        Map<String, Map<String, List<Map<String, String>>>> promoClick = new HashMap<>();
        promoClick.put("promoView", promotions);

        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.getTitle(), promoClick);
    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_my_coupon_stacked, parent, false);

        return new CouponListStackedBaseAdapter.ViewHolder(itemView);
    }

    @Override
    public void loadData(int pageNumber) {
        super.loadData(pageNumber);

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        graphqlUseCase.clearRequest();
        //Adding request for main query
        Map<String, Object> variablesMain = new HashMap<>();
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE, pageNumber);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE_SIZE, CommonConstant.HOMEPAGE_PAGE_SIZE);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.SERVICE_ID, "");
        variablesMain.put(CommonConstant.GraphqlVariableKeys.CATEGORY_ID_COUPON, mCategoryId);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.CATEGORY_ID, 0);

        String query = GraphqlHelper.loadRawString(mContext.getResources(), R.raw.tp_gql_coupon_listing_stack);
        GraphqlRequest graphqlRequestMain = new GraphqlRequest(query, TokoPointPromosEntity.class, variablesMain, false);
        graphqlUseCase.addRequest(graphqlRequestMain);

        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loadCompletedWithError();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //handling the catalog listing and tabs
                TokoPointPromosEntity catalogListingOuter = graphqlResponse.getData(TokoPointPromosEntity.class);
                if (catalogListingOuter != null) {
                    if (catalogListingOuter.getCoupon().getCoupons() != null) {
                        loadCompleted(catalogListingOuter.getCoupon().getCoupons(), catalogListingOuter);
                        setLastPage(!catalogListingOuter.getCoupon().getPaging().isHasNext());
                    }
                }
            }
        });

    }

    private void setData(ViewHolder holder, CouponValueEntity item) {
        ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());

        if (item.isNewCoupon()) {
            holder.viewCouponNew.setVisibility(View.VISIBLE);
        } else {
            holder.viewCouponNew.setVisibility(View.GONE);
        }

        if (item.getUsage() != null) {
            holder.label.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.imgLabel.setVisibility(View.VISIBLE);
            holder.value.setText(item.getUsage().getUsageStr().trim());
            holder.label.setText(item.getUsage().getText());
        } else {
            holder.label.setVisibility(View.GONE);
            holder.value.setVisibility(View.GONE);
            holder.imgLabel.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(item.getMinimumUsageLabel())) {
            holder.tvMinTxnLabel.setVisibility(View.GONE);
            holder.ivMinTxn.setVisibility(View.GONE);
        } else {
            holder.ivMinTxn.setVisibility(View.VISIBLE);
            holder.tvMinTxnLabel.setVisibility(View.VISIBLE);
            holder.tvMinTxnLabel.setText(item.getMinimumUsageLabel());
        }

        if (TextUtils.isEmpty(item.getMinimumUsage())) {
            holder.tvMinTxnValue.setVisibility(View.GONE);
            holder.tvMinTxnLabel.setPadding(0, holder.imgBanner.getResources().getDimensionPixelOffset(R.dimen.dp_5), 0, 0);
        } else {
            holder.tvMinTxnLabel.setPadding(0, 0, 0, 0);
            holder.tvMinTxnValue.setVisibility(View.VISIBLE);
            holder.tvMinTxnValue.setText(item.getMinimumUsage());
        }

        ConstraintLayout.LayoutParams layoutParamsCv1 = (ConstraintLayout.LayoutParams) holder.cvShadow1.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsCvData = (ConstraintLayout.LayoutParams) holder.cvData.getLayoutParams();
        if (item.isStacked()) {
            layoutParamsCv1.setMargins(holder.cvShadow1.getResources().getDimensionPixelOffset(R.dimen.dp_12),
                    0,
                    holder.cvShadow1.getResources().getDimensionPixelOffset(R.dimen.dp_12),
                    holder.cvShadow1.getResources().getDimensionPixelOffset(R.dimen.dp_5));
            layoutParamsCvData.setMargins(0, 0, 0,
                    holder.cvShadow1.getResources().getDimensionPixelOffset(R.dimen.dp_10));
            holder.cvShadow1.setVisibility(View.VISIBLE);
            holder.cvShadow2.setVisibility(View.VISIBLE);
            holder.cvShadow1.setLayoutParams(layoutParamsCv1);
            holder.cvData.setLayoutParams(layoutParamsCvData);
        } else {
            holder.cvShadow1.setVisibility(View.GONE);
            holder.cvShadow2.setVisibility(View.GONE);
            layoutParamsCv1.setMargins(0, 0, 0, 0);
            layoutParamsCvData.setMargins(0, 0, 0, 0);
            holder.cvShadow1.setLayoutParams(layoutParamsCv1);
            holder.cvData.setLayoutParams(layoutParamsCvData);
        }

        if (item.getUpperLeftSection() == null
                || item.getUpperLeftSection().getTextAttributes() == null
                || item.getUpperLeftSection().getTextAttributes().isEmpty()
                || item.getUpperLeftSection().getTextAttributes().get(0) == null
                || TextUtils.isEmpty(item.getUpperLeftSection().getTextAttributes().get(0).getText())) {
            holder.tvStackCount.setVisibility(View.GONE);
        } else {
            holder.tvStackCount.setVisibility(View.VISIBLE);
            holder.tvStackCount.setText(item.getUpperLeftSection().getTextAttributes().get(0).getText());

            if (item.getUpperLeftSection().getTextAttributes().get(0).isBold()) {
                holder.tvStackCount.setTypeface(holder.tvStackCount.getTypeface(), Typeface.BOLD);
            } else {
                holder.tvStackCount.setTypeface(holder.tvStackCount.getTypeface(), Typeface.NORMAL);
            }

            if (TextUtils.isEmpty(item.getUpperLeftSection().getTextAttributes().get(0).getColor())) {
                holder.tvStackCount.setTextColor(ContextCompat.getColor(holder.tvStackCount.getContext(), R.color.medium_green));
            } else {
                try {
                    holder.tvStackCount.setTextColor(Color.parseColor(item.getUpperLeftSection().getTextAttributes().get(0).getColor()));
                } catch (IllegalArgumentException iae) {
                    holder.tvStackCount.setTextColor(ContextCompat.getColor(holder.tvStackCount.getContext(), R.color.medium_green));
                }
            }

            if (!TextUtils.isEmpty(item.getUpperLeftSection().getBackgroundColor())) {
                GradientDrawable shape = getShape(item.getUpperLeftSection().getBackgroundColor(), holder.tvStackCount.getContext());
                if (shape != null) {
                    holder.tvStackCount.setBackground(shape);
                }
            }
        }

        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer.cancel();
        }

        if (item.getUsage() != null) {
            if (item.getUsage().getActiveCountDown() < 1) {
                if (item.getUsage().getExpiredCountDown() > 0
                        && item.getUsage().getExpiredCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    holder.progressTimer.setMax((int) CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S);
                    holder.progressTimer.setVisibility(View.VISIBLE);
                    holder.timer = new CountDownTimer(item.getUsage().getExpiredCountDown() * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            item.getUsage().setExpiredCountDown(l / 1000);
                            int seconds = (int) (l / 1000) % 60;
                            int minutes = (int) ((l / (1000 * 60)) % 60);
                            int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                            holder.value.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                            holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), R.color.medium_green));
                            holder.progressTimer.setProgress((int) l / 1000);
                            holder.value.setPadding(holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall));
                        }

                        @Override
                        public void onFinish() {
                            holder.value.setText("00 : 00 : 00");
                        }
                    }.start();
                } else {
                    holder.progressTimer.setVisibility(View.GONE);
                    holder.value.setPadding(0, 0, 0, 0);
                    holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), R.color.black_70));
                }
            } else {
                holder.progressTimer.setVisibility(View.GONE);
                holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), R.color.black_70));
            }

            if (item.getUsage().getActiveCountDown() > 0) {
                holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(), R.color.tp_coupon_enable), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), R.color.tp_coupon_enable), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(), R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if (holder.itemView != null) {
                holder.itemView.setOnClickListener(v -> {
                    if (item.isStacked()) {
                        mPresenter.getCouponInStack(item.getStackId());
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(CommonConstant.EXTRA_COUPON_CODE, item.getCode());
                        holder.imgBanner.getContext().startActivity(CouponDetailActivity.getCouponDetail(holder.imgBanner.getContext(), bundle), bundle);
                        sendClickEvent(holder.imgBanner.getContext(), item, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    private GradientDrawable getShape(String hex, Context context) {
        try {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[]{context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    context.getResources().getDimensionPixelOffset(R.dimen.dp_4)});
            shape.setColor(Color.parseColor(hex));
            shape.setStroke(context.getResources().getDimensionPixelOffset(R.dimen.dp_2), Color.parseColor(hex));
            return shape;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
