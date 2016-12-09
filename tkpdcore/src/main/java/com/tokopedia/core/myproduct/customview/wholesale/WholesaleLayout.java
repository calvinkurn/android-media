package com.tokopedia.core.myproduct.customview.wholesale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.List;

/**
 * Created by sebastianuskh on 12/2/16.
 */

public class WholesaleLayout extends RelativeLayout implements WholesaleAdapterImpl.WholesaleAdapterListener {
    private WholesaleAdapterImpl adapter;
    private double mainPrice;
    private int currency;

    public WholesaleLayout(Context context) {
        super(context);
        initView();
    }

    public WholesaleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WholesaleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public WholesaleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        getHeader();
        addView(getButton());
        addView(getRecyclerView());
    }

    private View getHeader() {
        return LayoutInflater.from(getContext()).inflate(R.layout.wholesale_column_name, this, false);
    }

    public void setupParams(double mainPrice, int currency){
        this.mainPrice = mainPrice;
        this.currency = currency;
    }

    public void setupParams(double mainPrice, int currency, List<WholesaleModel> datas){
        this.mainPrice = mainPrice;
        this.currency = currency;
        this.adapter.setData(datas);
    }

    public void setCurrencyUnit(int currency){
        this.currency = currency;

    }

    @NonNull
    private RecyclerView getRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(getContext());
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ABOVE, R.id.button_add_wholesale);
        layoutParams.addRule(TEXT_ALIGNMENT_CENTER, TRUE);
        layoutParams.addRule(ALIGN_PARENT_TOP, TRUE);
        recyclerView.setLayoutParams(layoutParams);
        adapter = new WholesaleAdapterImpl(this, mainPrice, currency);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return recyclerView;
    }

    @NonNull
    private Button getButton() {
        Button button = new Button(getContext());
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        layoutParams.addRule(TEXT_ALIGNMENT_CENTER, TRUE);
        button.setLayoutParams(layoutParams);
        button.setText("Tambah Wholesale");
        button.setId(R.id.button_add_wholesale);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addItem();
            }
        });
        return button;
    }

    @Override
    public void throwShomething(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }


    public List<WholesaleModel> getDatas() {
        return adapter.getDatas();
    }

    public void setDatas(List<WholesaleModel> models) {
        adapter.setData(models);
    }

    public void clearAll() {
        adapter.clearAll();
    }

    public void setPrice(double price) {
        mainPrice = price;
    }
}
