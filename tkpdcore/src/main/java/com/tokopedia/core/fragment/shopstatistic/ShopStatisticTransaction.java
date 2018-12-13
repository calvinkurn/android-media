package com.tokopedia.core.fragment.shopstatistic;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.customView.SimplePieChart;

@SuppressWarnings("unused")
public class ShopStatisticTransaction {

    private static final int HAS_TRANSACTION = 1;

    private static final int NO_TRANSACTION_AT_ALL = 1;
    private static final int NO_TRANSACTION_IN_ONE_TAB = 2;
    private static final int HAVE_MORE_TRANSACTIONS = 3;
    private static final int HAVE_LESS_TRANSACTION = 4;
    private static final String TAG = ShopStatisticTransaction.class.getSimpleName();

    public static class Model {
        public int hasTransaction;
        public TypeTransaction oneMonth;
        public TypeTransaction threeMonth;
        public TypeTransaction twelveMonth;

        public Model() {
            oneMonth = new TypeTransaction(1);
            threeMonth = new TypeTransaction(2);
            twelveMonth = new TypeTransaction(3);
        }
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

    private class ViewHolder {
        View oneMonth;
        View threeMonth;
        View twelveMonth;
        View tabMonth;
        View viewPie;
        View viewDescTransaction;
        View viewNoTransaction;
        TextView counterSuccess;
        TextView percentageSuccess;
        TextView totalTransaction;
        TextView noTransactionMessage;
        SimplePieChart pie;
    }

    private View view;
    private Model model;
    private ViewHolder holder;
    private Context context;

    public ShopStatisticTransaction(Context context, View view){
        this.view = view;
        holder = new ViewHolder();
        initView();
    }

    public void setModel(Model model){
        this.model = model;
        createView(model.oneMonth);
        setValue(model.oneMonth);
        setListener();
    }

    private void initView(){
        holder.oneMonth = findViewById(R.id.tab_1);
        holder.threeMonth = findViewById(R.id.tab_3);
        holder.twelveMonth = findViewById(R.id.tab_12);
        holder.tabMonth = findViewById(R.id.tab_view);
        holder.viewPie = findViewById(R.id.view_pie);
        holder.viewDescTransaction = findViewById(R.id.view_message_transaction);
        holder.viewNoTransaction = findViewById(R.id.view_no_transaction);
        holder.counterSuccess = (TextView) findViewById(R.id.counter_success);
        holder.percentageSuccess = (TextView) findViewById(R.id.percentage_success);
        holder.totalTransaction = (TextView) findViewById(R.id.total_transaction);
        holder.noTransactionMessage = (TextView) findViewById(R.id.no_transaction_message);
        holder.pie = (SimplePieChart) findViewById(R.id.pie);
    }

    private View findViewById(int id){
        return view.findViewById(id);
    }

    private void setListener() {
        holder.oneMonth.setOnClickListener(OnClickTab(0));
        holder.threeMonth.setOnClickListener(OnClickTab(1));
        holder.twelveMonth.setOnClickListener(OnClickTab(2));
    }

