package com.tokopedia.digital.widget.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.model.DigitalCategoryItemHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 8/7/17.
 */

public class DigitalItemHeaderHolder extends LinearLayout {
    @BindView(R2.id.iv_icon)
    ImageView ivIcon;
    @BindView(R2.id.tv_category_name)
    TextView tvName;

    private DigitalCategoryItemHeader data;
    private ActionListener actionListener;

    public DigitalItemHeaderHolder(Context context) {
        super(context);
        initView(context);
    }

    public DigitalItemHeaderHolder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DigitalItemHeaderHolder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setData(DigitalCategoryItemHeader data) {
        this.data = data;
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_digital_category_item_header_digital_module, this, true
        );
        ButterKnife.bind(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        tvName.setText(data.getTitle());
        ivIcon.setImageResource(data.getResIconId());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) actionListener.onClickCategoryHeaderMenu(data);
                else throw new RuntimeException("actionListener is null!!!");
            }
        });
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * 52 / 106;
        setMeasuredDimension(width, height);
    }

    public interface ActionListener {
        void onClickCategoryHeaderMenu(DigitalCategoryItemHeader data);
    }
}
