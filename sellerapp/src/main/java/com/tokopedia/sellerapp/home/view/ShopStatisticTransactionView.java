package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.sellerapp.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 8/29/16.
 */

public class ShopStatisticTransactionView extends FrameLayout implements BaseView<ShopTxStats> {

    @BindView(R.id.pie)
    SimplePieChart simplePieChart;

    @BindView(R.id.percentage_success)
    TextView percentageSuccess;

    @BindView(R.id.view_message_transaction)
    LinearLayout viewMessageTransaction;

    @BindView(R.id.total_transaction)
    TextView totalTransaction;

    Map<Integer, TypeTransaction> typeTransactions;

    boolean hasTransaction;
    TypeTransaction currentTypeTransaction;

    public ShopStatisticTransactionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.shop_statistic_transaction_view, this);
        ButterKnife.bind(this);
    }

    @Override
    public void init(ShopTxStats data) {
        typeTransactions = new HashMap<>();

        hasTransaction = data.shopTxHasTransaction == 1;

        //[START] parsing the data ~ somehow magically NOT GOOD
        TypeTransaction typeTransaction = new TypeTransaction(TypeTransaction.TYPE_ONE_MONTH);
        typeTransaction.hasTransaction = data.shopTxHasTransaction1Month;
        typeTransaction.showPercentage = data.shopTxShowPercentage1Month;
        typeTransaction.successPercentage = Float.valueOf(data.shopTxSuccessRate1Month);
        typeTransaction.totalTransaction = data.shopTxSuccess1MonthFmt;

        TypeTransaction typeTransaction2 = new TypeTransaction(TypeTransaction.TYPE_THREE_MONTH);
        typeTransaction2.hasTransaction = data.shopTxHasTransaction3Month;
        typeTransaction2.showPercentage = data.shopTxShowPercentage3Month;
        typeTransaction2.successPercentage = Float.valueOf(data.shopTxSuccessRate3Month);
        typeTransaction2.totalTransaction = data.shopTxSuccess3MonthFmt;

        TypeTransaction typeTransaction3 = new TypeTransaction(TypeTransaction.TYPE_TWELVE_MONTH);
        typeTransaction3.hasTransaction = data.shopTxHasTransaction1Year;
        typeTransaction3.showPercentage = data.shopTxShowPercentage1Year;
        typeTransaction3.successPercentage = Float.valueOf(data.shopTxSuccessRate1Year);
        typeTransaction3.totalTransaction = data.shopTxSuccess1YearFmt;
        //[END] parsing the data ~ somehow magically NOT GOOD

        // set current type transaction to 1 month
        currentTypeTransaction = typeTransaction;

        //[START] set the data based on current data
        String customPercentage = String.valueOf(currentTypeTransaction.successPercentage) + "%";
        String customTotalTx = "Dari " + currentTypeTransaction.totalTransaction + " Transaksi";

        simplePieChart.setPieFilled(currentTypeTransaction.successPercentage);
        percentageSuccess.setText(customPercentage);
        totalTransaction.setText(customTotalTx);
        //[END] set the data based on current data
    }

    public static class TypeTransaction {
        protected static final int TYPE_ONE_MONTH = 1;
        protected static final int TYPE_THREE_MONTH = 2;
        protected static final int TYPE_TWELVE_MONTH = 3;

        public int typeMonth;
        public int hasTransaction;
        public int showPercentage;
        public float successPercentage;
        public String totalTransaction;
        public String successCounter;

        public TypeTransaction(int type) {
            this.typeMonth = type;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("typeMonth : "+typeMonth);
            builder.append("\nhasTransaction : "+hasTransaction);
            builder.append("\nshowPercentage : "+showPercentage);
            builder.append("\nsuccessPercentage : "+successPercentage);
            builder.append("\ntotalTransaction : "+totalTransaction);
            builder.append("\nsuccessCounter : "+successCounter);
            return builder.toString();
        }
    }
}
