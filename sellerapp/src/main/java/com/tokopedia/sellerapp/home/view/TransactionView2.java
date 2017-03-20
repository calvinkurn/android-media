package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.ShopStatisticDetail;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.sellerapp.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 9/2/16.
 */

public class TransactionView2 extends FrameLayout implements BaseView<ShopModel>{
    @BindView(R.id.reputation_progress_bar)
    NumberProgressBar numberProgressBar;

    @BindView(R.id.reputation_percentage)
    TextView reputationPercentage;

    @BindView(R.id.from_transaction)
    TextView fromTransaction;

    private HashMap<Integer, ShopStatisticTransactionView.TypeTransaction> typeTransactions;
    private boolean hasTransaction;
    private ShopStatisticTransactionView.TypeTransaction currentTypeTransaction;
    private ShopModel shopModel;

    public TransactionView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.reputation_item_layout_new, this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                seeDetail();
            }
        });
        ButterKnife.bind(this);
    }
//    @OnClick(R.id.seller_home_transaction_view)
    public void seeDetail(){
//        Intent intent = new Intent(getContext(), ActivitySellingTransaction.class);
//        intent.putExtra("tab", 4);
//        getContext().startActivity(intent);
        if(shopModel != null) {
            String shopInfo = CacheUtil.convertModelToString(shopModel,
                    new TypeToken<ShopModel>() {
                    }.getType());
            Intent intent = new Intent(getContext(), ShopStatisticDetail.class);
            intent.putExtra(ShopStatisticDetail.EXTRA_SHOP_INFO, shopInfo);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void init(ShopModel shopModel) {
        this.shopModel = shopModel;
        ShopTxStats shopTxStats = shopModel.shopTxStats;
        parseData(shopTxStats);
    }

    private void parseData(ShopTxStats shopTxStats){
        typeTransactions = new HashMap<>();

        hasTransaction = shopTxStats.shopTxHasTransaction == 1;

        //[START] parsing the data ~ somehow magically NOT GOOD
        ShopStatisticTransactionView.TypeTransaction typeTransaction = new ShopStatisticTransactionView.TypeTransaction(ShopStatisticTransactionView.TypeTransaction.TYPE_ONE_MONTH);
        typeTransaction.hasTransaction = shopTxStats.shopTxHasTransaction1Month;
        typeTransaction.showPercentage = shopTxStats.shopTxShowPercentage1Month;
        typeTransaction.successPercentage = Float.valueOf(shopTxStats.shopTxSuccessRate1Month);
        typeTransaction.totalTransaction = shopTxStats.shopTxSuccess1MonthFmt;
        typeTransactions.put(1, typeTransaction);

        ShopStatisticTransactionView.TypeTransaction typeTransaction2 = new ShopStatisticTransactionView.TypeTransaction(ShopStatisticTransactionView.TypeTransaction.TYPE_THREE_MONTH);
        typeTransaction2.hasTransaction = shopTxStats.shopTxHasTransaction3Month;
        typeTransaction2.showPercentage = shopTxStats.shopTxShowPercentage3Month;
        typeTransaction2.successPercentage = Float.valueOf(shopTxStats.shopTxSuccessRate3Month);
        typeTransaction2.totalTransaction = shopTxStats.shopTxSuccess3MonthFmt;
        typeTransactions.put(2, typeTransaction2);

        ShopStatisticTransactionView.TypeTransaction typeTransaction3 = new ShopStatisticTransactionView.TypeTransaction(ShopStatisticTransactionView.TypeTransaction.TYPE_TWELVE_MONTH);
        typeTransaction3.hasTransaction = shopTxStats.shopTxHasTransaction1Year;
        typeTransaction3.showPercentage = shopTxStats.shopTxShowPercentage1Year;
        typeTransaction3.successPercentage = Float.valueOf(shopTxStats.shopTxSuccessRate1Year);
        typeTransaction3.totalTransaction = shopTxStats.shopTxSuccess1YearFmt;
        typeTransactions.put(3, typeTransaction3);
        //[END] parsing the data ~ somehow magically NOT GOOD

        // set current type transaction to 1 month
        currentTypeTransaction = typeTransaction;

        String customPercentage = String.valueOf((int)currentTypeTransaction.successPercentage) + "%";
        String customTotalTx = "Dari " + currentTypeTransaction.totalTransaction + " Transaksi";
        reputationPercentage.setText(customPercentage);
        fromTransaction.setText(customTotalTx);


        numberProgressBar.setMax(100);
        numberProgressBar.setProgress((int)currentTypeTransaction.successPercentage);
    }
}
