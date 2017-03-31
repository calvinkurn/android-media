package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.ShopStatisticDetail;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.sellerapp.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by normansyahputa on 10/20/16.
 */

public class TransactionView3 extends FrameLayout implements BaseView<ShopModel> {

    @BindView(R.id.from_transaction)
    TextView fromTransaction;

    ArcView transactionArcView;

    @BindView(R.id.arc_view_container)
    LinearLayout arcViewContainer;
    private ShopModel shopModel;
    private HashMap<Integer, ShopStatisticTransactionView.TypeTransaction> typeTransactions;
    private boolean hasTransaction;
    private ShopStatisticTransactionView.TypeTransaction currentTypeTransaction;

    public TransactionView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.container_transaction, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.transaction_view_container)
    public void seeDetail(){
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

        transactionArcView = new ArcView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        arcViewContainer.addView(transactionArcView);
//        transactionArcView.setModels(ArcView.getSameDefaultModels((int) currentTypeTransaction.successPercentage));
//        currentTypeTransaction.successPercentage = 0;
        if(currentTypeTransaction.successPercentage<=0){
            transactionArcView.mIsAnimated = false;
        }
        transactionArcView.setSingleValue(currentTypeTransaction.successPercentage);
        transactionArcView.animateProgress();

        String customPercentage = String.valueOf((int)currentTypeTransaction.successPercentage) + "%";
        String customTotalTx = "Dari " + currentTypeTransaction.totalTransaction + " Transaksi";
        fromTransaction.setText(customTotalTx);
    }
}
