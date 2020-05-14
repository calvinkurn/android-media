package com.tokopedia.opportunity.snapshot.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.opportunity.R;
import com.tokopedia.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class DetailInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    TextView tvWeight;
    TextView tvMinOrder;
    TextView tvInsurance;
    TextView tvCondition;
    TextView tvPreOrder;
    TableRow preOrderView;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        tvWeight = (TextView) findViewById(R.id.tv_weight);
        tvMinOrder = (TextView) findViewById(R.id.tv_minimum);
        tvInsurance = (TextView) findViewById(R.id.tv_insurance);
        tvCondition = (TextView) findViewById(R.id.tv_condition);
        tvPreOrder = (TextView) findViewById(R.id.tv_preorder);
        preOrderView = (TableRow) findViewById(R.id.tr_preorder);

    }

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
        tvMinOrder.setText(data.getInfo().getProductMinOrder().replace(".", ""));
        tvInsurance.setText(data.getInfo().getProductInsurance());
        tvCondition.setText(data.getInfo().getProductCondition());

        setVisibility(VISIBLE);
    }
}
