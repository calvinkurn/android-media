package com.tokopedia.core.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;

import butterknife.BindView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class DetailInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    @BindView(R2.id.tv_weight)
    TextView tvWeight;
    @BindView(R2.id.tv_minimum)
    TextView tvMinOrder;
    @BindView(R2.id.tv_insurance)
    TextView tvInsurance;
    @BindView(R2.id.tv_condition)
    TextView tvCondition;
    @BindView(R2.id.tv_preorder)
    TextView tvPreOrder;
    @BindView(R2.id.tr_preorder)
    TableRow preOrderView;

    public DetailInfoView(Context context) {
        super(context);
    }

    public DetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_snapshot_detail_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        preOrderView.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                && !data.getPreOrder().getPreorderStatus().equals("0")
                && !data.getPreOrder().getPreorderProcessTime().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
            tvPreOrder.setText(String.format("%s %s %s", "Waktu Proses",
                    data.getPreOrder().getPreorderProcessTime(),
                    data.getPreOrder().getPreorderProcessTimeTypeString()));
            preOrderView.setVisibility(VISIBLE);
        }

        tvWeight.setText(String.format("%s%s",
                data.getInfo().getProductWeight(),
                data.getInfo().getProductWeightUnit()));
        tvMinOrder.setText(data.getInfo().getProductMinOrder().replace(".",""));
        tvInsurance.setText(data.getInfo().getProductInsurance());
        tvCondition.setText(data.getInfo().getProductCondition());

        setVisibility(VISIBLE);
    }
}
