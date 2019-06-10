package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.activity.CouponCatalogDetailsActivity;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CatalogListingOuter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class CatalogListAdapter extends BaseAdapter<CatalogsValueEntity> {

    private int categoryId;
    private int subCategoryId;
    private final Context mContext;
    private CatalogPurchaseRedemptionPresenter mPresenter;
    private boolean mIsLimitEnable;
    private int pointRange;


    public CatalogListAdapter(CatalogPurchaseRedemptionPresenter mPresenter, Context context, AdapterCallback callback, int categoryId, int subCategoryId, int pointRange, boolean isLimitEnable) {
        super(callback);
        this.mPresenter = mPresenter;
        this.mIsLimitEnable = isLimitEnable;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.pointRange = pointRange;
        this.mContext = context;
    }

    public class ViewHolder extends BaseVH {
        TextView quota, description, pointLabel, pointValue,
                timeLabel, timeValue, disabledError, btnContinue,
                labelPoint, textDiscount;
        ImageView imgBanner, imgTime, imgPoint;
        ProgressBar pbQuota;
        boolean isVisited = false;

        public ViewHolder(View view) {
            super(view);
            quota = view.findViewById(R.id.text_quota_count);
            description = view.findViewById(R.id.text_description);
            pointLabel = view.findViewById(R.id.text_my_points_label);
            pointValue = view.findViewById(R.id.text_point_value);
            timeLabel = view.findViewById(R.id.text_time_label);
            timeValue = view.findViewById(R.id.text_time_value);
            disabledError = view.findViewById(R.id.text_disabled_error);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgTime = view.findViewById(R.id.img_time);
            imgPoint = view.findViewById(R.id.img_points_stack);
            labelPoint = view.findViewById(R.id.text_point_label);
            textDiscount = view.findViewById(R.id.text_point_discount);
            pbQuota = view.findViewById(R.id.progress_timer_quota);
        }

        @Override
        public void bindView(CatalogsValueEntity item, int position) {
            setData(this, item, position);
        }
    }

    private void setData(ViewHolder holder, CatalogsValueEntity item, int position) {
        holder.btnContinue.setEnabled(!item.isDisabledButton());
        holder.description.setText(item.getTitle());
        holder.btnContinue.setText(R.string.tp_label_exchange); //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner, item.getThumbnailUrlMobile());
        //setting points info if exist in response
        if (item.getPointsStr() == null || item.getPointsStr().isEmpty()) {
            holder.pointValue.setVisibility(View.GONE);
            holder.imgPoint.setVisibility(View.GONE);
        } else {
            holder.pointValue.setVisibility(View.VISIBLE);
            holder.imgPoint.setVisibility(View.VISIBLE);
            holder.pointValue.setText(item.getPointsStr());
        }

        //setting expiry time info if exist in response
        if (item.getExpiredLabel() == null || item.getExpiredLabel().isEmpty()) {
            holder.timeLabel.setVisibility(View.GONE);
            holder.timeValue.setVisibility(View.GONE);
            holder.imgTime.setVisibility(View.GONE);
        } else {
            holder.timeLabel.setVisibility(View.VISIBLE);
            holder.timeValue.setVisibility(View.VISIBLE);
            holder.imgTime.setVisibility(View.VISIBLE);
            holder.timeLabel.setText(item.getExpiredLabel());
            holder.timeValue.setText(item.getExpiredStr());
        }

        //Quota text handling
        if (item.getUpperTextDesc() == null || item.getUpperTextDesc().isEmpty()) {
            holder.quota.setVisibility(View.GONE);
            holder.pbQuota.setVisibility(View.GONE);
        } else {
            holder.quota.setVisibility(View.VISIBLE);
            holder.pbQuota.setVisibility(View.VISIBLE);
            holder.pbQuota.setProgress(0);
            StringBuilder upperText = new StringBuilder();

            if (item.getCatalogType() == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.getContext(), R.color.red_150));
            } else {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.getContext(), R.color.black_38));
            }

            for (int i = 0; i < item.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    if (item.getCatalogType() == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                        //for flash sale progress bar handling
                        holder.pbQuota.setProgress(item.getQuota());
                        upperText.append(item.getUpperTextDesc().get(i));
                    } else {
                        //exclusive case for handling font color of second index.
                        upperText.append("<font color='#ff5722'>" + item.getUpperTextDesc().get(i) + "</font>");
                    }
                } else {
                    upperText.append(item.getUpperTextDesc().get(i)).append(" ");
                }
            }

            holder.quota.setText(MethodChecker.fromHtml(upperText.toString()));
        }

        //Quota text handling
        if (item.getDisableErrorMessage() == null || item.getDisableErrorMessage().isEmpty()) {
            holder.disabledError.setVisibility(View.GONE);
        } else {
            holder.disabledError.setVisibility(View.VISIBLE);
            holder.disabledError.setText(item.getDisableErrorMessage());
        }

        //disabling the coupons if not eligible for current membership
        if (item.isDisabled()) {
            ImageUtil.dimImage(holder.imgBanner);
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(holder.imgBanner);
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.getContext(), R.color.orange_red));
        }

        if (item.isDisabledButton()) {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.black_12));
        } else {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.white));
        }

        if (item.getPointsSlash() <= 0) {
            holder.labelPoint.setVisibility(View.GONE);
        } else {
            holder.labelPoint.setVisibility(View.VISIBLE);
            holder.labelPoint.setText(item.getPointsSlashStr());
            holder.labelPoint.setPaintFlags(holder.labelPoint.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (item.getDiscountPercentage() <= 0) {
            holder.textDiscount.setVisibility(View.GONE);
        } else {
            holder.textDiscount.setVisibility(View.VISIBLE);
            holder.textDiscount.setText(item.getDiscountPercentageStr());
        }

        holder.btnContinue.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(item);

            AnalyticsTrackerUtil.sendEvent(holder.btnContinue.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    item.getBaseCode());
        });

        holder.imgBanner.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConstant.EXTRA_CATALOG_CODE, item.getSlug());
            holder.imgBanner.getContext().startActivity(CouponCatalogDetailsActivity.getCatalogDetail(holder.imgBanner.getContext(), bundle), bundle);
            sendClickEvent(holder.imgBanner.getContext(), item, position);
        });


        holder.btnContinue.setVisibility(item.isShowTukarButton() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void loadData(int currentPageIndex) {
        if (mIsLimitEnable) {
            return;
        }
        super.loadData(currentPageIndex);
        GraphqlUseCase mGetHomePageData = new GraphqlUseCase();
        mGetHomePageData.clearRequest();
        //Adding request for main query
        Map<String, Object> variablesMain = new HashMap<>();
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE, currentPageIndex);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE_SIZE, CommonConstant.PAGE_SIZE);
        //Default page sort id
        variablesMain.put(CommonConstant.GraphqlVariableKeys.SORT_ID, getSortId());
        variablesMain.put(CommonConstant.GraphqlVariableKeys.CATEGORY_ID, categoryId);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.SUB_CATEGORY_ID, subCategoryId);
        //Point range will be zero for all catalog
        variablesMain.put(CommonConstant.GraphqlVariableKeys.POINTS_RANGE, getPointsRange());

        GraphqlRequest graphqlRequestMain = new GraphqlRequest(GraphqlHelper.loadRawString(mContext.getResources(), R.raw.tp_gql_catalog_listing),
                CatalogListingOuter.class,
                variablesMain, false);
        mGetHomePageData.addRequest(graphqlRequestMain);


        mGetHomePageData.execute(new Subscriber<GraphqlResponse>() {
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
                CatalogListingOuter catalogListingOuter = graphqlResponse.getData(CatalogListingOuter.class);
                if (catalogListingOuter != null) {
                    loadCompleted(catalogListingOuter.getCatalog().getCatalogs(), catalogListingOuter);
                    setLastPage(!catalogListingOuter.getCatalog().getPaging().isHasNext());
                }else{
                    loadCompletedWithError();
                }
            }
        });
    }

    private int getPointsRange() {
        return pointRange;
    }

    private int getSortId() {
        return 1;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder vh) {
        super.onViewAttachedToWindow(vh);


        if (vh instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) vh;
            CatalogsValueEntity data = getItems().get(vh.getAdapterPosition());
            if (data == null) {
                return;
            }

            if (!holder.isVisited) {
                Map<String, String> item = new HashMap<>();
                item.put("id", String.valueOf(data.getId()));
                item.put("name", data.getTitle());
                item.put("position", String.valueOf(holder.getAdapterPosition()));
                item.put("creative", data.getTitle());
                item.put("creative_url", data.getImageUrlMobile());
                item.put("promo_code", data.getBaseCode());

                Map<String, List<Map<String, String>>> promotions = new HashMap<>();
                promotions.put("promotions", Arrays.asList(item));

                Map<String, Map<String, List<Map<String, String>>>> promoView = new HashMap<>();
                promoView.put("promoView", promotions);

                AnalyticsTrackerUtil.sendECommerceEvent(holder.btnContinue.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                        data.getTitle(), promoView);

                holder.isVisited = true;
            }
        }
    }

    private void sendClickEvent(Context context, CatalogsValueEntity data, int position) {
        Map<String, String> item = new HashMap<>();
        item.put("id", String.valueOf(data.getId()));
        item.put("name", data.getTitle());
        item.put("position", String.valueOf(position));
        item.put("creative", data.getTitle());
        item.put("creative_url", data.getImageUrlMobile());
        item.put("promo_code", data.getBaseCode());

        Map<String, List<Map<String, String>>> promotions = new HashMap<>();
        promotions.put("promotions", Arrays.asList(item));

        Map<String, Map<String, List<Map<String, String>>>> promoClick = new HashMap<>();
        promoClick.put("promoClick", promotions);

        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.getTitle(), promoClick);
    }
}
