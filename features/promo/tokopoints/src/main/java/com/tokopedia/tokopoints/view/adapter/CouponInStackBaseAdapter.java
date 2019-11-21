package com.tokopedia.tokopoints.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.tokopedia.tokopoints.view.fragment.CouponListingStackedFragment;
import com.tokopedia.tokopoints.view.model.CouponDetailEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Subscriber;

import static com.tokopedia.tokopoints.view.fragment.CouponListingStackedFragment.REQUEST_CODE_STACKED_ADAPTER;
import static com.tokopedia.tokopoints.view.fragment.CouponListingStackedFragment.REQUEST_CODE_STACKED_IN_ADAPTER;

public class CouponInStackBaseAdapter extends BaseAdapter<CouponValueEntity> {

    private Context mContext;
    private String mStackId;

    public void couponCodeVisible(String code) {
        for ( int i = 0 ;i < getItems().size(); i++){
            CouponValueEntity data = getItem(i);
            if (code.equals(data.getCode())){
                if (data.isNewCoupon()){
                    data.setNewCoupon(false);
                    notifyItemChanged(i);
                }
                break;
            }
        }
    }

    public class ViewHolder extends BaseVH {
        TextView label, value, tvMinTxnValue, tvMinTxnLabel;
        ImageView imgBanner, imgLabel, ivMinTxn;
        public boolean isVisited = false;
        /*This section is exclusively for handling timer*/
        public CountDownTimer timer;
        public ProgressBar progressTimer;
        public CardView cvShadow1, cvShadow2, cvData,cv1,cv2;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_time_label);
            value = view.findViewById(R.id.text_time_value);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_time);
            ivMinTxn = view.findViewById(R.id.iv_rp);
            tvMinTxnValue = view.findViewById(R.id.tv_min_txn_value);
            tvMinTxnLabel = view.findViewById(R.id.tv_min_txn_label);
            progressTimer = view.findViewById(R.id.progress_timer);
            cvShadow1 = view.findViewById(R.id.cv_shadow_1);
            cvShadow2 = view.findViewById(R.id.cv_shadow_2);
            cvData = view.findViewById(R.id.cv_data);
            cv1 = view.findViewById(R.id.cv_1);
            cv2 = view.findViewById(R.id.cv_2);
        }

        @Override
        public void bindView(CouponValueEntity item, int position) {
            setData(this, item);

        }
    }

    public CouponInStackBaseAdapter(AdapterCallback callback, Context context, String stackId) {
        super(callback);
        this.mContext = context;
        this.mStackId = stackId;
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

        return new CouponInStackBaseAdapter.ViewHolder(itemView);
    }

    @Override
    public void loadData(int pageNumber) {
        super.loadData(pageNumber);

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        graphqlUseCase.clearRequest();
        //Adding request for main query
        Map<String, Object> variablesMain = new HashMap<>();
        variablesMain.put(CommonConstant.GraphqlVariableKeys.STACK_ID, mStackId);

        String query = GraphqlHelper.loadRawString(mContext.getResources(), R.raw.tp_gql_coupon_in_stack);
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
                        setLastPage(true); //true mean no paging
                    }
                }
            }
        });

    }

    private void setData(ViewHolder holder, CouponValueEntity item) {
        ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());

        if (item.isNewCoupon()) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.new_coupon_background_color));
            holder.cv1.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.new_coupon_background_color));
            holder.cv2.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.new_coupon_background_color));
        } else {
            holder.cv1.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(com.tokopedia.design.R.color.white));
            holder.cv2.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(com.tokopedia.design.R.color.white));
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(com.tokopedia.design.R.color.white));
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
        } else {
            holder.tvMinTxnValue.setVisibility(View.VISIBLE);
            holder.tvMinTxnValue.setText(item.getMinimumUsage());
        }

        ConstraintLayout.LayoutParams layoutParamsCv1 = (ConstraintLayout.LayoutParams) holder.cvShadow1.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsCvData = (ConstraintLayout.LayoutParams) holder.cvData.getLayoutParams();
        if (item.isStacked()) {
            layoutParamsCv1.setMargins(holder.cvShadow1.getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12),
                    0,
                    holder.cvShadow1.getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12),
                    holder.cvShadow1.getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_5));
            layoutParamsCvData.setMargins(0, 0, 0,
                    holder.cvShadow1.getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_10));
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

        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer.cancel();
        }

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
                        holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.medium_green));
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
                holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.black_70));
            }
        } else {
            holder.progressTimer.setVisibility(View.GONE);
            holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.black_70));
        }

        if (item.getUsage().getActiveCountDown() > 0) {
            holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(), R.color.tp_coupon_enable), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), R.color.tp_coupon_enable), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(), com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        if (holder.itemView != null) {
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(CommonConstant.EXTRA_COUPON_CODE, item.getCode());
                if (item.isNewCoupon()) {
                    ((FragmentActivity) holder.imgBanner.getContext()).startActivityForResult(CouponDetailActivity.getCouponDetail(holder.imgBanner.getContext(), bundle), REQUEST_CODE_STACKED_IN_ADAPTER);
                } else {
                    holder.imgBanner.getContext().startActivity(CouponDetailActivity.getCouponDetail(holder.imgBanner.getContext(), bundle));
                }
                sendClickEvent(holder.imgBanner.getContext(), item, holder.getAdapterPosition());
            });
        }
    }

    public String getStackId(){
        return mStackId;
    }
}