    private View.OnClickListener OnClickTab(final int value) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (value) {
                    default:
                        holder.oneMonth.setBackgroundResource(R.color.greyish_bg);
                        holder.threeMonth.setBackgroundResource(0);
                        holder.twelveMonth.setBackgroundResource(0);
                        createView(model.oneMonth);
                        setValue(model.oneMonth);
                        break;
                    case 1:
                        holder.oneMonth.setBackgroundResource(0);
                        holder.threeMonth.setBackgroundResource(R.color.greyish_bg);
                        holder.twelveMonth.setBackgroundResource(0);
                        createView(model.threeMonth);
                        setValue(model.threeMonth);
                        break;
                    case 2:
                        holder.oneMonth.setBackgroundResource(0);
                        holder.threeMonth.setBackgroundResource(0);
                        holder.twelveMonth.setBackgroundResource(R.color.greyish_bg);
                        createView(model.twelveMonth);
                        setValue(model.twelveMonth);
                        break;
                }
            }
        };
    }

    private String getMessage(int typeMonth) {
        switch (typeMonth) {
            default: return "Belum ada Transaksi";
            case TypeTransaction.TYPE_ONE_MONTH: return "Belum ada Transaksi\ndalam 1 bulan terakhir";
            case TypeTransaction.TYPE_THREE_MONTH: return "Belum ada Transaksi\ndalam 3 bulan terakhir";
            case TypeTransaction.TYPE_TWELVE_MONTH: return "Belum ada Transaksi\ndalam 1 tahun terakhir";
        }
    }

    private boolean isShowPercent(TypeTransaction type) {
        return type.showPercentage == 1;
    }

    private void view_1() {
        // no transaction
        holder.tabMonth.setVisibility(View.GONE);
        holder.viewPie.setVisibility(View.GONE);
        holder.counterSuccess.setVisibility(View.GONE);
        holder.viewDescTransaction.setVisibility(View.GONE);
        holder.viewNoTransaction.setVisibility(View.VISIBLE);
    }

    private void view_2() {
        // has transaction tapi tidak ada transaksi dalam tab bulan tersebut
        holder.tabMonth.setVisibility(View.VISIBLE);
        holder.viewPie.setVisibility(View.GONE);
        holder.counterSuccess.setVisibility(View.GONE);
        holder.viewDescTransaction.setVisibility(View.GONE);
        holder.viewNoTransaction.setVisibility(View.VISIBLE);
    }

    private void view_3() {
        // has transaction using pie
        holder.tabMonth.setVisibility(View.VISIBLE);
        holder.viewPie.setVisibility(View.VISIBLE);
        holder.counterSuccess.setVisibility(View.GONE);
        holder.viewDescTransaction.setVisibility(View.VISIBLE);
        holder.viewNoTransaction.setVisibility(View.GONE);
    }

    private void view_4() {
        // has transaction using counter
        holder.tabMonth.setVisibility(View.VISIBLE);
        holder.viewPie.setVisibility(View.VISIBLE);
        holder.counterSuccess.setVisibility(View.GONE);
        holder.viewDescTransaction.setVisibility(View.VISIBLE);
        holder.viewNoTransaction.setVisibility(View.GONE);
    }
    
    private void view_5() {
        holder.tabMonth.setVisibility(View.GONE);
        holder.viewPie.setVisibility(View.GONE);
        holder.counterSuccess.setVisibility(View.GONE);
        holder.viewDescTransaction.setVisibility(View.GONE);
        holder.viewNoTransaction.setVisibility(View.GONE);
    }

    private int getTypeView(TypeTransaction type) {
        if (model.hasTransaction != HAS_TRANSACTION) {
            return NO_TRANSACTION_AT_ALL;
        } else {
            Log.d(TAG, "type "+type.toString());
            if (type.hasTransaction != HAS_TRANSACTION) {
                return NO_TRANSACTION_IN_ONE_TAB;
            } else {
                if (isShowPercent(type)) {
                    return HAVE_MORE_TRANSACTIONS;
                } else {
                    return HAVE_LESS_TRANSACTION;
                }
            }
        }
    }

    private void createView (TypeTransaction type) {
        Log.d(TAG, "createView type "+type.toString());
        switch (getTypeView(type)) {
            case NO_TRANSACTION_AT_ALL:
                view_1();
                break;
            case NO_TRANSACTION_IN_ONE_TAB:
                view_2();
                break;
            case HAVE_MORE_TRANSACTIONS:
                view_3();
                break;
            case HAVE_LESS_TRANSACTION:
                view_4();
                break;
            default:
                view_5();
        }
    }
    
    private void setValue (TypeTransaction type) {
        Log.d(TAG, "setValue type "+getTypeView(type));
        switch (getTypeView(type)) {
            case NO_TRANSACTION_AT_ALL:
                value_1(type);
                break;
            case NO_TRANSACTION_IN_ONE_TAB:
                value_2(type);
                break;
            case HAVE_MORE_TRANSACTIONS:
                value_3(type);
                break;
            case HAVE_LESS_TRANSACTION:
                value_4(type);
                break;
            default: 
                break;
        }
    }

    private void value_1(TypeTransaction type) {
        holder.noTransactionMessage.setText(getMessage(0));
    }

    private void value_2(TypeTransaction type) {
        holder.noTransactionMessage.setText(getMessage(type.typeMonth));
    }

    private void value_3(TypeTransaction type) {
        String customPercentage = String.valueOf(type.successPercentage) + "%";
        String customTotalTx = "Dari " + type.totalTransaction + " Transaksi";

        holder.pie.setPieFilled(type.successPercentage);
        holder.percentageSuccess.setText(customPercentage);
        holder.totalTransaction.setText(customTotalTx);
    }

    private void value_4(TypeTransaction type) {
        holder.percentageSuccess.setText(type.successPercentage+"%");
        holder.totalTransaction.setText("Dari "+type.totalTransaction+" Transaksi");
        holder.pie.setPieFilled(type.successPercentage);
    }

}
