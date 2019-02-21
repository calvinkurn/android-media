package view.customview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.tradein.R;

import viewmodel.ITradeInParamReceiver;
import viewmodel.TradeInResponseObserver;
import viewmodel.TradeInTextViewModel;
import viewmodel.TradeInVMFactory;

public class TradeInTextView extends ConstraintLayout {
    private TradeInTextViewModel viewModel;
    public TextView titleTextView;
    public TextView priceTextView;
    private TradeInTextView thisInstance;

    private View.OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            viewModel.startTradeIn();
        }
    };

    public TradeInTextView(Context context) {
        this(context, null);
    }

    public TradeInTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TradeInTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        viewModel = ViewModelProviders.of((FragmentActivity) getContext(), TradeInVMFactory.getInstance((FragmentActivity) getContext())).get(TradeInTextViewModel.class);
        viewModel.getResponseData().observe((FragmentActivity) getContext(), new TradeInResponseObserver(this));
        inflate(getContext(), R.layout.trade_in_textview, this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            this.setBackgroundResource(R.drawable.bg_rect_white);
            this.setElevation(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    getResources().getDimension(R.dimen.dp_4),
                    getResources().getDisplayMetrics()
            ));
        } else
            this.setBackgroundResource(R.drawable.bg_rect_white_shadow);
        thisInstance = this;
        this.setOnClickListener(clickListener);
        this.setAlpha(0);
        titleTextView = this.findViewById(R.id.tv_tambah_title);
        priceTextView = this.findViewById(R.id.tv_text_price);
        titleTextView.setAlpha(0);
        priceTextView.setAlpha(0);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                getViewTreeObserver().removeOnPreDrawListener(this);

                ViewCompat.animate(thisInstance).alpha(1).setDuration(500);
                ViewCompat.animate(titleTextView).alpha(1).setStartDelay(120).setDuration(500);
                ViewCompat.animate(priceTextView).alpha(1).setStartDelay(240).setDuration(500);

                return false;
            }
        });
    }

    public ITradeInParamReceiver getTradeInReceiver() {
        if (viewModel != null)
            return viewModel;
        else
            throw new NullPointerException("ITradeInParamReceiver is not initialised");
    }
}
