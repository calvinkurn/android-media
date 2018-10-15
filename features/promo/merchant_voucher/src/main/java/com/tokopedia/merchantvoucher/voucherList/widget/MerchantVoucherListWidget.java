package com.tokopedia.merchantvoucher.voucherList.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.merchantvoucher.R;
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView;
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 [Title]                                [See All]
 +----------+ +-----+  +----------+ +-----+  +---
 | VOUCHR_A +-+     |  | VOUCHR_B +-+     |  |
 | *20Rb     |  USE |  | *20Rb     |  USE |  |
 |          +-+     |  |          +-+     |  |
 +----------+ +-----+  +----------+ +-----+  +---
 */
public class MerchantVoucherListWidget extends FrameLayout
        implements MerchantVoucherView.OnMerchantVoucherViewListener,
        BaseListAdapter.OnAdapterInteractionListener<MerchantVoucherViewModel> {

    private String titleString;
    private int titleTextSize;
    private int textStyle;
    private int titleTextColor;

    private TextView tvTitle;
    private TextView tvSeeAll;
    private RecyclerView recyclerView;

    private BaseListAdapter<MerchantVoucherViewModel, MerchantVoucherAdapterTypeFactory> adapter;

    private OnMerchantVoucherListWidgetListener onMerchantVoucherListWidgetListener;
    private View rootView;

    @Override
    public boolean isOwner() {
        if (onMerchantVoucherListWidgetListener!= null) {
            return onMerchantVoucherListWidgetListener.isOwner();
        } else {
            return false;
        }
    }

    public interface OnMerchantVoucherListWidgetListener{
        void onMerchantUseVoucherClicked(MerchantVoucherViewModel merchantVoucherViewModel);
        void onItemClicked(MerchantVoucherViewModel merchantVoucherViewModel);
        void onSeeAllClicked();
        boolean isOwner();
    }

    public void setOnMerchantVoucherListWidgetListener(OnMerchantVoucherListWidgetListener onMerchantVoucherListWidgetListener) {
        this.onMerchantVoucherListWidgetListener = onMerchantVoucherListWidgetListener;
    }

    public MerchantVoucherListWidget(@NonNull Context context) {
        super(context);
        applyAttrs(context, null);
        init();
    }

    public MerchantVoucherListWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(context, attrs);
        init();
    }

    public MerchantVoucherListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MerchantVoucherListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyAttrs(context, attrs);
        init();
    }

    private void applyAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MerchantVoucherListWidget);
            titleString = typedArray.getString(R.styleable.MerchantVoucherListWidget_mvlwTitleString);
            titleTextSize = typedArray.getDimensionPixelOffset(R.styleable.MerchantVoucherListWidget_mvlwTitleTextSize, 0);
            if (typedArray.hasValue(R.styleable.MerchantVoucherListWidget_android_textStyle)) {
                textStyle = typedArray.getInt(R.styleable.MerchantVoucherListWidget_android_textStyle, 0);
            }
            if (typedArray.hasValue(R.styleable.MerchantVoucherListWidget_android_textColor)) {
                titleTextColor = typedArray.getColor(R.styleable.MerchantVoucherListWidget_android_textColor, 0);
            }
            typedArray.recycle();
        }
    }

    private void init() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.merchant_voucher_list_widget,
                this, true);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        if (titleTextSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        tvSeeAll = rootView.findViewById(R.id.tvSeeAll);
        tvSeeAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMerchantVoucherListWidgetListener!= null) {
                    onMerchantVoucherListWidgetListener.onSeeAllClicked();
                }
            }
        });
        if (titleTextSize>0) {
            tvSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        tvTitle.setText(titleString);
        if (textStyle!= 0) {
            tvTitle.setTypeface(null, textStyle);
        }
        if (titleTextColor!= 0) {
            tvTitle.setTextColor(titleTextColor);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new BaseListAdapter<>(
                new MerchantVoucherAdapterTypeFactory(this, true),
                this);
        recyclerView.setAdapter(adapter);

        rootView.setVisibility(View.GONE);
        tvSeeAll.setVisibility(View.GONE);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setData(ArrayList<MerchantVoucherViewModel> merchantVoucherViewModelArrayList){
        adapter.clearAllElements();
        if (merchantVoucherViewModelArrayList!= null && merchantVoucherViewModelArrayList.size() > 0) {
            adapter.addElement(merchantVoucherViewModelArrayList);
        }
        int dataSize = adapter.getDataSize();
        if (dataSize > 0){
            rootView.setVisibility(View.VISIBLE);
            if (dataSize == 1) {
                tvSeeAll.setVisibility(View.GONE);
            } else {
                tvSeeAll.setVisibility(View.VISIBLE);
            }
        } else {
            rootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMerchantUseVoucherClicked(@NotNull MerchantVoucherViewModel merchantVoucherViewModel) {
        if (onMerchantVoucherListWidgetListener!= null) {
            onMerchantVoucherListWidgetListener.onMerchantUseVoucherClicked(merchantVoucherViewModel);
        }
    }

    @Override
    public void onItemClicked(MerchantVoucherViewModel o) {
        if (onMerchantVoucherListWidgetListener!= null) {
            onMerchantVoucherListWidgetListener.onItemClicked(o);
        }
    }
}
