package com.tokopedia.posapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.productdetail.ProductInstallment;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.adapter.CreditSimulationAdapter;

import java.util.ArrayList;

/**
 * Created by okasurya on 8/11/17.
 */

public class InstallmentSimulationActivity extends TActivity {
    public static final String KEY_INSTALLMENT_DATA = "WHOLESALE_DATA";

    private TextView topBarTitle;
    private RecyclerView recyclerView;
    private CreditSimulationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_simulation);
        initView();
        setupData();
        setToolbar();
    }

    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        recyclerView = (RecyclerView) findViewById(R.id.re_credit_simulation);

        adapter = new CreditSimulationAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupData() {
        ArrayList<ProductInstallment> productInstallments =
                getIntent().getParcelableArrayListExtra(KEY_INSTALLMENT_DATA);
        if (productInstallments != null && productInstallments.size() != 0) {
            adapter.addData(productInstallments);
        }
    }

    private void setToolbar() {
        topBarTitle.setText(getString(R.string.simulation_page_title));
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }
}
