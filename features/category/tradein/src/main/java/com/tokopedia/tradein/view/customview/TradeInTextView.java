package com.tokopedia.tradein.view.customview;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.tokopedia.tradein.R;
import com.tokopedia.tradein.viewmodel.ITradeInParamReceiver;
import com.tokopedia.tradein.viewmodel.TradeInResponseObserver;
import com.tokopedia.tradein.viewmodel.TradeInTextViewModel;
import com.tokopedia.tradein.viewmodel.TradeInVMFactory;

import java.util.ArrayList;

public class TradeInTextView extends ConstraintLayout {
    public static final String ACTION_TRADEIN_ELLIGIBLE = "ACTION_TRADE_IN_ELLIGIBLE";
    public static final String EXTRA_ISELLIGIBLE = "EXTRA_ISELLIGIBLE";
    public TextView titleTextView;
    public TextView priceTextView;
    private TradeInTextViewModel viewModel;
    private TradeInTextView thisInstance;
    private ClickTrackListener trackListener;
    private View.OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (trackListener != null)
                trackListener.trackClick();
            viewModel.showAccessRequestDialog();
        }
    };

    public TradeInTextView(Context context) {
        super(context);
        initView();
    }

    public TradeInTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TradeInTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.trade_in_textview, this);
        if (!isInEditMode()) {
            viewModel = ViewModelProviders.of((FragmentActivity) getContext(),
                    TradeInVMFactory.getInstance(((FragmentActivity) getContext()).getApplication()))
                    .get(TradeInTextViewModel.class);
            viewModel.setActivity((FragmentActivity) getContext());
            viewModel.getResponseData().observe((FragmentActivity) getContext(),
                    new TradeInResponseObserver(this));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                this.setBackgroundResource(R.drawable.bg_rect_white_round);
                this.setElevation(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        getResources().getDimension(R.dimen.dp_1),
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
    }

    public ITradeInParamReceiver getTradeInReceiver() {
        if (viewModel != null)
            return viewModel;
        else
            throw new NullPointerException("ITradeInParamReceiver is not initialised");
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        if (listener == null) {
            listener = this.clickListener;
        }
        super.setOnClickListener(listener);
    }

    public void setTrackListener(ClickTrackListener listener) {
        this.trackListener = listener;
    }

    public interface ClickTrackListener {
        void trackClick();
    }
}
